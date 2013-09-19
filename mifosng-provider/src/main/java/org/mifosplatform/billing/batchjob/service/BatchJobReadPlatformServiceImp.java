package org.mifosplatform.billing.batchjob.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.batchjob.data.BatchJobData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class BatchJobReadPlatformServiceImp implements
		BatchJobReadPlatformService {

	private PlatformSecurityContext context; 
	private JdbcTemplate jdbcTemplate;
	

	@Autowired
	public BatchJobReadPlatformServiceImp(final PlatformSecurityContext context, final TenantAwareRoutingDataSource dataSource) {
	
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<BatchJobData> retriveBatchDetails() {
		
		String sql = "select"+query()+"from b_batch b";
		ScheduleMapper rowMapper = new ScheduleMapper();
		
		return jdbcTemplate.query(sql,rowMapper);
	}
	
	
	private String query(){
		String sql = " b.id as id, b.batch_name as batchName, b.query as query ";
		return sql;
	}
	
	
	private class ScheduleMapper implements RowMapper<BatchJobData>{
		@Override
		public BatchJobData mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			final Long id = rs.getLong("id");
			final String batchName = rs.getString("batchName");
			final String query = rs.getString("query");
			return new BatchJobData(id,batchName,query);
		}
	}
	
	
	

}
