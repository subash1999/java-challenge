package jp.co.axa.apidemo.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		employeeRepository.deleteAll();
	}

	/**
	 * Test the POST /api/v1/employees for the employee create new employee
	 * {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_201_status_with_new_created_employee() throws Exception {

		// employee object
		Employee employee = new Employee("Subash", 1111, "Sales");

		// mock MvcPerform
		ResultActions response = mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));

		// assert
		response.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.name").value(employee.getName()))
				.andExpect(jsonPath("$.department").value(employee.getDepartment()))
				.andExpect(jsonPath("$.salary").value(employee.getSalary()));

	}

	/**
	 * Test the POST /api/v1/employees for the employee bad request, one attribute
	 * null {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_400_status_with_error_message_check_null_blank() throws Exception {

		/* null name */

		// employee object
		Employee employee = new Employee(null, 1111, "Sales");

		// moclMvc perform
		ResultActions response = mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));

		// assert
		response.andDo(print()).andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.status").exists()).andExpect(jsonPath("$.path").exists())
				.andExpect(jsonPath("$.message").exists());
//		--------------------------------------------------------------------------------------------------
		/* blank name */
		employee = new Employee("", 1111, "Sales");

		// moclMvc perform
		response = mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));

		// assert
		response.andDo(print()).andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.status").exists()).andExpect(jsonPath("$.path").exists())
				.andExpect(jsonPath("$.message").exists());
//		--------------------------------------------------------------------------------------------------

		/* null Department */
		employee = new Employee("Subash", 1111, null);

		// moclMvc perform
		response = mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));

		// assert
		response.andDo(print()).andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.status").exists()).andExpect(jsonPath("$.path").exists())
				.andExpect(jsonPath("$.message").exists());
//		--------------------------------------------------------------------------------------------------

		/* blank Department */
		employee = new Employee("Subash", 1111, "");

		// moclMvc perform
		response = mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));

		// assert
		response.andDo(print()).andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.status").exists()).andExpect(jsonPath("$.path").exists())
				.andExpect(jsonPath("$.message").exists());

//		--------------------------------------------------------------------------------------------------
		/* null salary */
		employee = new Employee("Subash", null, "null");

		// moclMvc perform
		response = mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));

		// assert
		response.andDo(print()).andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.status").exists()).andExpect(jsonPath("$.path").exists())
				.andExpect(jsonPath("$.message").exists());

	}

	/**
	 * Test the POST /api/v1/employees for the employee bad request, no content
	 * given {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_400_status_with_error_message_no_content_given() throws Exception {

		// moclMvc perform
		ResultActions response = mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.status").exists()).andExpect(jsonPath("$.path").exists())
				.andExpect(jsonPath("$.message").exists());

	}

	/**
	 * Test the GET /api/v1/employees for the employees success, return all
	 * employees {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_200_status_with_all_employees() throws Exception {

		// employee object save
		Employee employee1 = new Employee("Employee 1", 1000, "HR");
		Employee employee2 = new Employee("Employee 2", 2000, "Development");
		employeeRepository.saveAll(Arrays.asList(employee1, employee2));

		// mock MvcPerform
		ResultActions response = mockMvc.perform(get("/api/v1/employees").contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value(employee1.getName()))
				.andExpect(jsonPath("$[0].salary").value(employee1.getSalary()))
				.andExpect(jsonPath("$[0].department").value(employee1.getDepartment()))
				.andExpect(jsonPath("$[1].name").value(employee2.getName()))
				.andExpect(jsonPath("$[1].salary").value(employee2.getSalary()))
				.andExpect(jsonPath("$[1].department").value(employee2.getDepartment()));
	}

	/**
	 * Test the GET /api/v1/employees for the employees success, return all sorted
	 * employees {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_200_status_with_all_employees_search_sort() throws Exception {
		/*
		 * SearchFilter: 'Employee 1' default sort i.e. sortBy:'name'
		 * sortDirection:'Asc'
		 */
		// employee object save
		Employee employee1 = new Employee("Employee 1", 1000, "HR");
		Employee employee2 = new Employee("Employee 2", 2000, "Development");
		employeeRepository.saveAll(Arrays.asList(employee1, employee2));

		// mock MvcPerform
		ResultActions response = mockMvc.perform(
				get("/api/v1/employees").param("searchFilter", "Employee 1").contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value(employee1.getName()))
				.andExpect(jsonPath("$[0].salary").value(employee1.getSalary()))
				.andExpect(jsonPath("$[0].department").value(employee1.getDepartment()));

