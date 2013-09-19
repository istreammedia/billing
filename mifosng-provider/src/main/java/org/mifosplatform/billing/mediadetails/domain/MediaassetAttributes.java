package org.mifosplatform.billing.mediadetails.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.mifosplatform.billing.media.domain.MediaAsset;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_mediaasset_attributes")
public class MediaassetAttributes extends AbstractPersistable<Long>  {



@ManyToOne
@JoinColumn(name="media_id")
private MediaAsset mediaAsset;

	@Column(name = "attribute_type")
	private String medaType;

	@Column(name = "attribute_name")
	private String mediaName;

	@Column(name = "attribute_value")
	private String mediaValue;
	
	@Column(name = "attribute_nickname")
	private String attributeNickname;
	
	@Column(name = "attribute_image")
	private String attributeImage;
	
	

	public MediaassetAttributes()
	{}

	
	public MediaassetAttributes(String mediaAttributeType,String mediaattributeName, String mediaattributeValue,
			String mediaattributeNickname, String mediaattributeImage) {
         this.mediaName=mediaattributeName;
         this.medaType=mediaAttributeType;
         this.mediaValue=mediaattributeValue;
         this.attributeNickname=mediaattributeNickname;
         this.attributeImage=mediaattributeImage;
	
	}

	public MediaAsset getMediaAsset() {
		return mediaAsset;
	}

	public String getMedaiType() {
		return medaType;
	}

	public String getMediaName() {
		return mediaName;
	}

	public String getMediaValue() {
		return mediaValue;
	}

	public String getAttributeNickname() {
		return attributeNickname;
	}

	public String getAttributeImage() {
		return attributeImage;
	}

	

	public void update(MediaAsset mediaAsset) {

		this.mediaAsset=mediaAsset;
	}

	
}
