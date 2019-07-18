START TRANSACTION;

INSERT INTO survey_result VALUES (DEFAULT, ?, ?, ?, ?);
SELECT * FROM survey_result;

SELECT survey_result.parkcode, parkname, count(survey_result.parkcode) AS tally FROM survey_result 
JOIN park ON park.parkcode = survey_result.parkcode 
GROUP BY survey_result.parkcode, parkname
ORDER BY tally DESC, parkname ASC;
DELETE FROM survey_result
SELECT count(parkname) as count, parkname FROM park GROUP BY parkname;
ROLLBACK;