# bankingSpringApplication

A Spring Boot Backend Application by Benjamin Sinzore.

This application consists of spring application written entirely in Java and a MySQL database. 
Connections to the database are under application.properties, th database is accessed using stored-procedures
called from the Controller.class and executed from the Dao.class. Services.class is used for communications between 
the Controller and the Dao class. In addition, some logical opperetions take place inside the Services.class.

Bellow are the MySQL scripts for, tables and the stored procedures.

# Create A Schemer.

CREATE SCHEMA `bankingtestdb` 

# 1. Create Tables


        
        # Table 1. (Customer Registration Details) t_customerregistrationdetails
use bankingtestdb;
create table bankingtestdb.t_customerregistrationdetails(
    accountNumber BIGINT PRIMARY KEY,
    userID BIGINT NOT NULL,
    userName VARCHAR(100) NOT NULL,
    userPassword VARCHAR(100)  NOT NULL,
    userEmailAddress VARCHAR(100) NOT NULL,
    status VARCHAR(100) NOT NULL
);





        # Table 2. (Bank Employee Registration) t_bankadminregistrationdetails
use bankingtestdb;
create table bankingtestdb.t_bankadminregistrationdetails(
        employeeID VARCHAR(100) NOT NULL PRIMARY KEY,
        employeePassword VARCHAR(100) NOT NULL,
        employeeName VARCHAR(100) NOT NULL,
        employeeCapacity VARCHAR(100)  NOT NULL
);





        # Table 3. (Customer Transaction Details) t_customertransactiondetails
use bankingtestdb;
create table bankingtestdb.t_customertransactiondetails(
        ID BIGINT PRIMARY KEY,
        accountNumber BIGINT NOT NULL ,
        amount BIGINT NOT NULL,
        createdOn VARCHAR(100)  NOT NULL,
        credit BIGINT NOT NULL,
        debit BIGINT  NOT NULL,
        sentTo VARCHAR(100) NOT NULL,
        receivedFrom VARCHAR(100) NOT NULL,
        availableBalance BIGINT NOT NULL
);
ALTER TABLE bankingtestdb.t_customertransactiondetails ADD FOREIGN KEY (accountNumber)
REFERENCES bankingtestdb.t_customertransactiondetails (accountNumber);





        # Table 4. (Bank Transaction) t_banktransactions
use bankingtestdb;
create table bankAccountTest.t_banktransactions(
        ID INT(11) NOT NULL PRIMARY KEY,
        accountNumber VARCHAR(100) NOT NULL,
        amount INT(11) NOT NULL,
        createdOn VARCHAR(100)  NOT NULL,
        credit DECIMAL(10,0) NOT NULL,
        debit DECIMAL(10,0) NOT NULL,
        sentTo VARCHAR(100) NOT NULL,
        receivedFrom VARCHAR(100)  NOT NULL,
        availableBalance DECIMAL(10,0) NOT NULL,
        loanBalance DECIMAL(10,0) NOT NULL,
        loanStatus VARCHAR(100) NOT NULL
)


        # Table 5. (Loan Management) t_loanmanagement
use bankingtestdb;
    create table t_loanmanagement(
    loanID bigint primary key,
    applicantID bigint,
    foreign key (applicantID) REFERENCES t_customertransactiondetails (accountNumber),
    amount decimal(10, 0),
    plaidDate varchar(100),
    dueDate varchar(100)
);


# 2. Create Stored Procedure

        # Stored Procedure 1. sp_CustomerRegistrationDetails
USE `bankingtestdb`;
DROP procedure IF EXISTS `sp_CustomerRegistrationDetails`;

USE `bankingtestdb`;
DROP procedure IF EXISTS `bankingtestdb`.`sp_CustomerRegistrationDetails`;
;

