

import java.io.File;

import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.Connection;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class ReportTest {
	
	public static void main(String[] args) {
		
	try
	{
	  // String printInvoicedetailsLocation = "/home/avinash/Desktop/jasper/Bill.pdf";
		String printInvoicedetailsLocation = "C:\\Users\\hugo\\billing\\Bill.pdf";

	   //BillMaster billMaster = this.billMasterRepository.findOne(billId);
	 //  billMaster.setFileName(printInvoicedetailsLocation);
	   //this.billMasterRepository.save(billMaster);
	   
	   Class.forName("com.mysql.jdbc.Driver");
	Connection	conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/mifostenant-default?user=root&password=mysql");
	   //FileInputStream inputStream = new FileInputStream("/home/avinash/Desktop/report1.jrxml");
	   //FileInputStream inputStream = new FileInputStream("/home/avinash/Desktop/Bill_Mainreport.jasper");
	   int id=1;
		String jpath =  System.getProperty("user.home") + File.separator + "billing";
	   String jfilepath ="C:\\Users\\hugo\\billing\\Bill_Mainreport.jasper";
	   /*Map parameters = new HashMap();
	                 
	 
	   parameters.put("param1", 1);
	   parameters.put("SUBREPORT_DIR","/home/avinash/Desktop/Bill_Subreport.jrxml"+""+File.separator);*/
	   /*String SUBREPORTDIR= "repo://home/avinash/Desktop/Bill_Subreport.jrxml";
	   JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
	   JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
	   JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,conn);
	   OutputStream output = new FileOutputStream(new File(printInvoicedetailsLocation));
	  
	   JasperExportManager.exportReportToPdfStream(jasperPrint,output);*/
	   Map<String, Object> parameters = new HashMap();
		
		parameters.put("param1", "1");
		parameters.put("SUBREPORT_DIR",jpath+""+File.separator);
	   JasperPrint jasperPrint = JasperFillManager.fillReport(jfilepath,parameters, conn);
		JasperExportManager.exportReportToPdfFile(jasperPrint,printInvoicedetailsLocation);
	}catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	}
}
