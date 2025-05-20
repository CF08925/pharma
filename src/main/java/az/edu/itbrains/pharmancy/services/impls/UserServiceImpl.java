package az.edu.itbrains.pharmancy.services.impls;

import az.edu.itbrains.pharmancy.dtos.auth.RegisterDto;
import az.edu.itbrains.pharmancy.models.User;
import az.edu.itbrains.pharmancy.repositories.UserRepository;
import az.edu.itbrains.pharmancy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public boolean registerUser(RegisterDto registerDto) {
        User user = userRepository.findByEmail(registerDto.getEmail());
        if (user != null){
            return false;
        }

        byte[] array = new byte[20]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));


        User newUser = modelMapper.map(registerDto, User.class);
        String password = passwordEncoder.encode(registerDto.getPassword());
        newUser.setEmailToken(generatedString);
        newUser.setPhotoUrl("https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909_1280.png");
        newUser.setEmailConfirmed(false);
        newUser.setPassword(password);
        userRepository.save(newUser);
        return true;
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public User findUserByEmail(String name) {
        return userRepository.findByEmail(name);
    }
}
