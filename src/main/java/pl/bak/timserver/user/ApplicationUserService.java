package pl.bak.timserver.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.service.CoachService;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.customer.service.CustomerService;
import pl.bak.timserver.exception.ConflictWithExistingException;

@Service
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    final private CoachService coachService;
    final private CustomerService customerService;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder, CoachService coachService, CustomerService customerService) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.coachService = coachService;
        this.customerService = customerService;
    }


    void saveApplicationUser(ApplicationUser applicationUser) {
        if (applicationUserRepository.findByEmail(applicationUser.getEmail()) == null) {
            applicationUser.setPassword(bCryptPasswordEncoder.encode(applicationUser.getPassword()));
            applicationUserRepository.save(applicationUser);
            if (applicationUser.getRoles().equals(ApplicationUser.Roles.coach)) {
                coachService.save(Coach.builder()
                        .email(applicationUser.getEmail())
                        .name(applicationUser.getName())
                        .password(applicationUser.getPassword())
                        .surname(applicationUser.getSurname())
                        .build());
            } else if (applicationUser.getRoles().equals(ApplicationUser.Roles.customer)) {
                customerService.save(Customer.builder()
                        .email(applicationUser.getEmail())
                        .name(applicationUser.getName())
                        .password(applicationUser.getPassword())
                        .surname(applicationUser.getSurname())
                        .build());
            }
        } else throw new ConflictWithExistingException(ApplicationUser.class, applicationUser.getId());
    }

    ApplicationUser findbyName(String name) {
        return applicationUserRepository.findByName(name);
    }

}
