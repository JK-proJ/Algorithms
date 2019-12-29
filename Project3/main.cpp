#include<iostream>
#include<fstream>
#include<string>
using namespace std;

struct treeNode {
	string chStr;
	int prob;
	string code;
	treeNode* left;
	treeNode* right;
	treeNode* next;

	treeNode() {
		chStr = "dummy";
		prob = 0;
		code = "";
		left = NULL;
		right = NULL;
		next = NULL;
	}

	treeNode(string ch, int p) {
		chStr = ch;
		prob = p;
		code = "";
		left = NULL;
		right = NULL;
		next = NULL;
	}
	static string printNode(treeNode* t) {
		// next left right
		string str = "(\"" + t->chStr + "\", " + to_string(t->prob) + ", ";

		if (t->next == NULL) {
			str += "NULL, ";
		}
		else {
			str += "\"" + t->next->chStr + "\", ";
		}
		if (t->left == NULL) {
			str += "NULL, ";
		}
		else {
			str += "\"" + t->left->chStr + "\", ";
		}
		if (t->right == NULL) {
			str += "NULL)";
		}
		else {
			str += "\"" + t->right->chStr + "\")";
		}
		return str;
	}
};

struct linkedList {
	treeNode* listHead;

	linkedList() {
		listHead = new treeNode();
	}

	static void insertNewNode(treeNode* listHead, treeNode* newNode) {
		treeNode* spot = findSpot(listHead, newNode);
		insert(spot, newNode);
	}

	static treeNode* findSpot(treeNode* listHead, treeNode* newNode) {
		treeNode* spot = listHead;
		while (spot->next != NULL && spot->next->prob < newNode->prob) {
			spot = spot->next;
		}
		return spot;
	}

	static void insert(treeNode* spot, treeNode* newNode) {
		newNode->next = spot->next;
		spot->next = newNode;
	}

	static void printList(treeNode* listHead, ofstream& outFile) {
		string str;
		string output = "listHead-->";
		for (treeNode* i = listHead; i != NULL; i = i->next) {
			if (i->next == NULL) {
				str = "NULL)-->";
			}
			else {
				str = "\"" + i->next->chStr + "\")-->";
			}
			output += "(\"" + i->chStr + "\", " + to_string(i->prob) + ", " + str;
		}
		output += "NULL";
		outFile << output << endl;
	}
};

struct HuffmanBinTree {
	treeNode* root;

	HuffmanBinTree() {
		root = new treeNode();
	}

	void constructHuffmanLList(ifstream& input, ofstream& output) {
		string chStr;
		int prob;
		linkedList list = linkedList();

		while (!input.eof()) {
			input >> chStr >> prob;
			treeNode* newNode = new treeNode();
			newNode->chStr = chStr;
			newNode->prob = prob;
			list.insertNewNode(list.listHead, newNode);
			list.printList(list.listHead, output);
		}
		input.close();
	}

	void constructHuffmanBinTree(linkedList list, ofstream& output) {
		treeNode* spot = list.listHead->next;
		while (spot->next != NULL) {
			treeNode* newNode = new treeNode();
			newNode->chStr = spot->chStr + spot->next->chStr;
			newNode->prob = spot->prob + spot->next->prob;
			newNode->left = spot;
			newNode->right = spot->next;
			list.insertNewNode(spot, newNode);
			list.printList(list.listHead, output);
			spot = spot->next->next;
			root = newNode;
		}
	}

	static void preOrderTraversal(treeNode* t, ofstream& output) {
		if (isLeaf(t)) {
			output << treeNode::printNode(t) << endl;
		}
		else {
			output << treeNode::printNode(t) << endl;
			preOrderTraversal(t->left, output);
			preOrderTraversal(t->right, output);
		}
	}
	static void inOrderTraversal(treeNode* t, ofstream& output) {
		if (isLeaf(t)) {
			output << treeNode::printNode(t) << endl;
		}
		else {
			inOrderTraversal(t->left, output);
			output << treeNode::printNode(t) << endl;
			inOrderTraversal(t->right, output);
		}
	}

	static void postOrderTraversal(treeNode* t, ofstream& output) {
		if (isLeaf(t)) {
			output << treeNode::printNode(t) << endl;
		}
		else {
			postOrderTraversal(t->left, output);
			postOrderTraversal(t->right, output);
			output << treeNode::printNode(t) << endl;

		}
	}

	void constructCharCode(treeNode* node, string cd, ofstream& output) {
		if (isLeaf(node)) {
			node->code = cd;
			output << node->chStr << " " << node->code << endl;
		}
		else {
			constructCharCode(node->left, cd + "0", output);
			constructCharCode(node->right, cd + "1", output);

		}
	}

	static bool isLeaf(treeNode* node) {
		return (node->left == NULL && node->right == NULL);

	}

};

int main(int argv, char** argc) {

	ifstream input;
	ofstream output1, output2, output3;

	input.open(argc[1]); 
	output1.open(argc[2]); 
	output2.open(argc[3]); 
	output3.open(argc[4]); 

	string chStr;
	int prob;
	linkedList list = linkedList();
  
	while (!input.eof()) {
		input >> chStr >> prob;
		treeNode* newNode = new treeNode();
		newNode->chStr = chStr;
		newNode->prob = prob;
		list.insertNewNode(list.listHead, newNode);
	}
  
	input.close();
	input.open(argc[1]);
	HuffmanBinTree tr = HuffmanBinTree();
	output3 << "Construction of Huffman Linked List:\n";
	tr.constructHuffmanLList(input, output3);
	output3 << "\nConstruction of Huffman Binary Tree:\n";
	tr.constructHuffmanBinTree(list, output3);
	output1 << "Character Code Construction:\n";
	tr.constructCharCode(tr.root, "", output1);

	treeNode* root;
	root = tr.root;

	output2 << "Pre-Order Traversal:\n";
	HuffmanBinTree::preOrderTraversal(root, output2);
	output2 << "\nIn-Order Traversal:\n";
	HuffmanBinTree::inOrderTraversal(root, output2);
	output2 << "\nPost-Order Traversal:\n";
	HuffmanBinTree::postOrderTraversal(root, output2);
	output2 << endl;

	input.close();
	output1.close();
	output2.close();
	output3.close();

	return 0;
}
