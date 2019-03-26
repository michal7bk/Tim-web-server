package pl.bak.timserver.user;

import com.google.gson.Gson;
import org.hibernate.ObjectNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.service.CoachService;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.customer.service.CustomerService;
import pl.bak.timserver.exception.ConflictWithExistingException;
import pl.bak.timserver.exception.ObjectNotFoundExcpetion;

import javax.persistence.EntityNotFoundException;

import static pl.bak.timserver.user.ApplicationUser.Roles.coach;
import static pl.bak.timserver.user.ApplicationUser.Roles.customer;

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
            if (applicationUser.getRoles().equals(coach)) {
                coachService.save(Coach.builder()
                        .email(applicationUser.getEmail())
                        .name(applicationUser.getName())
                        .surname(applicationUser.getSurname())
                        .build());
            } else if (applicationUser.getRoles().equals(customer)) {
                customerService.save(Customer.builder()
                        .email(applicationUser.getEmail())
                        .name(applicationUser.getName())
                        .surname(applicationUser.getSurname())
                        .build());
            }
        } else throw new ConflictWithExistingException(ApplicationUser.class, applicationUser.getId());
    }


    String matchUser(Long userId) {
        Gson gson = new Gson();
        ApplicationUser user = applicationUserRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(ApplicationUser.class, userId.toString()));
        if (user.getRoles().equals(customer)) {
            return gson.toJson(customerService.matchCustomerByUser(user));
        } else if (user.getRoles().equals(coach)) {
            return gson.toJson(coachService.matchCoachByUser(user));
        } else {
            throw new ObjectNotFoundException(ApplicationUser.class, userId.toString());
        }
    }

    void setOffline(Long userId) {
        try {
            ApplicationUser appUser = applicationUserRepository.getOne(userId);
            appUser.setActive(false);
            applicationUserRepository.save(appUser);
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundExcpetion(ApplicationUser.class, userId);
        }
    }

    void setOnline(Long userId) {
        try {
            ApplicationUser appUser = applicationUserRepository.getOne(userId);
            appUser.setActive(true);
            applicationUserRepository.save(appUser);
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundExcpetion(ApplicationUser.class, userId);
        }
    }
}
