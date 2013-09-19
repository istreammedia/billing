package org.mifosplatform.billing.currency.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.mifosplatform.billing.currency.data.CountryCurrencyData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


@Service
public class CountryCurrencyReadPlatformServiceImpl implements CountryCurrencyReadPlatformService {

	private final PlatformSecurityContext context;
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public CountryCurrencyReadPlatformServiceImpl(final TenantAwareRoutingDataSource dataSource,final PlatformSecurityContext context){
		this.context=context;
		this.jdbcTemplate=new JdbcTemplate(dataSource);
		
	}


	@Override
	public List<CountryCurrencyData> getTheCountryCurrencyDetaiils(String country) {
		
		try{
		
		    CurrencyMapper mapper=new CurrencyMapper();
           String sql="select "+mapper.schema()+" WHERE country = ? and  c.is_deleted='N' ";
		return this.jdbcTemplate.query(sql,mapper,new Object[] {country});
		}catch(EmptyResultDataAccessException accessException){
			return null;
		}
	}
	
	
	private static final class CurrencyMapper implements RowMapper<CountryCurrencyData> {

		public String schema() {
			return " c.id as id,c.country as country,c.currency as currency,c.status as status FROM b_country_currency c ";

		}

		@Override
		public CountryCurrencyData mapRow(final ResultSet rs,
				@SuppressWarnings("unused") final int rowNum)
				throws SQLException {

			Long id = rs.getLong("id");
			String country = rs.getString("country");
			String currency = rs.getString("currency");
			String status = rs.getString("status");
			return new CountryCurrencyData(id,country,currency,status);

		}
	}


	@Override
	public Collection<CountryCurrencyData> retrieveCurrencyConfigurationDetails() {
		
		try{
			CurrencyMapper mapper=new CurrencyMapper();
            
	           String sql="select "+mapper.schema()+" WHERE  c.is_deleted='N' ";
			return this.jdbcTemplate.query(sql,mapper,new Object[] {});
		}catch(EmptyResultDataAccessException exception){
			return null;
		}
	}


	@Override
	public CountryCurrencyData retrieveCurrencyConfigurationDetails(
			Long id) {
		try{
			CurrencyMapper mapper=new CurrencyMapper();
            
	           String sql="select "+mapper.schema()+" WHERE  c.is_deleted='N' and c.id=?";
			return this.jdbcTemplate.queryForObject(sql,mapper,new Object[] { id });
		}catch(EmptyResultDataAccessException exception){
			return null;
		}
	}

}
