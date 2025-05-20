package az.edu.itbrains.pharmancy.repositories;

import az.edu.itbrains.pharmancy.models.Basket;
import az.edu.itbrains.pharmancy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Basket findByProductIdAndUserId(Long productId, Long id1);

    Optional<Object> findByIdAndUser(Long id, User user);
}
