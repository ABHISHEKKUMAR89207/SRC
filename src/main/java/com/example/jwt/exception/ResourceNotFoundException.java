package com.example.jwt.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{

    String resourceName;
    String fieldName;
    Long fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName) {
        super(String.format("%s not found with %s : %s",resourceName,fieldName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
       // this.fieldValue = fieldValue;
    }



}
