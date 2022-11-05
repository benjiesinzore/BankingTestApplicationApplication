package com.sinzore.benjamin.bankingtestapplication.models.requestbody.transactions;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Data
@Getter
@Setter
public class CashTransferModel {

    BigInteger accountNumber;
    BigInteger accountNumberToSendTo;
    double amount;
    String userPassword;
}
