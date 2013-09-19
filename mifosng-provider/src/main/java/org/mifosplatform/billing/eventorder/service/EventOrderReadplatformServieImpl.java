package org.mifosplatform.billing.eventorder.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.eventorder.data.EventOrderData;
import org.mifosplatform.billing.onetimesale.data.OneTimeSaleData;
import org.mifosplatform.billing.order.data.OrderStatusEnumaration;
import org.mifosplatform.billing.paymode.data.SavingCategoryEnumaration;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Service;

@Service
public class EventOrderReadplatformServieImpl implements EventOrderReadplatformServie {

	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;
	  private CustomValidationProcedure myStoredProcedure; 

	@Autowired
	public EventOrderReadplatformServieImpl(final PlatformSecurityContext context,final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		 this. myStoredProcedure=new CustomValidationProcedure(this.jdbcTemplate.getDataSource());

	}

	

	@Override
	public List<OneTimeSaleData> retrieveEventOrderData(Long clientId) {
		EventOrderDataMapper mapper = new EventOrderDataMapper();

		String sql = "select " + mapper.schema()
				+ " where e.client_id = ? ";

		return this.jdbcTemplate.query(sql, mapper, new Object[] { clientId });
	}

	private static final class EventOrderDataMapper implements
			RowMapper<OneTimeSaleData> {

		public String schema() {
			return " e.id as orderid,e.client_id as clientId,e.event_id as eventId,e.eventprice_id as eventpriceId,0 as movieLink,e.booked_price as bookedPrice,"
			     +" e.charge_code as chargeCode,e.is_invoiced as isInvoiced from b_eventorder e ";

		}

		@Override
		public OneTimeSaleData mapRow(ResultSet rs, int rowNum)
				throws SQLException {

			Long orderid = rs.getLong("orderid");
			Long clientId = rs.getLong("clientId");
			String chargeCode = rs.getString("chargeCode");
			BigDecimal bookedPrice = rs.getBigDecimal("bookedPrice");
			String isInvoiced = rs.getString("isInvoiced");
			return new OneTimeSaleData(orderid,clientId, null, chargeCode, null,null, bookedPrice,isInvoiced,orderid,1l);

		}
	}

	@Override
	public boolean CheckClientCustomalidation(Long clientId) {
		 Map<String, Object> string= myStoredProcedure.execute(clientId);
	       
	       return  ((Boolean) string.get("status")).booleanValue();
	     
	}



	@Override
	public List<EventOrderData> getTheClientEventOrders(Long clientId) {
		
		try{
		EventOrderMapper mapper = new EventOrderMapper();
		String sql = "select " + mapper.schema()
				+ " and eo.client_id = ? ";

		return this.jdbcTemplate.query(sql, mapper, new Object[] { clientId });
	}catch(EmptyResultDataAccessException accessException){
	           return null;	
	}
	}

	private static final class EventOrderMapper implements
			RowMapper<EventOrderData> {

		public String schema() {
			return " eo.id AS id,eo.event_bookeddate AS bookedDate,em.event_name AS eventName,eo.charge_code AS chargeCode,eo.event_status as status,eo.booked_price AS price" +
					" FROM b_eventorder eo, b_event_master em WHERE eo.event_id = em.id ";

		}

		@Override
		public EventOrderData mapRow(ResultSet rs, int rowNum)
				throws SQLException {

			Long orderid = rs.getLong("id");
			LocalDate bookedDate = JdbcSupport.getLocalDate(rs,"bookedDate");
			String eventName = rs.getString("eventName");
			BigDecimal bookedPrice = rs.getBigDecimal("price");
			String chargeCode = rs.getString("chargeCode");
			int statusId = rs.getInt("status");
			EnumOptionData Enumstatus=OrderStatusEnumaration.OrderStatusType(statusId);
			String status=Enumstatus.getValue();
			return new EventOrderData(orderid,bookedDate,eventName,bookedPrice,chargeCode,status);

		}
		}
}
@SuppressWarnings("unused")
final class CustomValidationProcedure extends StoredProcedure {
        
  final static String SQL ="custom_validate_eventorders";//.getProcedureName();

	public CustomValidationProcedure(DataSource dataSource) {
		 super(dataSource,SQL);  
		 declareParameter( new SqlParameter( "clientid", Types.INTEGER) ); //declaring sql in parameter to pass input   
	      declareParameter( new SqlOutParameter( "status", Types.BOOLEAN ) ); //declaring sql out parameter   
	      compile();     
        }
		
			@SuppressWarnings("rawtypes")
		public Map execute(int emp_id) {
				//MyStoredProcedure myStoredProcedure;
				  Map<String, Object> results = super.execute(emp_id);   
				   return (Map) results.get("name"); 
    }
			}