DELIMITER $$
USE `bankingtestdb`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_CustomerRegistrationDetails`(
    IN userIDIN BIGINT,
    IN userNameIN VARCHAR(100),
    IN userPasswordIN VARCHAR(100),
    IN userEmailAddressIN VARCHAR(100)
)
BEGIN
    DECLARE Message VARCHAR(100);
    DECLARE GetAccNumber BIGINT;
    DECLARE ErrorStatus INT;
    BEGIN
        IF EXISTS(SELECT userID FROM bankingtestdb.t_CustomerRegistrationDetails WHERE userID = userIDIN)
        THEN
        SET Message = CONCAT(CONCAT('User with this ID NO. ', userIDIN), ', already exist. Please contact customer care for help.');
        ELSE
        GET diagnostics condition 1 @sqlstate = returned_sqlstate, @errno = mysql_errno, @text = message_text;
        SET @full_errno = concat("ERRNO ", @errno, "(", @sqlstate, "):", @text);
        SET GetAccNumber = (SELECT (CONCAT((100100), userIDIN)));
        INSERT INTO bankingtestdb.t_CustomerRegistrationDetails (
        accountNumber, userID, userName, userPassword, userEmailAddress, status
        ) VALUES (
        GetAccNumber, userIDIN, userNameIN, userPasswordIN, userEmailAddressIN, 'Pending Approval'
        );
        SET Message = CONCAT(GetAccNumber, ', is your account number. Thank you for banking with us.');
        END IF;
    END;
    SET ErrorStatus = (SELECT EXISTS (SELECT @full_errno) != NULL);
    IF (ErrorStatus)
    THEN
    SET Message = (SELECT @full_errno);
    END IF;
    SELECT Message;
END$$

DELIMITER ;
;






        # Stored Procedure 2. sp_CustomerLogin
USE `bankingtestdb`;
DROP procedure IF EXISTS `sp_CustomerLogin`;

USE `bankingtestdb`;
DROP procedure IF EXISTS `bankingtestdb`.`sp_CustomerLogin`;
;

DELIMITER $$
USE `bankingtestdb`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_CustomerLogin`(
IN accountNumberIN VARCHAR(100),
IN userPasswordIN VARCHAR(100)
)
BEGIN
DECLARE ResMessage VARCHAR(100);
DECLARE GetPassword VARCHAR(100);
IF EXISTS (SELECT accountNumber FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberIN AND status != 'Pending Approval')
THEN SET GetPassword = (SELECT userPassword FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberIN);
END IF;
IF (GetPassword = userPasswordIN)
THEN SET ResMessage = 'Login was Successful.';
ELSE SET ResMessage = 'Invalid Credentials, please contact the Bank for assistance.';
END IF;
SELECT ResMessage;
END$$

DELIMITER ;
;





        # Stored Procedure 3. sp_BankEmployeeRegistrationDetails
USE `bankingtestdb`;
DROP procedure IF EXISTS `sp_BankEmployeeRegistrationDetails`;

USE `bankingtestdb`;
DROP procedure IF EXISTS `bankingtestdb`.`sp_BankEmployeeRegistrationDetails`;
;

DELIMITER $$
USE `bankingtestdb`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_BankEmployeeRegistrationDetails`(
IN employeeIDIN BIGINT,
IN employeePasswordIN VARCHAR(100),
IN employeeNameIN VARCHAR(100),
IN employeeCapacityIN VARCHAR(100)
)
BEGIN
DECLARE Message VARCHAR(100);
DECLARE GetAccNumber BIGINT;
DECLARE ErrorStatus INT;
BEGIN
IF EXISTS(SELECT employeeID FROM bankingtestdb.t_BankEmployee WHERE employeeID = employeeIDIN)
THEN
SET Message = CONCAT(CONCAT('Employee with this ID NO. ', employeeIDIN), ', already exist.');
ELSE
GET diagnostics condition 1 @sqlstate = returned_sqlstate, @errno = mysql_errno, @text = message_text;
SET @full_errno = concat("ERRNO ", @errno, "(", @sqlstate, "):", @text);
INSERT INTO bankingtestdb.t_BankEmployee (
employeeID, employeePassword, employeeName, employeeCapacity
) VALUES (
employeeIDIN, employeePasswordIN, employeeNameIN, employeeCapacityIN
);
SET Message = CONCAT(employeeIDIN, ', is your employee curd number.');
END IF;
END;
SET ErrorStatus = (SELECT EXISTS (SELECT @full_errno) != NULL);
IF (ErrorStatus)
THEN
SET Message = (SELECT @full_errno);
END IF;
SELECT Message;
END$$

DELIMITER ;
;




        # Stored Procedure 4. sp_BankEmployeeLogin
USE `bankingtestdb`;
DROP procedure IF EXISTS `sp_BankEmployeeLogin`;

USE `bankingtestdb`;
DROP procedure IF EXISTS `bankingtestdb`.`sp_BankEmployeeLogin`;
;

DELIMITER $$
USE `bankingtestdb`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_BankEmployeeLogin`(
IN employeeIDIN VARCHAR(100),
IN employeePasswordIN VARCHAR(100)
)
BEGIN
DECLARE ResMessage VARCHAR(100);
DECLARE GetPassword VARCHAR(100);
IF EXISTS (SELECT employeeID FROM `bankingtestdb`.`t_BankEmployee` WHERE employeeID = employeeIDIN)
THEN SET GetPassword = (SELECT employeePassword FROM `bankingtestdb`.`t_BankEmployee` WHERE employeeID = employeeIDIN);
END IF;
IF (GetPassword = employeePasswordIN)
THEN SET ResMessage = 'Login was Successful.';
CALL  bankingtestdb.sp_CustomerValidationReminder();
ELSE SET ResMessage = 'Invalid Credentials, please try again.';
SELECT ResMessage;
END IF;
END$$

