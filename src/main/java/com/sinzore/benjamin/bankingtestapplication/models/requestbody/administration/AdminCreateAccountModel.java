package com.sinzore.benjamin.bankingtestapplication.models.requestbody.administration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AdminCreateAccountModel {

    int employeeID;
    String employeeName;
    String employeePassword;
    String confirmPassword;
    String employeeCapacity;
}
