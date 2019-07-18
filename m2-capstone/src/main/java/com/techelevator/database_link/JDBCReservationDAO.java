package com.techelevator.database_link;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public void reserve(Reservation reservation) {

		String sql = "INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date) VALUES (DEFAULT, ?, ?, ?, ?, ?);";

		jdbcTemplate.update(sql, reservation.getSiteId(), reservation.getName(), reservation.getFromDate(),
				reservation.getToDate(), reservation.getCreateDate());

	}

	@Override
	public long getId(Reservation reservation) {

		String sql = "SELECT reservation_id, site_id FROM reservation WHERE site_id = ? AND name = ? AND create_date = ?;";

		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, reservation.getSiteId(), reservation.getName(),
				reservation.getCreateDate());
		long reservationId = -1;

		if (result.next()) {
			reservationId = result.getLong("reservation_id");
			reservation.setReservationId(reservationId);
		}

		return reservationId;
	}

}
