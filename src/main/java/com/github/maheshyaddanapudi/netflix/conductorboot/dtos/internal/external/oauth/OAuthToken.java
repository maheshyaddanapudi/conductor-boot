package com.github.maheshyaddanapudi.netflix.conductorboot.dtos.internal.external.oauth;

public class OAuthToken {
   private String[] Role;
   private RealmAccess realm_access;

    public OAuthToken(String[] role, RealmAccess realm_access) {
        this.Role = role;
        this.realm_access = realm_access;
    }

    public RealmAccess getRealm_access() {
        return realm_access;
    }

    public void setRealm_access(RealmAccess realm_access) {
        this.realm_access = realm_access;
    }

    public OAuthToken() {

    }

    public String[] getRole() {
        return Role;
    }

    public void setRole(String[] role) {
        Role = role;
    }
}
