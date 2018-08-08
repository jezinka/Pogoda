package com.projects.jezinka.pogoda;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        holder.label.setText(sensor.label);
        holder.temperature.setText(TextUtils.concat(String.valueOf(sensor.temperature), "\u00B0C"));
        holder.timestamp.setText(formatter.format(sensor.timestamp));
        holder.humidity.setText(TextUtils.concat(String.valueOf(sensor.humidity), "%"));
        holder.lux.setText(TextUtils.concat(df.format(sensor.lux), " lx"));

        if (sensor.barPressing != null) {
            holder.barPress.setText(TextUtils.concat(df.format(sensor.barPressing), mContext.getString(R.string.pressure)));
            holder.barPress.setVisibility(View.VISIBLE);
            holder.pressureIcon.setVisibility(View.VISIBLE);
        } else {
            holder.barPress.setVisibility(View.INVISIBLE);
            holder.pressureIcon.setVisibility(View.INVISIBLE);
        }

        holder.battery.setText(TextUtils.concat(df.format(sensor.vbat), mContext.getString(R.string.volt), "/", df.format(sensor.vreg), mContext.getString(R.string.volt)));
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
        ImageView pressureIcon;
        TextView battery;

        ViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label_tv);
            temperature = itemView.findViewById(R.id.temperature_tv);
            timestamp = itemView.findViewById(R.id.timestamp_tv);
            humidity = itemView.findViewById(R.id.humidity_tv);
            lux = itemView.findViewById(R.id.lux_tv);
            barPress = itemView.findViewById(R.id.bar_press_tv);
            pressureIcon = itemView.findViewById(R.id.pressure_icon);
            battery = itemView.findViewById(R.id.battery_tv);
        }
    }
}
