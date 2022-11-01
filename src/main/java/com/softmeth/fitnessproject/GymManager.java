package com.softmeth.fitnessproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 * The Gym manager class has all of the high level methods and
 * constants that
 * help us keep track of the user input and then calls the
 * corresponding
 * methods based on the user's commands. It has the entry to the
 * program, the
 * run method, as well as a method for every possible command that
 * the user can
 * call. Manages an array of all users in the member_db variable as
 * well as all
 * of the user input in the UIArray
 *
 * @author Peter Chen, Jonathon Lopez
 */
public class GymManager {
    MemberDatabase member_db;
    String[] UIArray;
    private static int Empty = 0;
    private static int None = 0;
    private static int Does_Not_Exist = -1;
    private static int Add_Large = 6;
    private static int Add_Small = 5;
    private static int Invalid_Remove_Len = 4;
    private static int Invalid_Guest_Len = 7;
    private static int Invalid_FMember_Len = 5;
    private static int Invalid_PMember_Len = 5;
    private static int Invalid_ClassInfo_Len = 4;
    private static int Invalid_MemberInfo_Len = 5;
    private static int Invalid_Drop_Len = 7;
    private static int Invalid_CheckIn_Len = 7;


    private static ClassSchedule cs;

    /**
     * Check whether the command inputted by the user is valid, if
     * valid it will
     * then call the command, else, it will just print out invalid
     * command
     */
    public void run() {
        System.out.println("Gym Manager running...");
        Scanner scan = new Scanner(System.in);  // Create a Scanner
        // object
        UIArray = RemoveSpace( (scan.nextLine())).split(" ");
        member_db = new MemberDatabase();
        String Valid_Commands = "ARPPNPCPDSCDQLSLMAFAPPFCGDG";
        cs = new ClassSchedule();

        while (!(UIArray[0].equals("Q"))) {
            if (!(Valid_Commands.contains(UIArray[0]))) {
                System.out.println(UIArray[0] + " is an invalid " +
                        "command");
            } else {
                callCommand();
            }
            UIArray = ((scan.nextLine())).split(" ");
        }
        System.out.println("Gym Manager terminated.");
    }

    /**
     * Check which command to call based on the switch statement
     * below and then
     * calls the corresponding method for that specific command
     */
    private void callCommand() {
        switch (UIArray[0]) {
            case "A":
                Add(new Date());
                break;
            case "R":
                Remove(new Date());
                break;
            case "P":
                printDB();
                break;
            case "PN":
                printByName();
                break;
            case "PC":
                printByCounty();
                break;
            case "PD":
                printByDate();
                break;
            case "S":
                displaySchedule();
                break;
            case "C":
                checkin();
                break;
            case "D":
                drop();
                break;
            case "LS":
                loadFromFile("classSchedule.txt");
                break;
            case "LM":
                loadFromFile("memberList.txt");
                break;
            case "AF":
                addFamilyMember();
                break;
            case "AP":
                addPremiumMember();
                break;
            case "PF":
                printMembershipFees();
                break;
            case "CG":
                guestCheckIn();
                break;
            case "DG":
                guestCheckout();
                break;
            default:
                System.out.println(UIArray[0] + " is an invalid " +
                        "command");
        }
    }

    /**
     * Find a fitness class without outputting checks if its valid
     *
     * @param classType String of the Type of Class
     * @param instructor String of the Instructor of a Class
     * @param location String of the Location of a Class
     * @return Fitness Class if found, otherwise null
     */
    private FitnessClass findClassWithoutChecks(String classType,
                                                String instructor,
                                                String location){
        FitnessClass fitnessClass;

        for (int i = 0; i < cs.getNumClasses(); i++) {
            fitnessClass = cs.getClasses()[i];

            if (fitnessClass.getClassType().equalsIgnoreCase(classType) && fitnessClass.getTeacher()
                    .equalsIgnoreCase(instructor) && fitnessClass.getLocation()
                    .equals(Location.findLocation(location.toUpperCase())))
                return fitnessClass;
        }
        return null;
    }

