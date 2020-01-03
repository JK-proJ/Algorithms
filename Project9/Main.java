import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class listNode {
	int data;
	listNode next;

	listNode() {
		data = 0;
		next = null;
	}

	listNode(int d) {
		data = d;
		next = null;
	}
}

class LLStack {
	listNode top;

	LLStack() {
		top = null;
	}

	void push(listNode newNode) {
		newNode.next = top;
		top = newNode;
	}

	listNode pop() {
		if (top == null) {
			return null;
		} else {
			listNode temp = top;
			top = top.next;
			temp.next = null;
			return temp;
		}
	}

	boolean isEmpty() {
		return (top == null);
	}

	void printStack(listNode top, FileWriter output) {
		listNode node = top;
		try {
			output.write("Top-->");
			while (node.next != null) {
				output.write("(" + node.data + ", " + node.next.data + ")-->");
				node = node.next;
			}
			output.write("(" + node.data + ", NULL)-->NULL\n\n");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class LLQ {
	listNode head;
	listNode tail;
	listNode dummy;

	LLQ() {
		dummy = new listNode(0);
		head = dummy;
		tail = dummy;
	}

	void addTail(LLQ q, listNode node) {
		node.next = null;
		if (q.isEmpty(q)) {
			q.head = node;
			q.tail = node;
		} else {
			q.tail.next = node;
			q.tail = node;
		}
	}

	listNode deleteHead(LLQ q) {
		listNode temp = null;
		if (!q.isEmpty(q)) {
			temp = q.head;
			q.head = temp.next;
		}
		return temp;
	}

	boolean isEmpty(LLQ q) {
		return ((q.head == dummy && q.tail == dummy) || q.head == null);
	}

	String printQueue(int index) {
		String out = "";
		if (!this.isEmpty(this)) {
			listNode temp = this.head;
			out += "Front(" + index + ")-->";
			while (temp.next != null) {
				out += "(" + temp.data + ", " + temp.next.data + ")-->";
				temp = temp.next;
			}
			out += "(" + temp.data + ", NULL)-->NULL\n";
			out += "Tail(" + index + ")-->(" + temp.data + ", NULL)-->NULL\n";
		}
		return out;
	}
}

class RadixSort {
	LLStack stack;
	int tableSize, data, currentTable, previousTable, maxDigits, offSet, currentDigit, currentQueue;
	LLQ hashTable[][];

	RadixSort() {
		tableSize = 10;
		hashTable = new LLQ[2][tableSize];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < tableSize; j++) {
				hashTable[i][j] = new LLQ();
			}
		}
	}

	void firstReading(Scanner input) {
		int minNum = 0, maxNum = 0;

		while (input.hasNext()) {
			data = input.nextInt();
			if (data < minNum) {
				minNum = data;
			}
			if (data > maxNum) {
				maxNum = data;
			}
		}
		offSet = Math.abs(minNum);
		maxNum += offSet;
		maxDigits = getMaxDigits(maxNum);

	}

	void loadStack(Scanner input, FileWriter output) {
		stack = new LLStack();
		while (input.hasNext()) {
			data = input.nextInt();
			data += offSet;
			listNode newNode = new listNode(data);
			stack.push(newNode);
		}
		stack.printStack(stack.top, output);
	}

	void dumpStack(listNode top, int currentDigit, int currentTable, FileWriter output) {
		listNode node;
		int digit, hashIndex;
		while (!stack.isEmpty()) {
			node = stack.pop();
			digit = getDigit(node, currentDigit);
			hashIndex = digit;
			hashTable[currentTable][hashIndex].addTail(hashTable[currentTable][hashIndex], node);
		}
		printTable(hashTable[currentTable], output);
	}

	int getMaxDigits(int num) {
		int temp = num;
		int counter = 0;
		while (temp > 0) {
			temp /= 10;
			counter++;
		}
		return counter;
	}

	void RxSort(FileWriter output1, FileWriter output2) {
		listNode node;
		int digit, hashIndex, temp;
		currentDigit = 0;
		currentTable = 0;
		dumpStack(stack.top, currentDigit, currentTable, output2);
		currentDigit++;
		currentTable = 1;
		previousTable = 0;
		currentQueue = 0;

		while (currentDigit <= maxDigits) {
			while (currentQueue < tableSize) {
				while (!hashTable[previousTable][currentQueue].isEmpty(hashTable[previousTable][currentQueue])) {
					node = hashTable[previousTable][currentQueue].deleteHead(hashTable[previousTable][currentQueue]);
					digit = getDigit(node, currentDigit);
					hashIndex = digit;
					hashTable[currentTable][hashIndex].addTail(hashTable[currentTable][hashIndex], node);
				}
				currentQueue++;
			}
			printTable(hashTable[currentTable], output2);

			temp = currentTable;
			currentTable = previousTable;
			previousTable = temp;
			currentQueue = 0;
			currentDigit++;
		}
		subtractOffset();
		printSortResult(hashTable[currentTable], output1);
	}

	int getDigit(listNode node, int currentDigit) {
		int num = node.data;
		while (currentDigit > 0) {
			num /= 10;
			currentDigit--;
		}
		return num %= 10;
	}

	void printTable(LLQ[] table, FileWriter output) {
		try {
			int index = 0;
			String out = "";
			while (index < tableSize) {
				if (!(table[index].isEmpty(table[index]))) {
					out += table[index].printQueue(index);
				}
				index++;
			}
			out += "\n";
			output.write(out);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void printSortResult(LLQ[] hashTable, FileWriter output) {
		int index = 0;
		listNode spot = hashTable[index].head;
		try {
			output.write("Sorted Data:\n");
			while (index < tableSize) {
				while (spot != null) {
					if (spot != null) {
						data = spot.data;
						output.write(data + " ");
						spot = spot.next;
					}
				}
				index++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void subtractOffset() {
		currentTable = previousTable;
		listNode node = hashTable[currentTable][0].head;
		while (node != null) {
			node.data -= offSet;
			node = node.next;
		}
	}
}

public class Main {

	public static void main(String[] args) {

		try {
			Scanner input = new Scanner(new File(args[0]));
			FileWriter output1 = new FileWriter(new File(args[1]));
			FileWriter output2 = new FileWriter(new File(args[2]));

			RadixSort sorting = new RadixSort();
			sorting.firstReading(input);
			input.close();

			Scanner input1 = new Scanner(new File(args[0]));

			sorting.loadStack(input1, output2);

			sorting.RxSort(output1, output2);

			input1.close();
			output1.close();
			output2.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
