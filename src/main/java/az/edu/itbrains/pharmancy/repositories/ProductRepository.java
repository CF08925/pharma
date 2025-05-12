package az.edu.itbrains.pharmancy.repositories;

import az.edu.itbrains.pharmancy.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query(value = "SELECT * FROM products",nativeQuery = true)
    List<Product> findAllProducts();




    List<Product> findTop8ByOrderByIdDesc();

    List<Product> findTop5ByCategoryIdOrderByIdDesc(Long categoryId);


    @Query(value = "SELECT * FROM products WHERE featured = true ORDER BY id DESC LIMIT 8", nativeQuery = true)
    List<Product> findByFeaturedTrue();

    List<Product> findByCategoryId(Long categoryId);
}
