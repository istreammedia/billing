package org.mifosplatform.billing.planservice.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.mifosplatform.billing.planservice.data.PlanServiceData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class PlanServiceReadPlatformServiceImpl implements PlanServiceReadPlatformService{
	
	private final JdbcTemplate jdbcTemplate;
	
	private final PlatformSecurityContext context;
	
	@Autowired
	public PlanServiceReadPlatformServiceImpl(final PlatformSecurityContext context,final TenantAwareRoutingDataSource dataSource){
		this.context=context;
		this.jdbcTemplate=new JdbcTemplate(dataSource);
		
	}

	@Override
	public Collection<PlanServiceData> retrieveClientPlanService(Long clientId,
			String serviceType) {

		planServiceMapper mapper = new planServiceMapper();
		String sql = "select " + mapper.schema();

		return this.jdbcTemplate.query(sql, mapper, new Object[] {clientId,serviceType});

	}

	protected static final class planServiceMapper implements RowMapper<PlanServiceData> {

		@Override
		public PlanServiceData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum)
				throws SQLException {
			Long serviceId=rs.getLong("serviceId");
			Long clientId=rs.getLong("clientId");
			String serviceName = rs.getString("serviceName");
			String logo=rs.getString("logo");
			String url=rs.getString("serviceIdentification");


			return new PlanServiceData(serviceId,clientId,serviceName,logo,url);

		}


		public String schema() {
			return " s.id as serviceId,o.client_id as clientId,s.service_code as serviceName,sd.image as logo, sd.service_identification as serviceIdentification" +
					" FROM b_orders o,b_plan_detail p,b_service s,b_prov_service_details sd  WHERE o.client_id = ? AND p.plan_id = o.plan_id " +
					"AND s.service_code = p.service_code AND s.service_type =? AND s.id = sd.service_id";
		}
	}
}
