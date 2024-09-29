package com.skillbox.cryptobot.utils;

import java.math.BigDecimal;

public class TextUtil {

    public static String toString(double value) {
        return String.format("%.2f", value);
    }

    public static String toString(BigDecimal value) {
        return String.format("%.2f", value);
    }
}
