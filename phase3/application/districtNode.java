package application;

public class districtNode {
	String districtName;
	districtNode left, right;
	locationTree locations;
	

	public districtNode(String districtName) {
		this.districtName = districtName;
		locations = new locationTree();
		left = right = null;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public districtNode getLeft() {
		return left;
	}

	public void setLeft(districtNode left) {
		this.left = left;
	}

	public districtNode getRight() {
		return right;
	}

	public void setRight(districtNode right) {
		this.right = right;
	}

	public locationTree getLocations() {
		return locations;
	}

	public void setLocations(locationTree locations) {
		this.locations = locations;
	}

}
