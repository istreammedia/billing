package org.mifosplatform.billing.media.data;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.mediadetails.data.MediaLocationData;
import org.mifosplatform.billing.paymode.data.McodeData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.data.MediaEnumoptionData;

public class MediaAssetData {


	private final Long mediaId;
	private final String mediaTitle;
	private final String mediaImage;
	private final BigDecimal mediaRating;
	private final Long eventId;
	private Long noOfPages;
	private Long pageNo;
	private String assetTag;
	private List<EnumOptionData> mediaStatus;
	private List<MediaassetAttribute> mediaAttributes;
	private List<MediaassetAttribute> mediaFormat;
	private String status;
	private LocalDate releaseDate;
	private List<MediaEnumoptionData> mediaTypeData;
	private List<McodeData> mediaCategeorydata;
	private List<McodeData> mediaLanguageData;
	private MediaAssetData mediaAssetData;
	private List<MediaLocationData> mediaLocationData;
	private List<MediaassetAttributeData> mediaassetAttributes;
	private String mediatype;
	private String genre;
	private Long catageoryId;
	private String subject;
	private String overview;
	private String contentProvider;
	private String rated;
	private BigDecimal rating;
	private String duration;
	private Long ratingCount;
	
public MediaAssetData(final Long mediaId,final String mediaTitle,final String image,final BigDecimal rating, Long eventId, String assetTag){
	this.mediaId=mediaId;
	this.mediaTitle=mediaTitle;
	this.mediaImage=image;
	this.mediaRating=rating;
	this.eventId=eventId;
	this.assetTag=assetTag;
}
public MediaAssetData(Long noOfPages, Long pageNo) {
	this.mediaId=null;
	this.mediaTitle=null;
	this.mediaImage=null;
	this.mediaRating=null;
	this.eventId=null;
	this.noOfPages=noOfPages;
	this.pageNo=pageNo;
}
public MediaAssetData(MediaAssetData mediaAssetData, List<MediaassetAttributeData> mediaassetAttributes, List<MediaLocationData> mediaLocationData, List<EnumOptionData> status,List<MediaassetAttribute> data, List<MediaassetAttribute> mediaFormat,
		List<MediaEnumoptionData> mediaTypeData, List<McodeData> mediaCategeorydata,List<McodeData> mediaLangauagedata) {

	this.mediaAssetData=mediaAssetData;
	this.mediaStatus=status;
	this.mediaAttributes=data;
	this.mediaFormat=mediaFormat;
	this.mediaId=null;
	this.mediaTitle=null;
	this.mediaImage=null;
	this.mediaRating=null;
	this.eventId=null;
	this.mediaTypeData=mediaTypeData;
	this.mediaCategeorydata=mediaCategeorydata;
	this.mediaLanguageData=mediaLangauagedata;
	this.mediaLocationData=mediaLocationData;
	this.mediaassetAttributes=mediaassetAttributes;
	
}
public MediaAssetData(Long mediaId, String mediaTitle, String status,
		LocalDate releaseDate, BigDecimal rating) {
          this.mediaId=mediaId;
          this.mediaTitle=mediaTitle;
          this.status=status;
          this.releaseDate=releaseDate;
          this.mediaRating=rating;
      	this.mediaImage=null;
      	this.eventId=null;
}

public MediaAssetData(Long mediaId, String mediatitle, String type,
		String genre, Long catageoryId, LocalDate releaseDate, String subject,
		String overview, String image, String contentProvider, String rated,
		BigDecimal rating, Long ratingCount, String status, String duration) {
	// TODO Auto-generated constructor stub
	 this.mediaId=mediaId;
     this.mediaTitle=mediatitle;
     this.mediatype=type;
     this.genre=genre;
     this.catageoryId=catageoryId;
     this.releaseDate=releaseDate;
     this.subject=subject;
     this.overview=overview;
     this.mediaImage=image;
     this.contentProvider=contentProvider;
     this.rated=rated;
     this.mediaRating=rating;
     this.rating=rating;
     this.ratingCount=ratingCount;
     this.duration=duration;
     this.status=status;
    
 	this.eventId=null;
}
public Long getMediaId() {
	return mediaId;
}
public String getMediaTitle() {
	return mediaTitle;
}
public String getMediaImage() {
	return mediaImage;
}
public BigDecimal getMediaRating() {
	return mediaRating;
}
public Long getEventId() {
	return eventId;
}
	

}
