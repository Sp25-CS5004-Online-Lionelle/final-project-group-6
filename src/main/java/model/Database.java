package model;

import model.Records.Park;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import model.Records.Activity;

public class Database {

    /** The set of parks retrieved from the API */
    private Set<Park> parkSet;

    /**
     * Constructor for the ParkSet class.
     * 
     * @param dataList the collection of parks
     */
    public Database() {
        this.parkSet = new HashSet<>();
    }

    /**
     * Search if the Database contains a specific park.
     * 
     * @param park the park to search for
     * @return true if the park is found, false otherwise
     */
    public Boolean contains(Park park) {
        return this.parkSet.contains(park);
    }

    /**
     * Get the park by its name.
     * 
     * @return the park with the specified name
     */
    public Park getParkByName(String parkName) {
        for (Park park : this.parkSet) {
            if (park.name().equalsIgnoreCase(parkName)) {
                return park;
            }
        }
        return null;
    }

    /**
     * Get all parks that offer a specific activity.
     * 
     * @param activityName the name of the activity
     * @return a set of parks that offer the specified activity
     */
    public Set<Park> getParksByActivityName(String activityName) {
        Set<Park> result = new HashSet<>();
        for (Park park : this.parkSet) {
            for (Activity activity : park.activities()) {
                if (activity.name().equalsIgnoreCase(activityName)) {
                    result.add(park);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Get the park by its park code.
     * 
     * @return the park with the specified park code
     */
    public Park getParkByParkCode(String parkCode) {
        for (Park park : this.parkSet) {
            if (park.parkCode().equalsIgnoreCase(parkCode)) {
                return park;
            }
        }
        return null;
    }

    /**
     * Adds a park to the database.
     * 
     * @param park the park to add
     */
    public void savePark(Park park) {
        this.parkSet.add(park);
    }

    /**
     * Removes a park from the database.
     * 
     * @param park the park to remove
     */
    public void removePark(Park park) {
        this.parkSet.remove(park);
    }

    /**
     * Updates the database with a new collection of parks.
     */
    public void updateDB(Collection<Park> newParks) {
        this.parkSet.addAll(newParks);
    }

    /**
     * Clears the database.
     */
    public void clear() {
        this.parkSet.clear();
    }
}