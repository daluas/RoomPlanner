package edu.roomplanner.validation;

public class ValidationResult {

    private String error;

    public ValidationResult() {
    }

    public ValidationResult(String error) {
        this.error = error;
    }

    public boolean isError() {
        return error != null;
    }

    public String getError() {
        return error;
    }

}
