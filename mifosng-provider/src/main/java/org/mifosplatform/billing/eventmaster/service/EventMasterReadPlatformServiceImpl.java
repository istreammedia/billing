/**
 * 
 */
package org.mifosplatform.billing.eventmaster.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.eventmaster.data.EventDetailsData;
import org.mifosplatform.billing.eventmaster.data.EventMasterData;
import org.mifosplatform.billing.eventmaster.domain.EventMaster;
import org.mifosplatform.billing.eventmaster.domain.OptType;
import org.mifosplatform.billing.eventmaster.domain.OptTypeEnum;
import org.mifosplatform.billing.order.data.OrderStatusEnumaration;
import org.mifosplatform.billing.plan.domain.StatusTypeEnum;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


/**{@link Service} Class for {@link EventMaster} Read Service
 *implements {@link EventMasterReadPlatformService}
 * @author pavani
 *
 */
@Service
public class EventMasterReadPlatformServiceImpl implements
		EventMasterReadPlatformService {
	
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public EventMasterReadPlatformServiceImpl (final TenantAwareRoutingDataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<EnumOptionData> retrieveOptTypeData() {
		EnumOptionData rent = OptTypeEnum.optType(OptType.RENT);
		EnumOptionData own = OptTypeEnum.optType(OptType.OWN);
		List<EnumOptionData> optType = Arrays.asList(rent,own);
		return optType;
	}

	@Override
	public List<EnumOptionData> retrieveNewStatus() {
		EnumOptionData active = OrderStatusEnumaration
				.OrderStatusType(StatusTypeEnum.ACTIVE);
		EnumOptionData inactive = OrderStatusEnumaration
				.OrderStatusType(StatusTypeEnum.INACTIVE);
		List<EnumOptionData> categotyType = Arrays.asList(active, inactive);
		return categotyType;

	}

	@Override
	public List<EventMasterData> retrieveEventMasterData() {
		try {
			final EventMasterMapper eventMasterMapper = new EventMasterMapper();
			
			final String sql = "SELECT " + eventMasterMapper.eventMasterSchema()+" where evnt.event_end_date > now() or evnt.event_end_date is null";
			return jdbcTemplate.query(sql, eventMasterMapper,new Object[] {});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	private static final class EventMasterMapper implements RowMapper<EventMasterData> {
		public String eventMasterSchema() {
			return " evnt.id as id, evnt.event_name as eventName, evnt.event_description as eventDescription, evnt.status as status,evnt.created_date as createdDate "
						+ " from b_event_master evnt ";
		}
		@Override
		public EventMasterData mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			
			final Long id = rs.getLong("id");
			final String eventName = rs.getString("eventName");
			final String eventDescription = rs.getString("eventDescription");
			final Integer statusId = rs.getInt("status");
			String status=OrderStatusEnumaration.OrderStatusType(statusId).getValue();
			LocalDate createdDate = JdbcSupport.getLocalDate(rs, "createdDate");
			return new EventMasterData(id, eventName, eventDescription,status,null,createdDate);
		}
	}

	@Override
	public EventMasterData retrieveEventMasterDetails(Integer eventId){
		String sql = " select evnt.id as id, evnt.event_name as eventName, evnt.event_description as eventDescription, evnt.status as status,evnt.event_start_date as eventStartDate, " +
						" evnt.event_end_date as eventEndDate, evnt.event_validity as eventValidity, evnt.allow_cancellation as allowCancellation, charge_code as chargeCode "
						+ " from b_event_master evnt "
						+ "where evnt.id='"+eventId+"'";
		RowMapper<EventMasterData> rm = new EventMapper();
		return this.jdbcTemplate.queryForObject(sql, rm,new Object[]{});
	}
	
	private static final class EventMapper implements RowMapper<EventMasterData> {
		@Override
		public EventMasterData mapRow(final ResultSet rs,final int rowNum) throws SQLException {
			Long id = rs.getLong("id");
			String eventName = rs.getString("eventName");
			String eventDescription = rs.getString("eventDescription");
			LocalDate eventStartDate = JdbcSupport.getLocalDate(rs, "eventStartDate");
			LocalDate eventEndDate = JdbcSupport.getLocalDate(rs, "eventEndDate");
			LocalDate eventValidity = JdbcSupport.getLocalDate(rs, "eventValidity");
			String status = rs.getString("status");
			int allowCancellation = JdbcSupport.getInteger(rs, "allowCancellation");
			String chargeData = rs.getString("chargeCode"); 
			return new EventMasterData(id, eventName, eventDescription, status, null, eventStartDate, eventEndDate, eventValidity,allowCancellation,chargeData);
		}
	}
	
	@Override
	public List<EventDetailsData> retrieveEventDetailsData(Integer eventId) {
		String sql = "Select ed.id as id, ed.event_id as eventId,ed.media_id as mediaId,m.title as title "
					  +" from b_event_detail ed,b_media_asset m where ed.media_id=m.id and event_id = ?";
		
		RowMapper<EventDetailsData> rm = new EventDetailsMapper();
		
		return this.jdbcTemplate.query(sql, rm,new Object[]{eventId});
	}
	
	@Override
	public EventDetailsData retrieveEventDetails(Integer eventId) {
		String sql = "Select id as id, event_id as eventId,media_id as mediaId," 
				 + " event_start_date as eventStartDate, event_end_date as eventEndDate " 
				 + " from b_event_detail where event_id = ?";
			RowMapper<EventDetailsData> rm = new EventDetailsMapper();
			
			return this.jdbcTemplate.queryForObject(sql, rm,new Object[] {eventId});
 	}
	private static final class EventDetailsMapper implements RowMapper<EventDetailsData> {
		@Override
		public EventDetailsData mapRow(final ResultSet rs,final int rowNum) throws SQLException {
			Long id = rs.getLong("id");
			Integer eventId = rs.getInt("eventId");
			Long mediaId = rs.getLong("mediaId");
			String mediaTitle = rs.getString("title");
			return new EventDetailsData(id, eventId, mediaId, mediaTitle);
		}
	}
}	

	
