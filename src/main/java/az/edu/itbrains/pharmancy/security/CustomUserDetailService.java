package az.edu.itbrains.pharmancy.security;

import az.edu.itbrains.pharmancy.models.Role;
import az.edu.itbrains.pharmancy.models.User;
import az.edu.itbrains.pharmancy.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            if (findUser.getRoles() != null && !findUser.getRoles().isEmpty()) {
                authorities = findUser.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                        .collect(Collectors.toList());
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            return new org.springframework.security.core.userdetails.User(
                    findUser.getEmail(),
                    findUser.getPassword(),
                    true, // CHANGED: Always enabled for now - was: findUser.isEmailConfirmed()
                    true, // accountNonExpired
                    true, // credentialsNonExpired
                    true, // accountNonLocked
                    authorities
            );
        }
        throw new UsernameNotFoundException("User not found with email: " + username);
    }
}