package org.mifosplatform.infrastructure.core.exceptionmapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.mifosplatform.billing.inventory.exception.AbstractInventoryItemDetailsExist;
import org.mifosplatform.billing.inventory.exception.ApiInventoryErrorResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Provider
@Component
@Scope("singleton")
public class AbstractInventoryItemDetailsExistMapper implements ExceptionMapper<AbstractInventoryItemDetailsExist> {

    @Override
    public Response toResponse(AbstractInventoryItemDetailsExist exception) {

        ApiInventoryErrorResponse dataIntegrityError = ApiInventoryErrorResponse.dataIntegrityError(exception.getStatus(),exception.getErrorMessage(),exception.getResourceIdentifier(),exception.getParameterName());
       // ApiParameterError error = ApiParameterError.generalError(dataIntegrityError.getStatusCode(),dataIntegrityError.getErrorMessage());
        
        return Response.status(Status.FORBIDDEN).entity(dataIntegrityError).type(MediaType.APPLICATION_JSON).build();
       }
}
