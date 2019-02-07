package com.projects.jezinka.pogoda;

import android.graphics.Color;
import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.Map;

public class Sensor {

    private long id;
    private String label;
    private double temperature;
    private long timestamp;
    private int humidity;
    private double lux;
    private Double barPressure;
    private double vbat, vreg;

    public long getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }

    public CharSequence getTemperature() {
        return TextUtils.concat(String.valueOf(Constants.df.format(this.temperature)), Constants.C_DEGREE);
    }

    public CharSequence getTimestamp() {
        return Constants.formatter.format(this.timestamp);
    }

    public CharSequence getHumidity() {
        return TextUtils.concat(String.valueOf(this.humidity), Constants.PERCENT);
    }

    public CharSequence getLux() {
        return TextUtils.concat(Constants.df.format(this.lux), Constants.LUX);
    }

    public boolean isBarPressureNull() {
        return this.barPressure == null;
    }

    public CharSequence getBarPressure() {
        return TextUtils.concat(Constants.df.format(this.barPressure), Constants.H_PA);
    }

    public CharSequence getVbatVreg() {
        return TextUtils.concat(Constants.df.format(this.vbat), Constants.V, Constants.SEPARATOR, Constants.df.format(this.vreg), Constants.V);
    }

    public int getBatteryColor() {
        if (this.vbat > Constants.GOOD_BATTERY_VOLTAGE) {
            return Color.parseColor(Constants.GREEN);
        }
        if (this.vbat > Constants.CRITICAL_BATTERY_VOLTAGE + Constants.VOLTAGE_BUFFOR) {
            return Color.YELLOW;
        }
        return Color.RED;
    }

    public boolean batteryNeedRecharge() {
        return this.vbat <= Constants.CRITICAL_BATTERY_VOLTAGE;
    }

    public boolean isSensorDead() {
        return new Date().getTime() - this.timestamp > 3_600_000;
    }

    public Sensor() {
    }

    Sensor(Map.Entry<String, JsonElement> entry, String label) {

        this.id = Long.valueOf(entry.getKey());
        this.label = label;

        JsonObject measurement = entry.getValue().getAsJsonObject();

        this.temperature = measurement.get("hum_temp").getAsDouble();
        this.timestamp = measurement.get("stamp").getAsLong() * 1000;
        this.humidity = measurement.get("hum_hum").getAsInt();
        this.lux = measurement.get("lux").getAsDouble();
        this.barPressure = measurement.get("bar_pres_rel").isJsonNull() ? null : measurement.get("bar_pres_rel").getAsDouble();
        this.vbat = measurement.get("vbat").getAsDouble();
        this.vreg = measurement.get("vreg").getAsDouble();
    }
}
