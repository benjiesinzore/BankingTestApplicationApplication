package com.sinzore.benjamin.bankingtestapplication.models.requestbody.security;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@Data
public class CustomerLoginModel {

    BigInteger userAccountNumber;
    String userPassword;

}
