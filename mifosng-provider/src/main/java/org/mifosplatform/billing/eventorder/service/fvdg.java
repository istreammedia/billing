package org.mifosplatform.billing.eventorder.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.mifosplatform.billing.billmaster.domain.BillMaster;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.ReportGenerator;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class fvdg {


	public static void main(String[] args) {

	try {
		Connection conn;
		

		//JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
		//JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		//JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, this.dataSource.getConnection());
		
	Class.forName("com.mysql.jdbc.Driver");
	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mifostenant-default?user=root&password=mysql");
	System.out.println("Loading Report Designs");
	//---String jpath =  "/usr/hugotest/EmployeeReport.jasper";
	String jpath =  System.getProperty("user.home") + File.separator + "billing";
	//InputStream input = new FileInputStream(new File("/usr/hugotest/EmployeeReport.jasper"));
	//JasperDesign jasperDesign = JRXmlLoader.load(input);

	//String printInvoicedetailsLocation = fileLocation + File.separator + "Bill_" +billId + ".pdf";
	//BillMaster billMaster = this.billMasterRepository.findOne(billId);
	//billMaster.setFileName(printInvoicedetailsLocation);
	//this.billMasterRepository.save(billMaster);
	FileInputStream inputStream = new FileInputStream(jpath +File.separator +"Bill_Mainreport.jrxml");
	Map<String, Object> parameters = new HashMap();
	String id = String.valueOf(10);
	parameters.put("param1", id);
	parameters.put("SUBREPORT_DIR",jpath+""+File.separator);
	JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
	JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
	JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,conn);
	JasperExportManager.exportReportToPdfFile(jasperPrint,jpath+File.separator+"g.pdf");
	conn.close();
	} catch (FileNotFoundException e) {
	e.printStackTrace();
	} catch (JRException e) {
	e.printStackTrace();
	} catch (ClassNotFoundException e) {
	e.printStackTrace();
	} catch (SQLException e) {
	e.printStackTrace();
	}
	}

	

	}
	

