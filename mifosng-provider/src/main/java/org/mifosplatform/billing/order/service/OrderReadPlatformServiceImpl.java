package org.mifosplatform.billing.order.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.contract.data.SubscriptionData;
import org.mifosplatform.billing.order.data.OrderData;
import org.mifosplatform.billing.order.data.OrderHistoryData;
import org.mifosplatform.billing.order.data.OrderPriceData;
import org.mifosplatform.billing.order.data.OrderStatusEnumaration;
import org.mifosplatform.billing.order.domain.OrderRepository;
import org.mifosplatform.billing.payterms.data.PaytermData;
import org.mifosplatform.billing.plan.data.PlanCodeData;
import org.mifosplatform.billing.plan.data.ServiceData;
import org.mifosplatform.billing.pricing.service.PriceReadPlatformService;
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
public class OrderReadPlatformServiceImpl implements OrderReadPlatformService

{


	  private final JdbcTemplate jdbcTemplate;
	    private final PlatformSecurityContext context;
	    private OrderRepository orderRepository;

        private  static  PriceReadPlatformService priceReadPlatformService;
	    @Autowired
	    public OrderReadPlatformServiceImpl(final PlatformSecurityContext context, final TenantAwareRoutingDataSource dataSource,
			final PriceReadPlatformService priceReadPlatformService,final OrderRepository repository) {
	        this.context = context;
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	        this.orderRepository=repository;
	       OrderReadPlatformServiceImpl.priceReadPlatformService=priceReadPlatformService;

	    }
	@Override
	public List<PlanCodeData> retrieveAllPlatformData() {
		  context.authenticatedUser();

	        String sql = "select s.id as id,s.plan_code as plan_code,s.is_prepaid as isPrepaid from b_plan_master s where s.plan_status=1 and  s.is_deleted='n'  ";

	        RowMapper<PlanCodeData> rm = new PeriodMapper();

	        return this.jdbcTemplate.query(sql, rm, new Object[] {});
	}

	 private static final class PeriodMapper implements RowMapper<PlanCodeData> {

	        @Override
	        public PlanCodeData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

	        Long id = rs.getLong("id");
	            String plan_code = rs.getString("plan_code");
	            String isPrepaid = rs.getString("isPrepaid");

