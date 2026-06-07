package application;

import java.util.ArrayList;

public class MinHeap {
	private martyrNode[] martyrArr;
	private int heapSize;

	public MinHeap() {
		this.heapSize = 0;
		this.martyrArr = new martyrNode[1]; // Array index starts from 1
	}

	public void insertSortedAge(martyrNode martyr) {// insert martyr sorted by age(min heap)
		if (heapSize == martyrArr.length - 1) {
			resize();
		}
		heapSize++;
		int i = heapSize;
		martyrArr[i] = martyr;
		if (martyrArr[i] != null) {
			while (i > 1 && martyrArr[i].Age.compareTo(martyrArr[i / 2].Age) < 0) {
				martyrNode temp = martyrArr[i];
				martyrArr[i] = martyrArr[i / 2];
		
				martyrArr[i / 2] = temp;

				i = i / 2;
			}
		}
	}

	private void resize() {// resize array
		martyrNode[] newArr = new martyrNode[martyrArr.length * 2];
		for (int i = 1; i < martyrArr.length; i++) {
			newArr[i] = martyrArr[i];
		}
		martyrArr = newArr;
	}

	private void Heapify(martyrNode arr[], int N, int i) {// check min heap
		int left = 2 * i;
		int right = 2 * i + 1;
		int smallest = i;

		if (left < N && martyrArr[left] != null && martyrArr[left].Age.compareTo(martyrArr[i].Age) < 0) {
			smallest = left;
		}

		if (right < N && martyrArr[right] != null && martyrArr[right].Age.compareTo(martyrArr[smallest].Age) < 0) {
			smallest = right;
		}

		if (smallest != i) {
			// Swap heap[i] with heap[smallest]
			martyrNode temp = arr[i];
			arr[i] = arr[smallest];
			arr[smallest] = temp;

			Heapify(arr, N, smallest);
		}
	}

	private void buildHeap(martyrNode[] array, int N) {// build min heap
		int started = N / 2;
		for (int i = started; i >= 1; i--) {
			Heapify(array, N, i);
		}

	}

	
	public martyrNode deleteMin() {
		if (heapSize == 0) {
			return null;
		}
		martyrNode min = martyrArr[1];
		martyrArr[1] = martyrArr[heapSize];
		heapSize--;
		Heapify(martyrArr, heapSize + 1, 1);
		return min;
	}

	public void heapSort() {// sorted
		int N = heapSize;
		
		
		buildHeap(martyrArr, N);
		for (int i = N; i > 0; i--) {
			martyrNode temp = martyrArr[1];
			martyrArr[1] = martyrArr[i];
			martyrArr[i] = temp;
			Heapify(martyrArr, i, 1);
		}
	}

	public void printSorted() {
		for (int i = 1; i <= heapSize; i++) {// print array
			System.out.println(martyrArr[i].toString());
		}
	}

	public void clearHeap() {
		heapSize = 0;
		martyrArr = new martyrNode[1]; // Reset the array
	}

	public ArrayList<martyrNode> getSortedMartyrsAsList() {
		ArrayList<martyrNode> sortedMartyrs = new ArrayList<>();
		for (int i = 1; i <= heapSize; i++) {
			if (martyrArr[i] != null) {
				sortedMartyrs.add(martyrArr[i]);
			}
		}
		return sortedMartyrs;
	}

}
