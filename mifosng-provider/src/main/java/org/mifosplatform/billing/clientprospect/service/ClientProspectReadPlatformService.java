package org.mifosplatform.billing.clientprospect.service;

import java.util.Collection;
import java.util.List;

import org.mifosplatform.billing.clientprospect.data.ClientProspectData;
import org.mifosplatform.billing.clientprospect.data.ProspectDetailAssignedToData;
import org.mifosplatform.billing.clientprospect.data.ProspectDetailData;
import org.mifosplatform.billing.clientprospect.data.ProspectPlanCodeData;
import org.mifosplatform.billing.plan.data.PlanCodeData;
import org.mifosplatform.billing.ticketmaster.data.UsersData;

public interface ClientProspectReadPlatformService {

	public ClientProspectData retriveClientProspectTemplate();
	public Collection<ClientProspectData> retriveClientProspect();
	public ProspectDetailData retriveClientProspect(Long clientProspectId);
	public Collection<ProspectPlanCodeData> retrivePlans();
	List<ProspectDetailAssignedToData> retrieveUsers();
	public List<ProspectDetailData> retriveProspectDetailHistory(Long prospectdetailid);
}
