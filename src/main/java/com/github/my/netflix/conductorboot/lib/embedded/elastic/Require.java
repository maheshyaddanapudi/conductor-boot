package com.github.my.netflix.conductorboot.lib.embedded.elastic;

class Require {
    static void require(boolean condition, String message) {
        if (!condition) {
            throw new InvalidSetupException(message);
        }
    }
}
