package edu.roomplanner.validation;

public class ValidationResult {
    private String error;

    public ValidationResult(String error){
        this.error = error;
    }

    public boolean isError(){
        return error != null;
    }
}
