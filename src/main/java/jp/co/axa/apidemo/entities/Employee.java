package jp.co.axa.apidemo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "EMPLOYEE")
public class Employee {

	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Getter
	@Setter
	@NotNull(message = "name is required")
	@NotBlank(message = "name must not be blank")
	@Column(name = "EMPLOYEE_NAME")
	private String name;

	@Getter
	@Setter
	@NotNull(message = "salary is required")
	@Column(name = "EMPLOYEE_SALARY")
	private Integer salary;

	@Getter
	@Setter
	@NotNull(message = "department is required")
	@NotBlank(message = "department must not be blank")
	@Column(name = "DEPARTMENT")
	private String department;
	
	/**
	 * Default Constructor
	 */
	@Autowired
	public Employee() {
		
	}

	/**
	 * Constructor for the employee entity with the all the parameters including id
	 * @param id
	 * @param name
	 * @param salary
	 * @param department
	 */
	public Employee(Long id, String name, Integer salary, String department) {
		this.id = id;
		this.name = name;
		this.salary = salary;
		this.department = department;
	}

	/**
	 * Constructor for the employee entity excluding id
	 * @param name
	 * @param salary
	 * @param department
	 */
	public Employee(String name, Integer salary, String department) {
		this.name = name;
		this.salary = salary;
		this.department = department;
	}

	@Override
	public String toString() {
		return "Employee{id:%d,name:'%s',department:'%s',salary:%d}".formatted(this.id, this.name, this.department,
				this.salary);
	}

}
