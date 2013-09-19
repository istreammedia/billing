/**
 * 
 */
package org.mifosplatform.billing.eventpricing.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.eventpricing.data.ClientTypeData;
import org.mifosplatform.billing.eventpricing.data.EventPricingData;
import org.mifosplatform.billing.eventpricing.domain.EventPricing;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

/**
 * {@link Service} Class for {@link EventPricing} Read Service
 * implements {@link EventPricingWritePlatformService}
 * 
 * @author pavani
 *
 */
@Service
public class EventPricingReadPlatformServiceImpl implements 
		EventPricingReadPlatformService {

	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public EventPricingReadPlatformServiceImpl(final TenantAwareRoutingDataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	/* (non-Javadoc)
	 * @see org.mifosplatform.billing.eventpricing.service.EventPricingService#priceData()
	 */
	@Override
	public List<EventPricingData> retrieventPriceData(Long eventId) {
		try {
			final EventPricingMapper eventPricingMapper = new EventPricingMapper();
			final String sql = "SELECT " + eventPricingMapper.eventPricingSchema() + " where event_id = ? and is_deleted = 'n'";
			return jdbcTemplate.query(sql, eventPricingMapper,new Object[] {eventId});
		} catch (EmptyResultDataAccessException e) {
		return null;
		}
	}
	
	@Override
	public EventPricingData retrieventPriceDetails(Long eventPriceId) {
		try {
			final EventPricingMapper eventPricingMapper = new EventPricingMapper();
			final String sql = "SELECT " + eventPricingMapper.eventPricingSchema() + " where ep.id = ? and is_deleted = 'n'";
			return jdbcTemplate.queryForObject(sql, eventPricingMapper,new Object[] {eventPriceId});
		} catch (EmptyResultDataAccessException e) {
		return null;
		}
	}
	
	private static final class EventPricingMapper implements RowMapper<EventPricingData>  {
		
		public String eventPricingSchema() {
			return " ep.id AS id,em.id AS eventId,em.event_name AS eventName,ep.format_type AS formatType,ep.opt_type AS optType, ep.client_typeid as clientTypeId,"
				  +" d.discount_description AS discount,ep.price AS price,mc.code_value as clientType " +
				   " from b_event_pricing ep " +
				   " inner join m_code_value mc on  ep.client_typeid=mc.id"
				  +"  inner join b_discount_master d ON d.id = ep.discount_id " +
				   " inner join b_event_master em ON em.id = ep.event_id";
		}
		@Override
		public EventPricingData mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			
			final Long id = rs.getLong("id");
			final Long eventId = rs.getLong("eventId");
			final String eventName  = rs.getString("eventName");
			final String formatType = rs.getString("formatType");
			final String optType = rs.getString("optType");
			final Long clientTypeId = rs.getLong("clientTypeId");
			final String discount = rs.getString("discount");
			final BigDecimal price = rs.getBigDecimal("price");
			final String clientType = rs.getString("clientType");
			
			return new EventPricingData(id, eventName, formatType, optType, clientTypeId, discount, price,eventId,clientType);
		}
		
	}

	@Override
	public List<ClientTypeData> clientType() {
		try {
			final ClientTypeMapper clientTypeMapper = new ClientTypeMapper();
			final String sql = "SELECT " + clientTypeMapper.clientTypeSchema() + " where mc.code_name='Client Category'";
			return jdbcTemplate.query(sql, clientTypeMapper,new Object[] {});
		} catch (EmptyResultDataAccessException e) {
		return null;
		}
	}
	
private static final class ClientTypeMapper implements RowMapper<ClientTypeData>  {
		
		public String clientTypeSchema() {
			return " mcv.id as id , mcv.code_value as type from m_code_value mcv "
					+ " inner join m_code mc on mc.id = mcv.code_id " ;
		}
		@Override
		public ClientTypeData mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			
			final Long id = rs.getLong("id");
			final String type= rs.getString("type");
			return new ClientTypeData(id,type);
		}
		
	}
	

}
