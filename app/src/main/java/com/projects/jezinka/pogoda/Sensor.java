package com.projects.jezinka.pogoda;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class Sensor {

    private long id;
    String label;
    double temperature;
    long timestamp;
    int humidity;
    double lux;
    Double barPressing;
    double vbat, vreg;

    public long getId() {
        return id;
    }

    public Sensor(Map.Entry<String, JsonElement> entry, String label) {

        this.id = Long.valueOf(entry.getKey());
        this.label = label;

        JsonObject measurement = entry.getValue().getAsJsonObject();

        this.temperature = measurement.get("hum_temp").getAsDouble();
        this.timestamp = measurement.get("stamp").getAsLong() * 1000;
        this.humidity = measurement.get("hum_hum").getAsInt();
        this.lux = measurement.get("lux").getAsDouble();
        this.barPressing = measurement.get("bar_pres_rel").isJsonNull() ? null : measurement.get("bar_pres_rel").getAsDouble();
        this.vbat = measurement.get("vbat").getAsDouble();
        this.vreg = measurement.get("vreg").getAsDouble();
    }
}
