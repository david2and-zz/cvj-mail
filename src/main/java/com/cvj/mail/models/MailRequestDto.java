package com.cvj.mail.models;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MailRequestDto {
    private String studentName;
    private String studentMail;
    private String studentMsg;
    private String adviserMail;
    private String adviserName;
}
