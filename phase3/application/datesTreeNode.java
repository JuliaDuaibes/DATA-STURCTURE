package application;

public class datesTreeNode {
	String date;
	datesTreeNode left, right;
	martyrList allmartyrIndate;//all martyr in current date
	

	public datesTreeNode(String date) {
		this.date = date;
		this.allmartyrIndate = new martyrList();
		this.left = this.right = null;

	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public datesTreeNode getLeft() {
		return left;
	}

	public void setLeft(datesTreeNode left) {
		this.left = left;
	}

	public datesTreeNode getRight() {
		return right;
	}

	public void setRight(datesTreeNode right) {
		this.right = right;
	}

	public martyrList getAllmartyrIndate() {
		return allmartyrIndate;
	}

	public void setAllmartyrIndate(martyrList allmartyrIndate) {
		this.allmartyrIndate = allmartyrIndate;
	}

}
