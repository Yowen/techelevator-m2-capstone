package com.techelevator.database_link;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCSiteDAO implements SiteDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public List<Site> allAvailableSites(Campground campground, LocalDate startDate, LocalDate endDate) {

		String sql = "SELECT * FROM site " + "JOIN campground ON site.campground_id = campground.campground_id "
				+ "WHERE site.campground_id = ? " + "AND site.site_id NOT IN ( " + "SELECT s.site_id FROM site s "
				+ "JOIN reservation r ON s.site_id = r.site_id " + "WHERE s.campground_id = ?  " + "AND "
				+ "(? > r.from_date AND ? < r.to_date OR ? > r.from_date AND ? < r.to_date) " + "OR "
				+ "(r.from_date BETWEEN ? AND ? AND r.to_date BETWEEN ? AND ?)) "
				+ "GROUP BY site.site_id, campground.campground_id " + "ORDER BY site.site_id ASC " + "LIMIT 5;";

		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, campground.getCampgroundId(), campground.getCampgroundId(),
				startDate, startDate, endDate, endDate, startDate, endDate, startDate, endDate);

		List<Site> sites = new ArrayList<Site>();

		while (result.next()) {
			sites.add(mapRowToSite(result));
		}

		return sites;
	}

	private Site mapRowToSite(SqlRowSet result) {

		Site s = new Site();
		s.setSiteId(result.getLong("site_id"));
		s.setCampgroundId(result.getLong("campground_id"));
		s.setSiteNumber(result.getInt("site_number"));
		s.setMaxOccupancy(result.getInt("max_occupancy"));
		s.setAccessible(result.getBoolean("accessible"));
		s.setMaxRvLength(result.getInt("max_rv_length"));
		s.setUtilities(result.getBoolean("utilities"));

		return s;
	}

}
