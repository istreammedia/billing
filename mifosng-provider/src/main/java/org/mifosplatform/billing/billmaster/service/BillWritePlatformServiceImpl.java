package org.mifosplatform.billing.billmaster.service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.mifosplatform.billing.adjustment.domain.Adjustment;
import org.mifosplatform.billing.adjustment.domain.AdjustmentRepository;
import org.mifosplatform.billing.billingorder.data.BillDetailsData;
import org.mifosplatform.billing.billingorder.domain.BillingOrder;
import org.mifosplatform.billing.billingorder.domain.BillingOrderRepository;
import org.mifosplatform.billing.billingorder.domain.Invoice;
import org.mifosplatform.billing.billingorder.domain.InvoiceRepository;
import org.mifosplatform.billing.billingorder.domain.InvoiceTax;
import org.mifosplatform.billing.billingorder.domain.InvoiceTaxRepository;
import org.mifosplatform.billing.billmaster.domain.BillDetail;
import org.mifosplatform.billing.billmaster.domain.BillDetailRepository;
import org.mifosplatform.billing.billmaster.domain.BillMaster;
import org.mifosplatform.billing.billmaster.domain.BillMasterRepository;
import org.mifosplatform.billing.clientbalance.domain.ClientBalanceRepository;
import org.mifosplatform.billing.financialtransaction.data.FinancialTransactionsData;
import org.mifosplatform.billing.payments.domain.Payment;
import org.mifosplatform.billing.payments.domain.PaymentRepository;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.service.FileUtils;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class BillWritePlatformServiceImpl implements BillWritePlatformService {
	private final PlatformSecurityContext context;
	private final BillMasterRepository billMasterRepository;
	private final BillDetailRepository billDetailRepository;
	private final PaymentRepository paymentRepository;
	private final AdjustmentRepository adjustmentRepository;
	private final BillingOrderRepository billingOrderRepository;
	private final InvoiceTaxRepository invoiceTaxRepository;
	private final InvoiceRepository invoiceRepository;
	private final ClientBalanceRepository clientBalanceRepository;
	private final JdbcTemplate jdbcTemplate;
	private final TenantAwareRoutingDataSource dataSource;
	@Autowired
	public BillWritePlatformServiceImpl(final PlatformSecurityContext context,final BillMasterRepository billMasterRepository,
			final BillDetailRepository billDetailRepository,final PaymentRepository paymentRepository,final AdjustmentRepository adjustmentRepository,
			final BillingOrderRepository billingOrderRepository,final InvoiceTaxRepository invoiceTaxRepository,final InvoiceRepository invoiceRepository,
			final ClientBalanceRepository clientBalanceRepository,final TenantAwareRoutingDataSource dataSource) {

		this.context = context;
		this.billMasterRepository = billMasterRepository;
		this.billDetailRepository = billDetailRepository;
		this.adjustmentRepository = adjustmentRepository;
		this.billingOrderRepository = billingOrderRepository;
		this.invoiceTaxRepository = invoiceTaxRepository;
		this.paymentRepository = paymentRepository;
		this.clientBalanceRepository = clientBalanceRepository;
		this.invoiceRepository=invoiceRepository;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}

	@Override
	public List<BillDetail> createBillDetail(List<FinancialTransactionsData> financialTransactionsDatas,BillMaster master) {
		
		try{
			
		
		List<BillDetail> listOfBillingDetail = new ArrayList<BillDetail>();
		for (FinancialTransactionsData financialTransactionsData : financialTransactionsDatas) {
			BillDetail billDetail = new BillDetail(master,financialTransactionsData.getTransactionId(),
					financialTransactionsData.getTransactionDate(),	financialTransactionsData.getTransactionType(),
					financialTransactionsData.getAmount());
			this.billDetailRepository.save(billDetail);
			listOfBillingDetail.add(billDetail);
		}
		return listOfBillingDetail;
	}catch(DataIntegrityViolationException dve){
		return null;
	}
	}

	@Override
	public CommandProcessingResult updateBillMaster(List<BillDetail> billDetails, BillMaster billMaster,BigDecimal clientBalance) {
		
		try{
		BigDecimal chargeAmount = BigDecimal.ZERO;
		BigDecimal adjustmentAmount = BigDecimal.ZERO;
		BigDecimal paymentAmount = BigDecimal.ZERO;
		BigDecimal dueAmount = BigDecimal.ZERO;
		BigDecimal taxAmount = BigDecimal.ZERO;
		BigDecimal adjustMentsAndPayments = BigDecimal.ZERO;
		for (BillDetail billDetail : billDetails) {
			if (billDetail.getTransactionType().equalsIgnoreCase("SERVICE_CHARGES")) {
				if (billDetail.getAmount() != null)
					chargeAmount = chargeAmount.add(billDetail.getAmount());
			} else if (billDetail.getTransactionType().equalsIgnoreCase("TAXES")) {
				if (billDetail.getAmount() != null)
					taxAmount = taxAmount.add(billDetail.getAmount());

			} else if (billDetail.getTransactionType().equalsIgnoreCase("ADJUSTMENT")) {
				if (billDetail.getAmount() != null)
					adjustmentAmount = adjustmentAmount.add(billDetail.getAmount());
			} else if (billDetail.getTransactionType().contains("PAYMENT")) {
				if (billDetail.getAmount() != null)
					paymentAmount = paymentAmount.add(billDetail.getAmount());

			}
			dueAmount = chargeAmount.add(taxAmount).add(adjustmentAmount)
					.subtract(paymentAmount).add(clientBalance);
			billMaster.setChargeAmount(chargeAmount);
			billMaster.setAdjustmentAmount(adjustmentAmount);
			billMaster.setTaxAmount(taxAmount);
			billMaster.setPaidAmount(paymentAmount);
			billMaster.setDueAmount(dueAmount);
			billMaster.setAdjustmentsAndPayments(paymentAmount.add(adjustmentAmount));
			billMaster.setPreviousBalance(clientBalance);
			this.billMasterRepository.save(billMaster);

		}
		return new CommandProcessingResult(billMaster.getId());
		}catch(DataIntegrityViolationException dve){
			return null;
		}
	}

	@SuppressWarnings("null")
	@Override
	public String generatePdf(BillDetailsData billDetails,
			List<FinancialTransactionsData> datas) {

		String fileLocation = FileUtils.MIFOSX_BASE_DIR + File.separator
				+ "Print_invoice_Details";
		// String fileLocation = FileUtils.MIFOSX_BASE_DIR;

		// String fileLocation = "/home/ubuntu" + File.separator+
		// "Print_invoice_Details";

		/** Recursively create the directory if it does not exist **/
		if (!new File(fileLocation).isDirectory()) {
			new File(fileLocation).mkdirs();
		}
		String printInvoicedetailsLocation = fileLocation + File.separator
				+ "invoice" + billDetails.getId() + ".pdf";

		BillMaster billMaster = this.billMasterRepository.findOne(billDetails
				.getId());
		billMaster.setFileName(printInvoicedetailsLocation);
		this.billMasterRepository.save(billMaster);

		try {

			Document document = new Document();

			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(printInvoicedetailsLocation));
			document.open();
			PdfContentByte pdfContentByte = writer.getDirectContent();
			Font b = new Font(Font.BOLD + Font.BOLD, 8);
			Font b1 = new Font(Font.BOLD + Font.UNDERLINE + Font.BOLDITALIC
					+ Font.TIMES_ROMAN, 6);

			pdfContentByte.beginText();

			PdfPTable table = new PdfPTable(11);
			table.setWidthPercentage(100);

			PdfPCell cell1 = new PdfPCell((new Paragraph("Bill Invoice",
					FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD))));
			cell1.setColspan(11);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell1.setPadding(10.0f);
			table.addCell(cell1);
			PdfPCell cell = new PdfPCell();
			cell.setColspan(2);
			Paragraph para = new Paragraph("Name           :", b1);
			Paragraph addr = new Paragraph("Address        :", b);
			Paragraph branch = new Paragraph("Branch       :", b);
			branch.setSpacingBefore(25);

			cell.addElement(para);
			cell.addElement(addr);
			cell.addElement(branch);
			cell.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell);
			PdfPCell cell0 = new PdfPCell();

			Paragraph add0 = new Paragraph("" + billDetails.getClientName(), b);
			Paragraph add1 = new Paragraph("" + billDetails.getAddrNo() + ""
					+ billDetails.getStreet(), b);
			add1.setSpacingBefore(10);
			Paragraph add2 = new Paragraph("" + billDetails.getCity() + ""
					+ billDetails.getState() + "" + billDetails.getCountry()
					+ "" + billDetails.getZip(), b);
			cell0.setColspan(4);
			cell0.disableBorderSide(PdfPCell.LEFT);
			cell0.addElement(add0);
			cell0.addElement(add1);
			cell0.addElement(add2);
			table.addCell(cell0);

			 Image image = Image.getInstance(FileUtils.MIFOSX_BASE_DIR +File.separator+billDetails.getCompanyLogo());
			 image.scaleAbsolute(90,90);
			PdfPCell cell2 = new PdfPCell();
			 cell2.addElement(image);
			cell2.disableBorderSide(PdfPCell.TOP);
			cell2.disableBorderSide(PdfPCell.BOTTOM);
			cell2.disableBorderSide(PdfPCell.LEFT);
			cell2.disableBorderSide(PdfPCell.RIGHT);
			cell2.setColspan(2);
			table.addCell(cell2);
			PdfPCell cell02 = new PdfPCell();
			Paragraph addr1 = new Paragraph(billDetails.getAddr1(),
					FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD,
							new CMYKColor(0, 255, 255, 17)));
			Paragraph addr2 = new Paragraph(billDetails.getAddr2(), b);
			Paragraph addr3 = new Paragraph(billDetails.getOffCity()+","+billDetails.getOffState(), b);
			Paragraph addr4 = new Paragraph(billDetails.getOffCountry()+"-"+billDetails.getOffZip(),b);
			Paragraph addr5 = new Paragraph(" Tel: "+billDetails.getPhnNum(), b);
			Paragraph addr6 = new Paragraph(billDetails.getEmailId(), b);
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
			Paragraph BillId = new Paragraph("Client Id:   "
					+ billDetails.getClientId(), b);
			cell3.setColspan(6);
			cell3.addElement(BillId);
			cell3.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell3);
			PdfPCell cell12 = new PdfPCell();
			Paragraph billNo = new Paragraph("BillNo:" + billDetails.getId(), b);
			// billNo.setIndentationLeft(280);
			Paragraph billDate = new Paragraph("Bill Date:"
					+ billDetails.getBillDate(), b);
			// billDate.setIndentationLeft(280);
			Paragraph BillPeriod = new Paragraph("Bill Period:"
					+ billDetails.getBillPeriod(), b);
			// BillPeriod.setIndentationLeft(280);
			Paragraph dueDate = new Paragraph("Due Date:"
					+ billDetails.getDueDate(), b);
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

			Paragraph previousbal = new Paragraph("Previous Balance", b);
			Paragraph previousamount = new Paragraph(""
					+ billDetails.getPreviousBalance(), b);
			cell4.setColspan(2);
			cell4.addElement(previousbal);
			cell4.addElement(previousamount);
			cell4.disableBorderSide(PdfPCell.TOP);
			// cell5.disableBorderSide(PdfPCell.BOTTOM);
			// cell4.disableBorderSide(PdfPCell.LEFT);
			// cell4.disableBorderSide(PdfPCell.RIGHT);

			table.addCell(cell4);
			pdfContentByte.setTextMatrix(390, 405);

			PdfPCell cell5 = new PdfPCell();
			Paragraph adjstment = new Paragraph("Adjustment Amount", b);
			Paragraph adjstmentamount = new Paragraph(""
					+ billDetails.getAdjustmentAmount(), b);
			cell5.setColspan(2);
			cell5.addElement(adjstment);
			cell5.addElement(adjstmentamount);
			cell5.disableBorderSide(PdfPCell.TOP);
			// cell5.disableBorderSide(PdfPCell.BOTTOM);
			cell5.disableBorderSide(PdfPCell.LEFT);
			// cell5.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell5);

			PdfPCell cell6 = new PdfPCell();
			Paragraph paid_amount = new Paragraph("Payments", b);
			Paragraph amount = new Paragraph("" + billDetails.getPaidAmount(),
					b);
			cell6.setColspan(2);
			cell6.addElement(paid_amount);
			cell6.addElement(amount);
			cell6.disableBorderSide(PdfPCell.TOP);
			// cell5.disableBorderSide(PdfPCell.BOTTOM);
			cell6.disableBorderSide(PdfPCell.LEFT);
			// cell6.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell6);

			PdfPCell cell7 = new PdfPCell();
			Paragraph charge_amount = new Paragraph("Charge Amount", b);
			Paragraph chargeamount = new Paragraph(""
					+ billDetails.getChargeAmount(), b);
			cell7.setColspan(2);
			cell7.addElement(charge_amount);
			cell7.addElement(chargeamount);

			cell7.disableBorderSide(PdfPCell.TOP);
			// cell5.disableBorderSide(PdfPCell.BOTTOM);
			cell7.disableBorderSide(PdfPCell.LEFT);
			// cell7.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell7);

			PdfPCell cell8 = new PdfPCell();
			Paragraph due_amount = new Paragraph("Due Amount", b);
			Paragraph dueamount = new Paragraph(
					"" + billDetails.getDueAmount(), b);
			cell8.setColspan(3);
			cell8.addElement(due_amount);
			cell8.addElement(dueamount);

			cell8.disableBorderSide(PdfPCell.TOP);
			// cell5.disableBorderSide(PdfPCell.BOTTOM);
			cell8.disableBorderSide(PdfPCell.LEFT);
			// cell8.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell8);

			PdfPCell cell9 = new PdfPCell();
			cell9.setColspan(6);
			Paragraph billDetail = new Paragraph("Current Bill Details", b);
			cell9.setPadding(10.0f);
			cell9.setPaddingLeft(100.0f);
			cell9.addElement(billDetail);
			cell9.disableBorderSide(PdfPCell.TOP);
			cell9.disableBorderSide(PdfPCell.BOTTOM);
			cell9.disableBorderSide(PdfPCell.LEFT);
			cell9.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell9);

			PdfPCell cell10 = new PdfPCell();
			cell10.setColspan(5);
			Paragraph message = new Paragraph("Promotional Message", b);
			cell10.setPadding(10.0f);
			cell10.setPaddingLeft(100.0f);
			cell10.addElement(message);
			cell10.disableBorderSide(PdfPCell.TOP);
			cell10.disableBorderSide(PdfPCell.BOTTOM);
			cell10.disableBorderSide(PdfPCell.LEFT);
			cell10.disableBorderSide(PdfPCell.RIGHT);
			table.addCell(cell10);

			PdfPCell cell26 = new PdfPCell();
			cell26.setColspan(1);
			Paragraph charge = new Paragraph("Id", b);

			cell26.addElement(charge);

			// cell10.disableBorderSide(PdfPCell.TOP);
			// cell10.disableBorderSide(PdfPCell.BOTTOM);
			// cell26.disableBorderSide(PdfPCell.LEFT);
			cell26.disableBorderSide(PdfPCell.RIGHT);

			PdfPCell cell28 = new PdfPCell();
			cell28.setColspan(1);
			Paragraph Amount = new Paragraph("Amount", b);

			cell28.addElement(Amount);
			// cell10.disableBorderSide(PdfPCell.TOP);
			// cell10.disableBorderSide(PdfPCell.BOTTOM);
			cell28.disableBorderSide(PdfPCell.LEFT);
			cell28.disableBorderSide(PdfPCell.RIGHT);

			PdfPCell cell27 = new PdfPCell();
			cell27.setColspan(1);
			Paragraph Date = new Paragraph("Date", b);

			cell27.addElement(Date);
			// cell10.disableBorderSide(PdfPCell.TOP);
			// cell10.disableBorderSide(PdfPCell.BOTTOM);
			cell27.disableBorderSide(PdfPCell.LEFT);
			cell27.disableBorderSide(PdfPCell.RIGHT);

			PdfPCell cell23 = new PdfPCell();
			cell23.setColspan(3);
			Paragraph ID = new Paragraph("Transaction", b);

			cell23.addElement(ID);
			// cell10.disableBorderSide(PdfPCell.TOP);
			// cell10.disableBorderSide(PdfPCell.BOTTOM);
			cell23.disableBorderSide(PdfPCell.LEFT);
			cell23.disableBorderSide(PdfPCell.RIGHT);

			BigDecimal totalAmount = BigDecimal.ZERO;

			for (FinancialTransactionsData data : datas) {
				Paragraph id = new Paragraph("" + data.getTransactionId(), b);

				cell26.addElement(id);

				Paragraph transactionType = new Paragraph(""
						+ data.getTransactionType(), b);
				cell23.addElement(transactionType);
				Paragraph date = new Paragraph("" + data.getTransDate(), b);
				cell27.addElement(date);
				Paragraph tranAmount = new Paragraph("" + data.getAmount(), b);

				cell28.addElement(tranAmount);
				totalAmount = totalAmount.add(data.getAmount());

			}

			table.addCell(cell26);
			table.addCell(cell23);
			table.addCell(cell27);
			table.addCell(cell28);
			PdfPCell cell24 = new PdfPCell();
			cell24.setColspan(1);
			cell24.disableBorderSide(PdfPCell.TOP);
			cell24.disableBorderSide(PdfPCell.BOTTOM);
			table.addCell(cell24);
			PdfPCell cell25 = new PdfPCell();
			Paragraph proMessage = new Paragraph("" + billDetails.getMessage(),
					b);
			cell25.addElement(proMessage);
			cell25.setColspan(4);
			cell25.setPadding(70f);
			table.addCell(cell25);

			pdfContentByte.endText();
			document.add(table);
			document.close();

			// This option is to open the PDF on Server. Instead we have given
			// Financial Statement Download Option
			/*
			 * Runtime.getRuntime().exec(
			 * "rundll32 url.dll,FileProtocolHandler "
			 * +printInvoicedetailsLocation);
			 */

		} catch (Exception e) {
		}
		return printInvoicedetailsLocation;

	}

	@Override
	public void updateBillId(List<FinancialTransactionsData> financialTransactionsDatas,Long billId) {
		
		try{

		for (FinancialTransactionsData transIds : financialTransactionsDatas) {
			if (transIds.getTransactionType().equalsIgnoreCase("ADJUSTMENT")) {
				Adjustment adjustment = this.adjustmentRepository.findOne(transIds.getTransactionId());
				adjustment.updateBillId(billId);
				this.adjustmentRepository.save(adjustment);
			}
			if (transIds.getTransactionType().equalsIgnoreCase("TAXES")) {
				InvoiceTax invoice = this.invoiceTaxRepository.findOne(transIds.getTransactionId());
				invoice.updateBillId(billId);
				this.invoiceTaxRepository.save(invoice);
			}
			if (transIds.getTransactionType().contains("PAYMENT")) {
				Payment payment = this.paymentRepository.findOne(transIds.getTransactionId());
				payment.updateBillId(billId);
				this.paymentRepository.save(payment);
			}
			if (transIds.getTransactionType().equalsIgnoreCase("SERVICE_CHARGES")) {
				BillingOrder billingOrder = this.billingOrderRepository.findOne(transIds.getTransactionId());
				billingOrder.updateBillId(billId);
				this.billingOrderRepository.save(billingOrder);
				Invoice invoice = this.invoiceRepository.findOne(transIds.getTransactionId());
				invoice.updateBillId(billId);
				this.invoiceRepository.save(invoice);
			}
			
			if (transIds.getTransactionType().equalsIgnoreCase("INVOICE")) {
				Invoice invoice = this.invoiceRepository.findOne(transIds.getTransactionId());
				invoice.updateBillId(billId);
				this.invoiceRepository.save(invoice);
			}
			

            if (transIds.getTransactionType().equalsIgnoreCase("ONETIME_CHARGES")) {
            	BillingOrder billingOrder = this.billingOrderRepository.findOne(transIds.getTransactionId());
				billingOrder.updateBillId(billId);
				this.billingOrderRepository.save(billingOrder);
            }

		}
		}catch(Exception exception){
		
			
		}
		

	}

	@Transactional
	@Override
	public void ireportPdf(Long billId) {
		try {
			String fileLocation = FileUtils.BILLING_BASE_DIR;

			/** Recursively create the directory if it does not exist **/
			if (!new File(fileLocation).isDirectory()) {
				new File(fileLocation).mkdirs();
			}
			
			String jpath =  System.getProperty("user.home") + File.separator + "billing";
			String printInvoicedetailsLocation = fileLocation + File.separator + "Bill_" +billId + ".pdf";
			//InputStream input = new FileInputStream(new File("/usr/hugotest/EmployeeReport.jasper"));
			//JasperDesign jasperDesign = JRXmlLoader.load(input);

			//String printInvoicedetailsLocation = fileLocation + File.separator + "Bill_" +billId + ".pdf";
			BillMaster billMaster = this.billMasterRepository.findOne(billId);
			billMaster.setFileName(printInvoicedetailsLocation);
			this.billMasterRepository.save(billMaster);
			//FileInputStream inputStream = new FileInputStream(jpath +File.separator +"Bill_Mainreport.jasper");
			String jfilepath =jpath+File.separator+"Bill_Mainreport.jasper";
			Map<String, Object> parameters = new HashMap();
			String id = String.valueOf(billId);
				parameters.put("param1", id);
				parameters.put("SUBREPORT_DIR",jpath+""+File.separator);
			   JasperPrint jasperPrint = JasperFillManager.fillReport(jfilepath,parameters, this.dataSource.getConnection());
				JasperExportManager.exportReportToPdfFile(jasperPrint,printInvoicedetailsLocation);
			
			/*String jpath =  System.getProperty("user.home") + File.separator + "billing";
			String printInvoicedetailsLocation = fileLocation + File.separator + "Bill_" +billId + ".pdf";
			BillMaster billMaster = this.billMasterRepository.findOne(billId);
			billMaster.setFileName(printInvoicedetailsLocation);
			this.billMasterRepository.save(billMaster);
			FileInputStream inputStream = new FileInputStream(jpath +File.separator +"Bill_Mainreport.jrxml");
			Map parameters = new HashMap();
			String id = String.valueOf(billId);
			parameters.put("param1", id);
			parameters.put("SUBREPORT_DIR",jpath+""+File.separator);
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, this.dataSource.getConnection());
			JasperExportManager.exportReportToPdfFile(jasperPrint,printInvoicedetailsLocation);*/
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	
}
