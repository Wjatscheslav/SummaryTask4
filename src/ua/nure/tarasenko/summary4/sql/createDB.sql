DROP TABLE payment;
DROP TABLE credit_card;
DROP TABLE account_translate;
DROP TABLE account;
DROP TABLE autorization;
DROP TABLE client_translate;
DROP TABLE client;
DROP TABLE language;
DROP TABLE role;

CREATE TABLE role(
	id INT NOT NULL PRIMARY KEY,
	name VARCHAR(20)
);

INSERT INTO role (id, name) VALUES (1, 'User');
INSERT INTO role (id, name) VALUES (2, 'Admin');

CREATE TABLE language(
	language_id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name VARCHAR(20)
);

INSERT INTO language (name) VALUES ('Russian');
INSERT INTO language (name) VALUES ('English');

CREATE TABLE client(
	id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	born_date DATE NOT NULL,
	telephone_number VARCHAR(13) NOT NULL,
	role_id INT NOT NULL REFERENCES role(id)
);

INSERT INTO client (born_date, telephone_number, role_id) VALUES ('1975-11-20', '+380675554433', 1);
INSERT INTO client (born_date, telephone_number, role_id) VALUES ('1966-02-05', '+380667778899', 1);
INSERT INTO client (born_date, telephone_number, role_id) VALUES ('1985-01-23', '+380991112233', 2);
INSERT INTO client (born_date, telephone_number, role_id) VALUES ('1968-12-03', '+380991222444', 2);


CREATE TABLE client_translate(
	translate_id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	language_id INT NOT NULL REFERENCES language(language_id),
	client_id INT NOT NULL REFERENCES client(id),
	name VARCHAR(30) NOT NULL,
	surname VARCHAR(30) NOT NULL
);

INSERT INTO client_translate (language_id, client_id, name, surname) VALUES (1, 1, 'Мария', 'Андерс');
INSERT INTO client_translate (language_id, client_id, name, surname) VALUES (1, 2, 'Антонио', 'Морено');
INSERT INTO client_translate (language_id, client_id, name, surname) VALUES (1, 3, 'Томас', 'Харди');
INSERT INTO client_translate (language_id, client_id, name, surname) VALUES (2, 1, 'Maria', 'Anders');
INSERT INTO client_translate (language_id, client_id, name, surname) VALUES (2, 2, 'Antonio', 'Moreno');
INSERT INTO client_translate (language_id, client_id, name, surname) VALUES (2, 3, 'Thomas', 'Hardy');
INSERT INTO client_translate (language_id, client_id, name, surname) VALUES (1, 4, 'Питер', 'Джонсон');
INSERT INTO client_translate (language_id, client_id, name, surname) VALUES (2, 4, 'Peter', 'Johnson');

CREATE TABLE autorization(
	login VARCHAR(20) NOT NULL PRIMARY KEY,
	password VARCHAR(20) NOT NULL,
	client_id INT NOT NULL REFERENCES CLIENT(id),
	locked BOOLEAN NOT NULL
);

INSERT INTO autorization (login, password, client_id, locked) VALUES ('MariaMaria', 'MariaMaria', 1, 'false');
INSERT INTO autorization (login, password, client_id, locked) VALUES ('AntonioAntonio', 'AntonioAntonio', 2, 'false');
INSERT INTO autorization (login, password, client_id, locked) VALUES ('ThomasThomas', 'ThomasThomas', 3, 'false');
INSERT INTO autorization (login, password, client_id, locked) VALUES ('PeterPeter', 'PeterPeter', 4, 'false');

CREATE TABLE account(
	id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	client_id INT NOT NULL REFERENCES CLIENT(id),
	amount DECIMAL NOT NULL
);

INSERT INTO account (client_id, amount) VALUES (1, 3000);
INSERT INTO account (client_id, amount) VALUES (2, 5000);
INSERT INTO account (client_id, amount) VALUES (3, 10500);
INSERT INTO account (client_id, amount) VALUES (1, 5500);

CREATE TABLE account_translate(
	translate_id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	language_id INT NOT NULL REFERENCES language(language_id),
	account_id BIGINT NOT NULL REFERENCES ACCOUNT(id),
	name VARCHAR(30) NOT NULL
);

INSERT INTO account_translate (language_id, account_id, name) VALUES (1, 1, 'Карта для выплат');
INSERT INTO account_translate (language_id, account_id, name) VALUES (1, 2, 'Кредитная карта');
INSERT INTO account_translate (language_id, account_id, name) VALUES (1, 3, 'Депозитная карта');
INSERT INTO account_translate (language_id, account_id, name) VALUES (1, 4, 'Карта для выплат');
INSERT INTO account_translate (language_id, account_id, name) VALUES (2, 1, 'Card for payments');
INSERT INTO account_translate (language_id, account_id, name) VALUES (2, 2, 'Credit card');
INSERT INTO account_translate (language_id, account_id, name) VALUES (2, 3, 'Deposit card');
INSERT INTO account_translate (language_id, account_id, name) VALUES (2, 4, 'Card for payments');

CREATE TABLE credit_card(
	card_id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	client_id INT NOT NULL REFERENCES CLIENT(id),
	account_id BIGINT NOT NULL REFERENCES ACCOUNT(id),
	pin INT NOT NULL,
	locked BOOLEAN NOT NULL
);

INSERT INTO credit_card (client_id, account_id, pin, locked) VALUES (1, 1, 1687, 'false');
INSERT INTO credit_card (client_id, account_id, pin, locked) VALUES (2, 2, 3412, 'false');
INSERT INTO credit_card (client_id, account_id, pin, locked) VALUES (3, 3, 5475, 'false');
INSERT INTO credit_card (client_id, account_id, pin, locked) VALUES (1, 4, 5436, 'false');

CREATE TABLE payment(
	payment_id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	operation_date DATE NOT NULL,
	send_account BIGINT NOT NULL REFERENCES ACCOUNT(id),
	receive_account BIGINT NOT NULL REFERENCES ACCOUNT(id),
	amount DECIMAL NOT NULL
);

INSERT INTO payment (operation_date, send_account, receive_account, amount) VALUES ('2015-11-20', 1, 2, 200);
INSERT INTO payment (operation_date, send_account, receive_account, amount) VALUES ('2016-03-07', 2, 3, 100);
INSERT INTO payment (operation_date, send_account, receive_account, amount) VALUES ('2016-07-13', 3, 1, 300);