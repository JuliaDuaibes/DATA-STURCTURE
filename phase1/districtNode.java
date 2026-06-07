package application;

public class districtNode {
	String districtName;
	locationList AllLocations;// all locations in this.district node
	districtNode next, prev;

	public districtNode(String districtName) {// cons
		this.districtName = districtName;
		this.AllLocations = new locationList();
		this.next = null;
	}

	// setters and getters
	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public locationList getAllLocations() {
		return AllLocations;
	}

	public void setAllLocations(locationList allLocations) {
		AllLocations = allLocations;
	}

	public districtNode getNext() {
		return next;
	}

	public void setNext(districtNode next) {
		this.next = next;
	}

	public districtNode getPrev() {
		return prev;
	}

	public void setPrev(districtNode prev) {
		this.prev = prev;
	}

	public void updateDistrictName(String oldDistrictName, String newDistrictName) {// update district node by name
		if (this.getDistrictName().equalsIgnoreCase(oldDistrictName)) {
			this.setDistrictName(newDistrictName);
		}
	}
}
