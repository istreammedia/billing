package org.mifosplatform.billing.pricing.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.mifosplatform.billing.charge.data.ChargesData;
import org.mifosplatform.billing.contract.data.SubscriptionData;
import org.mifosplatform.billing.plan.data.PlanData;
import org.mifosplatform.billing.plan.data.ServiceData;
import org.mifosplatform.billing.plan.service.ChargeVariant;
import org.mifosplatform.billing.pricing.data.PricingData;
import org.mifosplatform.billing.pricing.data.SavingChargeVaraint;
import org.mifosplatform.billing.service.DiscountMasterData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


@Service
public class PriceReadPlatformServiceImpl implements PriceReadPlatformService{


	 private final JdbcTemplate jdbcTemplate;
	    private final PlatformSecurityContext context;

	    @Autowired
	    public PriceReadPlatformServiceImpl(final PlatformSecurityContext context, final TenantAwareRoutingDataSource dataSource) {
	        this.context = context;
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	    }


	/*@Override
	public List<PlanData> retrievePlanDetails() {

		  context.authenticatedUser();

	        String sql = "select s.id as id,s.plan_code as planCode from b_plan_master s ";


	        RowMapper<PlanData> rm = new PlanMapper();

	        return this.jdbcTemplate.query(sql, rm, new Object[] { });
	}


	 private static final class PlanMapper implements RowMapper<PlanData> {

	        @Override
	        public PlanData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

	        Long id = rs.getLong("id");
	            String plan_code = rs.getString("planCode");


	            return new PlanData(id,plan_code);
	        }
}
*/

		@Override
		public List<SubscriptionData> retrievePaytermData() {

			context.authenticatedUser();

			SubscriptionDataMapper mapper = new SubscriptionDataMapper();

			String sql = "select " + mapper.schema();

			return this.jdbcTemplate.query(sql, mapper, new Object[] {});
		}

		private static final class SubscriptionDataMapper implements
				RowMapper<SubscriptionData> {

			public String schema() {
				return " sb.id as id,sb.payterm_type as paytermType,sb.units as units "
						+ " from b_payments sb ";

			}

			@Override
			public SubscriptionData mapRow(ResultSet rs, int rowNum)
					throws SQLException {

				Long id = rs.getLong("id");
				String payterm_type = rs.getString("paytermType");
				String units = rs.getString("units");
				String contractPeriod = units.concat(payterm_type);
				SubscriptionData data = new SubscriptionData(id, contractPeriod);

				return data;

			}

		}

	 @Override
		public List<ServiceData> retrievePrcingDetails(Long planId) {

			  context.authenticatedUser();

		        String sql = "SELECT sm.id AS id,sm.service_description AS service_description,p.plan_code as planCode,"
				     +" pm.service_code AS service_code   FROM b_plan_detail pm, b_service sm,b_plan_master p"
					 +" WHERE pm.service_code = sm.service_code AND p.id = pm.plan_id and sm.is_deleted ='n' and  pm.plan_id=?";


		        RowMapper<ServiceData> rm = new PeriodMapper();

		        return this.jdbcTemplate.query(sql, rm, new Object[] { planId });
		}


		 private static final class PeriodMapper implements RowMapper<ServiceData> {

		        @Override
		        public ServiceData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

