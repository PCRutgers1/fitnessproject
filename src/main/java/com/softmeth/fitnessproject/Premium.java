package fitnessproject;

/**
 * The class to create a member with a premium membership
 * This has all of the related methods and variables to create
 * a member with a premium membership
 * @author  Peter Chen, Jonathan Lopez
 */
public class Premium extends Family{
    private boolean trialOver = false;

    /**
     Takes all information of a member and turns it into a
     printable String
     @return A String with all the information of a member
     */
    public String toString() {
        return String.format(
                "%s %s, DOB: %s, Membership expires %s, Location: " +
                        "%s, (Premium) guest-pass remaining: %d", this.getfname(),
                this.getlname(), this.getdob().toString(),
                this.getExpireDate().toString(),
                this.getLocation().getLocation(),
                this.getGuestPassesLeft());
    }

    @Override
    /**
     * Returns the Membership Fee for a premium member
     *
     * @return Membership Fee for a premium member
     */
    public double membershipFee(){
        if (this.trialOver){
            return 59.99 * 12;
        }else{
            return 59.99*11;
        }
    }

    /**
     * sets the trial period over for the premium user
     * This occurs one year after they join.
     */
    public void setTrialOver(){
        this.trialOver = true;
    }

    /**
     * Initializes a premium meber and sets all the Attributes of a
     * premium Member
     *
     * @param fname    the First Name of a Member
     * @param lname    the Last Name of a Member
     * @param dob      the DOB of a Member
     * @param expDate  the Expiration Date of a Member
     * @param location the Location of a Member
     */
    public Premium(String fname, String lname, Date dob, Date expDate,
                  Location location) {
        super(fname,lname,dob,expDate,location);
        this.trialOver = false;
        super.setGuestPassesLeft(3);
    }

}
