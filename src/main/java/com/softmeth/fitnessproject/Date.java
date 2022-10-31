package fitnessproject;

import java.util.Calendar;

/**
 * The Date class has everything to do with dates and will check for valid
 * dates, compare two dates, create new dates given a string and will create a
 * new date as today. Also, static final numbers and arrays are in this class
 * that helps the rest of the class methods verify dates and number of days in
 * a month
 *
 * @author Peter Chen, Jonathon Lopez
 */
public class Date implements Comparable<Date> {
    public static final int[] LONG_MONTHS = new int[]{1, 3, 5, 7, 8, 10, 12};
    public static final int[] SHORT_MONTHS = new int[]{4, 6, 9, 11};
    public static final int FEB = 2;
    public static final int LONG_MONTH_DAYS = 31;
    public static final int SHORT_MONTH_DAYS = 30;
    public static final int FEB_DAYS_NONLEAP = 28;
    public static final int FEB_DAYS_LEAP = 29;
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUARTERCENTENNIAL = 400;

    private static int No_Remainder = 0;
    private static int Invalid_Year_Low = 1900;
    private static int Invalid_Year_High = 2200;
    private static int Year_In_Months = 12;
    private static int First = 1;


    private int year;
    //months are indexed from 0 to 11
    private int month;
    private int day;

    /**
     * Getter method for Year
     *
     * @return returns the year for the current object
     */
    public int getYear() {
        return this.year;
    }


    /**
     * setter method for Year
     * sets the year of the date to the specified year
     * @param year the year that we want to set the current date object to
     */
    public void setYear(int year){
        this.year = year;
    }

    /**
     * Getter method for Month
     *
     * @return returns the month for the current object
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * setter method for Year
     * sets the year of the date to the specified year
     * @param month the year that we want to set the current date object to
     */
    public void setMonth(int month){
        this.month = month;
    }
    /**
     * Getter method for Day
     *
     * @return returns the day for the current object
     */
    public int getDay() {
        return this.day;
    }

