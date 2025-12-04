import java.util.InputMismatchException;
import java.util.Scanner;

public class JocOca {
    public static void main(String[] args) {
        //DECLARAR VARIABLES
        //SCANNER
        Scanner askVar = new Scanner(System.in);

        //INT
        int turn = 0;
        int measure = 0;
        int squares = 0;
        //DOUBLES

        //BOOLEANS
        boolean isFinish = false;
        //ARRAYS
        int[] numPlayers = new int[measure];
        int[] penalize = new int[2];
        //CASELLES ESPECIALS
            //ARRAYS
            int[] goose = new int[]{5, 9, 14, 18, 23, 27, 32, 36, 41, 45, 50, 54, 59};
            int[] bridges = new int[]{6, 12};
            //INT
            int inn = 19; //Fonda, un torn sense moure
            int dice36 = 26; //Nomes a la primera tirada si dau te la combinacio de 3 i 6 es va a la casella 26
            int well = 31; //Casella on et quedes 2 torns o si algu cau darrere teu, surts
            int maze = 42, mazeBackTo = 39; //Laberint casella 42, si caus, vas a la mazeBackTo (39)
            int jail = 52; //3 Torns sense tirar
            int dice45 = 53; //Nomes a la primera tirada si dau te la combinacio de 4 i 5 es va a la casella 53
            int die = 58, reset = 1; //Si caus aqui, tornes a la casella 1
            int finalSquare = 63; //Guanyes

        //Programa Principal
        numPlayers = askPlayers(numPlayers, askVar);
    }
    public static int[] askPlayers(int[] numPlayers, Scanner askVar){
        readInt("Introdueix el nombre de jugadors: ", askVar);
        if (numPlayers.length < 2 || numPlayers.length > 4){
            System.out.println("||ERROR|| --> Nombre de jugadors no valid, introdueix un nombre entre 2 i 4.");
            askPlayers(numPlayers, askVar);
        }
        return numPlayers;
    }








































    //LLEGIR SCANNER + GESTIO ERRORS
    public static void readInt(String message, Scanner askVar){
        try{
            System.out.print(message);
            int var = askVar.nextInt();
        }catch(InputMismatchException e) {
            System.out.println("||ERROR|| --> Entrada no valida, torna a introduir un numero enter.");
            askVar.next();
            readInt(message, askVar);
        }catch (Exception e){
            System.out.println("||ERROR|| --> Error, torna a introduir el valor");
            askVar.next();
            readInt(message, askVar);
        }
    }
}