DELIMITER ;
;






        # Stored Procedure 5. sp_CustomerValidationReminder
USE `bankingtestdb`;
DROP procedure IF EXISTS `sp_CustomerValidationReminder`;

USE `bankingtestdb`;
DROP procedure IF EXISTS `bankingtestdb`.`sp_CustomerValidationReminder`;
;

DELIMITER $$
USE `bankingtestdb`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_CustomerValidationReminder`()
BEGIN
SELECT * FROM t_CustomerRegistrationDetails WHERE status = 'Pending Approval';
END$$

DELIMITER ;
;






        # Stored Procedure 6. sp_ApproveCustomer
USE `bankingtestdb`;
DROP procedure IF EXISTS `sp_ApproveCustomer`;

USE `bankingtestdb`;
DROP procedure IF EXISTS `bankingtestdb`.`sp_ApproveCustomer`;
;

DELIMITER $$
USE `bankingtestdb`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_ApproveCustomer`(
IN accountNumberIN VARCHAR(100)
)
BEGIN
DECLARE ResMessage VARCHAR(100);
IF EXISTS (SELECT accountNumber FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberIN)
THEN UPDATE `bankingtestdb`.`t_CustomerRegistrationDetails` SET status = 'Approved' WHERE accountNumber = accountNumberIN AND status = 'Pending Approval';
SET ResMessage = CONCAT('User with account number ', CONCAT(accountNumberIN, ', has been approved'));
END IF;
SELECT ResMessage;
END$$

DELIMITER ;
;






        # Stored Procedure 7. sp_CustomerTransactionDetails_Deposit
USE `bankingtestdb`;
DROP procedure IF EXISTS `sp_CustomerTransactionDetails_Deposit`;

USE `bankingtestdb`;
DROP procedure IF EXISTS `bankingtestdb`.`sp_CustomerTransactionDetails_Deposit`;
;

