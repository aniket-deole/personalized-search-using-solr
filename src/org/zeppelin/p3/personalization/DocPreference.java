package org.zeppelin.p3.personalization;

public class DocPreference {
	/**
	 * Document Id
	 */
    Integer docId;
    
    /**
     * Number of clicks on the document
     */
    Integer numClicks;
    
    /**
     * likeScore given by the user
     */
    Integer likeScore;

	/**
	 * @return the docId
	 */
	public Integer getDocId() {
		return docId;
	}

	/**
	 * @param docId the docId to set
	 */
	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	/**
	 * @return the numClicks
	 */
	public Integer getNumClicks() {
		return numClicks;
	}

	/**
	 * @param numClicks the numClicks to set
	 */
	public void setNumClicks(Integer numClicks) {
		this.numClicks = numClicks;
	}

	/**
	 * @return the likeScore
	 */
	public Integer getLikeScore() {
		return likeScore;
	}

	/**
	 * @param likeScore the likeScore to set
	 */
	public void setLikeScore(Integer likeScore) {
		this.likeScore = likeScore;
	}
}
