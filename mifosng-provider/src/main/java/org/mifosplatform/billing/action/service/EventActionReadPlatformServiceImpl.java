package org.mifosplatform.billing.action.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mifosplatform.billing.action.data.VolumeDetailsData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


@Service
public class EventActionReadPlatformServiceImpl implements EventActionReadPlatformService{
	

	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;

	@Autowired
	public EventActionReadPlatformServiceImpl(final PlatformSecurityContext context,
			final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	@Override
	public VolumeDetailsData retrieveVolumeDetails(Long planId) {
		
		try{
	//	context.authenticatedUser();
		PlanMapper mapper = new PlanMapper();

		String sql = "select " + mapper.schema();

		return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] { planId });
	}catch(EmptyResultDataAccessException exception){
		return null;
	}
	}
	private static final class PlanMapper implements RowMapper<VolumeDetailsData> {

		public String schema() {
			return "v.id as id,v.plan_id as planId, v.volume_type as volumeType,v.units as units,v.units_type as unitType" +
					" FROM b_volume_details v WHERE v.plan_id =?";

		}

		@Override
		public VolumeDetailsData mapRow(final ResultSet rs,
				@SuppressWarnings("unused") final int rowNum)
				throws SQLException {

			Long id = rs.getLong("id");
			Long planId = rs.getLong("planId");
			String volumeType = rs.getString("volumeType");
			Long units = rs.getLong("units");
			String unitType = rs.getString("unitType");
			return new VolumeDetailsData(id,planId,volumeType,units,unitType);

		}
	}

}
