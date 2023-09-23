public class Move
{
    public int row;
    public int col;
    public int score;
    public String stack = "";
    
    public Move (int row, int col, int score) {
        this.row = row;
        this.col = col;
        this.score = score;
    }
    
    public void makeStack (Move move) {
        this.stack = String.format("%d,%d: %d\n", row, col, score);
        if (move == null) {
            this.stack += "end\n\n";
        } else {
            this.stack += move.stack;
        } 
    }
}
