package org.mifosplatform.billing.batchjobscheduling.service;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.mifosplatform.billing.batchjobscheduling.data.BatchJobSchedulingData;
import org.mifosplatform.billing.batchjobscheduling.data.BatchNameData;
import org.mifosplatform.billing.batchjobscheduling.data.MessageTemplateData;
import org.mifosplatform.billing.batchjobscheduling.data.ProcessTypeData;
import org.mifosplatform.billing.batchjobscheduling.data.ScheduleTypeData;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class BatchJobSchedulingReadPlatformServiceImp implements BatchJobSchedulingReadPlatformService {


	private JdbcTemplate jdbcTemplate;
	private PlatformSecurityContext context;
	
	
	@Autowired
	public BatchJobSchedulingReadPlatformServiceImp(final TenantAwareRoutingDataSource dataSource, final PlatformSecurityContext context) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<BatchJobSchedulingData> retriveAllData() {
	
		String sql = "select "+query()+" from b_schedule_jobs b";
		BatchJobMapper rowMapper = new BatchJobMapper();
		return jdbcTemplate.query(sql, rowMapper);

	
	}
	
	
	@Override
	public BatchJobSchedulingData retriveSingleData() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private String query(){
		String sql = "b.id as id, b.batch_name as batchName, b.process as process, b.schedule_type as scheduleType, b.status as status, b.process_params as msgTemplateDescription, b.schedule_time as scheduleTime";
		return sql;
	}
	
	
	public List<ScheduleTypeData> retriveScheduleTypes(){
		String sql = "select mcv.id as id,mcv.code_value as scheduleType from m_code_value mcv,m_code mc where mcv.code_id=mc.id and mc.code_name='Schedule Type' order by mcv.id";
		ScheduleTypeMapper rowMapper = new ScheduleTypeMapper();
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	public List<MessageTemplateData> retriveMessageTemplates(){
		String sql = "select m.id as id, template_description as msgTemplateDescription from b_message_template m";
		MessageTemplateMapper rowMapper = new MessageTemplateMapper();
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	public List<ProcessTypeData> retriveProcessTypes(){
		String sql = "select mcv.id as id,mcv.code_value as process from m_code_value mcv,m_code mc where mcv.code_id=mc.id and mc.code_name='Process' order by mcv.id";
		ProcessTypeMapper rowMapper = new ProcessTypeMapper();
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	public List<BatchNameData> retriveBatchNames(){
		
		String sql = "select id as id, batch_name as batchName from b_batch order by id";
		BatchNameMaper rowMapper = new BatchNameMaper();
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	private class MessageTemplateMapper implements RowMapper<MessageTemplateData>{
		
		@Override
		public MessageTemplateData mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			
			final Long id = rs.getLong("id");
			final String msgTemplateDescription = rs.getString("msgTemplateDescription");
			return new MessageTemplateData(id, msgTemplateDescription);
		}
		
	}
	
	
	private class ScheduleTypeMapper implements RowMapper<ScheduleTypeData>{
		
		
		@Override
		public ScheduleTypeData mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			final Long id = rs.getLong("id");
			final String scheduleType = rs.getString("scheduleType");
			return new ScheduleTypeData(id, scheduleType);
		}
	}
	
	
	private class ProcessTypeMapper implements RowMapper<ProcessTypeData>{
		
		@Override
		public ProcessTypeData mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			Long id = rs.getLong("id");
			String process = rs.getString("process");
			return new ProcessTypeData(id,process);
		}
	}
	
	private class BatchNameMaper implements RowMapper<BatchNameData>{
		
		@Override
		public BatchNameData mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			Long id = rs.getLong("id");
			String batchName = rs.getString("batchName");
			return new BatchNameData(id, batchName);
		}
	}
	
	private class BatchJobMapper implements RowMapper<BatchJobSchedulingData>{
		
		@Override
		public BatchJobSchedulingData mapRow(ResultSet rs, int rowNum)throws SQLException {
			final Long id = rs.getLong("id");
			final String batchName = rs.getString("batchName");
			final String process = rs.getString("process");
			final String scheduleType = rs.getString("scheduleType");
			final String status = rs.getString("status");
			final DateTime scheduleTime = JdbcSupport.getDateTime(rs,"scheduleTime");
			String msgTemplateDescription = rs.getString("msgTemplateDescription");
			if(msgTemplateDescription == null){
				msgTemplateDescription = "No Paramters";
			}
			
			return new BatchJobSchedulingData(id, batchName, process, scheduleType, status, scheduleTime, msgTemplateDescription);
		}
	}

}
