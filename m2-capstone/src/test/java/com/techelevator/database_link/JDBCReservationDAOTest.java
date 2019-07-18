package com.techelevator.database_link;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.DAOIntegrationTest;

public class JDBCReservationDAOTest extends DAOIntegrationTest {

	private JDBCReservationDAO dao = new JDBCReservationDAO(getDataSource());
	private JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
	private LocalDate startDate = LocalDate.of(2017, 07, 01);
	private LocalDate endDate = LocalDate.of(2017, 07, 05);
	private LocalDate createDate = LocalDate.of(2017, 06, 22);

	@Test
	public void reserve_is_successful() {

		String sql = "SELECT count(*) as count FROM reservation;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
		result.next();

		int originalSize = result.getInt("count");

		dao.reserve(newReservation());

		result = jdbcTemplate.queryForRowSet(sql);
		result.next();

		int newSize = result.getInt("count");

		Assert.assertEquals(originalSize + 1, newSize);

	}

	@Test
	public void gets_the_id() {
		dao.reserve(newReservation());
		String sql = "SELECT reservation_id FROM reservation WHERE site_id = ? AND name = ? AND create_date = ?;";
		SqlRowSet select = jdbcTemplate.queryForRowSet(sql, 46, "Test", createDate);
		select.next();

		long expected = select.getLong("reservation_id");

		long result = dao.getId(newReservation());

		Assert.assertEquals(expected, result);
	}

	private Reservation newReservation() {
		Reservation r = new Reservation();
		r.setCreateDate(createDate);
		r.setFromDate(startDate);
		r.setName("Test");
		r.setReservationId(50);
		r.setSiteId(46);
		r.setToDate(endDate);

		return r;
	}

}
