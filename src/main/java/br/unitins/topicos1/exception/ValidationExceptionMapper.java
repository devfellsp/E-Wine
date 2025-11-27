package br.unitins.topicos1.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.time.OffsetDateTime;

/**
 * Mapeia ValidationException para HTTP 400 com payload compatível com RFC 7807.
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    private static final Logger LOG = Logger.getLogger(ValidationExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ValidationException exception) {
        String traceId = ExceptionUtils.resolveTraceId();

        LOG.debugf("ValidationException handled (traceId=%s): %s", traceId, exception.getMessage());

        Problem problem = new Problem();
        problem.setType("https://example.com/problems/validation");
        problem.setTitle("Validation Error");
        problem.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
        problem.setDetail(exception.getMessage());
        problem.setInstance(uriInfo != null ? uriInfo.getRequestUri().toString() : null);
        problem.setTimestamp(OffsetDateTime.now());
        problem.setTraceId(traceId);
        problem.setErrors(exception.getFieldErrors());

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(problem)
                .build();
    }
}