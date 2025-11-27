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
 * Captura exceções não tratadas e devolve Problem genérico (HTTP 500).
 * Não expõe stack trace ao cliente. Inclui traceId para debugging interno.
 */
@Provider
public class UncaughtExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(UncaughtExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Exception exception) {
        String traceId = ExceptionUtils.resolveTraceId();

        // log com stacktrace para debugging interno
        LOG.errorf(exception, "Unexpected exception (traceId=%s): %s", traceId, exception.getMessage());

        Problem problem = new Problem();
        problem.setType("https://example.com/problems/internal-server-error");
        problem.setTitle("Internal Server Error");
        problem.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        problem.setDetail("Ocorreu um erro interno. Use o traceId para rastrear o problema.");
        problem.setInstance(uriInfo != null ? uriInfo.getRequestUri().toString() : null);
        problem.setTimestamp(OffsetDateTime.now());
        problem.setTraceId(traceId);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(problem)
                .build();
    }
}