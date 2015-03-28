package ro.fizbo.rents.util;

import java.util.ArrayList;
import java.util.List;

public final class EmailConstants {
	
	private EmailConstants(){
	}
	
	/* Email addresses.*/
	public static final String ADMIN_REPORT_EMAIL = "lucian@fizbo.ro";
	
	public static final List<String> ADMIN_REPORT_EMAILS;
	
	/* Subjects.*/
	public static final String RENT_ABUSE_USER_REPORT_SUBJECT = 
			"Raport abuz, Fizbo – Simply rent";
	
	public static final String RENT_ABUSE_REPORT_SUBJECT = "Fizbo abuz - ";
	
	/* Template files.*/
	public static final String RENT_ABUSE_USER_REPORT_TEMPLATE = "RentAbuseUserReport";
	
	public static final String RENT_ABUSE_REPORT_TEMPLATE = "RentAbuseReport";
	
	/* Content.*/
	
	public static final String REPORTED_RENT_DETAILS = "%s, %d camere, %s, Nr. %s, %s, %s";
	
	static {
		ADMIN_REPORT_EMAILS = new ArrayList<String>(2);
		ADMIN_REPORT_EMAILS.add(ADMIN_REPORT_EMAIL);
	}

}
