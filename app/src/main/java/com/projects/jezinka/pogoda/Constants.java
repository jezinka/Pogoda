package com.projects.jezinka.pogoda;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class Constants {

    static final int SPAN_COUNT = 2;

    static final String CHANNEL_ID = "S3N50R3K";

    static final String GREEN = "#008080";
    static final String PERCENT = "%";
    static final String LUX = " lx";
    static final String H_PA = " hPa";
    static final String V = " V";
    static final String SEPARATOR = "/";
    static final String C_DEGREE = "\u00B0C";
    static final double CRITICAL_BATTERY_VOLTAGE = 3.0;
    static final double GOOD_BATTERY_VOLTAGE = 3.4;
    static final double VOLTAGE_BUFFOR = 0.2;

    static final String BALKON = "Balkon";

    static final String ACTION_UPDATE_WIDGET = "com.projects.jezinka.pogoda.action.update";
    static final String ACTION_REFRESH_WIDGET = "com.projects.jezinka.pogoda.action.refresh";

    static final String SENSORS = "sensors";
    static final String LABEL = "label";

    static DecimalFormat df = new DecimalFormat("####0.00");
    static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.CANADA);
}
