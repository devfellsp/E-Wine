package br.unitins.topicos1.sga.exception;

import org.jboss.logging.MDC;
import java.util.UUID;

public final class ExceptionUtils {

    private ExceptionUtils() { }

    public static String resolveTraceId() {
        try {
            Object md = MDC.get("traceId");  // tenta pegar do contexto de tracing
            if (md != null) return md.toString();
        } catch (Throwable ignored) { }
        return UUID.randomUUID().toString();  // fallback: gera UUID aleatório
    }
}