package com.softmeth.fitnessproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 This class creates a Database full of a collection of Members
 Database to Add, Remove, Sort the Member's in it and Print the whole thing.
 While also checking if the Members Location is valid and if they are 18 and
 Over
 @author Peter Chen, Jonathan Lopez
 */
public class MemberDatabase {

    private Member[] mlist;
    private int size;
    private static int minimumAge=18;
    private static int invalid=-1;
    private static int zero=0;
    private static int Does_Not_Exist = -1;
    private static int Invalid_ClassInfo_Len = 4;
    private static int Invalid_MemberInfo_Len = 5;
    private static int None = 0;

    /**
     Finds the Member inside the Member Database
     @param member the Member to be found
     @return index of the Member if their found in the Database
             Otherwise-1 if the Database is empty or if the Member isn't found
     */
    private int find(Member member) {
        if (mlist == null) {
            return -1;
        }
        for (int i = 0; i < this.size; i++) {
            if (member.compareTo(mlist[i]) == zero) {
                return i;
            }
        }
        return -1;
    }

    /**
     See if the Member exists in the Database
     @param member the Member to be found in the Database
     @return index of the Member if their found in the Database
             Otherwise-1 if the Database is empty or if the Member isn't found
     */
    public int Exist(Member member) {
        return find(member);
    }

    /**
     Find if the Member is in the Datbase and send it back
     @param member the Member to be looked for
     @return Member that is equal to the parameter in th Database, Otherwise
             return a new Member if not found
     */
    public Member returnMember(Member member) {
        for (int i = 0; i < this.size; i++) {
            if (member.compareTo(mlist[i]) == zero) {
                return mlist[i];
            }
        }
        return new Member();
    }

    /**
     Grow the Database size by creating a new Database and copying over all
     the information of the old Database
     */
    private void grow() {
        Member[] newList = new Member[this.size + 1];
        for (int counter = 0; counter < this.size; counter++) {
            newList[counter] = this.mlist[counter];
        }

        this.mlist = newList;
    }

    /**
     Check whether a Members Location Exists or is a non-exist Location
     @param member the Members Location being checked
     @return true if the Member's Location is valid and existing, Otherwise
             false if the location is invalid or doesn't exist
     */
    public boolean checkLocationExists(Member member) {
        Location[] allLocations = Location.values();
        for (Location l : allLocations) {
            if (l == member.getLocation()) {
                return l != Location.INVALID;
            }
        }
        return false;
    }

    /**
     Check whether a Members age is 18 or over
     @param member the Members being checked to be 18 or over
     @return true if the Member's age is 18 or over, Otherwise false if the
             Member's age is under 18
     */
    public boolean checkOver18(Member member) {
        Date today = new Date();
        int ageYear = today.getYear() - member.getdob().getYear();
        int ageMonth = today.getMonth() - member.getdob().getMonth();
        int ageDay = today.getDay() - member.getdob().getDay();
        if (ageYear > minimumAge) {
            return true;
        } else if (ageYear == minimumAge) {
            if (ageMonth < zero) {
                return false;
            } else if (ageMonth > zero)
                return true;
            else if (ageDay >= zero)
                return true;
            else
                return false;
        }
            return false;
    }
    // returns true if member successfully added and false if member is under 18
    
    /**
     Add a Member into the Database
     @param member the Members that is being added
     @return true if the Member is added, Otherwise false
     */
    public boolean add(Member member) {
        if (member.getdob().isValid() && member.getExpireDate().isValid() && checkLocationExists(member) && checkOver18(member)) {
            if (this.mlist == null) {
                this.mlist = new Member[1];
                this.size = 1;
                this.mlist[size - 1] = member;
            } else if (size == this.mlist.length) {
                grow();
                this.size++;
                this.mlist[size - 1] = member;
            } else {
                size++;
                this.mlist[size - 1] = member;
            }
            return true;
        } else
            return false;
    }

    /**
     Remove a Member from the Database
     @param member the Members that is being removed
     @return true if the Member is removed, Otherwise false
     */
    public boolean remove(Member member) {
        int memberIndex = find(member);
        Member[] newArray = new Member[mlist.length];
        int j = 0;
        if (memberIndex == invalid) {
            return false;
        } else {
            for (int i = 0; i < this.size; i++) {
                if (i != memberIndex) {
                    newArray[j] = this.mlist[i];
                    j++;
                }
            }
        }
        size--;
        this.mlist = newArray;
        return true;
    }

