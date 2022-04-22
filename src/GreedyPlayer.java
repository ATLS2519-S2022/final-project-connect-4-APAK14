/**
 * A Connect-4 player that chooses the best move immediately available 
 * 
 * @author Alexis Pak
 *
 */
public class GreedyPlayer implements Player {

	private static java.util.Random rand = new java.util.Random();
	int id;
	int cols;


	@Override
	public String name() {
		return "Greedy Boi";
	}

	@Override
	public void init(int id, int msecPerMove, int rows, int cols) {
		this.id = id; //id is your player's id, opponent's id is 3-id
		this.cols = cols;
	}

	@Override
	public void calcMove(
			Connect4Board board, int oppMoveCol, Arbitrator arb) 
					throws TimeUpException {
		// Make sure there is room to make a move.
		if (board.isFull()) {
			throw new Error ("Complaint: The board is full!");
		}
		int startCol = rand.nextInt(board.numCols());
		int maxScore = -1, bestCol = -1;
		for(int col1 = 0; col1 < cols; col1++) {
			int col = (col1 + startCol) % board.numCols();
			if(board.isValidMove(col)) {
				board.move(col, id);
				int score = calcScore(board, id);
				if (score > maxScore) {
					bestCol = col;
					maxScore = score;
				}
				board.unmove(col, id);
			}
		}
		if (bestCol >= 0) {
			arb.setMove(bestCol);
		}
		else {
			throw new Error ("Complaint: Something is wrong!");
		}
	}

	// Return the id (>0) of the winner for normal connect-4 play.
	// Return 0 if there is no winner.
	// Return -1 if there is more than 1 winner.
	public int calcWinner(Connect4Board board) {

		final int rows = board.numRows();
		final int cols = board.numCols();
		int winner = 0;
		// Look for horizontal connect-4s.
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c <= cols - 4; c++) {
				int id = board.get(r, c);
				if (id == 0) continue;
				if (board.get(r, c + 1) != id) continue;
				if (board.get(r, c + 2) != id) continue;
				if (board.get(r, c + 3) != id) continue;
				if (winner != 0 && winner != id) return -1;
				winner = id;
			}
		}
		// Look for vertical connect-4s.
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				int id = board.get(r, c);
				if (id == 0) continue;
				if (board.get(r + 1, c) != id) continue;
				if (board.get(r + 2, c) != id) continue;
				if (board.get(r + 3, c) != id) continue;
				if (winner != 0 && winner != id) return -1;
				winner = id;
			}
		}
		// Look for diagonal connect-4s.
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				int id = board.get(r, c);
				if (id == 0) continue;
				if (board.get(r + 1, c + 1) != id) continue;
				if (board.get(r + 2, c + 2) != id) continue;
				if (board.get(r + 3, c + 3) != id) continue;
				if (winner != 0 && winner != id) return -1;
				winner = id;
			}
		}
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = rows - 1; r >= 4 - 1; r--) {
				int id = board.get(r, c);
				if (id == 0) continue;
				if (board.get(r - 1, c + 1) != id) continue;
				if (board.get(r - 2, c + 2) != id) continue;
				if (board.get(r - 3, c + 3) != id) continue;
				if (winner != 0 && winner != id) return -1;
				winner = id;
			}
		}
		return winner;
	}

	// Return the number of connect-4s that player #id has.
	public int calcScore(Connect4Board board, int id)
	{
		final int rows = board.numRows();
		final int cols = board.numCols();
		int score = 0;
		// Look for horizontal connect-4s.
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c <= cols - 4; c++) {
				if (board.get(r, c + 0) != id) continue;
				if (board.get(r, c + 1) != id) continue;
				if (board.get(r, c + 2) != id) continue;
				if (board.get(r, c + 3) != id) continue;
				score++;
			}
		}
		// Look for vertical connect-4s.
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c) != id) continue;
				if (board.get(r + 1, c) != id) continue;
				if (board.get(r + 2, c) != id) continue;
				if (board.get(r + 3, c) != id) continue;
				score++;
			}
		}
		// Look for diagonal connect-4s.
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c + 0) != id) continue;
				if (board.get(r + 1, c + 1) != id) continue;
				if (board.get(r + 2, c + 2) != id) continue;
				if (board.get(r + 3, c + 3) != id) continue;
				score++;
			}
		}
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = rows - 1; r >= 4 - 1; r--) {
				if (board.get(r - 0, c + 0) != id) continue;
				if (board.get(r - 1, c + 1) != id) continue;
				if (board.get(r - 2, c + 2) != id) continue;
				if (board.get(r - 3, c + 3) != id) continue;
				score++;
			}
		}
		return score;
	}

}
