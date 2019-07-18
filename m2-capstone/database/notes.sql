SELECT * FROM campground WHERE park_id = 1;
SELECT park_id, name, location, establish_date, area, visitors, description FROM park; 
 
INSERT INTO campground (campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES (8, 1, 'Test Camp', '03', '06', 6.01);

INSERT INTO park (park_id, name, location, establish_date, area, visitors, description) VALUES (400, 'Test Park', 'Here', '2000-01-01', 200, 2, 'A quaint place, a place where those off the beaten path go to PLEASE WORK');
        
        
        SELECT park_id, name, location, establish_date, area, visitors, description FROM park ORDER BY name ASC;
        
        SELECT * FROM site WHERE site_id = 46;
        
        -- true
        SELECT site.site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities FROM site 
        JOIN reservation ON site.site_id = reservation.site_id
        WHERE campground_id = ? AND site.site_id NOT IN (SELECT reservation.site_id FROM reservation WHERE ((? BETWEEN from_date AND to_date) AND (? BETWEEN from_date AND to_date) OR (from_date BETWEEN ? AND ?) AND (to_date BETWEEN ? AND ?)))
        ORDER BY site.site_id ASC;
        
        -- original true
        SELECT site.site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities FROM site 
        JOIN reservation ON site.site_id = reservation.site_id
        WHERE reservation.site_id = ? AND ((? BETWEEN from_date AND to_date) AND (? BETWEEN from_date AND to_date) OR (from_date BETWEEN ? AND ?) AND (to_date BETWEEN ? AND ?))
        ORDER BY site.site_id ASC;
        
        
        -- false
        SELECT count(*) FROM reservation WHERE (from_date BETWEEN ? AND ?) AND (to_date BETWEEN ? AND ?);
        
        SELECT count(*) FROM reservation WHERE ( BETWEEN ? AND ?) BETWEEN (from_date AND to_date);
        SELECT reservation_id FROM reservation WHERE site_id = ? AND name = ? AND create_date = ?;     

START TRANSACTION;
        
        SELECT * FROM site
        JOIN campground ON site.campground_id = campground.campground_id
        WHERE site.campground_id = ?
        AND site.site_id NOT IN (
        SELECT s.site_id FROM site s
        JOIN reservation r ON s.site_id = r.site_id
        WHERE s.campground_id = ? 
        AND 
        (? > r.from_date AND ? < r.to_date OR ? > r.from_date AND ? < r.to_date)
        OR 
        (r.from_date BETWEEN ? AND ? AND r.to_date BETWEEN ? AND ?))
        GROUP BY site.site_id, campground.campground_id
        ORDER BY site.site_id ASC
        LIMIT 5;
                
        SELECT * FROM reservation
        DELETE FROM reservation WHERE name = 'Hello'
        SELECT * FROM site WHERE site_id = 622;
        INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date) VALUES (DEFAULT, 622, 'TEST', '2019-07-01', '2019-07-05', NOW());
        
        INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date) VALUES (50, 46, 'Test Family', '2019-06-25', '2019-07-01', '2019-06-20');
ROLLBACK;

SELECT first_name, last_name, email, activebool FROM customer WHERE ? = first_name OR ? = last_name ORDER BY last_name ASC;
SELECT first_name, last_name, email, activebool FROM customer WHERE first_name = ? OR last_name = ? ORDER BY ?;

COMMIT;