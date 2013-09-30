package org.mifosplatform.billing.taxmapping.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.chargecode.data.ChargeCodeData;
import org.mifosplatform.billing.taxmapping.data.TaxMapData;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class TaxMapReadPlatformServiceImp implements TaxMapReadPlatformService{

	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;
	
	
	@Autowired
	public TaxMapReadPlatformServiceImp(final TenantAwareRoutingDataSource dataSource,PlatformSecurityContext context) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		 
	}


	@Override
	public List<TaxMapData> retriveTaxMapData(String chargeCode) {
		
		String sql = "SELECT tmr.id AS id,tmr.charge_code AS chargeCode,tmr.tax_code AS taxCode,tmr.start_date AS startDate,tmr.type," +
				"tmr.rate AS rate,tmr.tax_region_id as TaxRegionId," +
				" pr.priceregion_name as region  FROM b_tax_mapping_rate tmr,b_priceregion_master pr  where tmr.tax_region_id=pr.id and tmr.charge_code=?";
		TaxMapDataMapper rowMapper = new TaxMapDataMapper();
		return jdbcTemplate.query(sql, rowMapper,new Object[]{chargeCode});
	}
	
	@Override
	public TaxMapData retriveTaxMapDataForUpdate(final Long id) {
		
		String sql = "select tmr.id as id, tmr.charge_code as chargeCode, tmr.tax_code as taxCode, tmr.start_date as startDate, tmr.type, " +
				"tmr.rate as rate, tmr.tax_region_id as TaxRegionId,pr.priceregion_name as region" +
				" from b_tax_mapping_rate tmr,b_priceregion_master pr  where tmr.tax_region_id=pr.id and tmr.id = ?";
		TaxMapDataMapper rowMapper = new TaxMapDataMapper();
		return jdbcTemplate.queryForObject(sql, rowMapper,new Object[]{id});
	}



	@Override
	public List<ChargeCodeData> retriveTemplateData() {

		String sql = "select cc.charge_code as chargeCode, cc.charge_description as chargeDescription from b_charge_codes cc";
		TaxMapper rowMapper = new TaxMapper();
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	@Override
	public List<TaxMapData> retriveTaxMapTypeData(){
		String sql = "select mcv.id as id,mcv.code_value as type from m_code_value mcv,m_code mc where mcv.code_id=mc.id and mc.code_name='Type' order by mcv.id";
		TaxMapTypeMapper rowMapper = new TaxMapTypeMapper();
		return jdbcTemplate.query(sql,rowMapper);
	}
	
	private class TaxMapper implements RowMapper<ChargeCodeData>{
		@Override
		public ChargeCodeData mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			String chargeCode = rs.getString("chargeCode");
			String chargeDescription = rs.getString("chargeDescription");
			return new ChargeCodeData(chargeCode,chargeDescription);
		}
	}
	
	private class TaxMapDataMapper implements RowMapper<TaxMapData>{

		@Override
		public TaxMapData mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long id = rs.getLong("id");
			String chargeCode = rs.getString("chargeCode");
			String taxCode = rs.getString("taxCode");
			LocalDate startDate = JdbcSupport.getLocalDate(rs,"startDate");
			
			String type = rs.getString("type");
			BigDecimal rate = rs.getBigDecimal("rate"); 
			String region=rs.getString("region");
			Long TaxRegionId=rs.getLong("TaxRegionId");
			return new TaxMapData(id,chargeCode,taxCode,startDate,type,rate,region,TaxRegionId);
		}
		
	}
	
	private class TaxMapTypeMapper implements RowMapper<TaxMapData>{

		@Override
		public TaxMapData mapRow(ResultSet rs, int rowNum) throws SQLException {
			String type = rs.getString("type");
			Long id = rs.getLong("id");
			return new TaxMapData(id, type);
		}
		
	}
	
	

}
