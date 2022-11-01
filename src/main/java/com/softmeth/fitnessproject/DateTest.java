//package com.softmeth.fitnessproject;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
///**
// * Junit test class to test the ivalid method specifically from the date class
// * @author Peter Chen, Jonathan Lopez
// */
//class DateTest {
//
//    /**
//     * Tests that the given year is valid
//     */
//    @Test
//    public void year_isValid() {
//        //Validity of date before 1900:
//        Date badyear = new Date("11/21/800");
//        assertFalse(badyear.isValid());
//
//        Date goodyear = new Date("11/21/2000");
//        assertTrue(goodyear.isValid());
//
//        //Validity of date after 2100
//        badyear = new Date("1/1/2990");
//        assertFalse(badyear.isValid());
//
//        goodyear = new Date("11/21/2000");
//        assertTrue(goodyear.isValid());
//    }
//
//    /**
//     * tests that the given date in february is valid
//     * based on whether or not it is a leap year
//     */
//    @Test
//    public void feb_isValid() {
//        //Validity of february days on non-leap years
//        Date badDate = new Date("2/29/2018");
//        assertFalse(badDate.isValid());
//
//        badDate = new Date("2/30/2000");
//        assertFalse(badDate.isValid());
//
//        Date goodDate = new Date("2/29/2020");
//        assertTrue(goodDate.isValid());
//
//    }
//
//    /**
//     * tests that the month that is entered is valid
//     */
//    @Test
//    public void month_isValid() {
//        //User must enter valid month number
//        Date badDate = new Date("-1/31/2003");
//        assertFalse(badDate.isValid());
//
//        badDate = new Date("13/31/2003");
//        assertFalse(badDate.isValid());
//
//        Date goodDate = new Date("2/28/2018");
//        assertTrue(goodDate.isValid());
//
//    }
//
//    /**
//     * tests that the entered day is valid
//     */
//    @Test
//    public void day_isValid() {
//
//        //User can't enter 0 as date
//        Date badDate = new Date("1/0/2003");
//        assertFalse(badDate.isValid());
//
//        //Long months must have 31 days
//        badDate = new Date("3/32/2003");
//        assertFalse(badDate.isValid());
//
//        Date goodDate = new Date("1/31/2003");
//        assertTrue(goodDate.isValid());
//
//        //Short months must have 30 days
//        badDate = new Date("4/31/2022");
//        assertFalse(badDate.isValid());
//
//        goodDate = new Date("4/30/2003");
//        assertTrue(goodDate.isValid());
//    }
//}