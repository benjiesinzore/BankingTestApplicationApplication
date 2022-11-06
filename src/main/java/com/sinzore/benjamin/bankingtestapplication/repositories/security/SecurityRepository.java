package com.sinzore.benjamin.bankingtestapplication.repositories.security;


import com.sinzore.benjamin.bankingtestapplication.entity.MyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface SecurityRepository extends JpaRepository<MyEntity, Integer> {

    @Procedure(procedureName = "sp_CustomerRegistrationDetails")
    String customerRegistration(

            @Param("userIDIN")
            int userIDIN,

            @Param("userNameIN")
            String userNameIN,

            @Param("userPasswordIN")
            String userPasswordIN,

            @Param("userEmailAddressIN")
            String userEmailAddressIN
    );

    @Procedure(procedureName = "sp_CustomerLogin")
    String customerLogin(

            @Param("accountNumberIN")
            BigInteger accountNumberIN,

            @Param("userPasswordIN")
            String userPasswordIN
    );


}
