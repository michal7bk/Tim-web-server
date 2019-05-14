package pl.bak.timserver.customer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bak.timserver.common.Counter;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.customer.domain.dto.CustomerInfoDto;
import pl.bak.timserver.customer.service.CustomerService;
import pl.bak.timserver.training.domain.dto.TrainingsListDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public List<CustomerInfoDto> findCustomers() {
        return customerService.findCustomers();
    }

    @GetMapping(value = "/{customerId}")
    public CustomerInfoDto findCustomer(@PathVariable Long customerId) {
        return customerService.findCustomer(customerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@RequestBody @Valid Customer customer) {
        return customerService.save(customer);
    }

    @DeleteMapping(value = "/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("customerId") Long customerId) {
        customerService.delete(customerId);
    }

    @GetMapping(value = "/{customerId}/trainings")
    public List<TrainingsListDto> findCustomerTrainings(@PathVariable Long customerId) {
        return customerService.findCustomerTrainings(customerId);
    }

    @GetMapping(value = "/{customerId}/planned-trainings")
    public Counter countPlannedTrainings(@PathVariable Long customerId) {
        return new Counter(customerService.countPlannedTrainings(customerId));
    }

    @GetMapping(value = "/{customerId}/completed-trainings")
    public Counter countCompletedTrainings(@PathVariable Long customerId) {
        return new Counter(customerService.countCompletedTrainings(customerId));
    }

    @GetMapping(value = "{customerId}/unique-coaches")
    public Counter countUniqueCoach(@PathVariable Long customerId) {
        return new Counter(customerService.countUniqueCoach(customerId));
    }

    @GetMapping(value = "{customerId}/contact")
    public void askForContact(@PathVariable Long customerId, @RequestParam String email) {
        customerService.askCoachForContact(customerId, email);
    }

}
