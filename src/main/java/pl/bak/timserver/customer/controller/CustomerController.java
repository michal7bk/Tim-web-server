package pl.bak.timserver.customer.controller;

import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.customer.domain.dto.CustomerInfoDto;
import pl.bak.timserver.customer.service.CustomerService;
import pl.bak.timserver.training.domain.dto.TrainingDto;

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
    public List<Customer> findCustomers() {
        return customerService.findCustomers();
    }

    @GetMapping(value = "/{customerId}")
    public CustomerInfoDto findCustomer(@PathVariable Long customerId) {
        return customerService.findCustomer(customerId);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@RequestBody @Valid Customer customer) {
        return customerService.save(customer);
    }

    @RequestMapping(value = "/{customerId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("customerId") Long customerId) {
        customerService.delete(customerId);
    }

    @GetMapping(value = "/{customerId}/trainings")
    public List<TrainingDto> findCustomerTrainings(@PathVariable Long customerId) {
        return customerService.findCustomerTrainings(customerId);
    }

    @GetMapping(value = "/{customerId}/planned-trainings")
    public String countPlannedTrainings(@PathVariable Long customerId) {
        JsonObject result = new JsonObject();
        result.addProperty("count", String.valueOf(customerService.countPlannedTrainings(customerId)));
        return result.toString();
    }

    @GetMapping(value = "/{customerId}/completed-trainings")
    public String countCompletedTrainings(@PathVariable Long customerId) {
        JsonObject result = new JsonObject();
        result.addProperty("count", String.valueOf(customerService.countCompletedTrainings(customerId)));
        return result.toString();
    }

    @GetMapping(value = "{customerId}/unique-coaches")
    public String countUniqueCoach(@PathVariable Long customerId) {
        JsonObject result = new JsonObject();
        result.addProperty("count", String.valueOf(customerService.countUniqueCoach(customerId)));
        return result.toString();
    }

}
