package com.sinzore.benjamin.bankingtestapplication.services.administration;


import com.sinzore.benjamin.bankingtestapplication.entity.AccValidationRemModelData;
import com.sinzore.benjamin.bankingtestapplication.models.requestbody.administration.AdminCreateAccountModel;
import com.sinzore.benjamin.bankingtestapplication.models.requestbody.administration.AdminLoginModel;
import com.sinzore.benjamin.bankingtestapplication.models.requestbody.administration.BlockCustomerAccountModel;
import com.sinzore.benjamin.bankingtestapplication.models.requestbody.administration.ValidateCustomerAccModel;
import com.sinzore.benjamin.bankingtestapplication.models.responses.GlobalResponse;
import com.sinzore.benjamin.bankingtestapplication.models.responses.administration.AccValidationRemResp;
import com.sinzore.benjamin.bankingtestapplication.repositories.administration.AdministratorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Transactional
@Service
public class AdministratorService {

    @Autowired
    private final AdministratorRepository repository;
    public AdministratorService(AdministratorRepository repository) {
        this.repository = repository;
    }
    Logger logger = LoggerFactory.getLogger(AdministratorService.class);


    public ResponseEntity<GlobalResponse> adminCreateAccount(AdminCreateAccountModel model){

        GlobalResponse response = new GlobalResponse();
        String password = model.getEmployeePassword();
        String confirmPassword = model.getConfirmPassword();
        if (Objects.equals(password, confirmPassword)){

            try {

                String res;
                res = repository.adminCreateAccount(
                        model.getEmployeeID(),
                        model.getEmployeePassword(),
                        model.getEmployeeName(),
                        model.getEmployeeCapacity());
                response.setMessage(res);

                return new ResponseEntity<>(response, HttpStatus.OK);

            } catch (Exception ee){
                String error = ee.getMessage();

                logger.error(error);
                logger.info("Create Admin Endpoint : Administration Controller");
                response.setStatus(500);
                response.setError(error);
                response.setMessage("Internal Server Error.");

                return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
            }

        } else {

            response.setStatus(400);
            response.setError("Bad request.");
            response.setMessage("Please ensure the passwords match.");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }


    public ResponseEntity<GlobalResponse> adminLogin(AdminLoginModel model){

        GlobalResponse response = new GlobalResponse();
        String res;
        try {
            res = repository.adminLogin(
                    model.getEmployeeID(),
                    model.getEmployeePassword());
            response.setMessage(res);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ee){
            String error = ee.getMessage();
            logger.error(error);
            logger.info("Admin Login Endpoint : Administration Controller");
            response.setStatus(500);
            response.setError(error);
            response.setMessage("Internal Server Error.");

            return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
        }

    }

    public ResponseEntity<GlobalResponse> validateCustomerAccount(ValidateCustomerAccModel model){

        GlobalResponse response = new GlobalResponse();
        String res;
        try {
            res = repository.validateCustomerAccount(
                    model.getAccountNumber()
            );
            response.setMessage(res);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ee){
            String error = ee.getMessage();
            logger.error(error);
            logger.info("Validate Customer Account Endpoint : Administration Controller");
            response.setStatus(500);
            response.setError(error);
            response.setMessage("Internal Server Error.");

            return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
        }

    }


    public ResponseEntity<GlobalResponse> blockCustomerAccount(BlockCustomerAccountModel model){

        GlobalResponse response = new GlobalResponse();
        String res;
        try {
            res = repository.blockCustomerAccount(
                    model.getAccountNumber(),
                    model.getReasonForBlock(),
                    model.getDateInitiated(),
                    model.getBlockedBy()
            );

            response.setMessage(res);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ee){
            String error = ee.getMessage();
            logger.error(error);
            logger.info("Block Customer Account Endpoint : Administration Controller");
            response.setStatus(500);
            response.setError(error);
            response.setMessage("Internal Server Error.");

            return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);

        }

    }

    public ResponseEntity<AccValidationRemResp> accountValidationReinder(){

        AccValidationRemResp res = new AccValidationRemResp();
        List<AccValidationRemModelData> data;
        try {
            data = repository.accountValidationReinder();
            res.setData(data);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception ee){

            String error = ee.getMessage();
            logger.error(error);
            logger.info("Test SQL Mapping Endpoint : Administration Controller");
            res.setStatus(500);
            res.setError(error);
            return new ResponseEntity<>(res, HttpStatus.REQUEST_TIMEOUT);
        }
    }

}
