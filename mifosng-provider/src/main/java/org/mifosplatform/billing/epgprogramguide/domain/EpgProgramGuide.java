package org.mifosplatform.billing.epgprogramguide.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.useradministration.domain.AppUser;


@Entity
@Table(name="b_program_guide")
public class EpgProgramGuide extends AbstractAuditableCustom<AppUser, Long>{

	
	@Column(name="channel_name")
	private String channelName;
	
	@Column(name="channel_icon")
	private String channelIcon;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="program_date")
	private Date programDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_time")
	private Date startTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="stop_time")
	private Date stopTime;
	
	@Column(name="program_title")
	private String programTitle;
	
	@Column(name="program_desc")
	private String programDescription;
	
	@Column(name="type")
	private String type;
	
	@Column(name="genre")
	private String genre;
	
	public EpgProgramGuide() {
		
	}
	
	private EpgProgramGuide(final String channelName, final String channelIcon, final Date programDate, final Date startTime, final Date stopTime, final String programTitle, final String programDescriptoin, final String type, final String genre){
		this.channelName = channelName;
		this.channelIcon = channelIcon;
		this.programDate = programDate;
		this.startTime = startTime;
		this.stopTime = stopTime;
		this.programTitle = programTitle;
		this.programDescription = programDescriptoin;
		this.type = type;
		this.genre = genre;
		
		
	}
	
	public static EpgProgramGuide fromJson(JsonCommand command){
		try{
		String channelName = command.stringValueOfParameterNamed("channelName");
		String channelIcon = command.stringValueOfParameterNamed("channelIcon");
		Date programDate = command.DateValueOfParameterNamed("programDate");
		
		String startProgramTime = command.stringValueOfParameterNamed("startTime");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = df1.parse(startProgramTime);
		
		String stopProgramTime = command.stringValueOfParameterNamed("stopTime");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date stopTime = df2.parse(stopProgramTime);
		
		String programTitle = command.stringValueOfParameterNamed("programTitle");
		String programDescription = command.stringValueOfParameterNamed("programDescription");
		String type = command.stringValueOfParameterNamed("type");
		String genre = command.stringValueOfParameterNamed("grnre");
		return new EpgProgramGuide(channelName, channelIcon, programDate, startTime, stopTime, programTitle, programDescription, type, genre);
		}catch(Exception e){
			throw new PlatformDataIntegrityException(e.getMessage(), e.getMessage(), e.getMessage());
		}
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelIcon() {
		return channelIcon;
	}

	public void setChannelIcon(String channelIcon) {
		this.channelIcon = channelIcon;
	}

	public Date getProgramDate() {
		return programDate;
	}

	public void setProgramDate(Date programDate) {
		this.programDate = programDate;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public String getProgramTitle() {
		return programTitle;
	}

	public void setProgramTitle(String programTitle) {
		this.programTitle = programTitle;
	}

	public String getProgramDescription() {
		return programDescription;
	}

	public void setProgramDescription(String programDescription) {
		this.programDescription = programDescription;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	
}
