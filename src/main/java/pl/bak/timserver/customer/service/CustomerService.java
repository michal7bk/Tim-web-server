package pl.bak.timserver.customer.service;

import org.modelmapper.ModelMapper;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.customer.domain.dto.CustomerInfoDto;
import pl.bak.timserver.customer.repository.CustomerRepository;
import pl.bak.timserver.exception.ConflictWithExistingException;
import pl.bak.timserver.exception.objectNotFoundExcpetion;
import pl.bak.timserver.training.domain.Training;
import pl.bak.timserver.training.domain.dto.TrainingDto;
import pl.bak.timserver.user.ApplicationUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    final private CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerService(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    public List<Customer> findCustomers() {
        return customerRepository.findAll();
    }

    public CustomerInfoDto findCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new objectNotFoundExcpetion(Customer.class, id));
        return convertToDto(customer);
    }

    public Customer save(Customer customer) {
        if (!customerRepository.findByEmail(customer.getEmail()).isPresent())
            return customerRepository.save(customer);
        else throw new ConflictWithExistingException(Customer.class, customer.getId());

    }

    public void delete(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new objectNotFoundExcpetion(Customer.class, id));
        customerRepository.delete(customer);
    }

    public List<TrainingDto> findCustomerTrainings(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new objectNotFoundExcpetion(Customer.class, id));
        return customer.trainings.stream().map(this::convertToDto).collect(Collectors.toList());
    }


    public long getCountCompletedTrainings(Long customer_id) {
        Customer customer = customerRepository.findById(customer_id)
                .orElseThrow(() -> new objectNotFoundExcpetion(Customer.class, customer_id));
        List<Training> trainings = customer.getTrainings();
        return trainings.stream().map(x -> x.getStartTime().isBefore(LocalDateTime.now())).count();
    }

    public long getCountPlannedTrainings(Long customer_id) {
        Customer customer = customerRepository.findById(customer_id)
                .orElseThrow(() -> new objectNotFoundExcpetion(Customer.class, customer_id));
        List<Training> trainings = customer.getTrainings();
        return trainings.stream().map(x -> x.getStartTime().isAfter(LocalDateTime.now())).count();
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
                .orElseThrow(() -> new objectNotFoundExcpetion(Coach.class, user.getId()));
    }


}
