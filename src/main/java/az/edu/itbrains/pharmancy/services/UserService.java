package az.edu.itbrains.pharmancy.services;

import az.edu.itbrains.pharmancy.dtos.auth.RegisterDto;
import az.edu.itbrains.pharmancy.models.User;
import jakarta.validation.Valid;

public interface UserService {
    boolean registerUser(@Valid RegisterDto registerDto);

    User findByUsername(String username);
}
