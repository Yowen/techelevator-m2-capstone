package com.techelevator.database_link;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.DAOIntegrationTest;

public class JDBCParkDAOTest extends DAOIntegrationTest {

	private JDBCParkDAO dao = new JDBCParkDAO(getDataSource());
	private JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
	
	@Test
	public void get_all_parks() {
		
		Map<String, Park> parks = dao.allParks();
		int originalSize = parks.size();
		
		String sql = "INSERT INTO park (park_id, name, location, establish_date, area, visitors, description) VALUES (400, 'Test Park', 'Here', '2000-01-01', 200, 2, 'A quaint place, a place where those off the beaten path go to PLEASE WORK');";
		
		jdbcTemplate.update(sql);
		
		Map<String, Park> result = dao.allParks();
		
		Assert.assertEquals(originalSize + 1, result.size());
		
	}
	
}
