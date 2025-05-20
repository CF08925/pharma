package az.edu.itbrains.pharmancy.dtos.testimonial;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialDto {

    private Long id;
    private String name;
    private String surname;
    private String photoUrl;
    private String position;
    private String about;
}
