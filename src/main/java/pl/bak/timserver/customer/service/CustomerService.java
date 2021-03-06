package pl.bak.timserver.customer.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.customer.domain.dto.CustomerInfoDto;
import pl.bak.timserver.customer.repository.CustomerRepository;
import pl.bak.timserver.exception.ConflictWithExistingException;
import pl.bak.timserver.exception.ObjectNotFoundException;
import pl.bak.timserver.mail.MailSender;
import pl.bak.timserver.training.domain.Training;
import pl.bak.timserver.training.domain.dto.TrainingsListDto;
import pl.bak.timserver.user.ApplicationUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    final private CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerService(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    public List<CustomerInfoDto> findCustomers() {
        return customerRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public CustomerInfoDto findCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(Customer.class, id));
        return convertToDto(customer);
    }

    public Customer save(Customer customer) {
        if (!customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            MailSender.sendMail("Your account was created", customer.getEmail());
            return customerRepository.save(customer);
        } else throw new ConflictWithExistingException(Customer.class, customer.getId());

    }

    public void delete(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Customer.class, id));
        customerRepository.delete(customer);
    }

    public List<TrainingsListDto> findCustomerTrainings(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Customer.class, id));
        return customer.trainings.stream().map(this::convertToListDto).collect(Collectors.toList());
    }


    public long countCompletedTrainings(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ObjectNotFoundException(Customer.class, customerId));
        return customer.getTrainings().stream().filter(x -> x.getStartTime().isBefore(LocalDateTime.now())).count();
    }

    public long countPlannedTrainings(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ObjectNotFoundException(Customer.class, customerId));
        return customer.getTrainings().stream().filter(x -> x.getStartTime().isAfter(LocalDateTime.now())).count();
    }

    public long countUniqueCoach(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ObjectNotFoundException(Customer.class, customerId));
        return customer.getTrainings().stream().filter(distinctByKey(Training::getCoach)).count();
    }

    public void askCoachForContact(Long customerId, String coachEmail) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ObjectNotFoundException(Customer.class, customerId));
        MailSender.askForContact(customer, coachEmail);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private CustomerInfoDto convertToDto(Customer customer) {
        return modelMapper.map(customer, CustomerInfoDto.class);
    }

    private TrainingsListDto convertToListDto(Training training) {
        return modelMapper.map(training, TrainingsListDto.class);
    }

    public Customer matchCustomerByUser(ApplicationUser user) {
        return customerRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new ObjectNotFoundException(Coach.class, user.getId()));
    }


}
