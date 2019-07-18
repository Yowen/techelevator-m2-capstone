package com.techelevator.database_link;

public class Site {

	private long siteId;
	private long campgroundId;
	private int siteNumber;
	private int maxOccupancy;
	private boolean accessible;
	private int maxRvLength;
	private boolean utilities;

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public long getCampgroundId() {
		return campgroundId;
	}

	public void setCampgroundId(long campgroundId) {
		this.campgroundId = campgroundId;
	}

	public int getSiteNumber() {
		return siteNumber;
	}

	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}

	public int getMaxOccupancy() {
		return maxOccupancy;
	}

	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}

	public boolean isAccessible() {
		return accessible;
	}

	public String getAccessibility() {
		if (isAccessible()) {
			return "Yes";
		} else {
			return "No";
		}
	}

	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}

	public String getMaxRvLength() {
		if (maxRvLength == 0) {
			return "N/A";
		} else {
			return "" + maxRvLength;
		}
	}

	public void setMaxRvLength(int maxRvLength) {
		this.maxRvLength = maxRvLength;
	}

	public boolean isUtilities() {
		return utilities;
	}

	public void setUtilities(boolean utilities) {
		this.utilities = utilities;
	}

	public String utilitiesExist() {
		if (isUtilities()) {
			return "Yes";
		} else {
			return "N/A";
		}
	}

}
