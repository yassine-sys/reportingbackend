package com.example.backend.entities.DTOResp;

import com.example.backend.entities.Role;

public class TokenValidity {
    private boolean valid;
    private Role role;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
