package org.mifosplatform.billing.plan.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.contract.data.SubscriptionData;
import org.mifosplatform.billing.order.data.OrderStatusEnumaration;
import org.mifosplatform.billing.order.data.VolumeTypeEnumaration;
import org.mifosplatform.billing.plan.data.BillRuleData;
import org.mifosplatform.billing.plan.data.PlanData;
import org.mifosplatform.billing.plan.data.ServiceData;
import org.mifosplatform.billing.plan.data.SystemData;
import org.mifosplatform.billing.plan.domain.StatusTypeEnum;
import org.mifosplatform.billing.plan.domain.VolumeTypeEnum;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class PlanReadPlatformServiceImpl implements PlanReadPlatformService {

	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;
	public final static String planTypeConst="prepaid";

	@Autowired
	public PlanReadPlatformServiceImpl(final PlatformSecurityContext context,
			final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<ServiceData> retrieveAllServices() {

		context.authenticatedUser();
		PlanMapper mapper = new PlanMapper();

		String sql = "select " + mapper.schema();

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});

	}

	private static final class PlanMapper implements RowMapper<ServiceData> {

		public String schema() {
			return "da.id as id, da.service_code as service_code, da.service_description as service_description "
					+ " from b_service da where da.is_deleted='n' ";

		}

		@Override
		public ServiceData mapRow(final ResultSet rs,
				@SuppressWarnings("unused") final int rowNum)
				throws SQLException {

			Long id = rs.getLong("id");
			String serviceCode = rs.getString("service_code");
			String serviceDescription = rs.getString("service_description");
			//String serviceDescription = rs.getString("service_description");
			return new ServiceData(id,null,null,null,serviceCode, serviceDescription,null);

		}
	}

	@Override
	public List<BillRuleData> retrievebillRules() {

		context.authenticatedUser();

		BillRuleDataMapper mapper = new BillRuleDataMapper();

		String sql = "select " + mapper.schema();

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});
	}

	private static final class BillRuleDataMapper implements
			RowMapper<BillRuleData> {

		public String schema() {
			return " b.enum_id AS id,b.enum_message_property AS billingRule,b.enum_value AS value FROM r_enum_value b" +
					" WHERE enum_name = 'billing_rules'";

		}

		@Override
		public BillRuleData mapRow(ResultSet rs, int rowNum)
				throws SQLException {

			Long id = rs.getLong("id");
			String billrules = rs.getString("billingRule");
			String value = rs.getString("value");
			BillRuleData data = new BillRuleData(id, billrules,value);

			return data;

		}

	}

	@Override
	public List<PlanData> retrievePlanData(String planType) {

		context.authenticatedUser();

		 String sql=null;
		PlanDataMapper mapper = new PlanDataMapper();
		
		if(planType!=null && planType.equalsIgnoreCase(planTypeConst)){

		 sql = "select " + mapper.schema()+" AND pm.is_prepaid ='Y'";
		 
		}else{
		
			sql = "select " + mapper.schema();
		}

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});
	}

	private static final class PlanDataMapper implements RowMapper<PlanData> {

		public String schema() {


			return " pm.id,pm.plan_code,pm.plan_description,pm.start_date,pm.end_date,pm.plan_status,pm.duration as duration"
					 +" from  b_plan_master pm  where pm.is_deleted='n' ";

		}

		@Override
		public PlanData mapRow(ResultSet rs, int rowNum) throws SQLException {

			Long id = rs.getLong("id");
			String planCode = rs.getString("plan_code");
			String planDescription = rs.getString("plan_description");
			LocalDate startDate = JdbcSupport.getLocalDate(rs, "start_date");
		    int planStatus = JdbcSupport.getInteger(rs,"plan_status");
			LocalDate endDate = JdbcSupport.getLocalDate(rs, "end_date");
			String duration=rs.getString("duration");
			long plan=planStatus;
			EnumOptionData enumstatus=OrderStatusEnumaration.OrderStatusType(planStatus);
			//return new PlanData(id, planCode, planDescription, startDate,plan, endDate,status);
			/*return new PlanData(id,planCode,startDate,endDate,null,null,plan,planDescription,null,null,
					status);
			*/	
			return new PlanData(id, planCode, startDate, endDate,null,duration, plan, planDescription, plan, null, enumstatus,
					null,null, null,null,null);
		}
	}

	@Override
	public List<SubscriptionData> retrieveSubscriptionData() {

		context.authenticatedUser();

		SubscriptionDataMapper mapper = new SubscriptionDataMapper();

		String sql = "select " + mapper.schema();

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});
	}

	private static final class SubscriptionDataMapper implements
			RowMapper<SubscriptionData> {

		public String schema() {
			return " sb.id as id,sb.contract_period as subscription_type,sb.contract_duration as units "
					+ " from b_contract_period sb where is_deleted=0";

		}

		@Override
		public SubscriptionData mapRow(ResultSet rs, int rowNum)
				throws SQLException {

			Long id = rs.getLong("id");
			String subscriptionType = rs.getString("subscription_type");
			String units = rs.getString("units");

			SubscriptionData data = new SubscriptionData(id, subscriptionType);

			return data;

		}

	}

	@Override
	public List<EnumOptionData> retrieveNewStatus() {
		EnumOptionData active = OrderStatusEnumaration.OrderStatusType(StatusTypeEnum.ACTIVE);
		EnumOptionData inactive = OrderStatusEnumaration.OrderStatusType(StatusTypeEnum.INACTIVE);
		List<EnumOptionData> categotyType = Arrays.asList(active, inactive);
			return categotyType;

	}

	@Override
	public PlanData retrievePlanData(Long planCode) {
		  context.authenticatedUser();

	        String sql = "SELECT pm.id AS id,pm.plan_code AS plan_code,pm.plan_description AS plan_description,pm.start_date AS start_date,pm.end_date AS end_date,"
	        		   +"pm.plan_status AS plan_status,pm.provision_sys AS provisionSys,pm.bill_rule AS bill_rule,pm.duration AS contract_period,pm.is_prepaid as isPrepaid,"
	        		  +" pm.allow_topup as allowTopup,v.volume_type as volumeType, v.units as units,v.units_type as unitType FROM b_plan_master pm  left join b_volume_details v" +
	        		  " on pm.id = v.plan_id WHERE pm.id = ? AND pm.is_deleted = 'n'";


	        RowMapper<PlanData> rm = new ServiceMapper();

	        return this.jdbcTemplate.queryForObject(sql, rm, new Object[] { planCode });
	}


	 private static final class ServiceMapper implements RowMapper<PlanData> {

	        @Override
	        public PlanData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

	        Long id = rs.getLong("id");
	            String planCode = rs.getString("plan_code");
	            LocalDate startDate = JdbcSupport.getLocalDate(rs, "start_date");
	            LocalDate endDate = JdbcSupport.getLocalDate(rs, "end_date");
	            Long billRule = rs.getLong("bill_rule");
	            int planStatus = JdbcSupport.getInteger(rs,"plan_status");
	            String planDescription = rs.getString("plan_description");
	            String contractPeriod = rs.getString("contract_period");
	            String provisionSys=rs.getString("provisionSys");
	            String isPrepaid=rs.getString("isPrepaid");
	            String volume=rs.getString("volumeType");
	            String allowTopup=rs.getString("allowTopup");
	            String units=rs.getString("units");
	            String unitType=rs.getString("unitType");

	            //EnumOptionData status = SavingStatusEnumaration.OrderStatusType(plan_status);
	            long status1=planStatus;
	            return new PlanData(id,planCode,startDate,endDate,billRule,contractPeriod,status1,planDescription,status1,provisionSys,null,isPrepaid,
	            		allowTopup,volume,units,unitType);
	        }
	}

	@Override
	public List<ServiceData> getselectedService(List<ServiceData> data,
			List<ServiceData> services) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServiceData> retrieveSelectedServices(Long planId) {
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
	            return new ServiceData(id,null,planCode,serviceCode,serviceDescription,null,null);
	        }
}
	 
	 @Override
		public List<SystemData> retrieveSystemData() {
			context.authenticatedUser();

			SystemDataMapper mapper = new SystemDataMapper();

			String sql = "select " + mapper.schema();

			return this.jdbcTemplate.query(sql, mapper, new Object[] {});
		}

		private static final class SystemDataMapper implements RowMapper<SystemData> {

			public String schema() {


				return " mc.id as id,mc.code_value as codeValue from m_code m,m_code_value mc where m.id = mc.code_id and m.code_name='Provisioning' order by id";

			}

			@Override
			public SystemData mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				Long id=rs.getLong("id");
				String ProvisioingSystem = rs.getString("codeValue");
				return new SystemData(id,ProvisioingSystem);
				
			}

		}

		@Override
		public List<EnumOptionData> retrieveVolumeTypes() {
			EnumOptionData iptv = VolumeTypeEnumaration.VolumeTypeEnum(VolumeTypeEnum.IPTV);
					EnumOptionData vod = VolumeTypeEnumaration.VolumeTypeEnum(VolumeTypeEnum.VOD);
			List<EnumOptionData> categotyType = Arrays.asList(iptv,vod);
				return categotyType;
		}

		


	}
