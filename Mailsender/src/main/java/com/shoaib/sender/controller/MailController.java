package com.shoaib.sender.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

@RestController
public class MailController {
	
	@GetMapping("/test")
	public String test() {
		return "reunning";
	}
	
	@GetMapping("/send")
	public String Mailsender() {
		
		String to = "receiver mail";

		// Sender's email address
		String from = "sender mail";

		// SMTP server and port
		String host = "smtp.gmail.com";
		int port = 587;

		// Email username and password
		String username = "sender mail";
		String password = "sender password";

		// Set properties for the SMTP server
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		Session session = Session.getDefaultInstance(properties, new jakarta.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			String date = dateFormat.format(cal.getTime());
			System.out.println(date);

			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			message.setSubject("do not reply");

			message.setText("this is mail sended by system");
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText("This is the message body.");

			MimeMultipart multipart = new MimeMultipart();

			multipart.addBodyPart(messageBodyPart);

			messageBodyPart = new MimeBodyPart();
			String filename = "C:\\Users\\Shoaib Akhtar\\Downloads/industry_sic.csv";
			FileDataSource source =new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(date.toString() + ".csv");
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);

			Transport transport = session.getTransport("smtp");
			transport.connect(host, port, username, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

			System.out.println("Sent message successfully");
			return "ok";
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
		return "send";
		
	}

}
