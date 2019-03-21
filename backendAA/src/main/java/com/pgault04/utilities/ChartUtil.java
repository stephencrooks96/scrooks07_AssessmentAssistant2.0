package com.pgault04.utilities;

/**
 * @author Paul Gault - 40126005
 * @since December 2018
 * Allows for colors to be generated for chart areas
 */
public class ChartUtil {

    /**
     * Generates random number to correspond to red, green and blue to create a color
     *
     * @return randomly generated rgb color
     */
    public static String chartColourGenerate() {
        double r, g, b;
        r = Math.floor(Math.random() * 255);
        g = Math.floor(Math.random() * 255);
        b = Math.floor(Math.random() * 255);
        return "rgb(" + r + "," + g + "," + b + ")";
    }
}