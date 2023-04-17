package jp.co.axa.apidemo.dto;

import jp.co.axa.apidemo.annotations.AtLeastOneNotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AtLeastOneNotNull(message="At least one of the name, department or salary must be provided and must not be null")
public class EmployeeDTO {

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
