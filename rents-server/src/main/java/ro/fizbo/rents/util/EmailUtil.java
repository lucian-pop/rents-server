package ro.fizbo.rents.util;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STRawGroupDir;

public final class EmailUtil {
	
	private static final String EMAIL_TEMPLATES_DIR = "templates/email";
	
	private static STRawGroupDir emailTemplates;
	
	static {
		emailTemplates = new STRawGroupDir(EMAIL_TEMPLATES_DIR, "UTF-8");
	}
	
	private EmailUtil() {
	}

	public static ST getEmailBodyTemplate(String templateName) {
		ST emailBodyTemplate = emailTemplates.getInstanceOf(templateName);
		
		return emailBodyTemplate;
	}
}
