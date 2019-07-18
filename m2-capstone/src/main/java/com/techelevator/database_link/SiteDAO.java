package com.techelevator.database_link;

import java.time.LocalDate;
import java.util.List;

public interface SiteDAO {

	public List<Site> allAvailableSites(Campground campground, LocalDate startDate, LocalDate endDate);
	
}
