package org.mifosplatform.billing.uploadstatus.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.mifosplatform.billing.adjustment.api.AdjustmentApiResource;
import org.mifosplatform.billing.adjustment.data.AdjustmentData;
import org.mifosplatform.billing.adjustment.service.AdjustmentReadPlatformService;
import org.mifosplatform.billing.inventory.api.InventoryItemDetailsApiResource;
import org.mifosplatform.billing.inventory.command.ItemDetailsCommand;
import org.mifosplatform.billing.inventory.data.InventoryItemDetailsData;
import org.mifosplatform.billing.inventory.service.InventoryItemDetailsWritePlatformService;
import org.mifosplatform.billing.payments.api.PaymentsApiResource;
import org.mifosplatform.billing.paymode.data.McodeData;
import org.mifosplatform.billing.paymode.service.PaymodeReadPlatformService;
import org.mifosplatform.billing.uploadstatus.command.UploadStatusCommand;
import org.mifosplatform.billing.uploadstatus.domain.UploadStatus;
import org.mifosplatform.billing.uploadstatus.domain.UploadStatusCommandValidator;
import org.mifosplatform.billing.uploadstatus.domain.UploadStatusEnum;
import org.mifosplatform.billing.uploadstatus.domain.UploadStatusRepository;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.data.ApiParameterError;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.exception.InvalidJsonException;
import org.mifosplatform.infrastructure.core.exception.PlatformApiDataValidationException;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.core.exception.UnsupportedParameterException;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.core.service.FileUtils;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


@Service
public class UploadStatusWritePlatformServiceImp implements UploadStatusWritePlatformService{

	private PlatformSecurityContext context;
	private UploadStatusRepository uploadStatusRepository;
	private final Gson gsonConverter;
	private final Set<String> RESPONSE_DATA_ITEM_DETAILS_PARAMETERS = new HashSet<String>(Arrays.asList("id", "itemMasterId", "serialNumber", "grnId","provisioningSerialNumber", "quality", "status","warranty", "remarks"));
	private int rownumber;
	private String filePath;
	private int countno;
	private Long processRecords=(long) 0;
	private Long unprocessRecords=new Long(0);
	private Long totalRecords=new Long(0);
	private String processStatus=null;
	private String errormessage=null;
	private String uploadStatusValue="UPLOADSTATUS";
	private ApiRequestJsonSerializationSettings serSettings;
	private Long orderIdValue;
	private String resultStatus="";
	//public int rowupdateno=0;
	public List<AdjustmentData> adjustmentDataList;
	 public Collection<McodeData> paymodeDataList;
	
	 private String uploadProcess=null;
	
	 private final AdjustmentReadPlatformService adjustmentReadPlatformService;
	 private final PaymodeReadPlatformService paymodeReadPlatformService;
	 private PaymentsApiResource paymentsApiResource;
	 private AdjustmentApiResource adjustmentApiResource;
	
	private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	
	@Autowired
	private InventoryItemDetailsWritePlatformService inventoryItemDetailsWritePlatformService;
	@Autowired
	private  DefaultToApiJsonSerializer<ItemDetailsCommand> toApiJsonSerializer;
	
	private InventoryItemDetailsApiResource inventoryItemDetailsApiResource;
	
	@Autowired
	public UploadStatusWritePlatformServiceImp(final PlatformSecurityContext context,UploadStatusRepository uploadStatusRepository,DefaultToApiJsonSerializer<InventoryItemDetailsData> toApiJsonSerializer,final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService, final InventoryItemDetailsApiResource inventoryItemDetailsApiResource,
			AdjustmentReadPlatformService adjustmentReadPlatformService,PaymodeReadPlatformService paymodeReadPlatformService,PaymentsApiResource paymentsApiResource,AdjustmentApiResource adjustmentApiResource) {
		this.context=context;
		this.uploadStatusRepository=uploadStatusRepository;
		this.commandsSourceWritePlatformService=commandsSourceWritePlatformService;
		this.gsonConverter=new Gson();
		this.inventoryItemDetailsApiResource = inventoryItemDetailsApiResource;
		this.adjustmentReadPlatformService=adjustmentReadPlatformService;
		this.paymodeReadPlatformService=paymodeReadPlatformService;
		this.paymentsApiResource=paymentsApiResource;
		this.adjustmentApiResource=adjustmentApiResource;
	}
	
	//@Transactional
	