    /**
     * Find a fitness class while checking if the Type, Instructor, Location,
     * and Class exist and prints out the corresponding error messages
     *
     * @param classType String of the Type of Class
     * @param instructor String of the Instructor of a Class
     * @param location String of the Location of a Class
     * @return Fitness Class if found and passes all checks
     */
    private FitnessClass findClassWithChecks(String classType,
                                   String instructor,
                                   String location) {
        FitnessClass fitnessClass = findClassWithoutChecks( classType, instructor, location);

        if (!FitnessClass.allClasses.contains(classType.toLowerCase()))
            System.out.println(classType + " - class does not exist.");
        else if (!FitnessClass.allTeachers.contains(instructor.toLowerCase()))
            System.out.println(instructor + " - instructor does not exist.");
        else if (Location.findLocation(location.toUpperCase()) == Location.INVALID)
            System.out.println(location + " - invalid location.");
        else if (fitnessClass== null)
            System.out.printf("%s by %s does not exist at %s\n", classType, instructor,location);

        return fitnessClass;
    }

    /**
     * Checks a Guest Member into a Class and handles the related exceptions
     */
    private void guestCheckIn() {
        if (UIArray.length != Invalid_Guest_Len){
            System.out.println("Invalid number of parameters");
            return;
        }
        if (validMember(UIArray[4], UIArray[5], UIArray[6])) {
            Member member = member_db.returnMember(
                    new Member(UIArray[4], UIArray[5],
                            new Date(UIArray[6]),
                            new Date(), Location.PISCATAWAY));

            if (member instanceof Family) {
                if (((Family) member).getGuestPassesLeft() > None) {
                    if (Location.findLocation(
                            UIArray[3].toUpperCase()) == member.getLocation()) {
                        if (findClassWithChecks(UIArray[1], UIArray[2],
                                UIArray[3]) != null) {
                            ((Family) member).setGuestPassesLeft(
                                    ((Family) member).getGuestPassesLeft() - 1);
                            FitnessClass myClass =
                                    findClassWithoutChecks(UIArray[1], UIArray[2],
                                            UIArray[3]);
                            myClass.getGuestMembers().add(member);
                            System.out.printf("%s %s (guest) checked in %s- %s, %s, %s \n",
                                    member.getfname(), member.getlname(), myClass.getClassType(),
                                    myClass.getTeacher(), myClass.getTime().getTime(),
                                    myClass.getLocation().getLocationString());
                            printClassMembers(myClass);
                        } else {
                            System.out.println("Class does not exist");
                        }
                    } else
                        System.out.printf("%s %s Guest checking in %s- guest location restriction. \n",
                                member.getfname(), member.getlname(), Location.findLocation(
                                        UIArray[3].toUpperCase()).getLocation());
                } else {
                    System.out.printf("%s %s ran out of guest pass. \n",
                            member.getfname(), member.getlname());
                }
            } else {
                System.out.println(
                        "Standard membership - guest check-in is not allowed.");
            }
        }
    }

    /**
     * Check a Guest Member out of a Class if they are in 
     * the class and handles related exceptions
     */
    private void guestCheckout() {
        if (UIArray.length != Invalid_Guest_Len) {
            System.out.println("Invalid number of parameters");
            return;
            }
        if (validMember(UIArray[4], UIArray[5], UIArray[6])) {
            Member member = member_db.returnMember(
                    new Member(UIArray[4], UIArray[5],
                            new Date(UIArray[6]),
                            new Date(), Location.PISCATAWAY));
            FitnessClass myClass = findClassWithChecks(UIArray[1], UIArray[2]
                    , UIArray[3]);
            if (myClass != null) {
                myClass.dropGuestMember(member);
                ((Family)member).setGuestPassesLeft(((Family)member).getGuestPassesLeft()+1);
            }
        }
    }

