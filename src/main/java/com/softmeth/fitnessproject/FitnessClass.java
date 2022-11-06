package com.softmeth.fitnessproject;

import java.util.ArrayList;

/**
 * The Fitness class has all of the methods and variables that
 * pertains to
 * signing up and dropping fitness classes. It is the class that
 * allows users
 * to sign up and drop classes in pilates, spinning, and cardio.
 * Each of those
 * classes have their own instance of the member database to help
 * manage the
 * members that are inside it. Also contains methods to help sort
 * and print out
 * the members that are in each class
 *
 * @author Peter Chen, Jonathon Lopez
 */
public class FitnessClass {
    public static ArrayList<FitnessClass> morningClasses =
            new ArrayList<>();
    public static ArrayList<FitnessClass> afternoonClasses =
            new ArrayList<>();
    public static ArrayList<FitnessClass> eveningClasses =
            new ArrayList<>();

    public static ArrayList<String> allClasses = new ArrayList<>();
    public static ArrayList<String> allTeachers = new ArrayList<>();
    public static ArrayList<String> allLocations = new ArrayList<>();


    private ArrayList<Member> classRoster;
    private Location location;
    private Time time;
    private String teacher;
    private String classType;

    private ArrayList<Member> guestRoster;

    /**
     * Constructor to Set all the Attributes of a Fitness Class
     *
     * @param location the Location of a class
     * @param time     the Time of a class
     * @param teacher  the Instructor of a class
     * @param classType the Type of class
     * @param location the Location of a class
     */
    public FitnessClass(Location location, Time time,
                        String teacher, String classType) {
        this.location = location;
        this.time = time;
        this.teacher = teacher;
        this.classType = classType;
        classRoster = new ArrayList<Member>();
        guestRoster=new ArrayList<>();
    }


    /**
     * Gets the Class' Guest Member List
     *
     * @return The Class' Guest Member List
     */
    public ArrayList<Member> getGuestMembers(){
        return this.guestRoster;
    }

    /**
     * Gets the Class' Member List
     *
     * @return The Class' Guest Member List
     */
    public ArrayList<Member> getClassRoster() {
        return this.classRoster;
    }

    /**
     * Gets the Time a Class takes place
     *
     * @return The Time of a Class
     */
    public Time getTime() {
        return this.time;
    }

    /**
     * Gets a Classes Instructor
     *
     * @return The Instructor of a Class
     */
    public String getTeacher() {
        return this.teacher;
    }

    /**
     * Gets the Type of Class, For Example Spinning or Cardio
     *
     * @return The Type of Class
     */
    public String getClassType() {
        return this.classType;
    }

    /**
     * Gets the Location a Class takes place
     *
     * @return The Location of Class
     */
    public Location getLocation() {
        return this.location;
    }


    /**
     Checks whether another Fitness Class is the same as Fitness Class1(this)
     @param fitnessClass The Fitness Class equals Fitness Class1(this)
     @return true if a Fitness Class is the same as Fitness Class1(this)
     , false otherwise
     */
    public boolean equals(FitnessClass fitnessClass) {
        if (this == fitnessClass)
            return true;
        else {
            return this.getTime().equals(fitnessClass.getTime()) && this.getTeacher().equals(fitnessClass.getTeacher())
                    && this.getLocation().equals(fitnessClass.getLocation()) && this.getClassType().equals(fitnessClass.getClassType());
        }
    }


    /**
     * This method checks a Members DOB and Expiration date are valid calendar
     * dates, DOB is not today or a future date, check if a Member is 18 or
     * older, if a Members location is valid and if the Member is already in the
     * database. This method is used only for testing purposes and is the exact same
     * one that exists in the gymamanger class.
     *
     * @param member The Member who's being checked
     * @return true if member passes all checks, otherwise false
     */
    public boolean newValidMemberCheck(Member member) {
        MemberDatabase member_db = new MemberDatabase();
        Date today = new Date();
        if (!(member.getdob().isValid())) {
            return false;
        } else if (!member.getExpireDate().isValid()) {
            return false;
        } else if (today.compareTo(member.getdob()) <= 0) {
            return false;
        } else if (!(member_db.checkOver18(member))) {
            return false;
        }
        if (!member_db.checkLocationExists(member)) {
            return false;
        }
        if ((member_db.Exist(member) != -1)) {
            return false;
        }
        return true;
    }

    /**
     Add a Member into a fitness Class and checks for conflicts for that member
     @param member the Members that is being added
     @return The string message of whether the member has been added successfully
     */
    public String addMember(Member member) {
        if (this.getTime() == Time.MORNING) {
            for (FitnessClass f : FitnessClass.morningClasses) {
                if (f.classRoster.contains(member) && !this.equals(f)) {
                   return (String.format("Time conflict - %s - %s, %s, %s",
                            this.classType, this.teacher, this.time.getTime(), this.location.getLocation()));
                }
            }
        } else if (this.getTime() == Time.AFTERNOON) {
            for (FitnessClass f : FitnessClass.afternoonClasses) {
                if (f.classRoster.contains(member) && !this.equals(f)) {
                    return(String.format("Time " +
                            "conflict - %s - %s, %s, %s",
                            this.classType, this.teacher, this.time.getTime(),this.location.getLocation()));
                }
            }
        } else {
            for (FitnessClass f : FitnessClass.eveningClasses) {
                if (f.classRoster.contains(member) && !this.equals(f)) {
                    return (String.format("Time conflict - %s - %s, %s, %s",
                            this.classType, this.teacher, this.time.getTime(),this.location.getLocation()));
                }
            }
        }
        if (this.classRoster.contains(member)) {
            return (String.format("%s %s already checked in.", member.getfname(),member.getlname()));
        } else {
            this.classRoster.add(member);
            return (String.format("%s %s successfully checked in" +
                            " %s - %s, %s, %s " +
                            "\n", member.getfname(),
                    member.getlname(), this.getClassType(), this.getTeacher(),
                    this.getTime().getTime(), this.getLocation().getLocationString()));
//            GymManagerController.printClassMembers(this, );
        }
    }

    /**
     Remove a Member from a fitness Class
     @param member the Members that is being removed
     @return The string message of whether member was successfully dropped
     */
    public String dropMember(Member member) {
        if (this.classRoster.contains(member)) {
            this.classRoster.remove(member);
           return String.format("%s %s done with the class.\n", member.getfname(), member.getlname());
        }else{
            return String.format("%s %s did not check in.\n", member.getfname(), member.getlname());
        }
    }

    /**
     Remove a Guest Member from a Class
     @param member the Guest Members that is being removed
     @return The string message of whether the guest member has been successfully dropped or not
     */
    public String dropGuestMember(Member member) {
        if (this.guestRoster.contains(member)) {
            this.guestRoster.remove(member);
            return String.format("%s %s Guest done with the class.\n", member.getfname(), member.getlname());
        }else{
            return String.format("%s %s did not check in.\n", member.getfname(), member.getlname());
        }
    }
}
