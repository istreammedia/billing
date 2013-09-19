package org.mifosplatform.billing.ticketmaster.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.core.api.JsonCommand;

@Entity
@Table(name = "b_ticket_details")
public class TicketDetail {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "ticket_id", length = 65536)
	private Long ticketId;

	

	@Column(name = "comments")
	private String comments;

	@Column(name = "attachments")
	private String attachments;
	
	@Column(name = "assigned_to")
	private Integer assignedTo;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "createdby_id")
	private Long createdbyId;

	
	public TicketDetail() {
		// TODO Auto-generated constructor stub
	}
	
public static TicketDetail fromJson(final JsonCommand command) throws ParseException {
	
	Integer assignedTo = command.integerValueOfParameterNamed("assignedTo");
	String description  = command.stringValueOfParameterNamed("description");
	
	String startDateString = command.stringValueOfParameterNamed("ticketDate");
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date ticketDate = df.parse(startDateString);
	
	
	return new TicketDetail(assignedTo,description,ticketDate);
}

public TicketDetail(Integer assignedTo, String description, Date ticketDate) {
	this.assignedTo =assignedTo;
	this.comments = description;
	this.createdDate=ticketDate;
}



	public TicketDetail(Long ticketId, String comments, String fileLocation,
			Integer assignedTo,Long createdbyId) {
                   this.ticketId=ticketId;
                   this.comments=comments;
                   this.attachments=fileLocation;
                   this.assignedTo=assignedTo;
                   this.createdDate=new LocalDate().toDate();
                   this.createdbyId = createdbyId;	
	}


	
	public Long getId() {
		return id;
	}
	
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	public Long getTicketId() {
		return ticketId;
	}

	public String getComments() {
		return comments;
	}


	public String getAttachments() {
		return attachments;
	}


	public Integer getAssignedTo() {
		return assignedTo;
	}

	public void setCreatedbyId(Long createdbyId) {
		this.createdbyId = createdbyId;
	}
	
	public Long getCreatedbyId() {
		return createdbyId;
	}
}
