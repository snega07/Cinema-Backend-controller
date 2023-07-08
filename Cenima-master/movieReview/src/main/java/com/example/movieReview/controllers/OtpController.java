package com.example.movieReview.controllers;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.movieReview.models.UserRepository;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Controller
@ResponseBody
public class OtpController {

  @Value("${twilio.account.sid}")
  private String twilioAccountSid;

  @Value("${twilio.auth.token}")
  private String twilioAuthToken;

  @Value("${twilio.phone.number}")
  private String twilioPhoneNumber;

  @GetMapping("/api/send-otp")
  public ResponseEntity<String> sendOTP(@RequestParam String phoneNo) {
    int otp = new Random().nextInt(999999);

    Twilio.init(twilioAccountSid, twilioAuthToken);
    try {
      Message message = Message.creator(
          new PhoneNumber(phoneNo),
          new PhoneNumber(twilioPhoneNumber),
          "Your OTP is: " + otp).create();
      String sid = message.getSid();

    } catch (ApiException e) {
      System.out.println(e.getMessage());
    }
    return ResponseEntity.ok("OTP Send to your Number " + phoneNo);
  }

}
