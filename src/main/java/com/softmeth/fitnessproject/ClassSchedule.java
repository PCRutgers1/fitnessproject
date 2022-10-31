package fitnessproject;


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

}
