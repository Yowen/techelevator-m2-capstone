package com.techelevator.database_link;

import java.util.Map;

public interface CampgroundDAO {

	public Map<Integer, Campground> campgroundsFromPark(int id);

}
