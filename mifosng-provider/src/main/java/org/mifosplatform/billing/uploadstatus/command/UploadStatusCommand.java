package org.mifosplatform.billing.uploadstatus.command;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

import org.joda.time.LocalDate;


public class UploadStatusCommand {
	
	private String uploadProcess;
	private String uploadFilePath;
	private LocalDate processDate;
	private String processStatus;
	private Long processRecords;
	private String errorMessage;
	private String description;
	private String fileName;
	private InputStream inputStream;
	private String fileUploadLocation;

	
	private final Set<String> modifiedParameters;
	
	public UploadStatusCommand()
	{
		this.modifiedParameters=null;
	}
	
	public UploadStatusCommand(String uploadProcess,String uploadFilePath,LocalDate processDate, String processStatus,Long processRecords,String errorMessage,Set<String> modifiedParameters,String description,String fileName,InputStream inputStream,String fileUploadLocation)
	{
		this.uploadProcess=uploadProcess;
		this.uploadFilePath=uploadFilePath;
		this.processDate=processDate;
		this.processStatus=processStatus;
		this.processRecords=processRecords;
		this.errorMessage=errorMessage;
		this.modifiedParameters=modifiedParameters;
		this.description=description;
		this.fileName=fileName;
		this.inputStream=inputStream;
		this. fileUploadLocation= fileUploadLocation;
		
	}
	
	

	
	public String getFileUploadLocation() {
		return fileUploadLocation;
	}

	public void setFileUploadLocation(String fileUploadLocation) {
		this.fileUploadLocation = fileUploadLocation;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDescription() {
		return description;
	}




	public void setDescription(String description) {
		this.description = description;
	}




	public String getUploadProcess() {
		return uploadProcess;
	}







	public void setUploadProcess(String uploadProcess) {
		this.uploadProcess = uploadProcess;
	}







	public String getUploadFilePath() {
		return uploadFilePath;
	}







	public void setUploadFilePath(String uploadFilePath) {
		this.uploadFilePath = uploadFilePath;
	}







	public LocalDate getProcessDate() {
		return processDate;
	}







	public void setProcessDate(LocalDate processDate) {
		this.processDate = processDate;
	}







	public String getProcessStatus() {
		return processStatus;
	}







	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}







	public Long getProcessRecords() {
		return processRecords;
	}







	public void setProcessRecords(Long processRecords) {
		this.processRecords = processRecords;
	}







	public String getErrorMessage() {
		return errorMessage;
	}







	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}







	public Set<String> getModifiedParameters() {
		return modifiedParameters;
	}  
}
