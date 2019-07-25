


insert into user_ (user_id, email, passwordhash, name_, role_) values (1, 'sa@site.com', '912ec803b2ce49e4a541068d495ab570', 'Robert Griffioen', 'admin');
insert into user_ (user_id, email, passwordhash, name_, role_) values (2, 'thijs.griffioen@student.hu.nl', '912ec803b2ce49e4a541068d495ab570', 'Thijs Griffioen', 'user');
insert into user_ (user_id, email, passwordhash, name_, role_) values (3, 'user1@site.com', '912ec803b2ce49e4a541068d495ab570', 'User 1', 'user');
insert into user_ (user_id, email, passwordhash, name_, role_) values (4, 'user2@site.com', '912ec803b2ce49e4a541068d495ab570', 'User 2', 'user');

insert into car (car_id, model, price_per_km, mileage, user_id) values (1, 'Opel Corsa benzine 2003', 0.13, 193683, 1);
insert into car (car_id, model, price_per_km, mileage, user_id) values (2, 'Golf R 2018', 0.30, 9107, 1);
insert into car (car_id, model, price_per_km, mileage, user_id) values (3, 'Renault Twingo LPG 1998', 0.10, 210663, 1);

insert into availability_timeframe (timeframe_id, start_, end_, car_id, user_id) values (1, TO_TIMESTAMP('2019-07-01 09:30','YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2019-07-10 16:30','YYYY-MM-DD HH24:MI'), 1, 2);
insert into availability_timeframe (timeframe_id, start_, end_, car_id, user_id) values (2, TO_TIMESTAMP('2019-07-01 09:30','YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2019-07-10 16:30','YYYY-MM-DD HH24:MI'), 2, 2);
insert into availability_timeframe (timeframe_id, start_, end_, car_id, user_id) values (3, TO_TIMESTAMP('2019-07-01 09:30','YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2019-07-10 16:30','YYYY-MM-DD HH24:MI'), 3, 2);

insert into availability_timeframe (timeframe_id, start_, end_, car_id, user_id) values (4, TO_TIMESTAMP('2019-07-11 09:30','YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2019-07-30 22:30','YYYY-MM-DD HH24:MI'), 2, 2);
insert into availability_timeframe (timeframe_id, start_, end_, car_id, user_id) values (5, TO_TIMESTAMP('2019-07-11 09:30','YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2019-07-30 22:30','YYYY-MM-DD HH24:MI'), 1, 2);

insert into lend_session (session_id, kilometers, paid, kilometers_submitted, accepted, start_, end_, car_id, user_id) values (1, 96, 0, 1, 1, TO_TIMESTAMP('2019-07-02 09:30','YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2019-07-03 22:30','YYYY-MM-DD HH24:MI'), 1, 2);
insert into lend_session (session_id, paid, kilometers_submitted, accepted, start_, end_, car_id, user_id) values (2, 0, 0, 0, TO_TIMESTAMP('2019-07-19 09:30','YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2019-07-19 22:30','YYYY-MM-DD HH24:MI'), 1, 2);