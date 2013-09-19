package org.mifosplatform.billing.association.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.association.data.HardwareAssociationData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


@Service
public class HardwareAssociationReadplatformServiceImpl implements HardwareAssociationReadplatformService{
	
	
	 private final JdbcTemplate jdbcTemplate;
	 private final PlatformSecurityContext context;
	  
	    @Autowired
	    public HardwareAssociationReadplatformServiceImpl(final PlatformSecurityContext context, 
	    		final TenantAwareRoutingDataSource dataSource)
	    {
	        this.context = context;
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	    }

		@Override
		public List<HardwareAssociationData> retrieveClientHardwareDetails(Long clientId)
		{

              try
              {

            	  HarderwareMapper mapper = new HarderwareMapper();

			String sql = "select " + mapper.schema();
			return this.jdbcTemplate.query(sql, mapper, new Object[] {clientId});

		}catch(EmptyResultDataAccessException accessException){
			return null;
		}
		}
		private static final class HarderwareMapper implements RowMapper<HardwareAssociationData> {

			public String schema() {
				return "  a.id AS id, a.serial_no AS serialNo  FROM b_allocation a  WHERE    NOT EXISTS (SELECT * FROM  b_association s" +
						" WHERE  s.hw_serial_no=a.serial_no) and a.client_id=?";

			}

			@Override
			public HardwareAssociationData mapRow(final ResultSet rs,
					@SuppressWarnings("unused") final int rowNum)
					throws SQLException {
				Long id = rs.getLong("id");
				String serialNo = rs.getString("serialNo");
				
				HardwareAssociationData associationData=new HardwareAssociationData(id,serialNo,null,null);

				return associationData; 

			}

		}
		@Override
		public List<HardwareAssociationData> retrieveClientUnallocatePlanDetails(Long clientId) {
            try
            {

          	  PlanMapper mapper = new PlanMapper();

			String sql = "select " + mapper.schema();
			return this.jdbcTemplate.query(sql, mapper, new Object[] {clientId});

		}catch(EmptyResultDataAccessException accessException){
			return null;
		}
		}
		private static final class PlanMapper implements RowMapper<HardwareAssociationData> {

			public String schema() {
				return " o.id AS id, o.plan_id as planId FROM  b_orders o    where  NOT EXISTS (SELECT * FROM b_association a	 WHERE  a.order_id =o.id and o.is_deleted='N' )" +
						" and   o.client_id =? group by o.id ";

			}

			@Override
			public HardwareAssociationData mapRow(final ResultSet rs,
					@SuppressWarnings("unused") final int rowNum)
					throws SQLException {
				Long id = rs.getLong("id");
				Long planId = rs.getLong("planId");
				Long orderId=rs.getLong("id");
				
				HardwareAssociationData associationData=new HardwareAssociationData(id,null,planId,orderId);

				return associationData; 

			}
		}
}
