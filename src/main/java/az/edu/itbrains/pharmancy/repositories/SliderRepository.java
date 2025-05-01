package az.edu.itbrains.pharmancy.repositories;

import az.edu.itbrains.pharmancy.models.Slider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SliderRepository extends JpaRepository<Slider, Long> {
    List<Slider> findTop3ByOrderByIndex();
}
