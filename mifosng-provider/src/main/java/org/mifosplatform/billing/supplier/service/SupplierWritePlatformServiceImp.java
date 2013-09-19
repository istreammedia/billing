package org.mifosplatform.billing.supplier.service;

import org.mifosplatform.billing.supplier.domain.Supplier;
import org.mifosplatform.billing.supplier.domain.SupplierJpaRepository;
import org.mifosplatform.billing.supplier.serialization.SupplierCommandFromApiJsonDeserializer;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class SupplierWritePlatformServiceImp implements
		SupplierWritePlatformService {

	private final static Logger logger = (Logger) LoggerFactory.getLogger(SupplierWritePlatformServiceImp.class);
	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;
	private final SupplierJpaRepository supplierJpaRepository;
	private final SupplierCommandFromApiJsonDeserializer apiJsonDeserializer;
	
	@Autowired
	public SupplierWritePlatformServiceImp(final PlatformSecurityContext context
			, final TenantAwareRoutingDataSource dataSource
			, final SupplierJpaRepository  supplierJpaRepository
			, final SupplierCommandFromApiJsonDeserializer apiJsonDeserializer) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.supplierJpaRepository = supplierJpaRepository;
		this.apiJsonDeserializer = apiJsonDeserializer;
	}
	
	@Override
	public CommandProcessingResult createSupplier(JsonCommand command) {
		try{
		
		context.authenticatedUser();
		apiJsonDeserializer.validateForCreate(command.json());
		Supplier supplier = Supplier.formJson(command);
		supplierJpaRepository.save(supplier);
		return new CommandProcessingResultBuilder().withEntityId(supplier.getId()).build();
		}catch (DataIntegrityViolationException dve) {
		        handleDataIntegrityIssues(command, dve);
		        return  CommandProcessingResult.empty();
		}
	}
			
	private void handleDataIntegrityIssues(final JsonCommand command, final DataIntegrityViolationException dve) {

	    Throwable realCause = dve.getMostSpecificCause();
	   if (realCause.getMessage().contains("serial_no_constraint")){
	   	throw new PlatformDataIntegrityException("validation.error.msg.inventory.mrn.duplicate.entry", "validation.error.msg.inventory.mrn.duplicate.entry", "validation.error.msg.inventory.mrn.duplicate.entry","");
	   	
	   }
	   logger.error(dve.getMessage(), dve);   	
	}

}
