package com.techelevator.database_link;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.DAOIntegrationTest;

import org.junit.Assert;

public class JDBCSiteDAOTest extends DAOIntegrationTest {

	private JDBCSiteDAO dao = new JDBCSiteDAO(getDataSource());
	private JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
	private LocalDate startDate = LocalDate.of(2019, 07, 01);
	private LocalDate endDate = LocalDate.of(2019, 07, 05);
	private Campground campground = new Campground();
	
	@Test
	public void get_available_sites() {
		campground.setCampgroundId(7);
		List<Site> startList = dao.allAvailableSites(campground, startDate, endDate);
		int originalSize = startList.size();
		
		String sql = "INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date) " + 
				"VALUES (DEFAULT, 622, 'TEST', '2019-07-01', '2019-07-05', NOW());";
	
		jdbcTemplate.update(sql);
		
		List<Site> endList = dao.allAvailableSites(campground, startDate, endDate);
		int newSize = endList.size();
		
		Assert.assertEquals(originalSize - 1, newSize);
		
	}
	
}
