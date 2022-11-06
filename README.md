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
foreign key (accountNumber) REFERENCES t_CustomerRegistrationDetails (accountNumber),
amount decimal(65, 2) NOT NULL,
createdOn VARCHAR(100)  NOT NULL,
credit decimal(65, 2) NOT NULL,
debit decimal(65, 2)  NOT NULL,
sentTo VARCHAR(100) NOT NULL,
receivedFrom VARCHAR(100) NOT NULL,
availableBalance decimal(65, 2) NOT NULL
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
IN userEmailAddressIN VARCHAR(100),
OUT Message VARCHAR(900)
)
BEGIN
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
IN accountNumberIN bigint,
IN userPasswordIN VARCHAR(100),
OUT ResMessage VARCHAR(900)
)
BEGIN
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
IN employeeCapacityIN VARCHAR(100),
OUT Message VARCHAR(100)
)
BEGIN
DECLARE EmployeeCardNumber BIGINT;
DECLARE ErrorStatus INT;
BEGIN
IF EXISTS(SELECT employeeID FROM bankingtestdb.t_bankadminregistrationdetails WHERE employeeID = employeeIDIN)
THEN
SET Message = CONCAT(CONCAT('Employee with this ID NO. ', employeeIDIN), ', already exist.');
ELSE
GET diagnostics condition 1 @sqlstate = returned_sqlstate, @errno = mysql_errno, @text = message_text;
SET @full_errno = concat("ERRNO ", @errno, "(", @sqlstate, "):", @text);
SET EmployeeCardNumber = CONCAT('111100', employeeIDIN);
INSERT INTO bankingtestdb.t_bankadminregistrationdetails (
employeeID,
employeePassword,
employeeName,
employeeCapacity
) VALUES (
EmployeeCardNumber,
employeePasswordIN,
employeeNameIN,
employeeCapacityIN
);
SET Message = CONCAT(EmployeeCardNumber, ', is your office employee number.');
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
IN employeePasswordIN VARCHAR(100),
out ResMessage VARCHAR(900)
)
BEGIN
DECLARE GetPassword VARCHAR(100);
IF EXISTS (SELECT employeeID FROM `bankingtestdb`.`t_bankadminregistrationdetails` WHERE employeeID = employeeIDIN)
THEN SET GetPassword = (SELECT employeePassword FROM `bankingtestdb`.`t_bankadminregistrationdetails` WHERE employeeID = employeeIDIN);
END IF;
IF (GetPassword = employeePasswordIN)
THEN SET ResMessage = 'Login was Successful.';
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
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_CustomerValidationReminder`(

)
BEGIN
SELECT
accountNumber, #as valueOne#,
userName,
userEmailAddress
FROM t_CustomerRegistrationDetails WHERE status = 'Pending Approval';
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
IN accountNumberIN VARCHAR(100),
OUT ResMessage VARCHAR(100)
)
BEGIN
IF EXISTS (SELECT accountNumber FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberIN)
THEN UPDATE `bankingtestdb`.`t_CustomerRegistrationDetails` SET status = 'Approved' WHERE accountNumber = accountNumberIN AND status = 'Pending Approval';
SET ResMessage = CONCAT('User with account number ', CONCAT(accountNumberIN, ', has been approved'));
ELSE
SET ResMessage = CONCAT('User with account number ', CONCAT(accountNumberIN, ', does not exist.'));
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
IN amountIN double,
OUT ResMessage VARCHAR(900)
)
BEGIN
DECLARE getTotalNum BIGINT;
DECLARE getAvailableBal double;
DECLARE getLoanBal double;
IF EXISTS (SELECT accountNumber FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberIN)
THEN
BEGIN
IF ((SELECT EXISTS (SELECT availableBalance FROM `bankingtestdb`.`t_customertransactiondetails`WHERE accountNumber = accountNumberIN ) != 0))
THEN
SET getAvailableBal = (SELECT ((SELECT availableBalance FROM `bankingtestdb`.`t_customertransactiondetails` WHERE accountNumber = accountNumberIN AND ID = (SELECT MAX(ID) FROM `bankingtestdb`.`t_customertransactiondetails` WHERE accountNumber = accountNumberIN)) + amountIN));
ELSE        
SET getAvailableBal = amountIN;
END IF;
END;
SET getTotalNum = (SELECT(1 + (SELECT ID FROM `bankingtestdb`.`t_customertransactiondetails` WHERE ID = (SELECT MAX(ID) FROM `bankingtestdb`.`t_customertransactiondetails`))));
IF ((SELECT EXISTS (SELECT 1 FROM `bankingtestdb`.`t_customertransactiondetails`)) = 0)
THEN
SET getTotalNum = 0;
END IF;
INSERT INTO `bankingtestdb`.`t_customertransactiondetails`(
ID,
accountNumber,
amount,
createdOn,
credit,
debit,
sentTo,
receivedFrom,
availableBalance
) VALUES (
getTotalNum,
accountNumberIN,
amountIN,
(SELECT (current_date())),
amountIN,
0,
'NULL',
'NULL',
getAvailableBal
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
IN accountNumberIN BIGINT,
IN amountIN double,
IN userPasswordIN VARCHAR(900),
OUT ResMessage VARCHAR(900)
)
BEGIN
DECLARE getTotalNum INT;
DECLARE getAvailableBal double;
DECLARE getLoanBal double;
DECLARE getLoanStatus INT;
DECLARE getAvailableBalStatus INT;
    IF EXISTS (SELECT accountNumber FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberIN AND userPassword = userPasswordIN)
		THEN
			SET getTotalNum = (SELECT(1 + (SELECT ID FROM `bankingtestdb`.`t_customertransactiondetails` WHERE ID = (SELECT MAX(ID) FROM `bankingtestdb`.`t_customertransactiondetails`))));
			SET getAvailableBal = ((SELECT availableBalance FROM `bankingtestdb`.`t_customertransactiondetails` WHERE accountNumber = accountNumberIN AND ID = ((SELECT MAX(ID) FROM `bankingtestdb`.`t_customertransactiondetails` WHERE accountNumber = accountNumberIN))));
			SET getAvailableBalStatus = ((SELECT availableBalance FROM `bankingtestdb`.`t_customertransactiondetails` WHERE accountNumber = accountNumberIN AND ID = ((SELECT MAX(ID) FROM `bankingtestdb`.`t_customertransactiondetails` WHERE accountNumber = accountNumberIN))));
			IF (getAvailableBalStatus)
				THEN
                    IF (getAvailableBal < amountIN)
				THEN
					SET ResMessage = 'Your account balance is insufficient.';
				ELSE
					BEGIN
						INSERT INTO `bankingtestdb`.`t_customertransactiondetails`(
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
			SET ResMessage = CONCAT('User with this ', CONCAT(accountNumberIN,' account number does not exist. Please check your check your Account number and Password and try again or contact customer service. Thank you.'));
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
IN amountIN double,
IN userPasswordIN VARCHAR(900),
OUT ResMessage VARCHAR(900)
)
BEGIN
DECLARE getTotalNum INT;
DECLARE getAvailableBal double;
DECLARE getAvailableBalStatus INT;
	IF EXISTS (SELECT accountNumber FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberIN AND userPassword = userPasswordIN AND (SELECT exists(SELECT accountNumber FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberToSendToIN AND status = 'Approved')))
		THEN
			SET getTotalNum = (SELECT(1 + (SELECT ID FROM `bankingtestdb`.`t_customertransactiondetails` WHERE ID = (SELECT MAX(ID) FROM `bankingtestdb`.`t_customertransactiondetails`))));
			SET getAvailableBal = ((SELECT availableBalance FROM `bankingtestdb`.`t_customertransactiondetails` WHERE accountNumber = accountNumberIN AND ID = ((SELECT MAX(ID) FROM `bankingtestdb`.`t_customertransactiondetails` WHERE accountNumber = accountNumberIN))));
			SET getAvailableBalStatus = (SELECT EXISTS ((SELECT availableBalance FROM `bankingtestdb`.`t_customertransactiondetails` WHERE accountNumber = accountNumberIN AND ID = ((SELECT MAX(ID) FROM `bankingtestdb`.`t_customertransactiondetails` WHERE accountNumber = accountNumberIN)))) != 0);
			IF (getAvailableBalStatus != 0)
				THEN
                    IF (getAvailableBal < amountIN)
				THEN
					SET ResMessage = 'Your account balance is insufficient.';
				ELSE
					BEGIN                    
                    IF (accountNumberIN != accountNumberToSendToIN)
                    THEN
						INSERT INTO `bankingtestdb`.`t_customertransactiondetails`(
							ID, accountNumber, amount, createdOn, credit, debit, sentTo, receivedFrom, availableBalance
						) VALUES (
							getTotalNum, accountNumberIN, amountIN, (SELECT (current_date())), 0, amountIN, accountNumberToSendToIN, 'NULL', (getAvailableBal  - amountIN)
						);                        
                        CALL `bankingtestdb`.`sp_CustomerTransaction_Complete_TransferFunds`(accountNumberToSendToIN, accountNumberIN, amountIN, @ResMessage);
					ELSE
                        SET ResMessage = 'Please check your account number and the resipints acount number, they can not be the same.';
					END IF;
                    END;
			END IF;
                ELSE
					SET ResMessage = 'You have insufficient balance.';
                END IF;
		ELSE 
			SET ResMessage = CONCAT('User with this ', CONCAT(accountNumberIN,' account number does not exist, or your login details are incorect. Please contact customer service. Thank you.'));
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
IN amountIN double,
OUT ResMessage VARCHAR(900)
)
BEGIN
DECLARE getTotalNum BIGINT;
DECLARE getAvailableBal double;
DECLARE ResMessage VARCHAR(900);
IF EXISTS (SELECT accountNumber FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberIN)
THEN
BEGIN
IF ((SELECT EXISTS (SELECT availableBalance FROM `bankingtestdb`.`t_customertransactiondetails`WHERE accountNumber = accountNumberIN ) != 0))
THEN
SET getAvailableBal = (SELECT ((SELECT availableBalance FROM `bankingtestdb`.`t_customertransactiondetails` WHERE accountNumber = accountNumberIN AND ID = (SELECT MAX(ID) FROM `bankingtestdb`.`t_customertransactiondetails` WHERE accountNumber = accountNumberIN)) + amountIN));
ELSE        
SET getAvailableBal = amountIN;
END IF;
END;
SET getTotalNum = (SELECT(1 + (SELECT ID FROM `bankingtestdb`.`t_customertransactiondetails` WHERE ID = (SELECT MAX(ID) FROM `bankingtestdb`.`t_customertransactiondetails`))));
IF ((SELECT EXISTS (SELECT 1 FROM `bankingtestdb`.`t_customertransactiondetails`)) = 0)
THEN
SET getTotalNum = 0;
END IF;
INSERT INTO `bankingtestdb`.`t_customertransactiondetails`(
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
IN accountNumberIN BIGINT,
IN userPasswordIN VARCHAR(100),
OUT ResMessage VARCHAR(900)
)
BEGIN
    IF EXISTS (SELECT accountNumber FROM `bankingtestdb`.`t_CustomerRegistrationDetails` WHERE accountNumber = accountNumberIN AND userPassword = userPasswordIN)
    THEN
    SET ResMessage = CONCAT(CONCAT('Your balance is, ',((SELECT availableBalance FROM `bankingtestdb`.`t_customertransactiondetails` WHERE accountNumber = accountNumberIN AND ID = ((SELECT MAX(ID) FROM `bankingtestdb`.`t_customertransactiondetails` WHERE accountNumber = accountNumberIN)) ))), '. Thank you for banking with us.');
    ELSE
    SET ResMessage = CONCAT('User with this account number ', CONCAT(accountNumberIN, ' does not exist.'));
    END IF;
    SELECT ResMessage;
END$$

DELIMITER ;
;










# 2. Postman Collection {Endpoint, Request-Body and Response}

{
"info": {
"_postman_id": "54e66603-1f2f-45d8-b532-9a549440e6d1",
"name": "BankingTestApplication",
"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
},
"item": [
{
"name": "Administration",
"item": [
{
"name": "AdminCreateAccount",
"request": {
"method": "POST",
"header": [],
"body": {
"mode": "raw",
"raw": "{\r\n    \"employeeID\":12301,\r\n    \"employeeName\":\"Benjamin Sinzore\",\r\n    \"employeePassword\":\"pass1234\",\r\n    \"confirmPassword\":\"pass1234\",\r\n    \"employeeCapacity\":\"Managing Director\"\r\n}",
"options": {
"raw": {
"language": "json"
}
}
},
"url": {
"raw": "http://localhost:8083/BankingTest/AdminCreateAccount",
"protocol": "http",
"host": [
"localhost"
],
"port": "8083",
"path": [
"BankingTest",
"AdminCreateAccount"
]
}
},
"response": []
},
{
"name": "AdminLogin",
"request": {
"method": "POST",
"header": [],
"body": {
"mode": "raw",
"raw": "{\r\n    \"employeeID\":11110012301,\r\n    \"employeePassword\":\"pass1234\"\r\n}",
"options": {
"raw": {
"language": "json"
}
}
},
"url": {
"raw": "http://localhost:8083/BankingTest/AdminLogin",
"protocol": "http",
"host": [
"localhost"
],
"port": "8083",
"path": [
"BankingTest",
"AdminLogin"
]
}
},
"response": []
},
{
"name": "ValidateCustomerAccount",
"request": {
"method": "POST",
"header": [],
"body": {
"mode": "raw",
"raw": "{\r\n    \"accountNumber\":10010010\r\n}",
"options": {
"raw": {
"language": "json"
}
}
},
"url": {
"raw": "http://localhost:8083/BankingTest/ValidateCustomerAccount",
"protocol": "http",
"host": [
"localhost"
],
"port": "8083",
"path": [
"BankingTest",
"ValidateCustomerAccount"
]
}
},
"response": []
},
{
"name": "AccountValidationReinder",
"request": {
"method": "GET",
"header": [],
"url": {
"raw": "http://localhost:8083/BankingTest/AccountValidationReinder",
"protocol": "http",
"host": [
"localhost"
],
"port": "8083",
"path": [
"BankingTest",
"AccountValidationReinder"
]
}
},
"response": []
}
]
},
{
"name": "Security",
"item": [
{
"name": "CustomerRegistration",
"request": {
"method": "POST",
"header": [],
"body": {
"mode": "raw",
"raw": "{\r\n    \"userID\":1230,\r\n    \"userName\":\"Benjamin Sinzore\",\r\n    \"userEmailAddress\":\"benjaminsinzore@gmail.com\",\r\n    \"userPassword\":\"pass1234\",\r\n    \"confirmPassword\":\"pass1234\"\r\n}",
"options": {
"raw": {
"language": "json"
}
}
},
"url": {
"raw": "http://localhost:8083/BankingTest/CustomerRegistration",
"protocol": "http",
"host": [
"localhost"
],
"port": "8083",
"path": [
"BankingTest",
"CustomerRegistration"
]
}
},
"response": []
},
{
"name": "CustomerLogin",
"request": {
"method": "POST",
"header": [],
"body": {
"mode": "raw",
"raw": "{\r\n    \"userAccountNumber\":1001001230,\r\n    \"userPassword\":\"pass1234\"\r\n}",
"options": {
"raw": {
"language": "json"
}
}
},
"url": {
"raw": "http://localhost:8083/BankingTest/CustomerLogin",
"protocol": "http",
"host": [
"localhost"
],
"port": "8083",
"path": [
"BankingTest",
"CustomerLogin"
]
}
},
"response": []
}
]
},
{
"name": "Transactions",
"item": [
{
"name": "CashWithdraw",
"request": {
"method": "POST",
"header": [],
"body": {
"mode": "raw",
"raw": "{\r\n    \"accountNumber\":1001001230,\r\n    \"amount\":5000.133,\r\n    \"userPassword\":\"pass1234\"\r\n}",
"options": {
"raw": {
"language": "json"
}
}
},
"url": {
"raw": "http://localhost:8083/BankingTest/CashWithdraw",
"protocol": "http",
"host": [
"localhost"
],
"port": "8083",
"path": [
"BankingTest",
"CashWithdraw"
]
}
},
"response": []
},
{
"name": "CashDeposit",
"request": {
"method": "POST",
"header": [],
"body": {
"mode": "raw",
"raw": "{\r\n    \"accountNumber\":1001001230,\r\n    \"amount\":10000.133\r\n}",
"options": {
"raw": {
"language": "json"
}
}
},
"url": {
"raw": "http://localhost:8083/BankingTest/CashDeposit",
"protocol": "http",
"host": [
"localhost"
],
"port": "8083",
"path": [
"BankingTest",
"CashDeposit"
]
}
},
"response": []
},
{
"name": "CashTransfer",
"request": {
"method": "POST",
"header": [],
"body": {
"mode": "raw",
"raw": "{\r\n    \"accountNumber\":1001001230,\r\n    \"accountNumberToSendTo\":10010010,\r\n    \"amount\":100.05,\r\n    \"userPassword\":\"pass1234\"\r\n}",
"options": {
"raw": {
"language": "json"
}
}
},
"url": {
"raw": "http://localhost:8083/BankingTest/CashTransfer",
"protocol": "http",
"host": [
"localhost"
],
"port": "8083",
"path": [
"BankingTest",
"CashTransfer"
]
}
},
"response": []
},
{
"name": "CheckAvailableBalance",
"request": {
"method": "POST",
"header": [],
"body": {
"mode": "raw",
"raw": "{\r\n    \"accountNumber\":10010010,\r\n    \"userPassword\":\"\"\r\n}",
"options": {
"raw": {
"language": "json"
}
}
},
"url": {
"raw": "http://localhost:8083/BankingTest/CheckAvailableBalance",
"protocol": "http",
"host": [
"localhost"
],
"port": "8083",
"path": [
"BankingTest",
"CheckAvailableBalance"
]
}
},
"response": []
}
]
}
]
}







