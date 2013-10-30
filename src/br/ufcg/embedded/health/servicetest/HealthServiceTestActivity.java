package br.ufcg.embedded.health.servicetest;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import br.ufcg.embedded.health.R;
import br.ufcg.embedded.health.database.HealthDAO;
import br.ufcg.embedded.health.structures.HealthData;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.signove.health.service.HealthAgentAPI;
import com.signove.health.service.HealthServiceAPI;

public class HealthServiceTestActivity extends Activity {
    private static final String APP_PREF_FILE = "appPreferences";
    private static final String NOTIFICATION_ACTIVE = "notificationActive";

    int[] specs = { 0x1004 };
    HealthServiceAPI api;
    private HealthServiceTestActivity frame = this;

    private TextView sugestion;
    private TextView menssage;
    private TextView device;
    private TextView data;
    private TextView history;
    private CheckBox cbNotification;
    private TimePicker timePicker;
    private Button btnOk;
    private Button btnShowGraphic;
    private Button btnClearHistory;
    private AlarmManager alarmManager;
    private Intent myIntent;
    private List<HealthData> datasHistory;
    private ListView list;
    private HealthAgentAPI.Stub agent;

    private Map<String, String> map;
    PopupWindow popUp;
    boolean click = true;
    LayoutParams params;
    LinearLayout layoutMain;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.w("HST", "Service connection established");

            // that's how we get the client side of the IPC connection
            api = HealthServiceAPI.Stub.asInterface(service);
            agent = new AgentHealth(map, api, frame).getAgent();
            try {
                Log.w("HST", "Configuring...");
                api.ConfigurePassive(agent, specs);
            } catch (RemoteException e) {
                Log.e("HST", "Failed to add listener", e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.w("HST", "Service connection closed");
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        myIntent = new Intent(this, NotificationReceiver.class);

        SharedPreferences sharedPreferences = getSharedPreferences(
                APP_PREF_FILE, MODE_PRIVATE);
        boolean notification = sharedPreferences.getBoolean(
                NOTIFICATION_ACTIVE, true);

        OnLongClickListener l = new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finish();
                return true;
            }
        };

        OnClickListener o = new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                case R.id.checkBoxNotifications:
                    if (cbNotification.isChecked()) {
                        timePicker.setEnabled(true);
                    } else {
                        timePicker.setEnabled(false);
                    }
                    break;
                case R.id.btnOk:
                    if (cbNotification.isChecked()) {
                        setAlarm();
                    } else {
                        alarmManager.cancel(PendingIntent.getBroadcast(
                                getApplicationContext(), 0, myIntent, 0));
                    }
                    setNotificationActive(cbNotification.isChecked());
                    Toast.makeText(
                            getApplicationContext(),
                            getResources().getString(
                                    R.string.notifications_preferences_success),
                            Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnShowGraphic:
                    if (click) {
                        popUp.showAtLocation(layoutMain, Gravity.START, 10, 10);
                        popUp.setFocusable(true);
                        popUp.update(100, 100, 600, 500);
                        btnShowGraphic.setText("Close Graphic");
                        click = false;
                    } else {
                        popUp.dismiss();
                        btnShowGraphic.setText(getResources().getString(
                                R.string.btn_graphic));
                        click = true;
                    }
                    break;
                case R.id.btnClearHistory:
                    HealthDAO mDAO = HealthDAO.getInstance(frame);
                    mDAO.deleteAll();
                    updateListHistory();
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.history_clean),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
                }

            }
        };

        TabHost tabs = (TabHost) findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        spec.setContent(R.id.data);
        spec.setIndicator(getResources().getString(R.string.tab_data));
        tabs.addTab(spec);
        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.history);
        spec.setIndicator(getResources().getString(R.string.tab_history));
        tabs.addTab(spec);
        spec = tabs.newTabSpec("tag3");
        spec.setContent(R.id.settings);
        spec.setIndicator(getResources().getString(R.string.tab_settings));
        tabs.addTab(spec);
        tabs.setCurrentTab(0);

        list = (ListView) findViewById(R.id.listViewDataHistory);
        sugestion = (TextView) findViewById(R.id.tvSugestion);
        menssage = (TextView) findViewById(R.id.tvMsg);
        device = (TextView) findViewById(R.id.tVDevice);
        data = (TextView) findViewById(R.id.tVMeasurement);
        history = (TextView) findViewById(R.id.tvHistoryEmpty);
        timePicker = (TimePicker) findViewById(R.id.timePicker1);
        cbNotification = (CheckBox) findViewById(R.id.checkBoxNotifications);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnShowGraphic = (Button) findViewById(R.id.btnShowGraphic);
        btnClearHistory = (Button) findViewById(R.id.btnClearHistory);

        sugestion.setOnLongClickListener(l);
        menssage.setOnLongClickListener(l);
        device.setOnLongClickListener(l);
        data.setOnLongClickListener(l);
        cbNotification.setOnClickListener(o);
        btnOk.setOnClickListener(o);
        btnShowGraphic.setOnClickListener(o);
        btnClearHistory.setOnClickListener(o);

        cbNotification.setChecked(notification);

        map = new HashMap<String, String>();
        popUp = new PopupWindow(this);
        layoutMain = new LinearLayout(this);
        layoutMain.setOrientation(LinearLayout.VERTICAL);
       initGraphic();
        popUp.setContentView(layoutMain);

