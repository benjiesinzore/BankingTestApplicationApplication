package com.sinzore.benjamin.bankingtestapplication.services.transactions;

import com.sinzore.benjamin.bankingtestapplication.models.requestbody.transactions.CashDepositModel;
import com.sinzore.benjamin.bankingtestapplication.models.requestbody.transactions.CashTransferModel;
import com.sinzore.benjamin.bankingtestapplication.models.requestbody.transactions.CashWithdrawModel;
import com.sinzore.benjamin.bankingtestapplication.models.requestbody.transactions.CheckAvailableBalanceModel;
import com.sinzore.benjamin.bankingtestapplication.models.responses.GlobalResponse;
import com.sinzore.benjamin.bankingtestapplication.repositories.transactions.TransactionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TransactionsService {

    @Autowired
    protected TransactionsRepository repository;
    public TransactionsService(TransactionsRepository repository) {
        this.repository = repository;
    }

    Logger logger = LoggerFactory.getLogger(TransactionsService.class);


    public ResponseEntity<GlobalResponse> cashDeposit(CashDepositModel model){

        GlobalResponse response = new GlobalResponse();
        String res;
        try {

            res = repository.cashDeposit(
                    model.getAccountNumber(),
                    model.getAmount()
            );
            response.setMessage(res);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception ee){
            String error = ee.getMessage();
            logger.error(error);
            logger.info("Cash Deposit Endpoint : Transaction Controller");
            response.setStatus(500);
            response.setError(error);
            response.setMessage("Internal Server Error.");

            return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);

        }


    }


    public ResponseEntity<GlobalResponse> cashWithdraw(CashWithdrawModel model){

        GlobalResponse response = new GlobalResponse();
        String res;
        try {
            res = repository.cashWithdraw(
                    model.getAccountNumber(),
                    model.getAmount(),
                    model.getUserPassword()
            );
            response.setMessage(res);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception ee){
            String error = ee.getMessage();
            logger.error(error);
            logger.info("Cash Withdrawal Endpoint : Transaction Controller");
            response.setStatus(500);
            response.setError(error);
            response.setMessage("Internal Server Error.");

            return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);

        }

    }


    public ResponseEntity<GlobalResponse> cashTransfer(CashTransferModel model){

        GlobalResponse response = new GlobalResponse();
        String res;
        try {

            res = repository.cashTransfer(
                    model.getAccountNumber(),
                    model.getAccountNumberToSendTo(),
                    model.getAmount(),
                    model.getUserPassword()
            );
            response.setMessage(res);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception ee){
            String error = ee.getMessage();
            logger.error(error);
            logger.info("Cash Transfer Endpoint : Transaction Controller");
            response.setStatus(500);
            response.setError(error);
            response.setMessage("Internal Server Error.");

            return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);

        }

    }


    public ResponseEntity<GlobalResponse> checkAvailableBalance(CheckAvailableBalanceModel model){

        GlobalResponse response = new GlobalResponse();
        String res;
        try {
            res = repository.checkAvailableBalance(
                    model.getAccountNumber(),
                    model.getUserPassword()
            );

            response.setMessage(res);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception ee){

            String error = ee.getMessage();
            logger.error(error);
            logger.info("Cash Deposit Endpoint : Transaction Controller");
            response.setStatus(500);
            response.setError(error);
            response.setMessage("Internal Server Error.");

            return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);

        }

    }

}