//		------------------------------------------------------------------------------------------------
		/*
		 * SearchFilter: 'Employee' default sort i.e. sortBy:'name' sortDirection:'Asc'
		 */
		// mock MvcPerform
		response = mockMvc.perform(
				get("/api/v1/employees").param("searchFilter", "Employee").contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value(employee1.getName()))
				.andExpect(jsonPath("$[0].salary").value(employee1.getSalary()))
				.andExpect(jsonPath("$[0].department").value(employee1.getDepartment()))
				.andExpect(jsonPath("$[1].name").value(employee2.getName()))
				.andExpect(jsonPath("$[1].salary").value(employee2.getSalary()))
				.andExpect(jsonPath("$[1].department").value(employee2.getDepartment()));

//		------------------------------------------------------------------------------------------------
		/* SearchFilter: 'Employee' sortBy:'name' sortDirection:'Desc' */
		// mock MvcPerform
		response = mockMvc.perform(get("/api/v1/employees").param("searchFilter", "Employee").param("sortBy", "name")
				.param("sortDirection", "desc").contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$[1].name").value(employee1.getName()))
				.andExpect(jsonPath("$[1].salary").value(employee1.getSalary()))
				.andExpect(jsonPath("$[1].department").value(employee1.getDepartment()))
				.andExpect(jsonPath("$[0].name").value(employee2.getName()))
				.andExpect(jsonPath("$[0].salary").value(employee2.getSalary()))
				.andExpect(jsonPath("$[0].department").value(employee2.getDepartment()));

//		------------------------------------------------------------------------------------------------
		/* SearchFilter: 'Employee' sortBy:'department' sortDirection:'Desc' */
//		save employee for the sort assertion
		Employee employee3 = new Employee("Employee 3", 2000, "Sales");
		employeeRepository.save(employee3);

		// mock MvcPerform
		response = mockMvc.perform(get("/api/v1/employees").param("searchFilter", "Employee")
				.param("sortBy", "department").param("sortDirection", "desc").contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value(employee3.getName()))
				.andExpect(jsonPath("$[0].salary").value(employee3.getSalary()))
				.andExpect(jsonPath("$[0].department").value(employee3.getDepartment()))// Sales
				.andExpect(jsonPath("$[1].name").value(employee1.getName()))
				.andExpect(jsonPath("$[1].salary").value(employee1.getSalary()))
				.andExpect(jsonPath("$[1].department").value(employee1.getDepartment())) // HR
				.andExpect(jsonPath("$[2].name").value(employee2.getName()))
				.andExpect(jsonPath("$[2].salary").value(employee2.getSalary()))
				.andExpect(jsonPath("$[2].department").value(employee2.getDepartment())); // Development
	}

	/**
	 * Test the /api/v1/paginated-employees for the default search,page,pazeSize,
	 * sortBy and sortDirection {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_200_status_with_paginated_employees_default() throws Exception {
		/*
		 * On all the default parameters, searchFilter: null pageSize: 10 pageNumber: 0
		 * sortBy: name sortDirection: asc
		 */
		// employee object save
		Employee employee1 = new Employee("Employee 1", 1000, "HR");
		Employee employee2 = new Employee("Employee 2", 2000, "Development");
		Employee employee3 = new Employee("Employee 3", 3000, "Development");
		List<Employee> addEmployees = Arrays.asList(employee1, employee2, employee3);
		employeeRepository.saveAll(addEmployees);

		// mock MvcPerform
		ResultActions response = mockMvc
				.perform(get("/api/v1/paginated-employees").contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value(3)).andExpect(jsonPath("$.last").value(true))
				.andExpect(jsonPath("$.totalPages").value(1)).andExpect(jsonPath("$.size").value(10))
				.andExpect(jsonPath("$.number").value(0))
				.andExpect(jsonPath("$.content.[0].name").value(employee1.getName())) // Employee 1
				.andExpect(jsonPath("$.content.[0].salary").value(employee1.getSalary()))
				.andExpect(jsonPath("$.content.[0].department").value(employee1.getDepartment()))
				.andExpect(jsonPath("$.content.[1].name").value(employee2.getName())) // Employee 2
				.andExpect(jsonPath("$.content.[1].salary").value(employee2.getSalary()))
				.andExpect(jsonPath("$.content.[1].department").value(employee2.getDepartment()))
				.andExpect(jsonPath("$.content.[2].name").value(employee3.getName())) // Employee 3
				.andExpect(jsonPath("$.content.[2].salary").value(employee3.getSalary()))
				.andExpect(jsonPath("$.content.[2].department").value(employee3.getDepartment()));

	}

	/**
	 * Test the /api/v1/paginated-employees for the userGiven search,page,pazeSize,
	 * sortBy and sortDirection {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_200_status_with_paginated_employees_search_sort_pagination_test() throws Exception {

		// employee object save
		Employee employee1 = new Employee("Employee 1", 1000, "HR");
		Employee employee2 = new Employee("Employee 2", 2000, "Development");
		Employee employee3 = new Employee("Employee 3", 3000, "Development");
		List<Employee> addEmployees = Arrays.asList(employee1, employee2, employee3);
		employeeRepository.saveAll(addEmployees);

		/*
		 * On all the default parameters, searchFilter: Development pageSize: 2
		 * pageNumber: 0 sortBy: name sortDirection: asc
		 */
		// mock MvcPerform
		ResultActions response = mockMvc.perform(get("/api/v1/paginated-employees").param("searchFilter", "Development")
				.param("pageSize", "2").param("pageNumber", "0").param("sortBy", "name").param("sortDirection", "asc")
				.contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value(2)).andExpect(jsonPath("$.last").value(true))
				.andExpect(jsonPath("$.totalPages").value(1)).andExpect(jsonPath("$.size").value(2))
				.andExpect(jsonPath("$.number").value(0))
				.andExpect(jsonPath("$.content.[0].name").value(employee2.getName())) // Employee 2
				.andExpect(jsonPath("$.content.[0].salary").value(employee2.getSalary()))
				.andExpect(jsonPath("$.content.[0].department").value(employee2.getDepartment())) // Development
				.andExpect(jsonPath("$.content.[1].name").value(employee3.getName())) // Employee 3
				.andExpect(jsonPath("$.content.[1].salary").value(employee3.getSalary()))
				.andExpect(jsonPath("$.content.[1].department").value(employee3.getDepartment())); // Development

