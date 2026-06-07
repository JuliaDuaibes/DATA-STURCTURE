package application;

public class locationNode {
	String locationName;
	locationNode left, right;
	datesTree dates;// all dates in location

	public locationNode(String locationName) {
		this.locationName = locationName;
		this.dates = new datesTree();
		left = right = null;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public locationNode getLeft() {
		return left;
	}

	public void setLeft(locationNode left) {
		this.left = left;
	}

	public locationNode getRight() {
		return right;
	}

	public void setRight(locationNode right) {
		this.right = right;
	}

	public datesTree getMartyrs() {
		return dates;
	}

	public void setMartyrs(datesTree martyrs) {
		this.dates = martyrs;
	}
}
