package org.example.userservice.service;

import exception.AuthenticationException;
import exception.BadRequestException;
import exception.NotFoundException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.UserDTO;
import org.example.userservice.model.User;
import org.example.userservice.repository.RoleRepository;
import org.example.userservice.repository.UserRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    public UserDTO.Response.TokenAndShortUserInfo login(
            @NonNull UserDTO.Request.Login authRequest
    ) {
        final User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (user.getPassword().equals(authRequest.getPassword())) {
            if (!user.isActive()) {
                throw new NotFoundException("Аккаунт удалён");
            }

            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getEmail(), refreshToken);

            return new UserDTO.Response.TokenAndShortUserInfo(accessToken, refreshToken, user.getId(), user.getEmail(), user.getPassword());
        } else {
            throw new BadRequestException("Неправильный пароль");
        }
    }

    public UserDTO.Response.TokenAndShortUserInfo registration(
            @NonNull UserDTO.Request.Register userData
    ) {
        if (userRepository.findByEmail(userData.getEmail()).isPresent()) {
            throw new BadRequestException("этот email уже занят");
        }


        User user = User.builder()
                .username(userData.getUsername())
                .email(userData.getEmail())
                .password(userData.getPassword())
                .roles(Set.of(roleRepository.findByName("ROLE_USER")
                        .orElseThrow(() -> new BadRequestException("такой роли нет")))
                )
                .isActive(true)
                .build();

        userRepository.save(user);

        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getEmail(), refreshToken);

        return new UserDTO.Response.TokenAndShortUserInfo(accessToken, refreshToken, user.getId(), user.getEmail(), user.getPassword());
    }


    public UserDTO.Response.GetToken getAccessToken(@NonNull UserDTO.Request.RefreshToken token) {
        String refreshToken = token.getRefreshToken();

        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new AuthenticationException("invalid token"));
                return new UserDTO.Response.GetToken(jwtProvider.generateAccessToken(user));
            }
            throw new AuthenticationException("invalid token");
        }
        throw new AuthenticationException("invalid token");
    }

    public UserDTO.Response.GetMessage deactivateAccount(String authHeader) {
        String email = jwtProvider.getEmailFromAuthHeader(authHeader);

        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user.setActive(false);
        userRepository.save(user);

        return new UserDTO.Response.GetMessage("success");
    }

    public UserDTO.Response.GetMessage logout(String authHeader) {
        String email = jwtProvider.getEmailFromAuthHeader(authHeader);

        refreshStorage.remove(email);

        return new UserDTO.Response.GetMessage("success");
    }


    public Boolean validateAccessToken(UserDTO.Request.AccessToken token) {
        return jwtProvider.validateAccessToken(token.getAccessToken());
    }
}