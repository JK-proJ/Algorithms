#include <iostream>
#include <fstream>
#include<string>
using namespace std;

int main(int argc, char** argv) {

	ifstream input1, input2;
	ofstream output;

	input1.open(argv[1]);
	input2.open(argv[2]);
	output.open(argv[3]);

	int num1, num2;
	input1 >> num1;
	input2 >> num2;
	while (!input1.eof() || !input2.eof()) {

		if (num1 <= num2) {
			output << to_string(num1) << endl;
			input1 >> num1;

		}
		else {
			output << to_string(num2) << endl;
			input2 >> num2;
		}

	}

	if (!input1.eof()) {
		while (!input1.eof()) {
			input1 >> num1;
			output << to_string(num1) << endl;

		}
	}

	if (!input2.eof()) {
		while (!input2.eof()) {

			output << to_string(num2) << endl;
			input2 >> num2;

		}
	}
	if (num1 < num2) {
		output << to_string(num1) << endl;
		output << to_string(num2) << endl;
	}
	else {
		output << to_string(num2) << endl;
		output << to_string(num1) << endl;
	}

	input1.close();
	input2.close();
	output.close();

	return 0;
}
