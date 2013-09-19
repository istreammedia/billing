package org.mifosplatform.billing.mediadevice.data;

import java.util.List;

public class MediaDeviceData {
	
	
	private  Long deviceId;
	private  Long clientId;
	private  String clientType;
	private Long clientTypeId;
	
	private List<MediaDeviceData> data;

	
	
	public MediaDeviceData(Long deviceId, Long clientId, String clientType, Long clientTypeId) {
           this.deviceId=deviceId;
           this.clientId=clientId;
           this.clientType=clientType;
           this.clientTypeId=clientTypeId;
	}
	public MediaDeviceData(List<MediaDeviceData> data){
		this.data = data;
	}
	

	public Long getDeviceId() {
		return deviceId;
	}

	public Long getClientId() {
		return clientId;
	}

	public String getClientType() {
		return clientType;
	}

	public Long getClientTypeId() {
		return clientTypeId;
	}



	public List<MediaDeviceData> getData() {
		return data;
	}



	public void setData(List<MediaDeviceData> data) {
		this.data = data;
	}



	public void setClientTypeId(Long clientTypeId) {
		this.clientTypeId = clientTypeId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	
	
}
