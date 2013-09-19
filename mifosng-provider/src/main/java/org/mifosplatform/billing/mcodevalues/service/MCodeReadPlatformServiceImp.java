package org.mifosplatform.billing.mcodevalues.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.mifosplatform.billing.mcodevalues.data.MCodeData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class MCodeReadPlatformServiceImp implements MCodeReadPlatformService {

	private JdbcTemplate jdbcTemplate;
	private PlatformSecurityContext context;
	
	@Autowired
	public MCodeReadPlatformServiceImp(final PlatformSecurityContext context, final TenantAwareRoutingDataSource dataSource ) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	@Override
	public Collection<MCodeData> getCodeValue(String codeName) {
		
		MCodeDataMapper rowMapper = new MCodeDataMapper();
		String sql = "select " + rowMapper.codeScheme()+" and code_name=?";
		return jdbcTemplate.query(sql, rowMapper,new Object[]{codeName});
	}
	
	private final class MCodeDataMapper implements RowMapper<MCodeData>{
		
		
		@Override
		public MCodeData mapRow(ResultSet rs, int rowNum) throws SQLException {
			final Long id = rs.getLong("id");
			final String codeValue = rs.getString("code_value");
			return new MCodeData(id,codeValue);
		}
		
		public String codeScheme(){
			return "b.id,code_value from m_code a, m_code_value b where a.id = b.code_id ";
		}
	}
	
	

}