    /**
     Printout the Entire Database
     */
    public void print() {
        for (int i = 0; i < this.size; i++) {
            GymManagerController.appendToDbView(mlist[i].toString());
        }
    } //print the array contents as is

    /**
     Printout the Entire Database sorted by County and Zipcode
     */
    public void printByCounty() {
        countySort();
        for (int ind = 0; ind < this.size; ind++) {
            GymManagerController.appendToDbView(this.mlist[ind].toString());
        }
    } //sort by county and then zipcode

    /**
     Sort the Entire Database by County and Zipcode
     */
    private void countySort() {
        for (int i = 0; i < this.size - 1; i++)
            for (int j = 0; j < this.size - i - 1; j++)
                if (this.mlist[j].getLocation().compare(this.mlist[j + 1].getLocation()) > zero) {
                    // swap arr[j+1] and arr[j]
                    Member temp = this.mlist[j];
                    this.mlist[j] = this.mlist[j + 1];
                    this.mlist[j + 1] = temp;
                }
    }

    /**
     Printout the Entire Database sorted by Expiration Date
     */
    public void printByExpirationDate() {
        dateSort();
        for (int ind = 0; ind < this.size; ind++) {
            GymManagerController.appendToDbView(this.mlist[ind].toString());
        }
    } //sort by the expiration date

    /**
     Sort the Entire Database by Expiration Date
     */
    private void dateSort() {
        for (int i = 0; i < this.size - 1; i++)
            for (int j = 0; j < this.size - i - 1; j++)
                if (this.mlist[j].getExpireDate().compareTo(this.mlist[j + 1].getExpireDate()) > zero) {
                    // swap arr[j+1] and arr[j]
                    Member temp = this.mlist[j];
                    this.mlist[j] = this.mlist[j + 1];
                    this.mlist[j + 1] = temp;
                }
    }

    /**
     Printout the Entire Database sorted by Last Name and First Name
     */
    public void printByName() {
        nameSort();
        for (int ind = 0; ind < this.size; ind++) {
            GymManagerController.appendToDbView(this.mlist[ind].toString());
        }
    } //sort by last name and then first name

    /**
     Sort the Entire Database by Last Name and First Name
     */
    private void nameSort() {
        for (int i = 0; i < this.size - 1; i++)
            for (int j = 0; j < this.size - i - 1; j++)
                if (this.mlist[j].compareTo(this.mlist[j + 1]) > zero) {
                    // swap arr[j+1] and arr[j]
                    Member temp = this.mlist[j];
                    this.mlist[j] = this.mlist[j + 1];
                    this.mlist[j + 1] = temp;
                }
    }
    
    /**
     Return the size of the entire Database
     @return Size of the Member Database
     */
    public int getSize(){
        return this.size;
    }

    /**
     Return the entire Member Database
     @return The Member Database
     */
    public Member[] getMlist(){
        return this.mlist;
    }


    /**
     * Load in a text file line by line and add them to either
     * the ClassSchedule or to the Member List depending on
     * the file name which comes from the command that calls
     * this method
     *
     * @return message, Returns the message of whether file is successfully
     * loaded
     */
    public String loadFromFile() {
        File file = new File("memberList.txt");
        Scanner sc;
        String message = "";
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                 message = message + loadMembersList(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            return "Could not find the specified file " +
                    "path, please place file in root of project";
        }
            message = message + "-end of list. \n";
        return message;
    }
    /**
     * Add the Member loaded in from text file into the Member
     * Database
     *
     * @param member, the Member to be added into the Member Database
     * @return message of whether the member has been added successfully
     */
    private String loadMembersList(String member) {
        member = RemoveSpace(member);
        String[] memberInfo = member.split(" ");
        if (memberInfo.length != Invalid_MemberInfo_Len) {
            return Integer.toString(memberInfo.length);
        }
        Member newMember = new Member(capitalize(memberInfo[0]),
                capitalize(memberInfo[1]),
                new Date(memberInfo[2]),
                new Date(memberInfo[3]),
                Location.findLocation(memberInfo[4].toUpperCase()));

        if (this.Exist(newMember) < None)
            this.add(newMember);
         return String.format("%s %s, DOB: %s,  Membership expires: %s," +
                        " Location: %s \n", newMember.getfname(),
                newMember.getlname(), newMember.getdob(),
                newMember.getExpireDate(),
                newMember.getLocation().getLocation());
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
     * Capitalize the first letter of a word and make the rest lower case
     *
     * @param word, the word that needs to be capitalized
     */
    private String capitalize(String word) {
        return word.substring(0, 1).toUpperCase()
                + word.substring(1).toLowerCase();
    }

}
