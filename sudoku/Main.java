import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input;
		System.out.print("How many givens would you like in your sudoku puzzle (28-80 inclusive): ");
		Integer givens;
		while (true) {
			try {
				input = br.readLine();
				givens = Integer.parseInt(input);
				if (givens < 28 || givens > 80) {
					System.out.print("Please enter an integer between 28 and 80 inclusive: ");
					continue;
				}
				break;
			} catch (Exception e) {
				System.out.print("Please enter an integer between 28 and 80 inclusive: ");
				continue;
			}
		}
		System.out.println("Loading puzzle...");
		Grid myGrid = new Grid(givens);
		while (true) {
			myGrid.printPlayerGrid();
			System.out.print("Enter command (\"ROW,COL,NUM\" [enter 0 for NUM to delete], \"submit\", \"quit\", \"forfeit\"): ");
			try {
				input = br.readLine();
				if (input == null) {
					return;
				}
				if (input.equals("quit")) {
					return;
				}
				if (input.equals("submit")) {
					if (myGrid.isSolved()) {
						System.out.println("Congrats! Puzzle solved.");
						return;
					} else {
						System.out.println("Solution is incorrect.");
						continue;
					}
				}
				if (input.equals("forfeit")) {
					System.out.println("Game over. Correct solution:");
					myGrid.printSolution();
					return;
				}
				String[] nums = input.split(",");
				Integer row, col, num;
				try {
					row = Integer.parseInt(nums[0]) - 1;
					col = Integer.parseInt(nums[1]) - 1;
					num = Integer.parseInt(nums[2]);
				} catch (Exception e) {
					System.out.println("Invalid command.");
					continue;
				}
				myGrid.setAnswer(row, col, num);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
