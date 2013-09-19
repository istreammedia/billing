package org.mifosplatform.billing.inventory.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.mifosplatform.billing.inventory.data.InventoryItemDetailsData;
import org.mifosplatform.billing.inventory.data.InventoryItemSerialNumberData;
import org.mifosplatform.billing.inventory.data.ItemMasterIdData;
import org.mifosplatform.billing.inventory.data.QuantityData;
import org.mifosplatform.billing.inventory.domain.InventoryItemDetails;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class InventoryItemDetailsReadPlatformServiceImp implements InventoryItemDetailsReadPlatformService {

	
	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;
	
	@Autowired
	InventoryItemDetailsReadPlatformServiceImp(final TenantAwareRoutingDataSource dataSource,final PlatformSecurityContext context){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.context = context;
	}
	
	private class ItemDetailsMapper implements RowMapper<InventoryItemDetailsData>{

		@Override
		public InventoryItemDetailsData mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long id = rs.getLong("id");
			Long itemMasterId = rs.getLong("itemMasterId");
			String serialNumber = rs.getString("serialNumber");
			Long grnId = rs.getLong("grnId");
			String provisioningSerialNumber = rs.getString("provisioningSerialNumber");
			String quality= rs.getString("quality");
			String status = rs.getString("status");
			Long warranty = rs.getLong("warranty");
			String remarks = rs.getString("remarks");
			String itemDescription = rs.getString("itemDescription");
			String supplier = rs.getString("supplier");
			Long clientId = rs.getLong("clientId");
			return new InventoryItemDetailsData(id,itemMasterId,serialNumber,grnId,provisioningSerialNumber,quality,status,warranty,remarks,itemDescription,supplier,clientId);
		}
		
		public String schema(){
			//String sql = "item.id as id,item.item_master_id as itemMasterId, item.serial_no as serialNumber, item.grn_id as grnId, item.provisioning_serialno as provisioningSerialNumber, item.quality as quality, item.status as status, item.warranty as warranty, item.remarks as remarks, master.item_description as itemDescription from b_item_detail item left outer join b_item_master master on item.item_master_id = master.id";
			String sql = "item.id as id,item.item_master_id as itemMasterId, item.serial_no as serialNumber, item.grn_id as grnId, (select supplier_description from b_supplier where id = (select supplier_id from b_grn where b_grn.id=item.grn_id)) as supplier,item.provisioning_serialno as provisioningSerialNumber, item.quality as quality, item.status as status, item.warranty as warranty, item.remarks as remarks, master.item_description as itemDescription, item.client_id as clientId from b_item_detail item left outer join b_item_master master on item.item_master_id = master.id";
			return sql;
		}
		
	}
 

	@Override
	public Collection<InventoryItemDetailsData> retriveAllItemDetails() {
		// TODO Auto-generated method stub
		context.authenticatedUser();
		ItemDetailsMapper itemDetails = new ItemDetailsMapper();
		String sql = "select "+itemDetails.schema();
		return this.jdbcTemplate.query(sql, itemDetails, new Object[] {});
	}


	/*
	 * this method is not implemented or being used, who ever wants to use this method please remove this comment and give a message.
	 * */
	
	@Override
	public InventoryItemDetailsData retriveIndividualItemDetails() {
		// TODO Auto-generated method stub
		context.authenticatedUser();
		return null;
	}


	private class SerialNumberMapper implements RowMapper<QuantityData>{

		@Override
		public QuantityData mapRow(ResultSet rs, int rowNum)throws SQLException {
			String serialNumber = rs.getString("serialNumber");
			return new QuantityData(serialNumber);
		}
	}
	
	private class QuantityMapper implements RowMapper<QuantityData>{
		
		@Override
		public QuantityData mapRow(ResultSet rs,int rowNum)throws SQLException{
			Long quantity = rs.getLong("quantity");
			return new QuantityData(quantity);
		}
	}
	
	private class ItemMasterMapper implements RowMapper<ItemMasterIdData>{
		
		@Override
		public ItemMasterIdData mapRow(ResultSet rs, int rowNum)throws SQLException{
			Long itemMasterId = rs.getLong("itemMasterId");
			return new ItemMasterIdData(itemMasterId);
		}
	}
	
	private final class ItemDetailMapper implements RowMapper<InventoryItemDetails>{
		
		@Override
		public InventoryItemDetails mapRow(ResultSet rs, int rowNum)throws SQLException{
			
			Long id = rs.getLong("id");
			Long clientId = rs.getLong("clientId");
			
			return new InventoryItemDetails(id,clientId);
		}
	}  
	
	private class SerialNumberForItemMasterIDMapper implements RowMapper<Long>{
		@Override
		public Long mapRow(ResultSet rs,int rowNum)throws SQLException{
			Long itemMasterId = rs.getLong("itemMasterId");
			return itemMasterId;
		}
	}
	
