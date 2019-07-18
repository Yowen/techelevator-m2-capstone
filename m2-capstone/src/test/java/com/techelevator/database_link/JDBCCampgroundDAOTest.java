package com.techelevator.database_link;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.DAOIntegrationTest;

public class JDBCCampgroundDAOTest extends DAOIntegrationTest {

	private JDBCCampgroundDAO dao = new JDBCCampgroundDAO(getDataSource());
	private JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
	private int parkId = 1;

	@Test
	public void get_all_campgrounds_from_park() {

		Map<Integer, Campground> campgrounds = dao.campgroundsFromPark(parkId);
		int originalSize = campgrounds.size();

		String sql = "INSERT INTO campground (campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES (8, 1, 'Test Camp', '03', '06', 6.01);";

		jdbcTemplate.update(sql);

		Map<Integer, Campground> result = dao.campgroundsFromPark(parkId);

		Assert.assertEquals(originalSize + 1, result.size());

	}

}