DELIMITER $$
USE `bankingtestdb`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_CustomerTransactionDetails_Deposit`(
IN accountNumberIN BIGINT,
IN amountIN BIGINT
)
BEGIN
DECLARE getTotalNum BIGINT;
DECLARE getAvailableBal BIGINT;
DECLARE getLoanBal BIGINT;
DECLARE ResMessage VARCHAR(900);
IF EXISTS (SELECT accountNumber FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberIN)
THEN
BEGIN
IF ((SELECT EXISTS (SELECT availableBalance FROM `bankingtestdb`.`t_CustomerTransactionDetails`WHERE accountNumber = accountNumberIN ) != 0))
THEN
SET getAvailableBal = (SELECT ((SELECT availableBalance FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE accountNumber = accountNumberIN AND ID = (SELECT MAX(ID) FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE accountNumber = accountNumberIN)) + amountIN));
ELSE        
SET getAvailableBal = amountIN;
END IF;
END;
SET getTotalNum = (SELECT(1 + (SELECT ID FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE ID = (SELECT MAX(ID) FROM `bankingtestdb`.`t_CustomerTransactionDetails`))));
IF ((SELECT EXISTS (SELECT 1 FROM `bankingtestdb`.`t_CustomerTransactionDetails`)) = 0)
THEN
SET getTotalNum = 0;
END IF;
INSERT INTO `bankingtestdb`.`t_CustomerTransactionDetails`(
ID, accountNumber, amount, createdOn, credit, debit, sentTo, receivedFrom, availableBalance
) VALUES (
getTotalNum, accountNumberIN, amountIN, (SELECT (current_date())), amountIN, 0, 'NULL', 'NULL', getAvailableBal
);
SET ResMessage = (SELECT (CONCAT(CONCAT('Amount ', CONCAT(amountIN,' has been credited to,')), accountNumberIN)));
ELSE SET ResMessage = (SELECT (CONCAT('User with this ', CONCAT(accountNumberIN,' account number does not exist, please contact customer service. Thank you.'))));
END IF;
SELECT ResMessage;
END$$

DELIMITER ;
;








        # Stored Procedure 8. sp_CustomerTransactionDetails_Withdraw
USE `bankingtestdb`;
DROP procedure IF EXISTS `sp_CustomerTransactionDetails_Withdraw`;

USE `bankingtestdb`;
DROP procedure IF EXISTS `bankingtestdb`.`sp_CustomerTransactionDetails_Withdraw`;
;

DELIMITER $$
USE `bankingtestdb`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_CustomerTransactionDetails_Withdraw`(
IN accountNumberIN VARCHAR(100),
IN amountIN INT
)
BEGIN
DECLARE getTotalNum INT;
DECLARE getAvailableBal INT;
DECLARE getLoanBal INT;
DECLARE getLoanStatus INT;
DECLARE getAvailableBalStatus INT;
DECLARE ResMessage VARCHAR(900);
IF EXISTS (SELECT accountNumber FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberIN)
THEN
SET getTotalNum = (SELECT(1 + (SELECT ID FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE ID = (SELECT MAX(ID) FROM `bankingtestdb`.`t_CustomerTransactionDetails`))));
SET getAvailableBal = ((SELECT availableBalance FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE accountNumber = accountNumberIN AND ID = ((SELECT MAX(ID) FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE accountNumber = accountNumberIN))));
SET getAvailableBalStatus = ((SELECT availableBalance FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE accountNumber = accountNumberIN AND ID = ((SELECT MAX(ID) FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE accountNumber = accountNumberIN))));
IF (getAvailableBalStatus)
THEN
IF (getAvailableBal < amountIN)
THEN
SET ResMessage = 'Your account balance is insufficient.';
ELSE
BEGIN
INSERT INTO `bankingtestdb`.`t_CustomerTransactionDetails`(
ID, accountNumber, amount, createdOn, credit, debit, sentTo, receivedFrom, availableBalance
) VALUES (
getTotalNum, accountNumberIN, amountIN, (SELECT (current_date())), 0, amountIN, 'NULL', 'NULL', (getAvailableBal  - amountIN)
);
SET ResMessage = CONCAT(CONCAT('Withdrawal of ', CONCAT(amountIN,' was successful from account,')), accountNumberIN);
END;
END IF;
ELSE
SET ResMessage = 'You have insufficient balance.';
END IF;
ELSE
SET ResMessage = CONCAT('User with this ', CONCAT(accountNumberIN,' account number does not exist, please contact customer service. Thank you.'));
END IF;
SELECT ResMessage;
END$$

DELIMITER ;
;









        # Stored Proceedure 9. sp_CustomerTransaction_TransferFunds
USE `bankingtestdb`;
DROP procedure IF EXISTS `sp_CustomerTransaction_TransferFunds`;

USE `bankingtestdb`;
DROP procedure IF EXISTS `bankingtestdb`.`sp_CustomerTransaction_TransferFunds`;
;

