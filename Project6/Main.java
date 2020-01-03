import java.io.*;
import java.util.Scanner;

public class Main {
	
	static void encodeMethod1(String in, String out) {
		
		int numRows = 0, numCols = 0, minVal = 0, maxVal = 0, numRuns = 0;
		int row = 0, col = 0, length = 0;
		int currVal = -1, nextVal = -1;
		int whichMethod = 1;		
		Scanner input;
		
		try {
			
			input = new Scanner(new FileReader(in));
			FileWriter output = new FileWriter(new File(out));
			
			if(input.hasNextInt()) {
				numRows = input.nextInt();
			}
			if(input.hasNextInt()) {
				numCols = input.nextInt();
			}
			if(input.hasNextInt()) {
				minVal = input.nextInt();
			}
			if(input.hasNextInt()) {
				maxVal = input.nextInt();
			}
			output.write(numRows + " " + numCols + " " + minVal + " " + maxVal + "\n" + whichMethod + "\n");
			
			while(row < numRows) {
				col = 0;
				length = 0;
				currVal = input.nextInt();				
				output.write(row + " " + col + " " + currVal + " ");
				length++;
				
				while(col < numCols-1) {
					col++;
					nextVal = input.nextInt();
					if(currVal == nextVal) {
						length++;
					}
					else {
						output.write(length + "\n");
						currVal = nextVal;
						numRuns++;
						length = 1;
						output.write(row + " " + col + " " + currVal + " ");
					}
				}
				output.write(length + "\n");
				numRuns++;
				row++;
			}
			input.close();
			output.close();
			
		}
		catch (IOException e) {
				e.printStackTrace();
		}
		
	}
	
	static void decodeMethod1(String in, String out) {
		
		int numRows = 0, numCols = 0, minVal = 0, maxVal = 0, numRuns = 0;
		int row = 0, col = 0, length = 0;
		int whichMethod = -1;
		Scanner input;
    
		try {
			input = new Scanner(new FileReader(in));
			FileWriter output = new FileWriter(new File(out));
			
			if(input.hasNextInt()) {
				numRows = input.nextInt();
			}
			if(input.hasNextInt()) {
				numCols = input.nextInt();
			}
			if(input.hasNextInt()) {
				minVal = input.nextInt();
			}
			if(input.hasNextInt()) {
				maxVal = input.nextInt();
			}
			if(input.hasNextInt()) {
				whichMethod = input.nextInt();
			}
			
			output.write(numRows + " " + numCols + " " + minVal + " " + maxVal + "\n");
			int[][] array = new int[numRows][numCols];
			
			for (int i = 0; i < numRows; i++) {
				for (int j = 0; j < numCols; j++) {
					array[i][j] = 0;
				}
			}
      
			row = col = length = 0;
			int greyScale;
			
			while(input.hasNext()) {
				row = input.nextInt();
				col = input.nextInt();
				greyScale = input.nextInt();
				length = input.nextInt();
				
				while(length > 0) {
					array[row][col] = greyScale;
					col++;
					length--;
				}
			}
			
			for (int i = 0; i < numRows; i++) {
				for (int j = 0; j < numCols; j++) {
					output.write(array[i][j] + " ");
				}
				output.write("\n");
			}			
			
			input.close();
			output.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		
		String in = args[0];
		int whichMethod = 1;
		String encodeFile = args[0]+"_EncodeMethod"+whichMethod+".txt";
		
		
		encodeMethod1(in,encodeFile);
		String decodeFile = encodeFile+"_Decoded.txt";
		decodeMethod1(encodeFile, decodeFile);
		
	}				
}
