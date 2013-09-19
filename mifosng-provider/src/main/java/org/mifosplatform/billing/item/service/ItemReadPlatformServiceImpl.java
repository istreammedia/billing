package org.mifosplatform.billing.item.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.mifosplatform.billing.item.data.ChargesData;
import org.mifosplatform.billing.item.data.ItemData;
import org.mifosplatform.billing.item.data.ItemTypeData;
import org.mifosplatform.billing.item.data.UniteTypeData;
import org.mifosplatform.billing.item.domain.ItemEnumType;
import org.mifosplatform.billing.item.domain.UnitEnumType;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class ItemReadPlatformServiceImpl implements ItemReadPlatformService{
	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;

	@Autowired
	public ItemReadPlatformServiceImpl(final PlatformSecurityContext context,
			final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	


	@Override
	public List<EnumOptionData> retrieveItemClassType() {
	EnumOptionData hardware = ItemTypeData.ItemClassType(ItemEnumType.HARDWARE);
	EnumOptionData prepaidCard = ItemTypeData.ItemClassType(ItemEnumType.PREPAID_CARD);
	EnumOptionData softCharge = ItemTypeData.ItemClassType(ItemEnumType.SOFT_CHARGE);
	EnumOptionData event = ItemTypeData.ItemClassType(ItemEnumType.EVENT);
	List<EnumOptionData> categotyType = Arrays.asList(hardware, prepaidCard,softCharge,event);
	return categotyType;
	}

	@Override
	public List<EnumOptionData> retrieveUnitTypes() {
		EnumOptionData meters = UniteTypeData.UnitClassType(UnitEnumType.METERS);
		EnumOptionData numbers = UniteTypeData.UnitClassType(UnitEnumType.NUMBERS);
		EnumOptionData hours = UniteTypeData.UnitClassType(UnitEnumType.HOURS);
		List<EnumOptionData> categotyType = Arrays.asList(meters, numbers,hours);
		return categotyType;
	}



	@Override
	public List<ChargesData> retrieveChargeCode() {
		 String sql = "select s.id as id,s.charge_code as charge_code,s.charge_description as charge_description from b_charge_codes s  where s.charge_code='OTC'";


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
public List<ItemData> retrieveAllItems() {
	context.authenticatedUser();

	SalesDataMapper mapper = new SalesDataMapper();

	String sql = "select " + mapper.schema()+" where  i.is_deleted='n'";

	return this.jdbcTemplate.query(sql, mapper, new Object[] {  });
}

private static final class SalesDataMapper implements
		RowMapper<ItemData> {

	public String schema() {
		return "i.id AS id,i.item_code as itemCode,i.item_description AS itemDescription,i.item_class as itemClass,i.units AS units, "
			+" i.charge_code as chargeCode,i.unit_price as unitPrice,i.warranty as warranty FROM b_item_master i ";



	}

	@Override
	public ItemData mapRow(ResultSet rs, int rowNum)
			throws SQLException {

		Long id = rs.getLong("id");
		String itemCode = rs.getString("itemCode");
		String itemDescription = rs.getString("itemDescription");
		String itemClass = rs.getString("itemClass");
		String units = rs.getString("units");
		String chargeCode = rs.getString("chargeCode");
		BigDecimal unitPrice = rs.getBigDecimal("unitPrice");
		int warranty = rs.getInt("warranty");
		
		return new ItemData(id,itemCode,itemDescription,itemClass,units,chargeCode,warranty,unitPrice);
	

		

	}
	}


@Override
public ItemData retrieveSingleItemDetails(Long itemId) {
	context.authenticatedUser();

	SalesDataMapper mapper = new SalesDataMapper();

	String sql = "select " + mapper.schema()+" where i.id=? and  i.is_deleted='n'";

	return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] { itemId });
}
}


