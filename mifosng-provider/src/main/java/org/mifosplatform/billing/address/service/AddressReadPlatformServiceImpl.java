package org.mifosplatform.billing.address.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.mifosplatform.billing.address.data.AddressData;
import org.mifosplatform.billing.address.data.CountryDetails;
import org.mifosplatform.billing.address.domain.AddressEnum;
import org.mifosplatform.billing.order.data.AddressStatusEnumaration;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;



@Service
public class AddressReadPlatformServiceImpl implements AddressReadPlatformService {
	
	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;

	@Autowired
	public AddressReadPlatformServiceImpl(final PlatformSecurityContext context,
			final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	@Override
	public List<AddressData> retrieveAddressDetails(Long clientId) {

		try{
		context.authenticatedUser();
		AddressMapper mapper = new AddressMapper();

		String sql = "select " + mapper.schema()+" where is_deleted='n' and a.address_key='PRIMARY' and a.client_id="+clientId;

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});
		}catch (final EmptyResultDataAccessException e) {
			return null;
		}
	}

	private static final class AddressMapper implements RowMapper<AddressData> {

		public String schema() {
			return "a.address_id as id,a.client_id as clientId,a.address_key as addressKey,"
				+"a.address_no as addressNo,a.street as street,a.zip as zip,a.city as city,"
				+"a.state as state,a.country as country from b_client_address a";

		}

		@Override
		public AddressData mapRow(final ResultSet rs,
				@SuppressWarnings("unused") final int rowNum)
				throws SQLException {

			Long id = rs.getLong("id");
			Long clientId = rs.getLong("clientId");
			String addressKey = rs.getString("addressKey");
			String addressNo = rs.getString("addressNo");
			String street = rs.getString("street");
			String zip = rs.getString("zip");
			String city = rs.getString("city");
			String state = rs.getString("state");
			String country = rs.getString("country");
			EnumOptionData value=AddressStatusEnumaration.enumOptionData(AddressEnum.valueOf(addressKey));
			String addressKeyId=value.getId().toString();
			//String serviceDescription = rs.getString("service_description");
			return new AddressData(id,clientId,addressKeyId,addressNo,street,zip,city,state, country,addressKey);

		}
	}

	@Override
	public List<AddressData> retrieveSelectedAddressDetails(String selectedname) {
		
		AddressMapper mapper = new AddressMapper();
		String sql = "select " + mapper.schema()+" where a.city=? or a.state =? or a.country =? and a.is_deleted='n'";

		return this.jdbcTemplate.query(sql, mapper, new Object[]  { selectedname,selectedname,selectedname });
	}
	@Override
	public List<AddressData> retrieveAddressDetails() {

		context.authenticatedUser();
		AddressMapper mapper = new AddressMapper();

		String sql = "select " + mapper.schema()+" where is_deleted='n'";

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});

	}


	@Override
	public List<String> retrieveCountryDetails() {
		context.authenticatedUser();
		AddressMapper1 mapper = new AddressMapper1();

		String sql = "select " + mapper.sqlschema("country_name","country");

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});

	}

	private static final class AddressMapper1 implements RowMapper<String> {

		public String sqlschema(String placeholder,String tablename) {
			return placeholder+" as data from b_"+tablename;

		}

		@Override
		public String mapRow(final ResultSet rs,
				@SuppressWarnings("unused") final int rowNum)
				throws SQLException {

			
			String country = rs.getString("data");
			return country;
		

		}

	
	}

	@Override
	public List<String> retrieveStateDetails() {
		context.authenticatedUser();
		AddressMapper1 mapper = new AddressMapper1();

		String sql = "select " + mapper.sqlschema("state_name","state");

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});

	}


	@Override
	public List<String> retrieveCityDetails() {
		context.authenticatedUser();
		AddressMapper1 mapper = new AddressMapper1();

		String sql = "select " + mapper.sqlschema("city_name","city");

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});

	}


	@Override
	public List<AddressData> retrieveCityDetails(String selectedname) {
		context.authenticatedUser();
		DataMapper mapper = new DataMapper();

		String sql = "select " + mapper.schema(selectedname);

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});

	}

	private static final class DataMapper implements RowMapper<AddressData> {

		public String schema(String placeHolder) {
			return "id as id,"+placeHolder+"_name as data from b_"+placeHolder;

		}

		@Override
		public AddressData mapRow(final ResultSet rs,
				@SuppressWarnings("unused") final int rowNum)
				throws SQLException {

			Long id = rs.getLong("id");
			String data = rs.getString("data");
		
			//String serviceDescription = rs.getString("service_description");
			return new AddressData(id,data);

		}
	}

	@Override
	public List<EnumOptionData> addressType() {
		
		EnumOptionData primary = AddressStatusEnumaration.enumOptionData(AddressEnum.PRIMARY);
		EnumOptionData billing =AddressStatusEnumaration.enumOptionData(AddressEnum.BILLING);
		List<EnumOptionData> categotyType = Arrays.asList(primary,billing);
			return categotyType;
	}


	@Override
	public AddressData retrieveName(String Name) {
        try{
        	
		context.authenticatedUser();
		String sql;
		retrieveMapper mapper=new retrieveMapper();
	    sql = "SELECT  " + mapper.schema();
	
		return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] { Name });
	}catch (EmptyResultDataAccessException e) {
		return null;
	}
}
	private static final class retrieveMapper implements RowMapper<AddressData> {

		
		public String schema() {
			return " c.city_name as cityName,s.state_name as stateName,co.country_name as countryName" +
					"  FROM b_city c,b_state s,b_country co  WHERE c.parent_code = s.id and s.parent_code = co.id" +
					"  and c.city_name =?";

		}

		@Override
		public AddressData mapRow(final ResultSet rs, final int rowNum)	throws SQLException {
			String city = rs.getString("cityName");
			String state = rs.getString("stateName");
			String country=rs.getString("countryName");
			return new AddressData(city,state,country);
		}
	}

	@Override
	public List<CountryDetails> retrieveCountries() {
		try{
			context.authenticatedUser();
			CountryMapper mapper = new CountryMapper();

			String sql = "select " + mapper.schema();

			return this.jdbcTemplate.query(sql, mapper, new Object[] {});
			}catch (final EmptyResultDataAccessException e) {
				return null;
			}
		}

		private static final class CountryMapper implements RowMapper<CountryDetails> {

			public String schema() {
				return " c.id as id,c.country_name as countryName FROM b_country c";

			}

			@Override
			public CountryDetails mapRow(final ResultSet rs,
					@SuppressWarnings("unused") final int rowNum)
					throws SQLException {

				Long id = rs.getLong("id");
				String countryName = rs.getString("countryName");
			
				return new CountryDetails(id,countryName);

			}
		}

		@Override
		public List<AddressData> retrieveClientAddressDetails(Long clientId) {
			try{
				context.authenticatedUser();
				AddressMapper mapper = new AddressMapper();

				String sql = "select " + mapper.schema()+" where a.is_deleted='n' and a.client_id=?";

				return this.jdbcTemplate.query(sql, mapper, new Object[] {clientId});
				}catch (final EmptyResultDataAccessException e) {
					return null;
				}
			}


}


