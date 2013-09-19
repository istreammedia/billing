package org.mifosplatform.billing.order.domain;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.plan.data.ServiceData;
import org.mifosplatform.billing.pricing.data.PriceData;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class OrderReadPlatformImpl {

	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;



	public OrderReadPlatformImpl(PlatformSecurityContext context2,
			JdbcTemplate jdbcTemplate2) {

		this.context=context2;
		this.jdbcTemplate=jdbcTemplate2;

	}



	public List<ServiceData> retrieveAllServices(Long plan_code) {


		PlanMapper mapper = new PlanMapper();

		String sql = "select " + mapper.schema()+" and da.plan_id = '"+plan_code+"'" ;
		return this.jdbcTemplate.query(sql, mapper, new Object[] {});

	}

	private static final class PlanMapper implements RowMapper<ServiceData> {

		public String schema() {
			return "da.id as id,se.id as serviceId, da.service_code as service_code, da.plan_id as plan_code,se.service_type as serviceType "
					+" from b_plan_detail da,b_service se where da.service_code = se.service_code";

		}

		@Override
		public ServiceData mapRow(final ResultSet rs,
				@SuppressWarnings("unused") final int rowNum)
				throws SQLException {
			Long id = rs.getLong("id");
			String serviceCode = rs.getString("service_code");
			String serviceType = rs.getString("serviceType");
			Long serviceid = rs.getLong("serviceId");

			ServiceData serviceData=new ServiceData(id,serviceid,serviceCode, null,null,null, null);
			
			serviceData.setServiceType(serviceType);


			return serviceData; 

		}
	}
		public List<PriceData> retrieveAllPrices(Long plan_code,String billingFreq, Long clientId ) {


			PriceMapper mapper1 = new PriceMapper();

			String sql = "select " + mapper1.schema()+" and da.plan_id = '"+plan_code+"' and (c.billfrequency_code='"+billingFreq+"'  or c.billfrequency_code='Once')" +
					" AND ca.client_id = ?  AND da.price_region_id =pd.priceregion_id AND s.state_name = ca.state And s.parent_code=pd.country_id" +
					" AND pd.state_id = s.id";
			return this.jdbcTemplate.query(sql, mapper1, new Object[] { clientId });

		} 

		private static final class PriceMapper implements RowMapper<PriceData> {

			public String schema() {
				return " da.id AS id,se.id AS serviceId,da.service_code AS service_code,da.charge_code AS charge_code,da.charging_variant AS charging_variant," +
						"c.charge_type AS charge_type,c.charge_duration AS charge_duration,c.duration_type AS duration_type,da.discount_id AS discountId," +
						"c.tax_inclusive AS taxInclusive,da.price AS price,da.price_region_id,s.id AS stateId,s.parent_code AS countryId,pd.state_id AS regionState," +
						"pd.country_id AS regionCountryId FROM b_plan_pricing da,b_charge_codes c,b_service se,b_client_address ca,b_state s,b_priceregion_detail pd" +
						" WHERE  da.charge_code = c.charge_code  AND da.service_code = se.service_code AND da.is_deleted = 'n' AND ca.address_key='PRIMARY'" ;
					   

			}

			@Override
			public PriceData mapRow(final ResultSet rs,
					@SuppressWarnings("unused") final int rowNum)
					throws SQLException {

				Long id = rs.getLong("id");
				String serviceCode = rs.getString("service_code");
				String chargeCode = rs.getString("charge_code");
				String chargingVariant = rs.getString("charging_variant");
				BigDecimal price=rs.getBigDecimal("price");
				String chargeType = rs.getString("charge_type");
				String chargeDuration = rs.getString("charge_duration");
				String durationType = rs.getString("duration_type");
				Long serviceid = rs.getLong("serviceId");
				Long discountId=rs.getLong("discountId");
				Long stateId = rs.getLong("stateId");
				Long countryId=rs.getLong("countryId");
				Long regionState = rs.getLong("regionState");
				Long regionCountryId=rs.getLong("regionCountryId");
				boolean taxinclusive=rs.getBoolean("taxInclusive");
				
				return new PriceData(id, serviceCode, chargeCode,chargingVariant,price,chargeType,chargeDuration,durationType,
			              serviceid,discountId,taxinclusive,stateId,countryId,regionState,regionCountryId);

			}
	}
		public List<PriceData> retrieveDefaultPrices(Long planId,String billingFrequency, Long clientId) {
			
			PriceMapper mapper1 = new PriceMapper();
			String sql = "select " + mapper1.schema()+" and da.plan_id = '"+planId+"' and (c.billfrequency_code='"+billingFrequency+"'  or c.billfrequency_code='Once')" +
					" AND ca.client_id = ?  AND da.price_region_id =pd.priceregion_id AND s.state_name = ca.state And s.parent_code=pd.country_id" +
					" AND pd.state_id =0";
			return this.jdbcTemplate.query(sql, mapper1, new Object[] { clientId });
		}

	}


