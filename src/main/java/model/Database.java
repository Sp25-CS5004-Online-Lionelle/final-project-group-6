package model;
import java.util.Collection;
import model.Records.Park;

public class Database {

    /** The collection of parks from the API */
    private Collection<Park> dataList ;

    /**
     * Search the Database for a specific park.
     * @param park the park to search for
     * @return the park if found, or null
     */
    public Park search(Park park) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    /**
     * Adds a park to the database.
     * @param park the park to add
     */
    private void savePark(Park park) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    /**
     * Removes a park from the database.
     * @param park the park to remove
     */
    private void removePark(Park park) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    /**
     * Updates the database with a new collection of parks.
     */
    private void updateDB() {
        throw new UnsupportedOperationException("Unimplemented");
    }

    /**
     * Clears the database.
     */
    private void clear() {
        throw new UnsupportedOperationException("Unimplemented");
    }
}