package org.mifosplatform.billing.order.data;

import org.joda.time.LocalDate;

public class OrderHistoryData {
	
	private final Long id;
	private final LocalDate transactionDate;
	private final LocalDate actualDate;
	private final LocalDate provisioningDate;
	private final String transactioType;
	private Long PrepareRequsetId;

	public OrderHistoryData(Long id, LocalDate transDate, LocalDate actualDate,
			LocalDate provisionongDate, String transactionType, Long prepareRequsetId) {
               this.id=id;
               this.transactionDate=transDate;
               this.actualDate=actualDate;
               this.provisioningDate=provisionongDate;
               this.transactioType=transactionType;
               this.PrepareRequsetId=prepareRequsetId;
	
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the transactionDate
	 */
	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @return the actualDate
	 */
	public LocalDate getActualDate() {
		return actualDate;
	}

	/**
	 * @return the provisioningDate
	 */
	public LocalDate getProvisioningDate() {
		return provisioningDate;
	}

	/**
	 * @return the transactioType
	 */
	public String getTransactioType() {
		return transactioType;
	}

}
