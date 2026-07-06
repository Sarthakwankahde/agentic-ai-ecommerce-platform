import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DashboardController {
    // Only ADMIN can access
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "Welcome Admin Dashboard";
    }

    // Both USER and ADMIN can access
    @GetMapping("/user/dashboard")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String userDashboard() {
        return "Welcome User Dashboard";
    }
}