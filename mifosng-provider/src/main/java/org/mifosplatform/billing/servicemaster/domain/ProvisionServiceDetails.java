package org.mifosplatform.billing.servicemaster.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_prov_service_details")
public class ProvisionServiceDetails extends AbstractPersistable<Long> {

	@Column(name = "service_id")
	private Long serviceId;

	@Column(name = "service_identification", nullable = false, length = 100)
	private String serviceIdentification;

	@Column(name = "status", nullable = false, length = 100)
	private String status;
	
	@Column(name = "image", nullable = false, length = 100)
	private String image;

	@Column(name = "is_deleted")
	private String isDeleted="n";
	
	


public ProvisionServiceDetails()
{}


public ProvisionServiceDetails(Long serviceId, String serviceUrl, String status,
		String image) {
	this.serviceId=serviceId;
	this.serviceIdentification=serviceUrl;
	this.status=status;
	this.image=image;
}


public Long getServiceId() {
	return serviceId;
}


public String getServiceIdentification() {
	return serviceIdentification;
}


public String getStatus() {
	return status;
}


public String getImage() {
	return image;
}


public String getIsDeleted() {
	return isDeleted;
}





}
