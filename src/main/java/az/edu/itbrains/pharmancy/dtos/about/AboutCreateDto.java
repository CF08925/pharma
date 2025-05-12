package az.edu.itbrains.pharmancy.dtos.about;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AboutCreateDto {

    private String photoUrl;
    private String videoUrl;
    private String aboutText;
}
