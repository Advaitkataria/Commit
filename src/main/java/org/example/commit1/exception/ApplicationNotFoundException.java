package org.example.commit1.exception;

public class ApplicationNotFoundException extends RuntimeException {
    public ApplicationNotFoundException(int id) {
        super("Applcation with id " + id + "not found");
    }
    public ApplicationNotFoundException(){
        super("No Applications found");
    }
}
