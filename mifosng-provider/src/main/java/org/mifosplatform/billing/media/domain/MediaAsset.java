package org.mifosplatform.billing.media.domain;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.joda.time.LocalDate;
import org.mifosplatform.billing.mediadetails.domain.MediaassetAttributes;
import org.mifosplatform.billing.mediadetails.domain.MediaassetLocation;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_media_asset")
public class MediaAsset extends  AbstractPersistable<Long> {

	/*@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;*/

	@Column(name = "category_id")
	private Long categoryId;

	@Column(name = "status")
	private String status;

	@Column(name = "title")
	private String title;

	@Column(name = "type")
	private String type;

	@Column(name = "genre")
	private String genre;

	@Column(name = "release_date")
	private Date releaseDate;

	@Column(name = "overview")
	private String overview;

	@Column(name = "subject")
	private String subject;
	
	@Column(name = "image")
	private String image;
	
	@Column(name = "duration")
	private String duration;
	
	@Column(name = "content_provider")
	private String contentProvider;

	@Column(name = "rated")
	private String rated;

	@Column(name = "rating_count")
	private Long ratingCount;

	@Column(name = "rating", scale = 6, precision = 19, nullable = false)
	private BigDecimal rating;

	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "mediaAsset", orphanRemoval = true)
	private List<MediaassetAttributes> mediaassetAttributes = new ArrayList<MediaassetAttributes>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "mediaAsset", orphanRemoval = true)
	private List<MediaassetLocation> mediaassetLocations = new ArrayList<MediaassetLocation>();

	@Column(name = "is_deleted")
	private char isDeleted = 'N';

	 public  MediaAsset() {
		// TODO Auto-generated constructor stub
	}
	 public MediaAsset(String mediaTitle, String mediaType,
				Long mediaCategoryId, Date releaseDate, String genre, Long ratingCount,
				String overview, String subject, String image, String duration,
				String contentProvider, String rated, BigDecimal rating,
				String status) {
		 
		this.title=mediaTitle;
		this.type=mediaType;
		this.categoryId=mediaCategoryId;
		this.releaseDate=releaseDate;
		this.genre=genre;
		this.ratingCount=ratingCount;
		this.overview=overview;
		this.image=image;
		this.duration=duration;
		this.contentProvider=contentProvider;
		this.rated=rated;
		this.rating=rating;
		this.status=status;
		this.subject=subject;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public String getStatus() {
		return status;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	
	public Date getReleaseDate() {
		return releaseDate;
	}

	public String getOverview() {
		return overview;
	}

	public String getSubject() {
		return subject;
	}

	public String getImage() {
		return image;
	}

	public String getDuration() {
		return duration;
	}

	public String getContentProvider() {
		return contentProvider;
	}

	public String getRated() {
		return rated;
	}

	public Long getRatingCount() {
		return ratingCount;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public List<MediaassetAttributes> getMediaassetAttributes() {
		return mediaassetAttributes;
	}

	public List<MediaassetLocation> getMediaassetLocations() {
		return mediaassetLocations;
	}

	public String getGenre() {
		return genre;
	}
	
	public static MediaAsset fromJson(JsonCommand command) {
		
		 final String mediaTitle = command.stringValueOfParameterNamed("mediaTitle");
		 final String mediaType = command.stringValueOfParameterNamed("mediaType");
		 final LocalDate releaseDate = command.localDateValueOfParameterNamed("releaseDate");
		 final Long mediaCategoryId = command.longValueOfParameterNamed("mediaCategoryId");
		 final String genre = command.stringValueOfParameterNamed("genre");
		 final Long ratingCount = command.longValueOfParameterNamed("ratingCount");
		 final String overview=command.stringValueOfParameterNamed("overview");
		 final String subject=command.stringValueOfParameterNamed("subject");
		 final String image=command.stringValueOfParameterNamed("image");
		 final String duration=command.stringValueOfParameterNamed("duration");
		 final String contentProvider=command.stringValueOfParameterNamed("contentProvider");
		 final String rated=command.stringValueOfParameterNamed("rated");
		 final BigDecimal rating=command.bigDecimalValueOfParameterNamed("rating");
		 final String status=command.stringValueOfParameterNamed("status");
		 return new MediaAsset(mediaTitle,mediaType,mediaCategoryId,releaseDate.toDate(),genre,ratingCount,overview,subject,
					image,duration,contentProvider,rated,rating,status);
	}

	public void add(MediaassetAttributes attributes) {
		attributes.update(this);
		mediaassetAttributes.add(attributes);
		
	}

	public void addMediaLocations(MediaassetLocation mediaassetLocation) {
           mediaassetLocation.update(this);
           this.mediaassetLocations.add(mediaassetLocation);
	}

	public java.util.Map<String, Object> updateAssetDetails(JsonCommand command) {

		final LinkedHashMap<String, Object> actualChanges = new LinkedHashMap<String, Object>(1);
	
		final String rating="rating";
		if(command.isChangeInBigDecimalParameterNamed(rating, this.rating)){
			final BigDecimal newValue=command.bigDecimalValueOfParameterNamed("rating");
			actualChanges.put(rating, newValue);
			this.rating=newValue;
		}
		final String ratingCount="ratingCount";
		if(command.isChangeInLongParameterNamed(ratingCount, this.ratingCount)){
			final Long newValue=command.longValueOfParameterNamed("ratingCount");
			actualChanges.put(ratingCount, newValue);
			this.ratingCount=newValue;
		}
		final String rated = "rated";
		if (command.isChangeInStringParameterNamed(rated,this.rated)) {
			final String newValue = command
					.stringValueOfParameterNamed("rated");
			actualChanges.put(rated, newValue);
			this.rated = StringUtils.defaultIfEmpty(newValue, null);
		}
		
		final String contentProvider = "contentProvider";
		if (command.isChangeInStringParameterNamed(contentProvider,this.contentProvider)) {
			final String newValue=command.stringValueOfParameterNamed("contentProvider");
			actualChanges.put(contentProvider, newValue);
			this.contentProvider = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String duration = "duration";
		if (command.isChangeInStringParameterNamed(duration,this.duration)) {
			final String newValue=command.stringValueOfParameterNamed("duration");
			actualChanges.put(duration, newValue);
			this.duration = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String image = "image";
		if (command.isChangeInStringParameterNamed(image,this.image)) {
			final String newValue=command.stringValueOfParameterNamed("image");
			actualChanges.put(image, newValue);
			this.image = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String subject = "subject";
		if (command.isChangeInStringParameterNamed(subject,
				this.subject)) {
			final String newValue = command
					.stringValueOfParameterNamed("subject");
			actualChanges.put(subject, newValue);
			this.subject = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String overview = "overview";
		if (command.isChangeInStringParameterNamed(overview,
				this.overview)) {
			final String newValue =command.stringValueOfParameterNamed("overview");
			actualChanges.put(overview, newValue);
			this.overview = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String releaseDate = "releaseDate";
		if (command.isChangeInLocalDateParameterNamed(releaseDate,new LocalDate(this.releaseDate))) {
			 final LocalDate newValue = command.localDateValueOfParameterNamed("releaseDate");
			actualChanges.put(releaseDate, newValue);
			this.releaseDate = newValue.toDate();
		}
		final String genre = "genre";
		if (command.isChangeInStringParameterNamed(genre,this.genre)) {
			final String newValue = command
					.stringValueOfParameterNamed("genre");
			actualChanges.put(genre, newValue);
			this.genre = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String mediaType = "mediaType";
		if (command.isChangeInStringParameterNamed(mediaType,
				this.type)) {
			final String newValue = command.stringValueOfParameterNamed("mediaType");
			actualChanges.put(mediaType, newValue);
			this.type = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String mediaTitle = "mediaTitle";
		if (command.isChangeInStringParameterNamed(mediaTitle,this.title)) {
			final String newValue = command
					.stringValueOfParameterNamed("mediaTitle");
			actualChanges.put(title, newValue);
			this.title = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String status = "status";
		if (command.isChangeInStringParameterNamed(status,
				this.status)) {
			final String newValue = command
					.stringValueOfParameterNamed("status");
			actualChanges.put(status, newValue);
			this.status = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String mediaCategoryId="mediaCategoryId";
		if(command.isChangeInLongParameterNamed(mediaCategoryId, this.categoryId)){
			final Long newValue=command.longValueOfParameterNamed("mediaCategoryId");
			actualChanges.put(mediaCategoryId, newValue);
			this.categoryId=newValue;
		}
		return actualChanges;
	}

	public void isDelete() {
		if(this.isDeleted!= 'Y'){
			this.isDeleted='Y';
		}
		
	}

	
	 
	 
			
	}
 
	
	
