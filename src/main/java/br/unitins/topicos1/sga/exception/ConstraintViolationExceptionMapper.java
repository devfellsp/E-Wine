package br.unitins.topicos1.sga.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Mapeia ConstraintViolationException (Bean Validation) para HTTP 400.
 */
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger LOG = Logger.getLogger(ConstraintViolationExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        String traceId = 
        ExceptionUtils.resolveTraceId();

        LOG.debugf("ConstraintViolationException handled (traceId=%s): %s", traceId, exception.getMessage());

        List<Problem.FieldError> errors = exception.getConstraintViolations()
                .stream()
                .map(this::toFieldError)
                .collect(Collectors.toList());

        Problem problem = new Problem();
        problem.setType("https://example.com/problems/constraint-violation");
        problem.setTitle("Validation Error");
        problem.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
        problem.setDetail("Um ou mais campos inválidos");
        problem.setInstance(uriInfo != null ? uriInfo.getRequestUri().toString() : null);
        problem.setTimestamp(OffsetDateTime.now());
        problem.setTraceId(traceId);
        problem.setErrors(errors);

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(problem)
                .build();
    }

    private Problem.FieldError toFieldError(ConstraintViolation<?> v) {
        // extrai o último nó do propertyPath como nome do campo
        String field = StreamSupport.stream(v.getPropertyPath().spliterator(), false)
                .map(Object::toString)
                .reduce((first, second) -> second) // último elemento
                .orElse(v.getPropertyPath().toString());
        String message = v.getMessage();
        return new Problem.FieldError(field, message);
    }
}