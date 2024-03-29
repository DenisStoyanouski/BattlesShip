package battleship;

import java.io.IOException;
import java.util.*;

/*
Objectives
        In this stage, you should arrange your ships on the game field. Before you start, let's discuss the conventions
         of the game:

        * On a 10x10 field, the first row should contain numbers from 1 to 10 indicating the column, and the first column
        should contain letters from A to J indicating the row.
        * The symbol ~ denotes the fog of war: the unknown area on the opponent's field and the yet untouched area on
        your field.
        * The symbol O denotes a cell with your ship, X denotes that the ship was hit, and M signifies a miss.
        * You have 5 ships: Aircraft Carrier is 5 cells, Battleship is 4 cells, Submarine is 3 cells, Cruiser is also
        3 cells, and Destroyer is 2 cells. Start placing your ships with the largest one.
        * To place a ship, enter two coordinates: the beginning and the end of the ship.
        * If an error occurs in the input coordinates, your program should report it. The message should contain the word
        Error.
*/


public class Main {


    final private static char[][] battleField1= new char[10][10];
    final private static char[][] battleField2= new char[10][10];

    private static int player;

    private static boolean allShipsAreReplacedPlayer1 = false;

    private static boolean allShipsAreReplacedPlayer2 = false;

    final private static List<String> letterAddress = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");

    final private static List<String> numberAddress = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    static final Scanner scanner = new Scanner(System.in);

    enum Ship {
        AIRCRAFT_CARRIER(5, "Aircraft Carrier"),
        BATTLESHIP(4,"Battleship"),
        SUBMARINE(3, "Submarine"),
        CRUISER(3,"Cruiser"),
        DESTROYER(2, "Destroyer");

        final int size;
        final String name;

         Ship(int size, String name) {
            this.size = size;
            this.name = name;
        }

        public int getSize() {
            return size;
        }

        public String getName() {
            return name;
        }

    }

    public static void main(String[] args) {
        player = 1;
        placeYourShip(player);
    }

    // method fills up all cells with fog of war;
    public static void field(int player) {
        //Create empty battleField;
        for (int i = 0; i < getBattleField(player).length; i++) {
            for (int j = 0; j < getBattleField(player)[0].length; j++) {
                getBattleField(player)[i][j] = '~';
            }
        }
        currentField(getBattleField(player));
    }

    public static String getInput() {
        return scanner.next();
    }

    public static void placeYourShip(int player) {

        System.out.printf("Player %d, place your ships on the game field %n", player);
        field(player);
            try {
                for (Ship ship : Ship.values()) {
                    String inputFirst;
                    String inputSecond;
                    int beginVer;
                    int beginHor;
                    int endVer;
                    int endHor;

                    System.out.printf("Enter the coordinates of the %s (%d cells):%n", ship.getName(), ship.getSize());

                    do { //return input data into useful form for array;
                        inputFirst = getInput().toUpperCase().trim();
                        inputSecond = getInput().toUpperCase().trim();
                        if (inputFirst.hashCode() > inputSecond.hashCode()) {
                            endVer = letterAddress.indexOf(inputFirst.substring(0, 1));
                            endHor = numberAddress.indexOf(inputFirst.substring(1));
                            beginVer = letterAddress.indexOf(inputSecond.substring(0, 1));
                            beginHor = numberAddress.indexOf(inputSecond.substring(1));

                        } else {
                            beginVer = letterAddress.indexOf(inputFirst.substring(0, 1));
                            beginHor = numberAddress.indexOf(inputFirst.substring(1));
                            endVer = letterAddress.indexOf(inputSecond.substring(0, 1));
                            endHor = numberAddress.indexOf(inputSecond.substring(1));
                        }
                    } while (!checkOfInput(inputFirst, inputSecond, ship.getSize(), beginHor, beginVer, endHor, endVer, player));

                }
            } catch(Exception e){
                e.getMessage();
            }

        if (player == 1) {
            allShipsAreReplacedPlayer1 = true;
        } else if (player == 2){
            allShipsAreReplacedPlayer2 = true;
        }

        changePlayer(player);
    }

    private static void shooting(int player) {
        if (player == 1) {
            currentField(fogOfWar(2));
        } else {
            currentField(fogOfWar(1));
        }
        System.out.println("---------------------");
            currentField(getBattleField(player));
        System.out.printf("Player %d, it's your turn:%n", player);
        makeShot(player);
    }

    private static void makeShot(int player) {
        String shotInput;
        do {
            shotInput = getInput().toUpperCase();
            resultOfShot(shotInput, player);
        } while(!checkOfShotInput(shotInput));
        if (!checkGameOver(player)) {
            changePlayer(player);
        }
    }

