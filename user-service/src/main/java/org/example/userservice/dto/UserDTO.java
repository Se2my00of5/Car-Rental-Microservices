package org.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Value;
import org.example.userservice.model.Role;

import java.security.Key;
import java.util.Set;

public enum UserDTO {;
    private interface Id { @Positive Long getId(); }
    private interface Username { @NotBlank String getUsername(); }
    private interface Password { @Positive String getPassword(); }
    private interface Email { @Positive String getEmail(); }
    private interface Roles { @Positive Set<Role> getRoles(); }
    private interface IsActive { @Positive Boolean getIsActive(); }
    private interface AccessToken { @Positive String getAccessToken(); }
    private interface RefreshToken { @Positive String getRefreshToken(); }

    public enum Request{;
        @Value
        public static class Register implements Username, Email, Password {
            String username;
            String email;
            String password;
        }


        @Value
        public static class Login implements Email, Password {
            String email;
            String password;
        }

        @Value
        public static class EditProfile implements Username, Password {
            String username;
            String password;
        }

        @Value
        public static class ProfileById implements Id {
            Long id;
        }
    }

    public enum Response{;
        @Value public static class TokenAndShortUserInfo implements AccessToken, RefreshToken, Id, Email, Password {
            String accessToken;
            String refreshToken;
            Long id;
            String email;
            String password;
        }

        @Value public static class Profile implements Id, Username, Email, Password, Roles {
            Long id;
            String username;
            String email;
            String password;
            Set<Role> roles;
        }
    }
}