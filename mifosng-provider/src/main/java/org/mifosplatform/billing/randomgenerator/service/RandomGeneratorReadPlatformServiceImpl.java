package org.mifosplatform.billing.randomgenerator.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.address.domain.AddressEnum;
import org.mifosplatform.billing.order.data.AddressStatusEnumaration;
import org.mifosplatform.billing.randomgenerator.data.RandomGeneratorData;
import org.mifosplatform.billing.randomgenerator.domain.PinCategory;
import org.mifosplatform.billing.randomgenerator.domain.PinType;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class RandomGeneratorReadPlatformServiceImpl implements RandomGeneratorReadPlatformService {

	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;

	@Autowired
	public  RandomGeneratorReadPlatformServiceImpl(final PlatformSecurityContext context, final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	@Override
	public String retrieveIndividualPin(String pinNo) {
		  try{
	        	
				context.authenticatedUser();
				String sql;
				retrieveMapper mapper=new retrieveMapper();
			    sql = "SELECT  " + mapper.schema();
			
				return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] { pinNo });
			}catch (EmptyResultDataAccessException e) {
				return null;
			}
	}
	
private static final class retrieveMapper implements RowMapper<String> {

		
		public String schema() {
			return " d.pin_no as pinNo from b_pin_details d where d.pin_no =?";

		}

		@Override
		public String mapRow(final ResultSet rs, final int rowNum)	throws SQLException {
			String pinNo = rs.getString("pinNo");
			
			return pinNo;
		}
	}

   @Override
   public List<EnumOptionData> pinCategory() {
	
		EnumOptionData Numeric = RandomGeneratorEnumeration.enumOptionData(PinCategory.NUMERIC);
		EnumOptionData Alpha =RandomGeneratorEnumeration.enumOptionData(PinCategory.ALPHA);
		EnumOptionData AlphaNumeric = RandomGeneratorEnumeration.enumOptionData(PinCategory.ALPHANUMERIC);
		List<EnumOptionData> categotyType = Arrays.asList(Numeric,Alpha,AlphaNumeric);
		return categotyType;
}
   @Override
   public List<EnumOptionData> pinType() {
	
		EnumOptionData Value = RandomGeneratorEnumerationType.enumOptionData(PinType.VALUE);
		EnumOptionData Duration =RandomGeneratorEnumerationType.enumOptionData(PinType.DURATION);
		//EnumOptionData AlphaNumeric = RandomGeneratorEnumerationType.enumOptionData(PinType.PRODUCTION);
		List<EnumOptionData> categotyType = Arrays.asList(Value,Duration);
		return categotyType;
}


	@Override
	public List<RandomGeneratorData> getAllData() {
				try{
			    	
					context.authenticatedUser();
					String sql;
					retrieveRandomMapper mapper=new retrieveRandomMapper();
				    sql = "SELECT  " + mapper.schema();
				
					return this.jdbcTemplate.query(sql, mapper, new Object[] {});
				}catch (EmptyResultDataAccessException e) {
					return null;
				}
			}
			
	private static final class retrieveRandomMapper implements RowMapper<RandomGeneratorData> {
			
			
			public String schema() {
				return "m.id as id, m.batch_name as batchName,m.batch_description as batchDescription,m.length as length," +
						"m.begin_with as beginWith,m.pin_category as pinCategory,m.quantity as quantity," +
						"m.serial_no as serialNo,m.pin_type as pinType,m.pin_value as pinValue,m.expiry_date as expiryDate " +
						"from b_pin_master m;";
			
			}
			
			@Override
			public RandomGeneratorData mapRow(final ResultSet rs, final int rowNum)	throws SQLException {
				
				 Long id=rs.getLong("id");
				 String batchName=rs.getString("batchName");
				 String batchDescription=rs.getString("batchDescription");
				 Long length=rs.getLong("length");
				 String pinCategory=rs.getString("pinCategory");
				 String pinType=rs.getString("pinType");
				 Long quantity=rs.getLong("quantity");
				 String serial=rs.getString("serialNo");
				 Date expiryDate=rs.getDate("expiryDate");
				 String beginWith=rs.getString("beginWith");
				 String pinValue=rs.getString("pinValue");
				
				return new RandomGeneratorData(batchName,batchDescription,length,pinCategory,pinType,quantity,serial,expiryDate,beginWith,pinValue,id);
			}
		}
	
	
	@Override
	public Long retrieveMaxNo(Long minNo,Long maxNo) {
		  try{
				context.authenticatedUser();
				String sql;
				Mapper mapper=new Mapper();
			    sql = "SELECT  " + mapper.schema();
			
				return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] { minNo,maxNo });
			}catch (EmptyResultDataAccessException e) {
				return null;
			}
	}
	
private static final class Mapper implements RowMapper<Long> {

		
		public String schema() {
			return "max(m.serial_no) as serialNo from b_pin_details m where serial_no BETWEEN ? AND ?";

		}

		@Override
		public Long mapRow(final ResultSet rs, final int rowNum)	throws SQLException {
			Long serialNo = rs.getLong("serialNo");
			
			return serialNo;
		}
	}

}







