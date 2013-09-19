package org.mifosplatform.billing.plan.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.contract.data.SubscriptionData;
import org.mifosplatform.billing.discountmaster.service.DiscountReadPlatformService;
import org.mifosplatform.billing.order.data.OrderStatusEnumaration;
import org.mifosplatform.billing.order.data.VolumeTypeEnumaration;
import org.mifosplatform.billing.plan.data.BillRuleData;
import org.mifosplatform.billing.plan.data.PlanData;
import org.mifosplatform.billing.plan.data.ServiceData;
import org.mifosplatform.billing.plan.data.SystemData;
import org.mifosplatform.billing.plan.domain.StatusTypeEnum;
import org.mifosplatform.billing.plan.domain.VolumeTypeEnum;
import org.mifosplatform.billing.service.DiscountMasterData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class DiscountReadPlatformServiceImpl implements DiscountReadPlatformService
{

	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;


	@Autowired
	public DiscountReadPlatformServiceImpl(final PlatformSecurityContext context,final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<DiscountMasterData> retrieveAllDiscounts() {
   
	try{	
		context.authenticatedUser();
		PlanMapper mapper = new PlanMapper();

		String sql = "select " + mapper.schema();

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});
	}catch(EmptyResultDataAccessException accessException){
		return null;
	}

	}

	private static final class PlanMapper implements RowMapper<DiscountMasterData> {

		public String schema() {
			return "ds.id as id, ds.discount_code as discountCode, ds.discount_description as discountdescription," +
				 "ds.discount_type as discountType, ds.discount_rate as discountRate, ds.start_date as startDate, " +
				 "ds.discount_status as status from b_discount_master ds  where ds.is_delete='N' ";

		}

		@Override
		public DiscountMasterData mapRow(final ResultSet rs,
				@SuppressWarnings("unused") final int rowNum)
				throws SQLException {

			Long id = rs.getLong("id");
			String discountCode = rs.getString("discountCode");
			String discountdescription = rs.getString("discountdescription");
			String discountType = rs.getString("discountType");
			BigDecimal discountRate = rs.getBigDecimal("discountRate");
			LocalDate startDate = JdbcSupport.getLocalDate(rs,"startDate");
			String status = rs.getString("status");
			return new DiscountMasterData(id, discountCode, discountdescription, discountType, discountRate,startDate,status);

		}
	}

	@Override
	public DiscountMasterData retrieveDiscountDetails(Long discountId) {
		try{	
			context.authenticatedUser();
			PlanMapper mapper = new PlanMapper();

			String sql = "select " + mapper.schema()+" and ds.id=?";

			return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] { discountId });
		}catch(EmptyResultDataAccessException accessException){
			return null;
		}

		}
}

