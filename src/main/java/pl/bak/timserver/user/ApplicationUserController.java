package pl.bak.timserver.user;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class ApplicationUserController {

    private ApplicationUserService applicationUserService;


    public ApplicationUserController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody ApplicationUser applicationUser) {
        applicationUserService.saveApplicationUser(applicationUser);
    }

    @GetMapping("/{name}")
    public ApplicationUser get(@PathVariable() String name) {

        return applicationUserService.findbyName(name);
    }
}
