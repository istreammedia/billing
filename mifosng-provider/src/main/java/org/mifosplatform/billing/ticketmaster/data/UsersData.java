package org.mifosplatform.billing.ticketmaster.data;

public class UsersData {
	
	private Long id;
	private String userName;

	public UsersData(Long id, String username) {
           this.id=id;
           this.userName=username;
	
	}

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	
}
