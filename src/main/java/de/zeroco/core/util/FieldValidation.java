package de.zeroco.core.util;

import java.util.Map;

public class FieldValidation {
	
	public static String required(Map<String, Object> reqData, String... keys) {
		if (reqData == null || keys == null || keys.length == 0)
			return null;
		for (String key : keys) {
			Object value = reqData.get(key);
			if (value == null || value.toString().trim().equals(""))
				return "required fields are missing";
		}
		return null;
	}
}
