package pl.bak.timserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.repository.CoachRepository;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.customer.repository.CustomerRepository;
import pl.bak.timserver.training.domain.Training;
import pl.bak.timserver.training.repository.TrainingRepository;
import pl.bak.timserver.user.ApplicationUser;
import pl.bak.timserver.user.ApplicationUserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDateTime.now;

@Component
public class DataInitializr implements ApplicationRunner {

    private final List<Customer> customers = new ArrayList<>();
    private final List<Coach> coaches = new ArrayList<>();
    private final List<Training> trainings1 = new ArrayList<>();
    private final List<Training> trainings2 = new ArrayList<>();
    private final List<ApplicationUser> applicationUsers = new ArrayList<>();
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomerRepository customerRepository;
    private final CoachRepository coachRepository;
    private final TrainingRepository trainingRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private Coach coach1;
    private Customer customer1;
    private Coach coach2;
    private Customer customer2;

    @Autowired
    public DataInitializr(BCryptPasswordEncoder bCryptPasswordEncoder, CustomerRepository customerRepository, CoachRepository coachRepository, TrainingRepository trainingRepository, ApplicationUserRepository applicationUserRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customerRepository = customerRepository;
        this.coachRepository = coachRepository;
        this.trainingRepository = trainingRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        cleanTables();
        createUsers();
        saveTrainings();
        updateUsersWithTrainings();
    }

    private void updateUsersWithTrainings() {
        customerRepository.findByEmail("user1@user1.com").get().setTrainings(trainings1);
        customerRepository.findByEmail("user2@user2.com").get().setTrainings(trainings2);
        coachRepository.findByEmail("coach1@coach1.com").get().setTrainings(trainings1);
        coachRepository.findByEmail("coach2@coach2.com").get().setTrainings(trainings2);
    }

    private void cleanTables(){
        trainingRepository.deleteAll();
        customerRepository.deleteAll();
        coachRepository.deleteAll();
        applicationUserRepository.deleteAll();
    }

    private void createUsers() {
        customer1 = Customer.builder()
                .email("user1@user1.com")
                .name("user1")
                .surname("suruser1")
                .build();
        customer2 = Customer.builder()
                .email("user2@user2.com")
                .name("user2")
                .surname("suruser2")
                .build();
        customers.addAll(Arrays.asList(customer1, customer2));

        coach1 = Coach.builder()
                .email("coach1@coach1.com")
                .name("coach1")
                .surname("surcoach1")
                .build();

        coach2 = Coach.builder()
                .email("coach2@coach2.com")
                .name("coach2")
                .surname("surcoach2")
                .build();
        coaches.addAll(Arrays.asList(coach1, coach2));

        ApplicationUser applicationUser11 = ApplicationUser.builder()
                .email("user1@user1.com")
                .name("user1")
                .password(bCryptPasswordEncoder.encode("user1"))
                .roles(ApplicationUser.Roles.customer)
                .surname("suruser1")
                .build();
        ApplicationUser applicationUser12 = ApplicationUser.builder()
                .email("user2@user2.com")
                .name("user2")
                .password(bCryptPasswordEncoder.encode("user2"))
                .roles(ApplicationUser.Roles.customer)
                .surname("suruser2")
                .build();
        ApplicationUser applicationUser21 = ApplicationUser.builder()
                .email("coach1@coach1.com")
                .name("coach1")
                .password(bCryptPasswordEncoder.encode("coach1"))
                .roles(ApplicationUser.Roles.coach)
                .surname("surcoach1")
                .build();
        ApplicationUser applicationUser22 = ApplicationUser.builder()
                .email("coach2@coach2.com")
                .name("coach2")
                .password(bCryptPasswordEncoder.encode("coach2"))
                .roles(ApplicationUser.Roles.coach)
                .surname("surcoach2")
                .build();

        applicationUsers.addAll(Arrays.asList(applicationUser11, applicationUser12, applicationUser21, applicationUser22));


        for (ApplicationUser applicationUser : applicationUsers) {
            applicationUserRepository.save(applicationUser);
        }

        for (Coach coach : coaches) {
            coachRepository.save(coach);
        }
        for (Customer customer : customers) {
            customerRepository.save(customer);
        }
    }

    private void saveTrainings() {
        Training training1 = Training.builder()
                .accepted(false)
                .coach(coach1)
                .customer(customer1)
                .info("this is new training1")
                .startTime(now())
                .endTime(now().plusHours(1))
                .build();

        Training training11 = Training.builder()
                .accepted(true)
                .coach(coach1)
                .customer(customer1)
                .info("this is new training11")
                .startTime(now().minusHours(1))
                .endTime(now().plusHours(1))
                .build();

        Training training2 = Training.builder()
                .accepted(false)
                .coach(coach2)
                .customer(customer2)
                .info("this is new training2")
                .startTime(now())
                .endTime(now().plusHours(1))
                .build();

        Training training22 = Training.builder()
                .accepted(true)
                .coach(coach2)
                .customer(customer2)
                .info("this is new training22")
                .startTime(now().minusHours(2))
                .endTime(now().minusHours(1))
                .build();
        trainings1.addAll(Arrays.asList(training1, training11));
        trainings1.addAll(Arrays.asList(training2, training22));
        for (Training training : trainings1) {
            trainingRepository.save(training);
        }
        for (Training training : trainings2) {
            trainingRepository.save(training);
        }
    }
}
