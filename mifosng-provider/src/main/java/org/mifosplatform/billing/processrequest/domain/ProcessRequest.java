package org.mifosplatform.billing.processrequest.domain;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "b_process_request")
public class ProcessRequest   {

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "order_id")
	private Long orderId;

	@Column(name = "is_processed")
	private char isProcessed='N';


	@Column(name = "provisioing_system")
	private String provisioingSystem;
	
	@Column(name="request_type")
	private String requestType;
	
	@Column(name = "prepareRequest_id")
	private Long prepareRequestId;

	@Column(name = "is_notify")
	private char isNotify='N';



	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "processRequest", orphanRemoval = true)
	private List<ProcessRequestDetails> processRequestDetails = new ArrayList<ProcessRequestDetails>();

	

	 public ProcessRequest() {
		// TODO Auto-generated constructor stub
			
	}



	public ProcessRequest(Long clientId, Long orderId, 
			 String provisioningSystem, char isDeleted,String userName, String requestType, Long requestId) {
            this.clientId=clientId;
            this.orderId=orderId;
            this.provisioingSystem=provisioningSystem;
            
            
            this.requestType=requestType;
            this.prepareRequestId=requestId;
	
	
	}



	public void add(ProcessRequestDetails processRequestDetails) {
	     processRequestDetails.update(this);
	     this.processRequestDetails.add(processRequestDetails);
		
		
	}



	public void setNotify() {
		if(this.isNotify!='Y'){
			this.isNotify='Y';
		}
		
	}



	public void setProcessStatus() {
		this.isProcessed='Y';
		
	}



	public Long getId() {
		return id;
	}



	public Long getClientId() {
		return clientId;
	}



	public Long getOrderId() {
		return orderId;
	}



	public char getIsProcessed() {
		return isProcessed;
	}



	public String getProvisioingSystem() {
		return provisioingSystem;
	}



	public String getRequestType() {
		return requestType;
	}



	public Long getPrepareRequestId() {
		return prepareRequestId;
	}



	public char getIsNotify() {
		return isNotify;
	}



	public List<ProcessRequestDetails> getProcessRequestDetails() {
		return processRequestDetails;
	}
	
	
 
	

}
