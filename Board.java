import java.util.Scanner;
import java.util.InputMismatchException;

public class Board {
    private int boardSize;
    private String players = "";
    private String cellNames;
    private char[][] board; //Row-major order
    
    public static final char QUIT = 0;
    public static final char STALEMATE = 1;
    
    public Board (int boardSize, String players, String cellNames) {
        this.boardSize = boardSize;
        board = new char[boardSize][boardSize];
        this.players = players;
        this.cellNames = cellNames;
    }
    
    public Board clone () {
        Board clone = new Board(boardSize, players, cellNames);
        for (int r = 0; r < boardSize; ++r) {
            for (int c = 0; c < boardSize; ++c) {
                clone.setCell(r, c, this.getCell(r, c));
            }
        }
        return clone;
    }
    
    public void clear () {
        for (int r = 0; r < boardSize; ++r) {
            for (int c = 0; c < boardSize; ++c) {
                board[r][c] = 0;
            }
        }
    }
    
    public void setCell (int row, int col, char player) {
        board[row][col] = player;
        if (!(players.contains(String.valueOf(player)) || player == 0)) {
            System.err.printf("board.setCell() called with unknown player %c\n", player);
        }
    }
    
    public boolean setCellBySymbol (char symbol, char player) {
        if (!namedCellsEnabled()) {
            System.out.println("Invalid coordinates.\nTry again.");
            return false;
        }
        for (int r = 0; r < boardSize; ++r) {
            for (int c = 0; c < boardSize; ++c) {
                if (getCellName(r, c) == symbol && getCell(r, c) == 0) {
                    setCell(r, c, player);
                    return true;
                }
            }
        }
        System.out.printf("You can't put your %c\nthere. Try again.\n", player);
        return false;
    }
    
    public boolean setCellByCoords (String command, char player) {
        int r, c;
        try {
            Scanner coords = new Scanner(command).useDelimiter(",");
            c = coords.nextInt() - 1;
            r = coords.nextInt() - 1;
        } catch (InputMismatchException e) {
            System.out.println("Invalid coordinates.\nTry again.");
            return false;
        }
        if (c < 0 || c >= boardSize || r < 0 || r >= boardSize) {
            System.out.println("Coordinates outside\nthe board! No\nthinking outside the\nbox here.");
            return false;
        }
        if (getCell(r, c) != 0) {
            System.out.printf("You can't put your %c\nthere. Try again.\n", player);
            return false;
        }
        setCell(r, c, player);
        return true;
    }
    
    public char getCell (int row, int col) {
        return board[row][col];
    }
    
    public int getSize () {
        return boardSize;
    }
    
    public void print () {
        for (int r = 0; r < boardSize; ++r) {
            if (r != 0) {
                printTimes("---+", boardSize - 1);
                System.out.println("---");
            }
            for (int c = 0; c < boardSize; ++c) {
                char cell = board[r][c];
                if (cell == 0) {
                    cell = getCellName(r, c);
                }
                if (c != 0) {
                    System.out.print("|");
                }
                System.out.printf(" %c ", cell);
            }
            System.out.println("");
        }
    }
        
    public int emptyCells () {
        int num = 0;
        for (int r = 0; r < boardSize; ++r) {
            for (int c = 0; c < boardSize; ++c) {
                if (getCell(r, c) == 0) {
                    ++num;
                }
            }
        }
        return num;
    }
    
    public char checkWin () {
        for (int i = 0; i < players.length(); ++i) {
            char player = players.charAt(i);
            if (checkWinPlayer(player)) {
                return player;
            }
        }
        return 0;
    }
    
    private boolean checkWinPlayer (char player) {
        return checkRowWin(player) || checkColWin(player) || checkSlashWin(player) || checkBackslashWin(player);
    }
    
    private boolean checkRowWin (char player) {
        for (int r = 0; r < boardSize; ++r) {
            boolean rowFull = true;
            for (int c = 0; c < boardSize; ++c) {
                if (board[r][c] != player) {
                    rowFull = false;
                }
            }
            if (rowFull) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkColWin (char player) {
        for (int c = 0; c < boardSize; ++c) {
            boolean colFull = true;
            for (int r = 0; r < boardSize; ++r) {
                if (board[r][c] != player) {
                    colFull = false;
                }
            }
            if (colFull) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkSlashWin (char player) {
        for (int i = 0; i < boardSize; ++i) {
            if (board[i][boardSize - 1 - i] != player) {
                return false;
            }
        }
        return true;
    }
    
    private boolean checkBackslashWin (char player) {
        for (int i = 0; i < boardSize; ++i) {
            if (board[i][i] != player) {
                return false;
            }
        }
        return true;
    }
    
    private void printTimes (String str, int n) {
        for (int i = 0; i < n; ++i) {
            System.out.print(str);
        }
    }
    
    private char getCellName (int row, int col) {
        int index = (row * boardSize) + col;
        if (namedCellsEnabled()) {
            return cellNames.charAt(index);
        } else {
            return ' '; //Too many cells to handle!
        }
    }
    
    public boolean namedCellsEnabled () {
        int totalCells = boardSize * boardSize;
        return totalCells <= cellNames.length();
    }
    
    public char nextPlayer (char c) {
        int index = players.indexOf(c) + 1;
        if (index >= players.length())
            index = 0;
        return players.charAt(index);
    }
}