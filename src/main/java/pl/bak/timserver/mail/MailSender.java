package pl.bak.timserver.mail;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.bak.timserver.training.domain.Training;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
public class MailSender {

    private static Logger logger = LoggerFactory.getLogger(MailSender.class);
    private static final String mailUser = "tim.best.personal.trainer@gmail.com";
    private static final String mailPassword = "timtimtim";


    public static void sendMail(String messageBody, String to) {
        try {
            MimeMessage message = new MimeMessage(setUpSession());
            message.setFrom(new InternetAddress(mailUser));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Personal-trainer-tim");
            message.setText(messageBody);
            Transport.send(message);
            logger.info("Email was send to : " + to);
        } catch (MessagingException e) {
            logger.info("Cannot send email to : " + to);
        }
    }

    public static void acceptTraining(Training training) {
        try {
            MimeMessage message = new MimeMessage(setUpSession());
            message.setFrom(new InternetAddress(mailUser));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(training.getCustomer().getEmail()));
            message.setSubject("Your training was accepted");
            message.setText("Hi, \n We are pleased to inform you, that your training with "
                    + training.getCoach().getName() + " " + training.getCoach().getSurname()
                    + "was accepted. \n Remember your training start : " + training.getStartTime());
            Transport.send(message);
            logger.info("Email was send to : " + training.getCustomer().getEmail());
        } catch (MessagingException e) {
            logger.info("Cannot send email to : " + training.getCustomer().getEmail());
        }
    }

    public static void cancelTraining(Training training) {
        try {
            MimeMessage message = new MimeMessage(setUpSession());
            message.setFrom(new InternetAddress(mailUser));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(training.getCustomer().getEmail()));
            message.setSubject("Your training was canceled");
            message.setText("Hi, \n We are sorry to inform you, that your training with "
                    + training.getCoach().getName() + " " + training.getCoach().getSurname()
                    + "was canceled. \n You can contact with your coach by  : " + training.getCoach().getEmail());
            Transport.send(message);
            logger.info("Email was send to : " + training.getCustomer().getEmail());
        } catch (MessagingException e) {
            logger.info("Cannot send email to : " + training.getCustomer().getEmail());
        }
    }

    public static void proposeTraining(Training training){
        try {
            MimeMessage message = new MimeMessage(setUpSession());
            message.setFrom(new InternetAddress(mailUser));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(training.getCoach().getEmail()));
            message.setSubject("Someone propose u training");
            message.setText("Hi, \n We are pleased to inform you, that :"
                    + training.getCustomer().getName() + " " + training.getCustomer().getSurname()
                    + "proposed you training. \n Customer selected date : " + training.getStartTime()
            +"\n Please let know if this date is ok.");
            Transport.send(message);
            logger.info("Email was send to : " + training.getCustomer().getEmail());
        } catch (MessagingException e) {
            logger.info("Cannot send email to : " + training.getCustomer().getEmail());
        }
    }



    private static Session setUpSession() {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.port", "587");
        return Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUser, mailPassword);
            }
        });
    }
}
