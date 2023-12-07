package com.example.jwt.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{

    String resourceName;
    String fieldName;

    public ResourceNotFoundException(String resourceName) {
        super(String.format("%s not found with %s : %s",resourceName));
        this.resourceName = resourceName;
    }
}
