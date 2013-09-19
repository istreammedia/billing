package org.mifosplatform.billing.transactionhistory.data;

import java.util.Date;

public class TransactionHistoryData {
	
		private Long id;
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}


		private Long clientId;
		private String transactionType;
		private Date transactionDate;
		private String history;
		private String user;


		public TransactionHistoryData() {
			
		}
		
		public TransactionHistoryData(final Long clientId, final String transactionType, final Date transactionDate, final String history){
			this.clientId = clientId;
			this.transactionType = transactionType;
			this.transactionDate = transactionDate;
			this.history = history;
		}
		
		public TransactionHistoryData(final Long id, final Long clientId, final String transactionType, final Date transactionDate, final String history, String user){
			this.id = id;
			this.clientId = clientId;
			this.transactionType = transactionType;
			this.transactionDate = transactionDate;
			this.history = history;
			this.user=user;
		}
		
		
		/**
		 * @return the clientId
		 */
		public Long getClientId() {
			return clientId;
		}


		/**
		 * @param clientId the clientId to set
		 */
		public void setClientId(Long clientId) {
			this.clientId = clientId;
		}


		/**
		 * @return the transactionType
		 */
		public String getTransactionType() {
			return transactionType;
		}


		/**
		 * @param transactionType the transactionType to set
		 */
		public void setTransactionType(String transactionType) {
			this.transactionType = transactionType;
		}


		/**
		 * @return the transactionDate
		 */
		public Date getTransactionDate() {
			return transactionDate;
		}


		/**
		 * @param transactionDate the transactionDate to set
		 */
		public void setTransactionDate(Date transactionDate) {
			this.transactionDate = transactionDate;
		}


		/**
		 * @return the history
		 */
		public String getHistory() {
			return history;
		}


		/**
		 * @param history the history to set
		 */
		public void setHistory(String history) {
			this.history = history;
		}

		/**
		 * @return the user
		 */
		public String getUser() {
			return user;
		}
		

}
