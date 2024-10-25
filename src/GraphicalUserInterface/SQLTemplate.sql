CREATE TABLE  UserDetails
(	

    UserID CHAR NOT NULL, 
    FirstName Varchar NOT NULL, 
    SecondName Varchar NOT NULL, 
    DOB DATE NOT NULL,
    PassportNO Varchar NOT NULL, 
    Email Varchar NOT NULL,
    Password Varchar NOT NULL,
    PhoneNO Varchar NOT NULL,
    Country Varchar NOT NULL,
    PostCode Varchar NOT NULL,
    PRIMARY KEY (UserID)
);

 

INSERT INTO UserDetails 
VALUES 
('1','Liam','Noah','2000-12-09','TU676765','Yonashaf23@gmail.com','1234','8862376','Ireland','A65HG7'),
('2','Jacob','Benjamin','1990-09-02','TU637673','Jacob34@gmail.com','2345','8237837','Ireland','D01HJ21'),
('3','Jonas','Henry','2002-09-12','FG736736','Jonas6767@gmial.com','3456','8367833','Ireland','D087WG2'),
('4','John','Lucas','1994-09-12','YU767534','LucasJ567@gmial.com','4567','8123773','Ireland','2HD89Y7'),
('5','Yonas','Mathew','2000-09-11','TR373763','Matthewy@gmial.com','5678','8237386','Ireland','D5241GH'),
('6','Tupac','David','2000-02-21','TR633653','David12@gmial.com','6789','8392832','Ireland','D67JKJD'),
('7','Josh','Robert','2001-10-13','UY635323','Joshro6767@gmial.com','7899','8647543','Ireland','H2D8783'),
('8','Ann','James','1990-09-12','RD736383','AnnJames@gmial.com','8999','8273652','Ireland','DBJ3732'),
('9','Larry','Daniel','1988-09-21','SR635736','Larry67@gmial.com','9999','8367633','Ireland','DH3H3G'),
('10','Christina','Jason','1990-08-16','TY837635','Christina6@gmial.com','0123','8365786','Ireland','D67DKD3');



CREATE TABLE FlightDetails (
    FlightNumber CHAR NOT NULL, 
    FlightDestination VARCHAR NOT NULL, 
    FlightFrom VARCHAR NOT NULL, 
    FlightDepartureDate DATE NOT NULL, 
    FlightReturnDate DATE NOT NULL, 
    Passenger VARCHAR NOT NULL, 
    SeatNumber VARCHAR NOT NULL,
    PRIMARY KEY (FlightNumber)
);



INSERT INTO FlightDetails 
VALUES 
    ('1001', 'London', 'Dublin', '2022-11-09', '2022-11-09', '2', 'A11'),
    ('1002', 'Amsterdam', 'Cork', '2022-09-01', '2022-09-01', '4', 'A12'),
    ('1003', 'Florida', 'London', '2022-10-11', '2022-10-11', '4', 'A13'),
    ('1004', 'New York', 'Amsterdam', '2022-11-12', '2022-11-12', '2', 'A14'),
    ('1005', 'Ethiopia', 'Dublin', '2022-10-18', '2022-10-19', '1', 'A15'),
    ('1006', 'Sweden', 'London', '2022-11-19', '2022-11-19', '1', 'A16'),
    ('1007', 'Norway', 'Rome', '2022-12-12', '2022-12-12', '4', 'A17'),
    ('1008', 'Italy', 'Dublin', '2022-10-28', '2022-10-11', '3', 'A18'),
    ('1009', 'Spain', 'Paris', '2022-11-26', '2022-11-02', '2', 'A19'),
    ('1010', 'Morocco', 'Dublin', '2022-10-22', '2022-10-02', '4', 'A20'),
    ('1011', 'Sudan', 'Dublin', '2022-10-09', '2022-11-09', '2', 'A11'),
    ('1012', 'Kenya', 'Cork', '2022-09-29', '2022-09-01', '4', 'A12'),
    ('1013', 'Brazil', 'London', '2022-05-11', '2022-10-11', '4', 'A13'),
    ('1014', 'Spain', 'Amsterdam', '2022-11-12', '2022-11-12', '2', 'A14'),
    ('1015', 'Nigeria', 'Dublin', '2022-09-08', '2022-10-11', '1', 'A15'),
    ('1016', 'Sweden', 'London', '2022-03-09', '2022-11-11', '1', 'A16'),
    ('1017', 'Switzerland', 'Rome', '2022-01-10', '2022-12-03', '4', 'A17'),
    ('1018', 'Germany', 'Dublin', '2022-10-11', '2022-10-08', '3', 'A18'),
    ('1019', 'Madagascar', 'Paris', '2022-11-02', '2022-11-06', '2', 'A19');



CREATE TABLE UserBankCardDetails (
    UserBankCardNumber CHAR NOT NULL, 
    CardHolderName VARCHAR NOT NULL, 
    UserCardExpiryDate DATE NOT NULL, 
    UserCardVerificationValue CHAR NOT NULL,
    UserID CHAR NOT NULL,
    PRIMARY KEY (UserBankCardNumber),
    FOREIGN KEY (UserID) REFERENCES UserDetails(UserID)
);

INSERT INTO UserBankCardDetails VALUES 
    ('11111111111111', 'AnnJones', '2026-05-01', '111', '1'),
    ('22222222222222', 'EskandarAtarkchi', '2026-06-01', '222', '2'),
    ('33333333333333', 'YonasKibrom', '2026-01-01', '333', '3'),
    ('44444444444444', 'BrukHabte', '2026-02-01', '444', '4'),
    ('55555555555555', 'JacobBenjamin', '2026-03-01', '555', '5'),
    ('66666666666666', 'JonasHenry', '2026-04-01', '666', '6'),
    ('77777777777777', 'JohnLucas', '2026-07-01', '777', '7'),
    ('88888888888888', 'LarryDaniel', '2026-08-01', '888', '8'),
    ('99999999999999', 'ChristinaJason', '2026-09-01', '999', '9'),
    ('12121212121212', 'TupacDavid', '2026-10-01', '121', '10');



CREATE TABLE BookingReffrence (
   BookingReffrenceNumber CHAR NOT NULL, 
    UserID CHAR NOT NULL,
    FlightNumber CHAR NOT NULL,
    PRIMARY KEY (BookingReffrenceNumber),
    FOREIGN KEY (UserID) REFERENCES UserDetails(UserID),
    FOREIGN KEY (FlightNumber) REFERENCES FlightDetails(FlightNumber)
);


INSERT INTO BookingReffrence
VALUES 
    ('1','1','1001'),
    ('2','2','1002'),
    ('3','3','1003'),
    ('4','4','1004'),
    ('5','5','1005'),
    ('6','6','1006'),
    ('7','7','1007'),
    ('8','8','1008'), 
    ('9','9','1009'),
    ('10','10','1010');