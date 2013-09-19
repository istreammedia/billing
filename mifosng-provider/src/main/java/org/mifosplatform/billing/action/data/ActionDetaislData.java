package org.mifosplatform.billing.action.data;

public class ActionDetaislData {
	
	private final Long id;
	private final String procedureName;
	private final String actionType;

	public ActionDetaislData(Long id, String procedureName, String actionType) {
            
		this.id=id;
		this.procedureName=procedureName;
		this.actionType=actionType;

	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the procedureName
	 */
	public String getProcedureName() {
		return procedureName;
	}

	/**
	 * @return the actionType
	 */
	public String getActionType() {
		return actionType;
	}
	
	

}
