package ro.fizbo.rents.model.email;

import java.util.List;

public class Email {
	
	private String fromEmail;
	
	private List<String> toEmails;
	
	private String emailSubject;
	
	private String emailBody;

	private String emailAttachmentFilePath;

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public List<String> getToEmails() {
		return toEmails;
	}

	public void setToEmails(List<String> toEmails) {
		this.toEmails = toEmails;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public String getEmailAttachmentFilePath() {
		return emailAttachmentFilePath;
	}

	public void setEmailAttachmentFilePath(String emailAttachmentFilePath) {
		this.emailAttachmentFilePath = emailAttachmentFilePath;
	}
}
