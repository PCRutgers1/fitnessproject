//package com.softmeth.fitnessproject;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
///**
// * class to tests the membership fee returns the right amount
// * @author Peter Chen, Jonathan Lopez
// */
//class PremiumTest {
//
//    /**
//     * method that tests if the amount returned by the membership
//     * fee method is correct based on the disposition of the premium
//     * member
//     */
//    @Test
//    public void membershipFee() {
//        //create a new premium member
//        Premium premiumMember= new Premium("fname",
//                "lname",new Date("1/1/2000"), new Date("4/30/2023"),Location.FRANKLIN);
//        //make sure they are only being charged for 11 months when they first join
//        assertEquals(659.89, premiumMember.membershipFee());
//
//        //set the trial period over and make sure that they are now being charged for 12 months
//        premiumMember.setTrialOver();
//        assertEquals(719.88,premiumMember.membershipFee());
//
//    }
//}