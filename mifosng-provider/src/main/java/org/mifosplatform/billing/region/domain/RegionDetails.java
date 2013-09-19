package org.mifosplatform.billing.region.domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_priceregion_detail",uniqueConstraints = @UniqueConstraint(name = "state_id_key", columnNames = { "state_id" }))
public class RegionDetails extends AbstractPersistable<Long>{

	@ManyToOne
    @JoinColumn(name="priceregion_id")
    private RegionMaster regionMaster;

	@Column(name ="country_id")
    private Long countryId;

	@Column(name="state_id")
	private Long stateId;

	@Column(name = "is_deleted", nullable = false)
	private char isDeleted = 'N';

	
	public RegionDetails(){
		
		
	}

	public RegionDetails(Long countryId, Long stateId)
	{
		
		this.countryId=countryId;
		this.stateId=stateId;
	}
	
	public void update(RegionMaster regionMaster) {
		this.regionMaster=regionMaster;
		
	}

	public void delete() {
		this.isDeleted='Y';
		this.stateId=null;
		
	}



}