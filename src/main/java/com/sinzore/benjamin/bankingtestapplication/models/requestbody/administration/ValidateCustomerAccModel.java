package com.sinzore.benjamin.bankingtestapplication.models.requestbody.administration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@Data
public class ValidateCustomerAccModel {

    BigInteger accountNumber;
}
