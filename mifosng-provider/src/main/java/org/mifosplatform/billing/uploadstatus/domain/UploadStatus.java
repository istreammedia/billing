package org.mifosplatform.billing.uploadstatus.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Entity
@Table(name="uploads_status")
public class UploadStatus extends AbstractPersistable<Long>{

	/*CREATE TABLE `uploads_status` (
			  `id` int(20) NOT NULL AUTO_INCREMENT,
			  `upload_process` varchar(60) NOT NULL,
			  `upload_filepath` varchar(250) DEFAULT NULL,
			  `process_date` datetime NOT NULL,
			  `process_status` varchar(20)  NOT NULL  DEFAULT 'New Unprocessed',
			  `process_records` bigint(20) DEFAULT NULL,
			  `error_message` varchar(250) DEFAULT NULL,
			  `is_deleted` char(1) NOT NULL DEFAULT 'N',
			  PRIMARY KEY (`id`)
			) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
			
*/
	
	
	@Column(name="file_name", nullable=true, length=100)
	private String fileName;
	
	
	@Column(name="upload_process", nullable=false, length=20)
	private String uploadProcess;
	
	@Column(name="upload_filepath", nullable=true, length=100)
	private String uploadFilePath;
	
	@Column(name="process_date", nullable=false, length=20)
	private Date processDate;
	
	@Column(name="process_status",nullable=true,length=100)
	private String processStatus="New Unprocessed";
	
	@Column(name="process_records",nullable=true,length=20)
	private Long processRecords;
	
	@Column(name="description",nullable=true,length=100)
	private String description;
	
	@Column(name="error_message",nullable=true,length=20)
	private String errorMessage;
	
	@Column(name="is_deleted",nullable=false, length=20)
	private char isDeleted='N';

	@Column(name="unprocess_records",nullable=true,length=20)
	private Long unprocessedRecords;
	
	

	public UploadStatus(){}
	
	
	public UploadStatus(String uploadProcess,String uploadFilePath,LocalDate processDate,String processStatus,Long processRecords,String errorMessage,String description, String fileName){
		this.uploadProcess=uploadProcess;
		this.uploadFilePath=uploadFilePath;
		this.processDate=processDate.toDate();
		this.processStatus=UploadStatusEnum.NEW.toString();
		this.processRecords=processRecords;
		this.errorMessage=errorMessage;
		this.description=description;
		this.fileName=fileName;
		
		
	}
	
	public static UploadStatus create(String uploadProcess,String uploadFilePath,LocalDate processDate,String processStatus,Long processRecords,String errorMessage,String description, String fileName){
		
		return new UploadStatus(uploadProcess,uploadFilePath,processDate,processStatus,processRecords,errorMessage,description,fileName);
	}
	
	public void update(LocalDate currentDate,String processStatus,Long processRecords,Long unprocessedRecords, String errorMessage) {
			this.processDate = currentDate.toDate();
			this.processStatus=unprocessedRecords > 0?UploadStatusEnum.ERROR.toString():UploadStatusEnum.COMPLETED.toString();
			this.processRecords=processRecords;
			this.errorMessage=unprocessedRecords > 0?UploadStatusEnum.ERROR.toString():UploadStatusEnum.COMPLETED.toString();
			this.unprocessedRecords=unprocessedRecords;
			
		}


	public String getUploadProcess() {
		return uploadProcess;
	}


	public String getUploadFilePath() {
		return uploadFilePath;
	}


	public Date getProcessDate() {
		return processDate;
	}


	public String getProcessStatus() {
		return processStatus;
	}


	public Long getProcessRecords() {
		return processRecords;
	}


	public String getErrorMessage() {
		return errorMessage;
	}


	public char getIsDeleted() {
		return isDeleted;
	}


	public String getFileName() {
		// TODO Auto-generated method stub
		return fileName;
	}


	
	
}
