package jp.co.axa.apidemo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EntityNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceUnitTests {

	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
	private EmployeeServiceImpl employeeService = null;

	private List<Employee> employees;

	private List<Employee> searchResults;

	@BeforeEach
	public void setUp() {
		employees = new ArrayList<>();
		employees.add(new Employee(1L, "Employee1 Surname1", 2000, "IT"));
		employees.add(new Employee(2L, "Employee2 Surname1", 3000, "Finance"));
		employees.add(new Employee(3L, "Employee3 Surname2", 3000, "IT"));
		employees.add(new Employee(4L, "Employee4 Surname3", 3000, "HR"));
		employees.add(new Employee(5L, "Employee5 Surname3", 3000, "Sales"));
//        search results
		searchResults = new ArrayList<>();
		searchResults.add(employees.get(0));
		searchResults.add(employees.get(1));
	}

	@DisplayName("test for retrieveEmployees(search,sortBy,sortDirection) method search value given and sorting")
	@Test
	public void should_retrieve_employees_search_prameter_given() {
//      check for the ascending order by name search for "Surname1" keyword
		Sort sort = Sort.by(Sort.Direction.ASC, "name");
		when(employeeRepository.findAll(any(Specification.class), eq(sort))).thenReturn(searchResults);
		List<Employee> retrievedEmployees = employeeService.retrieveEmployees("Surname1", "name", "asc");
		assertThat(retrievedEmployees).isEqualTo(searchResults);

//      check for the descending order by department search for "Surname1" keyword
		sort = Sort.by(Sort.Direction.DESC, "department");
		when(employeeRepository.findAll(any(Specification.class), eq(sort))).thenReturn(searchResults);
		retrievedEmployees = employeeService.retrieveEmployees("Surname1", "department", "desc");
		assertThat(retrievedEmployees).isEqualTo(searchResults);

//        check for the descending order by name, search for "Surname1" keyword
		Collections.reverse(searchResults);
		sort = Sort.by(Sort.Direction.DESC, "name");
		when(employeeRepository.findAll(any(Specification.class), eq(sort))).thenReturn(searchResults);
		retrievedEmployees = employeeService.retrieveEmployees("Surname1", "name", "desc");
		assertThat(retrievedEmployees).isEqualTo(searchResults);

//      check for the descending order by name and search for "IT" keyword
		searchResults = new ArrayList<Employee>() {
			{
				add(new Employee(1L, "Employee1 Surname1", 2000, "IT"));
			}
		};
		sort = Sort.by(Sort.Direction.DESC, "name");
		when(employeeRepository.findAll(any(Specification.class), eq(sort))).thenReturn(searchResults);
		retrievedEmployees = employeeService.retrieveEmployees("Finance", "name", "desc");
		assertThat(retrievedEmployees).isEqualTo(searchResults);

	}
	
	@DisplayName("test for retrieveEmployees(search,sortBy,sortDirection) method search value not given")
	@Test
	public void should_retrieve_employees_all() {
//		retrieve all employees when search by is null
		when(employeeRepository.findAll()).thenReturn(employees);
		List<Employee> retrievedEmployees = employeeService.retrieveEmployees(null, "name", "asc");
		assertThat(retrievedEmployees).isEqualTo(employees);
	}
	

	@DisplayName("test for retrievePaginatedEmployees(search,pageNumber,pageSize,sortBy,sortDirection)")
	@Test
	public void should_retrieve_paginated_employees() {
		Page<Employee> page = new PageImpl<>(searchResults);
		Sort sort = Sort.by(Sort.Direction.ASC, "name");
		Pageable pageable = PageRequest.of(0, 2, sort);
		when(employeeRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
		Page<Employee> retrievedPage = employeeService.retrievePaginatedEmployees("Employee 1", 0, 2, "name", "asc");
		assertThat(retrievedPage).isEqualTo(page);
	}

	@DisplayName("test for getEmployee(employeeId) employee exists")
	@Test
	public void should_get_employee() {
//		if employee of given id exits
		Employee employee = employees.get(0);
		Optional<Employee> optionalEmployee = Optional.of(employee);
		when(employeeRepository.findById(1L)).thenReturn(optionalEmployee);
		Employee retrievedEmployee = employeeService.getEmployee(1L);
		assertThat(retrievedEmployee).isEqualTo(employee);

//		if the employee does not exist
		optionalEmployee = Optional.empty();
		when(employeeRepository.findById(100L)).thenReturn(optionalEmployee);
		assertThatExceptionOfType(NoSuchElementException.class)
		.isThrownBy(() -> employeeService.getEmployee(100L));
	}
	
	@DisplayName("test for getEmployee(employeeId) employee does not exists throw NoSuchElementException")
	@Test
	public void should_throw_exception_get_employee() {
//		if the employee does not exist
		Optional<Employee> optionalEmployee = Optional.empty();
		when(employeeRepository.findById(100L)).thenReturn(optionalEmployee);
		assertThatExceptionOfType(NoSuchElementException.class)
		.isThrownBy(() -> employeeService.getEmployee(100L));
	}

	@DisplayName("test for getEmployeeOrThrow(employeeId) employee exists")
	@Test
	public void should_get_employee_on_get_employee_or_throw() {
		Employee employee = employees.get(0);
		when(employeeService.getEmployeeOrThrow(1L)).thenReturn(employee);
		Employee retrievedEmployee = employeeService.getEmployeeOrThrow(1L);
		assertThat(retrievedEmployee).isEqualTo(employee);
	}
	
	@DisplayName("test for getEmployeeOrThrow(employeeId) employee does not exists throw EntityNotFoundException")
	@Test
	public void should_throw_exception_on_get_employee_or_throw() {
		Employee employee = employees.get(0);
		EntityNotFoundException ex = mock(EntityNotFoundException.class);
		when(employeeRepository.findByIdOrThrow(100L)).thenThrow(ex);
		assertThatExceptionOfType(EntityNotFoundException.class)
		.isThrownBy(() -> employeeService.getEmployeeOrThrow(100L));
	}

	@DisplayName("test for saveEmployee(employee) save new employee")
	@Test
	public void should_save_employee() {
		Employee employee = new Employee(3L, "Bob Smith", 4000, "HR");
		when(employeeRepository.save(employee)).thenReturn(employee);
		Employee savedEmployee = employeeService.saveEmployee(employee);
		assertThat(savedEmployee).isEqualTo(employee);
	}

	@DisplayName("test for deleteEmployee(employee) delete by employee id")
	@Test
	public void should_delete_employee() {
		Long employeeId = 1L;		
		employeeService.deleteEmployee(employeeId);
	}

	@DisplayName("test for updateEmployee(employee) update an employee")
	@Test
	public void should_update_employee() {
		Employee employee = employees.get(0);
		employee.setName("Updated Name");
		when(employeeService.updateEmployee(employee)).thenReturn(employee);
		Employee updatedEmployee = employeeService.updateEmployee(employee);
		assertThat(updatedEmployee).isEqualTo(employee);
	}
}
