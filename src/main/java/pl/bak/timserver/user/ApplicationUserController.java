package pl.bak.timserver.user;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;


    public ApplicationUserController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody ApplicationUser applicationUser) {
        applicationUserService.saveApplicationUser(applicationUser);
    }

    @GetMapping("/{userId}")
    public String matchUser(@PathVariable() Long userId) {
        return applicationUserService.matchUser(userId);
    }
}
