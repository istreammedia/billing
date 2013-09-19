package org.mifosplatform.billing.eventorder.service;



import org.mifosplatform.billing.eventorder.domain.PrepareRequest;
import org.mifosplatform.billing.eventorder.domain.PrepareRequsetRepository;
import org.mifosplatform.billing.order.data.OrderStatusEnumaration;
import org.mifosplatform.billing.order.domain.Order;
import org.mifosplatform.billing.plan.domain.Plan;
import org.mifosplatform.billing.plan.domain.StatusTypeEnum;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class PrepareRequestWriteplatformServiceImpl implements PrepareRequestWriteplatformService{
	private final PlatformSecurityContext context;
	private final PrepareRequsetRepository prepareRequsetRepository; 

	@Autowired
	public PrepareRequestWriteplatformServiceImpl(final PlatformSecurityContext context,
			final PrepareRequsetRepository prepareRequsetRepository	) {
		this.context = context;
		this.prepareRequsetRepository=prepareRequsetRepository;

	}

	@Override
	public CommandProcessingResult prepareNewRequest(Order eventOrder,Plan plan,String requestType) {
  
		try{
			this.context.authenticatedUser();
			//String requstStatus= SavingStatusEnumaration.interestCompoundingPeriodType(StatusTypeEnum.CONNECT).getValue();
		
			PrepareRequest prepareRequest=new PrepareRequest(eventOrder.getClientId(),eventOrder.getId(),requestType,plan.getProvisionSystem(),'N',"NONE",plan.getPlanCode());
			
			this.prepareRequsetRepository.save(prepareRequest);
			
			return CommandProcessingResult.commandOnlyResult(prepareRequest.getId());
          			
		} catch (DataIntegrityViolationException dve) {
			return CommandProcessingResult.empty();
		
	}
	}

	

}
