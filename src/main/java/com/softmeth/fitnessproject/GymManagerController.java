package com.softmeth.fitnessproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * The Controller class part of the MVC design pattern. This will
 * handle all the button on click events and will perform the necessary
 * checks to determine which methods should be called. Upon determination
 * it will call the corresponding model to create/alter that data
 *
 * @author Peter Chen, Jonathon Lopez
 */
public class GymManagerController {

    MemberDatabase member_db = new MemberDatabase();
    ;

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
    private static Date today = new Date();


    private static ClassSchedule cs = new ClassSchedule();

    @FXML
    private TextField memberFirstName, MemberFirstName, MemberLastName, MemberLocation, GuestMemberFirstName,
            GuestMemberLastName, GuestMemberFitnessClassType, CheckinMemberFitnessClassType,
            CheckinMemberFitnessClassInstructor, CheckinMemberFitnessClassLocation,
            GuestMemberLocation, GuestMemberFitnessClassInstructor, GuestMemberFitnessClassLocation,
            CheckinMemberFirstName, CheckinMemberLastName, CheckinMemberLocation, MemberFitnessClassLocation;

    @FXML
    private TextArea MemberTabOutput, GuestMemberTabOutput, MemberCheckinOutput, LoadDataOutput;


    @FXML
    private TextArea ViewDBOutput;
    private static TextArea DBOutput;
    @FXML
    private DatePicker MemberBirthday, GuestMemberBirthday, CheckinMemberBirthday;

    @FXML
    private ToggleGroup membershipType;

    /**
     * This is essentially the print function
     * It will determine where to display messages to
     * based on the textarea that is passed in
     *
     * @param textArea the textarea to display the message in
     * @param Message  The message to write to the textarea
     */
    public static void appendToTextArea(TextArea textArea, String Message) {
        String oldText;
        if (textArea == null){
            oldText = "";
        }else oldText= textArea.getText().trim() + "\n";
        textArea.setText(oldText + Message + "\n");
    }

    /**
     * This is very similar to the append to text area
     * function except that it is linked to the output textarea
     * in the view database tab.
     *
     * @param Message  The message to write to the textarea
     */
    public static void appendToDbView(String Message) {
        String oldText = DBOutput.getText().trim() + "\n";
        DBOutput.setText(oldText + Message + "\n");
    }


