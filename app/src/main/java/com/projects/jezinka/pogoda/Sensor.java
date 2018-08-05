package com.projects.jezinka.pogoda;

import com.google.gson.JsonElement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Sensor {

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private long id;
    String label;
    double temperature;
    String timestamp;

    public long getId() {
        return id;
    }

    public Sensor(Map.Entry<String, JsonElement> entry, String label) {

        this.id = Long.valueOf(entry.getKey());
        this.label = label;
        this.temperature = entry.getValue().getAsJsonObject().get("hum_temp").getAsDouble();
        this.timestamp = formatter.format(new Date(entry.getValue().getAsJsonObject().get("stamp").getAsLong() * 1000));
    }
}