//		--------------------------------------------------------------------------------------------------------------
		/*
		 * On all the default parameters, searchFilter: Development pageSize: 2
		 * pageNumber: 0 sortBy: name sortDirection: desc
		 */
		// mock MvcPerform
		response = mockMvc.perform(get("/api/v1/paginated-employees").param("searchFilter", "Development")
				.param("pageSize", "2").param("pageNumber", "0").param("sortBy", "name").param("sortDirection", "desc")
				.contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value(2)).andExpect(jsonPath("$.last").value(true))
				.andExpect(jsonPath("$.totalPages").value(1)).andExpect(jsonPath("$.size").value(2))
				.andExpect(jsonPath("$.number").value(0))
				.andExpect(jsonPath("$.content.[0].name").value(employee3.getName())) // Employee 3
				.andExpect(jsonPath("$.content.[0].salary").value(employee3.getSalary()))
				.andExpect(jsonPath("$.content.[0].department").value(employee3.getDepartment())) // Development
				.andExpect(jsonPath("$.content.[1].name").value(employee2.getName())) // Employee 2
				.andExpect(jsonPath("$.content.[1].salary").value(employee2.getSalary()))
				.andExpect(jsonPath("$.content.[1].department").value(employee2.getDepartment())); // Development

//		--------------------------------------------------------------------------------------------------------------
		/*
		 * On all the default parameters, searchFilter: Employee pageSize: 2 pageNumber:
		 * 1 sortBy: name sortDirection: desc
		 */
		// mock MvcPerform
		response = mockMvc.perform(get("/api/v1/paginated-employees").param("searchFilter", "Employee")
				.param("pageSize", "2").param("pageNumber", "1").param("sortBy", "name").param("sortDirection", "desc")
				.contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value(3)).andExpect(jsonPath("$.last").value(true))
				.andExpect(jsonPath("$.totalPages").value(2)).andExpect(jsonPath("$.size").value(2))
				.andExpect(jsonPath("$.number").value(1))
				.andExpect(jsonPath("$.content.[0].name").value(employee1.getName())) // Employee 1
				.andExpect(jsonPath("$.content.[0].salary").value(employee1.getSalary()))
				.andExpect(jsonPath("$.content.[0].department").value(employee1.getDepartment())); // Development
