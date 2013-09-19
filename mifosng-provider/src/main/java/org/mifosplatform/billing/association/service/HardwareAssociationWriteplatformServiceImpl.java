package org.mifosplatform.billing.association.service;

import org.mifosplatform.billing.association.domain.HardwareAssociation;
import org.mifosplatform.billing.order.domain.HardwareAssociationRepository;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class HardwareAssociationWriteplatformServiceImpl implements HardwareAssociationWriteplatformService
{

	private final PlatformSecurityContext context;
	private final HardwareAssociationRepository associationRepository;
	
@Autowired
	public HardwareAssociationWriteplatformServiceImpl(final PlatformSecurityContext context,
			final HardwareAssociationRepository associationRepository){
		this.context=context;
		this.associationRepository=associationRepository;
	}
	
	@Override
	public void createNewHardwareAssociation(Long clientId, Long planId,String serialNo,Long orderId) 
	{
	        try{
	        	
	        	this.context.authenticatedUser();
	        	HardwareAssociation hardwareAssociation=new HardwareAssociation(clientId,planId,serialNo,orderId);
	        	this.associationRepository.saveAndFlush(hardwareAssociation);
	        	
	        }catch(DataIntegrityViolationException exception){
	        	exception.printStackTrace();
	        }
		
	}

}