    /**
     * Find a fitness class without outputting checks if its valid
     *
     * @param classType  String of the Type of Class
     * @param instructor String of the Instructor of a Class
     * @param location   String of the Location of a Class
     * @return Fitness Class if found, otherwise null
     */
    private FitnessClass findClassWithoutChecks(String classType,
                                                String instructor,
                                                String location) {
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
     * @param classType  String of the Type of Class
     * @param instructor String of the Instructor of a Class
     * @param location   String of the Location of a Class
     * @return Fitness Class if found and passes all checks
     */
    private FitnessClass findClassWithChecks(String classType,
                                             String instructor,
                                             String location,
                                             TextArea printTo) {
        FitnessClass fitnessClass = findClassWithoutChecks(classType, instructor, location);

        if (!FitnessClass.allClasses.contains(classType.toLowerCase()))
            appendToTextArea(printTo, classType + " - class does not exist.");
        else if (!FitnessClass.allTeachers.contains(instructor.toLowerCase()))
            appendToTextArea(printTo, instructor + " - instructor does not exist.");
        else if (Location.findLocation(location.toUpperCase()) == Location.INVALID)
            appendToTextArea(printTo, location + " - invalid location.");
        else if (fitnessClass == null)
            appendToTextArea(printTo, String.format("%s by %s does not exist at %s\n", classType, instructor,
                    location));

        return fitnessClass;
    }


    /**
     * Printout the Entire Member Database sorted by Membership Fees using the member to
     * string method and all the other membership specific information
     * pertaining to the member like membership level
     */
    private void printMembershipFees() {
        if (member_db.getMlist() == null || member_db.getMlist().length == Empty) {
            appendToDbView("Member database is empty!");
            return;
        }
        String memberType = "";
        String remainingPasses = "";
        appendToDbView("-list of members with membership fees-");
        for (int i = 0; i < member_db.getSize(); i ++) {
            Member m = member_db.getMlist()[i];
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
            appendToDbView(String.format(
                    "%s %s, DOB: %s Membership expired %s, Location: %s,%s%s Membership fee: $%s \n",
                    m.getfname(), m.getlname(), m.getdob(),
                    m.getExpireDate(),
                    m.getLocation(), memberType, remainingPasses, m.membershipFee()));
        }
        appendToDbView("-end of list- \n");
    }

    /**
     * Initializes and Adds a premium member to a member database
     *
     * @param fname first name of the premium member
     * @param lname last name of the premium member
     * @param dob date of birth of the premium member
     * @param location location of the premium member's membership
     */
    private void addPremiumMember(String fname, String lname, String dob, String location) {
        Family member;
        member = new Premium(
                capitalize(fname),
                capitalize(lname),
                new Date(dob),
                new Date().addToDate(12),
                Location.findLocation(location.toUpperCase()));
        if (isValid(member)) {
            member_db.add(member);
        } else return;
        appendToTextArea(MemberTabOutput, String.format("%s %s added.",
                member.getfname(),
                member.getlname()));
    }

    /**
     * Capitalize the first letter of a word and make the rest lower case
     *
     * @param word, the word that needs to be capitalized
     */
    private String capitalize(String word) {
        return word.substring(0, 1).toUpperCase()
                + word.substring(1).toLowerCase();
    }


    /**
     * Adds and initializes a family member to the member database
     *
     * @param fname the first name of the family member to add
     * @param lname the last name of the fmaily member to add
     * @param dob the date of birth of the fmaily member to add
     * @param location location of the fmaily member's membership
     */
    public void addFamilyMember(String fname, String lname, String dob, String location) {
        Member member;
        if (!Date.isValid(dob)) {
            appendToTextArea(MemberTabOutput, "DOB " + dob + ": invalid calendar date!");
            return;
        }
        member = new Family(capitalize(fname),
                capitalize(lname),
                new Date(dob),
                new Date().addToDate(3),
                Location.findLocation(location.toUpperCase()));

        if (isValid(member)) {
            member_db.add(member);

            appendToTextArea(MemberTabOutput, String.format("%s %s added.",
                    member.getfname(),
                    member.getlname()));
        }

    }



    /**
     * Remove all double spaces and replace them with single spaces
     *
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
            appendToTextArea(ViewDBOutput, "Member database is empty!");
        } else {
            appendToTextArea(ViewDBOutput, "-list of members-");
            member_db.print();
            appendToTextArea(ViewDBOutput, "-end of list- \n");
        }
    }

    /**
     * Printout the Entire Member Database sorted by Last Name and
     * First Name
     */
    private void printByName() {
        if (member_db.getSize() == Empty) {
            appendToTextArea(ViewDBOutput, "Member database is empty!");
        } else {
            appendToTextArea(ViewDBOutput, "-list of members sorted by last name, and " +
                    "first name-");

            member_db.printByName();
            appendToTextArea(ViewDBOutput, "-end of list- \n");
        }
    }

    /**
     * Printout the Entire Member Database sorted by County and
     * Zipcodes
     */
    private void printByCounty() {
        if (member_db.getSize() == Empty) {
            appendToDbView("Member database is empty!");
        } else {
            appendToDbView(
                    "-list of members sorted by county and zipcode-");
            member_db.printByCounty();
            appendToDbView("-end of list- \n");
        }
    }

    /**
     * Printout the Entire Member Database sorted by Expiration Date
     */
    private void printByDate() {
        if (member_db.getSize() == Empty) {
            appendToDbView("Member database is Empty!");
        } else {
            appendToDbView(
                    "-list of members sorted by membership " +
                            "experation date-");
            member_db.printByExpirationDate();
            appendToDbView("-end of list-\n ");
        }
    }

    /**
     * Printout the Entire Fitness Class Schedule
     */
    private void displaySchedule() {
        if (cs.getNumClasses() == Empty) {
            appendToDbView("Fitness class schedule is empty.");
            return;
        }
        appendToDbView("-Fitness classes-");

        for (FitnessClass f : cs.getClasses()) {
            if (f == null) {
                appendToDbView("-end of class list. \n");
                return;
            }
            appendToDbView(String.format("%s - %s, %s, %s \n", f.getClassType(),
                    f.getTeacher(), f.getTime().getTime(),
                    f.getLocation()));
            if (f.getClassRoster().size() != Empty || f.getGuestMembers().size() != Empty) {
                printClassMembers(f, ViewDBOutput);
            }
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
     * @param fname    Member's First Name
     * @param lname    Member's Last Name
     * @param dob      Member's Date of Birth
     * @param location Member's Location
     */
    private void Add(String fname, String lname, String dob, String location) {
        if (!Date.isValid(dob)) {
            appendToTextArea(MemberTabOutput, "DOB " + dob + "2: invalid " +
                    "calendar date!");
            return;
        }
        Member member;

        member = new Member(capitalize(fname),
                capitalize(lname), new Date(dob),
                today.addToDate(3),
                Location.findLocation(location.toUpperCase()));

        if (isValid(member)) {
            member_db.add(member);
            appendToTextArea(MemberTabOutput, String.format("%s %s added.",
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
            appendToTextArea(MemberCheckinOutput,
                    member.getfname() + " " + member.getlname() + " is already in" +
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
        if (!(member.getdob().isValid())) {
            appendToTextArea(MemberTabOutput,
                    "DOB " + member.getdob().toString() + ": invalid calendar date!");
            return false;
        } else if (!member.getExpireDate().isValid()) {
            appendToTextArea(MemberTabOutput,
                    "Expiration date " + member.getExpireDate().toString() + ": invalid " +
                            "calendar " + "date!");
            return false;
        } else if (today.compareTo(member.getdob()) <= None) {
            appendToTextArea(MemberTabOutput,
                    "DOB " + member.getdob().toString() + ": cannot be today or " +
                            "future date!");
            return false;
        } else if (!(member_db.checkOver18(member))) {
            appendToTextArea(MemberTabOutput,
                    "DOB " + member.getdob().toString() + ": must be 18 or older to" +
                            " join!");
            return false;
        }
        if (!member_db.checkLocationExists(member)) {
            appendToTextArea(MemberTabOutput, member.getLocation().toString() + ": invalid location!");
            return false;
        }
        if ((member_db.Exist(member) != Does_Not_Exist)) {
            appendToTextArea(MemberTabOutput,
                    member.getfname() + " " + member.getlname() + " is already in" +
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
     * @param lname the last name of the member checking if they exist
     * @param date  the dob of the member that we are checking if they exist
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
            appendToTextArea(MemberCheckinOutput,
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
                                String dob, TextArea outputWriteTo) {
        if (checkIfMemberExists(fname, lname, dob)) {
            Member member = member_db.returnMember(
                    new Member(fname, lname,
                            new Date(dob),
                            new Date(), Location.PISCATAWAY));
            if (member.getExpireDate().compareTo(new Date()) == Does_Not_Exist) {
                appendToTextArea(outputWriteTo, String.format("%s %s %s " +
                                "membership expired",
                        member.getfname(), member.getlname(),
                        member.getdob().toString()));
                return false;
            } else if (!(member.getdob().isValid())) {
                appendToTextArea(outputWriteTo, "DOB " + dob + ": invalid calendar date");
                return false;
            }
            return true;
        } else {
            appendToTextArea(outputWriteTo, String.format("%s %s %s is not in " +
                            "the database",
                    fname, lname, dob));
            return false;
        }
    }

    /**
     * Remove a Member from the database and makes sure that they
     * are actually
     * inside the database before removing them
     *
     * @param fname Member's First Name
     * @param lname Member's Last Name
     * @param dob   Member's Date of Birth
     */
    private void Remove(String fname, String lname, String dob, String location) {
        if (!Date.isValid(dob)) {
            appendToTextArea(MemberTabOutput, "DOB " + dob + ": invalid calendar date!");
            return;
        }

        Member member = new Member(capitalize(fname),
                capitalize(lname), new Date(dob), today,
                Location.findLocation(location));
        if ((member_db.Exist(member) == Does_Not_Exist)) {
            appendToTextArea(MemberTabOutput,
                    member.getfname() + " " + member.getlname() +
                            " is not " + "in the database.");
            return;
        }
        if (member_db.remove(member)) appendToTextArea(MemberTabOutput,
                String.format("%s %s removed.", member.getfname(),
                        member.getlname()));
    }

    /**
     * Printout all Members in a Fitness Class with their own toString
     * method.
     */
    public static void printClassMembers(FitnessClass fc, TextArea printTo) {
        if (fc.getClassRoster() != null && fc.getClassRoster().size() > Empty) {
            appendToTextArea(printTo,"- Participants -");
            for (Member m : fc.getClassRoster()) {
                appendToTextArea(printTo,m.toString());
            }
        }
        if (fc.getGuestMembers() != null && fc.getGuestMembers().size() > Empty) {
            appendToTextArea(printTo,"- Guests -");
            for (Member m : fc.getGuestMembers()) {
                appendToTextArea(printTo,m.toString());
            }
        }
    }

    @FXML
    /**
     * Event Handler for the add members button
     *  @param event the event that triggered the function
     */
    void addMember(ActionEvent event) {
        try {
            if (MemberBirthday.getValue() == null || ((RadioButton) membershipType.getSelectedToggle()) == null) {
                appendToTextArea(MemberTabOutput, "Must fill out all fields correctly");
                return;
            }

            String fname = MemberFirstName.getText().trim();
            String lname = MemberLastName.getText().trim();
            String dob = MemberBirthday.getValue().toString().trim();
            String location = MemberLocation.getText().trim();
            String typeOfMembership = ((RadioButton) membershipType.getSelectedToggle()).getText().trim();

            dob = ConvertDate(dob);
            if (fname.equals("") || lname.equals("") || dob.equals("") || location.equals("") || typeOfMembership.equals("")) {
                appendToTextArea(MemberTabOutput, "Must fill out all fields correctly");
                return;
            }
            if (typeOfMembership.contains("Premium")) {
                addPremiumMember(fname, lname, dob, location);
            } else if (typeOfMembership.contains("Family")) {
                addFamilyMember(fname, lname, dob, location);
            } else
                Add(fname, lname, dob, location);

        } catch (Exception e) {
            appendToTextArea(MemberTabOutput, "Exception encountered " + e.toString());
        }
    }

    private String ConvertDate(String date) {
        String[] allDates = date.split("-");
        return allDates[1] + "/"+ allDates[2] + "/"+allDates[0];
    }

    @FXML
    /**
     * Event Handler for the add members button
     *  @param event the event that triggered the function
     */
    void removeMember(ActionEvent event) {
        try {
            if (MemberBirthday.getValue() == null || ((RadioButton) membershipType.getSelectedToggle()) == null) {
                appendToTextArea(MemberTabOutput, "Must fill out all fields");
                return;
            }

            String fname = MemberFirstName.getText().trim();
            String lname = MemberLastName.getText().trim();
            String dob = MemberBirthday.getValue().toString().trim();
            String location = MemberLocation.getText().trim();
            String typeOfMembership = ((RadioButton) membershipType.getSelectedToggle()).getText().trim();
            dob = ConvertDate(dob);
            if (fname.equals("") || lname.equals("") || dob.equals("") || location.equals("") || typeOfMembership.equals("")) {
                appendToTextArea(MemberTabOutput, "Must fill out all fields");
                return;
            }

            Remove(fname, lname, dob, location);

        } catch (Exception e) {
            appendToTextArea(MemberTabOutput, "Exception Encountered " + e.toString());
        }
    }

    @FXML
    /**
     * Event Handler for the View all members in database button
     *  @param event the event that triggered the function
     */
    void viewAllMembersInDB(ActionEvent event) {
        DBOutput =  this.ViewDBOutput;
        printDB();
    }

    @FXML
    /**
     * Event Handler for the sorted by name view all members in database button
     *  @param event the event that triggered the function
     */
    void viewAllMembersByName(ActionEvent event) {
        DBOutput =  this.ViewDBOutput;
        printByName();
    }

    @FXML
    /**
     * Event Handler for the sorted by county view all members in database button
     *  @param event the event that triggered the function
     */
    void viewAllMembersByCounty(ActionEvent event) {
        DBOutput =  this.ViewDBOutput;
        printByCounty();
    }

    @FXML
    /**
     * Event Handler for the sorted by membership expiration date
     * view all members in database button
     *  @param event the event that triggered the function
     */
    void viewAllMembersByDate(ActionEvent event) {
        DBOutput =  this.ViewDBOutput;
        printByDate();
    }

    @FXML
    /**
     * Event Handler for the button to show all classes being held
     * as well as all of the participants in them
     *  @param event the event that triggered the function
     */
    void displayScheduleForAllClasses(ActionEvent event) {
        DBOutput =  this.ViewDBOutput;
        displaySchedule();
    }

    @FXML
    /**
     * Event Handler for the button to load all the members
     * from the memberlist file and add them to the database
     * 
     */
    void loadMemberList(ActionEvent event) {
        String result = "";
        result = "-list of members loaded- \n" + member_db.loadFromFile();
        appendToTextArea(LoadDataOutput, result);

    }

    @FXML
    /**
     * Event Handler for the button to load all the classes
     * from the classschedule file and add them to the database
     * *  @param event the event that triggered the function
     */
    void loadClassScheduleList(ActionEvent event) {
        appendToTextArea(LoadDataOutput, "-Fitness classes loaded-");

        String result = "-end of class list. \n" + cs.loadFromFile();

        appendToTextArea(LoadDataOutput, result);


    }

    @FXML
    /**
     * Event Handler for button to show all members and their fees
     * @param event the event that triggered the function
     */
    void viewAllMembersByFees(ActionEvent event) {
        DBOutput =  this.ViewDBOutput;
        printMembershipFees();
    }

    @FXML
    /**
     * Checks a Guest Member into a Class and handles the related exceptions
     * Event Handler for the Guest Member Checkin Button
     * @param event the event that triggered the function
     */
    void guestCheckIn(ActionEvent event) {
        try {
            if (GuestMemberBirthday.getValue() == null) {
                appendToTextArea(GuestMemberTabOutput, "Please fill out all fields correctly");
                return;
            }
            String fname = GuestMemberFirstName.getText().trim();
            String lname = GuestMemberLastName.getText().trim();
            String dob = GuestMemberBirthday.getValue().toString().trim();
            String fcclass = GuestMemberFitnessClassType.getText().trim();
            String fcinstructor = GuestMemberFitnessClassInstructor.getText().trim();
            String fclocation = GuestMemberFitnessClassLocation.getText().trim();
            dob = ConvertDate(dob);
            if (fname.equals("") || lname.equals("") || dob.equals("") || fcclass.equals("") || fcinstructor.equals("") || fclocation.equals("")) {
                appendToTextArea(GuestMemberTabOutput, "Please fill out all fields correctly");
                return;
            }
            if (validMember(fname, lname, dob, GuestMemberTabOutput)) {
                Member member = member_db.returnMember( new Member(fname, lname,new Date(dob),new Date(), Location.PISCATAWAY));
                if (member instanceof Family) {
                    if (((Family) member).getGuestPassesLeft() > None) {
                        if (Location.findLocation(
                                fclocation.toUpperCase()) == member.getLocation()) {
                            if (findClassWithChecks(fcclass, fcinstructor,fclocation, GuestMemberTabOutput) != null) {
                                ((Family) member).setGuestPassesLeft(((Family) member).getGuestPassesLeft() - 1);
                                FitnessClass myClass =findClassWithoutChecks(fcclass, fcinstructor,fclocation);
                                myClass.getGuestMembers().add(member);
                                appendToTextArea(GuestMemberTabOutput, String.format("%s %s (guest) checked in %s- %s, %s, %s \n",
                                        member.getfname(), member.getlname(), myClass.getClassType(),myClass.getTeacher(), myClass.getTime().getTime(),
                                        myClass.getLocation().getLocationString()));
                                printClassMembers(myClass, GuestMemberTabOutput);
                            } else
                                appendToTextArea(GuestMemberTabOutput, String.format("Class does not exist"));
                        } else
                            appendToTextArea(GuestMemberTabOutput, String.format("%s %s Guest checking in %s- guest location restriction. \n",
                                    member.getfname(), member.getlname(), Location.findLocation(fclocation.toUpperCase()).getLocation()));
                    } else appendToTextArea(GuestMemberTabOutput, String.format("%s %s ran out of guest pass. \n",member.getfname(), member.getlname()));
                } else appendToTextArea(GuestMemberTabOutput,"Standard membership - guest check-in is not allowed.");
            }
        } catch (Exception e) {appendToTextArea(GuestMemberTabOutput, e.toString());}
    }

    @FXML
    /**
     * Check a Guest Member out of a Class if they are in
     * the class and handles related exceptions
     * Event Handler for the Guest Member Checkout
     *  @param event the event that triggered the function
     */
    void guestCheckout(ActionEvent event) {
        try {
            if (GuestMemberBirthday.getValue() == null) {
                appendToTextArea(GuestMemberTabOutput, "Please fill out all fields correctly");
                return;
            }
            String fname = GuestMemberFirstName.getText().trim();
            String lname = GuestMemberLastName.getText().trim();
            String dob = GuestMemberBirthday.getValue().toString().trim();
            String fcclass = GuestMemberFitnessClassType.getText().trim();
            String fcinstructor = GuestMemberFitnessClassInstructor.getText().trim();
            String fclocation = GuestMemberFitnessClassLocation.getText().trim();
            dob = ConvertDate(dob);
            if (fname.equals("") || lname.equals("") || dob.equals("") || fcclass.equals("") || fcinstructor.equals("") || fclocation.equals("")) {
                appendToTextArea(GuestMemberTabOutput, "Please fill out all fields correctly");
                return;
            }
            if (validMember(fname, lname, dob, GuestMemberTabOutput)) {
                Member member = member_db.returnMember(
                        new Member(fname, lname,
                                new Date(dob),
                                new Date(), Location.PISCATAWAY));
                FitnessClass myClass = findClassWithChecks(fcclass, fcinstructor
                        , fclocation, GuestMemberTabOutput);
                if (myClass != null) {
                    String output = myClass.dropGuestMember(member);
                    appendToTextArea(GuestMemberTabOutput, output);
                    if (output.contains("done"))
                        ((Family) member).setGuestPassesLeft(((Family) member).getGuestPassesLeft() + 1);
                }
            }
        } catch (Exception e) {
            appendToTextArea(GuestMemberTabOutput, e.toString());
        }
    }

    @FXML
    /**
     * Checks the user into either Pilates, Spinning or Cardio
     * class based on
     * what they have inputted.
     * Event Handler for the Member Checkin Button
     *  @param event the event that triggered the function
     */
    void memberCheckIn(ActionEvent event) {
        try {
            if (CheckinMemberBirthday.getValue() == null) {
                appendToTextArea(MemberCheckinOutput, "Please fill out all fields correctly");
                return;
            }
            String fname = CheckinMemberFirstName.getText().trim();
            String lname = CheckinMemberLastName.getText().trim();
            String dob = CheckinMemberBirthday.getValue().toString().trim();
            String fcclass = CheckinMemberFitnessClassType.getText().trim();
            String fcinstructor = CheckinMemberFitnessClassInstructor.getText().trim();
            String fclocation = CheckinMemberFitnessClassLocation.getText().trim();
            dob = ConvertDate(dob);
            if (fname.equals("") || lname.equals("") || dob.equals("") || fcclass.equals("") || fcinstructor.equals("") || fclocation.equals("")) {
                appendToTextArea(MemberCheckinOutput, "Please fill out all fields correctly");
                return;
            }
            if (!validDate(dob)) {
                return;
            }

            if (findClassWithChecks(fcclass, fcinstructor, fclocation, MemberCheckinOutput) == null) {
                return;
            }
            if (validMember(fname, lname, dob, MemberCheckinOutput)) {
                Member member = member_db.returnMember(
                        new Member(fname, lname,
                                new Date(dob),
                                new Date(), Location.PISCATAWAY));

                if (!(member instanceof Family)) {
                    if (member.getLocation() == Location.findLocation(fclocation.toUpperCase())) {
                        FitnessClass myClass = findClassWithChecks(fcclass,
                                fcinstructor, fclocation, MemberCheckinOutput);
                        String output = myClass.addMember(member);
                        appendToTextArea(MemberCheckinOutput, output);
                        if(output.contains("successfully"))
                            printClassMembers(myClass, MemberCheckinOutput);
                    } else
                        appendToTextArea(MemberCheckinOutput, String.format("%s %s checking in %s - standard " +
                                        "membership location restriction.\n",
                                member.getfname(), member.getlname(),
                                Location.findLocation(fclocation.toUpperCase()).getLocation()));

                } else {
                    String output = findClassWithChecks(fcclass, fcinstructor, fclocation, MemberCheckinOutput).addMember(member);
                    appendToTextArea(MemberCheckinOutput, output);
                    if(output.contains("successfully"))
                        printClassMembers(findClassWithChecks(fcclass, fcinstructor, fclocation, MemberCheckinOutput), MemberCheckinOutput);
                }
            }
        } catch (Exception e) {
            appendToTextArea(MemberCheckinOutput, e.toString());
        }
    }

    @FXML
    /**
     * Allows a Member to drop their Pilates, Spinning or Cardio.
     * Ensures that
     * the class the user enters is not case-sensitive, meaning
     * that Pilates
     * and PILATES are the same when searching for the class.
     * Event Handler for Member Checkout Button
     *  @param event the event that triggered the function
     */
    void memberCheckOut(ActionEvent event) {
        try {
            if (CheckinMemberBirthday.getValue() == null) {
                appendToTextArea(MemberCheckinOutput, "Please fill out all fields correctly");
                return;
            }
            String fname = CheckinMemberFirstName.getText().trim();
            String lname = CheckinMemberLastName.getText().trim();
            String dob = CheckinMemberBirthday.getValue().toString().trim();
            String fcclass = CheckinMemberFitnessClassType.getText().trim();
            String fcinstructor = CheckinMemberFitnessClassInstructor.getText().trim();
            String fclocation = CheckinMemberFitnessClassLocation.getText().trim();
            dob = ConvertDate(dob);
            if (fname.equals("") || lname.equals("") || dob.equals("") || fcclass.equals("") || fcinstructor.equals("") || fclocation.equals("")) {
                appendToTextArea(MemberCheckinOutput, "Please fill out all fields correctly");
                return;
            }
            if (!Date.isValid(dob) || !(new Date(dob).isValid())) {
                appendToTextArea(MemberCheckinOutput,
                        "DOB " + dob + ": invalid calendar date!");
                return;
            }

            if (validMember(fname, lname, dob, MemberCheckinOutput)) {
                Member member = member_db.returnMember(
                        new Member(fname, lname,
                                new Date(dob),
                                new Date(), Location.PISCATAWAY));
                FitnessClass myClass = findClassWithChecks(fcclass, fcinstructor
                        , fclocation, MemberCheckinOutput);

                if (myClass != null) {
                    String output = myClass.dropMember(member);
                    appendToTextArea(MemberCheckinOutput, output);
                }
            }
        } catch (Exception e) {
            appendToTextArea(GuestMemberTabOutput, e.toString());
        }
    }
}
