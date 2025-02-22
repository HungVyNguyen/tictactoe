import java.util.Scanner;

public class TicTacToe {
    // Definerer spiller og computer symboler
    private static final char PLAYER = 'X';
    private static final char COMPUTER = 'O';
    private static final char EMPTY = ' ';

    // Tic Tac Toe spillebræt
    private static char[][] board = {
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY}
    };

    private static final int[][] evaluationMatrix = {
            {3, 2, 3},
            {2, 4, 2},
            {3, 2, 3}
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Spillet kører i en løkke, indtil en vinder findes eller brættet er fyldt
        while (true) {
            playerMove(scanner);
            if (isGameOver()) break;

            computerMove();
            printBoard();
            if (isGameOver()) break;
        }
        scanner.close();
    }

    // Håndterer spillerens tur med fejlhåndtering
    private static void playerMove(Scanner scanner) {
        int row = -1, col = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("Enter row (0-2): ");
                row = Integer.parseInt(scanner.next());
                System.out.print("Enter column (0-2): ");
                col = Integer.parseInt(scanner.next());

                if (!isValidMove(row, col)) {
                    System.out.println("Invalid move! Please enter a valid empty cell.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter numbers between 0 and 2.");
                scanner.nextLine(); // Ryd scanner-bufferen
            }
        }
        board[row][col] = PLAYER;
    }

    // Håndterer computerens tur ved at bruge Minimax med en bestemt søgedybde
    private static void computerMove() {
        int[] bestMove = minimax(COMPUTER, 3); // Søger 3 træk frem
        board[bestMove[1]][bestMove[2]] = COMPUTER;
    }

    // Minimax-algoritme til at finde det bedste træk med søgedybde
    private static int[] minimax(char player, int depth) {
        int bestScore = (player == COMPUTER) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = {-1, -1, -1};

        // Stopbetingelser
        if (isWinner(PLAYER)) return new int[]{-10 + depth};
        if (isWinner(COMPUTER)) return new int[]{10 - depth};
        if (isBoardFull() || depth == 0) return new int[]{evaluateBoard()};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = player;
                    int score = minimax(player == COMPUTER ? PLAYER : COMPUTER, depth - 1)[0];
                    board[i][j] = EMPTY; // Tilbagefør træk

                    int adjustedScore = score + (player == COMPUTER ? evaluationMatrix[i][j] : -evaluationMatrix[i][j]);

                    if ((player == COMPUTER && adjustedScore > bestScore) || (player == PLAYER && adjustedScore < bestScore)) {
                        bestScore = adjustedScore;
                        bestMove = new int[]{bestScore, i, j};
                    }
                }
            }
        }
        return bestMove;
    }

    // Evaluerer spillebrættet baseret på evalueringstabellen
    private static int evaluateBoard() {
        int score = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == COMPUTER) {
                    score += evaluationMatrix[i][j];
                } else if (board[i][j] == PLAYER) {
                    score -= evaluationMatrix[i][j];
                }
            }
        }
        return score;
    }

    // Tjekker om et træk er gyldigt
    private static boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == EMPTY;
    }

    // Tjekker om spillet er afsluttet
    private static boolean isGameOver() {
        if (isWinner(PLAYER)) {
            System.out.println("You win!");
            return true;
        }
        if (isWinner(COMPUTER)) {
            System.out.println("Computer wins!");
            return true;
        }
        if (isBoardFull()) {
            System.out.println("It's a tie!");
            return true;
        }
        return false;
    }

    // Tjekker om en spiller har vundet
    private static boolean isWinner(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    // Tjekker om brættet er fyldt (uafgjort)
    private static boolean isBoardFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == EMPTY) return false;
            }
        }
        return true;
    }

    // Udskriver spillebrættet i konsollen
    private static void printBoard() {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell == EMPTY ? "-" : cell);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
