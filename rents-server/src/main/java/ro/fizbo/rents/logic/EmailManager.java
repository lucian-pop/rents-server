package ro.fizbo.rents.logic;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.stringtemplate.v4.ST;

import ro.fizbo.rents.client.email.EmailClient;
import ro.fizbo.rents.listener.ApplicationManager;
import ro.fizbo.rents.model.PropertyType;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.RentAbuse;
import ro.fizbo.rents.model.RentImage;
import ro.fizbo.rents.model.email.Email;
import ro.fizbo.rents.task.SendEmailTask;
import ro.fizbo.rents.util.EmailConstants;
import ro.fizbo.rents.util.EmailParams;
import ro.fizbo.rents.util.EmailUtil;
import ro.fizbo.rents.util.SupportTicketUtil;

public final class EmailManager {

	private EmailManager() {
	}

	public static void sendRentAbuseUserReportEmail(RentAbuse rentAbuse) {
		if(rentAbuse.getRentAbuseEmail() == null || rentAbuse.getRentAbuseEmail().equals("")) {
			return;
		}

		Email email = new Email();
		email.setEmailSubject(EmailConstants.RENT_ABUSE_USER_REPORT_SUBJECT);

		ST emailBodyTemplate = 
				EmailUtil.getEmailBodyTemplate(EmailConstants.RENT_ABUSE_USER_REPORT_TEMPLATE);
		emailBodyTemplate.add(EmailParams.RENT_ABUSE_TICKET_ID, 
				SupportTicketUtil.generateReportAbuseTicketId(rentAbuse));
		emailBodyTemplate.add(EmailParams.RENT_ABUSE_DESCRIPTION, 
				rentAbuse.getRentAbuseDescription());
		email.setEmailBody(emailBodyTemplate.render());

		email.setFromEmail(EmailConstants.ADMIN_REPORT_EMAIL);
		List<String> recipients = new ArrayList<String>(1);
		recipients.add(rentAbuse.getRentAbuseEmail());
		email.setToEmails(recipients);

		MimeMessage emailMessage = EmailClient.buildEmailMessage(email);
		AsyncTaskManager.post(new SendEmailTask(emailMessage));
	}
	
	public static void sendRentAbuseReportEmail(RentAbuse rentAbuse) {
		Email email = new Email();
		Rent rent = rentAbuse.getRent();

		String rentDetails = String.format(EmailConstants.REPORTED_RENT_DETAILS, 
				PropertyType.values()[rent.getRentType()].toString().toLowerCase(),
				rent.getRentRooms(),rent.getAddress().getAddressStreetName(),
				rent.getAddress().getAddressStreetNo(), rent.getAddress().getAddressNeighbourhood(),
				rent.getAddress().getAddressLocality());
		email.setEmailSubject(EmailConstants.RENT_ABUSE_REPORT_SUBJECT + rentDetails);

		ST emailBodyTemplate = 
				EmailUtil.getEmailBodyTemplate(EmailConstants.RENT_ABUSE_REPORT_TEMPLATE);
		emailBodyTemplate.add(EmailParams.APP_URL, ApplicationManager.getAppURL());
		emailBodyTemplate.add(EmailParams.RENT_ABUSE_TOKEN_KEY, rentAbuse.getRentAbuseTokenKey());
		emailBodyTemplate.add(EmailParams.RENT_ABUSE_EMAIL, rentAbuse.getRentAbuseEmail());
		emailBodyTemplate.add(EmailParams.RENT_ABUSE_PHONE, rentAbuse.getRentAbusePhone());
		emailBodyTemplate.add(EmailParams.RENT_ABUSE_TICKET_ID, 
				SupportTicketUtil.generateReportAbuseTicketId(rentAbuse));
		emailBodyTemplate.add(EmailParams.RENT_ABUSE_DESCRIPTION, 
				rentAbuse.getRentAbuseDescription());
		List<RentImage> rentImages = rentAbuse.getRent().getRentImages();
		if(rentImages != null && rentImages.size() > 0) {
			emailBodyTemplate.add(EmailParams.RENT_ABUSE_RENT_IMAGE, 
					rentImages.get(0).getRentImageURI());
		}
		emailBodyTemplate.add(EmailParams.RENT_ABUSE_RENT_DETAILS, rentDetails);
		emailBodyTemplate.add(EmailParams.RENT_ABUSE_RENT_PHONE, rent.getRentPhone());
		email.setEmailBody(emailBodyTemplate.render());

		email.setFromEmail(EmailConstants.ADMIN_REPORT_EMAIL);
		email.setToEmails(EmailConstants.ADMIN_REPORT_EMAILS);

		MimeMessage emailMessage = EmailClient.buildEmailMessage(email);
		AsyncTaskManager.post(new SendEmailTask(emailMessage));
	}
}
