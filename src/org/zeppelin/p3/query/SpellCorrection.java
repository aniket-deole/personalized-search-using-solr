package org.zeppelin.p3.query;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class SpellCorrection implements JSONAware{

	public SpellCorrection(){
		
	}
	
	private String correction;
	
	/**
	 * @return the correction
	 */
	public String getCorrection() {
		return correction;
	}

	/**
	 * @param correction the correction to set
	 */
	public void setCorrection(String correction) {
		this.correction = correction;
	}

	@Override
	public String toJSONString() {
		StringBuffer sb = new StringBuffer();
		//sb.append("{");
		sb.append("\"" + JSONObject.escape(correction) + "\"");
		//sb.append("}");

		return sb.toString();
	}
}
