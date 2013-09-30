 package org.mifosplatform.billing.ticketmaster.data;

import org.joda.time.LocalDate;

public class ClientTicketData {
	
	private final Long id;
    private final String priority;
    private final String status;
    private final Long userId;
    private final LocalDate ticketDate;
    private final String lastComment;
    private final String problemDescription;
    private final String userName;
    private final Long clientId;
	private String timeElapsed;
	private Object clientName;

	
	public ClientTicketData( Long id, String priority, String status, Long assignedTo, LocalDate ticketDate,
			String lastComment,String problemDescription,String userName, Long clientId) {
	this.id=id;
	this.priority=priority;
	this.status=status;
	this.userId=assignedTo;
	this.ticketDate=ticketDate;
	this.lastComment=lastComment;
	this.problemDescription=problemDescription;
	this.userName=userName;
	this.clientId=clientId;
	
}
	public ClientTicketData( Long id, String priority, String status, Long assignedTo, LocalDate ticketDate,
			String lastComment,String problemDescription,String userName, Long clientId,
			final String timeElapsed, final String clientName) {
	this.id=id;
	this.priority=priority;
	this.status=status;
	this.userId=assignedTo;
	this.ticketDate=ticketDate;
	this.lastComment=lastComment;
	this.problemDescription=problemDescription;
	this.userName=userName;
	this.clientId=clientId;
	this.timeElapsed = timeElapsed;
	this.clientName = clientName;
}



	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}


	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}


	/**
	 * @return the ticketDate
	 */
	public LocalDate getTicketDate() {
		return ticketDate;
	}


	/**
	 * @return the lastComment
	 */
	public String getLastComment() {
		return lastComment;
	}


	/**
	 * @return the problemDescription
	 */
	public String getProblemDescription() {
		return problemDescription;
	}


	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}


	/**
	 * @return the clientId
	 */
	public Long getClientId() {
		return clientId;
	}

}
