package model;

import model.Records.Park;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
            if (park.Name().equalsIgnoreCase(parkName)) {
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