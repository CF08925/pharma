package az.edu.itbrains.pharmancy.repositories;

import az.edu.itbrains.pharmancy.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select m from Team m order by m.id asc ")
    List<Team> getAllTeams();
}
