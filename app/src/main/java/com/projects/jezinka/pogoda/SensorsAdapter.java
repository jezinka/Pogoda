package com.projects.jezinka.pogoda;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SensorsAdapter extends RecyclerView.Adapter<SensorsAdapter.ViewHolder> {
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
        temperature.setText(String.valueOf(sensor.temperature));
        TextView timestamp = holder.timestamp;
        timestamp.setText(String.valueOf(sensor.timestamp));

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

        ViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label_tv);
            temperature = itemView.findViewById(R.id.temperature_tv);
            timestamp = itemView.findViewById(R.id.timestamp_tv);
        }
    }
}
