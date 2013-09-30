package org.mifosplatform.billing.processscheduledjobs.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.scheduledjobs.data.JobParameterData;
import org.mifosplatform.billing.scheduledjobs.data.ScheduleJobData;
import org.mifosplatform.billing.scheduledjobs.domain.JobParameters;
import org.mifosplatform.billing.scheduledjobs.domain.ScheduleJobs;
import org.mifosplatform.billing.scheduledjobs.domain.ScheduledJobRepository;
import org.mifosplatform.infrastructure.core.domain.MifosPlatformTenant;
import org.mifosplatform.infrastructure.core.service.DataSourcePerTenantService;
import org.mifosplatform.infrastructure.core.service.ThreadLocalContextUtil;
import org.mifosplatform.infrastructure.jobs.service.JobName;
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
	  private final ScheduledJobRepository scheduledJobDetailRepository;
	

	    @Autowired
	    public SheduleJobReadPlatformServiceImpl(final DataSourcePerTenantService dataSourcePerTenantService,final ScheduledJobRepository scheduledJobDetailRepository,
	            final TenantDetailsService tenantDetailsService) {
	            this.dataSourcePerTenantService = dataSourcePerTenantService;
	            this.tenantDetailsService = tenantDetailsService;
	            this.scheduledJobDetailRepository=scheduledJobDetailRepository;
	            
	        
	    }

	
/*	@Override
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

			}*/

			private static final class SheduleJobMapper implements RowMapper<ScheduleJobData> {

			public String sheduleLookupSchema() {
			return "  b.id as id,b.batch_name as batchName,b.query as query from b_batch b where b.batch_name=?";
  
			}

			@Override
			public ScheduleJobData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
             
			final Long id = rs.getLong("id");
			final String batchName = rs.getString("batchName");
			final String query = rs.getString("query");
			
			
			return new ScheduleJobData(id, batchName,query);
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

					final String sql = "select " + mapper.getmessageId();

					return jdbcTemplate.queryForObject(sql, mapper, new Object[]{ messageTemplateName });
					
					} catch (EmptyResultDataAccessException e) {
					return null;
					}
					

					}

					private static final class MessageIdMapper implements RowMapper<Long> {

					public String getmessageId() {
					return "mt.id as id from b_message_template mt where mt.template_description=?";
		  
					}

					@Override
					public Long mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

					final Long id = rs.getLong("id");
		
					return id ;
					}
				}

					


					@Override
					public List<ScheduleJobData> retrieveSheduleJobDetails(String paramValue) {

						try {
							
							  
					        final MifosPlatformTenant tenant = this.tenantDetailsService.loadTenantById("default");
					        ThreadLocalContextUtil.setTenant(tenant);
					        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePerTenantService.retrieveDataSource());
							final SheduleJobMapper mapper = new SheduleJobMapper();

							final String sql = "select " + mapper.sheduleLookupSchema();

							return jdbcTemplate.query(sql, mapper, new Object[] { paramValue });
							} catch (EmptyResultDataAccessException e) {
							return null;
							}

							
					}




					@Override
					public JobParameterData getJobParameters(String jobName) {


						try
						{
							ScheduleJobs scheduledJobDetail=this.scheduledJobDetailRepository.findByBatchName(JobName.INVOICE.toString());
						    
						    if(scheduledJobDetail!=null){
						    	 
						    	List<JobParameters> jobParameters=scheduledJobDetail.getDetails();
						    	
						    		return new JobParameterData(jobParameters); 
						    	  	 
						    }else{
						    	return null;
						    }
							
						}catch(EmptyResultDataAccessException exception){
							
							return null;
						}
					}


}
