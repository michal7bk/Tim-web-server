package pl.bak.timserver.user;

import com.google.gson.Gson;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.service.CoachService;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.customer.service.CustomerService;
import pl.bak.timserver.exception.ConflictWithExistingException;
import pl.bak.timserver.exception.ObjectNotFoundException;

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


    void tryToSaveUser(ApplicationUser applicationUser) {
        if (applicationUserRepository.findByEmail(applicationUser.getEmail()) != null) {
            throw new ConflictWithExistingException("User- mail ", applicationUser.getEmail());
        } else if (applicationUserRepository.findByName(applicationUser.getName()) != null) {
            throw new ConflictWithExistingException("User - name ", applicationUser.getName());
        } else {
            saveUser(applicationUser);
        }
    }

    private void saveUser(ApplicationUser applicationUser) {
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
    }


    String matchUser(Long userId) {
        Gson gson = new Gson();
        ApplicationUser user = applicationUserRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(ApplicationUser.class, userId));
        if (user.getRoles().equals(customer)) {
            return gson.toJson(customerService.matchCustomerByUser(user));
        } else if (user.getRoles().equals(coach)) {
            return gson.toJson(coachService.matchCoachByUser(user));
        } else {
            throw new ObjectNotFoundException(ApplicationUser.class, userId);
        }
    }

    private void setOffline(ApplicationUser user) {
        ApplicationUser applicationUser = matchCustomerCoachByAppUser(user);
        applicationUser.setActive(false);
        applicationUserRepository.save(applicationUser);
    }

    private void setOnline(ApplicationUser user) {
        ApplicationUser applicationUser = matchCustomerCoachByAppUser(user);
        applicationUser.setActive(true);
        applicationUserRepository.save(applicationUser);
    }

    private ApplicationUser matchCustomerCoachByAppUser(ApplicationUser user) {
        String emailCustCoachToUpdate = null;
        Long appUserId = null;
        try {
            if (user.getRoles().equals(coach)) {
                emailCustCoachToUpdate = coachService.findCoach(user.getId()).getEmail();
            } else if (user.getRoles().equals(customer)) {
                emailCustCoachToUpdate = customerService.findCustomer(user.getId()).getEmail();
            }
            appUserId = applicationUserRepository.findByEmail(emailCustCoachToUpdate).getId();
            return applicationUserRepository.getOne(appUserId);
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException(ApplicationUser.class, appUserId);
        }
    }

    void changeStatus(Long userId) {
        ApplicationUser applicationUser = applicationUserRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(ApplicationUser.class, userId));
        if (applicationUser.isActive())
            setOnline(applicationUser);
        else
            setOffline(applicationUser);

    }
}
