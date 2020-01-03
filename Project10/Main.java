import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Scheduling {
	class Node {
		int jobId;
		int jobTime;
		int dependentCount;
		Node next;

		Node() {
		}

		Node(int id, int time, int dependent) {
			jobId = id;
			jobTime = time;
			dependentCount = dependent;
			next = null;
		}
	}

	class Job {
		int jobTime;
		int onWhichProc;
		int onOpen;
		int parentCount;
		int dependentCount;

		Job() {
			jobTime = 0;
			onWhichProc = 0;
			onOpen = 0;
			parentCount = 0;
			dependentCount = 0;
		}
	}

	class Proc {
		int doWhichJob;
		int timeRemain;

		Proc() {
			doWhichJob = -1;
			timeRemain = 0;
		}
	}

	int numNodes;
	int numProcs;
	int procUsed;
	Job[] jobAry;
	Proc[] procAry;
	Node open;
	int[][] adjMatrix;
	int[] parentCountAry;
	int[] dependentCountAry;
	int[] onGraphAry;
	int totalJobTimes;
	int[][] scheduleTable;
	int currentTime;

	Scheduling() {
		currentTime = 0;
		procUsed = 0;
		Node dummy = new Node(0, 0, 0);
		open = dummy;
	}

	void initialization(Scanner input1, Scanner input2, String procsNum) {
		numProcs = Integer.parseInt(procsNum);
		if (input1.hasNext()) {
			numNodes = input1.nextInt();
		}
		if (numProcs <= 0) {
			System.out.println("Need 1 or more processors");
			System.exit(1);
		} else if (numProcs > numNodes) {
			numProcs = numNodes;
		}

		parentCountAry = new int[numNodes + 1];
		dependentCountAry = new int[numNodes + 1];
		onGraphAry = new int[numNodes + 1];
		adjMatrix = new int[numNodes + 1][numNodes + 1];
		jobAry = new Job[numNodes + 1];
		procAry = new Proc[numProcs + 1];

		for (int i = 1; i < numNodes + 1; i++) {
			Job job = new Job();
			jobAry[i] = job;
			parentCountAry[i] = 0;
			dependentCountAry[i] = 0;
			onGraphAry[i] = 1;

			for (int j = 0; j < numNodes + 1; j++) {
				adjMatrix[i][j] = 0;
			}
		}

		for (int i = 1; i < numProcs + 1; i++) {
			Proc proc = new Proc();
			procAry[i] = proc;
		}

		loadMatrix(input1);
		computeParentCount(adjMatrix, parentCountAry);
		computeDependentCount(adjMatrix, dependentCountAry);

		totalJobTimes = constructJobAry(input2, adjMatrix);
		scheduleTable = new int[numProcs + 1][totalJobTimes + 1];

		for (int i = 0; i < numProcs + 1; i++) {
			for (int j = 0; j < totalJobTimes + 1; j++) {
				scheduleTable[i][j] = 0;
			}
		}
	}

	void loadMatrix(Scanner input) {
		int i, j;
		while (input.hasNext()) {
			i = input.nextInt();
			j = input.nextInt();
			adjMatrix[i][j] = 1;
		}
	}

	int constructJobAry(Scanner input, int[][] adjMatrix) {
		int totalTime = 0;
		int nodeId, jobTime;
		int skip = input.nextInt();
		while (input.hasNext()) {
			nodeId = input.nextInt();
			jobTime = input.nextInt();
			totalTime += jobTime;
			System.out.println("NodeId: " + nodeId + " jobTime: " + jobTime);
			jobAry[nodeId].jobTime = jobTime;
			jobAry[nodeId].onWhichProc = -1;
			jobAry[nodeId].onOpen = 0;
			jobAry[nodeId].parentCount = parentCountAry[nodeId];
			jobAry[nodeId].dependentCount = dependentCountAry[nodeId];
		}
		return totalTime;
	}

	void computeParentCount(int[][] adjMatrix, int[] parentCountAry) {
		for (int nodeId = 1; nodeId < numNodes + 1; nodeId++) {
			int sum = 0;
			for (int i = 1; i < numNodes + 1; i++) {
				sum += adjMatrix[i][nodeId];
			}
			parentCountAry[nodeId] = sum;
		}
	}

	void computeDependentCount(int[][] adjMatrix, int[] dependentCountAry) {
		for (int nodeId = 1; nodeId < numNodes + 1; nodeId++) {
			int sum = 0;
			for (int i = 1; i < numNodes + 1; i++) {
				sum += adjMatrix[nodeId][i];
			}
			dependentCountAry[nodeId] = sum;
		}
	}

	int findOrphan() {
		for (int i = 1; i < numNodes + 1; i++) {
			if (jobAry[i].parentCount <= 0 && jobAry[i].onOpen == 0 && jobAry[i].onWhichProc <= 0) {
				return i;
			}
		}
		return -1;
	}

	Node findSpot(Node newNode) {
		Node spot = open;
		while (spot.next != null && dependentCountAry[spot.next.jobId] >= dependentCountAry[newNode.jobId]) {
			spot = spot.next;
		}
		return spot;
	}

	void openInsert(Node newNode) {
		Node spot = findSpot(newNode);
		newNode.next = spot.next;
		spot.next = newNode;
	}

	void loadOpen() {
		int orphanNode = findOrphan();
		System.out.println(orphanNode + " Orphan");
		while (orphanNode != -1) {
			if (orphanNode > 0) {
				int jobId = orphanNode;
				int jobTime = jobAry[jobId].jobTime;
				Node newNode = new Node(jobId, jobTime, dependentCountAry[jobId]);
				openInsert(newNode);
				jobAry[jobId].onOpen = 1;
			}
			 orphanNode = findOrphan();
		}
	}

	void loadProcAry(int currentTime) {
		int availProc = findProcessor();
		System.out.println(availProc + "Available P");
		while (availProc > 0 && open.next != null && procUsed < numProcs) {
			if (availProc > 0) {
				procUsed++;
				Node newJob = open.next;
				open.next = open.next.next;
				newJob.next = null;
				int jobId = newJob.jobId;
				int jobTime = newJob.jobTime;
				procAry[availProc].doWhichJob = jobId;
				procAry[availProc].timeRemain = jobTime;
				putJobOnTable(availProc, currentTime, jobId, jobTime);
			}
			 availProc = findProcessor();
		}
	}

	void printList(FileWriter output) {
		String str;
		String out = "OPEN-->";
		for (Node i = open; i != null; i = i.next) {
			if (i.next == null) {
				str = "NULL)-->";
			} else {
				str = "\"" + i.next.jobId + "\")-->";
			}
			out += "(\"" + i.jobId + "\", " + i.jobTime + ", " + str;
		}
		out += "NULL";
		try {
			output.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printScheduleTable(FileWriter output) {
		try {
			output.write("     -0--");
			for (int i = 1; i < totalJobTimes + 1; i++) {
				output.write("-" + i + "--");
			}
			output.write("\n");
			for (int i = 1; i < numProcs + 1; i++) {
				output.write("P(" + i + ")|");
				for (int j = 0; j < totalJobTimes + 1; j++) {
					if (scheduleTable[i][j] == 0) {
						output.write(" - |");
					} else {
						output.write(" " + scheduleTable[i][j] + " |");
					}
				}
				output.write(" \n");
				output.write("     -");
				for (int k = 0; k <= totalJobTimes * 4; k++) {
					output.write("-");
				}
				output.write("\n");
			}
			output.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void putJobOnTable(int availProc, int currentTime, int jobId, int jobTime) {
		int time = currentTime;
		int endTime = time + jobTime;
		while (time < endTime) {
			scheduleTable[availProc][time] = jobId;
			time++;
		}
	}

	int findProcessor() {
		for (int i = 1; i < numProcs + 1; i++) {
			if (procAry[i].timeRemain <= 0) {
				return i;
			}
		}
		return -1;
	}

	boolean checkCycle() {
		if (open.next == null && !graphIsEmpty() && jobsFinished()) {
			return true;
		} else {
			return false;
		}
	}

	boolean graphIsEmpty() {
		for (int i = 1; i < numNodes + 1; i++) {
			if (onGraphAry[i] > 0) {
				return false;
			}
		}
		return true;
	}

	boolean jobsFinished() {
		for (int i = 1; i < numProcs + 1; i++) {
			if (procAry[i].timeRemain > 0) {
				return false;
			}
		}
		return true;
	}

	void updateProcTime() {
		for (int i = 1; i < numProcs + 1; i++) {
			if (procAry[i].timeRemain > 0) {
				procAry[i].timeRemain--;
			}
		}
	}

	int findDoneProc() {
		for (int i = 1; i < numProcs + 1; i++) {
			if (procAry[i].timeRemain <= 0) {
				int jobId = procAry[i].doWhichJob;
				procAry[i].doWhichJob = -1;
				return jobId;
			}
		}
		return -1;
	}

	void deleteEdge(int jobId) {
		for (int dependent = 1; dependent < numNodes + 1; dependent++) {
			if (adjMatrix[jobId][dependent] > 0) {
				parentCountAry[dependent]--;
			}
		}
	}
}

public class Main {

	public static void main(String[] args) {
		int procUsed = 0, currentTime;
		boolean hasCycle;

		String in1 = args[0];
		String in2 = args[1];
		String numOfProcs = args[2];
		String out1 = args[3]; 
		String out2 = args[4]; 

		try {
			Scanner input1 = new Scanner(new File(in1));
			Scanner input2 = new Scanner(new File(in2));
			FileWriter output1 = new FileWriter(new File(out1));
			FileWriter output2 = new FileWriter(new File(out2));
			Scheduling schedule = new Scheduling();
			schedule.initialization(input1, input2, numOfProcs);
			schedule.procUsed = 0;
			schedule.currentTime = 0;
			currentTime = 0;
			while (!schedule.graphIsEmpty()) {
				schedule.loadOpen();
				schedule.printList(output2);
				schedule.loadProcAry(schedule.currentTime);
				hasCycle = schedule.checkCycle();
				if (hasCycle) {
					System.out.println("There is a cycle in the graph!");
					System.exit(1);
				}
				schedule.printScheduleTable(output1);
				schedule.currentTime++;
				schedule.updateProcTime();
				int jobId = schedule.findDoneProc();
				while (jobId > 0) {
					if (jobId > 0) {
						schedule.onGraphAry[jobId] = 0;
						schedule.deleteEdge(jobId);
						jobId = schedule.findDoneProc();
					}
					jobId = schedule.findDoneProc();
				}
			}
			schedule.printScheduleTable(output1);

			input1.close();
			input2.close();
			output1.close();
			output2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
