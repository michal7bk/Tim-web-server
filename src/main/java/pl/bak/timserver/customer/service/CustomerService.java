package pl.bak.timserver.customer.service;

import org.modelmapper.ModelMapper;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.customer.domain.dto.CustomerInfoDto;
import pl.bak.timserver.customer.repository.CustomerRepository;
import pl.bak.timserver.exception.ConflictWithExistingException;
import pl.bak.timserver.exception.ObjectNotFoundExcpetion;
import pl.bak.timserver.mail.MailSender;
import pl.bak.timserver.training.domain.Training;
import pl.bak.timserver.training.domain.dto.TrainingDto;
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
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ObjectNotFoundExcpetion(Customer.class, id));
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
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Customer.class, id));
        customerRepository.delete(customer);
    }

    public List<TrainingDto> findCustomerTrainings(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Customer.class, id));
        return customer.trainings.stream().map(this::convertToDto).collect(Collectors.toList());
    }


    public long countCompletedTrainings(Long customer_id) {
        Customer customer = customerRepository.findById(customer_id)
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Customer.class, customer_id));
        return customer.getTrainings().stream().map(x -> x.getStartTime().isBefore(LocalDateTime.now())).count();
    }

    public long countPlannedTrainings(Long customer_id) {
        Customer customer = customerRepository.findById(customer_id)
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Customer.class, customer_id));
        return customer.getTrainings().stream().map(x -> x.getStartTime().isAfter(LocalDateTime.now())).count();
    }

    public long countUniqueCoach(Long customer_id) {
        Customer customer = customerRepository.findById(customer_id)
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Customer.class, customer_id));
        return customer.getTrainings().stream().filter(distinctByKey(Training::getCoach)).count();
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private CustomerInfoDto convertToDto(Customer customer) {
        return modelMapper.map(customer, CustomerInfoDto.class);
    }

    private TrainingDto convertToDto(Training training) {
        return modelMapper.map(training, TrainingDto.class);
    }

    private Training convertToEntity(TrainingDto postDto) throws ParseException {
        return modelMapper.map(postDto, Training.class);
    }


    public Customer matchCustomerByUser(ApplicationUser user) {
        return customerRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Coach.class, user.getId()));
    }


}
