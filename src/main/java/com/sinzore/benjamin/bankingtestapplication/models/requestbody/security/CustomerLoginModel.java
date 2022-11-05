package com.sinzore.benjamin.bankingtestapplication.models.requestbody.security;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CustomerLoginModel {

    int userAccountNumber;
    String userPassword;

}
