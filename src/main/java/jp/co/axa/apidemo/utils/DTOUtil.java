package jp.co.axa.apidemo.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import jp.co.axa.apidemo.dto.EmployeeDTO;

public class DTOUtil {

	/**
	 * This will return the null properties of the object given
	 * @param employeeDto
	 * @return
	 */
	 public static String[] getNullPropertyNames(Object source) {
	        BeanWrapper src = new BeanWrapperImpl(source);
	        PropertyDescriptor[] pds = src.getPropertyDescriptors();

	        Set<String> emptyNames = new HashSet<>();
	        for (PropertyDescriptor pd : pds) {
	        	if (pd.getWriteMethod() != null) {
	                Object srcValue = src.getPropertyValue(pd.getName());
	                if (srcValue == null) {
	                    emptyNames.add(pd.getName());
	                }
	            }
	        }

	        String[] result = new String[emptyNames.size()];
	        return emptyNames.toArray(result);
	    }

}
