package org.mifosplatform.billing.epgprogramguide.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.epgprogramguide.data.EpgProgramGuideData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EpgProgramGuideReadPlatformServiceImp implements
		EpgProgramGuideReadPlatformService {

	
	private final JdbcTemplate jdbcTemplate;
	private PlatformSecurityContext context; 
	
	@Autowired
	public EpgProgramGuideReadPlatformServiceImp(final TenantAwareRoutingDataSource dataSource, final PlatformSecurityContext context) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional
	@Override
	public List<EpgProgramGuideData> retrivePrograms(String channelName,final Long counter) {
	
		String sql = "select p.channel_name as channelName, p.channel_icon as channelIcon, p.program_date as programDate, p.start_time as startTime, " +
				"p.stop_time as stopTime,p.program_title as programTitle, p.program_desc as programDescription, p.type as type, p.genre as genre" +
				" from b_program_guide p where p.channel_name=?  AND date(program_date) = date(now()) and time_format(start_time,'%H:%i') >=time_format(now(),'%H:%i')  limit ?,5";
		ChannelRowMapper channelRowMapper = new ChannelRowMapper();
		return jdbcTemplate.query(sql, channelRowMapper,new Object[]{channelName,counter});
	}
	
	
	
	private static final class ChannelRowMapper implements RowMapper<EpgProgramGuideData>{
		@Override
		public EpgProgramGuideData mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			String channelName = rs.getString("channelName");
			String channelIcon = rs.getString("channelIcon");
			String programDate = rs.getString("programDate");
			String startTime = rs.getString("startTime");
			String stopTime = rs.getString("stopTime");
			
			/*
			 * String programDate = JdbcSupport.getString(rs,"programDate");
			String startTime = JdbcSupport.getString(rs, "startTime");
			String stopTime = JdbcSupport.getString(rs,"stopTime");
			 * */
			
			String programTitle = rs.getString("programTitle");
			String programDescription = rs.getString("programDescription");
			String type = rs.getString("type");
			String genre = rs.getString("genre");
			
			
			return new EpgProgramGuideData(channelName, channelIcon, programDate, startTime, stopTime, programTitle, programDescription, type, genre);
		}
		
	}



}