	            List<ServiceData> services= priceReadPlatformService.retrievePrcingDetails(id);
	            return new PlanCodeData(id,plan_code,services,isPrepaid);

	        }


	 }

	@Override
	public List<PaytermData> retrieveAllPaytermData() {


		 context.authenticatedUser();

	        String sql = "select s.id as id,s.paymode_code as payterm_type,s.paymode_description as units from b_paymodes s";

	        RowMapper<PaytermData> rm = new PaytermMapper();

	        return this.jdbcTemplate.query(sql, rm, new Object[] {});
	}

	 private static final class PaytermMapper implements RowMapper<PaytermData> {

	        @Override
	        public PaytermData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

	        Long id = rs.getLong("id");
	            String paytermtype = rs.getString("payterm_type");
	            String units = rs.getString("units");
                 String data=units.concat(paytermtype);
	            return new PaytermData(id,data,null,null);
	        }


	}


	@Override
	public List<OrderPriceData> retrieveOrderPriceData(Long orderId) {
		 context.authenticatedUser();

	        /*String sql = "select s.id as id,s.order_id as order_id,s.charge_code as charge_code,s.service_id as service_id,s.charge_type as charge_type,s.charge_duration as charge_duration,"
                    +"s.duration_type as duration_type,s.price as price from order_price s where s.order_id = ?";*/

	      
	        
	        RowMapper<OrderPriceData> rm = new OrderPriceMapper();

	        final OrderPriceMapper orderPriceMapper=new OrderPriceMapper();
	        
	        String sql = "select " + orderPriceMapper.schema();

	        return this.jdbcTemplate.query(sql, rm, new Object[] { orderId });
	}

	 private static final class OrderPriceMapper implements RowMapper<OrderPriceData> {
		 public String schema() {
			return  "s.id as id,s.order_id as order_id,s.charge_code as charge_code,s.service_id as service_id,s.charge_type as charge_type,s.charge_duration as charge_duration,"
	                    +"s.duration_type as duration_type,s.price as price from b_order_price s where s.order_id = ?";
			 
		 }

	        @Override
	        public OrderPriceData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {


			  Long id = rs.getLong("id");
			  Long orderId = rs.getLong("order_id");
			  Long serviceId = rs.getLong("service_id");
	            String chargeCode = rs.getString("charge_code");
	            String chargeType = rs.getString("charge_type");
	            String chargeDuration = rs.getString("charge_duration");
	            String durationtype = rs.getString("duration_type");
	            BigDecimal price=rs.getBigDecimal("price");

	            return new OrderPriceData(id,orderId,serviceId,chargeCode,chargeType,chargeDuration,durationtype,price, null, null, null, null,null,null);
	        }

	}

	
	/*@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void retrieveInvoice(Long clientId) {
		 context.authenticatedUser();

	        String sql = "select *from b_bill_master b,b_bill_details be,b_client_address cd,m_client mc where b.id = bill_id and b.client_id=cd.client_id and mc.id=cd.client_id and mc.id ="+clientId+" ;";



	       this.jdbcTemplate.query(sql, new invoice(), new Object[] {});
	}*/

	/*
	private static final class invoice<T> implements RowMapper<T> {

	        @Override
	        public T mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

			try
			{
			String fileLocation="D:"+File.separator+"invoice"+File.separator;
				if (!new File(fileLocation).isDirectory()) {
					   new File(fileLocation).mkdirs();
					  }

			Document document = new Document();

			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("invoice"+rs.getInt("bill_no")+".pdf"));


			document.open();
			PdfContentByte pdfContentByte = writer.getDirectContent();
			Font b = new Font(Font.BOLD + Font.BOLD,8);
			Font b1 = new Font(Font.BOLD + Font.UNDERLINE + Font.BOLDITALIC+Font.TIMES_ROMAN,8);


			pdfContentByte.beginText();

			PdfPTable table = new PdfPTable(11);
			table.setWidthPercentage(100);

			PdfPCell cell1 = new PdfPCell(
					(new Paragraph("Statment of Invoice", FontFactory.getFont(FontFactory.HELVETICA,12, Font.BOLD))));
			cell1.setColspan(11);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell1.setPadding(10.0f);
			table.addCell(cell1);
			PdfPCell cell = new PdfPCell();
			cell.setColspan(2);
			Paragraph para = new Paragraph("Name           :", b1);
			Paragraph add = new Paragraph("  ", b);
			Paragraph addr = new Paragraph("Address        :", b);
			Paragraph branch = new Paragraph("Branch       :", b);
			branch.setSpacingBefore(12);

			cell.addElement(para);
			cell.addElement(addr);
			cell.addElement(branch);
			cell.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell);
			PdfPCell cell0 = new PdfPCell();
			Paragraph add0 = new Paragraph(""+rs.getString("firstname")+"   "+rs.getString("lastname")+"", b);
			Paragraph add1 = new Paragraph(""+rs.getString("address_no")+","+rs.getString("street"), b);
			add1.setSpacingBefore(10);
			Paragraph add2 = new Paragraph(""+rs.getString("city")+","+rs.getString("state")+"-"+rs.getString("zip"), b);
			cell0.setColspan(4);
			cell0.disableBorderSide(PdfPCell.LEFT);
			cell0.addElement(add0);
			cell0.addElement(add1);
			cell0.addElement(add2);
			table.addCell(cell0);

			 Image image = Image.getInstance("logo.jpg");
			 image.scaleAbsolute(60,60);
			 PdfPCell cell2 = new PdfPCell();
			 cell2.addElement(image);
			 cell2.disableBorderSide(PdfPCell.TOP);
				 cell2.disableBorderSide(PdfPCell.BOTTOM);
				 cell2.disableBorderSide(PdfPCell.LEFT);
				cell2.disableBorderSide(PdfPCell.RIGHT);
			cell2.setColspan(2);
			table.addCell(cell2);
			 PdfPCell cell02 = new PdfPCell();
			 Paragraph addr1 = new Paragraph("Hugo Technologies LLP",
				       FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD,
								       new CMYKColor(0, 255, 255,17)));
			 Paragraph addr2 = new Paragraph("# 501, Sai Balaji Cubicles,", b);
			 Paragraph addr3 = new Paragraph("Raghavendra Society, Kondapur,", b);
			 Paragraph addr4 = new Paragraph(" Hyderabad - 500 084, AP, India.",b);
			 Paragraph addr5 = new Paragraph(" Tel:	+91-40-65141823",b);
			 Paragraph addr6 = new Paragraph("www.hugotechnologies.com",b);
			 cell02.addElement(addr1);
			 cell02.addElement(addr2);
			 cell02.addElement(addr3);
			 cell02.addElement(addr4);
			 cell02.addElement(addr5);
			 cell02.addElement(addr6);

			 cell02.disableBorderSide(PdfPCell.TOP);
			 cell02.disableBorderSide(PdfPCell.BOTTOM);
			 cell02.disableBorderSide(PdfPCell.LEFT);
			cell2.disableBorderSide(PdfPCell.RIGHT);
			cell02.setColspan(3);
			table.addCell(cell02);
			PdfPCell cell3 = new PdfPCell();
			// cell3.setPadding (1.0f);
			Paragraph BillId = new Paragraph("Bill Id:   " + rs.getInt("id"), b);
			cell3.setColspan(6);
			cell3.addElement(BillId);
			cell3.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell3);
			PdfPCell cell12 = new PdfPCell();
			Paragraph billNo = new Paragraph("billNo:"
					+ rs.getString("bill_no"), b);
			// billNo.setIndentationLeft(280);
			Paragraph billDate = new Paragraph("Bill Date:"
					+ JdbcSupport.getLocalDate(rs, "bill_date"), b);
			// billDate.setIndentationLeft(280);
			Paragraph BillPeriod = new Paragraph("Bill Period:"
					+ rs.getString("bill_Period"), b);
			// BillPeriod.setIndentationLeft(280);
			Paragraph dueDate = new Paragraph("Due Date:"
					+ JdbcSupport.getLocalDate(rs, "due_date"), b);
			// dueDate.setIndentationLeft(280);

			// cell12.disableBorderSide(PdfPCell.TOP);
			// cell12.disableBorderSide(PdfPCell.BOTTOM);
			cell12.disableBorderSide(PdfPCell.LEFT);
			// cell12.disableBorderSide(PdfPCell.RIGHT);
			cell12.addElement(billNo);
			cell12.addElement(billDate);
			cell12.addElement(BillPeriod);
			cell12.setColspan(5);
			cell12.addElement(dueDate);
			table.addCell(cell12);
			PdfPCell cell4 = new PdfPCell();

			Paragraph previousbal = new Paragraph("Previous Balance:", b);
			Paragraph previousamount = new Paragraph(""
					+ rs.getDouble("previous_balance"), b);
			cell4.setColspan(2);
			cell4.addElement(previousbal);
			cell4.addElement(previousamount);
			cell4.disableBorderSide(PdfPCell.TOP);
			// cell5.disableBorderSide(PdfPCell.BOTTOM);
			// cell4.disableBorderSide(PdfPCell.LEFT);
			cell4.disableBorderSide(PdfPCell.RIGHT);

			table.addCell(cell4);
			pdfContentByte.setTextMatrix(390, 405);

			PdfPCell cell5 = new PdfPCell();
			Paragraph adjstment = new Paragraph("Adjustment Amount:", b);
			Paragraph adjstmentamount = new Paragraph(""
					+ rs.getDouble("adjustment_amount"), b);
			cell5.setColspan(2);
			cell5.addElement(adjstment);
			cell5.addElement(adjstmentamount);
			cell5.disableBorderSide(PdfPCell.TOP);
			// cell5.disableBorderSide(PdfPCell.BOTTOM);
			cell5.disableBorderSide(PdfPCell.LEFT);
			cell5.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell5);

			PdfPCell cell6 = new PdfPCell();
			Paragraph paid_amount = new Paragraph("paid_amount:", b);
			Paragraph amount = new Paragraph("" + rs.getDouble("paid_amount"),
					b);
			cell6.setColspan(2);
			cell6.addElement(paid_amount);
			cell6.addElement(amount);
			cell6.disableBorderSide(PdfPCell.TOP);
			// cell5.disableBorderSide(PdfPCell.BOTTOM);
			cell6.disableBorderSide(PdfPCell.LEFT);
			cell6.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell6);

			PdfPCell cell7 = new PdfPCell();
			Paragraph charge_amount = new Paragraph("Charge Amount:", b);
			Paragraph chargeamount = new Paragraph(""
					+ rs.getDouble("charges_amount"), b);
			cell7.setColspan(2);
			cell7.addElement(charge_amount);
			cell7.addElement(chargeamount);

			cell7.disableBorderSide(PdfPCell.TOP);
			// cell5.disableBorderSide(PdfPCell.BOTTOM);
			cell7.disableBorderSide(PdfPCell.LEFT);
			cell7.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell7);

			PdfPCell cell8 = new PdfPCell();
			Paragraph due_amount = new Paragraph("Due Amount:", b);
			Paragraph dueamount = new Paragraph(
					"" + rs.getDouble("due_amount"), b);
			cell8.setColspan(3);
			cell8.addElement(due_amount);
			cell8.addElement(dueamount);

			cell8.disableBorderSide(PdfPCell.TOP);
			// cell5.disableBorderSide(PdfPCell.BOTTOM);
			cell8.disableBorderSide(PdfPCell.LEFT);
			// cell8.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell8);

			PdfPCell cell9 = new PdfPCell();
			Paragraph invoice = new Paragraph("Invoice Charges:", b);
			// cell9.disableBorderSide(PdfPCell.LEFT);
			cell9.disableBorderSide(PdfPCell.RIGHT);
			cell9.addElement(invoice);
			cell9.setColspan(4);
			table.addCell(cell9);
			PdfPCell cell10 = new PdfPCell();
			Paragraph name = new Paragraph("User Name:", b);
			cell10.disableBorderSide(PdfPCell.LEFT);
			cell10.disableBorderSide(PdfPCell.RIGHT);
			cell10.setColspan(4);
			cell10.addElement(name);
			table.addCell(cell10);

			PdfPCell cell11 = new PdfPCell();
			Paragraph Uname = new Paragraph("User:", b);
			cell11.setColspan(3);
			cell11.disableBorderSide(PdfPCell.LEFT);
			cell10.disableBorderSide(PdfPCell.RIGHT);
			cell11.addElement(Uname);
			table.addCell(cell11);

			PdfPCell cell13 = new PdfPCell();
			Paragraph bill_No = new Paragraph("Bill No", b);
			cell13.addElement(bill_No);
			cell13.setColspan(1);
			table.addCell(cell13);

			PdfPCell cell14 = new PdfPCell();
			Paragraph periodno = new Paragraph("Period", b);
			cell14.addElement(periodno);
			cell14.setColspan(1);
			table.addCell(cell14);

			PdfPCell cell15 = new PdfPCell();
			Paragraph service = new Paragraph("Service Type", b);
			cell15.addElement(service);
			cell15.setColspan(1);
			table.addCell(cell15);

			PdfPCell cell16 = new PdfPCell();
			Paragraph desc = new Paragraph("Description", b);
			cell16.addElement(desc);
			cell16.setColspan(1);
			table.addCell(cell16);

			PdfPCell cell18 = new PdfPCell();
			Paragraph pack = new Paragraph("Package", b);
			cell18.addElement(pack);
			cell18.setColspan(1);
			table.addCell(cell18);

			PdfPCell cell17 = new PdfPCell();
			Paragraph rate = new Paragraph("Rate", b);
			cell17.addElement(rate);
			cell17.setColspan(1);
			table.addCell(cell17);

			PdfPCell cell22 = new PdfPCell();
			Paragraph quantity = new Paragraph("Quantity", b);
			cell22.addElement(quantity);
			cell22.setColspan(2);
			table.addCell(cell22);

			PdfPCell cell19 = new PdfPCell();
			Paragraph amount1 = new Paragraph("Amount", b);
			cell19.addElement(amount1);
			cell16.setColspan(1);
			table.addCell(cell19);

			PdfPCell cell20 = new PdfPCell();
			Paragraph tax = new Paragraph("Tax", b);
			cell20.addElement(tax);
			cell20.setColspan(1);
			table.addCell(cell20);

			PdfPCell cell21 = new PdfPCell();
			Paragraph total = new Paragraph("Total", b);
			cell21.addElement(total);
			cell21.setColspan(2);
			table.addCell(cell21);

			PdfPCell cell23 = new PdfPCell();
			Paragraph billid = new Paragraph("" + rs.getString("bill_no"), b);
			cell23.addElement(billid);
			cell23.setColspan(1);
			table.addCell(cell23);

			PdfPCell cell24 = new PdfPCell();
			Paragraph period = new Paragraph("" + rs.getString("bill_Period"),
					b);
			cell24.addElement(period);
			cell24.setColspan(1);
			table.addCell(cell24);

			PdfPCell cell25 = new PdfPCell();
			Paragraph trans = new Paragraph(""
					+ rs.getString("transaction_type"), b);
			cell25.addElement(trans);
			cell25.setColspan(1);
			table.addCell(cell25);

			PdfPCell cell26 = new PdfPCell();
			Paragraph description = new Paragraph(""
					+ rs.getString("description"), b);
			cell26.addElement(description);
			cell26.setColspan(1);
			table.addCell(cell26);

			PdfPCell cell27 = new PdfPCell();
			Paragraph pack1 = new Paragraph("" + rs.getString("plan_code"), b);
			cell27.addElement(pack1);
			cell27.setColspan(1);
			table.addCell(cell27);

			PdfPCell cell28 = new PdfPCell();
			Paragraph rate1 = new Paragraph("" + rs.getString("amount"), b);
			cell28.addElement(rate1);
			cell28.setColspan(1);
			table.addCell(cell28);

			PdfPCell cell29 = new PdfPCell();
			Paragraph period1 = new Paragraph("" + rs.getString("bill_Period"),
					b);
			cell29.addElement(period1);
			cell29.setColspan(2);
			table.addCell(cell29);

			PdfPCell cell30 = new PdfPCell();
			Paragraph camount = new Paragraph(""
					+ rs.getDouble("charges_amount"), b);
			cell30.addElement(camount);
			cell30.setColspan(1);
			table.addCell(cell30);

			PdfPCell cell31 = new PdfPCell();
			Paragraph tamount = new Paragraph("" + rs.getDouble("tax_amount"),
					b);
			cell31.addElement(tamount);
			cell31.setColspan(1);
			table.addCell(cell31);

			PdfPCell cell32 = new PdfPCell();
			Paragraph bamount = new Paragraph("" + rs.getDouble("amount"), b);
			cell32.addElement(bamount);
			cell32.setColspan(1);
			table.addCell(cell32);

			PdfPCell cell33 = new PdfPCell();
			Paragraph totalBal = new Paragraph("Total:", b);
			Paragraph totalinv = new Paragraph("Total Invoice Amount:", b);

			cell33.addElement(totalBal);
			cell33.addElement(totalinv);
			cell33.setColspan(8);
			totalBal.setIndentationLeft(333);
			totalinv.setIndentationLeft(280);
			cell33.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell33);

			PdfPCell cell34 = new PdfPCell();

			cell34.addElement(bamount);
			cell34.addElement(bamount);
			cell34.setColspan(3);
			cell34.disableBorderSide(PdfPCell.LEFT);
			table.addCell(cell34);

			PdfPCell cell35 = new PdfPCell();
			Paragraph title = new Paragraph(
					"Payments/Adjustments/Discounts/Deposits/RefundsOtherCharges:",
					b1);
			cell35.addElement(title);
			cell35.setColspan(11);

			// cell35.disableBorderSide(PdfPCell.TOP);
			cell35.disableBorderSide(PdfPCell.BOTTOM);
			// cell35.disableBorderSide(PdfPCell.LEFT);
			// cell35.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell35);

			PdfPCell cell38 = new PdfPCell();
			Paragraph em = new Paragraph("", b);
			cell38.disableBorderSide(PdfPCell.TOP);
			cell38.disableBorderSide(PdfPCell.RIGHT);
			cell38.disableBorderSide(PdfPCell.BOTTOM);
			cell38.addElement(em);
			cell38.setColspan(2);
			table.addCell(cell38);

			PdfPCell cell36 = new PdfPCell();
			Paragraph ref = new Paragraph("AccountNo:", b);
			cell36.disableBorderSide(PdfPCell.TOP);
			cell36.disableBorderSide(PdfPCell.LEFT);
			cell36.disableBorderSide(PdfPCell.RIGHT);
			cell36.disableBorderSide(PdfPCell.BOTTOM);
			cell36.addElement(ref);
			cell36.setColspan(3);
			table.addCell(cell36);

			PdfPCell cell37 = new PdfPCell();
			Paragraph uname = new Paragraph("UserName:", b);
			cell37.disableBorderSide(PdfPCell.TOP);
			cell37.disableBorderSide(PdfPCell.LEFT);
			cell37.disableBorderSide(PdfPCell.BOTTOM);
			cell37.addElement(uname);
			cell37.setColspan(6);
			table.addCell(cell37);

			PdfPCell cell39 = new PdfPCell();
			Paragraph ref1 = new Paragraph("RefNo:", b);
			cell39.disableBorderSide(PdfPCell.TOP);
			// cell39.disableBorderSide(PdfPCell.LEFT);
			cell39.disableBorderSide(PdfPCell.RIGHT);
			cell39.addElement(ref1);
			cell39.setColspan(2);
			table.addCell(cell39);

			PdfPCell cell40 = new PdfPCell();
			Paragraph details = new Paragraph("Details:", b);
			cell40.disableBorderSide(PdfPCell.TOP);
			cell40.disableBorderSide(PdfPCell.LEFT);
			cell40.disableBorderSide(PdfPCell.RIGHT);
			cell40.addElement(details);
			cell40.setColspan(2);
			table.addCell(cell40);

			PdfPCell cell41 = new PdfPCell();
			Paragraph pol = new Paragraph("Polarity:", b);
			cell41.disableBorderSide(PdfPCell.TOP);
			cell41.disableBorderSide(PdfPCell.LEFT);
			cell41.disableBorderSide(PdfPCell.RIGHT);
			cell41.addElement(pol);
			cell41.setColspan(2);
			table.addCell(cell41);

			PdfPCell cell42 = new PdfPCell();
			Paragraph am = new Paragraph("Amount:", b);
			cell42.disableBorderSide(PdfPCell.TOP);
			cell42.disableBorderSide(PdfPCell.LEFT);
			cell42.disableBorderSide(PdfPCell.RIGHT);
			cell42.addElement(am);
			cell42.setColspan(1);
			table.addCell(cell42);

			PdfPCell cell43 = new PdfPCell();

			cell43.disableBorderSide(PdfPCell.TOP);
			cell43.disableBorderSide(PdfPCell.LEFT);
			cell43.disableBorderSide(PdfPCell.RIGHT);
			cell43.addElement(tax);
			cell43.setColspan(1);
			table.addCell(cell43);

			PdfPCell cell44 = new PdfPCell();

			cell44.disableBorderSide(PdfPCell.TOP);
			cell44.disableBorderSide(PdfPCell.LEFT);
			cell44.disableBorderSide(PdfPCell.RIGHT);
			cell44.addElement(total);
			cell44.setColspan(1);
			table.addCell(cell44);

			PdfPCell cell45 = new PdfPCell();
			Paragraph re = new Paragraph("Remarks:", b);
			cell45.disableBorderSide(PdfPCell.TOP);
			cell45.disableBorderSide(PdfPCell.LEFT);

			cell45.addElement(re);
			cell45.setColspan(2);
			table.addCell(cell45);

			PdfPCell cell46 = new PdfPCell();

			cell46.setFixedHeight(25f);
			cell46.setColspan(11);
			table.addCell(cell46);

			PdfPCell cell47 = new PdfPCell();

			Paragraph para2 = new Paragraph(":", b);
			Paragraph toal3 = new Paragraph("Total:", b);

			para2.setIndentationLeft(333);
			toal3.setIndentationLeft(318);
			cell47.addElement(para2);
			cell47.addElement(toal3);
			cell47.setColspan(11);
			table.addCell(cell47);

			table.addCell("Delhi");
			table.addCell("RoseIndia");
			table.addCell("Delhi");
			pdfContentByte.endText();
			document.add(table);
			document.close();
			Runtime.getRuntime().exec(
					"rundll32 url.dll,FileProtocolHandler invoice"+rs.getInt("bill_no")+".pdf");



return null;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
	   }     }
	*/


	@Override
	public List<PaytermData> getChargeCodes(Long planCode) {

		 context.authenticatedUser();

	        String sql = "SELECT DISTINCT b.billfrequency_code AS billfrequency_code, b.id AS id,pm.duration as duration,pm.is_prepaid as isPrepaid" +
	        		     " FROM b_plan_pricing a, b_charge_codes b,b_plan_master pm  WHERE a.charge_code = b.charge_code" +
	        		     " AND a.is_deleted = 'n' AND plan_id = ? and pm.id = a.plan_id";


	        RowMapper<PaytermData> rm = new BillingFreaquencyMapper();


	        return this.jdbcTemplate.query(sql, rm, new Object[] { planCode });
	}

	 private static final class BillingFreaquencyMapper implements RowMapper<PaytermData> {

	        @Override
	        public PaytermData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {


			  Long id = rs.getLong("id");

	            String serviceType = rs.getString("billfrequency_code");
	            String duration = rs.getString("duration");
	            String isPrepaid = rs.getString("isPrepaid");

	            return new PaytermData(id,serviceType,duration,isPrepaid);
	}
	}

	
	@Override
	public List<OrderPriceData> retrieveOrderPriceDetails(Long orderId,Long clientId) {
		 RowMapper<OrderPriceData> rm = new OrderPriceDataMapper();

	      	        
	        String sql = "SELECT p.id AS id,o.client_id AS clientId,p.order_id AS order_id,c.charge_description AS chargeDescription,"
	        		+"s.service_description AS serviceDescription,p.charge_type AS charge_type,p.charge_duration AS chargeDuration, p.duration_type AS durationType,"
	        		+"p.price AS price,p.bill_start_date as billStartDate,p.bill_end_date as billEndDate,p.next_billable_day as nextBillableDay,p.invoice_tilldate as invoiceTillDate,"
	        		+"  o.billing_align as billingAlign, o.billing_frequency as billingFrequency FROM b_order_price p,b_charge_codes c,b_service s, b_orders o "
	        		+"WHERE p.charge_code = c.charge_code AND p.service_id = s.id  AND o.id = p.order_id  AND p.order_id =?";

	        return this.jdbcTemplate.query(sql, rm, new Object[] { orderId });
	}
	private static final class OrderPriceDataMapper implements RowMapper<OrderPriceData> {

        @Override
        public OrderPriceData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {


        	  Long id = rs.getLong("id");
			  Long orderId = rs.getLong("order_id");
			  Long clientId = rs.getLong("clientId");
	            String chargeCode = rs.getString("serviceDescription");
	            String chargeType = rs.getString("chargeDescription");
	            String chargeDuration = rs.getString("chargeDuration");
	            String durationtype = rs.getString("durationType");
	            String billingAlign = rs.getString("billingAlign");
	            String billingFrequency = rs.getString("billingFrequency");
	            BigDecimal price=rs.getBigDecimal("price");
	            LocalDate billStartDate=JdbcSupport.getLocalDate(rs,"billStartDate");
	            LocalDate billEndDate=JdbcSupport.getLocalDate(rs,"billEndDate");
	            LocalDate nextBillDate=JdbcSupport.getLocalDate(rs,"nextBillableDay");
	            LocalDate invoiceTillDate=JdbcSupport.getLocalDate(rs,"invoiceTillDate");
	            
	            
	            return new OrderPriceData(id,orderId,clientId,chargeCode,chargeType,chargeDuration,durationtype,price,billStartDate,billEndDate,nextBillDate,invoiceTillDate,billingAlign,billingFrequency);
	        
}

}

	@Override
	public List<org.mifosplatform.billing.order.data.OrderData> retrieveClientOrderDetails(
			Long clientId) {
		try {
			final ClientOrderMapper mapper = new ClientOrderMapper();

			final String sql = "select " + mapper.clientOrderLookupSchema()+" where o.plan_id = p.id and o.client_id= ? and o.is_deleted='n' and o.contract_period = co.id order by o.id desc";

			return jdbcTemplate.query(sql, mapper, new Object[] { clientId});
			} catch (EmptyResultDataAccessException e) {
			return null;
			}

			}

			private static final class ClientOrderMapper implements RowMapper<OrderData> {

			public String clientOrderLookupSchema() {
			return "o.id AS id,o.plan_id AS plan_id, o.start_date AS start_date,o.order_status AS order_status,p.plan_code AS plan_code,"
					+"o.end_date AS end_date,co.contract_period as contractPeriod ,o.user_action AS userAction,p.is_prepaid as isprepaid,p.allow_topup as allowTopUp,  " +
					"(SELECT sum(ol.price) AS price FROM b_order_price ol"
					+" WHERE o.id = ol.order_id)  AS price  FROM b_orders o, b_plan_master p,b_contract_period co";
			}

			@Override
			public OrderData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

			final Long id = rs.getLong("id");
			final Long planId = rs.getLong("plan_id");
			final String plancode = rs.getString("plan_code");
			final String contractPeriod = rs.getString("contractPeriod");
			final int statusId = rs.getInt("order_status");
			
			LocalDate startDate=JdbcSupport.getLocalDate(rs,"start_date");
			LocalDate endDate=JdbcSupport.getLocalDate(rs,"end_date");
			final double price=rs.getDouble("price");
            final String isprepaid=rs.getString("isprepaid");
            final String allowtopup=rs.getString("allowTopUp");
            final String userAction=rs.getString("userAction");
			EnumOptionData Enumstatus=OrderStatusEnumaration.OrderStatusType(statusId);
			String status=Enumstatus.getValue();

			return new OrderData(id, planId, plancode, status, startDate,endDate,price,contractPeriod,isprepaid,allowtopup,userAction);
			}
			}

			@Override
			public List<OrderHistoryData> retrieveOrderHistoryDetails(
					Long orderId) {
				

				try {
					final OrderHistoryMapper mapper = new OrderHistoryMapper();

					final String sql = "select " + mapper.clientOrderLookupSchema();

					return jdbcTemplate.query(sql, mapper, new Object[] { orderId});
					} catch (EmptyResultDataAccessException e) {
					return null;
					}

					}

					private static final class OrderHistoryMapper implements RowMapper<OrderHistoryData> {

					public String clientOrderLookupSchema() {
					return " h.id AS id,h.transaction_date AS transDate,h.actual_date AS actualDate,h.transaction_type AS transactionType," +
							"h.prepare_id AS PrepareRequsetId  FROM b_orders_history h  where h.order_id =?";
					}

					@Override
					public OrderHistoryData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

					final Long id = rs.getLong("id");
					final LocalDate transDate=JdbcSupport.getLocalDate(rs,"transDate");
					final LocalDate actualDate=JdbcSupport.getLocalDate(rs,"actualDate");
					final LocalDate provisionongDate=JdbcSupport.getLocalDate(rs,"actualDate");
					final String transactionType=rs.getString("transactionType");
					final Long PrepareRequsetId=rs.getLong("PrepareRequsetId");

					return new OrderHistoryData(id,transDate,actualDate,provisionongDate,transactionType,PrepareRequsetId );
					}
			}

					@Override
					public List<OrderData> getActivePlans(Long clientId,	String planType) {
						

						try {
							final ActivePlanMapper mapper = new ActivePlanMapper();
							
							 String sql =null;
							if(planType!=null)
							  {
								if(planType.equalsIgnoreCase("prepaid")){
									  sql = "select " + mapper.activePlanLookupSchema()+" AND p.is_prepaid = 'Y'";
								}else{
									  sql = "select " + mapper.activePlanLookupSchema()+" AND p.is_prepaid = 'N'";
								}
							  }else{
								    sql = "select " + mapper.activePlanLookupSchema();
							  }

							

							return jdbcTemplate.query(sql, mapper, new Object[] { clientId});
							} catch (EmptyResultDataAccessException e) {
							return null;
							}

							}

					private static final class ActivePlanMapper implements RowMapper<OrderData> {

						public String activePlanLookupSchema() {
						return "o.id AS orderId,p.plan_code AS planCode,p.plan_description as planDescription,o.billing_frequency AS billingFreq," +
								"c.contract_period as contractPeriod,(SELECT sum(ol.price) AS price FROM b_order_price ol"
					+" WHERE o.id = ol.order_id)  AS price  FROM b_orders o, b_plan_master p, b_contract_period c WHERE client_id =?" +
								" AND p.id = o.plan_id  and o.contract_period=c.id ";
						}

						@Override
						public OrderData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

						final Long orderId = rs.getLong("orderId");
						final String planCode=rs.getString("planCode");
						final String planDescription=rs.getString("planDescription");
						final String billingFreq=rs.getString("billingFreq");
						final String contractPeriod=rs.getString("contractPeriod");
						final Double price=rs.getDouble("price");
						

						return new OrderData(orderId,planCode,planDescription,billingFreq,contractPeriod,price);
						}
				}

			
	}

