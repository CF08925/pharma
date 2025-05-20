package az.edu.itbrains.pharmancy.repositories;

import az.edu.itbrains.pharmancy.models.About;
import az.edu.itbrains.pharmancy.models.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {
    @Query("select m from Testimonial m order by m.id desc ")
    List<Testimonial> getAllTestimonials();
}