    /**
     * Default constructor for the class if no values/parameters are provided.
     * Creates a new date object with today's date and sets this new object
     * with current day, month, and year
     */
    public Date() {
        Calendar today = Calendar.getInstance();
        this.year = today.get(Calendar.YEAR);
        this.month = today.get(Calendar.MONTH) + 1;
        this.day = today.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Overloaded constructor for the date class if values/parameters are
     * provided. Creates a new date object with the given day, year, and month.
     * It does so by finding the forward slashes that are inside of the date
     * string and then splitting the string at those places
     *
     * @param date that you would like to create a date object of.
     */
    public Date(String date) {
        String[] dates = date.split("/");

        this.month = Integer.parseInt(dates[0]);
        this.day = Integer.parseInt(dates[1]);
        this.year = Integer.parseInt(dates[2]);
    }

    /**
     * Takes a string date and checks if it is in a valid format. The method
     * will check if string contains foward slashes separating the month, day,
     * and year, and checks if the string follows this format: number of length
     * 1-2 followed by a forward slash, followed by another number of length of
     * either 1 or 2, followed by another forward slash, followed by a 4 digit
     * year
     *
     * @param date that you would like to check if it is in correct format
     * @return true if the date is in valid format and false otherwise
     */
    public static boolean isValid(String date) {
        if (!date.contains("/") || !date.matches(
                "^\\d{1,2}\\/\\d{1,2}\\/\\d{4}$")) {
            return false;
        }
        return true;
    }

    /**
     * Does a comparison between two dates. Simply checks if they are identical
     * first, then which one has a greater year, then which one has a greater
     * month, and finally which one has a greater day.
     *
     * @param date that we are comparing to the current object
     * @return 0 if identical, a number less than 0 if the second one is
     * greater (a later date) than the first date, and a number greater than 0
     * if the current date object occurs later
     */
    @Override
    public int compareTo(Date date) {
        if (date == this) return 0;

        if (date.year > this.year) return -1;
        else if (date.year < this.year) return 1;
        else {
            if (date.month > this.month) return -1;
            else if (date.month < this.month) return 1;
            else {
                if (date.day > this.day) return -1;
                else if (date.day < this.day) return 1;
                else return 0;
            }
        }
    }

    /**
     * The other isvalid method checker. This method will check if the current
     * date object is valid. It does so by analyzing whether the year is an
     * unreasonably far away year, such as before 1900 or after 2200. Then it
     * if the month is february and if the number of days are correct based on
     * whether it is a leap year or not. Finally, the method will check if the
     * month is a longer month or a shorter month and validate that the number
     * of days in the month does not exceed the corresponding value for that
     * month.
     *
     * @return true if the date is valid, false otherwise
     */
    public boolean isValid() {
        if (this.year < Invalid_Year_Low || this.year > Invalid_Year_High) return false;
        if (this.month < First || this.month > Year_In_Months) return false;
        if (this.day < First || this.day > LONG_MONTH_DAYS) {
            return false;
        }

        if (this.month == FEB) {
            if (isLeapYear(this.year)) {
                if (this.day > FEB_DAYS_LEAP) return false;
            } else if (this.day > FEB_DAYS_NONLEAP) return false;
        }

        for (int longMonth : LONG_MONTHS) {
            if (this.month == longMonth) {
                if (this.day > LONG_MONTH_DAYS) {
                    return false;
                }
                return true;
            }
        }
        for (int shortMonth : SHORT_MONTHS) {
            if (this.month == shortMonth) {
                if (this.day > SHORT_MONTH_DAYS) {
                    return false;
                }
                return true;
            }
        }
        return true;
    }

    /**
     * Quick method to check if the given integer year is a leap year or not.
     * It does so by checking if the year is divisible by 4 , 100, and 400, and
     * returns true and false accordingly
     *
     * @param year that the method is checking
     * @return true if the year is a leap year, false otherwise
     */
    public boolean isLeapYear(int year) {
        if (year % QUADRENNIAL == No_Remainder) if (year % CENTENNIAL == No_Remainder)
            if (year % QUARTERCENTENNIAL == No_Remainder) return true;
            else return false;
        else return true;
        else return false;
    }

    /**
     * Converts a date object to a string with format month/day/year
     *
     * @return converts a date to a string in the format month/day/year
     */
    @Override
    public String toString() {
        return String.format("%s/%s/%s", this.month, this.day, this.year);
    }


    /**
     * adds 3 months to the current date object
     */

    public Date addToDate(int numOfMonths){
        int newMonth = this.getMonth() + numOfMonths;
        if (newMonth > Year_In_Months){
            this.setMonth(newMonth%12);
            this.setYear(this.getYear()+1);
        }
        return this;
    }

    /**
     * testbed main to test the CompareTo method all of the cases can be found
     * in the test design document
     **/
    public static void main(String[] args) {
        Date testDate = new Date("11/21/800");
        System.out.printf(
                "Validity of date before 1900: %s and isvalid: %s \n",
                testDate, testDate.isValid());

        testDate = new Date("1/1/2990");
        System.out.printf(
                "Validity of date after 2100 days: %s and isvalid: %s \n",
                testDate, testDate.isValid());

        testDate = new Date("2/29/2018");
        System.out.printf(
                "Validity of february days on non-leap years: %s and " +
                        "isvalid: %s \n",
                testDate, testDate.isValid());

        testDate = new Date("-1/31/2003");
        System.out.printf(
                "Months must be between 1 and 12: %s and isvalid: %s \n",
                testDate, testDate.isValid());

        testDate = new Date("13/31/2003");
        System.out.printf(
                "Months must be between 1 and 12: %s and isvalid: %s \n",
                testDate, testDate.isValid());

        testDate = new Date("1/0/2003");
        System.out.printf("User can't enter 0 as date: %s and isvalid: %s \n",
                testDate, testDate.isValid());

        testDate = new Date("3/32/2003");
        System.out.printf(
                "Long months must have 31 days: %s and isvalid: %s \n",
                testDate, testDate.isValid());

        testDate = new Date("2/30/2000");
        System.out.printf(
                "Feb. must have appropriate number of days: %s and isvalid: "
                        + "%s \n",
                testDate, testDate.isValid());

        testDate = new Date("4/31/2022");
        System.out.printf(
                "Short months must have 30 days: %s and isvalid: %s \n",
                testDate, testDate.isValid());

        testDate = new Date("1/31/2003");
        System.out.printf(
                "Long months must have 31 days: %s and isvalid: %s \n",
                testDate, testDate.isValid());

        testDate = new Date("4/30/2003");
        System.out.printf(
                "Short months must have 30 days: %s and isvalid: %s \n",
                testDate, testDate.isValid());
    }
}
