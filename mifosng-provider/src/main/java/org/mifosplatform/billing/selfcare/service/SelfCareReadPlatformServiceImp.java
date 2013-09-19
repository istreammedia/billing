package org.mifosplatform.billing.selfcare.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


@Service
public class SelfCareReadPlatformServiceImp implements SelfCareReadPlatformService {

	private JdbcTemplate jdbcTemplate;
	private PlatformSecurityContext context;
	
	
	@Autowired
	public SelfCareReadPlatformServiceImp(final PlatformSecurityContext context, final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	@Override
	public Long getClientId(String uniqueReference) {
		
		String sql = null;
		if(uniqueReference.contains("@")){
			sql = "select c.id as clientId, c.email as email from m_client c where c.email = ?";
		}else{
			sql = "select c.id as clientId, c.email as email from m_client c where c.account_no = ?";
		}
		ClientIdMapper rowMapper = new ClientIdMapper();
		return jdbcTemplate.queryForObject(sql,rowMapper,new Object[]{uniqueReference.contains("@")?uniqueReference:Long.getLong(uniqueReference)});
	}
	
	@Override
	public String getEmail(Long clientId) {
		String sql = "select c.email as email from m_client c where c.id = ?";
		ClientEmailMapper rowMapper = new ClientEmailMapper();
		return jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{clientId});
	}
	
	
	@Override
	public Long login(String userName, String password) {
		
		String sql = "";
		if(userName.contains("@")){
			sql = "select client_id as clientId from b_clientuser where unique_reference=? and password=?";
		}else{
			sql = "select client_id as clientId from b_clientuser where username=? and password=?";
		}	
		PasswordMapper mapper1 = new PasswordMapper();
		return jdbcTemplate.queryForObject(sql,mapper1,new Object[]{userName,password});
	}
	
	private class ClientEmailMapper implements RowMapper<String>{
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String email = rs.getString("email");
			return email;
		}
	}
	
	private class ClientIdMapper implements RowMapper<Long>{
		@Override
		public Long mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			Long clientId = rs.getLong("clientId");
			return clientId;
		}
	}
	private class PasswordMapper implements RowMapper<Long>{
		@Override
		public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long clientId = rs.getLong("clientId");
			return clientId;
		}
	}
}
