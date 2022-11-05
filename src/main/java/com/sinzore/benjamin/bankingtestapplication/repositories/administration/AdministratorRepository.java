package com.sinzore.benjamin.bankingtestapplication.repositories.administration;


import com.sinzore.benjamin.bankingtestapplication.entity.AccValidationRemModel;
import com.sinzore.benjamin.bankingtestapplication.entity.AccValidationRemModelData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministratorRepository extends JpaRepository<AccValidationRemModel, Integer> {

    @Procedure(procedureName = "sp_BankEmployeeRegistrationDetails")
    String adminCreateAccount(

            @Param("employeeIDIN")
            int employeeIDIN,

            @Param("employeePasswordIN")
            String employeePasswordIN,

            @Param("employeeNameIN")
            String employeeNameIN,

            @Param("employeeCapacityIN")
            String employeeCapacityIN
    );

    @Procedure(procedureName = "sp_BankEmployeeLogin")
    String adminLogin(

            @Param("employeeIDIN")
            int employeeIDIN,

            @Param("employeePasswordIN")
            String employeePasswordIN
    );


    @Procedure(procedureName = "sp_ApproveCustomer")
    String validateCustomerAccount(

            @Param("accountNumberIN")
            int accountNumberIN
    );

    @Procedure(procedureName = "sp_BlockCustomerAcc")
    String blockCustomerAccount(

            @Param("accountNumberIN")
            String accountNumberIN,

            @Param("reasonForBlockIN")
            String reasonForBlockIN,

            @Param("dateInitiatedIN")
            String dateInitiatedIN,

            @Param("blockedByIN")
            String blockedByIN

    );

    @Procedure(name = "accountValidationReinder")
    List<AccValidationRemModelData> accountValidationReinder();


}
