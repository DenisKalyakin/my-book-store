package mate.academy.bookstore.service.user;

import mate.academy.bookstore.dto.user.external.UserResponseDto;
import mate.academy.bookstore.dto.user.internal.UserRegistrationRequestDto;
import mate.academy.bookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto registration(UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException;
}
