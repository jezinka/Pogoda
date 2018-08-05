package com.projects.jezinka.pogoda;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class SensorsAdapter extends RecyclerView.Adapter<SensorsAdapter.ViewHolder> {
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static DecimalFormat df = new DecimalFormat("####0.00");


    private Context mContext;
    private Sensor[] mSensor;

    public SensorsAdapter(Context c) {
        mContext = c;
        mSensor = new Sensor[0];
    }

    public Context getContext() {
        return this.mContext;
    }

    public int getItemCount() {
        return mSensor.length;
    }

    private Sensor getItem(int position) {
        return this.mSensor[position];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View weatherView = layoutInflater.inflate(R.layout.recycler_view_item, parent, false);

        return new ViewHolder(weatherView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Sensor sensor = mSensor[position];

        TextView label = holder.label;
        label.setText(sensor.label);

        TextView temperature = holder.temperature;
        temperature.setText(TextUtils.concat("Temp. ", String.valueOf(sensor.temperature), "\u00B0C"));

        TextView timestamp = holder.timestamp;
        timestamp.setText(formatter.format(sensor.timestamp));

        TextView humidity = holder.humidity;
        humidity.setText(TextUtils.concat(String.valueOf(sensor.humidity), "%"));

        TextView lux = holder.lux;
        lux.setText(TextUtils.concat(df.format(sensor.lux), " lx"));

        if (sensor.barPressing != null) {
            TextView barPress = holder.barPress;
            barPress.setText(TextUtils.concat(df.format(sensor.barPressing), " hPa"));
        }

        TextView battery = holder.battery;
        battery.setText(TextUtils.concat(df.format(sensor.vbat), " V", "/", df.format(sensor.vreg), " V"));
    }

    public long getItemId(int position) {
        return this.getItem(position).getId();
    }

    public void updateResults(Sensor[] results) {
        this.mSensor = results;
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView label;
        TextView temperature;
        TextView timestamp;
        TextView humidity;
        TextView lux;
        TextView barPress;
        TextView battery;

        ViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label_tv);
            temperature = itemView.findViewById(R.id.temperature_tv);
            timestamp = itemView.findViewById(R.id.timestamp_tv);
            humidity = itemView.findViewById(R.id.humidity_tv);
            lux = itemView.findViewById(R.id.lux_tv);
            barPress = itemView.findViewById(R.id.bar_press_tv);
            battery = itemView.findViewById(R.id.battery_tv);
        }
    }
}
