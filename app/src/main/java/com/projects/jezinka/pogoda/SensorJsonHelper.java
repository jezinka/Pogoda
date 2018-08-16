package com.projects.jezinka.pogoda;

import com.google.gson.JsonObject;

public class SensorJsonHelper {

    private static final String SENSORS = "sensors";
    private static final String LABEL = "label";

    public static String getLabelFromJson(JsonObject body, String key) {
        return body
                .getAsJsonObject(SENSORS)
                .getAsJsonObject(key)
                .get(LABEL)
                .toString()
                .replaceAll("\"", "");
    }

}
