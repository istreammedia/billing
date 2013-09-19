package org.mifosplatform.billing.mediadevice.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.mediadevice.data.MediaDeviceData;
import org.mifosplatform.billing.plan.data.PlanData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


@Service
public class MediaDeviceReadPlatformServiceImpl implements MediaDeviceReadPlatformService{
	
	private final PlatformSecurityContext context;
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public MediaDeviceReadPlatformServiceImpl (final PlatformSecurityContext context,final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	

	@Override
	public MediaDeviceData retrieveDeviceDetails(String deviceId) {
		try {
			final EventMasterMapper eventMasterMapper = new EventMasterMapper();
			
			final String sql = eventMasterMapper.eventMasterSchema();
			return jdbcTemplate.queryForObject(sql, eventMasterMapper,new Object[] {deviceId,deviceId});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	private static final class EventMasterMapper implements RowMapper<MediaDeviceData> {
		public String eventMasterSchema() {
			return "SELECT a.id AS deviceId,a.client_id AS clientId,mc.code_value AS clientType,mc.id AS clientTypeId FROM b_allocation a, m_client c, m_code_value mc" +
					" WHERE a.client_id = c.id AND mc.id = c.category_type AND serial_no = ? UNION SELECT a.id,a.client_id,mc.code_value,mc.id FROM b_owned_hardware a, " +
					" m_client c, m_code_value mc WHERE a.client_id = c.id AND mc.id = c.category_type AND serial_number =? "; 
  
		}
		@Override
		public MediaDeviceData mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			
			final Long deviceId = rs.getLong("deviceId");
			final Long clientId = rs.getLong("clientId");
			final String clientType = rs.getString("clientType");
			final Long clientTypeId = rs.getLong("clientTypeId");
			return new MediaDeviceData(deviceId,clientId,clientType,clientTypeId);
		}
	}

	@Override
	public List<MediaDeviceData> retrieveDeviceDataDetails(String deviceId) {
		try {
			final EventMasterMapper1 eventMasterMapper = new EventMasterMapper1();
			
			final String sql = eventMasterMapper.eventMasterSchema();
			return jdbcTemplate.query(sql, eventMasterMapper,new Object[] {deviceId,deviceId});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	private static final class EventMasterMapper1 implements RowMapper<MediaDeviceData> {
		public String eventMasterSchema() {
			return "select a.id AS deviceId,a.client_id AS clientId,mc.code_value AS clientType,mc.id as clientTypeId FROM b_allocation a, m_client c,m_code_value mc WHERE a.client_id = c.id AND	 mc.id=c.category_type and serial_no=? union select a.id,a.client_id ,mc.code_value ,mc.id FROM b_owned_hardware a, m_client c,m_code_value mc WHERE a.client_id = c.id AND	 mc.id=c.category_type and serial_number=?"; 
  
		}
		@Override
		public MediaDeviceData mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			
			final Long deviceId = rs.getLong("deviceId");
			final Long clientId = rs.getLong("clientId");
			final String clientType = rs.getString("clientType");
			final Long clientTypeId = rs.getLong("clientTypeId");
			return new MediaDeviceData(deviceId,clientId,clientType,clientTypeId);
		}
	}
	
	@Override
	public List<PlanData> retrievePlanDetails(Long clientId) {
		PlanMasterMapper mapper = new PlanMasterMapper();
		String sql = mapper.planMasterSchema();
		return jdbcTemplate.query(sql,mapper, new Object[]{clientId});
	}
	
	private static final class PlanMasterMapper implements RowMapper<PlanData> {
		public String planMasterSchema() {
			return "select pm.id as id, pm.plan_code as planCode, pm.plan_description as planDescription from b_plan_master pm inner join b_orders o on o.plan_id = pm.id where o.client_id=? and pm.is_prepaid = 'Y' and pm.plan_status=1";/*"select pm.id as id, pm.plan_code as planCode, pm.plan_description as planDescription from b_plan_master pm where plan_status=1 and is_prepaid='Y'";*/ 
  
		}
		@Override
		public PlanData mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			
			final Long id = rs.getLong("id");
			final String planCode = rs.getString("planCode");
			final String planDescription = rs.getString("planDescription");
			return new PlanData(id, planCode, planDescription);
		}
	}
	
	
	
	
	@Override
	public List<PlanData> retrievePlanPostpaidDetails(Long clientId) {
		PlanMasterMapperPostPaid mapper = new PlanMasterMapperPostPaid();
		String sql = mapper.planMasterSchema();
		return jdbcTemplate.query(sql,mapper, new Object[]{clientId});	
	}
	
	private static final class PlanMasterMapperPostPaid implements RowMapper<PlanData> {
		public String planMasterSchema() {
			return "select pm.id as id, pm.plan_code as planCode, pm.plan_description as planDescription from b_plan_master pm inner join b_orders o on o.plan_id = pm.id where o.client_id=? and pm.is_prepaid = 'N' and pm.plan_status=1";/*"select pm.id as id, pm.plan_code as planCode, pm.plan_description as planDescription from b_plan_master pm where plan_status=1 and is_prepaid='Y'";*/ 
  
		}
		@Override
		public PlanData mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			
			final Long id = rs.getLong("id");
			final String planCode = rs.getString("planCode");
			final String planDescription = rs.getString("planDescription");
			return new PlanData(id, planCode, planDescription);
		}
	}
}

