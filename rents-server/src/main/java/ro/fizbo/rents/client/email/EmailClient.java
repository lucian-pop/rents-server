package ro.fizbo.rents.client.email;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import ro.fizbo.rents.model.email.Email;
import ro.fizbo.rents.webservice.util.HeadersConstants;

public class EmailClient {
	
	private static Logger logger = Logger.getLogger(EmailClient.class);

	private static final String AUTH_EMAIL = "lucian@fizbo.ro";
	
	private static final String AUTH_PASSWORD = "Pllzxc980!";
	
	private static final String SMTP_SERVER_NAME = "smtp.zoho.com";
	
	private static final int SMTP_SERVER_PORT = 465;
	
	private static Session session;
	
	private EmailClient() {
	}
	
	private synchronized static final Session createSession() {
		Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_SERVER_NAME);
        props.put("mail.smtp.socketFactory.port", SMTP_SERVER_PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", SMTP_SERVER_PORT);
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(AUTH_EMAIL, AUTH_PASSWORD);
            }
        };
        Session session = Session.getDefaultInstance(props, authenticator);
        
		return session;
	}
	
	public static Session getSession() {
		if(session != null) {
			return session;
		}
		session = createSession();
		return session;
	}
	
	public static MimeMessage buildEmailMessage(Email email) {
		try {
			MimeMessage emailMessage = new MimeMessage(EmailClient.getSession());
			emailMessage.setHeader(HeadersConstants.CONTENT_TYPE, "text/HTML; charset=UTF-8");
			emailMessage.setFrom(new InternetAddress(email.getFromEmail()));
			emailMessage.setReplyTo(InternetAddress.parse(email.getFromEmail(), false));
			emailMessage.setSubject(email.getEmailSubject(), "UTF-8");
			emailMessage.setContent(email.getEmailBody(), "text/HTML; charset=UTF-8");	
			emailMessage.setSentDate(new Date());
			emailMessage.setRecipients(Message.RecipientType.TO, 
					InternetAddress.parse(buildRecipientsAddresses(email.getToEmails()), false));
			return emailMessage;
		} catch (MessagingException e) {
			logger.error("Got error while sending email with subject '" + email.getEmailSubject() 
					+ "'", e);
		}
		return null;
	}
	
	private static String buildRecipientsAddresses(List<String> toEmails) {
		if(toEmails == null || toEmails.size() == 0) {
			return null;
		}
		
		if(toEmails.size() == 1) {
			return toEmails.get(0);
		}

		StringBuilder recipientsAddressesBuilder = new StringBuilder();
		int emailIndex = 0;
		for(String email : toEmails) {
			recipientsAddressesBuilder.append(email);
			if(emailIndex != toEmails.size() - 1) {
				recipientsAddressesBuilder.append(",");
			}
		}

		return recipientsAddressesBuilder.toString();
	}
}
