package org.mifosplatform.billing.chargecode.service;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.chargecode.data.BillFrequencyCodeData;
import org.mifosplatform.billing.chargecode.data.ChargeCodeData;
import org.mifosplatform.billing.chargecode.data.ChargeTypeData;
import org.mifosplatform.billing.chargecode.data.DurationTypeData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class ChargeCodeReadPlatformServiceImpl implements
		ChargeCodeReadPlatformService {
	
	private final JdbcTemplate jdbcTemplate;  

	@Autowired
	public ChargeCodeReadPlatformServiceImpl(final TenantAwareRoutingDataSource tenantAwareRoutingDataSource){
		this.jdbcTemplate = new JdbcTemplate(tenantAwareRoutingDataSource);
	}
	
	
	private static final class ChargeCodeMapper implements RowMapper<ChargeCodeData> {
		public String schema() {
			return "id as id, charge_code as chargeCode, charge_description as chargeDescription, charge_type as chargeType,charge_duration as chargeDuration, duration_type as durationType, tax_inclusive as taxInclusive,billfrequency_code as billFrequencyCode"
					+ " from b_charge_codes";
		}
		
		@Override
		public ChargeCodeData mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			Long id = rs.getLong("id");
			String chargeCode = rs.getString("chargeCode");
			String chargeDescription = rs.getString("chargeDescription");
			String chargeType = rs.getString("chargeType");
			Integer chargeDuration = rs.getInt("chargeDuration");
			String durationType = rs.getString("durationType");
			Integer taxInclusive = rs.getInt("taxInclusive");
			 
			String billFrequencyCode = rs.getString("billFrequencyCode");
			return new ChargeCodeData(id,chargeCode, chargeDescription, chargeType, chargeDuration, durationType, taxInclusive, billFrequencyCode);
		}
	}
	
	
	private class ChargeTypeDataMapper implements RowMapper<ChargeTypeData>{
		public ChargeTypeData mapRow(final ResultSet rs , final int rowNum)throws SQLException{
			String chargeType = rs.getString("chargeType");
			Long id = rs.getLong("id");
			return new ChargeTypeData(chargeType,id);
		}
	}
	
	private class DurationTypeDataMapper implements RowMapper<DurationTypeData>{
		
		public DurationTypeData mapRow(final ResultSet rs, final int rowNum)throws SQLException{
			String durationType = rs.getString("durationType");
			Long id = rs.getLong("id");
			return new DurationTypeData(durationType,id);
		}
	}
	
	private class BillFrequencyRowMapper implements RowMapper<BillFrequencyCodeData>{
		public BillFrequencyCodeData mapRow(final ResultSet rs,int rowNum)throws SQLException{
			String billFrequency = rs.getString("billFrequency");
			Long id = rs.getLong("id");
			return new BillFrequencyCodeData(billFrequency,id);
		}
	}
	
	private class ChargeCodeDataByIdRowMapper implements RowMapper<ChargeCodeData>{
		
		@Override
		public ChargeCodeData mapRow(ResultSet rs, int rowNum)throws SQLException{
			String chargeCode = rs.getString("chargeCode");
			String chargeDescription = rs.getString("chargeDescription");
			String chargeType = rs.getString("chargeType");
			Integer chargeDuration = rs.getInt("chargeDuration");
			String durationType = rs.getString("durationType");
			Integer taxInclusive = rs.getInt("taxInclusive");
			String billFrequencyCode = rs.getString("billFrequencyCode");
			return new ChargeCodeData(null, chargeCode, chargeDescription, chargeType, chargeDuration, durationType, taxInclusive, billFrequencyCode);
		}
	}
	
	public List<ChargeCodeData> getChargeCode() {
		ChargeCodeMapper mapper = new ChargeCodeMapper();
		String sql = "Select " + mapper.schema();
		return this.jdbcTemplate.query(sql, mapper,new Object[] {});
	}
	
	public List<ChargeTypeData> getChargeType(){
		String sql = "select mcv.id as id,mcv.code_value as chargeType from m_code_value mcv,m_code mc where mcv.code_id=mc.id and mc.code_name='Charge Type' order by mcv.id";
		ChargeTypeDataMapper rowMapper = new ChargeTypeDataMapper();
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	public List<DurationTypeData> getDurationType(){
		String sql = "select mcv.id as id,mcv.code_value as durationType from m_code_value mcv,m_code mc where mcv.code_id=mc.id and mc.code_name='Duration Type' order by mcv.id";
		
		DurationTypeDataMapper rowMapper = new DurationTypeDataMapper();
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	public List<BillFrequencyCodeData> getBillFrequency(){
		String sql = "select mcv.id as id,mcv.code_value as billFrequency from m_code_value mcv,m_code mc where mcv.code_id=mc.id and mc.code_name='Bill Frequency' order by mcv.id";
		BillFrequencyRowMapper rowMapper = new BillFrequencyRowMapper();
		return jdbcTemplate.query(sql,rowMapper);
	}
	
	public ChargeCodeData getChargeCode(Long chargeCodeId){
		String sql = "select charge_code as chargeCode, charge_description as chargeDescription, charge_type as chargeType,charge_duration as chargeDuration, duration_type as durationType, tax_inclusive as taxInclusive,billfrequency_code as billFrequencyCode from b_charge_codes where id = ?";
		ChargeCodeDataByIdRowMapper rowMapper = new ChargeCodeDataByIdRowMapper();
		return jdbcTemplate.queryForObject(sql, rowMapper,new Object[]{chargeCodeId});
	}
}
