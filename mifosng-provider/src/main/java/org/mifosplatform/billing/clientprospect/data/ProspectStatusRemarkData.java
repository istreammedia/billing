package org.mifosplatform.billing.clientprospect.data;

import java.util.List;

public class ProspectStatusRemarkData {
	
	
	private Long statusRemarkId;
	private String statusRemark;
	
	private List<ProspectStatusRemarkData> statusRemarkData;
	
	public ProspectStatusRemarkData(final Long statusRemarkId, final String statusRemark){
		this.statusRemarkId = statusRemarkId;
		this.statusRemark = statusRemark;
	}
	
	public String getStatusRemark() {
		return statusRemark;
	}
	public Long getStatusRemarkId() {
		return statusRemarkId;
	}
	public void setStatusRemark(String statusRemark) {
		this.statusRemark = statusRemark;
	} 
	 
	public void setStatusRemarkId(Long statusRemarkId) {
		this.statusRemarkId = statusRemarkId;
	}
	
	public List<ProspectStatusRemarkData> getStatusRemarkData() {
		return statusRemarkData;
	}

	public void setStatusRemarkData(List<ProspectStatusRemarkData> statusRemarkData) {
		this.statusRemarkData = statusRemarkData;
	}

	public ProspectStatusRemarkData() {
		
	}
}
