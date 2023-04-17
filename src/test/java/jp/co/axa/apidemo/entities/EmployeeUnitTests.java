package jp.co.axa.apidemo.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeUnitTests {

	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@DisplayName("test for toString() override checks if proper string is created")
	@Test
    public void should_give_proper_employee_object_string() {
        String expectedToString = "Employee{id:null,name:'Employee 1',department:'HR',salary:1000}";
        Employee employee = new Employee("Employee 1", 1000, "HR");
        assertEquals(expectedToString, employee.toString());
    }

	@DisplayName("test for Employee(id,name,salary,department) constructor checks if initialized successfully")
	@Test
    public void should_initialize_successfully_including_id() {
        Employee employee = new Employee("Employee 1", 1000, "HR");
        assertNotNull(employee);
        assertEquals("Employee 1", employee.getName());
        assertEquals((Integer) 1000, (Integer)employee.getSalary());
        assertEquals("HR", employee.getDepartment());
    }

	@DisplayName("test for Employee(name,salary,department) constructor checks if initialized successfully")
	@Test
    public void should_initialize_successfully_excluding_id() {
        Employee employee = new Employee(1L, "Employee 1", 1000, "HR");
        assertNotNull(employee);
        assertEquals((Long)1L, (Long)employee.getId());
        assertEquals("Employee 1", employee.getName());
        assertEquals((Integer)1000, (Integer)employee.getSalary());
        assertEquals("HR", employee.getDepartment());
    }	

	@DisplayName("test for constructor intialization validation for @Valid annotation in parameter")
	@Test
	void should_validate_all_values() {
		Employee employee = new Employee(null, null, null, null);
		assertThat(validator.validate(employee)).hasSize(5);

		employee = new Employee(1L, null, null, null);
		assertThat(validator.validate(employee)).hasSize(5);

		employee = new Employee(1L, "Employee 1", null, null);
		assertThat(validator.validate(employee)).hasSize(3);

		employee = new Employee(1L, "Employee 1", 1000, null);
		assertThat(validator.validate(employee)).hasSize(2);
		
		employee = new Employee(null, "Employee 1", 1000, "HR");
		assertThat(validator.validate(employee)).isEmpty();
	}

}
