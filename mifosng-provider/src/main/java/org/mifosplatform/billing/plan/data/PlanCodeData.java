package org.mifosplatform.billing.plan.data;

import java.util.List;

import org.joda.time.LocalDate;

public class PlanCodeData {
	private Long id;
	private String planCode;
	private List<ServiceData> availableServices;
	private LocalDate start_date;
	private String isPrepaid;
	public PlanCodeData(final Long id,final String planCode,final List<ServiceData> data, String isPrepaid)
	{
		this.id=id;
		this.planCode=planCode;
		this.availableServices=data;
		this.start_date=new LocalDate();
		this.isPrepaid=isPrepaid;

	}

	public Long getId() {
		return id;
	}

	public String getPlanCode() {
		return planCode;
	}

	public List<ServiceData> getData() {
		return availableServices;
	}

	public LocalDate getStartDate() {
		return start_date;
	}

	/**
	 * @return the availableServices
	 */
	public List<ServiceData> getAvailableServices() {
		return availableServices;
	}

	/**
	 * @return the start_date
	 */
	public LocalDate getStart_date() {
		return start_date;
	}

	/**
	 * @return the isPrepaid
	 */
	public String getIsPrepaid() {
		return isPrepaid;
	}


}
