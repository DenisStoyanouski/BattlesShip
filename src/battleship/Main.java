package battleship;

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


    final private static char[][] battleField = new char[10][10];

    final private static List<String> typeOfShip = Arrays.asList("Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer");

    final private static List<Integer> lengthOfShip = Arrays.asList(5, 4, 3, 3, 2);

    final private static List<String> verAddress = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");

    final private static List<String> horAddress = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        field();
        game();
    }

    // create field
    public static void field() {
        //Create empty battleField;
        for (int i = 0; i < battleField.length; i++) {
            for (int j = 0; j < battleField[0].length; j++) {
                battleField[i][j] = '~';
            }
        }
        currentField(battleField);
    }

    public static void game() {
        String inputFirst;
        String inputSecond;
        String shotInput;
        int beginVer;
        int beginHor;
        int endVer;
        int endHor;

            try {
                for (String ship : typeOfShip) {
                    int length = lengthOfShip.get(typeOfShip.indexOf(ship));
                    System.out.printf("Enter the coordinates of the %s (%d cells):%n", ship, length);
                    do { //return input data into useful form for array;
                        inputFirst = scanner.next().toUpperCase();
                        inputSecond = scanner.next().toUpperCase();
                        if (inputFirst.compareTo(inputSecond) > 0 || inputFirst.length() > inputSecond.length()) {
                            endVer = verAddress.indexOf(inputFirst.substring(0, 1));
                            endHor = horAddress.indexOf(inputFirst.substring(1));
                            beginVer = verAddress.indexOf(inputSecond.substring(0, 1));
                            beginHor = horAddress.indexOf(inputSecond.substring(1));

                        } else {
                            beginVer = verAddress.indexOf(inputFirst.substring(0, 1));
                            beginHor = horAddress.indexOf(inputFirst.substring(1));
                            endVer = verAddress.indexOf(inputSecond.substring(0, 1));
                            endHor = horAddress.indexOf(inputSecond.substring(1));
                        }
                    } while (!checkOfInput(inputFirst, inputSecond, length, beginHor, beginVer, endHor, endVer));
                    placementOfShip(beginHor, beginVer, endHor, endVer);
                }
                System.out.println("The game starts!");
                currentField(fogOfWar());
                System.out.println("Take a shot!");

                do {
                    shotInput = scanner.next().toUpperCase();
                    makeShot(shotInput);
                } while(!checkOfShotInput(shotInput));

                currentField(battleField);
            } catch(Exception e){
                    e.getMessage();
            }

    }

    static boolean checkOfInput(String inputFirst, String inputSecond, int length, int beginHor, int beginVer, int endHor, int endVer) {
        boolean check = true;
        if (!inputFirst.matches("[A-Z]\\d\\d?") || !inputSecond.matches("[A-Z]\\d\\d?")) {
            System.out.println("Error! You entered the wrong coordinates! Try again:\"");
            check = false;
        } else if (!verAddress.contains(inputFirst.substring(0, 1)) || !horAddress.contains(inputFirst.substring(1))) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            check = false;
        } else if (!verAddress.contains(inputSecond.substring(0, 1)) || !horAddress.contains(inputSecond.substring(1))) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            check = false;
        } else if (!checkReplacementOfShip(beginHor, beginVer, endHor, endVer, length)) {
            check = false;
        }
        return check;

    }

    static boolean checkReplacementOfShip(int beginHor, int beginVer, int endHor, int endVer, int length) {
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
            for (int i = beginHor - 1; i <= endHor + 1; i++) {
                if (i > 0 && i < 10) {
                    for (int j = beginVer - 1; j <= endVer + 1; j++) {
                        if (j > 0 && j < 10) {
                            if (battleField[j][i] == 'O') {
                                System.out.println("Error! You placed it too close to another one. Try again:");
                                checkReplacement = false;
                                break search;
                            }
                        }
                    }
                }
            }
        }
        return checkReplacement;
    }

    static void placementOfShip(int beginHor, int beginVer, int endHor, int endVer) {
        if (beginHor == endHor) {
            for (int i = beginVer; i <= endVer; i++) {
                battleField[i][endHor] = 'O';
            }
        } else {
            for (int i = beginHor; i <= endHor; i++) {
                battleField[beginVer][i] = 'O';
            }
        }
        currentField(battleField);
    }

    public static void currentField(char[][] array) {
        // display current version of field;
        System.out.print("  ");
        for (String a : horAddress) {
            System.out.print(a + " ");
        }
        System.out.println(" ");

        for (int i = 0; i < array.length; i++) {
            System.out.print(verAddress.get(i));
            for (char b : array[i]) {
                System.out.print(" " + b);
            }
            System.out.printf("%n");
        }
    }

    public static void makeShot(String shotInput) {
        int addressVer;
        int addressHor;

        addressVer = verAddress.indexOf(shotInput.substring(0, 1));
        addressHor = horAddress.indexOf(shotInput.substring(1));
        if (checkOfShotInput(shotInput)) {
            if (battleField[addressVer][addressHor] == '~') {
                battleField[addressVer][addressHor] = 'M';
                currentField(fogOfWar());
                System.out.println("You missed!");

            } else {
                battleField[addressVer][addressHor] = 'X';
                currentField(fogOfWar());
                System.out.println("You hit a ship!");


            }

        }
    }

    static boolean checkOfShotInput(String shotInput) {
        boolean check = true;
        if (!shotInput.matches("[A-Z]\\d\\d?")) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            check = false;
        } else if (!verAddress.contains(shotInput.substring(0, 1)) || !horAddress.contains(shotInput.substring(1))) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            check = false;
        }
        return check;
    }

    private static char[][] fogOfWar() {
        char[][] fogOfWar = new char[10][10];
        for (int i = 0; i < fogOfWar.length; i++) {
            for (int j = 0; j < fogOfWar[0].length; j++) {
                fogOfWar[i][j] = battleField[i][j];
                if(fogOfWar[i][j] == 'O') {
                    fogOfWar[i][j] = '~';
                }
            }
        }
        return fogOfWar;
    }

}


