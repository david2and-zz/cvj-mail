package com.cvj.mail.service;

import com.cvj.mail.models.MailRequestDto;

public interface CvjMailService {

    String sendMail(MailRequestDto mailRequestDto);
}
