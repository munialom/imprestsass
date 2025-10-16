package com.ctecx.brs.tenant.util;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Component;

@Component
public class SmsUtility {

    private static final String API_KEY = "a1e4d99bdcb95545251091c705d6a20543163e0a6c4bbb446565a6fb90b029db";
    private static final String BASE_URL = "https://api.africastalking.com/version1/messaging";

    public static void sendSms(String phone, String textM) {
        try {
            HttpResponse<String> response = Unirest.post(BASE_URL)
                    .header("apiKey", API_KEY)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept", "application/json")
                    .field("username", "saccomode")
                    .field("to", phone)
                    .field("message", textM)
                    .field("from", SmsConfig.getSenderId()) // Use the injected senderId here
                    .field("enqueue", "1")
                    .asString();

            System.out.println("Current Response is: " + response.getStatusText());
        } catch (UnirestException ex) {
            handleException(ex);
        }
    }

    private static void handleException(UnirestException ex) {
        ex.printStackTrace(); // Print the exception for error handling
        // You can add more sophisticated error handling here if needed
    }
}
