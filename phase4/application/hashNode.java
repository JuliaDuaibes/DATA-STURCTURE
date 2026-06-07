package application;

public class hashNode {
	String date;
    char flag; // 'E' for empty, 'F' for full, 'D' for deleted
    MartyrsAVLTree martyrsTree;

    public hashNode() {
        this.flag = 'E';
        this.martyrsTree = new MartyrsAVLTree();
    }

    public hashNode(String date) {
        this.date = date;
        this.flag = 'F';
        this.martyrsTree = new MartyrsAVLTree();
    }

}
