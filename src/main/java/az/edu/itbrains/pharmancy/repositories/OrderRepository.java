package az.edu.itbrains.pharmancy.repositories;

import az.edu.itbrains.pharmancy.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByUserIdOrderByOrderDateDesc(Long userId);

    @Query("SELECT o FROM Order o WHERE o.receipt.id = :receiptId")
    Order findByReceiptId(@Param("receiptId") Long receiptId);
}
