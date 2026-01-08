import java.util.InputMismatchException;
import java.util.Scanner;

public class JocOca {

    public static void main(String[] args) {

        // ----- VARIABLES -----
        // SCANNER
        Scanner askVar = new Scanner(System.in);

        // INT
        int turn = 0;
        int measure = 0;
        int randStart = 0;

        // BOOLEANS
        boolean isFinish = false;

        // CASELLES ESPECIALS
        int[] goose = new int[] { 5, 9, 14, 18, 23, 27, 32, 36, 41, 45, 50, 54, 59 };
        int finalSquare = 63;

        // INICI JOC
        measure = askPlayers(measure, askVar); // demana el nombre de jugadors

        String[] playersNames = new String[measure]; // array amb els noms dels jugadors
        int[] squares = new int[measure]; // casella actual per jugador
        int[] penalizeTurns = new int[measure]; // penalització per jugador

        // Cada jugador té el seu "primer tir" independent
        boolean[] firstThrow = new boolean[measure];
        for (int i = 0; i < firstThrow.length; i++) {
            firstThrow[i] = true;
        }

        playersNames = askNames(playersNames, askVar); // demana els noms dels jugadors
        randStart = startGame(playersNames, randStart); // inicia el joc i retorna el jugador que comença aleatoriament
        turn = randStart - 1;

        // BUCLE PRINCIPAL DEL JOC
        while (!isFinish) {

            System.out.println("\nPrem ENTER per continuar amb el torn de " + playersNames[turn] + "...");
            askVar.nextLine();

            // Si esta penalitzat, no tira i es redueix la penalitzacio
            if (penalizeTurns[turn] > 0) {
                penalizeTurns[turn]--;
                System.out.println("\nCasella actual pel jugador " + playersNames[turn] + ": Casella " + squares[turn]);
                System.out.println("Estàs penalitzat. Et queden " + penalizeTurns[turn] + " torn(s) sense tirar.");
            } else {
                isFinish = playTurn(turn, playersNames, squares, penalizeTurns, goose, finalSquare, firstThrow);
            }

            turn++;
            if (turn >= measure) { // Torna turn a 0 quan supera la quantitat de jugadors.
                turn = 0;
            }
        }
    }

    // Demana nombre de jugadors amb gestio de errors
    public static int askPlayers(int measure, Scanner askVar) {
        System.out.println("\n||| JOC DE L'OCA |||\n");
        int num = readInt("Introdueix el nombre de jugadors: ", askVar);
        if (num < 2 || num > 4) {
            System.out.println("|||ERROR||| --> Nombre de jugadors no valid, introdueix un nombre entre 2 i 4.");
            return askPlayers(measure, askVar);
        } else {
            measure = num;
        }
        return measure;
    }

    // Demana nom de jugadors
    public static String[] askNames(String[] playersNames, Scanner askVar) {
        for (int i = 0; i < playersNames.length; i++) {
            System.out.print("Introdueix el nom del jugador " + (i + 1) + ": ");
            playersNames[i] = askVar.nextLine();
        }
        return playersNames;
    }

    // Inici Joc + Torn Aleatori
    public static int startGame(String[] playersNames, int randStart) {
        System.out.println("\n||| BENVINGUTS AL JOC DE L'OCA |||\n");
        System.out.println("Jugadors: ");
        for (int i = 0; i < playersNames.length; i++) {
            System.out.println("> " + playersNames[i]);
        }
        randStart = (int) (Math.random() * playersNames.length + 1);
        System.out.println("\nComença el jugador Nº" + randStart + " - " + playersNames[randStart - 1]);
        return randStart;
    }

    // Refresca les caselles
    public static int[] refreshSquares(int[] squares, int playerIndex, int move) {
        squares[playerIndex] = squares[playerIndex] + move;
        return squares;
    }

    public static boolean checkFinalSquare(int square, int finalSquare) {
        return square >= finalSquare;
    }

    // Tir de daus (retorna els valors dels 2 daus)
    public static int[] rollDice(int numDice) {
        int dice1 = 0, dice2 = 0;

        System.out.println("> Tirant els daus...");

        if (numDice == 1) {
            dice1 = (int) (Math.random() * 6 + 1);
            System.out.println("Has tirat el dau i ha sortit: " + dice1);
            return new int[] { dice1 };
        } else if (numDice == 2) {
            dice1 = (int) (Math.random() * 6 + 1);
            dice2 = (int) (Math.random() * 6 + 1);
            int sum = dice1 + dice2;
            System.out.println("Has tirat els daus i ha sortit " + dice1 + " i " + dice2 + ", avances " + sum + " caselles.");
            return new int[] { dice1, dice2 };
        } else {
            System.out.println("|||ERROR||| --> Error de calcul en els daus, torna a intentar.");
            return new int[] { 0, 0 };
        }
    }

