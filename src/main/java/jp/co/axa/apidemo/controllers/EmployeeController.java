package jp.co.axa.apidemo.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.co.axa.apidemo.dto.EmployeeDTO;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import jp.co.axa.apidemo.utils.DTOUtil;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * set the service for the controller
	 * 
	 * @param employeeService
	 */
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	/**
	 * filter and get all the list of employees
	 * @param searchFilter
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	@GetMapping("/employees")
	public List<Employee> getEmployees(
			@RequestParam(required = false, defaultValue = "") String searchFilter
			, @RequestParam(required = false, defaultValue = "name") String sortBy
			, @RequestParam(required = false, defaultValue = "asc") String sortDirection
			) {
		log.info("Employee Controller: get /employees Parameter Validated");
		List<Employee> employees = employeeService.retrieveEmployees(searchFilter, sortBy, sortDirection);
		log.info("Employee Controller: Employees List Retrived");
		return employees;
	}

	/**
	 * filter and get the paginated Page of the employees according to the
	 * parameters passed
	 * 
	 * @param searchFilter
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	@GetMapping("/paginated-employees")
	public Page<Employee> getPaginatedEmployees(@RequestParam(required = false, defaultValue = "") String searchFilter,
			@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDirection) {
		log.info("Employee Controller: get /paginated-employees Parameter Validated");
		Page<Employee> employees = employeeService.retrievePaginatedEmployees(searchFilter, pageNumber, pageSize,
				sortBy, sortDirection);
		log.info("Employee Controller: Paginated Employees List Retrived");
		return employees;
	}

	/**
	 * get the data of a single employee
	 * 
	 * @param employeeId
	 * @return
	 */
	@GetMapping("/employees/{employeeId}")
	public Employee getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
		log.info("Employee Controller: get /employees/{employeeId} Parameter Validated");
//    	this will throw an custom EntityNotFound if the employee does not exists
		Employee retrivedEmployee = employeeService.getEmployeeOrThrow(employeeId);
		log.info("Employee Controller: Single Employee Retrived");
		return retrivedEmployee;
	}

	/**
	 * add an employee
	 * 
	 * @param employee
	 * @return
	 */
	@PostMapping("/employees")
	public Employee saveEmployee(@Valid @RequestBody Employee employee, HttpServletResponse response) {
		log.info("Employee Controller: post /employees valid request");
		response.setStatus(HttpServletResponse.SC_CREATED);
		Employee retrivedEmployee =  employeeService.saveEmployee(employee);
		log.info("Employee Controller: Employee created successfully");
		return retrivedEmployee;
	}

	/**
	 * delete an employee
	 * 
	 * @param employeeId
	 */
	@DeleteMapping("/employees/{employeeId}")
	public void deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
//    	throw the exception if not found
		employeeService.getEmployeeOrThrow(employeeId);
		log.info("Employee Controller: deelete /employees/{employeeId} Valid Id passed");
		employeeService.deleteEmployee(employeeId);
		log.info("Employee Controller: Employee Deleted Successfully");
	}

	/**
	 * update an employee with all the attributes
	 * 
	 * @param employee
	 * @param employeeId
	 */
	@PutMapping("/employees/{employeeId}")
	public Employee updateEmployee(@Valid @RequestBody Employee employee,
			@PathVariable(name = "employeeId") Long employeeId) {
//    	throw the exception if not found
		employeeService.getEmployeeOrThrow(employeeId);
		log.info("Employee Controller: put /employees/{employeeId} Valid Id passed");
		employee.setId(employeeId);
		Employee retrivedEmployee =  employeeService.updateEmployee(employee);
		log.info("Employee Controller: All Employee Fields Updated Successfully");
		return retrivedEmployee;

	}

	/**
	 * partial update an employee with at least one attribute provided as not null
	 * 
	 * @param employee
	 * @param employeeId
	 */
	@PatchMapping("/employees/{employeeId}")
	public Employee partialUpdateEmployee(@Valid @RequestBody EmployeeDTO employeeDto,
			@PathVariable(name = "employeeId") Long employeeId) {
//    	this will throw the exception if not found    	
		Employee employee = employeeService.getEmployeeOrThrow(employeeId);
		log.info("Employee Controller: patch /employees/{employeeId} Valid Id passed");
		// copy non-null properties of employeeDto to employee
		String[] res = DTOUtil.getNullPropertyNames(employeeDto);
//    	this is a partial update so copying the values of employeeDto to employee while ignoring the null values
		BeanUtils.copyProperties(employeeDto, employee, DTOUtil.getNullPropertyNames(employeeDto));
//    	updating the enployee and retrieving the updated employee
		employee = employeeService.updateEmployee(employee);
		log.info("Employee Controller: Employee Partial Update Successful");
		return employee;

	}

}
