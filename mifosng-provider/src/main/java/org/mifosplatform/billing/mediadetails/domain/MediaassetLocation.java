package org.mifosplatform.billing.mediadetails.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.mifosplatform.billing.media.domain.MediaAsset;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_mediaasset_location")
public class MediaassetLocation extends AbstractPersistable<Long> {

	/*@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;*/

	@Column(name = "language_id")
	private Long languageId;

	@Column(name = "format_type")
	private String formatType;

	@Column(name = "location")
	private String location;
	
	@ManyToOne
    @JoinColumn(name="media_id")
	private MediaAsset mediaAsset;
 
	public MediaassetLocation(){
	}

	public MediaassetLocation(Long languageId, String formatType,
			String location) {
		this.languageId=languageId;
		this.formatType=formatType;
		this.location=location;
	}

	public Long getLanguageId() {
		return languageId;
	}

	public String getFormatType() {
		return formatType;
	}

	public String getLocation() {
		return location;
	}

	public MediaAsset getMediaAsset() {
		return mediaAsset;
	}

	public void update(MediaAsset mediaAsset) {
		this.mediaAsset=mediaAsset;
		
	}
		

}
