package com.netflix.conductorboot.lib.embedded.elastic;

@SuppressWarnings("serial")
public class EmbeddedElasticsearchStartupException extends RuntimeException {

    public EmbeddedElasticsearchStartupException(String message) {
        super(message);
    }

    public EmbeddedElasticsearchStartupException(Throwable cause) {
        super(cause);
    }

    public EmbeddedElasticsearchStartupException(String message, Throwable cause) {
        super(message, cause);
    }
}
