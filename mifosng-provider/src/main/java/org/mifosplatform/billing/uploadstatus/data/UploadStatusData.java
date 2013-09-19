package org.mifosplatform.billing.uploadstatus.data;

import java.util.Date;

import org.joda.time.LocalDate;

public class UploadStatusData {
	private final Long id;
	private final String uploadProcess;
	private final String uploadFilePath;
	private final LocalDate processDate;
	private final String processStatus;
	private final Long processRecords;
	private final String errorMessage;
	private final boolean flag;
	private final Long unprocessRecords;
	 public static UploadStatusData instance(Long id,String uploadProcess,String uploadFilePath,LocalDate processDate,String processStatus,
			 Long processRecords,Long unprocessRecords,String errorMessage)
		{
	        return new UploadStatusData(id,uploadProcess,uploadFilePath,processDate,processStatus,processRecords,unprocessRecords,errorMessage);
	    }
	
	
	public UploadStatusData(Long id,String uploadProcess,String uploadFilePath,LocalDate processDate,String processStatus,Long processRecords,Long unprocessRecords, String errorMessage)
	{
		this.id=id;
		this.uploadProcess=uploadProcess;
		this.uploadFilePath=uploadFilePath;
		this.processDate=processDate;
		this.processRecords=processRecords;
		this.processStatus=processStatus;
		this.errorMessage=errorMessage;
	    this.flag=processStatus.equalsIgnoreCase("Processed")?true:false;
	    this.unprocessRecords=unprocessRecords;
		
	}


	public Long getId() {
		return id;
	}


	public String getUploadProcess() {
		return uploadProcess;
	}


	public String getUploadFilePath() {
		return uploadFilePath;
	}


	public LocalDate getProcessDate() {
		return processDate;
	}


	public String getProcessStatus() {
		return processStatus;
	}


	public Long getProcessRecords() {
		return processRecords;
	}


	public boolean isFlag() {
		return flag;
	}


	public String getErrorMessage() {
		return errorMessage;
	}
	
	
}
