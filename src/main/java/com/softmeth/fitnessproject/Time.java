package com.softmeth.fitnessproject;

/**
 * This is the time enum class which contains all of the classes that are being
 * offered as well as the times associated with each class stored as values
 *
 * @author Peter Chen,Jonathan Lopez
 */
public enum Time {
    PILATES("09:30"), SPINNING("14:00"), CARDIO("14:00"), MORNING("9:30"), AFTERNOON("14:00"), EVENING("18:30"), INVALID("00:00");

    private final String time;

    /**
     * Simple constructor for each of our enum values that records the time
     * each class takes place and sets the time
     * @param time is the time to set the current time object equal to
     */
    private Time(String time) {
        this.time = time;
    }

    /**
     * Gets the Time
     *
     * @return The Time
     */
    public String getTime(){
        return this.time;
    }

    /**
     * Take in a string time like morning and change it to a numeric time
     *
     * @param time The String that is being changed to numeric
     * @return The numeric Time
     */
    public static Time findTime(String time) {
        Time[] allTime = Time.values();

        for (Time t : allTime) {
            if (t.name().equalsIgnoreCase(time)) return t;
        }
        return INVALID;
    }
}
