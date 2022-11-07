package com.softmeth.fitnessproject;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The Class schedule class basically maintains all of the classes
 * that a member can take, it maintains all of the available classes
 * in a single array called classes. It will grow its array classes
 * accordingly if more fitness classes need to be added
 *
 * @author Peter Chen, Jonathon Lopez
 */
public class ClassSchedule {


    private FitnessClass [] classes;
    private int numClasses;
    private static int Invalid_ClassInfo_Len = 4;

    /**
     * Simple constructor to make a Class Schedule object
     */
    public ClassSchedule(){
        this.classes = new FitnessClass[1];
        this.numClasses = 0;
    }

    /**
     Return the entire Class Schedule array
     @return Class Schedule array with all the classes
     */
    public FitnessClass[] getClasses(){
        return this.classes;
    }

    /**
     Return the number of classes in the entire Class Schedule
     @return Number of classes in the Class Schedule
     */
    public int getNumClasses(){
        return this.numClasses;
    }

    /**
     * Adds a fitness class to the class array
     * grows the array accordingly
     * @param fitnessClass the fitness class we are trying to add
     */
    public void add(FitnessClass fitnessClass){
        this.numClasses ++;
        if (numClasses >= classes.length){
            grow();
            this.classes[this.numClasses - 1] = fitnessClass;
            addToClassTime(fitnessClass);
        }else{
            classes[numClasses-1] = fitnessClass;
            addToClassTime(fitnessClass);
        }
    }

    /**
     Fitnessclass class maintains an arraylist with all the classes
     in each period of the day so that it can check if a user has a time
     conflict with a class they are trying to add. Here is where a fitness class
     gets added to their respective arraylist of times
     @param fitnessClass the Fitness Class that is being added
     */
    private void addToClassTime(FitnessClass fitnessClass){
        if(fitnessClass.getTime() == Time.MORNING){
            FitnessClass.morningClasses.add(fitnessClass);
        }else if(fitnessClass.getTime() == Time.AFTERNOON){
            FitnessClass.afternoonClasses.add(fitnessClass);
        }else {
            FitnessClass.eveningClasses.add(fitnessClass);
        }
    }

    /**
     Grow the Class Schedule size by creating a new Class Schedule and copying over all
     the information of the old Class Schedule
     */
    private void grow(){
        FitnessClass[] fitnessClass = new FitnessClass[this.numClasses + 10];
        for (int counter = 0; counter < this.numClasses; counter++) {
            fitnessClass[counter] = this.classes[counter];
        }
        this.classes = fitnessClass;
    }

    /**
     * Load in a text file line by line and add them to either
     * the ClassSchedule or to the Member List depending on
     * the file name which comes from the command that calls
     * this method
     *
     * @return the string message of whether the file
     * has een successfully loaded
     */
    public String loadFromFile() {
        File file = new File("classSchedule.txt");
        Scanner sc;
        String message = "";
        try {
            sc = new Scanner(file);

            while (sc.hasNextLine()) {
                message = message + loadClassSchedule(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            return "Could not find the specified file " +
                    "path, please place file in root of project";
        }
        return message;
    }

    /**
     * Add the Class loaded in from text file into the Class Schedule
     *
     * @param schedule, the Class to be added into the Class Schedule
     */
    private String loadClassSchedule(String schedule) {
        String[] classInfo = schedule.split(" ");
        if (classInfo.length != Invalid_ClassInfo_Len) {
            return Integer.toString(classInfo.length) + " Invalid length for class \n";
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
            this.add(fc);
        return String.format("%s - %s, %s, %s \n", fc.getClassType(),
                fc.getTeacher().toUpperCase(),
                fc.getTime().getTime(), fc.getLocation());
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

        for (int i = 0; i < this.getNumClasses(); i++) {
            fitnessClass = this.getClasses()[i];

            if (fitnessClass.getClassType().equalsIgnoreCase(classType) && fitnessClass.getTeacher()
                    .equalsIgnoreCase(instructor) && fitnessClass.getLocation()
                    .equals(Location.findLocation(location.toUpperCase())))
                return fitnessClass;
        }
        return null;
    }
}
