package com.projects.jezinka.pogoda;

import android.graphics.Color;
import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

public class Sensor {

    private static final String GREEN = "#008080";

    private static final String PERCENT = "%";
    private static final String LUX = " lx";
    private static final String H_PA = " hPa";
    private static final String V = " V";
    private static final String SEPARATOR = "/";
    private static final String C_DEGREE = "\u00B0C";

    private static final double CRITICAL_BATTERY_VOLTAGE = 3.0;
    private static final double GOOD_BATTERY_VOLTAGE = 3.5;
    private static final double VOLTAGE_BUFFOR = 0.1;

    private static DecimalFormat df = new DecimalFormat("####0.00");
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.CANADA);

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
        return TextUtils.concat(String.valueOf(this.temperature), C_DEGREE);
    }

    public CharSequence getTimestamp() {
        return formatter.format(this.timestamp * 1000);
    }

    public CharSequence getHumidity() {
        return TextUtils.concat(String.valueOf(this.humidity), PERCENT);
    }

    public CharSequence getLux() {
        return TextUtils.concat(df.format(this.lux), LUX);
    }

    public boolean isBarPressureNull() {
        return this.barPressure == null;
    }

    public CharSequence getBarPressure() {
        return TextUtils.concat(df.format(this.barPressure), H_PA);
    }

    public CharSequence getVbatVreg() {
        return TextUtils.concat(df.format(this.vbat), V, SEPARATOR, df.format(this.vreg), V);
    }

    public int getBatteryColor() {
        if (this.vbat > GOOD_BATTERY_VOLTAGE) {
            return Color.parseColor(GREEN);
        }
        if (this.vbat > CRITICAL_BATTERY_VOLTAGE + VOLTAGE_BUFFOR) {
            return Color.YELLOW;
        }
        return Color.RED;
    }

    public boolean batteryNeedRecharge() {
        return this.vbat <= CRITICAL_BATTERY_VOLTAGE;
    }

    Sensor(Map.Entry<String, JsonElement> entry, String label) {

        this.id = Long.valueOf(entry.getKey());
        this.label = label;

        JsonObject measurement = entry.getValue().getAsJsonObject();

        this.temperature = measurement.get("hum_temp").getAsDouble();
        this.timestamp = measurement.get("stamp").getAsLong();
        this.humidity = measurement.get("hum_hum").getAsInt();
        this.lux = measurement.get("lux").getAsDouble();
        this.barPressure = measurement.get("bar_pres_rel").isJsonNull() ? null : measurement.get("bar_pres_rel").getAsDouble();
        this.vbat = measurement.get("vbat").getAsDouble();
        this.vreg = measurement.get("vreg").getAsDouble();
    }
}
