package br.unitins.topicos1.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exceção customizada para agrupar erros de validação de campos.
 */
public class ValidationException extends RuntimeException {

    private final List<Problem.FieldError> fieldErrors = new ArrayList<>();

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(List<Problem.FieldError> errors) {
        super("Validation failed");
        if (errors != null) this.fieldErrors.addAll(errors);
    }

    public ValidationException addError(String field, String message) {
        this.fieldErrors.add(new Problem.FieldError(field, message));
        return this;
    }

    public List<Problem.FieldError> getFieldErrors() {
        return Collections.unmodifiableList(fieldErrors);
    }
}