private final class SerialNumberForValidation implements RowMapper<String>{
		
		@Override
		public String mapRow(ResultSet rs, int rowNum)throws SQLException{
			String serialNumber = rs.getString("serialNumber");
			return serialNumber;
		}
	}
	
	@Override
	public List<String> retriveSerialNumbers() {
		context.authenticatedUser();
		SerialNumberForValidation rowMapper = new SerialNumberForValidation();
		String sql = "select serial_no as serialNumber from b_item_detail item";
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	@Override
	public List<QuantityData> retriveSerialNumbers(Long oneTimeSaleId) {
		
		context.authenticatedUser();
		SerialNumberMapper rowMapper = new SerialNumberMapper();
		String sql = "select idt.serial_no as serialNumber from b_onetime_sale ots left join b_item_detail idt on idt.item_master_id = ots.item_id where ots.id = ? and idt.client_id is null";/*"select serial_no as serialNumber from b_item_detail where item_master_id=(select item_id from b_onetime_sale where id=?) and client_id is null";*/
		return this.jdbcTemplate.query(sql,rowMapper,new Object[]{oneTimeSaleId});
	}
	
	@Override
	public QuantityData retriveQuantity(Long oneTimeSaleId){
		context.authenticatedUser();
		QuantityMapper rowMapper = new QuantityMapper();
		String sql = "select ots.quantity as quantity from b_onetime_sale ots where ots.id = ?";/*String sql = "select ots.quantity as quantity from b_onetime_sale ots left join b_item_detail idt on idt.item_master_id = ots.item_id where ots.id = ? limit 1";*/
		return this.jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{oneTimeSaleId});
	}
	
	@Override
	public ItemMasterIdData retriveItemMasterId(Long oneTimeSaleId){
		context.authenticatedUser();
		ItemMasterMapper rowMapper = new ItemMasterMapper();
		String sql = "select idt.item_master_id as itemMasterId from b_onetime_sale ots left join b_item_detail idt on idt.item_master_id = ots.item_id where ots.id = ? limit 1";
		return this.jdbcTemplate.queryForObject(sql, rowMapper,new Object[]{oneTimeSaleId});
	}
	
	
	public InventoryItemDetails retriveInventoryItemDetail(String serialNumber,Long itemMasterId){
		
		try{
		context.authenticatedUser();
		ItemDetailMapper rowMapper = new ItemDetailMapper();
		String sql = "select id,client_id as clientId from b_item_detail i where i.serial_no=? and i.item_master_id=?";
		return this.jdbcTemplate.queryForObject(sql,rowMapper,new Object[]{serialNumber,itemMasterId});
		}catch(EmptyResultDataAccessException accessException){
			return null;
		}
	}
	
	@Override
	public InventoryItemSerialNumberData retriveAllocationData(List<QuantityData> itemSerialNumbers,QuantityData quantityData, ItemMasterIdData itemMasterIdData){
		
		return new InventoryItemSerialNumberData(itemSerialNumbers, quantityData.getQuantity(), itemMasterIdData.getItemMasterId());
	}


	@Override
	public List<Long> retriveSerialNumberForItemMasterId(String serialNumber) {
		SerialNumberForItemMasterIDMapper rowMapper = new SerialNumberForItemMasterIDMapper();
		String sql = "select i.item_master_id as itemMasterId from b_item_detail i where i.serial_no =?";
		return jdbcTemplate.query(sql, rowMapper, new Object[]{serialNumber});
	}	
	
	
	

}
