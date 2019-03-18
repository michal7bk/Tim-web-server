package pl.bak.timserver.customer.service;

import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.repository.CoachRepository;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.customer.repository.CustomerRepository;
import pl.bak.timserver.exception.ConflictWithExistingException;
import pl.bak.timserver.exception.ObjectNotFoundExcpetion;
import pl.bak.timserver.training.domain.Training;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    final private CustomerRepository customerRepository;
    final private CoachRepository coachRepository;

    public CustomerService(CustomerRepository customerRepository, CoachRepository coachRepository) {
        this.customerRepository = customerRepository;
        this.coachRepository = coachRepository;
    }

    public List<Customer> findCustomers() {
        return customerRepository.findAll();
    }

    public Customer findCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new ObjectNotFoundExcpetion(Customer.class, id));
    }

    public Customer save(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()) == null)
            return customerRepository.save(customer);
        else throw new ConflictWithExistingException(Customer.class, customer.getId());

    }

    public void delete(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        customerRepository.delete(customer.get());
    }

    public List<Training> findCustomerTrainings(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Customer.class, id));
        return customer.trainings;
    }

    public Training proposeTraining(Training training) {
        Coach coach = training.getCoach();
        coach.getProposedTrainings().add(training);
        return training;


    }

}
