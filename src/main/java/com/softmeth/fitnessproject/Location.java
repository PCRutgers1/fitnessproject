package fitnessproject;

/**
 * The Location enum class contains all the information pertaining to the
 * location of individuals and offered classes. There are 5 set locations of
 * bridgewater, edison, franklin, piscataway,and somerville. Will by default
 * return invalid as location if the user input an invalid location that could
 * not be found. Each location has values that correspond to its zipcode and
 * county
 *
 * @author Peter Chen, Jonathon Lopez
 */
public enum Location {
    BRIDGEWATER("Bridgewater", "08807", "Somerset"), EDISON("Edison", "08837",
            "Middlesex"), FRANKLIN("Franklin", "08873",
            "Somerset"), PISCATAWAY("Piscataway", "08854",
            "Middlesex"), SOMERVILLE("Somerville", "08876",
            "Somerset"), INVALID("INVALID LOCATION", "00000", "INVALIDCOUNTY");
    private final String location;
    private final String zipCode;
    private final String county;
    private static int zero=0;

    /**
     * Gets the name of the current Location object
     *
     * @return the Location object as a string
     */
    public String getLocationString(){
        return this.location;
    }

    /**
     * Default private constructor that will setup the location value of each
     * of the corresponding locations. Constructor returns nothing and will set
     * each location's zip code and county
     */
    private Location(String location, String zipCode, String county) {
        this.location = location;
        this.zipCode = zipCode;
        this.county = county;
    }

    /**
     * Returns the current location object as a concatenated string with commas
     * separating the location, zip code, and county
     *
     * @return the location in string format as follows: name of the location,
     * zip code of location, county of location
     */
    public String getLocation() {
        return this.location + ", " + this.zipCode + ", " + this.county;
    }

    /**
     * This method checks the if the given location, in string format, is valid
     * and then returns that corresponding location as a location object. If an
     * invalid location is given, it will return INVALID
     *
     * @param location is the location in string format that we would like to
     *                 check if it is an actual valid location
     * @return returns the location if it exists and INVALID if no matching
     * location is found
     */
    public static Location findLocation(String location) {
        Location[] allLocations = Location.values();

        for (Location l : allLocations) {
            if (l.name().equals(location)) return l;
        }
        return INVALID;
    }

    /**
     * This method checks two locations lexicographically and returns an
     * integer value based on which location's county, when converted to a
     * string, comes before the other. If both locations have the same county,
     * then it will compare the zipcode and return the result of its
     * comparison
     *
     * @param loc is the location in string format that is being compared to
     *            the current this string
     * @return will return 0 if the given location is identical to this current
     * location's county, a number less than 0 if the current string is
     * lexicographically behind the location in the argument; and will return
     * an integer greater than 0 if the argument location county is
     * lexicographically before the current this location countys. If the
     * county locations are identical, it compares the zip codes instead.
     */
    public int compare(Location loc) {
        if (this.county.compareTo(loc.county) != zero) {
            return this.county.compareTo(loc.county);
        } else {
            return this.zipCode.compareTo(loc.zipCode);
        }
    }
}
