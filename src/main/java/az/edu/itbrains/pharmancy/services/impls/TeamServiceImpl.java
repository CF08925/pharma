package az.edu.itbrains.pharmancy.services.impls;

import az.edu.itbrains.pharmancy.dtos.team.TeamCreateDto;
import az.edu.itbrains.pharmancy.dtos.team.TeamDto;
import az.edu.itbrains.pharmancy.dtos.testimonial.TestimonialDto;
import az.edu.itbrains.pharmancy.models.Team;
import az.edu.itbrains.pharmancy.models.Testimonial;
import az.edu.itbrains.pharmancy.repositories.TeamRepository;
import az.edu.itbrains.pharmancy.services.TeamService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;

    @Override
    public List<TeamDto> getTeams() {
        List<Team> teams = teamRepository.getAllTeams();
        List<TeamDto> teamDtos = teams.stream().map(team -> modelMapper.map(team, TeamDto.class)).collect(Collectors.toList());
        return teamDtos;
    }

    @Override
    public void createTeam(TeamCreateDto teamCreateDto, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Product image is required");
        }
        try{
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String photoUrl = (String) uploadResult.get("url");

            Team team = new Team();
            team.setName(teamCreateDto.getName());
            team.setSurname(teamCreateDto.getSurname());
            team.setPosition(teamCreateDto.getPosition());
            team.setPhotoUrl(photoUrl);
            team.setAbout(teamCreateDto.getAbout());
            teamRepository.save(team);
        }catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Member with ID " + id + " not found");
        }
    }
}
