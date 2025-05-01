package az.edu.itbrains.pharmancy.repositories;

import az.edu.itbrains.pharmancy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);
}
