package pl.bak.timserver.mail;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.training.domain.Training;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.*;

@Slf4j
public class MailSender {

    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private static String mailUser = "";
    private static String mailPassword = "";


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
            logger.info("Cannot send email to : " + to + "exception" + e);
        }
    }

    public static void acceptTraining(Training training) {
        try {
            MimeMessage message = new MimeMessage(setUpSession());
            message.setFrom(new InternetAddress(mailUser));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(training.getCustomer().getEmail()));
            message.setSubject("Your training was accepted");
            Map<String, String> variables = new HashMap<>();
            variables.put("@trainer", String.valueOf(training.getCoach()));
            variables.put("@start_time", String.valueOf(training.getStartTime()));
            message.setText(createEmailContent("acceptTraining.txt", variables));
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
            message.setSubject("Your training was canceled");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(training.getCustomer().getEmail()));
            Map<String, String> variables = new HashMap<>();
            variables.put("@coach", String.valueOf(training.getCoach()));
            variables.put("@coach_email", training.getCoach().getEmail());
            message.setText(createEmailContent("cancelTraining.txt", variables));
            Transport.send(message);
            logger.info("Email was send to : " + training.getCustomer().getEmail());
        } catch (MessagingException e) {
            logger.info("Cannot send email to : " + training.getCustomer().getEmail());
        }
    }

    public static void proposeTraining(Training training) {
        try {
            MimeMessage message = new MimeMessage(setUpSession());
            message.setFrom(new InternetAddress(mailUser));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(training.getCoach().getEmail()));
            message.setSubject("Someone propose u training")
            ;
            Map<String, String> variables = new HashMap<>();
            variables.put("@customer", training.getCustomer().getName() + " " + training.getCustomer().getSurname());
            variables.put("@date", String.valueOf(training.getStartTime()));
            message.setText(createEmailContent("proposeTraining.txt", variables));
            Transport.send(message);
            logger.info("Email was send to : " + training.getCustomer().getEmail());
        } catch (MessagingException e) {
            logger.info("Cannot send email to : " + training.getCustomer().getEmail());
        }
    }

    public static void askForContact(Customer customer, String coachEmail) {
        try {
            MimeMessage message = new MimeMessage(setUpSession());
            message.setFrom(new InternetAddress(mailUser));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(coachEmail));
            message.setSubject("Customer asks for contact");
            Map<String, String> variables = new HashMap<>();
            variables.put("@customer", customer.getName() + " " + customer.getSurname());
            variables.put("@customer_mail", customer.getEmail());
            String body = createEmailContent("askForContact.txt", variables);
            message.setText(body);
            Transport.send(message);
            logger.info("Email was send to : " + coachEmail);
        } catch (MessagingException e) {
            logger.info("Cannot send email to : " + coachEmail + "exception " + e);
        }
    }


    private static Session setUpSession() {
        setCredentials();
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

    private static void setCredentials() {
        try (Scanner scanner = new Scanner(ResourceUtils.getFile("classpath:gmailCredentials.txt"))) {
            String user = scanner.nextLine();
            mailUser = user.substring(user.lastIndexOf("=") + 1);
            String password = scanner.nextLine();
            mailPassword = password.substring(password.lastIndexOf("=") + 1);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    private static String createEmailContent(String filePath, Map<String, String> new_vallue) {
        StringBuilder oldContent = new StringBuilder();
        BufferedReader reader = null;
        String newContent = "";
        try {
            File fileToBeModified = ResourceUtils.getFile("classpath:mails/" + filePath);
            reader = new BufferedReader(new FileReader(fileToBeModified));
            String line = reader.readLine();
            while (line != null) {
                oldContent.append(line).append(System.lineSeparator());
                line = reader.readLine();
            }
            newContent = oldContent.toString();
            for (Map.Entry<String, String> pair : new_vallue.entrySet()) {
                newContent = newContent.replaceAll(pair.getKey(), pair.getValue());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(reader).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newContent;
    }

}
