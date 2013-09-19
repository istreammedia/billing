package org.mifosplatform.billing.adjustment.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.adjustment.data.AdjustmentData;
import org.mifosplatform.billing.clientbalance.data.ClientBalanceData;
import org.mifosplatform.billing.clientbalance.service.ClientBalanceReadPlatformServiceImpl.ClientBalanceMapper;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
@Service
public class AdjustmentReadPlatformServiceImpl implements AdjustmentReadPlatformService{
	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;

	@Autowired
	public  AdjustmentReadPlatformServiceImpl(final PlatformSecurityContext context, final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	protected static final class AdjustmentMapperForId implements RowMapper<ClientBalanceData> {

		@Override
		public ClientBalanceData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum)
				throws SQLException {
            Long id=JdbcSupport.getLong(rs, "id");
			Long clientId = JdbcSupport.getLong(rs, "client_id");
			BigDecimal balanceAmount = JdbcSupport.getBigDecimalDefaultToZeroIfNull(rs,"amount_paid");
			return new ClientBalanceData(id,clientId,balanceAmount);

		}


		public String schema() {
			return "d.id as id, d.client_id as client_id , d.amount_paid as paid_amount  from b_adjustment d";
		}
}




	@Override
	public List<ClientBalanceData> retrieveAllAdjustments(Long id){
		 this.context.authenticatedUser();
		 ClientBalanceMapper mapper = new ClientBalanceMapper();
		String sql = "select " + mapper.schema()+ " where d.client_id=?";
		return this.jdbcTemplate.query(sql, mapper, new Object[] {id});
	}




	@Override
	public List<AdjustmentData> retrieveAllAdjustmentsCodes() {
		 this.context.authenticatedUser();
		 context.authenticatedUser();
			PlanMapper mapper = new PlanMapper();

			String sql = "Select b.id,code_value from m_code a, m_code_value b where a.id = b.code_id and code_name='Adjustment Code';";

			return this.jdbcTemplate.query(sql, mapper, new Object[] {});

		}

		private static final class PlanMapper implements RowMapper<AdjustmentData> {



			@Override
			public AdjustmentData mapRow(final ResultSet rs,
					@SuppressWarnings("unused") final int rowNum)
					throws SQLException {

				Long id = rs.getLong("id");
				String adjustment_code = rs.getString("code_value");
				

				return new AdjustmentData(id, adjustment_code);

			}
	}



}
