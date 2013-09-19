package org.mifosplatform.billing.inventory.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.inventory.data.InventoryGrnData;
import org.mifosplatform.billing.inventory.domain.InventoryGrnRepository;
import org.mifosplatform.billing.inventory.exception.InventoryItemDetailsExist;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class InventoryGrnReadPlatformServiceImp implements InventoryGrnReadPlatformService{

	
	private final JdbcTemplate jdbcTemplate;
	private final InventoryGrnRepository inventoryGrnRepository;
	
	@Autowired
	public InventoryGrnReadPlatformServiceImp(final TenantAwareRoutingDataSource dataSource,InventoryGrnRepository inventoryGrnRepository){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.inventoryGrnRepository = inventoryGrnRepository;
		
	}
	
	
	

	@Override
	public Collection<InventoryGrnData> retriveGrnDetails() {

		GrnMapper grn = new GrnMapper();
		String sql = "select g.id as id, g.purchase_date as purchaseDate, g.supplier_id as supplierId, g.item_master_id as itemMasterId, g.orderd_quantity as orderdQuantity, g.received_quantity as receivedQuantity, im.item_description as itemDescription, s.supplier_description as supplierDescription from b_grn g left outer join b_item_master im on g.item_master_id = im.id left outer join b_supplier s on g.supplier_id=s.id";
		return jdbcTemplate.query(sql,grn,new Object[]{});
	}

	
		
	public boolean validateForExist(final Long grnId){
		
		boolean exist = inventoryGrnRepository.exists(grnId);
		if(!exist){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public InventoryGrnData retriveGrnDetailTemplate(final Long grnId){
		GrnMapper grn = null;
		String sql = null;
		/*try{*/
		grn = new GrnMapper();
		sql = "select "+grn.schema()+" where g.id = ?";
		/*}catch(Exception e){
			 throw new InventoryItemDetailsExist("Item is already existing with item SerialNumber:","Item is already existing with item SerialNumber:",grnId);
		}*/
		return jdbcTemplate.queryForObject(sql,grn,new Object[]{grnId});
	}




	@Override
	public Collection<InventoryGrnData> retriveGrnIds() {
		GrnIds rowMapper = new GrnIds();
		String sql = "select id from b_grn where orderd_quantity>received_quantity";
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	
	private class GrnMapper implements RowMapper<InventoryGrnData>{

		@Override
		public InventoryGrnData mapRow(ResultSet rs, int rowNum)
			throws SQLException {
			
			Long id = rs.getLong("id");
			LocalDate purchaseDate =JdbcSupport.getLocalDate(rs,"purchaseDate");
			Long supplierId = rs.getLong("supplierId");
			Long itemMasterId = rs.getLong("itemMasterId");
			Long orderedQuantity = rs.getLong("orderdQuantity");
			Long receivedQuantity = rs.getLong("receivedQuantity");
			String itemDescription = rs.getString("itemDescription");
			String supplierName = rs.getString("supplierDescription");
			return new InventoryGrnData(id,purchaseDate,supplierId,itemMasterId,orderedQuantity,receivedQuantity,itemDescription,supplierName);
			
		}
		
		public String schema(){
			String sql = "g.id as id, g.purchase_date as purchaseDate, g.supplier_id as supplierId, g.item_master_id as itemMasterId, g.orderd_quantity as orderdQuantity, g.received_quantity as receivedQuantity, im.item_description as itemDescription, s.supplier_description as supplierDescription from b_grn g left outer join b_item_master im on g.item_master_id = im.id left outer join b_supplier s on g.supplier_id = s.id";
			return sql;
		}
		
	}
	
	private class GrnIds implements RowMapper<InventoryGrnData>{

		@Override
		public InventoryGrnData mapRow(ResultSet rs, int rowNum)throws SQLException {
			
			Long id = rs.getLong("id");
			
			return new InventoryGrnData(id);
		}
		
	}

	
	
}
