package org.sportradar.util;

public class TerminalColors {

    // Color codes
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String RESET = "\u001B[0m";

    /**
     * @param text to color
     * @return the text in green.
     */
    public static String green(String text) {
        return GREEN + text + RESET;
    }

    /**
     * @param text to color
     * @return the text in yellow
     */
    public static String yellow(String text) {
        return YELLOW + text + RESET;
    }

}