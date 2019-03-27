package pl.bak.timserver.security;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.service.CoachService;
import pl.bak.timserver.customer.service.CustomerService;
import pl.bak.timserver.user.ApplicationUser;
import pl.bak.timserver.user.ApplicationUserRepository;

import static java.util.Collections.emptyList;
import static pl.bak.timserver.user.ApplicationUser.Roles.coach;
import static pl.bak.timserver.user.ApplicationUser.Roles.customer;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ApplicationUserRepository applicationUserRepository;
    private final CoachService coachService;
    private final CustomerService customerService;

    public UserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository, CoachService coachService, CustomerService customerService) {
        this.applicationUserRepository = applicationUserRepository;
        this.coachService = coachService;
        this.customerService = customerService;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByName(name);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(name);
        }
        return new org.springframework.security.core.userdetails.User(applicationUser.getName(), applicationUser.getPassword(), emptyList());
    }

    public ApplicationUser getUserByUsername(String name) {
        ApplicationUser applicationUser = applicationUserRepository.findByName(name);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(name);
        } else if (applicationUser.getRoles() == coach) {
            applicationUser.setId(coachService.matchCoachByUser(applicationUser).getId());
        } else if (applicationUser.getRoles() == customer) {
            applicationUser.setId(customerService.matchCustomerByUser(applicationUser).getId());

        }
        return applicationUser;
    }
}
