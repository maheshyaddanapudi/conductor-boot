package com.github.my.netflix.conductorboot.lib.embedded.elastic;

@SuppressWarnings("serial")
public class InvalidSetupException extends IllegalArgumentException {

    public InvalidSetupException(String message) {
        super(message);
    }
}
