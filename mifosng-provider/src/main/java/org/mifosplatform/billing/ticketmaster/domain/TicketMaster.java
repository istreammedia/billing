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

import org.mifosplatform.billing.ticketmaster.command.TicketMasterCommand;
import org.mifosplatform.infrastructure.core.api.JsonCommand;

@Entity
@Table(name = "b_ticket_master")
public class TicketMaster {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "client_id", length = 65536)
	private Long clientId;

	@Column(name = "priority")
	private String priority;

	@Column(name = "problem_code")
	private Integer problemCode;
	
	@Column(name = "description")
	private String description;

	@Column(name = "ticket_date")
	private Date ticketDate;

	@Column(name = "status")
	private String status;
	
	@Column(name="status_code")
	private Integer statusCode;

	@Column(name = "resolution_description")
	private String resolutionDescription;
	
	@Column(name = "assigned_to")
	private Integer assignedTo;

	@Column(name = "source")
	private String source;
	
	@Column(name = "closed_date")
	private Date closedDate;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "createdby_id") 
	private Long createdbyId;

	

	
	public TicketMaster() {
		// TODO Auto-generated constructor stub
	}
	
public static TicketMaster fromJson(final JsonCommand command) throws ParseException {
	
	String priority = command.stringValueOfParameterNamed("priority");
	Integer problemCode = command.integerValueOfParameterNamed("problemCode");
	String description = command.stringValueOfParameterNamed("description");
	Integer assignedTo = command.integerValueOfParameterNamed("assignedTo");
	
	
	String startDateString = command.stringValueOfParameterNamed("ticketDate");
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date ticketDate = df.parse(startDateString);
	
	String statusCode = command.stringValueOfParameterNamed("problemDescription");
	Long clientId = command.getClientId();
	/*Integer createdbyId = command.getI*/
	return new TicketMaster(clientId, priority,ticketDate, problemCode,description,statusCode, null, assignedTo,null,null,null);
}

	public TicketMaster(Integer statusCode, Integer assignedTo) {
		this.clientId=null;
		this.priority=null;
		this.ticketDate=null;
		this.problemCode=null;
		this.description=null;
		this.status=null;
		this.statusCode = statusCode;
		this.source=null;
		this.resolutionDescription=null;
		this.assignedTo=assignedTo;	
		this.createdDate = null;
		this.createdbyId = null;
	}



	public TicketMaster(Long clientId, String priority,
			Date ticketDate, Integer problemCode, String description,
			String status, String resolutionDescription, Integer assignedTo, Integer statusCode, Date createdDate, Integer createdbyId) {
		
		this.clientId=clientId;
		this.priority=priority;
		this.ticketDate=ticketDate;
		this.problemCode=problemCode;
		this.description=description;
		this.status="OPEN";
		this.statusCode = statusCode;
		this.source="Manual";
		this.resolutionDescription=resolutionDescription;
		this.assignedTo=assignedTo;	
		this.createdDate = new Date();
		this.createdbyId = null;
	}



	public String getSource() {
		return source;
	}



	public Long getId() {
		return id;
	}



	public Long getClientId() {
		return clientId;
	}



	public String getPriority() {
		return priority;
	}



	public Integer getProblemCode() {
		return problemCode;
	}



	public String getDescription() {
		return description;
	}



	public Date getTicketDate() {
		return ticketDate;
	}



	public String getStatus() {
		return status;
	}
	
	public Integer getStatusCode() {
		return statusCode;
	}



	public String getResolutionDescription() {
		return resolutionDescription;
	}



	public Integer getAssignedTo() {
		return assignedTo;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void updateTicket(TicketMasterCommand command) {
		this.statusCode = command.getStatusCode();
		this.assignedTo = command.getAssignedTo();
	}


	public void closeTicket(JsonCommand command) {
		this.status = "CLOSED";
	    this.statusCode = Integer.parseInt(command.stringValueOfParameterNamed("status"));
		this.resolutionDescription=command.stringValueOfParameterNamed("resolutionDescription");
		this.closedDate=new Date();
		
	}
	
	
	
	public Date getClosedDate() {
		return closedDate;
	}

	/**
	 * @return the createdbyId
	 */
	public Long getCreatedbyId() {
		return createdbyId;
	}

	/**
	 * @param createdbyId the createdbyId to set
	 */
	public void setCreatedbyId(Long createdbyId) {
		this.createdbyId = createdbyId;
	}



	
	 
}
