package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.user.external.UserLoginResponseDto;
import mate.academy.bookstore.dto.user.external.UserResponseDto;
import mate.academy.bookstore.dto.user.internal.UserLoginRequestDto;
import mate.academy.bookstore.dto.user.internal.UserRegistrationRequestDto;
import mate.academy.bookstore.exception.RegistrationException;
import mate.academy.bookstore.security.AuthenticationService;
import mate.academy.bookstore.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication management",
        description = "Endpoints for registration and authentication users")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Create a new user", description = "Create a new user")
    @PostMapping("/register")
    public UserResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto registrationRequestDto
    ) throws RegistrationException {
        return userService.registration(registrationRequestDto);
    }

    @Operation(summary = "Authenticate user",
            description = "Authenticate user by JWT token validation")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto loginRequestDto) {
        return authenticationService.authenticate(loginRequestDto);
    }
}
