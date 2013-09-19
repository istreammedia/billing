package org.mifosplatform.billing.ownedhardware.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.mifosplatform.billing.item.data.ItemData;
import org.mifosplatform.billing.ownedhardware.data.OwnedHardwareData;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;



@Service
public class OwnedHardwareReadPlatformServiceImp implements	OwnedHardwareReadPlatformService {

	
	private JdbcTemplate jdbcTemplate;
	private PlatformSecurityContext context;
	
	@Autowired
	public OwnedHardwareReadPlatformServiceImp(final TenantAwareRoutingDataSource dataSource, final PlatformSecurityContext context) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	@Override
	public List<OwnedHardwareData> retriveOwnedHardwareData(Long clientId) {
		OwnedHardwareMapper mapper = new OwnedHardwareMapper();
		String sql = "select "+mapper.schema()+" where oh.client_id = ?";
		return jdbcTemplate.query(sql, mapper, new Object[]{clientId});
	}
	
	
	@Override
	public List<ItemData> retriveTemplate(){
		ItemDataMapper mapper = new ItemDataMapper();
		String sql = mapper.schema();
		return jdbcTemplate.query(sql, mapper,new Object[]{});
	}
	
	
	private static final class OwnedHardwareMapper implements RowMapper<OwnedHardwareData>{
		@Override
		public OwnedHardwareData mapRow(ResultSet rs, int rowNum)
				throws SQLException {
				Long id = rs.getLong("id");
				Long clientId = rs.getLong("clientId");
				String serialNumber = rs.getString("serialNumber");
				String provisioningSerialNumber = rs.getString("provisioningSerialNumber");
				String status = rs.getString("status");
				LocalDate allocationDate = JdbcSupport.getLocalDate(rs,"allocationDate");
				String itemType = rs.getString("itemType");
			return new OwnedHardwareData(id, clientId, serialNumber, provisioningSerialNumber, allocationDate, status, itemType);
		}
		
		public String schema(){
			String sql = "oh.id as id, oh.client_id as clientId, oh.serial_number as serialNumber, oh.provisioning_serial_number as provisioningSerialNumber, oh.allocated_date as allocationDate, oh.status as status, im.item_description as itemType from b_owned_hardware oh left outer join b_item_master im on oh.item_type=im.id";
			return sql;
		}
	}
	
	private final class SerialNumberMapper implements RowMapper<String>{
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String serialNumber = rs.getString("serialNumber");
			return serialNumber;
		}
	}
	
	@Override
	public List<String> retriveSerialNumbers() {
		SerialNumberMapper rowMapper = new SerialNumberMapper();
		String sql = "select serial_number as serialNumber from b_owned_hardware";
		return jdbcTemplate.query(sql,rowMapper);
	}
	
	private static final class ItemDataMapper implements RowMapper<ItemData>{
		
		@Override
		public ItemData mapRow(ResultSet rs, int rowNum) throws SQLException {
				Long id = rs.getLong("id");
				String itemDesc = rs.getString("itemDescription");
			return new ItemData(id, "", itemDesc, "", "", "", 0, new BigDecimal(0));
		}
		
		public String schema(){	
			String sql = "select im.id as id, im.item_description as itemDescription from b_item_master im order by id asc";
			return sql;
		}
	}
	
	
}
