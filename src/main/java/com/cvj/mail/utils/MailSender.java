package com.cvj.mail.utils;

import com.cvj.mail.models.MailRequestDto;

public interface MailSender {
    String sendMail(MailRequestDto mailRequestDto);
}
