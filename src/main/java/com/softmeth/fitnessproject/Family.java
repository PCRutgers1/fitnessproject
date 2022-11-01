package com.softmeth.fitnessproject;

/**
 * The family inherits from the member class and allows the
 * the creation of users with family membership
 * Has all the methods and variables associated with the
 * a family member
 * @author Peter Chen, Jonathon Lopez
 */
public class Family extends Member {
    private int guestPassesLeft;
    
    /**
     Takes all information of a member and turns it into a
     printable String
     @return A String with all the information of a member
     */
    public String toString() {
        return String.format(
                "%s %s, DOB: %s, Membership expires %s, Location: " +
                        "%s, (Family) guest-pass remaining: %d", this.getfname(),
                this.getlname(), this.getdob().toString(),
                this.getExpireDate().toString(),
                this.getLocation().getLocation(),
                this.getGuestPassesLeft());
    }

    @Override
    /**
     * Returns the Membership Fee
     *
     * @return Membership Fee
     */
    public double membershipFee() {
        return 29.99 + 3 * 59.99;
    }

    /**
     * Gets the Guest Passes of a Family Member
     *
     * @return Family Member's Guest Passes
     */
    public int getGuestPassesLeft() {
        return this.guestPassesLeft;
    }

    /**
     * Set the Number of Guest passes for a Family Member
     *
     * @param passes the Number of Guest Passes
     */
    public void setGuestPassesLeft(int passes) {
        this.guestPassesLeft = passes;
    }


    /**
     * Constructor to set all the Attributes of a Family
     *
     * @param fname    the First Name of a Member
     * @param lname    the Last Name of a Member
     * @param dob      the DOB of a Member
     * @param expDate  the Expiration Date of a Member
     * @param location the Location of a Member
     */
    public Family(String fname, String lname, Date dob, Date expDate,
                  Location location) {
        super.setFname(fname);
        super.setLname(lname);
        super.setDob(dob);
        super.setExpire(expDate);
        super.setLocation(location);
        this.guestPassesLeft = 1;
    }

}

