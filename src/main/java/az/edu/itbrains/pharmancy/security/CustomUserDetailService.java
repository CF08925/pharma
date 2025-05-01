package az.edu.itbrains.pharmancy.security;


import az.edu.itbrains.pharmancy.models.User;
import az.edu.itbrains.pharmancy.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findByEmail(username);
        if (findUser != null) {
            org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
                    findUser.getEmail(),
                    findUser.getPassword(),
                    Collections.EMPTY_LIST

            );
            return user;
        }
        throw new UsernameNotFoundException("User not found");
    }
}
