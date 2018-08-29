package com.projects.jezinka.pogoda;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SensorsAdapter extends RecyclerView.Adapter<SensorsAdapter.ViewHolder> {

    private List<Sensor> mSensor;

    SensorsAdapter() {
        mSensor = new ArrayList<>();
    }

    public int getItemCount() {
        return mSensor.size();
    }

    private Sensor getItem(int position) {
        return this.mSensor.get(position);
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
        final Sensor sensor = getItem(position);

        holder.label.setText(sensor.getLabel());
        holder.temperature.setText(sensor.getTemperature());
        holder.timestamp.setText(sensor.getTimestamp());
        holder.humidity.setText(sensor.getHumidity());
        holder.lux.setText(sensor.getLux());

        holder.battery.setColorFilter(sensor.getBatteryColor());
        holder.battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), sensor.getVbatVreg(), Toast.LENGTH_SHORT).show();
            }
        });

        if (!sensor.isBarPressureNull()) {
            holder.barPress.setText(sensor.getBarPressure());
        } else {
            holder.pressureGroup.setVisibility(View.INVISIBLE);
        }

        if (sensor.isSensorDead()) {
            holder.timestamp.setBackgroundColor(Color.RED);
            holder.timestamp.setTextColor(Color.WHITE);
        }
    }

    public long getItemId(int position) {
        return this.getItem(position).getId();
    }

    public void updateResults(List<Sensor> results) {
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
        ImageView battery;

        Group pressureGroup;

        ViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label_tv);
            temperature = itemView.findViewById(R.id.temperature_tv);
            timestamp = itemView.findViewById(R.id.timestamp_tv);
            humidity = itemView.findViewById(R.id.humidity_tv);
            lux = itemView.findViewById(R.id.lux_tv);
            barPress = itemView.findViewById(R.id.bar_press_tv);
            battery = itemView.findViewById(R.id.battery_icon);

            pressureGroup = itemView.findViewById(R.id.pressure_group);
        }
    }
}
