package org.mifosplatform.billing.region.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.address.data.StateDetails;
import org.mifosplatform.billing.region.data.RegionData;
import org.mifosplatform.billing.region.data.RegionDetailsData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


@Service
public class RegionReadPlatformServiceImpl implements RegionReadPlatformService{
	
	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;
	
	
@Autowired
public RegionReadPlatformServiceImpl(final TenantAwareRoutingDataSource dataSource,final PlatformSecurityContext context){
	
	this.jdbcTemplate=new JdbcTemplate(dataSource);
	this.context=context;
}
	
	@Override
	public List<StateDetails> getAvailableStates(Long countryId) {
		try{
			context.authenticatedUser();
			StatesMapper mapper = new StatesMapper();

			String sql = "select " + mapper.schema();

			return this.jdbcTemplate.query(sql, mapper, new Object[] {countryId});
			}catch (final EmptyResultDataAccessException e) {
				return null;
			}
	}
		private static final class StatesMapper implements RowMapper<StateDetails> {

			public String schema() {
				return " s.id as id,s.state_name as stateName FROM b_state s, b_country c WHERE    NOT EXISTS (SELECT * FROM   b_priceregion_detail pd" +
						"  WHERE  pd.state_id  =s.id and pd.is_deleted='N' ) and s.parent_code=c.id and c.id =?";

			}

			@Override
			public StateDetails mapRow(final ResultSet rs,
					@SuppressWarnings("unused") final int rowNum)
					throws SQLException {

				Long id=rs.getLong("id");
				String stateName = rs.getString("stateName");
				
                        return new StateDetails(id,stateName);
			}
		}
		
		private static final class RegionMapper implements RowMapper<RegionData> {

			public String schema() {
				return " p.id AS id,p.priceregion_code AS regionCode,p.priceregion_name AS regionName  FROM b_priceregion_master p where p.is_deleted ='N' ";

			}

			@Override
			public RegionData mapRow(final ResultSet rs,
					@SuppressWarnings("unused") final int rowNum)
					throws SQLException {

				
				Long id= rs.getLong("id");
				String regionCode = rs.getString("regionCode");
				String regionName = rs.getString("regionName");
				
                        return new RegionData(null,regionCode,regionName,null,id);
			}
		}
		
		@Override
		public List<RegionData> getRegionDetails() {
			try{
				context.authenticatedUser();
				RegionMapper mapper = new RegionMapper();

				String sql = "select " + mapper.schema();

				return this.jdbcTemplate.query(sql, mapper, new Object[] {});
				}catch (final EmptyResultDataAccessException e) {
					return null;
				}
}

		@Override
		public RegionData getSingleRegionDetails(Long regionId) {
			try{
				context.authenticatedUser();
				RegionMapper mapper = new RegionMapper();

				String sql = "select " + mapper.schema()+" and p.id=?";

				return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] {regionId});
				}catch (final EmptyResultDataAccessException e) {
					return null;
				}
		}


		@Override
		public List<RegionDetailsData> getRegionDetailsData(Long regionId) {
			try{
				context.authenticatedUser();
				RegionDetailMapper mapper = new RegionDetailMapper();

				String sql = "select " + mapper.schema()+" left join b_state s on pd.state_id=s.id WHERE pd.priceregion_id =? ";

				return this.jdbcTemplate.query(sql, mapper, new Object[] {regionId});
				}catch (final EmptyResultDataAccessException e) {
					return null;
				}
		}	
			
			private static final class RegionDetailMapper implements RowMapper<RegionDetailsData> {

				public String schema() {
					return " pd.id AS id,pd.priceregion_id AS regionId,pd.country_id AS countryId,pd.state_id AS stateId,s.state_name AS stateName" +
							" FROM b_priceregion_detail pd  ";

				}

				@Override
				public RegionDetailsData mapRow(final ResultSet rs,
						@SuppressWarnings("unused") final int rowNum)
						throws SQLException {

					Long id=rs.getLong("id");
					Long regionId=rs.getLong("regionId");
					Long stateId=rs.getLong("stateId");
					Long countryId=rs.getLong("countryId");
					String stateName=rs.getString("stateName");
					
	                        return new RegionDetailsData(stateId,regionId,stateId,countryId,stateName);
				}
			}

			@Override
			public List<RegionDetailsData> getCountryRegionDetails(Long countryId,Long stateId) {
				
				try{
					context.authenticatedUser();
					RegionDetailMapper mapper = new RegionDetailMapper();

					String sql = "select " + mapper.schema()+" left join b_state s on pd.state_id=s.id where pd.country_id =? and pd.state_id =? and pd.is_deleted = 'N'";

					return this.jdbcTemplate.query(sql, mapper, new Object[] {countryId,stateId});
					}catch (final EmptyResultDataAccessException e) {
						return null;
					}
			}	
}
