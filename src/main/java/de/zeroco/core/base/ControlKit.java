package de.zeroco.core.base;

import java.util.Map;

public class ControlKit {
	
	private Map<String, Object> reqData;
	
	public ControlKit() {
		super();
	}

	public ControlKit(Map<String, Object> reqData) {
		super();
		this.reqData = reqData;
	}

	public String param(String key) {
		Object object = reqData.get(key);
		return object == null ? null : object.toString().trim();
	}

}
