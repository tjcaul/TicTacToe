import java.util.Scanner;

public class TicTacToe {
    private static String players = "";
    private static String cellNames = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ()[]{}<>";

    public static void main (String[] args) {
        Scanner in = new Scanner(System.in);
        Ai[] ais = null;
        boolean playAgain;
        int boardSize = 3;
        boolean showAiMoves = false;
        boolean autoPlayAgain = false;
 
        if (args.length >= 1) {
            boardSize = Integer.parseInt(args[0]);
        }
        if (args.length >= 2) { //Humans
            addPlayers(args[1].toUpperCase());
        } else {
            addPlayers("XO");
        }
        if (args.length >= 3) { //AIs
            String list = args[2].toLowerCase();
            addPlayers(list);
            ais = new Ai[list.length()];
            for (int i = 0; i < list.length(); ++i) {
                ais[i] = new Ai(list.charAt(i), 1.0);
            }
        }
        if (args.length >= 4 && args[3].toLowerCase().equals("auto")) {
            autoPlayAgain = true;
        }
        
        Board board = new Board(boardSize, players, cellNames);
        System.out.println("\nWelcome to Tic-Tac-Toe.");
        if (board.namedCellsEnabled()) {
            System.out.println("To place your mark,\npress the number of\nthe corresponding\ncell on your turn.\n");
        } else {
            System.out.println("To place your mark,\ntype its coordinates\nin the format\n\"column,row\".\n");
        }
        System.out.println("********************");
        
        //Game loop
        do {
            board.clear();
            char result = play(board, in, ais, showAiMoves);
            if (result == Board.QUIT) {
                System.out.println("\nWhat are you, a quitter?\n");
            } else if (result == Board.STALEMATE) {
                System.out.println("\nDraw.\n\"A strange game. The\nonly winning move is\nnot to play.\"\n");
                board.print();
            } else {
                System.out.printf("\nPlayer %c wins!\n", result);
                board.print();
            }
            
            System.out.println("\n********************");

            if (autoPlayAgain) {
                playAgain = true;
            } else {
                System.out.println();
                playAgain = askToPlayAgain(in);
                System.out.println("********************");
            }
        } while (playAgain);
    }
    
    private static char play (Board board, Scanner in, Ai[] ais, boolean showAiMoves) {
        char player = players.charAt(0);
        char winner;
        do {
            char c;
            boolean valid;
            if (showAiMoves || !isAi(player)) {
                System.out.println();
                board.print();
                System.out.printf("\nPlayer %c's turn\n", player);
            }
            do {
                String command;
                if (isAi(player)) {
                    command = Ai.getAiByName(ais, player).play(board);
                } else {
                    command = in.nextLine();
                }
                if (command.toLowerCase().equals("quit")) {
                    return Board.QUIT;
                }
                if (command.contains(",")) {
                    valid = board.setCellByCoords(command, player);
                } else {
                    valid = board.setCellBySymbol(command.charAt(0), player);
                }
            } while (!valid);
            
            winner = board.checkWin();
            if (winner == 0 && board.emptyCells() == 0)
                return Board.STALEMATE;
            player = board.nextPlayer(player);
        } while (winner == 0);
        return winner;
    }
        
    private static void addPlayers (String list) {
        players += list;
        for (int i = 0; i < list.length(); ++i) {
            cellNames = cellNames.replace(String.valueOf(list.charAt(i)), ""); //Remove players from valid cell labels to prevent ambiguity
        }
    }
    
    private static boolean isAi (char c) {
        return (c >= 'a' && c <= 'z'); //If using some weird non-ASCII format like AaBbCc..., this will fail. Too bad.
    }
    
    private static boolean askToPlayAgain (Scanner in) {
        boolean playAgain = false; //Guaranteed to be changed in do..while loop, but Java doesn't realise this
        boolean valid;
        do {
            valid = true;
            System.out.print("Shall we play a game? ");
            String response = in.nextLine().toLowerCase();
            if (response.equals("y") || response.equals("yes")|| response.equals("sure")|| response.equals("yep") || response.equals("love to")) {
                playAgain = true;
            } else if (response.equals("n") || response.equals("no") || response.equals("no thanks") || response.equals("nah") || response.equals("quit")) {
                playAgain = false;
            } else {
                valid = false;
            }
        } while (!valid);
        System.out.print("\n");
        return playAgain;
    }
}