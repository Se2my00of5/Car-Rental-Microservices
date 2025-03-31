package org.example.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.example.userservice.model.Role;

import java.util.Set;

public enum UserDTO {;
    private interface Id { @Positive Long getId(); }
    private interface Username { @NotBlank String getUsername(); }
    private interface Password { @NotBlank @Size(min = 6) String getPassword(); }
    private interface Email { @jakarta.validation.constraints.Email String getEmail(); }
    private interface Roles { @NotNull Set<Role> getRoles(); }
    private interface IsActive { @NotNull Boolean getIsActive(); }
    private interface AccessToken { @NotBlank String getAccessToken(); }
    private interface RefreshToken { @NotBlank String getRefreshToken(); }
    private interface Message { @NotBlank String getMessage(); }

    public enum Request {;

        @Value
        public static class Register implements Username, Email, Password {

            @NotBlank
            @Schema(description = "Имя пользователя", example = "john_doe")
            String username;


            @NotBlank
            @jakarta.validation.constraints.Email
            @Schema(description = "Email пользователя", example = "john@example.com", format = "email")
            String email;

            @NotBlank
            @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
            @Schema(description = "Пароль", example = "secret123")
            String password;
        }

        @Value
        public static class Login implements Email, Password {

            @jakarta.validation.constraints.Email
            @NotBlank
            @Schema(description = "Email пользователя", example = "john@example.com", format = "email")
            String email;

            @NotBlank
            @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
            @Schema(description = "Пароль", example = "secret123")
            String password;
        }

        @Value
        public static class EditProfile implements Username, Password {

            @NotBlank
            @Schema(description = "Новое имя пользователя", example = "new_username")
            String username;

            @NotBlank
            @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
            @Schema(description = "Новый пароль", example = "newpassword123")
            String password;
        }

        @Value
        public static class RefreshToken implements UserDTO.RefreshToken {

            @NotBlank
            @Schema(description = "Refresh токен", example = "eyJhbGciOiJIUzI1NiIsInR...")
            String refreshToken;
        }

        @Value
        public static class AccessToken implements UserDTO.AccessToken {

            @NotBlank
            @Schema(description = "Access токен", example = "eyJhbGciOiJIUzI1NiIsInR...")
            String accessToken;
        }
    }

    public enum Response {;

        @Value
        public static class TokenAndShortUserInfo implements AccessToken, RefreshToken, Id, Email, Password {

            @NotBlank
            @Schema(description = "Access токен", example = "eyJhbGciOiJIUzI1NiIsInR...")
            String accessToken;

            @NotBlank
            @Schema(description = "Refresh токен", example = "eyJhbGciOiJIUzI1NiIsInR...")
            String refreshToken;

            @Positive
            @Schema(description = "ID пользователя", example = "1")
            Long id;

            @jakarta.validation.constraints.Email
            @NotBlank
            @Schema(description = "Email пользователя", example = "john@example.com")
            String email;

            @NotBlank
            @Size(min = 6)
            @Schema(description = "Пароль", example = "secret123")
            String password;
        }

        @Value
        public static class Profile implements Id, Username, Email, Password, Roles {

            @Positive
            @Schema(description = "ID пользователя", example = "1")
            Long id;

            @NotBlank
            @Schema(description = "Имя пользователя", example = "john_doe")
            String username;

            @jakarta.validation.constraints.Email
            @NotBlank
            @Schema(description = "Email", example = "john@example.com")
            String email;

            @NotBlank
            @Size(min = 6)
            @Schema(description = "Пароль", example = "secret123")
            String password;

            @NotNull
            @Schema(description = "Список ролей пользователя", example = "[\"ROLE_USER\"]")
            Set<Role> roles;
        }

        @Value
        public static class GetMessage implements Message {

            @NotBlank
            @Schema(description = "Сообщение", example = "Операция выполнена успешно")
            String message;
        }

        @Value
        public static class GetToken implements AccessToken {

            @NotBlank
            @Schema(description = "Access токен", example = "eyJhbGciOiJIUzI1NiIsInR...")
            String accessToken;
        }
    }
}