    /**
     * Printout the Entire Member Database sorted by Membership Fees using the member to 
     * string method and all the other membership specific information 
     * pertaining to the member like membership level
     */
    private void printMembershipFees() {
        if (member_db.getMlist() == null || member_db.getMlist().length == Empty) {
            System.out.println("Member database is empty!");
            return;
        }
        String memberType = "";
        String remainingPasses = "";
        System.out.println("-list of members with membership fees-");
        for (Member m : member_db.getMlist()) {
            if (m instanceof Premium) {
                memberType = " (Premium)";
                remainingPasses = " Guess-pass remaining:" + ((Premium) m).getGuestPassesLeft();
            } else if (m instanceof Family) {
                memberType = " (Family)";
                remainingPasses = " Guess-pass remaining:" + ((Family) m).getGuestPassesLeft();
            } else {
                memberType = "";
                remainingPasses = "";
            }
            System.out.printf(
                    "%s %s, DOB: %s Membership expired %s, Location: %s,%s%s Membership fee: $%s \n",
                    m.getfname(), m.getlname(), m.getdob(),
                    m.getExpireDate(),
                    m.getLocation(), memberType, remainingPasses, m.membershipFee());
        }
        System.out.println("-end of list-");
    }

    /**
     * Initializes and Adds a premium member to a member database 
     */
    private void addPremiumMember() {
        Family member;
        if (UIArray.length == Invalid_FMember_Len) {
            member = new Premium(
                    capitalize(UIArray[1]),
                    capitalize(UIArray[2]),
                    new Date(UIArray[3]),
                    new Date().addToDate(12),
                    Location.findLocation(UIArray[4].toUpperCase()));
            if (isValid(member)) {
                member_db.add(member);
            } else return;
            System.out.println(String.format("%s %s added.",
                    member.getfname(),
                    member.getlname()));
        } else {
            System.out.println("Incorrect number of Inputs");
        }
    }

    /**
     * Capitalize the first letter of a word and make the rest lower case
     * @param word, the word that needs to be capitalized
     */
    private String capitalize(String word) {
        return word.substring(0, 1).toUpperCase()
                + word.substring(1).toLowerCase();
    }


    /**
     * Adds and initializes a family member to the member database
     */
    public void addFamilyMember() {
        Member member;
        if (UIArray.length == Invalid_PMember_Len) {
            if (!Date.isValid(UIArray[3])) {
                System.out.println(
                        "DOB " + UIArray[3] + ": invalid calendar " +
                                "date!");
                return;
            }
            member = new Family(capitalize(UIArray[1]),
                    capitalize(UIArray[2]),
                    new Date(UIArray[3]),
                   new Date().addToDate(3),
                    Location.findLocation(UIArray[4].toUpperCase()));

            if (isValid(member)) {
                member_db.add(member);

                System.out.println(String.format("%s %s added.",
                        member.getfname(),
                        member.getlname()));
            }
        } else {
            System.out.println("Incorrect Number of Inputs");
        }
    }


