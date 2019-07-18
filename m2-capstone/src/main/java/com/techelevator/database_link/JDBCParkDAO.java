package com.techelevator.database_link;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public Map<String, Park> allParks() {

		String sql = "SELECT park_id, name, location, establish_date, area, visitors, description FROM park ORDER BY name ASC;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
		int i = 1;

		Map<String, Park> allParks = new HashMap<String, Park>();

		while (result.next()) {
			String s = Integer.toString(i);
			allParks.put(s, mapRowToParks(result));
			i++;
		}

		return allParks;
	}

	private Park mapRowToParks(SqlRowSet result) {

		Park p = new Park();
		p.setId(result.getLong("park_id"));
		p.setName(result.getString("name"));
		p.setAnnualVisitorCount(result.getInt("visitors"));
		p.setDescription(result.getString("description"));
		p.setArea(result.getInt("area"));
		p.setEstablishDate(result.getDate("establish_date").toLocalDate());
		p.setLocation(result.getString("location"));

		return p;
	}

}
