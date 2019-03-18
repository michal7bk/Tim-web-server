package pl.bak.timserver.customer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.customer.service.CustomerService;
import pl.bak.timserver.training.domain.Training;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public List<Customer> findCUstomers() {
//        return customerService.findCustomers();
        List<Customer> array = new ArrayList<>();
        Customer customer = Customer.builder().id(100L).name("Dsdsds").surname("no i piknie").build();
        array.add(customer);
        return array;
    }

    @GetMapping(value = "/{id}")
    public Customer findCUstomer(@PathVariable Long id) {
        return customerService.findCustomer(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@RequestBody @Valid Customer customer) {
        return customerService.save(customer);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        customerService.delete(id);
    }

    @GetMapping(value = "/{id}/trainings")
    public List<Training> findCustomerTrainings(@PathVariable Long id) {
        return customerService.findCustomerTrainings(id);
    }

    @PostMapping(value = "/training")
    @ResponseStatus(HttpStatus.CREATED)
    public Training proposeTraining(@RequestBody @Valid Training training) {
        return customerService.proposeTraining(training);

    }

}