//        AlertDialog.Builder builder = new AlertDialog.Builder(
//                getApplicationContext());
//        builder.setCancelable(true);
//        builder.setTitle("Title");
//        //builder.setMessage( initGraphic());
//       LinearLayout layout3 = (LinearLayout) findViewById(R.id.data);
//       // builder.setView(R.id.data);
//        builder.setInverseBackgroundForced(true);
//        builder.setPositiveButton("Close",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog,
//                            int which) {
//                        dialog.dismiss();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
        
        updateListHistory();

        Intent intent = new Intent("com.signove.health.service.HealthService");
        startService(intent);
        bindService(intent, serviceConnection, 0);
        Log.w("HST", "Activity created");

        sugestion.setText("--");
        menssage.setText("--");
        device.setText("--");
        data.setText("--");
    }

    private void initGraphic() {
        List<HealthData> datasHistoryInternal = HealthDAO.getInstance(this).ListAll();
        
        GraphViewData[] datasGraph = new GraphViewData[datasHistoryInternal.size()];
        for (int i = 0; i < datasHistoryInternal.size(); i++) {
            datasGraph[i] = new GraphViewData(i+1, datasHistoryInternal.get(i).getSystolic());
        }
        
        GraphViewSeries graph = new GraphViewSeries(datasGraph
               // new GraphViewData[] { 
                        
                        
                        
                        
                        
                      //  new GraphViewData(1, 2.0d),
                       // new GraphViewData(2, 1.5d), new GraphViewData(3, 2.5d),
                //        new GraphViewData(4, 1.0d) }
                );

        GraphView graphView = new LineGraphView(this, "Blood Pressure Graph");
        graphView.addSeries(graph); // data
        layoutMain.addView(graphView, 600, 450);
        
        
   }

    public void updateListHistory() {
        datasHistory = HealthDAO.getInstance(this).ListAll();
        if (datasHistory.size() == 0) {
            history.setText(getResources().getString(R.string.history_empty));
        } else {
            history.setText("");
        }
        HealthDataAdapter adapter = new HealthDataAdapter(
                getApplicationContext(), datasHistory);
        list.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            Log.w("HST", "Unconfiguring...");
            api.Unconfigure(agent);
        } catch (Throwable t) {
            Log.w("HST", "Erro tentando desconectar");
        }
        Log.w("HST", "Activity destroyed");
        unbindService(serviceConnection);

    }

    private void setNotificationActive(boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                APP_PREF_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NOTIFICATION_ACTIVE, value);
        editor.commit();
    }

    private void setAlarm() {

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cur_cal = new GregorianCalendar(); // TIRAR

        GregorianCalendar data = calculateNotificationDate();

        Log.e("MAX DIA",
                String.valueOf(data.getActualMaximum(Calendar.DAY_OF_MONTH)));
        Log.e("MES", String.valueOf(data.get(Calendar.MONTH)));
        Log.e("DIA", String.valueOf(data.get(Calendar.DAY_OF_MONTH)));
        Log.e("ANO", String.valueOf(data.get(Calendar.YEAR)));
        // Log.e("DIA", String.valueOf(data.get(Calendar.DAY_OF_MONTH)));
        // Log.e("HORA",String.valueOf(data.get(Calendar.HOUR_OF_DAY)));
        // Log.e("MIN",String.valueOf(data.get(Calendar.MINUTE)));

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                data.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);
    }

    private GregorianCalendar calculateNotificationDate() {
        Calendar actualCal = new GregorianCalendar();
        actualCal.setTimeInMillis(System.currentTimeMillis());

        int dayOfMonth = actualCal.get(Calendar.DAY_OF_MONTH);
        int month = actualCal.get(Calendar.MONTH);
        int year = actualCal.get(Calendar.YEAR);

        if ((actualCal.get(Calendar.HOUR_OF_DAY) > timePicker.getCurrentHour())
                || (actualCal.get(Calendar.HOUR_OF_DAY) == timePicker
                        .getCurrentHour())
                && (actualCal.get(Calendar.MINUTE) > timePicker
                        .getCurrentMinute())) {
            if (actualCal.get(Calendar.DAY_OF_MONTH) == actualCal
                    .getActualMaximum(Calendar.DAY_OF_MONTH)) {
                if (actualCal.get(Calendar.MONTH) == 11) {
                    year += 1;
                    month = 0;
                    dayOfMonth = 1;
                } else {
                    month += 1;
                    dayOfMonth = 1;
                }
            } else {
                dayOfMonth += 1;
            }
        }

        return new GregorianCalendar(year, month, dayOfMonth,
                timePicker.getCurrentHour(), timePicker.getCurrentMinute());
    }
}
