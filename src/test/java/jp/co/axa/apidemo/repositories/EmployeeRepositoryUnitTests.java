package jp.co.axa.apidemo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EntityNotFoundException;
import jp.co.axa.apidemo.services.EmployeeService;

//@ContextConfiguration(locations = "classpath:logback-dev.xml")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Transactional
@AutoConfigureTestEntityManager
public class EmployeeRepositoryUnitTests {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	EmployeeRepository repository;

	@MockBean	
	private EmployeeService employeeService;

	@BeforeEach
	public void setUp() {
//		delete all the data in the repository before each test
		repository.deleteAll();
	}

	@DisplayName("test for findAll() checks if empty")
	@Test
	public void should_find_no_employees_if_repository_is_empty() {
//		delete all the employees as the data is pre-loaded using the data.sql file of spring boot application
		List<Employee> tutorials = repository.findAll();

		assertThat(tutorials).isEmpty();
	}

	@DisplayName("test for save() checks if a new employee is added")
	@Test
	public void should_save_an_employee() {
//		this testing is not required as it is a inbuilt function without any modification 
//		just for showing the assertion knowledge in java-challenge coding assignment
//		we need to do unit testing if there is some sort of customization in the EmployeeRepository Code
//	  saving a new employee
		Employee employee = repository.save(new Employee("Subash", 1000, "IT"));
//	Asserting that the new employee added is as provided
		assertThat(employee).hasFieldOrPropertyWithValue("name", "Subash");
		assertThat(employee).hasFieldOrPropertyWithValue("salary", 1000);
		assertThat(employee).hasFieldOrPropertyWithValue("department", "IT");
	}

	@DisplayName("test for findAll() after adding the 2 data, checks data count and value")
	@Test
	public void should_find_all_employees() {
//		this testing is not required as it is a inbuilt function without any modification 
//		just for showing the assertion knowledge in java-challenge coding assignment
//		find all the tutorials using findAll
		Employee employee1 = new Employee("Employee 1", 1000, "Depart 1");
		entityManager.persist(employee1);

		Employee tut2 = new Employee("Employee 2", 2000, "Depart 2");
		entityManager.persist(tut2);

		List<Employee> employees = repository.findAll();

		assertThat(employees).hasSize(2).contains(employee1, tut2);
	}

	@DisplayName("test for findByIdOrThrow() custom method checks if it throws exception")
	@Test
	public void should_find_by_id_throws_exception_on_non_existing_employee() {
		Long id = 1000L;
//		checking if the EntityNotFoundException is raised with the excepted message when non existing id is passed
		assertThatExceptionOfType(EntityNotFoundException.class)
				.isThrownBy(() -> repository.findByIdOrThrow(id))
				.withMessage("Employee with id " + id + " not found");

	}
	
	@DisplayName("test for findByIdOrThrow() custom method checks if it returns valid employee")
	@Test
	public void should_find_by_id() {
		Employee employee1 = new Employee("Employee 1", 1000, "Depart 1");
		entityManager.persist(employee1);
//		checking if the returned object contains the same value
		Employee returnedEmployee = repository.findByIdOrThrow(employee1.getId());
		assertThat(returnedEmployee).hasFieldOrPropertyWithValue("name", employee1.getName());
		assertThat(returnedEmployee).hasFieldOrPropertyWithValue("salary", employee1.getSalary());
		assertThat(returnedEmployee).hasFieldOrPropertyWithValue("department", employee1.getDepartment());
		assertThat(returnedEmployee).hasFieldOrPropertyWithValue("id", employee1.getId());
		assertThat(returnedEmployee).hasNoNullFieldsOrProperties();

	}

}
