package com.sinzore.benjamin.bankingtestapplication.services.security;

import com.sinzore.benjamin.bankingtestapplication.models.requestbody.security.CustomerLoginModel;
import com.sinzore.benjamin.bankingtestapplication.models.requestbody.security.CustomerRegModel;
import com.sinzore.benjamin.bankingtestapplication.models.requestbody.security.CustomerRequestPinChangeModel;
import com.sinzore.benjamin.bankingtestapplication.models.responses.GlobalResponse;
import com.sinzore.benjamin.bankingtestapplication.repositories.security.SecurityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Transactional
@Service
public class SecurityService {

    @Autowired
    protected final SecurityRepository repository;

    SecurityService(SecurityRepository repository) {
        this.repository = repository;
    }

    Logger logger = LoggerFactory.getLogger(SecurityService.class);

    public ResponseEntity<GlobalResponse> customerRegistration(CustomerRegModel model){

        GlobalResponse response = new GlobalResponse();
        String password = model.getUserPassword();
        String conPassword = model.getConfirmPassword();
        if (Objects.equals(password, conPassword)){

            String res;
            try {
                res = repository.customerRegistration(
                        model.getUserID(),
                        model.getUserName(),
                        model.getUserPassword(),
                        model.getUserEmailAddress());

                response.setMessage(res);

                return new ResponseEntity<>(response, HttpStatus.OK);

            } catch (Exception ee){

                String error = ee.getMessage();
                logger.error(error);
                logger.info("Customer Registration Endpoint : Security Controller");
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

    public ResponseEntity<GlobalResponse> customerLogin(CustomerLoginModel model){


        GlobalResponse response = new GlobalResponse();
        String res;
        try {
            res = repository.customerLogin(
                    model.getUserAccountNumber(),
                    model.getUserPassword());

            response.setMessage(res);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception ee){
            String error = ee.getMessage();
            logger.error(error);
            logger.info("Customer Login Endpoint : Security Controller");
            response.setStatus(500);
            response.setError(error);
            response.setMessage("Internal Server Error.");

            return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);

        }

    }


}
