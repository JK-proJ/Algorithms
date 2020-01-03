import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class treeNode {

	public String chStr;
	public char chCh;
	public int prob;
	public String code;
	public treeNode left;
	public treeNode right;
	public treeNode next;

	treeNode() {
		chStr = "dummy";
		prob = 0;
		code = "";
		left = null;
		right = null;
		next = null;
	}

	treeNode(String ch, int p) {
		chStr = ch;
		prob = p;
		code = "";
		left = null;
		right = null;
		next = null;
	}

	treeNode(char ch, int p, treeNode n) {

		chStr = String.valueOf(ch);
		chCh = ch;
		prob = p;
		code = "";
		left = null;
		right = null;
		next = n;
	}

	treeNode(char ch, int p) {
		chStr = String.valueOf(ch);
		chCh = ch;
		prob = p;
		code = "";
		left = null;
		right = null;
		next = null;
	}

	static String printNode(treeNode t) {

		String str = "(\"" + t.chStr + "\", " + t.prob + ", " + t.code + ", ";

		if (t.next == null) {
			str += "NULL, ";
		} else {
			str += "\"" + t.next.chStr + "\", ";
		}
		if (t.left == null) {
			str += "NULL, ";
		} else {
			str += "\"" + t.left.chStr + "\", ";
		}
		if (t.right == null) {
			str += "NULL)";
		} else {
			str += "\"" + t.right.chStr + "\")";
		}
		return str;
	}
}

class linkedList {

	public treeNode listHead;

	linkedList() {
		listHead = new treeNode();
	}

	void insertNewNode(treeNode listHead, treeNode newNode) {
		treeNode spot = findSpot(listHead, newNode);
		insert(spot, newNode);
	}

	treeNode findSpot(treeNode listHead, treeNode newNode) {
		treeNode spot = listHead;
		while (spot.next != null && spot.next.prob < newNode.prob) {
			spot = spot.next;
		}
		return spot;
	}

	void insert(treeNode spot, treeNode newNode) {
		newNode.next = spot.next;
		spot.next = newNode;
	}

