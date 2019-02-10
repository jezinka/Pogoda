package com.projects.jezinka.pogoda;

import android.graphics.Color;
import android.text.TextUtils;

import com.squareup.moshi.Json;

import java.util.Date;

public class Sensor {

    private long id;
    private String label;

    @Json(name = "hum_temp")
    private double temperature;

    @Json(name = "stamp")
    private long timestamp;

    @Json(name = "hum_hum")
    private double humidity;

    private double lux;

    @Json(name = "bar_pres_rel")
    private Double barPressure;

    private double vbat;
    private double vreg;

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

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", temperature=" + temperature +
                ", timestamp=" + timestamp +
                ", humidity=" + humidity +
                ", lux=" + lux +
                ", barPressure=" + barPressure +
                ", vbat=" + vbat +
                ", vreg=" + vreg +
                '}';
    }
}
