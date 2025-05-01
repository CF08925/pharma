package az.edu.itbrains.pharmancy.repositories;

import az.edu.itbrains.pharmancy.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select m from Category m order by m.id desc ")
    List<Category> getCategories();
}
