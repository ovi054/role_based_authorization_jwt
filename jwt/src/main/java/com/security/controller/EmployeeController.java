package com.security.controller;

import com.security.entity.Employee;
import com.security.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/home")
    @RolesAllowed({"ROLE_ADMIN","ROLE_EDITOR","ROLE_VIEWER"})
    public String home() {
        return "Welcome Home";
    }

    @GetMapping("/employees")
    @RolesAllowed({"ROLE_ADMIN","ROLE_EDITOR","ROLE_VIEWER"})
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{id}")
    @RolesAllowed({"ROLE_ADMIN","ROLE_EDITOR","ROLE_VIEWER"})
    public Optional<Employee> getEmployeeById(@PathVariable String id)
    {
        Optional<Employee> employee = employeeRepository.findById(Integer.parseInt(id));
        return employee;
    }

    @PostMapping("/employees")
    @RolesAllowed("ROLE_ADMIN")
    public Employee createEmployee(@RequestBody Employee employee)
    {
        return employeeRepository.save(employee);
    }
    @DeleteMapping("/employees/{id}")
    @RolesAllowed({"ROLE_ADMIN","ROLE_EDITOR","ROLE_VIEWER"})
    public void deleteEmployee(@PathVariable(value = "id") Integer employeeId)
    {
        //Optional<Employee> employee = employeeRepository.findById(employeeId);
        employeeRepository.deleteById(employeeId);
    }
}
