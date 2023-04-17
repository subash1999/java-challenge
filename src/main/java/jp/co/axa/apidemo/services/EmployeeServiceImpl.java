package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.controllers.EmployeeController;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * set the repository for this service
	 * 
	 * @param employeeRepository
	 */
	public void setEmployeeRepository(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public List<Employee> retrieveEmployees(String search, String sortBy, String sortDirection) {
//      specification for the filtering (Search) of the data
		Specification<Employee> specification = this.getSearchSpecification(search);
//  	sort the data
		Sort sort = Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
		List<Employee> employees;
		if (specification != null) {
			// Apply the specification if it is defined using JpaSpecificationExecutor
			employees =  employeeRepository.findAll(specification, sort);
		} else {
			// Return all results if no specification is defined
			employees =  employeeRepository.findAll();
		}
		log.info("Employee Service: Employee retrived successfully");
		return employees;
	}

	public Page<Employee> retrievePaginatedEmployees(String search, int pageNumber, int pageSize, String sortBy,
			String sortDirection) {
//    	sort for the pageable
		Sort sort = Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
//    	apply the sort in the pageable
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
//        specification for the filtering (Search) of the data
		Specification<Employee> specification = this.getSearchSpecification(search);
		Page<Employee> employees;
		if (specification != null) {
			// Apply the specification if it is defined using JpaSpecificationExecutor
			employees =  employeeRepository.findAll(specification, pageable);
		} else {
			// Return all results if no specification is defined
			employees =  employeeRepository.findAll(pageable);
		}
		log.info("Employee Service: Paginated Employee retrived successfully");
		return employees;
	}

	/**
	 * Get the Specification for the Employee. The specification helps to filter the
	 * data according to name and department of Employee.
	 * 
	 * @param search
	 * @return
	 */
	private Specification<Employee> getSearchSpecification(String search) {
		Specification<Employee> specification = null;
		if (search != null && !search.trim().isEmpty()) {
			specification = (root, query, criteriaBuilder) -> {
				String pattern = "%" + search.toLowerCase() + "%";
				return criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
						criteriaBuilder.like(criteriaBuilder.lower(root.get("department")), pattern));
			};
		}
		log.info("Employee Service: Specification retrived from string");
		return specification;
	}

	public Employee getEmployee(Long employeeId) {
		Optional<Employee> optEmp = employeeRepository.findById(employeeId);
		log.info("Employee Service: Employee Retrived by id");
		return optEmp.get();
	}

	public Employee getEmployeeOrThrow(Long employeeId) {
		Employee employee = employeeRepository.findByIdOrThrow(employeeId);
		log.info("Employee Service: Employee Retrived by id Succefully without throwing exception");
		return employee;
	}

	@CacheEvict(value = "employees", allEntries = true)
	public Employee saveEmployee(Employee employee) {
		Employee savedEmployee =  employeeRepository.save(employee);
		log.info("Employee Service: Employee saved successfully");
		return savedEmployee;
	}

	@CacheEvict(value = "employees", allEntries = true)
	public void deleteEmployee(Long employeeId) {
		employeeRepository.deleteById(employeeId);
		log.info("Service: Employee Deleted Successfully");
	}

	@CacheEvict(value = "employees", allEntries = false)
	public Employee updateEmployee(Employee employee) {
		Employee updatedEmployee =  employeeRepository.save(employee);
		log.info("Service: Employee Updated Successfully");
		return updatedEmployee;
	}
}