//		--------------------------------------------------------------------------------------------------------------
		/*
		 * On all the default parameters, searchFilter: Employee pageSize: 2 pageNumber:
		 * 1 sortBy: department sortDirection: asc
		 */
		// mock MvcPerform
		response = mockMvc.perform(get("/api/v1/paginated-employees").param("searchFilter", "Employee")
				.param("pageSize", "2").param("pageNumber", "1").param("sortBy", "department")
				.param("sortDirection", "desc").contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value(3)).andExpect(jsonPath("$.last").value(true))
				.andExpect(jsonPath("$.totalPages").value(2)).andExpect(jsonPath("$.size").value(2))
				.andExpect(jsonPath("$.number").value(1))
				.andExpect(jsonPath("$.content.[0].name").value(employee2.getName())) // Employee 2
				.andExpect(jsonPath("$.content.[0].salary").value(employee2.getSalary()))
				.andExpect(jsonPath("$.content.[0].department").value(employee2.getDepartment())); // Development

	}

	/**
	 * Test the GET /api/v1/employees/{id} for the employees success, for existing
	 * id {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_200_status_with_single_employee() throws Exception {
		/* id: 1 */
		// employee object save
		Employee employee1 = new Employee("Employee 1", 1000, "HR");
		Employee employee2 = new Employee("Employee 2", 2000, "Development");
		employeeRepository.saveAll(Arrays.asList(employee1, employee2));

		// mock MvcPerform
		ResultActions response = mockMvc
				.perform(get("/api/v1/employees/{id}", employee1.getId()).contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.name").value(employee1.getName()))
				.andExpect(jsonPath("$.salary").value(employee1.getSalary()))
				.andExpect(jsonPath("$.department").value(employee1.getDepartment()));
//		------------------------------------------------------------------------------------------------
		/* id: 2 */
		// mock MvcPerform
		response = mockMvc
				.perform(get("/api/v1/employees/{id}", employee2.getId()).contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.name").value(employee2.getName()))
				.andExpect(jsonPath("$.salary").value(employee2.getSalary()))
				.andExpect(jsonPath("$.department").value(employee2.getDepartment()));
	}

	/**
	 * Test the GET /api/v1/employees/{id} for the employees bad request, not
	 * existing id {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_404_status_with_single_employee_not_found() throws Exception {

		// employee object save
		Employee employee1 = new Employee("Employee 1", 1000, "HR");
		Employee employee2 = new Employee("Employee 2", 2000, "Development");
		employeeRepository.saveAll(Arrays.asList(employee1, employee2));

		// mock MvcPerform
		ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}", -1).contentType(MediaType.ALL));

		// assert
		response.andDo(print()).andExpect(status().isNotFound()).andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.message").exists()).andExpect(jsonPath("$.path").exists())
				.andExpect(jsonPath("$.timestamp").exists()).andExpect(jsonPath("$.name").doesNotExist())
				.andExpect(jsonPath("$.salary").doesNotExist()).andExpect(jsonPath("$.department").doesNotExist());

	}

	/**
	 * Test the PUT /api/v1/employees/{id} for the employees success update all
	 * fields fields {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_200_status_with_single_employee_updated() throws Exception {

		// employee object save
		Employee employee1 = new Employee("Employee 1", 1000, "HR");
		employeeRepository.saveAll(Arrays.asList(employee1));
		// data to update to
		Employee employee2 = new Employee("Employee 2", 2000, "Development");

		// mock MvcPerform
		ResultActions response = mockMvc.perform(put("/api/v1/employees/{id}", employee1.getId())
				.content(objectMapper.writeValueAsString(employee2)).contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(employee1.getId())) // 1
				.andExpect(jsonPath("$.name").value(employee2.getName())) // Employee 2
				.andExpect(jsonPath("$.salary").value(employee2.getSalary())) // 2000
				.andExpect(jsonPath("$.department").value(employee2.getDepartment())); // Development

	}

	/**
	 * Test the PUT /api/v1/employees/{id} for the employees error when id not found
	 * fields {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_404_status_with_not_found_error() throws Exception {

		// employee object save
		Employee employee1 = new Employee("Employee 1", 1000, "HR");
		employeeRepository.saveAll(Arrays.asList(employee1));
		// data to update to
		Employee employee2 = new Employee("Employee 2", 2000, "Development");

		// mock MvcPerform
		ResultActions response = mockMvc.perform(put("/api/v1/employees/{id}", -1)
				.content(objectMapper.writeValueAsString(employee2)).contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isNotFound()).andExpect(jsonPath("$.timestamp").exists())
				.andExpect(jsonPath("$.error").exists()).andExpect(jsonPath("$.message").exists())
				.andExpect(jsonPath("$.path").exists());

	}

	/**
	 * Test the PUT /api/v1/employees/{id} for the employees error when all the
	 * fields are not given fields {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_400_status_put_update_with_bad_response_error() throws Exception {

		// employee object save
		Employee employee1 = new Employee("Employee 1", 1000, "HR");
		employeeRepository.saveAll(Arrays.asList(employee1));

		// mock MvcPerform
		ResultActions response = mockMvc.perform(put("/api/v1/employees/{id}", employee1.getId())
				.content("{\"name\":\"Updated Name\"}").contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isBadRequest()).andExpect(jsonPath("$.timestamp").exists())
				.andExpect(jsonPath("$.error").exists()).andExpect(jsonPath("$.message").exists())
				.andExpect(jsonPath("$.path").exists());

	}

	/**
	 * Test the PATCH /api/v1/employees/{id} for the employees success: update only
	 * some of the fields
	 * 
	 * {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_200_status_with_single_employee_updated_some_filds() throws Exception {

		// employee object save
		Employee employee1 = new Employee("Employee 1", 1000, "HR");
		employeeRepository.saveAll(Arrays.asList(employee1));

		/* update only name to name:"Updated Name" */
		// mock MvcPerform
		ResultActions response = mockMvc.perform(patch("/api/v1/employees/{id}", employee1.getId())
				.content("{\"name\":\"Updated Name\"}").contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(employee1.getId())) // 1
				.andExpect(jsonPath("$.name").value("Updated Name")) // Updated Name
				.andExpect(jsonPath("$.salary").value(employee1.getSalary()))
				.andExpect(jsonPath("$.department").value(employee1.getDepartment()));

//		---------------------------------------------------------------------------------------------------------------
		/* update only department, to department:"Updated Department" */
		// mock MvcPerform
		response = mockMvc.perform(patch("/api/v1/employees/{id}", employee1.getId())
				.content("{\"department\":\"Updated Department\"}").contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(employee1.getId())) // 1
				.andExpect(jsonPath("$.name").value("Updated Name")) // Updated Name
				.andExpect(jsonPath("$.salary").value(employee1.getSalary()))
				.andExpect(jsonPath("$.department").value("Updated Department"));

//		---------------------------------------------------------------------------------------------------------------
		/* update only salary, to salary:"123" */
		// mock MvcPerform
		response = mockMvc.perform(patch("/api/v1/employees/{id}", employee1.getId()).content("{\"salary\":123}")
				.contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(employee1.getId())) // 1
				.andExpect(jsonPath("$.name").value("Updated Name")) // Updated Name
				.andExpect(jsonPath("$.salary").value("123"))
				.andExpect(jsonPath("$.department").value("Updated Department"));

	}

	/**
	 * Test the PATCH /api/v1/employees/{id} for the employees bad error: no content
	 * given At least one valid field should be given {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_400_status_for_patch_update_no_valid_fields_given() throws Exception {

		// employee object save
		Employee employee1 = new Employee("Employee 1", 1000, "HR");
		employeeRepository.saveAll(Arrays.asList(employee1));

		// mock MvcPerform
		ResultActions response = mockMvc.perform(patch("/api/v1/employees/{id}", employee1.getId()).content("{}")
				.contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isBadRequest()).andExpect(jsonPath("$.timestamp").exists())
				.andExpect(jsonPath("$.error").exists()).andExpect(jsonPath("$.message").exists())
				.andExpect(jsonPath("$.path").exists());
	}

	/**
	 * Test the DELETE /api/v1/employees/{id} for the employees success
	 * {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_200_status_for_delete() throws Exception {
		// employee object save
		Employee employee1 = new Employee("Employee 1", 1000, "HR");
		employeeRepository.saveAll(Arrays.asList(employee1));
		// mock MvcPerform
		ResultActions response = mockMvc
				.perform(delete("/api/v1/employees/{id}", employee1.getId()).contentType(MediaType.APPLICATION_JSON));

		// assert
		response.andDo(print()).andExpect(status().isOk());
	}

	/**
	 * Test the DELETE /api/v1/employees/{id} for the employees not found
	 * {@inheritDoc}
	 *
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void should_return_404_status_for_delete() throws Exception {
		// employee object save
		Employee employee1 = new Employee("Employee 1", 1000, "HR");
		employeeRepository.saveAll(Arrays.asList(employee1));
		// mock MvcPerform
		ResultActions response = mockMvc
				.perform(delete("/api/v1/employees/{id}", -1).contentType(MediaType.APPLICATION_JSON));

		// assert
		// assert
		response.andDo(print()).andExpect(status().isNotFound()).andExpect(jsonPath("$.timestamp").exists())
				.andExpect(jsonPath("$.error").exists()).andExpect(jsonPath("$.message").exists())
				.andExpect(jsonPath("$.path").exists());
	}
}
