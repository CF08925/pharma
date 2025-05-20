package az.edu.itbrains.pharmancy.services;

import az.edu.itbrains.pharmancy.dtos.team.TeamCreateDto;
import az.edu.itbrains.pharmancy.dtos.team.TeamDto;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeamService {
    List<TeamDto> getTeams();

    void createTeam(@Valid TeamCreateDto teamCreateDto, MultipartFile image);

    void delete(Long id);
}
