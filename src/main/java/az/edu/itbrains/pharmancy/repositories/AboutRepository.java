package az.edu.itbrains.pharmancy.repositories;

import az.edu.itbrains.pharmancy.models.About;
import az.edu.itbrains.pharmancy.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AboutRepository extends JpaRepository<About,Long> {

    @Query("select m from About m order by m.id desc ")
    List<About> getAbout();
}
