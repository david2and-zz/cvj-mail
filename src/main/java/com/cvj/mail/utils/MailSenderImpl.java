package com.cvj.mail.utils;

import com.cvj.mail.models.MailRequestDto;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.activation.FileDataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@ConfigurationProperties("mail.properties.smtp")
@Service
@Setter
public class MailSenderImpl implements MailSender{

    private String host;
    private Integer port;
    private String username;
    private String password;
    private final String SENDER = "OWNER";
    private final String SUCCESS = "MAIL ENVIADO EXITOSAMENTE";
    private final String INTRO_SUBJECT = "SOLICITUD DE ESTUDIANTE ";
    private final String IMAGE = "escudo";

    public String sendMail(MailRequestDto mailRequestDto) {


        Email email = EmailBuilder.startingBlank()
                .from(SENDER, username)
                .bcc(mailRequestDto.getAdviserName(), mailRequestDto.getAdviserMail())
                .withSubject(INTRO_SUBJECT + mailRequestDto.getStudentName())
                .withEmbeddedImage(IMAGE, new FileDataSource("src/main/resources/static/tiara_javeriana.png"))
                .withHTMLText(this.getStringHTMLText(mailRequestDto))
                .buildEmail();


        Mailer mailer = MailerBuilder
                .withSMTPServerHost(host)
                .withSMTPServerPort(port)
                .withSMTPServerUsername(username)
                .withSMTPServerPassword(password)
                .withTransportStrategy(TransportStrategy.SMTP)
                .withDebugLogging(true)
                .buildMailer();
        mailer.sendMail(email);
        return SUCCESS;
    }
    private static String encodeFileToBase64Binary(File file) throws Exception{
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        fileInputStreamReader.read(bytes);
        return new String(Base64.encodeBase64(bytes), "UTF-8");
    }

    private String getStringHTMLText(MailRequestDto mailRequestDto){
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(p);

        VelocityContext context = new VelocityContext();

        context.put("student_email", mailRequestDto.getStudentMail());
        context.put("student_name", mailRequestDto.getStudentName());
        context.put("adviser_mail", mailRequestDto.getAdviserMail());
        context.put("adviser_name", mailRequestDto.getAdviserName());
        context.put("message",mailRequestDto.getStudentMsg());


        Template template = null;
        try {

            template = Velocity.getTemplate("templates/mailTemplate.vm");
        } catch (ResourceNotFoundException rnfe) {
            rnfe.printStackTrace();
        } catch (ParseErrorException pee) {
            pee.printStackTrace();
        } catch (MethodInvocationException mie) {
        } catch (Exception e) {
        }
        StringWriter templateString = new StringWriter();
        template.merge(context,templateString);

        return templateString.toString();
    }


}
