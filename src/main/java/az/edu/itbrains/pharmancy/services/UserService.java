package az.edu.itbrains.pharmancy.services;

import az.edu.itbrains.pharmancy.dtos.auth.RegisterDto;
import jakarta.validation.Valid;

public interface UserService {
    boolean registerUser(@Valid RegisterDto registerDto);
}
