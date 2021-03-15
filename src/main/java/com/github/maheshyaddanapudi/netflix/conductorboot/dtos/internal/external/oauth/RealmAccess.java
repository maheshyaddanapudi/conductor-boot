package com.github.maheshyaddanapudi.netflix.conductorboot.dtos.internal.external.oauth;

import java.util.Arrays;

public class RealmAccess {

    private String[] roles;

    public RealmAccess(String[] roles) {
        this.roles = roles;
    }

    public RealmAccess() {
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "RealmAccess{" +
                "roles=" + Arrays.toString(roles) +
                '}';
    }
}
