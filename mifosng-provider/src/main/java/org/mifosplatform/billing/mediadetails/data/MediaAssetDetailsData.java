package org.mifosplatform.billing.mediadetails.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.mifosplatform.billing.eventpricing.data.EventPricingData;
import org.mifosplatform.billing.mediadetails.domain.MediaassetAttributes;
import org.mifosplatform.billing.mediadetails.domain.MediaassetLocation;

public class MediaAssetDetailsData {


	private Long mediaId;
	private String title;
	private final String type;
	private final String classType;
	private final String overview;
	private final String subject;
	private final String image;
	private final String duration;
	private final String contentProvider;
	private final String rated;
	private final BigDecimal rating;
	private final int ratingCount;
	private final String location;
	private final String status;
	private final Date releaseDate;
	private final List<String> Genre;
	private final List<String> Producer;
	private final List<MediaLocationData> filmLocations;
	private  List<MediaassetLocation> filmLocationsdata;
	private  List<MediaassetAttributes> mediaassetAttributes; 
	private final List<String> Writer;
	private final List<String> Director;
	private final List<String> Actor;
	private final List<String> countries;
	private List<EventPricingData> priceDetails;

	public MediaAssetDetailsData(Long mediaId, String title, String type,
			String classType, String overview, String subject, String image,
			String duration, String contentProvider, String rated,
			BigDecimal rating, int ratingCount, String location, String status,
			Date releaseDate, List<String> genres, List<String> production, List<MediaLocationData> filmingLocation,
			List<String> writers, List<String> directors, List<String> actors, List<String> country, List<EventPricingData> eventPricingData) {
		this.mediaId=mediaId;
		this.title=title;
		this.type=type;
		this.classType=classType;
		this.overview=overview;
		this.subject=subject;
		this.image=image;
		this.duration=duration;
		this.contentProvider=contentProvider;
		this.rated=rated;
		this.rating=rating;
		this.ratingCount=ratingCount;
		this.location=location;
		this.status=status;
		this.releaseDate=releaseDate;
		this.Genre=genres;
		this.Producer=production;
		this.filmLocations=filmingLocation;
		this.Writer=writers;
		this.Director=directors;
		this.Actor=actors;
		this.countries=country;
		this.priceDetails=eventPricingData;
		
	}

	

	
	public Long getMediaId() {
		return mediaId;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public String getClassType() {
		return classType;
	}

	public String getOverview() {
		return overview;
	}

	public List<String> getGenres() {
		return Genre;
	}

	public List<String> getProduction() {
		return Producer;
	}

	public List<MediaLocationData> getFilmLocations() {
		return filmLocations;
	}

	public List<String> getWriters() {
		return Writer;
	}

	public List<String> getDirectors() {
		return Director;
	}

	public List<String> getActors() {
		return Actor;
	}

	public List<String> getCountries() {
		return countries;
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

	public BigDecimal getRating() {
		return rating;
	}

	public int getRatingCount() {
		return ratingCount;
	}

	public String getLocation() {
		return location;
	}

	public String getStatus() {
		return status;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

}
