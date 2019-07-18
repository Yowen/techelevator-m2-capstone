package com.techelevator.database_link;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;
	private Map<Integer, Campground> allCampgrounds;

	public JDBCCampgroundDAO(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public Map<Integer, Campground> campgroundsFromPark(int id) {
		String sql = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, "
				+ "daily_fee FROM campground WHERE park_id = ?;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);

		allCampgrounds = new HashMap<Integer, Campground>();

		int i = 1;

		while (result.next()) {
			Campground mappedCampground = mapRowToCampground(result);
			allCampgrounds.put(i, mappedCampground);
			i++;
		}

		return allCampgrounds;
	}

	private Campground mapRowToCampground(SqlRowSet result) {
		Campground c = new Campground();
		c.setCampgroundId(result.getLong("campground_id"));
		c.setParkId(result.getLong("park_id"));
		c.setName(result.getString("name"));
		c.setDailyFee(result.getDouble("daily_fee"));
		c.setOpenFrom(result.getString("open_from_mm"));
		c.setOpenTo(result.getString("open_to_mm"));

		return c;
	}

}
