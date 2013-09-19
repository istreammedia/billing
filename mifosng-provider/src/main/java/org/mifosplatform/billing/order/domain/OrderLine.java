package org.mifosplatform.billing.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.useradministration.domain.AppUser;

@Entity
@Table(name = "b_order_line")
public class OrderLine extends AbstractAuditableCustom<AppUser, Long>  {

/*@Id
@GeneratedValue
@Column(name="id")
private Long id;
*/

    @ManyToOne
    @JoinColumn(name="order_id")
	private Order orders;

	@Column(name = "service_id")
	private Long serviceId;

	@Column(name = "service_type")
	private String serviceType;

	@Column(name = "service_status")
	private Long serviceStatus;

	@Column(name = "is_deleted")
	private char isDeleted;

	public OrderLine()
	{}

	public OrderLine(final Long serviceId,final String serviceType,final Long serviceStatus,final char isdeleted )
	 {
		this.orders=null;
		this.serviceId=serviceId;
		this.serviceStatus=serviceId;
		this.isDeleted=isdeleted;
		this.serviceType=serviceType;

	 }
    public OrderLine(final String serviceCode)
     {
	this.serviceType=serviceCode;
	 }

	 public Order getOrderId() {
		return orders;
	 }
  
	 public Long getServiceId() {
		return serviceId;
	 }
	
	 public String getServiceType() {
		return serviceType;
	 }


	public Long getServiceStatus() {
		return serviceStatus;
	}


	public char isDeleted() {
		return isDeleted;
	}
	public  void update(Order order)
	{
		this.orders=order;

	}

	public void delete() {

		this.isDeleted='y';
		


	}



}
