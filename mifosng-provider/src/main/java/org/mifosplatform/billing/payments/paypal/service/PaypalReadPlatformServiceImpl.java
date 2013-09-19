package org.mifosplatform.billing.payments.paypal.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mifosplatform.billing.address.data.AddressData;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class PaypalReadPlatformServiceImpl implements PaypalReadPlatformService {

	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;

	@Autowired
	public PaypalReadPlatformServiceImpl(final PlatformSecurityContext context,
			final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Long retrieveClientId(JsonCommand command) {
		
		try{
			context.authenticatedUser();
			PaypalMapper mapper = new PaypalMapper();

			String sql = "SELECT c.id AS clientId FROM  m_client c WHERE c.email = ?";
 
 ;

			return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] { command.getSupportedEntityType() });
			}catch (final EmptyResultDataAccessException e) {
				return null;
			}
		
	}
	
	private static final class PaypalMapper implements RowMapper<Long> {


		@Override
		public Long mapRow(final ResultSet rs,
				@SuppressWarnings("unused") final int rowNum)
				throws SQLException {
			Long clientId = rs.getLong("clientId");
			return clientId;

		}
	}

}