    static boolean checkOfInput(String inputFirst, String inputSecond, int length, int beginHor, int beginVer, int endHor, int endVer, int player) {
        boolean check = true;
        if (!inputFirst.matches("[A-Z]\\d\\d?") || !inputSecond.matches("[A-Z]\\d\\d?")) {
            System.out.println("Error! You entered the wrong coordinates! Try again:\"");
            check = false;
        } else if (!letterAddress.contains(inputFirst.substring(0, 1)) || !numberAddress.contains(inputFirst.substring(1))) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            check = false;
        } else if (!letterAddress.contains(inputSecond.substring(0, 1)) || !numberAddress.contains(inputSecond.substring(1))) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            check = false;
        } else if (!checkPlacementOfShip(beginHor, beginVer, endHor, endVer, length, player)) {
            check = false;
        }
        return check;

    }

    static boolean checkPlacementOfShip(int beginHor, int beginVer, int endHor, int endVer, int length, int player) {
        boolean checkReplacement = true;
        //check size of ship
        if (Math.abs(beginHor - endHor) != (length - 1)  && Math.abs(beginVer - endVer) != (length - 1)) {
            System.out.println("Error! Wrong length of the Submarine! Try again:");
            checkReplacement = false;
        } else if (beginHor != endHor && beginVer != endVer) { //check ship direction is not diagonal;
            System.out.println("Error! Wrong ship location! Try again:");
            checkReplacement = false;
        } else {
            //check placing to another one;
            search:
            for (int i = beginVer - 1; i <= endVer + 1; i++) {
                if (i >= 0 && i < 10) {
                    for (int j = beginHor - 1; j <= endHor + 1; j++) {
                        if (j >= 0 && j < 10) {
                            if (getBattleField(player)[i][j] == 'O') {
                                System.out.println("Error! You placed it too close to another one. Try again:");
                                checkReplacement = false;
                                break search;
                            }
                        }
                    }
                }
            }
        }
        if (checkReplacement) {
            placementOfShip(beginHor, beginVer, endHor, endVer, player);
        }
        return checkReplacement;
    }

    static void placementOfShip(int beginHor, int beginVer, int endHor, int endVer, int player) {
        if (beginHor == endHor) {
            for (int i = beginVer; i <= endVer; i++) {
                getBattleField(player)[i][endHor] = 'O';
            }
        } else {
            for (int i = beginHor; i <= endHor; i++) {
                getBattleField(player)[beginVer][i] = 'O';
            }
        }
        currentField(getBattleField(player));
    }

    public static void currentField(char[][] array) {
        // display current version of field;
        System.out.print("  ");
        for (String a : numberAddress) {
            System.out.print(a + " ");
        }
        System.out.println(" ");

        for (int i = 0; i < array.length; i++) {
            System.out.print(letterAddress.get(i));
            for (char b : array[i]) {
                System.out.print(" " + b);
            }
            System.out.printf("%n");
        }
    }

    public static void resultOfShot(String shotInput, int player) {
        int addressVer;
        int addressHor;
        int enemy;
        if (player == 1) {
            enemy = 2;
        } else {
            enemy = 1;
        }
        addressVer = letterAddress.indexOf(shotInput.substring(0, 1));
        addressHor = numberAddress.indexOf(shotInput.substring(1));
        if (checkOfShotInput(shotInput)) {
            if(getBattleField(enemy)[addressVer][addressHor] == 'M') {
                System.out.println("You missed!");
            } else if(getBattleField(enemy)[addressVer][addressHor] == 'X') {
                System.out.println("You hit a ship!");
            } else if (getBattleField(enemy)[addressVer][addressHor] == '~') {
                getBattleField(enemy)[addressVer][addressHor] = 'M';
                System.out.println("You missed. Try again:");
            } else if (getBattleField(enemy)[addressVer][addressHor] == 'O') {
                getBattleField(enemy)[addressVer][addressHor] = 'X';
                if (checkShipSank(addressVer, addressHor, enemy)) {
                    System.out.println("You sank a ship! Specify a new target:");
                } else {
                    System.out.println("You hit a ship! Try again");
                }
            }
        }
    }

    static boolean checkOfShotInput(String shotInput) {
        boolean check = true;
        if (!shotInput.matches("[A-Z]\\d\\d?")) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            check = false;
        } else if (!letterAddress.contains(shotInput.substring(0, 1)) || !numberAddress.contains(shotInput.substring(1))) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            check = false;
        }
        return check;
    }

    //method create fog of war;
    private static char[][] fogOfWar(int player) {
        char[][] fogOfWar = new char[10][10];
        for (int i = 0; i < fogOfWar.length; i++) {
            for (int j = 0; j < fogOfWar[0].length; j++) {
                fogOfWar[i][j] = getBattleField(player)[i][j];
                if(fogOfWar[i][j] == 'O') {
                    fogOfWar[i][j] = '~';
                }
            }
        }
        return fogOfWar;
    }

    // the method check that all ships on the battlefield was sunk;
    private static boolean checkGameOver(int player) {
        boolean gameOver = true;
        int enemy;
        if (player == 1) {
            enemy = 2;
        } else {
            enemy = 1;
        }
        for (char[] row : getBattleField(enemy) ) {
            for (char cell : row) {
                if (cell == 'O') {
                    gameOver = false;
                    break;
                }
            }
        }
        if (gameOver) {
            System.out.println("You sank the last ship. You won. Congratulations!");
        }
        return gameOver;
    }

    // the method for checking all cells of the ship was shot and the ship is sunk;
    private static boolean checkShipSank(int addressVer, int addressHor, int player) {
        boolean checkShipSank = true;
        search:
        for (int i = addressVer - 1; i <= addressVer + 1; i++)
            if (i >= 0 && i < 10) {
                for (int j = addressHor - 1; j <= addressHor + 1; j++) {
                    if (j >= 0 && j < 10) {
                        if (getBattleField(player)[i][j] == 'O') {
                            checkShipSank = false;
                            break search;

                        }
                    }
                }
            }
        return checkShipSank;
    }

    // the method for switching between players by enter;
    private static void changePlayer(int player) {
        promptEnterKey();
            if (player != 1) {
                player = 1;
                if (!allShipsAreReplacedPlayer1) {
                    placeYourShip(player);
                } else {
                    shooting(player);
                }
            } else if (player != 2 ) {
                player = 2;
                if (!allShipsAreReplacedPlayer2) {
                    placeYourShip(player);
                } else {
                    shooting(player);
                }
            }
    }

    public static void promptEnterKey(){
        System.out.println("Press Enter and pass the move to another player");
        System.out.print("...");
        try {
            int read = System.in.read(new byte[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //method for take the battlefield of a current player;
    static char[][] getBattleField(int player) {
        return player == 1 ? battleField1 : battleField2;
    }


}


