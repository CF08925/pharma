package az.edu.itbrains.pharmancy.dtos.receipt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptCreateDto {

    @NotBlank(message = "Ad daxil edilməlidir")
    @Size(min = 2, max = 50, message = "Ad 2-50 simvol arasında olmalıdır")
    private String firstName;

    @NotBlank(message = "Soyad daxil edilməlidir")
    @Size(min = 2, max = 50, message = "Soyad 2-50 simvol arasında olmalıdır")
    private String lastName;

    @NotBlank(message = "Operator seçilməlidir")
    private String phoneOperator;

    @NotBlank(message = "Telefon nömrəsi daxil edilməlidir")
    @Pattern(regexp = "\\d{7}", message = "Telefon nömrəsi 7 rəqəmdən ibarət olmalıdır")
    private String phoneNumber;



//    private Double amount; // Will be converted to cents

    @Size(max = 1000, message = "Qeyd 1000 simvoldan çox ola bilməz")
    private String notes;
}
