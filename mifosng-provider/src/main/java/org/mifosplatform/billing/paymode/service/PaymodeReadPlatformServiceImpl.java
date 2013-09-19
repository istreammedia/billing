package org.mifosplatform.billing.paymode.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.payments.data.PaymentData;
import org.mifosplatform.billing.paymode.data.McodeData;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class PaymodeReadPlatformServiceImpl implements
		PaymodeReadPlatformService {

	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;

	@Autowired
	public PaymodeReadPlatformServiceImpl(
			final PlatformSecurityContext context,
			final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private static final class PaymodeMapper implements RowMapper<McodeData> {

		public String codeScheme() {
			return "b.id,code_value from m_code a, m_code_value b where a.id = b.code_id ";
		}
		
		@Override
		public McodeData mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long id = rs.getLong("id");
			String paymodeCode = rs.getString("code_value");

			return  McodeData.instance(id, paymodeCode);
		}

	}

	@Transactional
	@Override
	public Collection<McodeData> retrievemCodeDetails(String codeName) {
		PaymodeMapper mapper = new PaymodeMapper();
		String sql = "select " + mapper.codeScheme()+" and code_name=?";

		return this.jdbcTemplate.query(sql, mapper, new Object[] { codeName });
	}

	@Override
	public McodeData retrieveSinglePaymode(Long paymodeId) {
		PaymodeMapper mapper = new PaymodeMapper();
		String sql = "select " + mapper.codeScheme() + " and b.id="+ paymodeId;

		return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] {});
	}

@Override
public McodeData retrievePaymodeCode(JsonCommand command) {
	PaymodeMapper1 mapper = new PaymodeMapper1();
	String sql = "select id from m_code where code_name='" +command.stringValueOfParameterNamed("code_id")+"'";

	return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] {});
}
private static final class PaymodeMapper1 implements RowMapper<McodeData> {

	@Override
	public McodeData mapRow(ResultSet rs, int rowNum) throws SQLException {
		Long id = rs.getLong("id");
		return  McodeData.instance1(id);
	}

}

@Override
public List<PaymentData> retrivePaymentsData(final Long clientId) {
 String sql = "select (select display_name from m_client where id = p.client_id) as clientName, (select code_value from m_code_value where id = p.paymode_id) as payMode, p.payment_date as paymentDate, p.amount_paid as amountPaid, p.is_deleted as isDeleted, p.bill_id as billNumber, p.receipt_no as receiptNo from b_payments p where p.client_id=?";
 PaymentsMapper pm = new PaymentsMapper();
 return jdbcTemplate.query(sql, pm,new Object[]{clientId});
}

private class PaymentsMapper implements RowMapper<PaymentData>{
	  @Override
	  public PaymentData mapRow(ResultSet rs, int rowNum) throws SQLException {
	   String clientName = rs.getString("clientName");
	   String payMode = rs.getString("payMode");
	   LocalDate paymentDate = JdbcSupport.getLocalDate(rs, "paymentDate");
	   BigDecimal amountPaid = rs.getBigDecimal("amountPaid");
	   Boolean isDeleted = rs.getBoolean("isDeleted");
	   Long billNumber = rs.getLong("billNumber");
	   String receiptNumber = rs.getString("receiptNo");
	   return new PaymentData(clientName,payMode,paymentDate,amountPaid,isDeleted,billNumber,receiptNumber);
	  }
	 }
}
