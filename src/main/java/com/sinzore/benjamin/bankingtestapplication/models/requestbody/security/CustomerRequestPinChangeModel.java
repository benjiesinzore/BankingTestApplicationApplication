package com.sinzore.benjamin.bankingtestapplication.models.requestbody.security;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Data
public class CustomerRequestPinChangeModel {

    int userAccountNumber;
    String userName;
    String userPassword;
    String requestDate = new Date().toString();
}
