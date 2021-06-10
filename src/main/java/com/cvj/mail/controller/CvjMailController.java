package com.cvj.mail.controller;

import com.cvj.mail.models.MailRequestDto;
import com.cvj.mail.service.CvjMailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CvjMailController {

    private final CvjMailService cvjMailService;

    public CvjMailController(CvjMailService cvjMailService) {
        this.cvjMailService = cvjMailService;
    }

    @PostMapping("/mail")
    public ResponseEntity<String> sendMail(@RequestBody MailRequestDto mailRequestDto){
        String response = cvjMailService.sendMail(mailRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
