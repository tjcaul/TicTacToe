import java.util.Random;
import java.util.ArrayList;

public class Ai
{    
    private char name;
    private double accuracy; //Unimplemented. Would make the AI make mistakes if <1.0
    static private Random rand = new Random(); //Static because randomness should generally be global. 
    
    public Ai (char name, double accuracy) {
        this.name = name;
        this.accuracy = accuracy;
    }
    
    public String play (Board board) {
        Move bestMove = bestMove(board, name, 0);
        int row = bestMove.row;
        int col = bestMove.col;
        
        //bestMove.makeStack(null); //The stack holds the AI's plan, created as the recursive function stack unwinds.
        //System.err.printf(bestMove.stack); //It is very useful for debugging to understand *why* the AI thinks its move is reasonable.

        return (col+1) + "," + (row+1);
    }
    
    public char getName () {
        return name;
    }
    
    public static Ai getAiByName (Ai[] ais, char name) {
        for (int i = 0; i < ais.length; ++i) {
            if (ais[i].getName() == name) {
                return ais[i];
            }
        }
        System.err.printf("No AI named %c found.\n", name);
        return null;
    }
    
    private Move bestMove (Board board, char player, int depth) { //Recursive
        Move bestMove = null;
        
        //Check for wins
        char winner = board.checkWin();
        if (winner == name) {
            return new Move (-1, -1, 10);
        } else if (winner != 0) {
            return new Move (-1, -1, -10);
        }
        
        //Check for stalemate
        if (board.emptyCells() == 0) {
            return new Move (-1, -1, 0);
        }
        
        for (int r = 0; r < board.getSize(); ++r) {
            for (int c = 0; c < board.getSize(); ++c) {
                if (board.getCell(r, c) == 0) {
                    Board tempBoard = board.clone();
                    tempBoard.setCell(r, c, player);
                    Move tempMove = bestMove(tempBoard, board.nextPlayer(player), depth + 1);
                    int difference = 0;
                    if (bestMove != null) {
                        if (player == name) { //Maximize for me
                            difference = tempMove.score - bestMove.score;
                        } else { //Minimize for others
                            difference = bestMove.score - tempMove.score;
                        }
                    }
                    if (bestMove == null || difference > 0 || (difference == 0 && rand.nextBoolean())) {
                        tempMove.row = r;
                        tempMove.col = c;
                        if (tempMove.score > 0) { //Devalue the move so that moves which win faster are scored better
                            --tempMove.score;
                        } else if (tempMove.score < 0) {
                            ++tempMove.score;
                        }
                        //if (bestMove != null) {
                        //    tempMove.makeStack(bestMove);
                        //}
                        bestMove = tempMove;
                    }
                }
            }
        }
        return bestMove;
    }
    
    private Move randomMove (Board board) {
        int r;
        int c;
        do {
            r = rand.nextInt(board.getSize());
            c = rand.nextInt(board.getSize());
        } while (board.getCell(r, c) != 0);
        
        return new Move(r, c, 0);
    }
}
