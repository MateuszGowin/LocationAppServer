package pl.gowin.location_app_server.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    private JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendRecoverPasswordToken(String to, String link) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(to);
        helper.setSubject("Zmiana hasło w LocationApp");
        helper.setText("<p>Aby zmienić hasło kliknij w link:</p>"
                +"<p><a href=\""+link+"\">Zmien hasło</p>",true);
        mailSender.send(message);
    }

    public void sendVerifyToken(String to, String link) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(to);
        helper.setSubject("Utworzono konto w Locationapp");
        helper.setText("<p>Aby aktywować konto kliknij w link:</p>"
                +"<p><a href=\""+link+"\">Aktywuj konto</p>",true);
        mailSender.send(message);
    }
}
