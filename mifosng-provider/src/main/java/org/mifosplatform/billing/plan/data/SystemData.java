package org.mifosplatform.billing.plan.data;

public class SystemData {
	private final Long id;
	private final String provisioingSystem;

	public SystemData(Long id, String provisioingSystem) {
		
		this.id=id;
		this.provisioingSystem=provisioingSystem;
	}

	public Long getId() {
		return id;
	}

	public String getProvisioingSystem() {
		return provisioingSystem;
	}
	
	

}
