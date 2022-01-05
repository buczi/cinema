-- ticket types
INSERT INTO Ticket VALUES ('ADULT',25.0);
INSERT INTO Ticket VALUES ('STUDENT',18.0);
INSERT INTO Ticket VALUES ('CHILD',12.5);

-- movies
INSERT INTO Movie (MOVIE_ID,NAME,LENGTH,GENRE) VALUES (1,'James Bond 007',180,'action');
INSERT INTO Movie (MOVIE_ID,NAME,LENGTH,GENRE) VALUES (2,'Spider Man',120,'fantasy');
INSERT INTO Movie (MOVIE_ID,NAME,LENGTH,GENRE) VALUES (3,'Diune',154,'sci-fi');
INSERT INTO Movie (MOVIE_ID,NAME,LENGTH,GENRE) VALUES (4,'M jak miłość',78,'romance');
INSERT INTO Movie (MOVIE_ID,NAME,LENGTH,GENRE) VALUES (5,'Pan wołodyjowski',180,'historical');
INSERT INTO Movie (MOVIE_ID,NAME,LENGTH,GENRE) VALUES (6,'Jak rozpętałem drugą wojnę światową',88,'historical');

-- hall 1
INSERT INTO Hall VALUES (1,'Rose Stage');
-- first row
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (1,1,1,1);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (2,1,2,1);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (3,1,3,1);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (4,1,4,1);
-- second row
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (5,2,1,1);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id)VALUES (6,2,2,1);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (7,2,3,1);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (8,2,4,1);

-- hall 2
INSERT INTO Hall VALUES (2,'Golden Stage');
-- first row
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (9,1,1,2);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (10,1,2,2);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (11,1,3,2);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (12,1,4,2);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (13,1,5,2);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (14,1,6,2);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (15,1,7,2);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (16,1,8,2);
-- second row
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (17,2,1,2);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (18,2,2,2);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (19,2,3,2);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (20,2,4,2);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (21,2,5,2);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (22,2,6,2);

-- hall 3
INSERT INTO Hall VALUES (3,'Brocolli Amphitheater');
-- first row
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (23,1,1,3);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (24,1,2,3);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (25,1,3,3);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (26,1,4,3);
-- second
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (27,2,1,3);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (28,2,2,3);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (29,2,3,3);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (30,2,4,3);
-- third row
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (31,3,1,3);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (32,3,2,3);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (33,3,3,3);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (34,3,4,3);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (35,3,5,3);
INSERT INTO Seat (Seat_id,seat_row,seat_number,hall_hall_id) VALUES (36,3,6,3);

-- plus 60 minutes for differing time zone
-- movies in first hall
INSERT INTO Event VALUES (1,EXTRACT(EPOCH FROM current_timestamp) * 1000 + (60+20) * 60 * 1000,1,3); -- movie in 20 minutes
INSERT INTO Event VALUES (2,EXTRACT(EPOCH FROM current_timestamp) * 1000 + (60+120) * 60 * 1000,1,1); -- movie in 2 hours
INSERT INTO Event VALUES (3,EXTRACT(EPOCH FROM current_timestamp) * 1000 + (60+10) * 60 * 1000,1,1); -- movie in 10 minutes

-- movies in second hall
INSERT INTO Event VALUES (4,EXTRACT(EPOCH FROM current_timestamp) * 1000 + (60+30) * 60 * 1000,2,6); -- movie in 30 minutes
INSERT INTO Event VALUES (5,EXTRACT(EPOCH FROM current_timestamp) * 1000 + (60+180) * 60 * 1000,2,6); -- movie in 3 hours
INSERT INTO Event VALUES (6,EXTRACT(EPOCH FROM current_timestamp) * 1000 + (60+5) * 60 * 1000,2,4); -- movie in 5 minutes

-- movies in third hall
INSERT INTO Event VALUES (7,EXTRACT(EPOCH FROM current_timestamp) * 1000 + (60+20) * 60 * 1000,3,3); -- movie in 20 minutes
INSERT INTO Event VALUES (8,EXTRACT(EPOCH FROM current_timestamp) * 1000 + (60+120) * 60 * 1000,3,4); -- movie in 2 hours
INSERT INTO Event VALUES (9,EXTRACT(EPOCH FROM current_timestamp) * 1000 + (60+10) * 60 * 1000,3,2); -- movie in 10 minutes

-- Reservations for first movie
INSERT INTO Reservation (RESERVATION_ID,NAME,SURNAME,SUBMIT_DATE,finished,event_event_id) VALUES (1,'ANTONI','STONI',12000000,false,1);
INSERT INTO Reservation (RESERVATION_ID,NAME,SURNAME,SUBMIT_DATE,finished,event_event_id) VALUES (2,'Błogusława','Troć',13000000,false,1);

INSERT INTO RESERVED_SEAT (reservation_id_reservation_id,seat_seat_id,ticket_type_type) VALUES (1,3,'ADULT'); --2
INSERT INTO RESERVED_SEAT (reservation_id_reservation_id,seat_seat_id,ticket_type_type) VALUES (1,4,'ADULT');  --3

INSERT INTO RESERVED_SEAT (reservation_id_reservation_id,seat_seat_id,ticket_type_type) VALUES (2,7,'ADULT');
INSERT INTO RESERVED_SEAT (reservation_id_reservation_id,seat_seat_id,ticket_type_type) VALUES (2,8,'STUDENT');
