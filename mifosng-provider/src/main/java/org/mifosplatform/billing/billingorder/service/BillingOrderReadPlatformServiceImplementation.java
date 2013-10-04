package org.mifosplatform.billing.billingorder.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.billingorder.data.BillingOrderData;
import org.mifosplatform.billing.billingorder.data.GenerateInvoiceData;
import org.mifosplatform.billing.order.data.OrderPriceData;
import org.mifosplatform.billing.service.DiscountMasterData;
import org.mifosplatform.billing.taxmaster.data.TaxMappingRateData;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class BillingOrderReadPlatformServiceImplementation implements
		BillingOrderReadPlatformService {

	private final PlatformSecurityContext context;
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public BillingOrderReadPlatformServiceImplementation(
			final PlatformSecurityContext context,
			final TenantAwareRoutingDataSource dataSource) {

		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		LocalDate localDate = new LocalDate();


	}

	@Override
	public List<BillingOrderData> retrieveBillingOrderData(Long clientId,LocalDate date,Long planId) {

		BillingOrderMapper billingOrderMapper = new BillingOrderMapper();
		String sql = "select " + billingOrderMapper.billingOrderSchema();
		return this.jdbcTemplate.query(sql, billingOrderMapper,
				new Object[] { clientId,planId,date.toString() });
	}

	private static final class BillingOrderMapper implements
			RowMapper<BillingOrderData> {

		@Override
		public BillingOrderData mapRow(ResultSet resultSet,
				@SuppressWarnings("unused") int rowNum) throws SQLException {
			
			Long clientOderId = resultSet.getLong("clientOrderId");
			Long orderPriceId = resultSet.getLong("orderPriceId");
			Long planId = resultSet.getLong("planId");
			Long clientId = resultSet.getLong("clientId");
			Date startDate = resultSet.getDate("startDate");
			Date nextBillableDate = resultSet.getDate("nextBillableDate");
			Date endDate = resultSet.getDate("endDate");
			String billingFrequency = resultSet.getString("billingFrequency");
			String chargeCode = resultSet.getString("chargeCode");
			String chargeType = resultSet.getString("chargeType");
			Integer chargeDuration = resultSet.getInt("chargeDuration");
			String durationType = resultSet.getString("durationType");
			Date invoiceTillDate = resultSet.getDate("invoiceTillDate");
			BigDecimal price = resultSet.getBigDecimal("price");
			String billingAlign = resultSet.getString("billingAlign");
			Date billStartDate = resultSet.getDate("billStartDate");
			Date billEndDate = resultSet.getDate("billEndDate");
			Long orderStatus = resultSet.getLong("orderStatus");
			Integer taxInclusive = resultSet.getInt("taxInclusive");
			return new BillingOrderData(clientOderId,orderPriceId,planId, clientId, startDate,
					nextBillableDate, endDate, billingFrequency, chargeCode,
					chargeType, chargeDuration, durationType, invoiceTillDate,
					price, billingAlign,billStartDate,billEndDate,orderStatus,taxInclusive);
		}

		public String billingOrderSchema() {

			return " co.id as clientOrderId,op.id AS orderPriceId,co.plan_id as planId,co.client_id AS clientId,co.start_date AS startDate,IFNULL(op.next_billable_day, co.start_date) AS nextBillableDate,"
					+ "co.end_date AS endDate,co.billing_frequency AS billingFrequency,op.charge_code AS chargeCode,op.charge_type AS chargeType,"
					+ "op.charge_duration AS chargeDuration,op.duration_type AS durationType,op.invoice_tilldate AS invoiceTillDate,op.price AS price,co.order_status as orderStatus,op.tax_inclusive as taxInclusive, "
					+ "co.billing_align AS billingAlign,op.bill_start_date as billStartDate,Date_format(IFNULL(op.bill_end_date,'3099-12-31'), '%Y-%m-%d') AS billEndDate "
					+ "FROM b_orders co left JOIN b_order_price op ON co.id = op.order_id"
					+ " WHERE co.client_id = ? AND co.id = ? AND Date_format(IFNULL(op.invoice_tilldate,now() ),'%Y-%m-%d') <= ? "
					+ " AND Date_format(IFNULL(op.next_billable_day, co.start_date ), '%Y-%m-%d')  <= Date_format(IFNULL(op.bill_end_date,'3099-12-31'), '%Y-%m-%d')";
		}

	}

	@Override
	public List<TaxMappingRateData> retrieveTaxMappingDate(Long clientId,String chargeCode) {
		
		try{
			
		
		TaxMappingMapper taxMappingMapper = new TaxMappingMapper();
		String sql = "select" + taxMappingMapper.taxMappingSchema()
				+ " AND CA.client_id = ?  AND tm.charge_code =? AND pd.state_id =S.id";
		return this.jdbcTemplate.query(sql, taxMappingMapper,
				new Object[] { clientId,chargeCode });
	}catch(EmptyResultDataAccessException accessException){
	       return null;	
	}
	}
	private static final class TaxMappingMapper implements
			RowMapper<TaxMappingRateData> {

		@Override
		public TaxMappingRateData mapRow(ResultSet rs, int rowNum)
				throws SQLException {

			Long id = rs.getLong("id");
			String chargeCode = rs.getString("chargeCode");
			String taxCode = rs.getString("taxCode");
			Date startDate = rs.getDate("startDate");
			BigDecimal rate = rs.getBigDecimal("rate");

			return new TaxMappingRateData(id, chargeCode, taxCode, startDate,
					rate);
		}

		public String taxMappingSchema() {

			return " tm.id AS id,tm.charge_code AS chargeCode,tm.tax_code AS taxCode,tm.start_date AS startDate,tm.type AS type,tm.rate AS rate" +
					" FROM b_state S,b_tax_mapping_rate tm,b_priceregion_detail pd,b_priceregion_master prm,b_client_address CA WHERE  pd.priceregion_id = tm.tax_region_id" +
					" AND prm.id = pd.priceregion_id   AND CA.state = S.state_name";
		}

	}

	@Override
	public List<OrderPriceData> retrieveInvoiceTillDate(Long orderId) {

		OrderPriceMapper orderPriceMapper = new OrderPriceMapper();
		String sql = "select " + orderPriceMapper.orderPriceSchema();
		return this.jdbcTemplate.query(sql, orderPriceMapper,
				new Object[] { orderId });

	}

	private static final class OrderPriceMapper implements
			RowMapper<OrderPriceData> {

		@Override
		public OrderPriceData mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			Long id = rs.getLong("orderPriceId");
			Long orderId = rs.getLong("orderId");
			Long serviceId = rs.getLong("serviceId");
			String chargeCode = rs.getString("chargeCode");
			String chargeType = rs.getString("chargeType");
			String chargeDuration = rs.getString("chargeDuration");
			String durationType = rs.getString("durationType");
			Date invoiceTillDate = rs.getDate("invoiceTillDate");
			BigDecimal price = rs.getBigDecimal("price");
			Long createdbyId = rs.getLong("createdId");
			Date createdDate = rs.getDate("createdDate");
			Date lastModefiedDate = rs.getDate("lastModefiedDate");
			Long lastModefiedId = rs.getLong("lastModefiedId");
			return new OrderPriceData(id, orderId, serviceId, chargeCode,
					chargeType, chargeDuration, durationType, invoiceTillDate,
					price, createdbyId, createdDate, lastModefiedDate,
					lastModefiedId);
		}

		public String orderPriceSchema() {
			return " op.id as orderPriceId,op.order_id as orderId,op.service_id as serviceId,"
					+ " op.charge_code as chargeCode,op.charge_type as chargeType,op.charge_duration as chargeDuration,"
					+ " op.duration_type as durationType,op.invoice_tilldate as invoiceTillDate,"
					+ " op.price as price,op.createdby_id as createdId,op.created_date as createdDate,"
					+ " op.lastmodified_date lastModefiedDate,op.lastmodifiedby_id as lastModefiedId "
					+ "FROM b_order_price op WHERE order_id = ? ";

		}
	}

	@Override
	public List<Long> retrieveOrderIds(Long clientId,LocalDate processDate) {
		OrderIdMapper planIdMapper = new OrderIdMapper();
		String sql = "select" + planIdMapper.planIdSchema();
		return this.jdbcTemplate.query(sql, planIdMapper,new Object[] { clientId,processDate.minusDays(1).toDate(),processDate.toDate() });
		
	}
	
	
	private static final class OrderIdMapper implements RowMapper<Long> {

		@Override
		public Long  mapRow(ResultSet resultSet, int rowNum) throws SQLException {
//			Long orderId = resultSet.getLong("orderId");
//			String durationType = resultSet.getString("durationType");
//			Date billStartDate = resultSet.getDate("billStartDate");
			return resultSet.getLong("orderId");
		}
		

		public String planIdSchema() {
			return " distinct os.id as orderId FROM b_orders os"+
					" left outer join b_order_price op on os.id = op.order_id"+
					" WHERE os.client_id = ? AND Date_format(IFNULL(os.next_billable_day, ?), '%Y-%m-%d') < ? and os.is_deleted = 'N' "+
					" and Date_format(IFNULL(os.next_billable_day,Date_format(IFNULL(op.bill_start_date, '3099-12-12'),'%Y-%m-%d')), '%Y-%m-%d')" +
					" <=Date_format(IFNULL(op.bill_end_date, '3099-12-12'),'%Y-%m-%d'); ";
		}
		
		
	}
	