    // Retorna la següent oca si hi caus, si no, retorna la mateixa casella
    public static int checkOcaSquares(int square, int[] gooseSquares) {
        for (int i = 0; i < gooseSquares.length; i++) {
            if (square == gooseSquares[i]) {
                if (i + 1 < gooseSquares.length) {
                    System.out.println("De oca en oca i tiro perquè em toca.");
                    return gooseSquares[i + 1];
                }
            }
        }
        return square;
    }

    // Caselles especials
    public static int checkSpecialSquares(int square) {
        switch (square) {
            case 6:
                System.out.println("Has caigut al Pont (casella 6). Avances a la casella 12.");
                return 12;
            case 12:
                System.out.println("Has caigut al Pont (casella 12). Retrocedeixes a la casella 6.");
                return 6;
            case 42:
                System.out.println("Has caigut al Laberint (casella 42). Retrocedeixes a la casella 39.");
                return 39;
            case 58:
                System.out.println("Has caigut al Mort (casella 58). Tornes a la casella inicial (0).");
                return 0;
            default:
                return square;
        }
    }

    // Penalitzacions
    public static int checkPenalize(int square, int turnsLeft) {
        if (turnsLeft > 0)
            return turnsLeft;

        switch (square) {
            case 19:
                System.out.println("Has caigut a la Fonda (casella 19). Perds 1 torn.");
                return 1;
            case 31:
                System.out.println("Has caigut al Pou (casella 31). Perds 2 torns.");
                return 2;
            case 52:
                System.out.println("Has caigut a la Presó (casella 52). Perds 3 torns.");
                return 3;
            default:
                return 0;
        }
    }

    // En cas de pasar-se de 63 te en compte el rebot
    public static int bounceFinal(int square, int finalSquare) {
        if (square > finalSquare) {
            int extra = square - finalSquare;
            return finalSquare - extra;
        }
        return square;
    }

    // Llegir int amb gestio d'errors
    public static int readInt(String message, Scanner askVar) {
        int var = 0;
        try {
            System.out.print(message);
            var = askVar.nextInt();
            askVar.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("|||ERROR||| --> Entrada no valida, torna a introduir un numero enter.");
            askVar.next();
            var = readInt(message, askVar);
        } catch (Exception e) {
            System.out.println("|||ERROR||| --> Error, torna a introduir el valor");
            askVar.next();
            var = readInt(message, askVar);
        }
        return var;
    }

    // Torn
    public static boolean playTurn(int turn, String[] playersNames, int[] squares, int[] penalizeTurns, int[] goose,
            int finalSquare, boolean[] firstThrow) {

        boolean extraRoll;

        do {
            extraRoll = false; //Tir extra segons normes del joc, només si aplica

            if (squares[turn] == 0) {
                System.out.println("\nCasella actual pel jugador " + playersNames[turn] + ": Casella Inicial (0)");
            } else {
                System.out.println("\nCasella actual pel jugador " + playersNames[turn] + ": Casella " + squares[turn]);
            }

            int[] dice = rollDice(2); //Daus fet amb llista per que aixi puc gestionar les combinacions de 6+3 i 5+4
            int dice1 = dice[0]; //Numero del dau 1
            int dice2 = dice[1]; //Numero del dau 1
            int move = dice1 + dice2; //Resultat

            //Si es el primer tir mira la combinació que t'ajuda a avançar
            if (firstThrow[turn] && squares[turn] == 0) {
                if ((dice1 == 3 && dice2 == 6) || (dice1 == 6 && dice2 == 3)) {
                    System.out.println("De dado a dado i tiro perquè em toca. (3 i 6) Vas a la casella 26.");
                    squares[turn] = 26;
                    extraRoll = true;
                } else if ((dice1 == 4 && dice2 == 5) || (dice1 == 5 && dice2 == 4)) {
                    System.out.println("De dado a dado i tiro perquè em toca. (4 i 5) Vas a la casella 53.");
                    squares[turn] = 53;
                    extraRoll = true;
                } else {
                    squares = refreshSquares(squares, turn, move);
                }
                firstThrow[turn] = false; // ja no és el primer tir d'aquest jugador
            } else {
                squares = refreshSquares(squares, turn, move);
            }

            // Rebot si passa de 63
            squares[turn] = bounceFinal(squares[turn], finalSquare);

            // Oques
            int nextSquare = checkOcaSquares(squares[turn], goose);
            if (nextSquare != squares[turn]) {
                squares[turn] = nextSquare;
                extraRoll = true;
            }

            // Especials i penalitzacions
            squares[turn] = checkSpecialSquares(squares[turn]);
            penalizeTurns[turn] = checkPenalize(squares[turn], penalizeTurns[turn]);

            System.out.println("Nou estat del jugador " + playersNames[turn] + ": Casella " + squares[turn]);

            if (checkFinalSquare(squares[turn], finalSquare)) {
                System.out.println("\n||| EL GUANYADOR ÉS: " + playersNames[turn] + " |||");
                System.out.println("DEBUG -> isFinish=true (ha guanyat " + playersNames[turn] + ")");
                return true;
            }

        } while (extraRoll);
    

        return false;
    }
}
