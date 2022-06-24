package battleship;

import java.util.*;


public class Main {


    final private static char[][] battleField = new char[10][10];

    final private static List<String> typeOfShip = Arrays.asList("Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer");

    final private static List<Integer> lengthOfShip = Arrays.asList(5, 4, 3, 3, 2);

    final private static List<String> verAddress = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");

    final private static List<String> horAddress = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    public static void main(String[] args) {
        Field();
        Coordinate();
    }

    // create field
    public static void Field() {
        //Create empty battleField;
        for (int i = 0; i < battleField.length; i++) {
            for (int j = 0; j < battleField[0].length; j++) {
                battleField[i][j] = '~';
            }
        }
        CurrentField();
    }

    public static void Coordinate() {
        String coordinateBegin;
        String coordinateEnd;
        int beginVer;
        int beginHor;
        int endVer;
        int endHor;

            try (Scanner scanner = new Scanner(System.in)) {
                for (String ship : typeOfShip) {
                    int length = lengthOfShip.get(typeOfShip.indexOf(ship));
                    System.out.printf("Enter the coordinates of the %s (%d cells):%n", ship, length);
                    do {
                        coordinateBegin = scanner.next().toUpperCase();
                        coordinateEnd = scanner.next().toUpperCase();
                        //return input data into useful form for array;
                        beginVer = verAddress.indexOf(coordinateBegin.substring(0, 1));
                        beginHor = horAddress.indexOf(coordinateBegin.substring(1));
                        endVer = verAddress.indexOf(coordinateEnd.substring(0, 1));
                        endHor = horAddress.indexOf(coordinateEnd.substring(1));
                    } while (!checkOfInput(coordinateBegin, coordinateEnd, length, beginHor, beginVer, endHor, endVer));
                    ReplacementOfShip(beginHor, beginVer, endHor, endVer);
                }
            } catch(Exception e){
                    e.getMessage();
            }

    }

    static boolean checkOfInput(String coordinateEnd, String coordinateBegin, int length, int beginHor, int beginVer, int endHor, int endVer) {
        boolean check = true;
        if (!coordinateBegin.matches("[A-Z]\\d\\d?") || !coordinateEnd.matches("[A-Z]\\d\\d?")) {
            System.out.println("Try again");
            check = false;
        } else if (!verAddress.contains(coordinateBegin.substring(0, 1)) || !horAddress.contains(coordinateBegin.substring(1))) {
            System.out.println("Change the coordinate of the begin");
            check = false;
        } else if (!verAddress.contains(coordinateEnd.substring(0, 1)) || !horAddress.contains(coordinateEnd.substring(1))) {
            System.out.println("Change the coordinate of the end");
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
            for (int i = beginHor - 1; i <= endHor + 1; i++) {
                if (i > 0 && i < 10) {
                    for (int j = beginVer - 1; j <= endVer + 1; j++) {
                        if (j > 0 && j < 10) {
                            if (battleField[j][i] == 'O') {
                                System.out.println("Error! You placed it too close to another one. Try again:");
                                checkReplacement = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return checkReplacement;
    }

    static void ReplacementOfShip(int beginHor, int beginVer, int endHor, int endVer) {
        if (beginHor == endHor) {
            for (int i = beginVer; i <= endVer; i++) {
                battleField[i][endHor] = 'O';
            }
        } else {
            for (int i = beginHor; i <= endHor; i++) {
                battleField[beginVer][i] = 'O';
            }
        }
        CurrentField();
    }

    public static void CurrentField() {
        // display current version of field;
        System.out.print("  ");
        for (String a : horAddress) {
            System.out.print(a + " ");
        }
        System.out.println(" ");

        for (int i = 0; i < battleField.length; i++) {
            System.out.print(verAddress.get(i));
            for (char b : battleField[i]) {
                System.out.print(" " + b);
            }
            System.out.println();
        }
    }

}


