package org.mifosplatform.billing.action.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.action.data.ActionDetaislData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class ActionDetailsReadPlatformServiceImpl implements ActionDetailsReadPlatformService{
	
	
	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;

	@Autowired
	public  ActionDetailsReadPlatformServiceImpl(final PlatformSecurityContext context, final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}



	public List<ActionDetaislData> retrieveActionDetails(String eventType) {
   
		try{

		PlanMapper mapper = new PlanMapper();
		String sql = "select " + mapper.schema()+"and c.code_value='"+eventType+"'" ;
		return this.jdbcTemplate.query(sql, mapper, new Object[] {});
		}catch(EmptyResultDataAccessException accessException){
			return null;
		}

	}

	private static final class PlanMapper implements RowMapper<ActionDetaislData> {

		public String schema() {
			return " a.id AS actionId,a.action_name AS actionName,a.process AS processName" +
					" FROM b_event_action a,m_code_value c where  a.event_id=c.code_id and a.isCheck='Y'";

		}

		@Override
		public ActionDetaislData mapRow(final ResultSet rs,
				@SuppressWarnings("unused") final int rowNum)
				throws SQLException {
			Long id = rs.getLong("actionId");
			String procedureName = rs.getString("processName");
			String procedureType = rs.getString("actionName");
		




			return new ActionDetaislData(id,procedureName,procedureType);

		}
	}

	

}
