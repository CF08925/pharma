package az.edu.itbrains.pharmancy.dtos.auth;



import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {


    @NotEmpty(message = "User's name cannot be empty.")
    @Email(message = "Enter valid e-mail" )
    private String email;
    @NotEmpty(message = "User's name cannot be empty.")
    @Size(min = 3, max = 250, message = "User's name must be minimum 3 characters.")
    private String firstName;
    @NotEmpty(message = "User's surname cannot be empty.")
    @Size(min = 3, max = 250, message = "User's surname must be minimum 3 characters.")
    private String lastName;
    @NotEmpty(message = "User's password cannot be empty.")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})", message = "Enter valid password")
   private String password;
    @NotEmpty(message = "User's password repeat cannot be empty.")
   private String repeatPassword;


    private boolean passwordsEqual;

    public void setPasswordsEqual(boolean passwordsEqual) {
        this.passwordsEqual = passwordsEqual;
    }
    @AssertTrue(message = "Passwords should match")
    public boolean isPasswordsEqual() {
        return password.equals(repeatPassword);
   }
}
