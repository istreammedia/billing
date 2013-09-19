package org.mifosplatform.portfolio.client.service;

public class ClientCategoryData {
	
	private final Long id;
	private final String categoryType;

	public ClientCategoryData(Long id, String categoryType) {
           this.id=id;
           this.categoryType=categoryType;
	
	}

	public Long getId() {
		return id;
	}

	public String getCategoryType() {
		return categoryType;
	}

	
}
