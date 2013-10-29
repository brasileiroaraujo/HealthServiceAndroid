package br.ufcg.embedded.health.servicetest;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.ufcg.embedded.health.R;
import br.ufcg.embedded.health.structures.HealthData;

public class HealthDataAdapter extends BaseAdapter {
    private List<HealthData> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    public HealthDataAdapter(Context context, List<HealthData> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int index) {
        return mData.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int posicao, View view, ViewGroup viewGroup) {
        view = mInflater.inflate(R.layout.history_adapter_item, null);
        HealthData data = mData.get(posicao);

        TextView tvDate = (TextView) view.findViewById(R.id.historyDate);
        tvDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data
                .getDate()));

        TextView tvDevice = (TextView) view.findViewById(R.id.historyDevice);
        tvDevice.setText(data.getDevice());

        TextView tvData = (TextView) view.findViewById(R.id.historyData);
        tvData.setText(mContext.getResources().getString(R.string.pressure_sys)
                + " " + String.valueOf(data.getSystolic().intValue()) + "\n"
                + mContext.getResources().getString(R.string.pressure_dis)
                + " " + String.valueOf(data.getDiastolic().intValue()));

        TextView tvAnalyzePressure = (TextView) view
                .findViewById(R.id.historyAnalyzePressure);
        tvAnalyzePressure.setText(Handlers.analyzePressure(data.getSystolic()
                .intValue(), data.getDiastolic().intValue()));
        return view;
    }
}
