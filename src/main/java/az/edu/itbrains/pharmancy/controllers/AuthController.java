package az.edu.itbrains.pharmancy.controllers;

import az.edu.itbrains.pharmancy.dtos.auth.RegisterDto;
import az.edu.itbrains.pharmancy.models.Role;
import az.edu.itbrains.pharmancy.models.User;
import az.edu.itbrains.pharmancy.repositories.RoleRepository;
import az.edu.itbrains.pharmancy.repositories.UserRepository;
import az.edu.itbrains.pharmancy.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/login")
    public String login(){
        return "auth/login.html";  // FIXED: Consistent path (removed leading slash)
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("registerDto", new RegisterDto());
        return "auth/register.html";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterDto registerDto, BindingResult result,
                           RedirectAttributes redirectAttributes){

        System.out.println("Registration attempt for email: " + registerDto.getEmail());

        if (result.hasErrors()){
            System.out.println("Registration validation errors: " + result.getAllErrors());
            return "auth/register.html";
        }

        try {
            boolean success = userService.registerUser(registerDto);
            if (success) {
                System.out.println("User registered successfully: " + registerDto.getEmail());
                redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
                return "redirect:/login";
            } else {
                System.out.println("Registration failed - user already exists: " + registerDto.getEmail());
                result.rejectValue("email", "error.email", "Email already exists");
                return "auth/register.html";
            }
        } catch (Exception e) {
            System.out.println("Registration error for email: " + registerDto.getEmail() + " - " + e.getMessage());
            e.printStackTrace();
            result.reject("global.error", "Registration failed: " + e.getMessage());
            return "auth/register.html";
        }
    }

    @GetMapping("/forgot-password")
    public String forgot(){
        return "auth/forgot.html";
    }

    @GetMapping("/change-password")
    public String change(){
        return "auth/change.html";
    }


    @GetMapping("/create-admin")
    @ResponseBody
    public String createAdmin() {
        try {
            // Check if admin already exists
            User existingAdmin = userRepository.findByEmail("admin@pharma.com");
            if (existingAdmin != null) {
                return "✅ Admin user already exists: admin@pharma.com / admin123";
            }

            // Create or get ADMIN role
            Role adminRole = roleRepository.findByName("ADMIN");
            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setName("ADMIN");
                adminRole = roleRepository.save(adminRole);
                System.out.println("Created ADMIN role");
            }

            // Create admin user
            User admin = new User();
            admin.setEmail("admin@pharma.com");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmailConfirmed(true);
            admin.setPhotoUrl("https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909_1280.png");

            // Encode password
            admin.setPassword(passwordEncoder.encode("admin123"));

            // Add admin role
            List<Role> roles = new ArrayList<>();
            roles.add(adminRole);
            admin.setRoles(roles);

            // Save admin user
            User savedAdmin = userRepository.save(admin);

            System.out.println("Admin user created: " + savedAdmin.getEmail());

            return "✅ Admin user created successfully!<br><br>" +
                    "<strong>Login Details:</strong><br>" +
                    "Email: admin@pharma.com<br>" +
                    "Password: admin123<br><br>" +
                    "<a href='/login'>Go to Login</a><br>" +
                    "<a href='/admin'>Go to Admin Panel</a>";

        } catch (Exception e) {
            System.out.println("Error creating admin: " + e.getMessage());
            e.printStackTrace();
            return "❌ Error creating admin user: " + e.getMessage();
        }
    }
}