	void printList(treeNode listHead, FileWriter outFile) {
		String str = "";
		String output = "listHead-->";
		for (treeNode i = listHead; i != null; i = i.next) {
			str = "-->";
			output += i.printNode(i) + str;
		}
		output += "NULL";
		try {
			outFile.write(output);
			outFile.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class HuffmanBinTree {
	treeNode root;
	static String[] charCode = new String[256];

	HuffmanBinTree() {
		root = new treeNode();
	}

	void constructHuffmanLList(int[] charCounts, FileWriter output) {

		char chr;
		int prob;
		linkedList list = new linkedList();
		try {

			output.write("Construction of Huffman Linked List:\n");
			for (int i = 0; i < 256; i++) {
				if (charCounts[i] > 0) {
					chr = (char) i;
					prob = charCounts[i];
					treeNode newNode = new treeNode(chr, prob);
					list.insertNewNode(list.listHead, newNode);
					list.printList(list.listHead, output);
					output.write("\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void constructHuffmanBinTree(linkedList list, FileWriter output) {
		try {
			output.write("\nConstruction of Huffman Binary Tree:\n");

			treeNode spot = list.listHead.next;
			while (spot.next != null) {

				treeNode newNode = new treeNode();
				newNode.chStr = spot.chStr + spot.next.chStr;
				newNode.prob = spot.prob + spot.next.prob;
				newNode.left = spot;
				newNode.right = spot.next;
				list.insertNewNode(spot, newNode);
				list.printList(list.listHead, output);
				output.write("\n");

				spot = spot.next.next;
				root = newNode;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void constructCharCode(treeNode node, String cd) {
		if (isLeaf(node)) {
			node.code = cd;
			int index = (int) node.chCh;
			if (node.code != null)
				charCode[index] = node.code;
		} else {
			constructCharCode(node.left, cd + "0");
			constructCharCode(node.right, cd + "1");
		}
	}

	static void preOrderTraversal(treeNode t, FileWriter output) {

		try {
			if (isLeaf(t)) {
				output.write(treeNode.printNode(t));
				output.write("\n");
			} else {
				output.write(treeNode.printNode(t));
				output.write("\n");
				preOrderTraversal(t.left, output);
				preOrderTraversal(t.right, output);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void inOrderTraversal(treeNode t, FileWriter output) {

		try {
			if (isLeaf(t)) {
				output.write(treeNode.printNode(t));
				output.write("\n");
			} else {
				inOrderTraversal(t.left, output);
				output.write(treeNode.printNode(t));
				output.write("\n");
				inOrderTraversal(t.right, output);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void postOrderTraversal(treeNode t, FileWriter output) {

		try {
			if (isLeaf(t)) {
				output.write(treeNode.printNode(t));
				output.write("\n");
			} else {
				postOrderTraversal(t.left, output);
				postOrderTraversal(t.right, output);
				output.write(treeNode.printNode(t));
				output.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static boolean isLeaf(treeNode node) {
		return (node.left == null && node.right == null);

	}

	void Encode(Scanner input, FileWriter output) {

		char charIn;
		int index;
		String code = null;
		try {
			input.useDelimiter("");
			while (input.hasNext()) {
				charIn = input.next().charAt(0);
				index = (int) charIn;
				code = charCode[index];
				output.write(code);
				output.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void Decode(Scanner input, FileWriter output, treeNode tRoot) {
		char oneBit;
		treeNode spot = tRoot;

		try {
			input.useDelimiter("");
			while (input.hasNext()) {
				if (isLeaf(spot)) {
					output.write(spot.chCh);
					spot = tRoot;
				} else {
					oneBit = input.next().charAt(0);
					if (oneBit == '0') {
						spot = spot.left;
					} else if (oneBit == '1') {
						spot = spot.right;
					} else {
						System.out.println("Error! The compress file contains invalid character!");
						System.exit(1);
					}
				}
			}
			if (!input.hasNext() && !isLeaf(spot)) {
				System.out.println("Error: The comress file is corrupted!");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

public class Main {

	static void userInterface(HuffmanBinTree tree) {
		String nameOrg, nameCompress, nameDeCompress;
		char yesNo = ' ';
		boolean quit = false;

		while (!quit) {
			System.out.print("Enter 'Y' to comress file, or 'N' to exit the program: ");
			Scanner user = new Scanner(System.in);
			if (user.hasNext())
				yesNo = Character.toUpperCase(user.next().charAt(0));

			if (yesNo == 'N') {
				System.out.println("Exiting the program");
				quit = true;
			}
			if (yesNo == 'Y') {
				try {
					System.out.print("Enter name of the file to be compressed without an extension: ");
					Scanner name = new Scanner(System.in);
					nameOrg = name.nextLine();
					nameCompress = nameOrg + "_Compressed";
					nameDeCompress = nameOrg + "_DeCompress";

					Scanner orgFile = new Scanner(new File(nameOrg + ".txt"));
					FileWriter compFile = new FileWriter(new File(nameCompress + ".txt"));
					FileWriter deCompFile = new FileWriter(new File(nameDeCompress + ".txt"));

					tree.Encode(orgFile, compFile);
					System.out.println("File is compressed");
					compFile.close();

					Scanner comprFile = new Scanner(new File(nameCompress + ".txt"));

					tree.Decode(comprFile, deCompFile, tree.root);
					orgFile.close();
					deCompFile.close();
				}

				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	static void computeCharCounts(Scanner input, int[] charCounts) {
		char charIn;
		int index;
		try {
			input.useDelimiter("");
			while (input.hasNext()) {
				charIn = input.next().charAt(0);
				index = (int) charIn;
				charCounts[index]++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void printCountAry(int[] charCounts, FileWriter out) {
		try {
			for (int i = 0; i < 256; i++) {
				if (charCounts[i] != 0) {
					out.write((char) i + " " + charCounts[i] + "\n");
				}
			}
			out.write("\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		int[] charCounts = new int[256];

		char charIn;
		int prob;
		String in = args[0]; // args
		String fileName2 = "_DeBug";
		String out = in + fileName2 + ".txt";

		try {
			Scanner input = new Scanner(new File(in));
			FileWriter output = new FileWriter(new File(out));

			computeCharCounts(input, charCounts);
			printCountAry(charCounts, output);

			linkedList list = new linkedList();
			HuffmanBinTree tr = new HuffmanBinTree();
			tr.constructHuffmanLList(charCounts, output);

			for (int i = 0; i < 256; i++) {
				if (charCounts[i] > 0) {
					charIn = (char) i;
					prob = charCounts[i];
					treeNode newNode = new treeNode(charIn, prob);
					list.insertNewNode(list.listHead, newNode);
				}
			}

			tr.constructHuffmanBinTree(list, output);
			treeNode root;
			root = tr.root;
			tr.constructCharCode(root, "");

			output.write("\nOutput of the entire linked list:\n");
			list.printList(list.listHead, output);

			output.write("\nTraversals:\n");
			output.write("Pre-Order Traversal:\n");
			HuffmanBinTree.preOrderTraversal(root, output);
			output.write("\nIn-Order Traversal:\n");
			HuffmanBinTree.inOrderTraversal(root, output);
			output.write("\nPost-Order Traversal:\n");
			HuffmanBinTree.postOrderTraversal(root, output);

			userInterface(tr);

			input.close();
			output.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
