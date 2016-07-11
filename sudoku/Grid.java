import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Grid {
	
	private static final int GRID_SIZE = 9;

	private List<List<Integer>> _grid;
	private List<List<Integer>> _solution;
	private List<List<Integer>> _answers;

	Grid(int filledCells) {
		_grid = new ArrayList<>();
		_answers = new ArrayList<>();
		for (int i = 0; i < GRID_SIZE; i++) {
			_grid.add(new ArrayList<Integer>());
			_answers.add(new ArrayList<Integer>());
			for (int j = 0; j < GRID_SIZE; j++) {
				_grid.get(i).add(null);
				_answers.get(i).add(null);
			}
		}
		_solution = getSolution();
		fillGridCells(filledCells, _solution);
	}

	public boolean isSolved() {
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				Integer num;
				if (_grid.get(i).get(j) == null) {
					num = _answers.get(i).get(j);
				} else {
					num = _grid.get(i).get(j);
				}
				if (num != _solution.get(i).get(j)) {
					return false;
				}
			}
		}
		return true;
	}

	public void setAnswer(int row, int col, int num) {
		if (_grid.get(row).get(col) != null) {
			return;
		}
		if (num == 0) {
			_answers.get(row).set(col, null);
		} else {
			_answers.get(row).set(col, num);
		}
	}

	private void fillGridCells(int filledCells, List<List<Integer>> completeGrid) {
		while (true) {
			for (int i = 0; i < filledCells; i++) {
				Integer row = (int) (Math.random() * GRID_SIZE); 
				Integer col = (int) (Math.random() * GRID_SIZE); 
				while (_grid.get(row).get(col) != null) {
					row = (int) (Math.random() * GRID_SIZE); 
					col = (int) (Math.random() * GRID_SIZE); 
				}
				_grid.get(row).set(col, completeGrid.get(row).get(col));
			}
			if (gridSolutionIsUnique()) {
				break;
			} else {
				_grid = new ArrayList<>();
				for (int i = 0; i < GRID_SIZE; i++) {
					_grid.add(new ArrayList<Integer>());
					for (int j = 0; j < GRID_SIZE; j++) {
						_grid.get(i).add(null);
					}
				}
			}
		}
	}

	private boolean gridSolutionIsUnique() {
		List<List<Integer>> toReturn = new ArrayList<>();
		for (int i = 0; i < GRID_SIZE; i++) {
			toReturn.add(new ArrayList<Integer>());
			for (int j = 0; j < GRID_SIZE; j++) {
				toReturn.get(i).add(null);
			}
		}
		List<Pair<Integer, Integer>> emptyStack = new ArrayList<>();
		Map<Pair<Integer, Integer>, List<Integer>> possibleEntries = new HashMap<>();

		Pair<Integer, Integer> currentEmpty = null;
		int solutionCount = 0;
		while (true) {
			currentEmpty = nextEmpty(currentEmpty);
			if (currentEmpty == null) { //We've filled all possible empties
				solutionCount++;
				if (solutionCount > 1) {
					return false;
				}
				currentEmpty = emptyStack.get(emptyStack.size() - 1);
			} else {
				emptyStack.add(currentEmpty);
				possibleEntries.put(currentEmpty, randomOneToNine());
				possibleEntries.get(currentEmpty).removeAll(intsInBlock(currentEmpty, toReturn));
				possibleEntries.get(currentEmpty).removeAll(intsInRow(currentEmpty, toReturn));
				possibleEntries.get(currentEmpty).removeAll(intsInCol(currentEmpty, toReturn));
			}

			while (possibleEntries.get(currentEmpty).isEmpty()) {
				possibleEntries.remove(currentEmpty);
				emptyStack.remove(emptyStack.size() - 1);
				if (emptyStack.isEmpty()) { //no more solutions possible
					return true;
				}
				toReturn.get(currentEmpty.getLeft()).set(currentEmpty.getRight(), null);
				currentEmpty = emptyStack.get(emptyStack.size() - 1);
			}
			
			int currRow = currentEmpty.getLeft();
			int currCol = currentEmpty.getRight();
			List<Integer> currPossibleEntries = possibleEntries.get(currentEmpty);
			toReturn.get(currRow).set(currCol, currPossibleEntries.remove(0));
		}
	}

	public List<List<Integer>> getSolution() {
		List<List<Integer>> toReturn = new ArrayList<>();
		for (int i = 0; i < GRID_SIZE; i++) {
			toReturn.add(new ArrayList<Integer>());
			for (int j = 0; j < GRID_SIZE; j++) {
				toReturn.get(i).add(null);
			}
		}
		List<Pair<Integer, Integer>> emptyStack = new ArrayList<>();
		Map<Pair<Integer, Integer>, List<Integer>> possibleEntries = new HashMap<>();

		Pair<Integer, Integer> currentEmpty = null;

		while (true) {
			currentEmpty = nextEmpty(currentEmpty);
			if (currentEmpty == null) { //We've filled all possible empties
				return toReturn;
			}

			emptyStack.add(currentEmpty);
			possibleEntries.put(currentEmpty, randomOneToNine());
			possibleEntries.get(currentEmpty).removeAll(intsInBlock(currentEmpty, toReturn));
			possibleEntries.get(currentEmpty).removeAll(intsInRow(currentEmpty, toReturn));
			possibleEntries.get(currentEmpty).removeAll(intsInCol(currentEmpty, toReturn));

			while (possibleEntries.get(currentEmpty).isEmpty()) {
				possibleEntries.remove(currentEmpty);
				emptyStack.remove(emptyStack.size() - 1);
				if (emptyStack.isEmpty()) { //no more solutions possible
					return null;
				}
				toReturn.get(currentEmpty.getLeft()).set(currentEmpty.getRight(), null);
				currentEmpty = emptyStack.get(emptyStack.size() - 1);
			}
			
			int currRow = currentEmpty.getLeft();
			int currCol = currentEmpty.getRight();
			List<Integer> currPossibleEntries = possibleEntries.get(currentEmpty);
			toReturn.get(currRow).set(currCol, currPossibleEntries.remove(0));
		}
		//return null;
	}

	private List<Integer> intsInRow(Pair<Integer, Integer> currentEmpty, List<List<Integer>> currSolution) {
		List<Integer> toReturn = new ArrayList<Integer>();
		Integer row = currentEmpty.getLeft();
		for (int i = 0; i < GRID_SIZE; i++) {
			Integer entry = _grid.get(row).get(i); 
			Integer currSolEntry = currSolution.get(row).get(i);
			if (entry != null) {
				toReturn.add(entry);
			}
			if (currSolEntry != null) {
				toReturn.add(currSolEntry);
			}
		}
		return toReturn;
	}

	private List<Integer> intsInCol(Pair<Integer, Integer> currentEmpty, List<List<Integer>> currSolution) {
		List<Integer> toReturn = new ArrayList<Integer>();
		Integer col = currentEmpty.getRight();
		for (int i = 0; i < GRID_SIZE; i++) {
			Integer entry = _grid.get(i).get(col); 
			Integer currSolEntry = currSolution.get(i).get(col);
			if (entry != null) {
				toReturn.add(entry);
			}
			if (currSolEntry != null) {
				toReturn.add(currSolEntry);
			}
		}
		return toReturn;
	}

	//Given an empty coordinate, returns all non-empty entries in the coordinate's block
	private List<Integer> intsInBlock(Pair<Integer, Integer> currentEmpty, List<List<Integer>> currSolution) {
		int blockX = currentEmpty.getLeft() / 3;
		int blockY = currentEmpty.getRight() / 3;
		List<Integer> toReturn = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Integer entry = _grid.get(blockX * 3 + i).get(blockY * 3 + j); 
				Integer currSolEntry = currSolution.get(blockX * 3 + i).get(blockY * 3 + j);
				if (entry != null) {
					toReturn.add(entry);
				}
				if (currSolEntry != null) {
					toReturn.add(currSolEntry);
				}
			}
		}
		return toReturn;
	}

	private Pair<Integer, Integer> firstEmpty() {
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				if (_grid.get(i).get(j) == null) {
					return new Pair<Integer, Integer>(i, j);
				}
			}
		}
		return null;
	}

	private Pair<Integer, Integer> nextEmpty(Pair<Integer, Integer> currentEmpty) {
		if (currentEmpty == null) {
			return firstEmpty();
		}
		int currRow = currentEmpty.getLeft();
		int currCol = currentEmpty.getRight();
		if (currCol == GRID_SIZE - 1) {
			currCol = 0;
			currRow++;
		} else {
			currCol++;
		}
		if (currRow > GRID_SIZE - 1) {
			return null;
		}
		for (int i = currCol; i < GRID_SIZE; i++) {
			if (_grid.get(currRow).get(i) == null) {
				return new Pair<Integer, Integer>(currRow, i);
			}
		}
		for (int i = currRow + 1; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				if (_grid.get(i).get(j) == null) {
					return new Pair<Integer, Integer>(i, j);
				}
			}
		}
		return null;
	}

	private static List<Integer> randomOneToNine() {
		List<Integer> toReturn = new ArrayList<>();
		while (toReturn.size() < GRID_SIZE) {
			int toAdd = (int) (Math.random() * GRID_SIZE + 1);
			if (toReturn.contains(toAdd)) {
				continue;
			} else {
				toReturn.add(toAdd);
			}
		}
		return toReturn;
	}

	public void printSolution() {
		printGrid(_solution);
	}

	public static void printGrid(List<List<Integer>> toPrint) {
		if (toPrint == null) {
			System.out.println("Grid is null!");
			return;
		}
		String toReturn = "";
		String emptyRow = "";
		String dashesRow = "";
		for (int i = 0; i < GRID_SIZE; i++) {
			if (i % 3 == 0) {
				dashesRow += "----";
				emptyRow += "|   ";
			}
			emptyRow += "    ";
			dashesRow += "----";
		}
		emptyRow += "\n";
		dashesRow += "\n";
		for (int i = 0; i < toPrint.size(); i++) {
			if (i % 3 == 0) {
				toReturn += "\n" + dashesRow;
			} else {
				toReturn += "\n" + emptyRow;
			}
			List<Integer> toPrintList = toPrint.get(i);
			for (int j = 0; j < toPrintList.size(); j++) {
				Integer num = toPrintList.get(j);
				if (j % 3 == 0) {
					toReturn += "|   ";
				}
				if (num == null) {
					toReturn += "    ";
				} else {
					toReturn += num + "   ";
				}
			}
		}
		System.out.println(toReturn);
	}
	
	public void printPlayerGrid() {
		String toReturn = "";
		String emptyRow = "";
		String dashesRow = "";
		for (int i = 0; i < GRID_SIZE; i++) {
			if (i % 3 == 0) {
				dashesRow += "----";
				emptyRow += "|   ";
			}
			emptyRow += "    ";
			dashesRow += "----";
		}
		emptyRow += "\n";
		dashesRow += "\n";
		for (int i = 0; i < _grid.size(); i++) {
			if (i % 3 == 0) {
				toReturn += "\n" + dashesRow;
			} else {
				toReturn += "\n" + emptyRow;
			}
			List<Integer> intList = _grid.get(i);
			List<Integer> answerList = _answers.get(i);
			for (int j = 0; j < intList.size(); j++) {
				Integer num = intList.get(j);
				Integer answer = answerList.get(j); 
				if (j % 3 == 0) {
					toReturn += "|   ";
				}
				if (num == null) {
					if (answer != null) {
						toReturn += answer + "*  ";
					} else {
						toReturn += "    ";
					}
				} else {
					toReturn += num + "   ";
				}
			}
		}
		System.out.println(toReturn);
	}

}
