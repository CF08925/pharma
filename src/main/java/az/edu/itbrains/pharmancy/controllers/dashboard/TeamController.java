package az.edu.itbrains.pharmancy.controllers.dashboard;


import az.edu.itbrains.pharmancy.dtos.team.TeamCreateDto;
import az.edu.itbrains.pharmancy.dtos.team.TeamDto;
import az.edu.itbrains.pharmancy.dtos.testimonial.TestimonialCreateDto;
import az.edu.itbrains.pharmancy.dtos.testimonial.TestimonialDto;
import az.edu.itbrains.pharmancy.services.TeamService;
import az.edu.itbrains.pharmancy.services.TestimonialService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class TeamController {

    private final TeamService teamService;
    private final ModelMapper modelMapper;

    public TeamController(TestimonialService testimonialService, TeamService teamService, ModelMapper modelMapper) {
        this.teamService = teamService;

        this.modelMapper = modelMapper;
    }

    @GetMapping("/teams")
    public String Testimonial(Model model){
        List<TeamDto> teamDtos = teamService.getTeams();
        model.addAttribute("teams", teamDtos);
        return "dashboard/teams/index.html";
    }

    @GetMapping("/teams/create")
    public String create(Model model){
        model.addAttribute("teamCreateDto", new TeamCreateDto());
        return "dashboard/teams/create.html";
    }

    @PostMapping("/teams/create")
    public String create(@Valid TeamCreateDto teamCreateDto, BindingResult bindingResult, org.springframework.web.multipart.MultipartFile image){
        if(bindingResult.hasErrors()){
            return "/dashboard/teams/create";
        }
        teamService.createTeam(teamCreateDto, image);
        return "redirect:/admin/teams";
    }

    @PostMapping("/teams/delete/{id}")
    public String delete(@PathVariable Long id){
        teamService.delete(id);
        return "redirect:/teams";
    }

}
