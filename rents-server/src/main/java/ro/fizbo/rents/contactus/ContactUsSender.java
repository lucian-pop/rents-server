package ro.fizbo.rents.contactus;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.stringtemplate.v4.ST;

import ro.fizbo.rents.client.email.EmailClient;
import ro.fizbo.rents.logic.AsyncTaskManager;
import ro.fizbo.rents.model.email.Email;
import ro.fizbo.rents.task.SendEmailTask;
import ro.fizbo.rents.util.EmailUtil;

public class ContactUsSender {
	
	private final String BUSINESS_SUPPORT_EMAIL="hello@fizboapp.co.uk";
			
	private final String CONFIRMATION_SUBJECT="Fizbo App - Thank you for contacting us!";
	
	private final String CONFIRMATION_TEMPLATE="ContactUsConfirmationEmail";
	
	private final String REQUEST_TEMPLATE="ContactUsRequestEmail";
	
	private final String USERNAME_PARAM = "userName";
	
	private final String USEREMAIL_PARAM = "userEmail";
	
	private final String USERREQUEST_PARAM = "userRequest";
	
	private final String USER_SUBJECT_REQUEST_FORMAT= "Received 'Contact Us' request from %s";
	
	public void sendContactUsEmails(String userName, String userEmail, String userRequest) {
		Session emailSession = EmailClient.getSession();

		sendContactUsRequestEmailToSupportTeam(userEmail, userName, userRequest, emailSession);
		sendConfirmationEmailToUser(userName, userEmail, emailSession);
	}
	
	protected void sendConfirmationEmailToUser(String userName, String userEmail, Session session) {
		
		Email email = new Email();
		
		email.setFromEmail(BUSINESS_SUPPORT_EMAIL);
		
		List<String> recipients = new ArrayList<String>(1);
		recipients.add(userEmail);
		email.setToEmails(recipients);
		
		email.setEmailSubject(CONFIRMATION_SUBJECT);
		
		ST confirmationEmailTemplate = EmailUtil.getEmailBodyTemplate(CONFIRMATION_TEMPLATE);
		confirmationEmailTemplate.add(USERNAME_PARAM, userName);
		email.setEmailBody(confirmationEmailTemplate.render());
		
		MimeMessage emailMessage = EmailClient.buildEmailMessage(email, session);
		AsyncTaskManager.post(new SendEmailTask(emailMessage));
	}
	
	protected void sendContactUsRequestEmailToSupportTeam(String userEmail, String userName, String userRequest, Session session) {
	
		Email email = new Email();
		
		email.setFromEmail(BUSINESS_SUPPORT_EMAIL);
		
		List<String> recipients = new ArrayList<String>(1);
		recipients.add(BUSINESS_SUPPORT_EMAIL);
		email.setToEmails(recipients);
		
		ST requestEmailTemplate = EmailUtil.getEmailBodyTemplate(REQUEST_TEMPLATE);
		requestEmailTemplate.add(USEREMAIL_PARAM, userEmail);
		requestEmailTemplate.add(USERREQUEST_PARAM, userRequest);
		email.setEmailBody(requestEmailTemplate.render());
		
		email.setEmailSubject(String.format(USER_SUBJECT_REQUEST_FORMAT, userName));
		
		MimeMessage emailMessage = EmailClient.buildEmailMessage(email, session);
		AsyncTaskManager.post(new SendEmailTask(emailMessage));
	}

}
