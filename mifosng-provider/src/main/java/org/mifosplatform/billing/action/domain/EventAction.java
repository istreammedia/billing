
package org.mifosplatform.billing.action.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_event_action")
public class EventAction extends AbstractPersistable<Long> {

	@Column(name = "action_name", nullable = false, length = 20)
	private String actionName;

	@Column(name = "process", nullable = false, length = 100)
	private String process;

	@Column(name = "event_id", nullable = false, length = 100)
	private Long eventId;

	@Column(name = "isCheck")
	private char isCheck='N';


public EventAction()
{}


public void update() {
	if(this.isCheck!='Y'){
		this.isCheck='Y';
	}
	
}


}
