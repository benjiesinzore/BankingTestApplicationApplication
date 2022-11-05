package com.sinzore.benjamin.bankingtestapplication.models.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GlobalResponse {

    String timestamp = new Date().toString();
    int status = 200;
    String error = "null";
    String message = "null";
}
