package az.edu.itbrains.pharmancy.dtos.category;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDashboardDto {
    private Long id;
    private String name;
    private String photoUrl;
    private int productCount;
}