package ro.fizbo.rents.task;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class SendEmailTask implements Runnable {
	
	private static Logger logger = Logger.getLogger(SendEmailTask.class);
	
	private MimeMessage emailMessage;
	
	public SendEmailTask(MimeMessage emailMessage) {
		this.emailMessage = emailMessage;
	}

	@Override
	public void run() {
		try {
			logger.info("Send email with subject '" + emailMessage.getSubject() + "'");
			Transport.send(emailMessage);
		} catch (MessagingException sendException) {
			try {
				logger.error("Got error while sending email with subject " 
						+ emailMessage.getSubject(), sendException);
			} catch (MessagingException msgException) {
				logger.error("Got error while getting subject from email message.", msgException);
			}
		}
	}

}
