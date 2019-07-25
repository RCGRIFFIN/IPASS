DROP TABLE IF EXISTS availability_timeframe;
DROP TABLE IF EXISTS lend_session;
DROP TABLE IF EXISTS car;
DROP TABLE IF EXISTS user_;



CREATE TABLE car (
	car_id numeric(10) NOT NULL,
	model varchar(40) NOT NULL,
	price_per_km numeric(4,2),
	mileage numeric(6),
	user_id numeric(10) NOT NULL,
	PRIMARY KEY (car_id)
);



CREATE TABLE user_ (
	user_id numeric(10) NOT NULL,
	email varchar(30) NOT NULL,
	name_ varchar(30) NOT NULL,
	passwordhash varchar(32),
	role_ varchar(10) NOT NULL,
	PRIMARY KEY (user_id)
);

CREATE TABLE availability_timeframe (
	timeframe_id numeric(10) NOT NULL,
	start_ timestamp NOT NULL,
	end_ timestamp NOT NULL,
	car_id numeric(10) NOT NULL,
	user_id numeric(10) NOT NULL,
	PRIMARY KEY (timeframe_id)
);

ALTER TABLE availability_timeframe ADD CONSTRAINT timeframe_car_fk FOREIGN KEY (car_id) REFERENCES car (car_id);
ALTER TABLE availability_timeframe ADD CONSTRAINT timeframe_user_fk FOREIGN KEY (user_id) REFERENCES user_ (user_id);


CREATE TABLE lend_session (
	session_id numeric(10) NOT NULL,
	kilometers numeric(5),
	paid numeric(1) NOT NULL,
	kilometers_submitted numeric(1) NOT NULL,
	accepted numeric(1) NOT NULL,
	start_ timestamp NOT NULL,
	end_ timestamp NOT NULL,
	car_id numeric(10) NOT NULL,
	user_id numeric(10) NOT NULL
);

ALTER TABLE lend_session ADD CONSTRAINT session_car_fk FOREIGN KEY (car_id) REFERENCES car (car_id);
ALTER TABLE lend_session ADD CONSTRAINT session_user_fk FOREIGN KEY (user_id) REFERENCES user_ (user_id);
ALTER TABLE lend_session ADD CONSTRAINT accepted_check_yn CHECK (accepted in (0, 1));
ALTER TABLE lend_session ADD CONSTRAINT paid_check_yn CHECK (paid in (0, 1));
ALTER TABLE lend_session ADD CONSTRAINT kilometers_submitted_check_yn CHECK (kilometers_submitted in (0, 1));