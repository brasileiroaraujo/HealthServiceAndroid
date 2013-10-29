package br.ufcg.embedded.health.servicetest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import br.ufcg.embedded.health.R;
import br.ufcg.embedded.health.database.HealthDAO;
import br.ufcg.embedded.health.structures.HealthData;

public class Handlers {
    private static final int TIMEOUT_PERSISTENCE = 10;
    private ProgressBar progressBar;
    private TextView menssage;
    private TextView device;
    private TextView data;
    private TextView sugestion;
    private Map<String, String> map;
    private Handler tm;
    private ParserXML parser;
    private static Activity frame;
    private List<Double> datas = new ArrayList<Double>();

    public Handlers(Map<String, String> map, Activity frame) {
        this.menssage = (TextView) frame.findViewById(R.id.tvMsg);
        this.data = (TextView) frame.findViewById(R.id.tVMeasurement);
        this.device = (TextView) frame.findViewById(R.id.tVDevice);
        this.sugestion = (TextView) frame.findViewById(R.id.tvSugestion);
        this.map = map;
        this.tm = new Handler();
        this.parser = new ParserXML();
        this.frame = frame;
        this.progressBar=(ProgressBar) frame.findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void handle_packet_connected(String path, String dev) {
        map.put(path, dev);
        show_dev(path);
        show(menssage, frame.getResources()
                .getString(R.string.status_connected));
        showStatusImage(frame.getResources().getString(
                R.string.status_connected));
        progressBar.setVisibility(View.VISIBLE);
    }

    public void handle_packet_disconnected(String path) {
        show(menssage,
                frame.getResources().getString(R.string.status_disconnected));
        show_dev(path);
        showStatusImage(frame.getResources().getString(
                R.string.status_disconnected));
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void handle_packet_associated(String path, String xml) {
        show(menssage,
                frame.getResources().getString(R.string.status_associated));
        show_dev(path);
        showStatusImage(frame.getResources().getString(
                R.string.status_associated));
        progressBar.setVisibility(View.VISIBLE);
    }

    public void handle_packet_disassociated(String path) {
        show(menssage,
                frame.getResources().getString(R.string.status_disassociated));
        show_dev(path);
        showStatusImage(frame.getResources().getString(
                R.string.status_disassociated));
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void handle_packet_description(String path, String xml) {
        show(menssage,
                frame.getResources().getString(R.string.status_mds_received));
        show_dev(path);
        showStatusImage(frame.getResources().getString(
                R.string.status_mds_received));
    }

    public void handle_packet_measurement(String path, String xml) {
        Document document = parser.parse_xml(xml);

        if (document == null) {
            return;
        }

        datas = parser.extractValues(document);
        if(datas.isEmpty()){
            Toast.makeText(frame.getApplicationContext(), frame.getResources().getString(R.string.invalid_data_received),
                    Toast.LENGTH_SHORT).show();
        }else{
            show(data, frame.getResources().getString(R.string.pressure_sys)
                    + datas.get(0).intValue() + "\n"
                    + frame.getResources().getString(R.string.pressure_dis)
                    + datas.get(1).intValue());
            show(menssage,
                    frame.getResources().getString(R.string.status_measurement));
            show(sugestion,
                    analyzePressure(datas.get(0).intValue(), datas.get(1)
                            .intValue()));
            show_dev(path);
            persistInDatabase(datas, path);
            updateHistoryList();
        }
    }

    /**
     * Method to analyse blood pressure. (National Institutes of Health)
     * http://www.nhlbi.nih.gov/health/health-topics/topics/hyp/
     * http://www.nhlbi.nih.gov/health/health-topics/topics/hbp/
     * 
     * @param sys
     * @param dis
     * @return
     */
    public static String analyzePressure(int sys, int dis) {
        if ((sys < 90) || (dis < 60)) {
            return frame.getResources().getString(R.string.pressure_low_blood);
        } else if ((sys > 120 && sys <= 139) || (dis > 80 && dis <= 89)) {
            return frame.getResources().getString(
                    R.string.pressure_prehypertension);
        } else if ((sys >= 140 && sys <= 159) || (dis >= 90 && dis <= 99)) {
            return frame.getResources().getString(
                    R.string.pressure_high_blood_stage1);
        } else if ((sys >= 160) || (dis >= 100)) {
            return frame.getResources().getString(
                    R.string.pressure_high_blood_stage2);
        } else {
            return frame.getResources().getString(R.string.pressure_normal);
        }
    }

    private void persistInDatabase(List<Double> datas, String path) {
        if (map.containsKey(path) && frame != null) {
            HealthDAO healthDao = HealthDAO.getInstance(frame);
            Log.w("AntidoteDatabase",
                    "Insert " + map.get(path) + " - " + datas.size() + " ("
                            + new Date() + ")");
            HealthData healthObject = new HealthData(map.get(path),
                    datas.get(0), datas.get(1), datas.get(2), new Date());
            if (validateInterval(healthObject, healthDao.lastInsert())) {
                healthDao.save(healthObject);
            }

        } else {
            Log.w("AntidoteDatabase", "Cannot save this health data.");
        }

    }

    private boolean validateInterval(HealthData insert, HealthData lastInsert) {
        if (insert != null && lastInsert != null) {
            long interval = (insert.getDate().getTime() - lastInsert.getDate()
                    .getTime()) / 1000;
            if (interval > TIMEOUT_PERSISTENCE
                    || insert.getSystolic() != lastInsert.getSystolic()
                    || insert.getDiastolic() != lastInsert.getDiastolic()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void show(TextView field, String menssage) {
        final TextView text = field;
        final String alert = menssage;
        tm.post(new Runnable() {
            @Override
            public void run() {
                text.setText(alert);
            }
        });
    }

    public void updateHistoryList() {
        final ListView list = (ListView) frame
                .findViewById(R.id.listViewDataHistory);
        final TextView history = (TextView) frame
                .findViewById(R.id.tvHistoryEmpty);
        final List<HealthData> datasHistory = HealthDAO.getInstance(frame)
                .ListAll();
        tm.post(new Runnable() {
            @Override
            public void run() {
                if (datasHistory.size() == 0) {
                    history.setText(frame.getResources().getString(
                            R.string.history_empty));
                } else {
                    history.setText("");
                    ;
                }
                HealthDataAdapter adapter = new HealthDataAdapter(frame
                        .getApplicationContext(), datasHistory);
                list.setAdapter(adapter);
            }
        });
    }

    public void show_dev(String path) {
        if (map.containsKey(path)) {
            show(device,
                    frame.getResources().getString(R.string.device)
                            + map.get(path));
        } else {
            show(device, frame.getResources()
                    .getString(R.string.device_unknown));
        }
    }

    public Handler getTm() {
        return tm;
    }

    public void showStatusImage(final String menssage) {
        final ImageView img = (ImageView) frame
                .findViewById(R.id.imageViewStatus);
        tm.post(new Runnable() {
            @Override
            public void run() {
                if (menssage.contains(frame.getResources().getString(
                        R.string.status_disconnected))
                        || menssage.contains(frame.getResources().getString(
                                R.string.status_disassociated))) {
                    img.setImageResource(R.drawable.status_red);
                } else {
                    img.setImageResource(R.drawable.status_green);
                }
            }
        });
    }
}