    /**
     * Load in a text file line by line and add them to either 
     * the ClassSchedule or to the Member List depending on 
     * the file name which comes from the command that calls
     * this method
     * @param fileName, File that is being loaded
     */
    private void loadFromFile(String fileName) {
        File file = new File(fileName);
        Scanner sc;
        if (fileName.equals("classSchedule.txt"))
            System.out.println("-Fitness classes loaded-");
        else if (fileName.equals("memberList.txt"))
            System.out.println("-list of members loaded-");

        try {
            sc = new Scanner(file);

            while (sc.hasNextLine()) {
                if (fileName.equals("classSchedule.txt")) {
                    loadClassSchedule(sc.nextLine());
                } else if (fileName.equals("memberList.txt")) {
                    loadMemberList(sc.nextLine());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the specified file " +
                    "path, please place file in root of project");
//            throw new RuntimeException(e);
        }
        if (fileName.equals("classSchedule.txt"))
            System.out.println("-end of class list.");
        else if (fileName.equals("memberList.txt"))
            System.out.println("-end of list.");
    }

    /**
     * Add the Class loaded in from text file into the Class Schedule
     *
     * @param schedule, the Class to be added into the Class Schedule
     */
    private void loadClassSchedule(String schedule) {
        String[] classInfo = schedule.split(" ");
        if (classInfo.length != Invalid_ClassInfo_Len) {
            return;
        }

        FitnessClass fc = new FitnessClass(
                Location.findLocation(classInfo[3].toUpperCase()),
                Time.findTime(classInfo[2]), classInfo[1],
                classInfo[0]);

        if (!FitnessClass.allClasses.contains(classInfo[0]))
            FitnessClass.allClasses.add(classInfo[0].toLowerCase());
        if (!FitnessClass.allTeachers.contains(classInfo[1]))
            FitnessClass.allTeachers.add(classInfo[1].toLowerCase());
        if (!FitnessClass.allLocations.contains(classInfo[2]))
            FitnessClass.allLocations.add(classInfo[2].toLowerCase());

        if (findClassWithoutChecks(fc.getClassType(), fc.getTeacher(),
                fc.getLocation().getLocationString()) == null)
            cs.add(fc);
        System.out.printf("%s - %s, %s, %s \n", fc.getClassType(),
                fc.getTeacher().toUpperCase(),
                fc.getTime().getTime(), fc.getLocation());
    }

    /**
     * Add the Member loaded in from text file into the Member
     * Database
     *
     * @param member, the Member to be added into the Member Database
     */
    private void loadMemberList(String member) {
        member = RemoveSpace(member);
        String[] memberInfo = member.split(" ");
        if (memberInfo.length != Invalid_MemberInfo_Len) {
            System.out.println(memberInfo.length);
            return;
        }
        Member newMember = new Member(capitalize(memberInfo[0]),
                capitalize(memberInfo[1]),
                new Date(memberInfo[2]),
                new Date(memberInfo[3]),
                Location.findLocation(memberInfo[4].toUpperCase()));

        if (member_db.Exist(newMember) < None)
            member_db.add(newMember);
        System.out.printf("%s %s, DOB: %s,  Membership expires: %s," +
                        " Location: %s \n", newMember.getfname(),
                newMember.getlname(), newMember.getdob(),
                newMember.getExpireDate(),
                newMember.getLocation().getLocation());
    }

    /**
     * Remove all double spaces and replace them with single spaces
     * @param line, the line that needs tall double spaces removed
     */
    private String RemoveSpace(String line) {
        while (line.indexOf("  ") != Does_Not_Exist) {
            line = line.replace("  ", " ");
        }
        return line;
    }


    /**
     * Printout the Entire Member Database in the order that it is
     * currently
     * in
     */
    private void printDB() {
        if (member_db.getSize() == Empty) {
            System.out.println("Member database is empty!");
        } else {
            System.out.println("-list of members-");
            member_db.print();
            System.out.println("-end of list-");
        }
    }

    /**
     * Printout the Entire Member Database sorted by Last Name and
     * First Name
     */
    private void printByName() {
        if (member_db.getSize() == Empty) {
            System.out.println("Member database is empty!");
        } else {
            System.out.println(
                    "-list of members sorted by last name, and " +
                            "first name-");
            member_db.printByName();
            System.out.println("-end of list-");
        }
    }

    /**
     * Printout the Entire Member Database sorted by County and
     * Zipcodes
     */
    private void printByCounty() {
        if (member_db.getSize() == Empty) {
            System.out.println("Member database is empty!");
        } else {
            System.out.println(
                    "-list of members sorted by county and zipcode-");
            member_db.printByCounty();
            System.out.println("-end of list-");
        }
    }

    /**
     * Printout the Entire Member Database sorted by Expiration Date
     */
    private void printByDate() {
        if (member_db.getSize() == Empty) {
            System.out.println("Member database is Empty!");
        } else {
            System.out.println(
                    "-list of members sorted by membership " +
                            "experation date-");
            member_db.printByExpirationDate();
            System.out.println("-end of list-");
        }
    }

    /**
     * Printout the Entire Fitness Class Schedule
     */
    private void displaySchedule() {
        if (cs.getNumClasses() == Empty) {
            System.out.println("Fitness class schedule is empty.");
            return;
        }
        System.out.println("-Fitness classes-");

        for (FitnessClass f : cs.getClasses()) {
            if (f == null) {
                System.out.println("-end of class list.");
                return;
            }
            System.out.printf("%s - %s, %s, %s \n", f.getClassType(),
                    f.getTeacher(), f.getTime().getTime(),
                    f.getLocation());
            if (f.getClassRoster().size() != Empty || f.getGuestMembers().size() != Empty) {
                printClassMembers(f);
            }
        }
    }

    /**
     * Allows a Member to drop their Pilates, Spinning or Cardio.
     * Ensures that
     * the class the user enters is not case-sensitive, meaning
     * that Pilates
     * and PILATES are the same when searching for the class.
     */
    private void drop() {
        if (UIArray.length != Invalid_Drop_Len) {
            System.out.println("Invalid number of parameters " +
                    "entered");
            return;
        } else if (!Date.isValid(UIArray[6]) || !(new Date(UIArray[6]).isValid()) ) {
            System.out.println(
                    "DOB " + UIArray[6] + ": invalid calendar date!");
            return;
        }

        if (validMember(UIArray[4], UIArray[5], UIArray[6])) {
            Member member = member_db.returnMember(
                    new Member(UIArray[4], UIArray[5],
                            new Date(UIArray[6]),
                            new Date(), Location.PISCATAWAY));
            FitnessClass myClass = findClassWithChecks(UIArray[1], UIArray[2]
                    , UIArray[3]);

            if (myClass != null) {
                myClass.dropMember(member);
            }
        }
    }

    /**
     * Allows any other method in this project to print out a message
     * 
     * @param message the message that we want to print out
     */
    public static void printMessage(String message) {
        System.out.println(message);
    }


    /**
     * This method adds a Member into the Member Database, as long
     * as they
     * follow these conditions: Any date that isn't invalid
     * calendar date, The
     * DOB isn't today or a future date, A Member isn't younger
     * than 18, and an
     * invalid location. Performs the check and then adds the member
     * if they pass these checks
     * In order to maintain backwards compatibility with project 1
     * the method checks if there are 5 or 6 parameters in the command and
     * calls the command accordingly
     * @param today the Date of today to check the DOB of a Member
     */
    private void Add(Date today) {
        if (UIArray.length == Add_Large) {
            Addfor6();
        } else if (UIArray.length == Add_Small) {
            Addfor5(today);
        } else {
            System.out.println("Insufficient amount of input");
        }
    }

    /**
     * This method adds a Member into the Member Database, as long
     * as they
     * follow these conditions: Any date that isn't invalid
     * calendar date, The
     * DOB isn't today or a future date, A Member isn't younger
     * than 18, and an
     * invalid location. Performs the check and then adds the 
     * user if they pass the check
     *
     */
    private void Addfor6() {
        if (UIArray.length == Add_Large && !Date.isValid(UIArray[4])) {
            System.out.println(
                    "Expiration date " + UIArray[4] + ": invalid " +
                            "calendar date!");
            return;
        } else if (!Date.isValid(UIArray[3])) {
            System.out.println(
                    "DOB " + UIArray[3] + ": invalid calendar date!");
            return;
        }
        Member member;

        member = new Member(capitalize(UIArray[1]),
                capitalize(UIArray[2]), new Date(UIArray[3]),
                new Date(UIArray[4]),
                Location.findLocation(UIArray[5].toUpperCase()));

        if (isValid(member)) {
            member_db.add(member);
            System.out.println(String.format("%s %s added.",
                    member.getfname(),
                    member.getlname()));
        }
    }

    /**
     * This method adds a Member into the Member Database, as long
     * as they
     * follow these conditions: Any date that isn't invalid
     * calendar date, The
     * DOB isn't today or a future date, A Member isn't younger
     * than 18, and an
     * invalid location. Performs the check and then adds the user
     *
     * @param today the Date of today to check the DOB of a Member
     */
    private void Addfor5(Date today) {
        if (!Date.isValid(UIArray[3])) {
            System.out.println("DOB " + UIArray[3] + "2: invalid " +
                    "calendar date!");
            return;
        }
        Member member;

        member = new Member(capitalize(UIArray[1]),
                capitalize(UIArray[2]), new Date(UIArray[3]),
                today.addToDate(3),
                Location.findLocation(UIArray[4].toUpperCase()));

        if (isValid(member)) {
            member_db.add(member);
            System.out.println(String.format("%s %s added.",
                    member.getfname(),
                    member.getlname()));
        }
    }

    /**
     * This method checks if Any date that isn't invalid calendar
     * date, The
     * DOB isn't today or a future date, A Member isn't younger
     * than 18, and an
     * invalid location.
     *
     * @param member The Member who's being checked
     * @return true if member passes all checks, otherwise false
     */
    private boolean isValid(Member member) {
        if (!(newValidMemberCheck(member))) {
            return false;
        }
        if ((member_db.Exist(member) != Does_Not_Exist)) {
            System.out.println(
                    UIArray[1] + " " + UIArray[2] + " is already in" +
                            " the database.");
            return false;
        }
        return true;
    }

    /**
     * This method checks a Members DOB and Expiration date are valid calendar
     * dates, DOB is not today or a future date, check if a Member is 18 or
     * older, if a Members location is valid and if the Member is already in the
     * database
     *
     * @param member The Member who's being checked
     * @return true if member passes all checks, otherwise false
     */
    private boolean newValidMemberCheck(Member member) {
        Date today = new Date();
        if (!(member.getdob().isValid())) {
            System.out.println(
                    "DOB " + UIArray[3] + ": invalid calendar date!");
            return false;
        } else if (!member.getExpireDate().isValid()) {
            System.out.println(
                    "Expiration date " + UIArray[4] + ": invalid " +
                            "calendar " + "date!");
            return false;
        } else if (today.compareTo(member.getdob()) <= None) {
            System.out.println(
                    "DOB " + UIArray[3] + ": cannot be today or " +
                            "future date!");
            return false;
        } else if (!(member_db.checkOver18(member))) {
            System.out.println(
                    "DOB " + UIArray[3] + ": must be 18 or older to" +
                            " join!");
            return false;
        }
        if (!member_db.checkLocationExists(member)) {
            System.out.println(UIArray[4] + ": invalid location!");
            return false;
        }
        if ((member_db.Exist(member) != Does_Not_Exist)) {
            System.out.println(
                    UIArray[1] + " " + UIArray[2] + " is already in" +
                            " the database.");
            return false;
        }
        return true;
    }


    /**
     * Check if the member exists inside of the database, in other
     * words, if
     * the member actually exists in the database
     *
     * @param fname the first name of the member we are if they exist
     * @param  lname the last name of the member checking if they exist
     * @param date the dob of the member that we are checking if they exist
     *
     *
     * @return true if Member exist in the Database, false otherwise
     */
    private boolean checkIfMemberExists(String fname, String lname,
                                        String date) {
        if (member_db.Exist(
                new Member(fname, lname, new Date(date), new Date(),
                        Location.PISCATAWAY)) >= None) {
            return true;
        } else return false;
    }

    /**
     * Check if the date is a valid date
     *
     * @param date the date being checked
     * @return true if the date is valid, false otherwise
     */
    private boolean validDate(String date) {
        if (!Date.isValid(date) || !(new Date(date).isValid())) {
            System.out.println(
                    String.format("DOB %s: invalid calendar date!",
                            date));
            return false;
        }
        return true;
    }

    /**
     * Check if a Member is able to be added to a class like
     * Pilates, Spinning
     * or Cardio. Also checks if the member is an actual valid
     * member and can
     * be checked into the class
     *
     * @return true if Member valid to be added to a class, false
     * otherwise
     */
    private boolean validMember(String fname, String lname,
                                String dob) {
        if (checkIfMemberExists(fname, lname, dob)) {
            Member member = member_db.returnMember(
                    new Member(fname, lname,
                            new Date(dob),
                            new Date(), Location.PISCATAWAY));
            if (member.getExpireDate().compareTo(new Date()) == Does_Not_Exist) {
                System.out.println(String.format("%s %s %s " +
                                "membership expired",
                        member.getfname(), member.getlname(),
                        member.getdob().toString()));
                return false;
            } else if (!(member.getdob().isValid())) {
                System.out.println(
                        "DOB " + dob + ": invalid calendar date");
                return false;
            }
            return true;
        } else {
            System.out.println(String.format("%s %s %s is not in " +
                            "the database",
                    fname, lname, dob));
            return false;
        }
    }

    /**
     * Checks the user into either Pilates, Spinning or Cardio
     * class based on
     * what they have inputted.
     */
    private void checkin() {
        if (UIArray.length != Invalid_CheckIn_Len) {
            System.out.println("Invalid number of parameters for this command");
            return;
        }

        if (!validDate(UIArray[6])) {
            return;
        }

        if (findClassWithChecks(UIArray[1], UIArray[2], UIArray[3]) == null) {
            return;
        }
        if (validMember(UIArray[4], UIArray[5], UIArray[6])) {
            Member member = member_db.returnMember(
                    new Member(UIArray[4], UIArray[5],
                            new Date(UIArray[6]),
                            new Date(), Location.PISCATAWAY));

            if (!(member instanceof Family)) {
                if (member.getLocation() == Location.findLocation(UIArray[3].toUpperCase())) {
                    FitnessClass myClass = findClassWithChecks(UIArray[1],
                            UIArray[2], UIArray[3]);
                    myClass.addMember(member);
                } else
                    System.out.printf("%s %s checking in %s - standard membership location restriction.\n",
                            member.getfname(), member.getlname(), Location.findLocation(UIArray[3].toUpperCase()).getLocation());

            } else {
                findClassWithChecks(UIArray[1], UIArray[2], UIArray[3]).addMember(member);
            }
        }
    }

    /**
     * Remove a Member from the database and makes sure that they
     * are actually
     * inside the database before removing them
     *
     * @param today is just today's date
     */
    private void Remove(Date today) {
        if (UIArray.length != Invalid_Remove_Len) {
            System.out.println("Invalid number of parameters");
            return;
        } else if (!Date.isValid(UIArray[3])) {
            System.out.println(
                    "DOB " + UIArray[3] + ": invalid calendar date!");
            return;
        }

        Member member = new Member(capitalize(UIArray[1]),
                capitalize(UIArray[2]), new Date(UIArray[3]), today,
                Location.findLocation("PISCATAWAY"));
        if ((member_db.Exist(member) == Does_Not_Exist)) {
            System.out.println(
                    member.getfname() + " " + member.getlname() +
                            " is not " + "in the database.");
            return;
        }
        if (member_db.remove(member)) System.out.println(
                String.format("%s %s removed.", member.getfname(),
                        member.getlname()));
    }

    /**
     * Printout all Members in a Fitness Class with their own toString
     * method.
     */
    public static void printClassMembers(FitnessClass fc) {
        if (fc.getClassRoster() != null && fc.getClassRoster().size() > Empty) {
            System.out.println("- Participants -");
            for (Member m : fc.getClassRoster()) {
                System.out.println(m.toString());
            }
        }
        if (fc.getGuestMembers() != null && fc.getGuestMembers().size() > Empty) {
            System.out.println("- Guests -");
            for (Member m : fc.getGuestMembers()) {
                System.out.println(m.toString());
            }
        }
    }
}
