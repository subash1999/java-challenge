package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

public interface EmployeeService {

	/**
	 * Retrieve all the employees that matches the search parameter passed.
	 * @param search
	 * @return
	 */
    public List<Employee> retrieveEmployees(
    		String search
    		, String sortBy
    		, String sortDirection);
    
    /**
     * Retrieve the paginated data of the Employees that matches the search parameter passed. 
     * The pagination can be configured using the other parameters
     * @param search
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDirection
     * @return
     */
    public Page<Employee> retrievePaginatedEmployees(
    		String search
    		, int pageNumber
    		, int pageSize
    		, String sortBy
    		, String sortDirection
    	);
    

    /**
     * Get a single employee
     * @param employeeId
     * @return
     */
    public Employee getEmployee(Long employeeId);
    
    /**
     * Get a single employee and throw a EntityNotFoundException if the employee does not exists
     * @param employeeId
     * @return
     */
    public Employee getEmployeeOrThrow(Long employeeId);
	
    /**
	 * Save an employee to the database
	 * @param employee
	 * @return
	 */
    public Employee saveEmployee(Employee employee);
    
    /**
     * Delete an employee with the given id
     * @param employeeId
     */
    public void deleteEmployee(Long employeeId);

    /**
     * Update the employee
     * @param employee
     * @return
     */
    public Employee updateEmployee(Employee employee);
}