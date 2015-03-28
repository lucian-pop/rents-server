package ro.fizbo.rents.util;

import ro.fizbo.rents.model.RentAbuse;
import ro.fizbo.rents.model.RentForm;

public final class SupportTicketUtil {
	
	private static final int TICKET_PREFIX_LENGTH = 3;
	
	private static final String TICKET_PREFIX_SEPARATOR = "-";

	private SupportTicketUtil() {
	}
	
	public static final String generateReportAbuseTicketId(RentAbuse rentAbuse) {
		StringBuilder ticketIdBuilder = new StringBuilder();
		RentForm rentForm = RentForm.values()[rentAbuse.getRent().getRentForm().intValue()];
		ticketIdBuilder.append(rentForm.toString().substring(0, TICKET_PREFIX_LENGTH));
		ticketIdBuilder.append(TICKET_PREFIX_SEPARATOR);
		ticketIdBuilder.append(rentAbuse.getRentAbuseId());
		return ticketIdBuilder.toString();
	}
}