	@Override
	public CommandProcessingResult updateUploadStatus(Long orderId,int countno, ApiRequestJsonSerializationSettings settings) {
		//processRecords=(long)0;
		processStatus=null;
		serSettings=settings;
		errormessage="";
		
		
		UploadStatus uploadStatus = this.uploadStatusRepository.findOne(orderId);
		uploadProcess=uploadStatus.getUploadProcess();
		LocalDate currentDate = new LocalDate();
		currentDate.toDate();
    
		 filePath=uploadStatus.getUploadFilePath();
		try {
			InputStream excelFileToRead = new FileInputStream(filePath);

			XSSFWorkbook wb = new XSSFWorkbook(excelFileToRead);

			XSSFSheet sheet = wb.getSheetAt(0);
			
			XSSFRow row;
			XSSFCell cell;
			String serialno = "0";
			 
			if (countno == 0) {
				countno = countno + 2;
			} else if (countno == 1) {
				countno = countno + 1;
			}
			System.out.println("Excel Row No is: " + countno);
			
			Iterator rows = sheet.rowIterator();
			Vector<XSSFCell> v = new Vector<XSSFCell>();
			if (countno > 0) {
				countno = countno - 1;
			}
			while (rows.hasNext()) {
				
			

				row = (XSSFRow) rows.next();
				rownumber = row.getRowNum();
				
				if (rownumber > 0) {
					if (rownumber >= countno) {
						
						Iterator cells = row.cellIterator();
						while (cells.hasNext()) {

 							cell = (XSSFCell) cells.next();

							//if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
								v.add(cell);
							//} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
								//v.add(cell);
							//} else {
								//v.add(cell);
						//	}

						}
					
						System.out.println(v.elementAt(0).toString());
						if(v.elementAt(0).toString().equalsIgnoreCase("EOF"))
						{
							long unprocessedRecords=totalRecords-processRecords;
							uploadStatus.update(currentDate,processStatus,processRecords,unprocessedRecords,errormessage);
							this.uploadStatusRepository.save(uploadStatus);
							processRecords=new Long(0);
							totalRecords=new Long(0);
							unprocessRecords=new Long(0);
							break;
						}
						else{
						
							


       JSONObject jsonobject = new JSONObject();
     
      if(uploadProcess.equalsIgnoreCase("Hardware Items"))
      {
    		totalRecords++;
      
       jsonobject.put("itemMasterId", new Double(v.elementAt(0).toString()).longValue());
             jsonobject.put("serialNumber", v.elementAt(1).toString());
             jsonobject.put("grnId", new Double(v.elementAt(2).toString()).longValue());
             jsonobject.put("provisioningSerialNumber", v.elementAt(3).toString());
             jsonobject.put("quality", v.elementAt(4).toString());
             jsonobject.put("status",v.elementAt(5).toString());
             jsonobject.put("warranty", new Double(v.elementAt(6).toString()).longValue());
             jsonobject.put("locale", "en");
             jsonobject.put("remarks", v.elementAt(7).toString());
             jsonobject.put("clientId", 1);
             jsonobject.put("officeId", 1);
             jsonobject.put("flag", 1);
      
      inventoryItemDetailsApiResource.addItemDetails(jsonobject.toString().toString());
      }
      else if (uploadProcess.equalsIgnoreCase("Adjustments")) {
    		totalRecords++;
       
         adjustmentDataList=this.adjustmentReadPlatformService.retrieveAllAdjustmentsCodes();
       
        if(adjustmentDataList.size()>0)
        {
         for(AdjustmentData adjustmentData:adjustmentDataList)
         {
         if( adjustmentData.getAdjustment_code().equalsIgnoreCase(v.elementAt(2).toString()));
          {          
          // jsonobject.put("clientId", new Double(v.elementAt(2).toString()).longValue());
                 jsonobject.put("adjustment_date", v.elementAt(1).toString());
                 jsonobject.put("adjustment_code", adjustmentData.getId());
                 jsonobject.put("amount_paid", v.elementAt(4).toString());
                 jsonobject.put("adjustment_type",v.elementAt(3).toString());
                 jsonobject.put("Remarks", v.elementAt(5).toString());
                 jsonobject.put("locale", "en");
                 jsonobject.put("dateFormat","dd MMMM yyyy");
                 adjustmentApiResource.addNewAdjustment(new Double(String.valueOf(v.elementAt(0)).toString()).longValue(), jsonobject.toString());
                 break;
          }
         }
        }
      }
        else if (uploadProcess.equalsIgnoreCase("Payments")) {
        	  
        	 totalRecords++;
             paymodeDataList = this.paymodeReadPlatformService.retrievemCodeDetails("Payment Mode");
       
             if(paymodeDataList.size()>0)
                 {
                   for(McodeData paymodeData:paymodeDataList)
                      {
                    if( paymodeData.getPaymodeCode().equalsIgnoreCase(v.elementAt(2).toString()));
                     {
        	              jsonobject.put("paymentCode",paymodeData.getId());
                      }
                    }
          
                     jsonobject.put("clientId", "");
                    
                 jsonobject.put("paymentDate", v.elementAt(1).toString());
                 jsonobject.put("amountPaid", v.elementAt(3).toString());
                 jsonobject.put("remarks",  v.elementAt(4).toString());
                 jsonobject.put("locale", "en");
                 jsonobject.put("dateFormat","dd MMMM yyyy");
                 paymentsApiResource.createPayment(new Double(String.valueOf(v.elementAt(0))).longValue()/*v.elementAt(0).toString())*/, jsonobject.toString());
               //  break;
        }
        }
						
						++processRecords;
						 resultStatus="Success";
						writeXLSXFile(filePath,resultStatus);
		           processStatus=UploadStatusEnum.COMPLETED.toString();
		          
						}
		                v.removeAllElements();
					}
				}
			
			}

		} 
		
		catch (Exception e) {
			//writeXLSXFile(filePath);
			unprocessRecords++;
			System.out.println("exceptuon"+e);
			errormessage=UploadStatusEnum.ERROR.toString();
		 
		   resultStatus="Failure";
		System.out.println(e.toString());
		  
		   
		
			if(e.toString().contains("ClientNotFoundException"))
			{
			errormessage="Client with this id does not exist";
			
			}else if(e.toString().contains("NoGrnIdFoundException")){
				errormessage="GrnId is not a valid Id";
			
			}else if(e.toString().contains("PlatformDataIntegrityException")){
			   
				errormessage="Serial Number is Already existed";	
			   
			}else if(e.toString().contains("OrderQuantityExceedsException")){
				errormessage ="order quntity is completed";
			}else if(e.toString().contains("PlatformApiDataValidationException")){
				errormessage ="missing some value in this record";
			}
			 writeXLSXFile(filePath,errormessage);
			
			if (rownumber+1 >= countno) {
				int rownum = rownumber;
				rownum = rownum + 2;
			
					//updateUploadStatusReadXls(filePath, rownum);
				updateUploadStatus(orderId,rownum,null);
			
			} 
		
	//	this.exception();
		}
		
		
		
		
		// if (order==null || order.getStatus() == 3) {
		// throw new ProductNotFoundException(order.getId());
		// }
		
		
		//totalRecords=new Long(0);
		//processRecords=totalRecords;
	//	unprocessedRecords=totalRecords;
		return new CommandProcessingResult(Long.valueOf(-1));

	}
	
	
	public CommandProcessingResult updateUploadStatusReadXls(String filepath,int rowvalue)
	{
		try {
			
		//	processRecords=(long)0;
			
			errormessage=UploadStatusEnum.COMPLETED.toString();
			InputStream excelFileToRead = new FileInputStream(filePath);

			XSSFWorkbook wb = new XSSFWorkbook(excelFileToRead);

			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row;
			XSSFCell cell;
			String serialno = "0";
			 countno = rowvalue;
			if (countno == 0) {
				countno = countno + 2;
			} else if (countno == 1) {
				countno = countno + 1;
			}
			System.out.println("Excel Row No is: " + countno);
			
			Iterator rows = sheet.rowIterator();
			Vector<XSSFCell> v = new Vector<XSSFCell>();
			if (countno > 0) {
				countno = countno - 1;
			}
			while (rows.hasNext()) {

				row = (XSSFRow) rows.next();
				rownumber = row.getRowNum();
				
				if (rownumber > 0) {
					if (rownumber >= countno) {
						
						Iterator cells = row.cellIterator();
						while (cells.hasNext()) {

							cell = (XSSFCell) cells.next();

							if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
								v.add(cell);
							} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
								v.add(cell);
							} else {
								v.add(cell);
							}

						}
					
						
						
						
					//	ItemDetailsCommand itemDetailsCommand=new ItemDetailsCommand();
						
						System.out.println(v.elementAt(0).toString());
						if(v.elementAt(0).toString().equalsIgnoreCase("EOF"))
						{
							break;
						}
						else{

						       JSONObject jsonobject = new JSONObject();
						     
						      if(uploadProcess.equalsIgnoreCase("Hardware Items"))
						      {
						      
						       jsonobject.put("itemMasterId", new Double(v.elementAt(0).toString()).longValue());
						             jsonobject.put("serialNumber", v.elementAt(1).toString());
						             jsonobject.put("grnId", new Double(v.elementAt(2).toString()).longValue());
						             jsonobject.put("provisioningSerialNumber", v.elementAt(3).toString());
						             jsonobject.put("quality", v.elementAt(4).toString());
						             jsonobject.put("status",v.elementAt(5).toString());
						             jsonobject.put("warranty", new Double(v.elementAt(6).toString()).longValue());
						             jsonobject.put("locale", "en");
						             jsonobject.put("remarks", v.elementAt(7).toString());
						             jsonobject.put("clientId", 1);
						             jsonobject.put("officeId", 1);
						             jsonobject.put("flag", 1);
						      inventoryItemDetailsApiResource.addItemDetails(jsonobject.toString().toString());
						      }
						      else if (uploadProcess.equalsIgnoreCase("Adjustments")) {
						       
						       
						         adjustmentDataList=this.adjustmentReadPlatformService.retrieveAllAdjustmentsCodes();
						       
						      //  if(adjustmentDataList.size()>0)
						      //  {
						         for(AdjustmentData adjustmentData:adjustmentDataList)
						         {
						         if( adjustmentData.getAdjustment_code().equalsIgnoreCase(v.elementAt(2).toString()));
						          {
						        	  jsonobject.put("adjustment_code", adjustmentData.getId());
						          }
						         }
						          // jsonobject.put("clientId", new Double(v.elementAt(2).toString()).longValue());
						                 jsonobject.put("adjustment_date", v.elementAt(1).toString());
						               
						                 jsonobject.put("amount_paid", v.elementAt(4).toString());
						                 jsonobject.put("adjustment_type",v.elementAt(3).toString());
						                 jsonobject.put("Remarks", v.elementAt(5).toString());
						                 jsonobject.put("locale", "en");
						                 jsonobject.put("dateFormat","dd MMMM yyyy");
						                 adjustmentApiResource.addNewAdjustment(new Double(String.valueOf(v.elementAt(0))).longValue(), jsonobject.toString());
						                 break;
						          
						         }
						       						     // }
						        else if (uploadProcess.equalsIgnoreCase("Payments")) {
						        	
						        	totalRecords++;
						                          paymodeDataList = this.paymodeReadPlatformService.retrievemCodeDetails("Payment Mode");
						       
						       //                   if(paymodeDataList.size()>0)
						     //   {
						         for(McodeData paymodeData:paymodeDataList)
						         {
						         if( paymodeData.getPaymodeCode().equalsIgnoreCase(v.elementAt(2).toString()));
						          {
						        	  jsonobject.put("paymentCode",paymodeData.getId());
						          }
						         }
						          // jsonobject.put("clientId", new Double(v.elementAt(2).toString()).longValue());
						           jsonobject.put("clientId", "");
						                            
						                 jsonobject.put("paymentDate", v.elementAt(1).toString());
						                 jsonobject.put("amountPaid", v.elementAt(3).toString());
						                 jsonobject.put("remarks",  v.elementAt(4).toString());
						                 jsonobject.put("locale", "en");
						                 jsonobject.put("dateFormat","dd MMMM yyyy");
						                 paymentsApiResource.createPayment(new Double(String.valueOf(v.elementAt(0))).longValue(), jsonobject.toString());
						                 break;
						          
						          
						       
						     //   }
						        }
						++processRecords;
						resultStatus=UploadStatusEnum.COMPLETED.toString();
						writeXLSXFile(filePath,resultStatus);
                  // processStatus="Processed";
						}
                        v.removeAllElements();
					}
				}
			
			}

		} 
		catch (Exception e) {
			//writeXLSXFile(filePath);
			System.out.println("exceptuon"+e);
			processStatus=UploadStatusEnum.ERROR.toString();
	       e.printStackTrace();
	       resultStatus=UploadStatusEnum.ERROR.toString();
	       
			
			System.out.println("exception method");
			if(e.toString().contains("ClientNotFoundException"))
			{
			errormessage="Client with this id does not exist";
			}
			writeXLSXFile(filePath,errormessage);
			exception();
		}
		
		
		return null;
	}
	public  void exception() {
		if (rownumber >= countno) {
			int rownum = rownumber;
			rownum = rownum + 2;
			
			try {
				updateUploadStatusReadXls(filePath, rownum);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				exception();
				e.printStackTrace();

			}
		} else {
			
			return;
		}
	}
	
	public  void writeXLSXFile(String filepath, String errormessage)  {
		
		
		try{
		int rowupdateno=rownumber;
			InputStream excelFileToRead = new FileInputStream(filepath);
			XSSFWorkbook wb = new XSSFWorkbook(excelFileToRead);

			XSSFSheet sheet = wb.getSheetAt(0);
			 
			XSSFRow row = sheet.getRow(rowupdateno);
			 
			 XSSFCell cell = row.getCell(8, row.CREATE_NULL_AS_BLANK);
			cell = row.getCell(8);
			
			/*if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
				return;
			} else {
				cell.setCellValue(resultStatus);
			}*/
			
			if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
				cell.setCellValue(errormessage);
				//return;
			} else{
				cell.setCellValue(errormessage);
			}


			OutputStream fileOut = new FileOutputStream(filepath);
			
			// write this workbook to an Outputstream.
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
			resultStatus=null;
			++countno;
	}catch (Exception e) {
		// TODO: handle exception
	}
	}
	
	
	
	
	@Transactional
	@Override
	public CommandProcessingResult addItem(UploadStatusCommand command) {
		UploadStatus uploadStatus;
	
		 try{
			 
			this.context.authenticatedUser();
			UploadStatusCommandValidator validator = new UploadStatusCommandValidator(command);
		    validator.validateForCreate();
        	String fileLocation=null;
			fileLocation = FileUtils.saveToFileSystem(command.getInputStream(), command.getFileUploadLocation(),command.getFileName());
			
			uploadStatus = UploadStatus.create(command.getUploadProcess(), fileLocation, command.getProcessDate(),command.getProcessStatus(),
					command.getProcessRecords(), command.getErrorMessage(),command.getDescription(),command.getFileName());
			
			 this.uploadStatusRepository.save(uploadStatus);
			 return new CommandProcessingResult(uploadStatus.getId());
			 
		} catch (DataIntegrityViolationException dve) {
			handleCodeDataIntegrityIssues(command,dve);
			return new CommandProcessingResult(Long.valueOf(-1));
		}catch (IOException e) {
			return new CommandProcessingResult(Long.valueOf(-1));
		}
		
		
		
	}

	private void handleCodeDataIntegrityIssues(final UploadStatusCommand command, final DataIntegrityViolationException dve) {
        Throwable realCause = dve.getMostSpecificCause();
        if (realCause.getMessage().contains("file_name_key")) {
            final String name = command.getFileName();
       
            throw new PlatformDataIntegrityException("error.msg.file.duplicate.name", "A file with name'"
                    + name + "'already exists", "displayName", name);
        }

//        logger.error(dve.getMessage(), dve);
        throw new PlatformDataIntegrityException("error.msg.cund.unknown.data.integrity.issue",
                "Unknown data integrity issue with resource: " + realCause.getMessage());
    }
	
	@Transactional
	@Override
	 public UploadStatusCommand convertJsonToUploadStatusCommand(Object object,String jsonRequestBody) {
	       
	        if(StringUtils.isBlank(jsonRequestBody)){
	            throw new InvalidJsonException();
	        }
	     
	       
	        Type typeOfMap = new TypeToken<Map<String,String>>(){}.getType();
	        Map<String,String> requestMap = gsonConverter.fromJson(jsonRequestBody, typeOfMap);
	        Set<String> supportedParams = new HashSet<String>(Arrays.asList("locale","dateFormat","uploadProcess","uploadFilePath","processDate","processStatus","processRecords","errorMessage","isDeleted"));
	        checkForUnsupportedParameters(requestMap, supportedParams);
	        Set<String> modifiedParameters = new HashSet<String>();
	       
	       String uploadProcess = extractStringParameter("uploadProcess", requestMap, modifiedParameters);
	        String uploadFilePath = extractStringParameter("uploadFilePath", requestMap, modifiedParameters);
	        LocalDate processDate = extractLocalDateParameter("processDate", requestMap, modifiedParameters);
	        String processStatus = extractStringParameter("processStatus", requestMap, modifiedParameters);
	        Long processRecords = extractLongParameter("processRecords", requestMap, modifiedParameters);
	        String errorMessage = extractStringParameter("errorMessage", requestMap, modifiedParameters);
	        String description = extractStringParameter("description", requestMap, modifiedParameters);
	        
	       // UploadStatusCommand(String uploadProcess,String uploadFilePath,Date processDate, String processStatus,Long processRecords,String errorMessage,char isDeleted,Set<String> modifiedParameters)
	        return new UploadStatusCommand(uploadProcess,uploadFilePath,processDate,processStatus,processRecords,errorMessage,modifiedParameters,description,null,null,null);
	    }
	private String extractStringParameter(final String paramName,
			final Map<String, ?> requestMap,
			final Set<String> modifiedParameters) {
		String paramValue = null;
		if (requestMap.containsKey(paramName)) {
			paramValue = (String) requestMap.get(paramName);
			modifiedParameters.add(paramName);
		}

		if (paramValue != null) {
			paramValue = paramValue.trim();
		}

		return paramValue;
	}
	private Long extractLongParameter(final String paramName,
			final Map<String, ?> requestMap,
			final Set<String> modifiedParameters) {
		Long paramValue = null;
		if (requestMap.containsKey(paramName)) {
			String valueAsString = (String) requestMap.get(paramName);
			if (StringUtils.isNotBlank(valueAsString)) {
				paramValue = Long.valueOf(Double.valueOf(valueAsString)
						.longValue());
			}
			modifiedParameters.add(paramName);
		}
		return paramValue;
	}
	private LocalDate extractLocalDateParameter(final String paramName,
			final Map<String, ?> requestMap,
			final Set<String> modifiedParameters) {
		LocalDate paramValue = null;
		if (requestMap.containsKey(paramName)) {
			String valueAsString = (String) requestMap.get(paramName);
			if (StringUtils.isNotBlank(valueAsString)) {
				final String dateFormat = (String) requestMap.get("dateFormat");
				final Locale locale = new Locale(
						(String) requestMap.get("locale"));
				paramValue = convertFrom(valueAsString, paramName, dateFormat,
						locale);
			}
			modifiedParameters.add(paramName);
		}
		return paramValue;
	}
	
		
	private LocalDate convertFrom(final String dateAsString,
			final String parameterName, final String dateFormat,
			final Locale clientApplicationLocale) {

		if (StringUtils.isBlank(dateFormat) || clientApplicationLocale == null) {

			List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
			if (StringUtils.isBlank(dateFormat)) {
				String defaultMessage = new StringBuilder(
						"The parameter '"
								+ parameterName
								+ "' requires a 'dateFormat' parameter to be passed with it.")
						.toString();
				ApiParameterError error = ApiParameterError.parameterError(
						"validation.msg.missing.dateFormat.parameter",
						defaultMessage, parameterName);
				dataValidationErrors.add(error);
			}
			if (clientApplicationLocale == null) {
				String defaultMessage = new StringBuilder(
						"The parameter '"
								+ parameterName
								+ "' requires a 'locale' parameter to be passed with it.")
						.toString();
				ApiParameterError error = ApiParameterError.parameterError(
						"validation.msg.missing.locale.parameter",
						defaultMessage, parameterName);
				dataValidationErrors.add(error);
			}
			throw new PlatformApiDataValidationException(
					"validation.msg.validation.errors.exist",
					"Validation errors exist.", dataValidationErrors);
		}

		LocalDate eventLocalDate = null;
		if (StringUtils.isNotBlank(dateAsString)) {
			try {
				// Locale locale = LocaleContextHolder.getLocale();
				eventLocalDate = DateTimeFormat
						.forPattern(dateFormat)
						.withLocale(clientApplicationLocale)
						.parseLocalDate(
								dateAsString
										.toLowerCase(clientApplicationLocale));
			} catch (IllegalArgumentException e) {
				List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
				ApiParameterError error = ApiParameterError.parameterError(
						"validation.msg.invalid.date.format", "The parameter "
								+ parameterName
								+ " is invalid based on the dateFormat: '"
								+ dateFormat + "' and locale: '"
								+ clientApplicationLocale + "' provided:",
						parameterName, dateAsString, dateFormat);
				dataValidationErrors.add(error);

				throw new PlatformApiDataValidationException(
						"validation.msg.validation.errors.exist",
						"Validation errors exist.", dataValidationErrors);
			}
		}

		return eventLocalDate;
	}
	private void checkForUnsupportedParameters(Map<String, ?> requestMap,
			Set<String> supportedParams) {
		List<String> unsupportedParameterList = new ArrayList<String>();
		for (String providedParameter : requestMap.keySet()) {
			if (!supportedParams.contains(providedParameter)) {
				unsupportedParameterList.add(providedParameter);
			}
		}

		if (!unsupportedParameterList.isEmpty()) {
			throw new UnsupportedParameterException(unsupportedParameterList);
		}
	}
}