		        Long id = rs.getLong("id");
		            String planCode = rs.getString("planCode");
		            String serviceCode = rs.getString("service_code");
		            String serviceDescription = rs.getString("service_description");
		         //   return new ServiceData(id,null,planCode,null,serviceCode,serviceDescription,null,null);
		            return new  ServiceData(id, null, planCode,null, serviceCode, serviceDescription,null);
		            
		        }
	}

		 @Override
			public List<ServiceData> retrievePriceDetails(String planCode) {

				  context.authenticatedUser();

			        String sql = "SELECT  p.plan_code as plan_code,pm.id as id,pm.service_code as service_code,se.service_description as service_description,"
			        		   +" c.charge_description as charge_description,pm.charge_code as charge_code,pm.charging_variant as charging_variant,pm.price as price,"
			        		  +" pr.priceregion_name as priceregion from  b_plan_master p,b_plan_pricing  pm,b_service se,b_charge_codes c,b_priceregion_master pr"
			        		  +" where p.id=pm.plan_id and pm.service_code = se.service_code and  pm.price_region_id =pr.id  AND "
			        		   +" pm.charge_code=c.charge_code and pm.is_deleted='n' and se.is_deleted='n' and  pm.plan_id ='"+planCode+"'";


			        RowMapper<ServiceData> rm = new PriceMapper();

			        return this.jdbcTemplate.query(sql, rm, new Object[] { });
			}


			 private static final class PriceMapper implements RowMapper<ServiceData> {

			        @Override
			        public ServiceData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

			        Long id = rs.getLong("id");
			            String planCode = rs.getString("plan_code");
			            String planDescription=null;
			            String serviceCode = rs.getString("service_description");
			            String chargeCode = rs.getString("charge_description");
			           String chargingVariant=rs.getString("charging_variant");
			           String priceregion=rs.getString("priceregion");
			            BigDecimal price=rs.getBigDecimal("price");
			            int chargingVariant1 = new Integer(chargingVariant);
			           EnumOptionData chargingvariant = SavingChargeVaraint.interestCompoundingPeriodType(chargingVariant1);
			           String chargeValue=chargingvariant.getValue();
			            return new ServiceData(id,planCode,serviceCode,planDescription,chargeCode,chargeValue,price,priceregion);
			        }
		}


	@Override
	public List<DiscountMasterData> retrieveDiscountDetails() {

		  context.authenticatedUser();

	        String sql = "select s.id as id,s.discount_code as discountcode,s.discount_description as discount_description from b_discount_master s";


	        RowMapper<DiscountMasterData> rm = new DiscountMapper();

	        return this.jdbcTemplate.query(sql, rm, new Object[] {});
	}


	 private static final class DiscountMapper implements RowMapper<DiscountMasterData> {

	        @Override
	        public DiscountMasterData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

	        Long id = rs.getLong("id");
	            String discountcode = rs.getString("discountcode");
	            String discountdesc = rs.getString("discount_description");


	            return new DiscountMasterData(id,discountcode,discountdesc,null,null,null,null);
	        }

	 }

	@Override
	public List<ChargesData> retrieveChargeCode() {
		 String sql = "select s.id as id,s.charge_code as charge_code,s.charge_description as charge_description from b_charge_codes s";


		 RowMapper<ChargesData> rm = new ChargeMapper();

        return this.jdbcTemplate.query(sql, rm, new Object[] {});
}


 private static final class ChargeMapper implements RowMapper<ChargesData> {

        @Override
        public ChargesData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

        Long id = rs.getLong("id");
            String chargeCode = rs.getString("charge_code");
            String chargeDesc= rs.getString("charge_description");

            return new ChargesData(id,chargeCode,chargeDesc);
        }
}


@Override
public List<EnumOptionData> retrieveChargeVariantData() {



	EnumOptionData base = SavingChargeVaraint.interestCompoundingPeriodType(ChargeVariant.BASE);

	List<EnumOptionData> categotyType = Arrays.asList(base);
	return categotyType;
}

@Override

public List<ServiceData> retrieveServiceCodeDetails(Long planCode) {

	  context.authenticatedUser();

        String sql = "SELECT p.id AS planId, pm.id AS id,ch.charge_description AS chargeDescription, pm.plan_id AS plan_code,"
			+"pm.service_code AS service_code,pm.charge_code AS charge_code,pm.price_region_id as priceregion " +
			" FROM b_plan_master p, b_plan_pricing pm,b_charge_codes ch"
           +" WHERE p.id = pm.plan_id AND  ch.charge_code = pm.charge_code and pm.is_deleted='n' and pm.plan_id="+planCode;


        RowMapper<ServiceData> rm = new ServiceMapper();

        return this.jdbcTemplate.query(sql, rm, new Object[] {  });
}


 private static final class ServiceMapper implements RowMapper<ServiceData> {

        @Override
        public ServiceData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

               Long id = rs.getLong("id");
               Long planId = rs.getLong("planId");
               String planCode = rs.getString("plan_code");
               String serviceCode = rs.getString("service_code");
               String chargeCode = rs.getString("charge_code");
               String chargeDescription = rs.getString("chargeDescription");
               String priceRegion = rs.getString("priceregion");

            return new ServiceData(id,planId,planCode,chargeCode,serviceCode,chargeDescription,priceRegion);
        }
}


@Override
public PricingData retrieveSinglePriceDetails(String priceId) {
	 context.authenticatedUser();

     String sql = "SELECT p.plan_id AS planId,p.plan_code as planCode,s.id AS serviceId,c.id AS chargeId,p.charging_variant AS chargeVariant,p.price AS price," +
     		" p.discount_id AS discountId,p.price_region_id as priceregion FROM b_plan_pricing p, b_service s, b_charge_codes c " +
     		" WHERE p.charge_code = c.charge_code AND p.service_code = s.service_code AND p.id = ?";


     RowMapper<PricingData> rm = new PricingMapper();

     return this.jdbcTemplate.queryForObject(sql, rm, new Object[] { priceId });
}


private static final class PricingMapper implements RowMapper<PricingData> {

     @Override
     public PricingData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

	  Long planId = rs.getLong("planId");
     Long serviceId = rs.getLong("serviceId");
         Long chargeId = rs.getLong("chargeId");
        BigDecimal price = rs.getBigDecimal("price");
         Long discountId = rs.getLong("discountId");
         String chargeVariant = rs.getString("chargeVariant");
         int chargeVariantId=new Integer(chargeVariant);
         Long priceregion = rs.getLong("priceregion");
         String planCode = rs.getString("planCode");
         return new PricingData(planId,serviceId,chargeId,price,discountId,chargeVariantId,priceregion,planCode);
     }
}



}
