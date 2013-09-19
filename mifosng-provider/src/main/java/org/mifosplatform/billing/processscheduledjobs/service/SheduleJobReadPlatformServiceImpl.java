package org.mifosplatform.billing.processscheduledjobs.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.scheduledjobs.data.ScheduleJobData;
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
public class SheduleJobReadPlatformServiceImpl implements SheduleJobReadPlatformService{
	  private final TenantDetailsService tenantDetailsService;
	  private final DataSourcePerTenantService dataSourcePerTenantService;
	

	    @Autowired
	    public SheduleJobReadPlatformServiceImpl(final DataSourcePerTenantService dataSourcePerTenantService,
	            final TenantDetailsService tenantDetailsService) {
	            this.dataSourcePerTenantService = dataSourcePerTenantService;
	            this.tenantDetailsService = tenantDetailsService;
	            
	        
	    }

	
	@Override
	public List<ScheduleJobData> retrieveSheduleJobDetails() {
		try {
			
			  
	        final MifosPlatformTenant tenant = this.tenantDetailsService.loadTenantById("default");
	        ThreadLocalContextUtil.setTenant(tenant);
	        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePerTenantService.retrieveDataSource());
			final SheduleJobMapper mapper = new SheduleJobMapper();

			final String sql = "select " + mapper.sheduleLookupSchema();

			return jdbcTemplate.query(sql, mapper, new Object[] { });
			} catch (EmptyResultDataAccessException e) {
			return null;
			}
			

			}

			private static final class SheduleJobMapper implements RowMapper<ScheduleJobData> {

			public String sheduleLookupSchema() {
			return " s.id as id,s.process as processType,b.query as query,s.process_params as processParam  FROM b_schedule_jobs s,  b_batch b"
				 + "  where s.batch_name = b.batch_name and s.status='N' ";
  
			}

			@Override
			public ScheduleJobData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
             
			final Long id = rs.getLong("id");
			final String processType = rs.getString("processType");
			final String query = rs.getString("query");
			final String processParam=rs.getString("processParam");
			
			return new ScheduleJobData(id, processType,query,processParam);
			}
			}

			@Override
			public List<Long> getClientIds(String query) {
				try {
					
					  
			        final MifosPlatformTenant tenant = this.tenantDetailsService.loadTenantById("default");
			        ThreadLocalContextUtil.setTenant(tenant);
			        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePerTenantService.retrieveDataSource());
					final ClientIdMapper mapper = new ClientIdMapper();

					//final String sql ="select c.id   as clientId from m_client c where c.id < 6";

					return jdbcTemplate.query(query, mapper, new Object[] { });
					} catch (EmptyResultDataAccessException e) {
					return null;
					}
					

					}
			
			private static final class ClientIdMapper implements RowMapper<Long> {

				

				@Override
				public Long mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

				final Long clientId = rs.getLong("clientId");
			       return clientId;
				
					}
				}
			
			@Override
			public Long getMessageId(String messageTemplateName) {
				
				try {  
			        final MifosPlatformTenant tenant = this.tenantDetailsService.loadTenantById("default");
			        ThreadLocalContextUtil.setTenant(tenant);
			        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePerTenantService.retrieveDataSource());
					final MessageIdMapper mapper = new MessageIdMapper();

					final String sql = "select " + mapper.getmessageId()+"'"+messageTemplateName+"'";

					return jdbcTemplate.queryForObject(sql, mapper, new Object[]{});
					
					} catch (EmptyResultDataAccessException e) {
					return null;
					}
					

					}

					private static final class MessageIdMapper implements RowMapper<Long> {

					public String getmessageId() {
					return "mt.id as id from b_message_template mt where mt.template_description=";
		  
					}

					@Override
					public Long mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

					final Long id = rs.getLong("id");
		
					return id ;
					}
				}


}
