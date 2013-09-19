package org.mifosplatform.billing.address.data;

public class StateDetails  {
	
	private Long id;
	private String stateName;

	public StateDetails(Long id, String stateName) {

	    this.id=id;
	    this.stateName=stateName;
	
	}

	public Long getId() {
		return id;
	}

	public String getStateName() {
		return stateName;
	}

}
