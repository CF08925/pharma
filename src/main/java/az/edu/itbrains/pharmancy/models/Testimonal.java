package az.edu.itbrains.pharmancy.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="testimonals")
public class Testimonal {

    private Long id;
    private String name;
    private String surname;
    private String position;
    private String about;
}
