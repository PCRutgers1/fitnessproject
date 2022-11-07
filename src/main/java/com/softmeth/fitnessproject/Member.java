package com.softmeth.fitnessproject;

/**
 * This class crates an instance of member that will be added to
 * the Member
 * Database All the methods equate to properties of a person to be
 * added to the
 * Member Databases.While also being able to turn the Member in to
 * a String,
 * see if it is equal to an objects, and compare them to other members
 *
 * @author Peter Chen, Jonathan Lopez
 */
public class Member implements Comparable<Member> {
    private String fname;
    private String lname;
    private Date dob;
    private Date expire;
    private Location location;

    private static int zero = 0;

    @Override
    /**
     Takes all information of a member and turns it into a
     printable String
     @param
     @return A String with all the information of a member
     */
    public String toString() {
        return String.format(
                "%s %s, DOB: %s, Membership expires %s, Location: " +
                        "%s", this.getfname(),
                this.getlname(), this.getdob().toString(),
                this.getExpireDate().toString(),
                this.getLocation().getLocation());
    }

    @Override
    /**
     Checks whether an Object is a Member and is the same as
     Member1(this)
     @param obj that methos is comparing to
     @return true if an Object is a Member and the same as Member1
     (this),
     false otherwise
     */
    public boolean equals(Object obj) {
        if (obj instanceof Member) {
            return (((Member) obj).getfname() == this.getfname()) && (((Member) obj).getlname() == this.getlname()) && (((Member) obj).getdob() == this.getdob());
        } else {
            return false;
        }
    }



    @Override
    /**
     Compares two Members to see if they are the same person, if
     @param member the Member that is being comparedTo
     @return int>0 if Member1(this)>Member2(member),
     int<0 if Member1(this)<Member2(member)
     0 if Member1(this)=Member2(member)

     */
    public int compareTo(Member member) {
        if (this.getlname().compareToIgnoreCase(member.getlname()) != zero)
            return this.getlname().compareToIgnoreCase(member.getlname());
        if (this.getlname().compareToIgnoreCase(
                member.getlname()) == zero && this.getfname().compareToIgnoreCase(
                member.getfname()) != zero)
            return this.getfname().compareToIgnoreCase(member.getfname());
        return this.getdob().compareTo(member.getdob());
    }

    /**
     * Gets the DOB of a Member
     *
     * @return Member's DOB
     */
    public Date getdob() {
        return this.dob;
    }

    /**
     * Gets the First Name of a Member
     *
     * @return Member's First Name
     */
    public String getfname() {
        return this.fname;
    }

    /**
     * Gets the Last Name of a Member
     *
     * @return Member's Last Name
     */
    public String getlname() {
        return this.lname;
    }

    /**
     * Gets the Expiration Date of a Member
     *
     * @return Member's Expiration Date
     */
    public Date getExpireDate() {
        return this.expire;
    }

    /**
     * Gets the Location of a Member
     *
     * @return Member's Location
     */
    public Location getLocation() {
        return this.location;
    }
    /**
     * Set all the Attributes of a Member
     *
     * @param fname    the First Name of a Member
     * @param lname    the Last Name of a Member
     * @param dob      the DOB of a Member
     * @param expDate  the Expiration Date of a Member
     * @param location the Location of a Member
     */
    public Member(String fname, String lname, Date dob, Date expDate,
                  Location location) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.expire = expDate;
        this.location = location;
    }

    /**
     * sets the fname of the current object to fname
     * @param fname that we set the current object to
     */
    public void setFname(String fname){
        this.fname = fname;
    }

    /**
     * sets the last name of the current object to lname
     * @param lname that we set the current object's last name to
     */
    public void setLname(String lname){
        this.lname = lname;
    }

    /**
     * sets the dob of the current object dob
     * @param dob that we set the current object's dob to
     */
    public void setDob(Date dob){
        this.dob = dob;
    }

    /**
     * sets the expiration date of the current object to expire
     * @param expire that we set the current object to
     */
    public void setExpire(Date expire){
        this.expire = expire;
    }

    /**
     * sets the location of the current object to location
     * @param location that we set the current object to
     */
    public void setLocation(Location location){
        this.location = location;
    }
    /**
     * get the membership fee for a standard member
     * @return  the Membership fee for the member
     */
    public double membershipFee() {
        return 29.99 + 3 * 39.99;
    }

    /**
     * Default constructor, does nothing
     */
    public Member() {
    }
}
