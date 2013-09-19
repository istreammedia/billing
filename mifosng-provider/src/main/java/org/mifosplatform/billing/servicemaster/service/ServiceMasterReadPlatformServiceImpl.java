package org.mifosplatform.billing.servicemaster.service;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.mifosplatform.billing.service.ServiceMasterReadPlatformService;
import org.mifosplatform.billing.servicemaster.data.ServiceMasterOptionsData;
import org.mifosplatform.billing.servicemaster.data.ServiceMasterData;
import org.mifosplatform.billing.servicemaster.data.ServiceStatusEnumaration;
import org.mifosplatform.billing.servicemaster.data.ServiceTypeEnum;
import org.mifosplatform.billing.servicemaster.data.ServiceUnitTypeEnum;
import org.mifosplatform.billing.servicemaster.data.ServiceUnitTypeEnumaration;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
@Service
public class ServiceMasterReadPlatformServiceImpl implements  ServiceMasterReadPlatformService{

	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;

	@Autowired
	public  ServiceMasterReadPlatformServiceImpl(final PlatformSecurityContext context, final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Collection<ServiceMasterData> retrieveAllServiceMasterData() {
		this.context.authenticatedUser();

		ServiceMasterMapper mapper = new ServiceMasterMapper();
		String sql = "select " + mapper.schema();

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});

	}

	protected static final class ServiceMasterMapper implements RowMapper<ServiceMasterData> {

		@Override
		public ServiceMasterData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum)
				throws SQLException {
			String discounType = rs.getString("serviceType");
			String discountValue=rs.getString("categoryType");


			return new ServiceMasterData(discounType,discountValue);

		}


		public String schema() {
			return "d.servicetype as servicetype , d.categorytype as categorytype from m_servicemaster_type d";
		}
	}

	@Override
	public List<ServiceMasterOptionsData> retrieveServices() {
		this.context.authenticatedUser();

		ServiceMapper mapper = new ServiceMapper();
		String sql = "select " + mapper.schema()+" where d.is_deleted='n' ";

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});

	}

	protected static final class ServiceMapper implements RowMapper<ServiceMasterOptionsData> {

		@Override
		public ServiceMasterOptionsData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum)
				throws SQLException {
			Long id=rs.getLong("id");
			String serviceCode = rs.getString("serviceCode");
			String serviceDescription=rs.getString("serviceDescription");
			String serviceType=rs.getString("serviceType");
			String serviceUnitType=rs.getString("serviceUnitType");
			String status=rs.getString("status");
			String isOptional=rs.getString("isOptional");


			return new ServiceMasterOptionsData(id,serviceCode,serviceDescription,serviceType,serviceUnitType,status,isOptional);

		}


		public String schema() {
			return "d.id AS id,d.service_code AS serviceCode,d.service_description AS serviceDescription,d.service_type AS serviceType," +
					"d.service_unittype as serviceUnitType,d.status as status,d.is_optional as isOptional FROM b_service d";
		}

}

	@Override
	public ServiceMasterOptionsData retrieveIndividualService(Long serviceId) {
		ServiceMapper mapper = new ServiceMapper();
		String sql = "select " + mapper.schema()+" where d.id="+serviceId;

		return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] {});

	}

	private static final class ServicesMapper implements RowMapper<ServiceMasterOptionsData> {

		@Override
		public ServiceMasterOptionsData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum)
				throws SQLException {
			Long id=rs.getLong("id");
			String serviceCode = rs.getString("serviceCode");
			String serviceDescription=rs.getString("serviceDescription");
			String serviceType=rs.getString("serviceType");


			return new ServiceMasterOptionsData(id,serviceCode,serviceDescription,serviceType, serviceType, serviceType, serviceType);

		}


		public String schema() {
			return "d.id AS id,d.service_code AS serviceCode,d.service_description AS serviceDescription,d.service_type AS serviceType," +
					"d.service_unittype as serviceUnitType,d.status as status,d.is_optional as isOptional FROM b_service d";
		}
}

	@Override
	public List<EnumOptionData> retrieveServicesTypes() {
		EnumOptionData tv = ServiceStatusEnumaration.serviceType(ServiceTypeEnum.TV);
		EnumOptionData bb= ServiceStatusEnumaration.serviceType(ServiceTypeEnum.BB);
		EnumOptionData voId= ServiceStatusEnumaration.serviceType(ServiceTypeEnum.VOIP);
		EnumOptionData iptv= ServiceStatusEnumaration.serviceType(ServiceTypeEnum.IPTV);
		EnumOptionData vod= ServiceStatusEnumaration.serviceType(ServiceTypeEnum.VOD);
		EnumOptionData none= ServiceStatusEnumaration.serviceType(ServiceTypeEnum.NONE);
		
		List<EnumOptionData> categotyType = Arrays.asList(tv,bb,voId,iptv,vod,none);
			return categotyType;
	}

	@Override
	public List<EnumOptionData> retrieveServiceUnitType() {
		EnumOptionData onOff = ServiceUnitTypeEnumaration.serviceUnitType(ServiceUnitTypeEnum.ON_OFF);
		EnumOptionData scheme= ServiceUnitTypeEnumaration.serviceUnitType(ServiceUnitTypeEnum.SCHEME);
		EnumOptionData quantity = ServiceUnitTypeEnumaration.serviceUnitType(ServiceUnitTypeEnum.QUANTITY);
		
		List<EnumOptionData> categotyType = Arrays.asList(onOff,scheme,quantity);
			return categotyType;
	}
}