DELIMITER $$
USE `bankingtestdb`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_CustomerTransaction_TransferFunds`(
IN accountNumberIN BIGINT,
IN accountNumberToSendToIN BIGINT,
IN amountIN BIGINT
)
BEGIN
DECLARE getTotalNum INT;
DECLARE getAvailableBal INT;
DECLARE getLoanBal INT;
DECLARE getLoanStatus INT;
DECLARE getAvailableBalStatus INT;
DECLARE ResMessage VARCHAR(900);
IF EXISTS (SELECT accountNumber FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberIN)
THEN
SET getTotalNum = (SELECT(1 + (SELECT ID FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE ID = (SELECT MAX(ID) FROM `bankingtestdb`.`t_CustomerTransactionDetails`))));
SET getAvailableBal = ((SELECT availableBalance FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE accountNumber = accountNumberIN AND ID = ((SELECT MAX(ID) FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE accountNumber = accountNumberIN))));
SET getAvailableBalStatus = (SELECT EXISTS ((SELECT availableBalance FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE accountNumber = accountNumberIN AND ID = ((SELECT MAX(ID) FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE accountNumber = accountNumberIN)))) != 0);
IF (getAvailableBalStatus != 0)
THEN
IF (getAvailableBal < amountIN)
THEN
SET ResMessage = 'Your account balance is insufficient.';
ELSE
BEGIN
INSERT INTO `bankingtestdb`.`t_CustomerTransactionDetails`(
ID, accountNumber, amount, createdOn, credit, debit, sentTo, receivedFrom, availableBalance
) VALUES (
getTotalNum, accountNumberIN, amountIN, (SELECT (current_date())), 0, amountIN, accountNumberToSendToIN, 'NULL', (getAvailableBal  - amountIN)
);
#call sp_CustomerTransaction_Complete_TransferFunds to credit "accountNumberToSendToIN"
CALL `bankingtestdb`.`sp_CustomerTransaction_Complete_TransferFunds`(accountNumberToSendToIN, accountNumberIN, amountIN);
SET ResMessage = CONCAT(CONCAT('Amount ', CONCAT(amountIN,' was successful creadited to account,')), accountNumberToSendToIN);
END;
END IF;
ELSE
SET ResMessage = 'You have insufficient balance. 676';
END IF;
ELSE
SET ResMessage = CONCAT('User with this ', CONCAT(accountNumberIN,' account number does not exist, please contact customer service. Thank you.'));
END IF;
SELECT ResMessage;
END$$

DELIMITER ;
;









        # Stored Proceedure 10. sp_CustomerTransaction_Complete_TransferFunds
USE `bankingtestdb`;
DROP procedure IF EXISTS `sp_CustomerTransaction_Complete_TransferFunds`;

USE `bankingtestdb`;
DROP procedure IF EXISTS `bankingtestdb`.`sp_CustomerTransaction_Complete_TransferFunds`;
;

DELIMITER $$
USE `bankingtestdb`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_CustomerTransaction_Complete_TransferFunds`(
IN accountNumberIN BIGINT,
IN accountNumberReceivedFromIN BIGINT,
IN amountIN BIGINT
)
BEGIN
DECLARE getTotalNum BIGINT;
DECLARE getAvailableBal BIGINT;
DECLARE getLoanBal BIGINT;
DECLARE ResMessage VARCHAR(900);
IF EXISTS (SELECT accountNumber FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberIN)
THEN
BEGIN
IF ((SELECT EXISTS (SELECT availableBalance FROM `bankingtestdb`.`t_CustomerTransactionDetails`WHERE accountNumber = accountNumberIN ) != 0))
THEN
SET getAvailableBal = (SELECT ((SELECT availableBalance FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE accountNumber = accountNumberIN AND ID = (SELECT MAX(ID) FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE accountNumber = accountNumberIN)) + amountIN));
ELSE        
SET getAvailableBal = amountIN;
END IF;
END;
SET getTotalNum = (SELECT(1 + (SELECT ID FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE ID = (SELECT MAX(ID) FROM `bankingtestdb`.`t_CustomerTransactionDetails`))));
IF ((SELECT EXISTS (SELECT 1 FROM `bankingtestdb`.`t_CustomerTransactionDetails`)) = 0)
THEN
SET getTotalNum = 0;
END IF;
INSERT INTO `bankingtestdb`.`t_CustomerTransactionDetails`(
ID, accountNumber, amount, createdOn, credit, debit, sentTo, receivedFrom, availableBalance
) VALUES (
getTotalNum, accountNumberIN, amountIN, (SELECT (current_date())), amountIN, 0, 'NULL', accountNumberReceivedFromIN, getAvailableBal
);
SET ResMessage = (SELECT (CONCAT(CONCAT('Amount ', CONCAT(amountIN,' has been credited to,')), accountNumberIN)));
ELSE SET ResMessage = (SELECT (CONCAT('User with this ', CONCAT(accountNumberIN,' account number does not exist, please contact customer service. Thank you.'))));
END IF;
SELECT ResMessage;
END$$

DELIMITER ;
;







        # Stored Proceedure 11. sp_GetAvailableBalance
USE `bankingtestdb`;
DROP procedure IF EXISTS `sp_GetAvailableBalance`;

USE `bankingtestdb`;
DROP procedure IF EXISTS `bankingtestdb`.`sp_GetAvailableBalance`;
;

