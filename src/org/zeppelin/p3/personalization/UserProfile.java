package org.zeppelin.p3.personalization;

import java.util.List;

public class UserProfile {
	
	private List<DocPreference> preferredDocs;

	/**
	 * @return the preferredDocs
	 */
	public List<DocPreference> getPreferredDocs() {
		return preferredDocs;
	}

	/**
	 * @param preferredDocs the preferredDocs to set
	 */
	public void setPreferredDocs(List<DocPreference> preferredDocs) {
		this.preferredDocs = preferredDocs;
	}
	
	

}
