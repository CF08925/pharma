package az.edu.itbrains.pharmancy.dtos.team;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreateDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private String name;
    private String surname;
    private String photoUrl;
    private String position;
    private String about;
}
