package org.mifosplatform.billing.supplier.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.supplier.data.SupplierData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


@Service
public class SupplierReadPlatformServiceImpl implements
		SupplierReadPlatformService {
	
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public SupplierReadPlatformServiceImpl(final TenantAwareRoutingDataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private class SupplierMapper implements RowMapper<SupplierData> {
		@Override
		public SupplierData mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long id = rs.getLong("id");
			String supplierCode = rs.getString("supplierCode");
			String supplierDescription = rs.getString("supplierDescription");
			String supplierAddress = rs.getString("supplierAddress");
			return new SupplierData(id, supplierCode, supplierDescription, supplierAddress);
		}
		
		public String schema() {
			String sql = " id as id, supplier_code as supplierCode, supplier_description as supplierDescription, supplier_address as supplierAddress "
						 + " from b_supplier ";
			return sql;
		}
	}

	@Override
	public List<SupplierData> retrieveSupplier() {
		SupplierMapper supplierMapper = new SupplierMapper();
		String sql = " Select " + supplierMapper.schema();
		
		return this.jdbcTemplate.query(sql, supplierMapper,new Object[] {});
	}
	
	
}
