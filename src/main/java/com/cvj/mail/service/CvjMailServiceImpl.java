package com.cvj.mail.service;

import com.cvj.mail.models.MailRequestDto;
import com.cvj.mail.utils.MailSender;
import com.cvj.mail.utils.MailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class CvjMailServiceImpl implements CvjMailService{

    private final MailSender mailSender;

    public CvjMailServiceImpl(MailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public String sendMail(MailRequestDto mailRequestDto) {
        return mailSender.sendMail(mailRequestDto);
    }
}
