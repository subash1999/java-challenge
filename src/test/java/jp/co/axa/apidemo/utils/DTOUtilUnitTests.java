package jp.co.axa.apidemo.utils;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DTOUtilUnitTests {

	@DisplayName("test for all Null Values properties")
	@Test
	public void should_return_all_properties_as_null() {
		DummyDTO employee = new DummyDTO();
		String[] nullProperties = DTOUtil.getNullPropertyNames(employee);
		String[] expected = new String[] { "name", "salary", "department" };
//		sort the array for the easier comparison
		Arrays.sort(nullProperties);
		Arrays.sort(expected);
		assertArrayEquals(expected, nullProperties);
	}

	@DisplayName("test for single property as Null")
	@Test
	public void should_return_single_property_as_null() {
		DummyDTO employee = new DummyDTO();
		employee.setName("Subash");
		employee.setSalary(5555);
		String[] nullProperties = DTOUtil.getNullPropertyNames(employee);
		String[] expected = new String[] { "department" };
//		sort the array for the easier comparison
		Arrays.sort(nullProperties);
		Arrays.sort(expected);
		assertArrayEquals(expected, nullProperties);
	}
	
	@DisplayName("test for a single property set as Null and another property not set by the user")
	@Test
	public void should_return_two_properties_as_null() {
		DummyDTO employee = new DummyDTO();
		employee.setName(null);
		employee.setSalary(0);
		String[] nullProperties = DTOUtil.getNullPropertyNames(employee);
		String[] expected = new String[] { "name", "department" };
//		sort the array for the easier comparison
		Arrays.sort(nullProperties);
		Arrays.sort(expected);
		assertArrayEquals(expected, nullProperties);
	}

	@DisplayName("test for the IllegalArgumentException when null is passed instead of Object")
	@Test
	public void should_throw_illegal_argument_exception() {
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> DTOUtil.getNullPropertyNames(null));
	}
	
	//dummy DTO for testing
	@Data
	private class DummyDTO {
		@Getter
		@Setter
		private String name;
		
		@Getter
		@Setter
		private String department;
		
		@Getter
		@Setter
		private Integer salary;
		
	}

}
