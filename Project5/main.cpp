#include<iostream>
#include <string>
#include<fstream>
using namespace std;

int main(int argc, char** argv) {
	
	ifstream input;
	ofstream output1,output2;

	int data, index, max;
	int* BucketArray;

	input.open(argv[1]); 
	output1.open(argv[2]);
	output2.open(argv[3]);

	input >> max;

	while (!input.eof()) {
		input >> data;
		if (max < data) {
			max = data;
		}
	}
	input.close();

	BucketArray = new int[max + 1];
  
  for(int i = 0; i <= max; i++){
    BucketArray[i] = 0;
  }  

	input.open(argv[1]);
	output2 << "Showing how array is changed:\n";

	while (!input.eof()) {

		input >> data;
		BucketArray[data]++;
		for (int i = 0; i < max + 1; i++) {
			output2 << "bucket #" << i << " " << BucketArray[i] << endl;
		}
		output2 << endl;

	}
	output2 << "\n\nThe final result is:\n";
	for (int i = 0; i < max + 1; i++) {
		if (BucketArray[i] > 0) {
			output2 << i << " " << BucketArray[i] << endl;
		}
	}

	index = 0;

	output1 << "Displaying sorted numbers:\n";
	while(index <= max){
		while(BucketArray[index] > 0) {
			output1 << index << " ";
			BucketArray[index]--;
		}
		index++;
	}
	
	input.close();
	output1.close();
	output2.close();

	return 0;
}