private static final class OrderMapper implements RowMapper<GenerateInvoiceData> {
		
		public String schema() {
			return " o.client_id as clientId, o.next_billable_day as nextBillableDay from b_orders o join m_client c on c.id=o.client_id  and Date_format(IFNULL(o.next_billable_day, ?), '%Y-%m-%d') <= ? ";
		}

		@Override
		public GenerateInvoiceData mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long clientId = rs.getLong("clientId");
			LocalDate nextBillableDay = JdbcSupport.getLocalDate(rs, "nextBillableDay");
			return new GenerateInvoiceData(clientId, nextBillableDay); 
		}
		
	}
	
	@Override
	public List<GenerateInvoiceData> retrieveClientsWithOrders(LocalDate processDate) {
		AppUser user = this.context.authenticatedUser();
		OrderMapper mapper = new OrderMapper();
		String schema = "Select " + mapper.schema() + " and o.createdby_id = ? ";
		return this.jdbcTemplate.query(schema, mapper, new Object[]{processDate.toDate(),processDate.toDate(),user.getId()});
	}
	
	
	private static final class DiscountOrderMapper implements
	RowMapper<DiscountMasterData> {

@Override
public DiscountMasterData mapRow(ResultSet rs, int rowNum)
		throws SQLException {
	Long discountMasterid = rs.getLong("discountOrderId");
	Long orderPriceId = rs.getLong("orderPriceId");
	Long orderDiscountId = rs.getLong("discountId");
	Date discountStartDate = rs.getDate("discountStartDate");
	Date discountEndDate = rs.getDate("discountEndDate");
	String discountType = rs.getString("discountType");
	BigDecimal discountRate = rs.getBigDecimal("discountRate");
	String isDeleted = rs.getString("isDeleted");

	return new DiscountMasterData(discountMasterid, orderPriceId,orderDiscountId, 
			new LocalDate(discountStartDate), discountEndDate,discountType, discountRate, isDeleted);
}

public String discountOrderSchema() {
	return    " od.id AS discountOrderId,od.orderprice_id AS orderPriceId,od.discount_id AS discountId,od.discount_startdate AS discountStartDate,"
			+ " od.discount_enddate AS discountEndDate,od.discount_type AS discountType,od.discount_rate AS discountRate,od.is_deleted AS isDeleted "
			+ " FROM b_orders os inner join b_order_price op on op.order_id = os.id inner join b_order_discount od on od.order_id = os.id and od.orderprice_id=op.id "
			+ " WHERE od.is_deleted='N' and os.id= ? and op.id= ? ";

}
}

	@Override
	public List<DiscountMasterData> retrieveDiscountOrders(Long orderId,Long orderPriceId) {
		DiscountOrderMapper discountOrderMapper = new DiscountOrderMapper();
		String sql = "select " + discountOrderMapper.discountOrderSchema();
		return this.jdbcTemplate.query(sql, discountOrderMapper,
				new Object[] {orderId,orderPriceId});

	}

	@Override
	public List<TaxMappingRateData> retrieveDefaultTaxMappingDate(Long clientId, String chargeCode) {

		try{
		
		TaxMappingMapper taxMappingMapper = new TaxMappingMapper();
		String sql = "select" + taxMappingMapper.taxMappingSchema()
				+ " AND CA.client_id = ?  AND tm.charge_code =? AND pd.state_id =0";
		return this.jdbcTemplate.query(sql, taxMappingMapper,new Object[] { clientId,chargeCode });
		
	}catch(EmptyResultDataAccessException accessException){
	       return null;	
	}
	}

	@Override
	public List<BillingOrderData> getReverseBillingOrderData(Long clientId,LocalDate date, Long planId) {

		ReverseBillingOrderMapper billingOrderMapper = new ReverseBillingOrderMapper();
		String sql = "select " + billingOrderMapper.billingOrderSchema();
		return this.jdbcTemplate.query(sql, billingOrderMapper,
				new Object[] { clientId,planId });
	}

	private static final class ReverseBillingOrderMapper implements
			RowMapper<BillingOrderData> {

		@Override
		public BillingOrderData mapRow(ResultSet resultSet,
				@SuppressWarnings("unused") int rowNum) throws SQLException {
			
			Long clientOderId = resultSet.getLong("clientOrderId");
			Long orderPriceId = resultSet.getLong("orderPriceId");
			Long planId = resultSet.getLong("planId");
			Long clientId = resultSet.getLong("clientId");
			Date startDate = resultSet.getDate("startDate");
			Date nextBillableDate = resultSet.getDate("nextBillableDate");
			Date endDate = resultSet.getDate("endDate");
			String billingFrequency = resultSet.getString("billingFrequency");
			String chargeCode = resultSet.getString("chargeCode");
			String chargeType = resultSet.getString("chargeType");
			Integer chargeDuration = resultSet.getInt("chargeDuration");
			String durationType = resultSet.getString("durationType");
			Date invoiceTillDate = resultSet.getDate("invoiceTillDate");
			BigDecimal price = resultSet.getBigDecimal("price");
			String billingAlign = resultSet.getString("billingAlign");
			Date billStartDate = resultSet.getDate("billStartDate");
			Date billEndDate = resultSet.getDate("billEndDate");
			Long orderStatus = resultSet.getLong("orderStatus");
			Integer taxInclusive = resultSet.getInt("taxInclusive");
			//Long invoiceId = resultSet.getLong("invoiceId");
			return new BillingOrderData(clientOderId,orderPriceId,planId, clientId, startDate,nextBillableDate, endDate, billingFrequency,
					chargeCode,chargeType, chargeDuration, durationType, invoiceTillDate,price, billingAlign,billStartDate,billEndDate,orderStatus,
					taxInclusive,null);
		}

		public String billingOrderSchema() {

			return " co.id as clientOrderId,op.id AS orderPriceId,co.plan_id as planId,co.client_id AS clientId,co.start_date AS startDate,IFNULL(op.next_billable_day, co.start_date) AS nextBillableDate,"
					+ "co.end_date AS endDate,co.billing_frequency AS billingFrequency,op.charge_code AS chargeCode,op.charge_type AS chargeType,"
					+ "op.charge_duration AS chargeDuration,op.duration_type AS durationType,op.invoice_tilldate AS invoiceTillDate,op.price AS price,co.order_status as orderStatus,op.tax_inclusive as taxInclusive,"
					+ "co.billing_align AS billingAlign,op.bill_start_date as billStartDate,Date_format(IFNULL(op.bill_end_date,'3099-12-31'), '%Y-%m-%d') AS billEndDate "
					+ "FROM b_orders co left JOIN b_order_price op ON co.id = op.order_id"
					+ " WHERE co.client_id = ? AND co.id = ?"/* AND Date_format(IFNULL(op.invoice_tilldate,now() ),'%Y-%m-%d') >= ? "*/;
					
		}

	}
}
