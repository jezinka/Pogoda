package com.projects.jezinka.pogoda;

import com.google.gson.JsonObject;

public class SensorJsonHelper {

    public static String getLabelFromJson(JsonObject body, String key) {
        return body
                .getAsJsonObject(Constants.SENSORS)
                .getAsJsonObject(key)
                .get(Constants.LABEL)
                .toString()
                .replaceAll("\"", "");
    }

}