DELIMITER $$
USE `bankingtestdb`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_GetAvailableBalance`(
IN accountNumberIN BIGINT
)
BEGIN
DECLARE ResMessage VARCHAR(900);
IF EXISTS (SELECT accountNumber FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberIN)
THEN
SET ResMessage = CONCAT(CONCAT('Your balance is, ',((SELECT availableBalance FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE accountNumber = accountNumberIN AND ID = ((SELECT MAX(ID) FROM `bankingtestdb`.`t_CustomerTransactionDetails` WHERE accountNumber = accountNumberIN)) ))), '. Thank you for banking with us.');
ELSE
SET ResMessage = CONCAT('User with this account does not exist.');
END IF;
SELECT ResMessage;
END$$

DELIMITER ;
;








# 2. Postman Collection {Endpoint, Request-Body and Response}

        # POST - REQUEST 1. (Customer Registration)
http://localhost:8080/BankApi/CustomerRegistration

        #Request Body
{
    "UserID": "123456",
    "UserName": "Benjamin Sinzore",
    "UserPassword": "pass1234",
    "UserEmailAddress": "benjaminsinzore@gmail.com"
}

        #Response Positive
{
    "timestamp": "Sat Sep 10 17:10:53 EAT 2022",
    "status": 200,
    "error": null,
    "message": "100100123456, is your account number. Thank you for banking with us."
}

        #Response Negative
{
    "timestamp": "Sat Sep 10 17:11:47 EAT 2022",
    "status": 200,
    "error": null,
    "message": "User with this ID NO. 123456, already exist. Please contact customer care for help."
}



        # POST - REQUEST 2. (Customer Registration)
http://localhost:8080/BankApi/CustomerLogin

        #Request Body
{
    "AccountNumber": "100100123456",
    "UserPassword": "pass12"
}

        #Response Positive
{
    "timestamp": null,
    "status": 200,
    "error": "null.",
    "message": "Login was Successful."
}

        #Response Negative
{
    "timestamp": null,
    "status": 200,
    "error": "null.",
    "message": "Invalid Credentials, please contact the Bank for assistance."
}


        # POST - REQUEST 3. (Customer Approval)
http://localhost:8080/BankApi/CustomerApproval

        #Request Body
{
    "AccountNumber": "100100123456"
}

        #Response Positive
{
    "timestamp": null,
    "status": 200,
    "error": null,
    "message": "User with account number 100100123456, has been approved"
}


        # POST - REQUEST 4. (Bank Employee Registration)
http://localhost:8080/BankApi/BankEmployeeRegistration

        #Request Body
{
    "EmployeeID": "100100123456",
    "EmployeePassword": "pass12",
    "EmployeeName": "Benjamin Sinzore",
    "EmployeeCapacity": "Customer Care"
}

        #Response Positive
{
    "timestamp": null,
    "status": 200,
    "error": null,
    "message": "100100123456, is your employee curd number."
}

        #Response Negative
{
    "timestamp": null,
    "status": 200,
    "error": null,
    "message": "Employee with this ID NO. 100100123456, already exist."
}


        # POST - REQUEST 5. (Bank Employee Login And Return Customer Pending Approval)
http://localhost:8080/BankApi/BankEmployeeLoginAndReturnCustomerPendingApproval

        #Request Body
{
    "EmployeeID": "100100123456",
    "EmployeePassword": "pass12"
}

        #Response Positive
{
"status": 200,
"message": "Hello, here is a list of Customers Pending Approval.",
"error": "null",
"data": [
[
22,
"Ben",
"ben@12"
],
[
6932462,
"Benjamin",
"ben@123"
],
[
8142122,
"Benjamin",
"ben@123"
],
[
12532462,
"Benjamin",
"ben@123"
],
[
12833123,
"Benjamin",
"ben@123"
],
[
19730045,
"Benjamin",
"ben@123"
],
[
23412345,
"Benjamin",
"ben@123"
],
[
32846232,
"Benjamin",
"ben@123"
],
[
59873322,
"Benjamin",
"ben@123"
],
[
100100402,
"Benjamin",
"ben@123"
],
[
100100622,
"Benjamin",
"ben@123"
],
[
101062092,
"Benjamin",
"ben@12"
],
[
101062992,
"Benjamin",
"ben@12"
],
[
131223902,
"Benjamin",
"ben@123"
],
[
199702392,
"Benjamin",
"ben@123"
],
[
219151234,
"Benjamin",
"ben@123"
],
[
562112345,
"Benjamin",
"ben@123"
],
[
624524602,
"Benjamin",
"ben@123"
],
[
836131045,
"Benjamin",
"ben@123"
],
[
959383262,
"Benjamin",
"ben@123"
],
[
1001004002,
"Benjamin",
"ben@123"
],
[
10106299092,
"Benjamin",
"ben@12"
],
[
1001006211092,
"Benjamin",
"ben@12"
]
]
}

        #Response Negative
{
    "status": 200,
    "message": "Hello, here is a list of Customers Pending Approval.",
    "error": "null",
    "data": [
        "Invalid Credentials, please try again."
    ]
}


        # POST - REQUEST 6. (Customer Transaction Deposit)
http://localhost:8080/BankApi/CustomerTransactionDeposit

        #Request Body
{
    "AccountNumber": "100100123456",
    "Amount": "800000"
}

        #Response Positive
{
    "timestamp": null,
    "status": 200,
    "error": null,
    "message": "Amount 800000 has been credited to,100100123456"
}

        #Response Negative 1
{
    "timestamp": null,
    "status": 200,
    "error": null,
    "message": "User with this 1001001234561 account number does not exist, please contact customer service. Thank you."
}

        #Response Negative 2 (when amount is a string)
{
    "timestamp": "2022-09-11T08:55:31.943+00:00",
    "status": 400,
    "error": "Bad Request",
    "path": "/BankApi/CustomerTransactionDeposit"
}

        #Response Negative 3 (When amount is less or equal to 0)
{
    "timestamp": null,
    "status": 200,
    "error": null,
    "message": "Please check the amount before you proceed."
}


        # POST - REQUEST 7. (Customer Transaction Withdraw)
http://localhost:8080/BankApi/CustomerTransactionWithdraw

        #Request Body
{
    "AccountNumber": "100100123456",
    "Amount": "10000"
}

        #Response Positive
{
    "timestamp": null,
    "status": 200,
    "error": null,
    "message": "Withdrawal of 10000 was successful from account,100100123456"
}

        #Response Negative 1
{
    "timestamp": null,
    "status": 200,
    "error": null,
    "message": "User with this 1001001234561 account number does not exist, please contact customer service. Thank you."
}
        #Response Negative 2 (when amount is a string)
{
    "timestamp": "2022-09-11T08:55:31.943+00:00",
    "status": 400,
    "error": "Bad Request",
    "path": "/BankApi/CustomerTransactionDeposit"
}

        #Response Negative 3 (When amount is less or equal to 0)
{
    "timestamp": null,
    "status": 200,
    "error": null,
    "message": "Please check the amount before you proceed."
}

        #Response Negative 4 (Insufficient funds)
{
"timestamp": null,
"status": 200,
"error": null,
"message": "Your account balance is insufficient."
}


        # POST - REQUEST 8. (Customer Transaction Transfer Funds)
http://localhost:8080/BankApi/CustomerTransaction_TransferFunds

        #Request Body
{
    "AccountNumber": "100100123465",
    "AccountNumberToSendTo": "100100123456",
    "Amount": "500000"
}

        #Response Positive
{
    "timestamp": null,
    "status": 200,
    "error": null,
    "message": "Withdrawal of 10000 was successful from account,100100123456"
}

        #Response Negative 1 (insufficient balance)
{
    "timestamp": null,
    "status": 200,
    "error": null,
    "message": "Your account balance is insufficient."
}

        #Response Negative 2 (Wrong account number.)
{
    "timestamp": null,
    "status": 200,
    "error": null,
    "message": "User with this 1001001234645 account number does not exist, please contact customer service. Thank you."
}



        # POST - REQUEST 9. (Customer Transaction Transfer Funds)
http://localhost:8080/BankApi/CustomerTransaction_GetAvailableBalance

        #Request Body
{
"AccountNumber": "100100123465"
}

        #Response Positive
{
"timestamp": null,
"status": 200,
"error": null,
"message": "Your balance is, 433000. Thank you for banking with us."
}

        #Response Negative 1 (insufficient balance)
{
"timestamp": null,
"status": 200,
"error": null,
"message": "Your account balance is insufficient."
}

        #Response Negative 2 (wrong account number)
{
    "timestamp": null,
    "status": 200,
    "error": null,
    "message": "User with this account does not exist."
}







