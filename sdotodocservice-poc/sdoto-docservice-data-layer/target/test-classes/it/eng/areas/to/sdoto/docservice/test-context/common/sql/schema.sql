--DDL GENERABILE CON HBM2DDL PLUGIN DI MAVEN

  --  create table EXAMPLE (
  --      ID varchar(255) not null,
  --      COL1 varchar(255),        
  --      primary key (ID)
  --  );
    
    create table BOOKING_DOCUMENT (
    	ID varchar(64) NOT NULL,
    	BOOKING_CODE varchar(8),
    	STATE INTEGER not null,    	
    	PARKING varchar(64) not null,
    	CREATION_DATE TIMESTAMP(3),
    	OPEN_DATE TIMESTAMP(3),
    	CLOSE_DATE TIMESTAMP(3),
    	SENT_DATE TIMESTAMP(3),
    	FAILURE_DATE TIMESTAMP(3),
    	FAILURE_CODE VARCHAR(8),
    	FAILURE_REASON VARCHAR(512),
    	DOCUMENT blob not null,
    	OPERATION_DATE TIMESTAMP(3),
    	USER_AUDIT_OPEN VARCHAR(32)
    );
    
    
    create table DEPARTMENT_ADDRESSEE (
    	DEPARTMENT_ID varchar(64) NOT NULL,
    	DESCRIPTION VARCHAR(128) NOT NULL,
    	ADDRESS VARCHAR(32) NOT NULL,
    	WEB_IDENTITY_ID VARCHAR(28)
    );
    
    ALTER TABLE WEB_IDENTITY ADD (PASSWORD_MODIFY_DATE DATE);
   
    

    