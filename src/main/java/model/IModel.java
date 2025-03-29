package model;
import java.util.Collection;

public interface IModel {
        public void updateDB(String query);
        public Collection<Park> getFilteredData(Filter filter);
}