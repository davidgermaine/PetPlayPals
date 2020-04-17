BEGIN TRANSACTION;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id serial PRIMARY KEY,
  username varchar(255) NOT NULL UNIQUE,     -- Username
  password varchar(32) NOT NULL,      -- Password
  salt varchar(256) NOT NULL,          -- Password Salt
  role varchar(255) NOT NULL default('user')
);

COMMIT TRANSACTION;

BEGIN TRANSACTION;

DROP TABLE IF EXISTS pet;
CREATE TABLE pet (
        petId serial PRIMARY KEY,
        username varchar(255),
        petName varchar (50) NOT NULL,
        petSpecies varchar (20) NOT NULL,
        petBreed varchar(30),
        petAgeGroup varchar(20),
        petActivityLevel varchar(20),
        petDescription text,
        
        CONSTRAINT FK_users FOREIGN KEY (username) references users (username)
);

COMMIT TRANSACTION;
        
BEGIN TRANSACTION;

DROP TABLE IF EXISTS playdate;
CREATE TABLE playdate (
        playdateid serial PRIMARY KEY,
        username varchar(255),
        playdateDate date NOT NULL,
        address varchar(50) NOT NULL,
        city varchar(50) NOT NULL,
        region varchar(50) NOT NULL,
        zipcode varchar(20) NOT NULL,
        latitude varchar(20) NOT NULL,
        longitude varchar(20) NOT NuLL,
        playdatetime varchar(20) NOT NULL,
        
        CONSTRAINT FK_users FOREIGN KEY (username) references users (username)       
);

COMMIT TRANSACTION;    

BEGIN TRANSACTION;    
        
DROP TABLE IF EXISTS pet_playdate;
CREATE TABLE pet_playdate ( 
        petplaydateid serial PRIMARY KEY,      
        playdateid integer,
        petid integer,
        
        CONSTRAINT FK_pet FOREIGN KEY (petid) references pet (petid),
        CONSTRAINT FK_playdate FOREIGN KEY (playdateid) references playdate (playdateid)        
);

COMMIT TRANSACTION;      

BEGIN TRANSACTION;    
        
DROP TABLE IF EXISTS forumpost;
CREATE TABLE forumpost (
        forumId serial PRIMARY KEY,
        username varchar(255),        
        forumposttitle varchar (30),
        forumpostcomment text,
        forumpostdate date,
        
        CONSTRAINT FK_users FOREIGN KEY (username) references users (username)        
);

COMMIT TRANSACTION;  

BEGIN TRANSACTION;    
        
DROP TABLE IF EXISTS playdaterequest;
CREATE TABLE playdaterequest (
        requestid serial PRIMARY KEY,       
        playdateid integer,
        username varchar(255),
        petid integer,
        
        CONSTRAINT FK_pet FOREIGN KEY (petid) references pet (petid),
        CONSTRAINT FK_playdate FOREIGN KEY (playdateid) references playdate (playdateid),
        CONSTRAINT FK_users FOREIGN KEY (username) references users (username)        
        
);

COMMIT TRANSACTION;    

BEGIN TRANSACTION;    
        
DROP TABLE IF EXISTS playdatechat;
CREATE TABLE playdatechat (
        playdatechatid serial PRIMARY KEY,
        playdateid integer NOT NULL,
        messagedate date NOT NULL,       
        username varchar(255) NOT NULL,
        message text NOT NULL,
        
        CONSTRAINT FK_playdate FOREIGN KEY (playdateid) references playdate (playdateid),
        CONSTRAINT FK_users FOREIGN KEY (username) references users (username)        
        
);

COMMIT TRANSACTION;  




