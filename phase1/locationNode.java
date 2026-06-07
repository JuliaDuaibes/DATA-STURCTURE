package application;

public class locationNode {
	String locationName;
	locationNode next;
	martyrList Allmartyrs;// all martyr in location node
	MartyrNode martyrHead;

	public locationNode(String locationName) {// cons
		this.locationName = locationName;
		this.Allmartyrs = new martyrList();
		this.next = null;
		this.martyrHead = null;
	}

	// setters & getter
	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public locationNode getNext() {
		return next;
	}

	public void setNext(locationNode next) {
		this.next = next;
	}

	public martyrList getAllmartyrs() {
		return Allmartyrs;
	}

	public void setAllmartyrs(martyrList allmartyrs) {
		Allmartyrs = allmartyrs;
	}

	public MartyrNode getMartyrHead() {
		return martyrHead;
	}

	public void setMartyrHead(MartyrNode martyrHead) {
		this.martyrHead = martyrHead;
	}

	public void updateLocationName(String newLocationName) {// update location node by name
		this.locationName = newLocationName;

	}

}
