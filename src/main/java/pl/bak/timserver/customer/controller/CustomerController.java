package pl.bak.timserver.customer.controller;

import com.google.gson.JsonObject;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public CustomerController(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<Customer> findCUstomers() {
        return customerService.findCustomers();
    }

    @GetMapping(value = "/{id}")
    public CustomerInfoDto findCustomer(@PathVariable Long id) {
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
    public List<TrainingDto> findCustomerTrainings(@PathVariable Long id) {
        return customerService.findCustomerTrainings(id);
    }

    @GetMapping(value = "/{id}/planned-trainings")
    public String countPlannedTrainings(@PathVariable Long id) {
        JsonObject result = new JsonObject();
        result.addProperty("count", String.valueOf(customerService.getCountPlannedTrainings(id)));
        return result.toString();
    }

    @GetMapping(value = "/{id}/completed-trainings")
    public String countCompletedTrainings(@PathVariable Long id) {
        JsonObject result = new JsonObject();
        result.addProperty("count", String.valueOf(customerService.getCountCompletedTrainings(id)));
        return result.toString();
    }

}
