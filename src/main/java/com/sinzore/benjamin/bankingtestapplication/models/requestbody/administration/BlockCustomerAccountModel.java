package com.sinzore.benjamin.bankingtestapplication.models.requestbody.administration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BlockCustomerAccountModel {

    String accountNumber;
    String reasonForBlock;
    String dateInitiated;
    String blockedBy;
}
