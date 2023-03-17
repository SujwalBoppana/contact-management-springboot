package de.zeroco.core.util;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.zeroco.core.pojo.Company;
import de.zeroco.core.pojo.Contact;

public class Utility {
	/**
	 * this method will check wheather the given input is empty or not
	 * 
	 * @author sujwal B
	 * @since 2022-12-02
	 * @param input
	 * @return true or false
	 */
	public static boolean isBlank(Object input) {
		if (input == null)
			return true;
		else if (input instanceof String) {
			String variable = (String) input;
			if ((variable.trim().equals("")))
				return true;
		} else if (input instanceof Boolean) {
			if ((Boolean) input == false)
				return true;
		} else if (input instanceof Character) {
			if ((Character) input == ' ')
				return true;
		} else if (input instanceof Byte) {
			if ((Byte) input <= 0)
				return true;
		} else if (input instanceof Short) {
			if ((Short) input <= 0)
				return true;
		} else if (input instanceof Integer) {
			if ((Integer) input <= 0)
				return true;
		} else if (input instanceof Long) {
			if ((Long) input <= 0)
				return true;
		} else if (input instanceof Float) {
			if ((Float) input <= 0)
				return true;
		} else if (input instanceof Double) {
			if ((Double) input <= 0)
				return true;
		} else if (input.getClass().isArray()) {
			if (Array.getLength(input) == 0)
				return true;
		} else if (input instanceof Collection<?>) {
			if (((Collection<?>) input).size() == 0)
				return true;
		} else if (input instanceof Map<?, ?>) {
			if (((Map<?, ?>) input).size() == 0)
				return true;
		}

		return false;
	}

	public static boolean isBlankWithVarArguments(Object... input) {
		if (isBlank(input)) {
			return true;
		}
		for (Object object : input) {
			if (isBlank(object)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * this method will check wheather the given input has data or not
	 * 
	 * @author sujwal B
	 * @since 2022-12-02
	 * @param input
	 * @return true or false
	 */
	public static boolean hasData(Object input) {
		return !isBlank(input);
	}

	public static int ageCalculator(String dob) {
		if (isBlank(dob))
			return 0;
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate birthDate = LocalDate.parse(dob, dateFormat);
		LocalDate currentDate = LocalDate.now();
		Period period = Period.between(birthDate, currentDate);
		return period.getYears();
	}

	public static java.sql.Date stringToDate(String stringDate) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		java.sql.Date sqlDate = null;
		try {
			date = df.parse(stringDate);
			sqlDate = new java.sql.Date(date.getTime());
			return sqlDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sqlDate;
	}

	public static boolean isValidEmail(String input) {
		if (isBlank(input)) return false;
		return Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z]+\\.)+[a-zA-Z]{2,7}$")
				.matcher(input).find();
	}

	public static boolean isValidMoblieNo(String input) {
		if (isBlank(input)) return false;
		return Pattern.compile("[6-9][0-9]{9}").matcher(input).find();
	}
	
	public static Map<String, Object> objectToMap(Company company) {
		if (company == null)
			return null;
		Map<String, Object> resData = new LinkedHashMap<String, Object>();
		resData.put("id", company.getId());
		resData.put("name", company.getName());
		resData.put("code", company.getCode());
		resData.put("email", company.getEmail());
		resData.put("phone", company.getPhone());
		resData.put("website", company.getWebsite());
		resData.put("address", company.getAddress());
		return resData;
	}
	
	public static Map<String, Object> objectToMap(Contact contact) {
		if (contact == null)
			return null;
		Map<String, Object> resData = new LinkedHashMap<String, Object>();
		resData.put("company", contact.getCompany());
		resData.put("id", contact.getId());
		resData.put("name", contact.getName());
		resData.put("email", contact.getEmail());
		resData.put("phone", contact.getPhone());
		resData.put("address", contact.getAddress());
		resData.put("dob", contact.getDob());
		resData.put("age", contact.getAge());
		resData.put("gender", contact.getGender());
		return resData;
	}
	
	public Map<String, Object> objectToMap(Object obj) {
		ObjectMapper oMapper = new ObjectMapper();
		return oMapper.convertValue(obj, Map.class);
	}
	
	
}
