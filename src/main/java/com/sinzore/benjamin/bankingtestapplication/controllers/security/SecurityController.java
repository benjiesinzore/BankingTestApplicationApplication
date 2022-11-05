package com.sinzore.benjamin.bankingtestapplication.controllers.security;

import com.sinzore.benjamin.bankingtestapplication.models.requestbody.security.CustomerLoginModel;
import com.sinzore.benjamin.bankingtestapplication.models.requestbody.security.CustomerRegModel;
import com.sinzore.benjamin.bankingtestapplication.models.requestbody.security.CustomerRequestPinChangeModel;
import com.sinzore.benjamin.bankingtestapplication.models.responses.GlobalResponse;
import com.sinzore.benjamin.bankingtestapplication.services.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/BankingTest")
public class SecurityController {

    @Autowired
    private final SecurityService service;
    private SecurityController(SecurityService service) {
        this.service = service;
    }
    protected ResponseEntity<GlobalResponse> response;

    @PostMapping(value = "/CustomerRegistration")
    public ResponseEntity<GlobalResponse> customerRegistration(
            @RequestBody CustomerRegModel model){

        response = service.customerRegistration(model);
        return response;
    }

    @PostMapping("/CustomerLogin")
    public ResponseEntity<GlobalResponse> customerLogin(
            @RequestBody CustomerLoginModel model){

        response = service.customerLogin(model);
        return response;
    }

    @PostMapping("/CustomerRequestPinChange")
    public ResponseEntity<GlobalResponse> customerRequestPinChange(
            @RequestBody CustomerRequestPinChangeModel model){

        response = service.customerRequestPinChange(model);
        return response;
    }


}
