//package com.softmeth.fitnessproject;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
///**
// * Class for junit test cases for the fitness class
// *
// * @author Peter Chen, Jonathan Lopez
// */
//class FitnessClassTest {
//
//    /**
//     * Method to test that we can add members and guest members without issue
//     */
//    @Test
//    void addMember() {
//        FitnessClass fitnessClass1 =
//                new FitnessClass(Location.FRANKLIN, Time.EVENING, "teacher1",
//                        "running");
//        FitnessClass fitnessClass2 =
//                new FitnessClass(Location.FRANKLIN, Time.EVENING, "teacher1",
//                        "weights");
//        FitnessClass fitnessClass3 =
//                new FitnessClass(Location.FRANKLIN, Time.MORNING, "teacher1",
//                        "weights");
//
//        FitnessClass.eveningClasses.add(fitnessClass1);
//        FitnessClass.eveningClasses.add(fitnessClass2);
//
//        Member member =
//                new Member("firstname", "lastname", new Date("1/1/2000"),
//                        new Date("1/1/2040"), Location.FRANKLIN);
//        assertTrue(fitnessClass1.addMember(member));
//
//        //can't add member during same time
//        assertFalse(fitnessClass2.addMember(member));
//
//        //Already in class
//        assertFalse(fitnessClass1.addMember(member));
//
//        assertTrue(fitnessClass3.addMember(member));
//
//        //makes ure that premium and family members can be added
//        Premium premiumMember =
//                new Premium("firstname", "lastname", new Date("1/1/2000"),
//                        new Date("1/1/2040"), Location.FRANKLIN);
//        assertTrue(fitnessClass2.addMember(premiumMember));
//        assertFalse(fitnessClass2.addMember(premiumMember));
//
//        Family familyMember =
//                new Family("firstname", "lastname", new Date("1/1/2000"),
//                        new Date("1/1/2040"), Location.FRANKLIN);
//        assertTrue(fitnessClass2.addMember(familyMember));
//        assertFalse(fitnessClass2.addMember(familyMember));
//
//        assertTrue(fitnessClass2.newValidMemberCheck(familyMember));
//        assertTrue(fitnessClass2.newValidMemberCheck(premiumMember));
//
//        //checks if members with bad dates can be added to the database
//        Premium badMember =
//                new Premium("firstname", "lastname", new Date("111/1/2000"),
//                        new Date("1/1/2040"), Location.FRANKLIN);
//        Family badfamilyMember =
//                new Family("firstname", "lastname", new Date("11/1/2000"),
//                        new Date("111/1/223040"), Location.FRANKLIN);
//
//        assertFalse(fitnessClass2.newValidMemberCheck(badMember));
//        assertFalse(fitnessClass2.newValidMemberCheck(badfamilyMember));
//
//        badMember = new Premium("firstname", "lastname", new Date("11/1/2000"),
//                new Date("1/1/2040"), Location.INVALID);
//        assertFalse(fitnessClass2.newValidMemberCheck(badMember));
//
//        //makes sure that we can only add one guest member for the family membership
//        // and that premium membership allows us to add 3 members
//        badMember = new Premium("firstname", "lastname", new Date("11/1/2010"),
//                new Date("1/1/2040"), Location.BRIDGEWATER);
//        assertFalse(fitnessClass2.newValidMemberCheck(badMember));
//        assertTrue(fitnessClass2.getGuestMembers().add(familyMember));
//        assertTrue(fitnessClass2.getGuestMembers().add(premiumMember));
//        assertTrue(fitnessClass2.getGuestMembers().add(premiumMember));
//        assertTrue(fitnessClass2.getGuestMembers().add(premiumMember));
//    }
//
//    /**
//     * JUNIt test method to make sure that we can drop members without issue
//     */
//    @Test
//    void dropMember() {
//        //same as above just with different output message
//        FitnessClass fitnessClass1 =
//                new FitnessClass(Location.FRANKLIN, Time.EVENING, "teacher1",
//                        "running");
//        FitnessClass fitnessClass2 =
//                new FitnessClass(Location.FRANKLIN, Time.EVENING, "teacher1",
//                        "weights");
//        FitnessClass fitnessClass3 =
//                new FitnessClass(Location.FRANKLIN, Time.MORNING, "teacher1",
//                        "weights");
//
//        FitnessClass.eveningClasses.add(fitnessClass1);
//        FitnessClass.eveningClasses.add(fitnessClass2);
//
//        Member member =
//                new Member("firstname", "lastname", new Date("1/1/2000"),
//                        new Date("1/1/2040"), Location.FRANKLIN);
//        assertTrue(fitnessClass1.addMember(member));
//        fitnessClass1.addMember(member);
//        //see that we can successfully drop member
//        assertTrue(fitnessClass1.dropMember(member));
//
//        assertFalse(fitnessClass1.dropMember(member));
//
//        //check that we won't be able to drop member from class
//        // that they have never joined
//        assertFalse(fitnessClass2.dropMember(member));
//        assertFalse(fitnessClass3.dropMember(member));
//
//        Premium premiumMember =
//                new Premium("firstname", "lastname", new Date("1/1/2000"),
//                        new Date("1/1/2040"), Location.FRANKLIN);
//        fitnessClass3.addMember(premiumMember);
//        //we can drop them from a class too
//        assertTrue(fitnessClass3.dropMember(premiumMember));
//        assertFalse(fitnessClass3.dropMember(premiumMember));
//
//        //do the same thing with family member
//        Family familyMember =
//                new Family("firstname", "lastname", new Date("1/1/2000"),
//                        new Date("1/1/2040"), Location.FRANKLIN);
//        fitnessClass3.addMember(familyMember);
//        //we can drop them from a class too
//        assertTrue(fitnessClass3.dropMember(familyMember));
//        assertFalse(fitnessClass3.dropMember(familyMember));
//    }
//
//
//    /**
//     * Junit test method to make sure that we can drop guest members without
//     * issue
//     */
//    @Test
//    void dropGuestMember() {
//        //same as above just with different output message
//        FitnessClass fitnessClass1 =
//                new FitnessClass(Location.FRANKLIN, Time.EVENING, "teacher1",
//                        "running");
//        FitnessClass fitnessClass2 =
//                new FitnessClass(Location.FRANKLIN, Time.EVENING, "teacher1",
//                        "weights");
//        FitnessClass fitnessClass3 =
//                new FitnessClass(Location.FRANKLIN, Time.MORNING, "teacher1",
//                        "weights");
//
//        FitnessClass.eveningClasses.add(fitnessClass1);
//        FitnessClass.eveningClasses.add(fitnessClass2);
//
//        Member member =
//                new Member("firstname", "lastname", new Date("1/1/2000"),
//                        new Date("1/1/2040"), Location.FRANKLIN);
//        assertTrue(fitnessClass1.addMember(member));
//        fitnessClass1.getGuestMembers().add(member);
//
//        //see that we can successfully drop member
//        assertTrue(fitnessClass1.dropGuestMember(member));
//        //check if we can drop them again
//        assertFalse(fitnessClass1.dropGuestMember(member));
//
//        //check that we won't be able to drop member from class
//        // that they have never joined
//        assertFalse(fitnessClass2.dropGuestMember(member));
//        assertFalse(fitnessClass3.dropGuestMember(member));
//
//        //check that guest pass works and the premium members
//        // can successfullly add and drop from class
//        Premium premiumMember =
//                new Premium("firstname", "lastname", new Date("1/1/2000"),
//                        new Date("1/1/2040"), Location.FRANKLIN);
//        fitnessClass2.getGuestMembers().add(premiumMember);
//        fitnessClass2.getGuestMembers().add(premiumMember);
//        fitnessClass2.getGuestMembers().add(premiumMember);
//        assertTrue(fitnessClass2.dropGuestMember(premiumMember));
//        fitnessClass2.dropMember(premiumMember);
//        assertTrue(fitnessClass2.dropGuestMember(premiumMember));
//        fitnessClass2.dropMember(premiumMember);
//        assertTrue(fitnessClass2.dropGuestMember(premiumMember));
//        fitnessClass2.dropMember(premiumMember);
//        assertFalse(fitnessClass2.dropGuestMember(premiumMember));
//        assertFalse(fitnessClass1.dropGuestMember(premiumMember));
//        assertFalse(fitnessClass3.dropGuestMember(premiumMember));
//
//
//        //same check with family membership level, make sure that guests can be added
//        // and dropped from class
//        Family familyMember =
//                new Family("firstname", "lastname", new Date("1/1/2000"),
//                        new Date("1/1/2040"), Location.FRANKLIN);
//        fitnessClass2.getGuestMembers().add(familyMember);
//        assertTrue(fitnessClass2.dropGuestMember(familyMember));
//        fitnessClass2.dropMember(familyMember);
//        assertFalse(fitnessClass2.dropGuestMember(familyMember));
//        fitnessClass2.dropMember(familyMember);
//        assertFalse(fitnessClass2.dropGuestMember(familyMember));
//        fitnessClass2.dropMember(premiumMember);
//        assertFalse(fitnessClass2.dropGuestMember(familyMember));
//        assertFalse(fitnessClass1.dropGuestMember(familyMember));
//        assertFalse(fitnessClass3.dropGuestMember(familyMember));
//    }
//}