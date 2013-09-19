package org.mifosplatform.billing.processrequest.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.processrequest.data.ProcessingDetailsData;
import org.mifosplatform.infrastructure.core.domain.MifosPlatformTenant;
import org.mifosplatform.infrastructure.core.service.DataSourcePerTenantService;
import org.mifosplatform.infrastructure.core.service.ThreadLocalContextUtil;
import org.mifosplatform.infrastructure.security.service.TenantDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


@Service
public class ProcessRequestReadplatformServiceImpl implements ProcessRequestReadplatformService{
	
	
	
	  private final TenantDetailsService tenantDetailsService;
	  private final DataSourcePerTenantService dataSourcePerTenantService;
	  	

	    @Autowired
	    public ProcessRequestReadplatformServiceImpl(final DataSourcePerTenantService dataSourcePerTenantService,
	            final TenantDetailsService tenantDetailsService) {
	            this.dataSourcePerTenantService = dataSourcePerTenantService;
	            this.tenantDetailsService = tenantDetailsService;
	           
	        
	    }

	
	@Override
	public List<ProcessingDetailsData> retrieveProcessingDetails() {
		try {
			
			  
	        final MifosPlatformTenant tenant = this.tenantDetailsService.loadTenantById("default");
	        ThreadLocalContextUtil.setTenant(tenant);
	        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePerTenantService.retrieveDataSource());
			final ClientOrderMapper mapper = new ClientOrderMapper();

			final String sql = "select " + mapper.clientOrderLookupSchema();

			return jdbcTemplate.query(sql, mapper, new Object[] { });
			} catch (EmptyResultDataAccessException e) {
			return null;
			}

			}

			private static final class ClientOrderMapper implements RowMapper<ProcessingDetailsData> {

			public String clientOrderLookupSchema() {
			return "p.id as id,p.order_id as orderId,p.provisioing_system as provisionigSys,p.request_type as requestType  FROM b_process_request p" +
					" where p.is_processed='Y' and p.is_notify='N'";
			}

			@Override
			public ProcessingDetailsData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

			final Long id = rs.getLong("id");
			final Long orderId = rs.getLong("orderId");
			final String provisionigSys = rs.getString("provisionigSys");
			final String requestType = rs.getString("requestType");
		    
			
		
			
			return new ProcessingDetailsData(id, orderId,provisionigSys,requestType);
			}
			}

	
			

}
