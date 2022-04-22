/**
 * A Connect-4 player that chooses the best move immediately available 
 * 
 * @author Alexis Pak
 *
 */
public class Minimax implements Player {

	private static java.util.Random rand = new java.util.Random();
	int id;
	int opponent_id;
	int cols;


	@Override
	public String name() {
		return "Minnie";
	}

	@Override
	public void init(int id, int msecPerMove, int rows, int cols) {
		this.id = id; //id is your player's id, opponent's id is 3-id
		opponent_id = 3 -id;
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
		int maxDepth = 1;

		//While there is time remaining and search depth is <= the number of moves remaining
		int bestCol = -1, bestScore = -1;
		while(!arb.isTimeUp() && maxDepth <= board.numEmptyCells()) {
			for(int col = 0; col < board.numCols(); col++) {
				System.out.println("col = " + col + ", maxDepth = " + maxDepth + ", bestCol = " + bestCol + ", bestScore = " + bestScore);
				if (board.isValidMove(col)) {
					board.move(col, id);
					int score = minimax(board, maxDepth, false, arb);
					if(score > bestScore) {
						bestCol = col;
						bestScore = score;
					}
					board.unmove(col, id);
				}
			}
			//run the first level of the minimax search and set move to be the best column corresponding to be best score
			//
			maxDepth++;
		}
		if (bestCol >= 0) {
			arb.setMove(bestCol);
		}
		else {
			throw new Error ("Complaint: Something is wrong!");
		}
	}

	public int minimax(Connect4Board board, int depth, boolean isMaximizing, Arbitrator arb) {
		//		if depth = 0 or no moves or time is up
		//				return the heuristic value of node

		if (depth == 0 || board.isFull() || arb.isTimeUp()) {
			return calcScore(board,id) - calcScore(board, opponent_id);
		}
		//	if isMaximizing then
		if (isMaximizing) {
			int bestScore = - 1000;
			for(int col = 0; col < board.numCols(); col++) {
				if (board.isValidMove(col)) {
					board.move(col, id);

					bestScore = Math.max(bestScore, minimax(board, depth -1, false, arb));
					board.unmove(col, id);

				} 
			}
			return bestScore;
		}

		else {
			int bestScore = 1000;
			for(int col = 0; col < board.numCols(); col++) {
				if (board.isValidMove(col)) {

					board.move(col, opponent_id);
					bestScore = Math.min(bestScore, minimax(board, depth -1, true, arb));
					board.unmove(col, opponent_id);
				}
			}
			return bestScore;
		}
	}

	// return the number of connect-4s that player #id has.
	public int calcWinner(Connect4Board board)
	{
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
