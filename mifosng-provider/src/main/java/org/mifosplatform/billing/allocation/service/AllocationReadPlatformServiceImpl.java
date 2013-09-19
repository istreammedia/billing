package org.mifosplatform.billing.allocation.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.onetimesale.data.AllocationDetailsData;
import org.mifosplatform.billing.order.domain.OrderRepository;
import org.mifosplatform.billing.order.service.OrderReadPlatformServiceImpl;
import org.mifosplatform.billing.pricing.service.PriceReadPlatformService;
import org.mifosplatform.infrastructure.core.domain.MifosPlatformTenant;
import org.mifosplatform.infrastructure.core.service.DataSourcePerTenantService;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.core.service.ThreadLocalContextUtil;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.infrastructure.security.service.TenantDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


@Service
public class AllocationReadPlatformServiceImpl implements AllocationReadPlatformService {
	
	
	    private final JdbcTemplate jdbcTemplate;
	    private final PlatformSecurityContext context;
	    
	    @Autowired
	    public AllocationReadPlatformServiceImpl(final PlatformSecurityContext context, final TenantAwareRoutingDataSource dataSource)
	    {
	        this.context = context;
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	       
	     

	    }

	
	@Override
	public AllocationDetailsData getTheHardwareItemDetails(Long orderId) {
		try {
			
			final ClientOrderMapper mapper = new ClientOrderMapper();
			final String sql = "select " + mapper.clientOrderLookupSchema();
			return jdbcTemplate.queryForObject(sql, mapper, new Object[] {  orderId });
			} catch (EmptyResultDataAccessException e) {
			return null;
			}

			}

			private static final class ClientOrderMapper implements RowMapper<AllocationDetailsData> {

			public String clientOrderLookupSchema() {
			return " a.id as id,a.order_id as orderId,a.hw_serial_no as serialNum, a.client_id as clientId from b_association a where a.order_id=?";
			}

			@Override
			public AllocationDetailsData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

			final Long id = rs.getLong("id");
			final Long orderId = rs.getLong("orderId");
			final String serialNum = rs.getString("serialNum");
			final Long clientId = rs.getLong("clientId");	
		
			
			return new AllocationDetailsData(id,orderId,serialNum,clientId);
			}
			}

			@Override
			public List<AllocationDetailsData> retrieveHardWareDetailsByItemCode(Long clientId, String itemCode) 
			{
				try {
					  
					final HardwareMapper mapper = new HardwareMapper();
					final String sql = "select " + mapper.schema();
					return jdbcTemplate.query(sql, mapper, new Object[] {  clientId,itemCode });
					} catch (EmptyResultDataAccessException e) {
					return null;
					}

					}

					private static final class HardwareMapper implements RowMapper<AllocationDetailsData> {

					public String schema() {
					return " a.id as id,a.serial_no as serialNo,i.item_description as itemDescription  FROM b_allocation a,b_item_master i" +
							" WHERE client_id =? and a.item_master_id=i.id and i.item_code =?";
					}

					@Override
					public AllocationDetailsData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

					final Long id = rs.getLong("id");
					
					final String serialNum = rs.getString("serialNo");
					final String itemDescription = rs.getString("itemDescription");	
				
					
					return new AllocationDetailsData(id, itemDescription, serialNum, null);
					}
			}

			
		
			

	
}
