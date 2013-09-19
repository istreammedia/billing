package org.mifosplatform.billing.billmaster.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.billingorder.data.BillDetailsData;
import org.mifosplatform.billing.financialtransaction.data.FinancialTransactionsData;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class BillMasterReadPlatformServiceImplementation implements
		BillMasterReadPlatformService {

	private final PlatformSecurityContext context;
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public BillMasterReadPlatformServiceImplementation(
			final PlatformSecurityContext context,
			final TenantAwareRoutingDataSource dataSource) {

		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);

	}

	@Override
	public List<FinancialTransactionsData> retrieveFinancialData(Long clientId) {

		FinancialTransactionsMapper financialTransactionsMapper = new FinancialTransactionsMapper();
		String sql = "select " + financialTransactionsMapper.financialTransactionsSchema();
		return this.jdbcTemplate.query(sql, financialTransactionsMapper,
				new Object[] { clientId,clientId,clientId,clientId,clientId });

	}

	private static final class FinancialTransactionsMapper implements
			RowMapper<FinancialTransactionsData> {

		@Override
		public FinancialTransactionsData mapRow(ResultSet rs, int rowNum)

				throws SQLException {
			Long transactionId = rs.getLong("transId");
			Date transactionDate = rs.getDate("transDate");
			String transactionType = rs.getString("transType");
			BigDecimal amount = rs.getBigDecimal("amount");
			LocalDate transDate=JdbcSupport.getLocalDate(rs,"transDate");

			return new FinancialTransactionsData(transactionId,transDate,transactionType,null,null,amount);
		}

		public String financialTransactionsSchema() {

			return  	 " a.id as transId,Concat(Date_format(charge_start_date,'%Y-%m-%d'),' to ' ,Date_format(charge_end_date,'%Y-%m-%d')) transDate,'SERVICE_CHARGES' AS transType, charge_amount AS amount"
						+" from b_charge a,b_invoice b, b_orders c where a.invoice_id=b.id and a.order_id=c.id"
						+" and a.bill_id IS NULL AND invoice_date <= NOW() AND b.client_id = ?"
						+" union all"
						+" Select  a.id as transId,Date_format(invoice_date,'%Y-%m-%d') transDate,'TAXES' AS transType, a.tax_amount AS amount"
						+" from b_charge_tax a,b_invoice b where a.invoice_id=b.id"
						+" and a.bill_id IS NULL AND invoice_date <= NOW() AND b.client_id = ?"
						+" UNION ALL"
						+" SELECT id as transId,Date_format(adjustment_date,'%Y-%m-%d') transDate,'ADJUSTMENT' AS transType,"
						+" CASE adjustment_type"
						+" WHEN 'DEBIT' THEN adjustment_amount"
						+" WHEN 'CREDIT' THEN -adjustment_amount"
						+" END"
						+" AS amount"
						+" FROM b_adjustments"
						+" WHERE bill_id IS NULL AND adjustment_date <= NOW() AND client_id = ?"
						+" UNION ALL"
						+" SELECT pa.id as transId,Date_format(pa.payment_date,'%Y-%m-%d') transDate,concat('PAYMENT',' - ',p.code_value) AS transType,"
					    +" pa.amount_paid AS invoiceAmount  " 
						+" FROM b_payments pa,m_code_value p "
						+" WHERE bill_id IS NULL AND payment_date <= NOW() AND client_id =? and pa.paymode_id = p.id"
						+" union all"
						+" SELECT a.id as transId ,Date_format(sale_date,'%Y-%m-%d') transDate,'ONETIME_CHARGES' AS transType,total_price  AS amount " 
						+" FROM b_charge a,b_invoice b , b_onetime_sale c WHERE a.invoice_id=b.id and a.order_id=c.id and a.bill_id IS NULL AND c.sale_date <= NOW() AND b.client_id = ?";


		}
	}

	@Override
	public List<FinancialTransactionsData> retrieveInvoiceFinancialData(
			Long clientId) {
		FinancialInvoiceTransactionsMapper financialTransactionsMapper = new FinancialInvoiceTransactionsMapper();
		String sql = "select " + financialTransactionsMapper.financialTransactionsSchema();
		return this.jdbcTemplate.query(sql, financialTransactionsMapper,
				new Object[] { clientId });

	}

	private static final class FinancialInvoiceTransactionsMapper implements
			RowMapper<FinancialTransactionsData> {

		@Override
		public FinancialTransactionsData mapRow(ResultSet rs, int rowNum)

				throws SQLException {
			Long transactionId = rs.getLong("TransId");
		//	Date transactionDate = rs.getDate("transDate");
			String transactionType = rs.getString("TransType");
			BigDecimal debitAmount = rs.getBigDecimal("Dr_amt");
			BigDecimal creditAmount =rs.getBigDecimal("Cr_amt");
			
			
			LocalDate transDate=JdbcSupport.getLocalDate(rs,"TransDate");

			return new FinancialTransactionsData(transactionId,transDate,transactionType,debitAmount,creditAmount,null);
		}

		public String financialTransactionsSchema() {

			/* "  id AS transId,  Date_format(invoice_date, '%Y-%m-%d') transDate,'INVOICE' AS transType,invoice_amount AS amount FROM b_invoice"
					+" WHERE invoice_date <= NOW() AND client_id =? UNION ALL SELECT id AS transId, Date_format(adjustment_date, '%Y-%m-%d') transdate,"
					+" 'ADJUSTMENT' AS transType,  CASE adjustment_type  WHEN 'DEBIT' THEN adjustment_amount  WHEN 'CREDIT' THEN -adjustment_amount"
					+" END  AS amount FROM b_adjustments WHERE adjustment_date <= NOW() AND client_id = ? UNION ALL SELECT pa.id AS transId,"
					+"Date_format(pa.payment_date, '%Y-%m-%d') transDate,'PAYMENT' AS transType,-pa.amount_paid AS amount FROM b_payments pa"
					+" WHERE payment_date <= NOW()  AND client_id =?   ORDER BY 2";*/
			return "v.* from  fin_trans_vw  v where v.client_id=?";


		}
	}

	@Override
	public BillDetailsData retrievebillDetails(Long clientId) {

		BillMapper mapper = new BillMapper();
	        String sql = "select " + mapper.billSchema() + " and b.id ="+clientId;

	         return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] {  });
	    }

	private static final class BillMapper implements RowMapper<BillDetailsData> {

        public String billSchema() {
            return " b.*,c.*, mc.*,ca.line_1 as addr1,ca.line_2 as addr2,ca.city as offCity,ca.state as offSate,ca.country as offCountry,"
            	+"ca.zip as offZip,ca.phone_number as offPhnNum,ca.email_id as email,ca.company_logo as companyLogo  FROM b_bill_master b,"
            		+"b_company_address_detail ca,m_client mc LEFT JOIN  b_client_address c  ON c.client_id = mc.id WHERE b.client_id = mc.id";
        }



        @Override
        public BillDetailsData mapRow(ResultSet rs, @SuppressWarnings("unused") int rowNum) throws SQLException {

            Long id = rs.getLong("id");
            Long clientId = rs.getLong("Client_id");
            String addrNo = rs.getString("address_no");
            String clientName = rs.getString("display_name");
            String billPeriod = rs.getString("bill_Period");

            String street = rs.getString("street");
            String zip = rs.getString("zip"); 
            String city = rs.getString("country");
            String state = rs.getString("state");
            String country = rs.getString("country");
            
            String addr1 = rs.getString("addr1");
            String addr2 = rs.getString("addr2");
            String offCity = rs.getString("offCity");
            String offState = rs.getString("offSate");
            String offCountry = rs.getString("offCountry");
            String offZip = rs.getString("offZip");
            String phnNum = rs.getString("offPhnNum");
            String emailId = rs.getString("email");
            String companyLogo = rs.getString("companyLogo");
            

            Double previousBal=rs.getDouble("Previous_balance");
            Double chargeAmount=rs.getDouble("Charges_amount");
            Double adjustmentAmount=rs.getDouble("Adjustment_amount");
            Double taxAmount=rs.getDouble("Tax_amount");
            Double paidAmount=rs.getDouble("Paid_amount");
            Double adjustAndPayment=rs.getDouble("Due_amount");
            String message=rs.getString("Promotion_description");


            LocalDate billDate = JdbcSupport.getLocalDate(rs, "Bill_date");
            LocalDate dueDate = JdbcSupport.getLocalDate(rs, "Due_date");

            return new BillDetailsData(id,clientId,addrNo,clientName,billPeriod,street,zip,city,
			state,country,previousBal,chargeAmount,adjustmentAmount,taxAmount,
			paidAmount,adjustAndPayment,billDate,dueDate,message,addr1,addr2,offCity,offState,offCountry,offZip,phnNum,emailId,companyLogo);
			}


	}
	@Override

	public List<FinancialTransactionsData> getFinancialTransactionData(Long id) {

				TransactionDataMapper mapper = new TransactionDataMapper();
		        String sql = "select " + mapper.billSchema() + " and b.id ="+id;

		        return   this.jdbcTemplate.query(sql, mapper, new Object[] {  });


		    }

		private static final class TransactionDataMapper implements RowMapper<FinancialTransactionsData> {

	        public String billSchema() {
	            return "be.id,be.transaction_id as transaction_id,be.Transaction_type as Transaction_type, " +
				"be.description as description,be.Amount as Amount,be.Transaction_date as Transaction_date" +
				" from b_bill_master b,b_bill_details be where b.id = be.bill_id";
	        }

	        @Override
	        public FinancialTransactionsData mapRow(ResultSet rs, @SuppressWarnings("unused") int rowNum) throws SQLException {
	            Long id = rs.getLong("id");
	            Long transctionId = rs.getLong("transaction_id");
	            String transactionType = rs.getString("Transaction_type");
	            String description = rs.getString("description");
	            BigDecimal amount = rs.getBigDecimal("Amount");
	            LocalDate transactionDate = JdbcSupport.getLocalDate(rs, "Transaction_date");
				return new FinancialTransactionsData(transctionId,transactionType,transactionDate,amount);


				}
			}

		@Override
		public List<FinancialTransactionsData> retrieveStatments(Long clientId) {
			BillStatmentMapper mapper = new BillStatmentMapper();
			String sql = "select " + mapper.billStatemnetSchema();
			return this.jdbcTemplate.query(sql, mapper,new Object[] { clientId });

		}

		private static final class BillStatmentMapper implements
				RowMapper<FinancialTransactionsData> {

			@Override
			public FinancialTransactionsData mapRow(ResultSet rs, int rowNum)

					throws SQLException {
				Long id = rs.getLong("id");
				BigDecimal amount = rs.getBigDecimal("dueAmount");
				LocalDate billDate=JdbcSupport.getLocalDate(rs,"billDate");
				LocalDate dueDate=JdbcSupport.getLocalDate(rs,"dueDate");

				return new FinancialTransactionsData(id,billDate,dueDate,amount);
			}

			public String billStatemnetSchema() {

				return  "b.id as id,b.bill_date as billDate,b.due_date as dueDate,b.Due_amount as dueAmount from b_bill_master b where b.Client_id=? ";


			}
		}

		@Override
		public BigDecimal retrieveClientBalance(Long clientId) {

			ClientBalanceMapper mapper = new ClientBalanceMapper();
			String sql = "select " + mapper.billStatemnetSchema();

			BigDecimal previousBalance =  this.jdbcTemplate.queryForObject(sql, mapper,new Object[] { clientId });
			if( previousBalance == null ){
				previousBalance = BigDecimal.ZERO;
			}
			return previousBalance;
		}

		private static final class ClientBalanceMapper implements
				RowMapper<BigDecimal> {

			@Override
			public BigDecimal mapRow(ResultSet rs, int rowNum)

					throws SQLException {


				BigDecimal amount = rs.getBigDecimal("dueAmount");


				return amount;
			}

			public String billStatemnetSchema() {

				String result =   " IFNULL(b.Due_amount,0) as dueAmount  from b_bill_master b  where b.Client_id=? and id=(Select max(id) from b_bill_master a where a.client_id=b.client_id)";

				return result;
			}
}

		@Override
		public List<FinancialTransactionsData> retrieveSingleInvoiceData(
				Long invoiceId) {
			InvoiceDataMapper mapper = new InvoiceDataMapper();
			String sql = "select " + mapper.invoiceSchema();
			return this.jdbcTemplate.query(sql, mapper,
					new Object[] { invoiceId,invoiceId });

		}

		private static final class InvoiceDataMapper implements
				RowMapper<FinancialTransactionsData> {

			@Override
			public FinancialTransactionsData mapRow(ResultSet rs, int rowNum)

					throws SQLException {
				   Long chargeId=rs.getLong("chargeId");
				   String chargeType=rs.getString("chargeType");
				   String chargeDescription=rs.getString("chargeDescription");
				   BigDecimal chargeAmount = rs.getBigDecimal("chargeAmount");   
				   LocalDate chargeStartDate =JdbcSupport.getLocalDate(rs,"chargeStartDate");
				   LocalDate chargeEndDate =JdbcSupport.getLocalDate(rs,"chargeEndDate");
				BigDecimal taxAmount = rs.getBigDecimal("taxAmount");
				

				return new FinancialTransactionsData(chargeId,chargeType,chargeDescription,chargeAmount,taxAmount,chargeStartDate,chargeEndDate);
			}

			public String invoiceSchema() {

				return  "  * from (SELECT c.id as chargeId,c.charge_type AS chargeType,ch.charge_description AS chargeDescription," +
						"c.charge_start_date AS chargeStartDate, c.charge_end_date AS chargeEndDate,c.charge_amount AS chargeAmount,'' AS taxAmount" +
						"  FROM b_charge_codes ch,b_charge c WHERE c.charge_code = ch.charge_code AND c.invoice_id = ? union all" +
						" Select c.id,'Tax' AS chargeType,ct.tax_code  AS chargeDescription,c.charge_start_date AS chargeStartDate," +
						" c.charge_end_date AS chargeEndDate,'' AS chargeAmount,ct.tax_amount AS taxAmount from b_charge c,b_charge_tax ct" +
						" WHERE c.id=ct.charge_id  AND c.invoice_id = ? ) A order by 2,1 ";


			}
		}
}
