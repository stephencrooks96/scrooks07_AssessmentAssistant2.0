package com.pgault04.utilities;

public class ChartUtil {

    public static String chartColourGenerate() {
        double r, g, b;
        r = Math.floor(Math.random() * 255);
        g = Math.floor(Math.random() * 255);
        b = Math.floor(Math.random() * 255);
        return "rgb(" + r + "," + g + "," + b + ")";
    }
}
