package battleship;

import java.util.*;


public class Main {
    private static int fieldHorizontal;
    private static int fieldVertical;

    final private static char[][] battleField = new char[10][10];

    static String coordinateBegin;
    static String coordinateEnd;

    final private static List<String> verAddress = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");

    final private static List<String> horAddress = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    public Main(int fieldHorizontal, int fieldVertical) {
        fieldHorizontal = this.fieldHorizontal;
        fieldVertical = this.fieldVertical;
    }

    public int getFieldHorizontal() {
        return fieldHorizontal;
    }

    public int getFieldVertical() {
        return fieldVertical;
    }

    public void setFieldHorizontal(int fieldHorizontal) {
        this.fieldHorizontal = fieldHorizontal;
    }

    public void setFieldVertical(int fieldVertical) {
        this.fieldVertical = fieldVertical;
    }

    public static void main(String[] args) {
        Field();
        Coordinate();
        CurrentField();
    }

    // create field
    public static void Field() {

        for (int i = 0; i < battleField.length; i++) {
            for (int j = 0; j < battleField[0].length; j++) {
                battleField[i][j] = '~';
            }
        }
        CurrentField();
    }

    public static void Coordinate() {

        try (Scanner scanner = new Scanner(System.in)) {
            boolean check;
            do {
                check = true;
                coordinateBegin = scanner.next().toUpperCase();
                coordinateEnd = scanner.next().toUpperCase();
                if (!coordinateBegin.matches("[A-Z]\\d\\d?") || !coordinateEnd.matches("[A-Z]\\d\\d?")) {
                    System.out.println("Try again");
                    check = false;
                } else if (!verAddress.contains(coordinateBegin.substring(0, 1)) || !horAddress.contains(coordinateBegin.substring(1))) {
                    System.out.println("Change the coordinate of the begin");
                    check = false;
                } else if (!verAddress.contains(coordinateEnd.substring(0,1)) || !horAddress.contains(coordinateEnd.substring(1))) {
                    System.out.println("Change the coordinate of the end");
                    check = false;
                }
            } while (check != true);

        } catch (Exception e) {
            e.getMessage();
        }

        System.out.println("Enter the coordinates of the Aircraft Carrier (5 cells):");


        int beginVer = verAddress.indexOf(String.valueOf(coordinateBegin.charAt(0)));
        int beginHor = horAddress.indexOf(String.valueOf(coordinateBegin.charAt(1)));
        int endVer = verAddress.indexOf(String.valueOf(coordinateEnd.charAt(0)));
        int endHor = horAddress.indexOf(String.valueOf(coordinateEnd.charAt(1)));
        System.out.printf("beginHor - %d, beginVer - %d, endHor - %d, endVer - %d %n", beginHor, beginVer, endHor, endVer);

        boolean fuckCheck = false;
        if (Math.abs(beginHor - endHor) != 4 && Math.abs(beginVer - endVer) != 4) {
            System.out.println("Error! Wrong length of the Submarine! Try again:");
            fuckCheck = false;
        } else if (beginHor != endHor && beginVer != endVer) {
            System.out.println("Error! Wrong ship location! Try again:");
            fuckCheck = false;
        } else {
            battleField[beginVer][beginHor] = 'O';
            battleField[endVer][endHor] = 'O';
            fuckCheck = true;
            System.out.println(beginHor - beginVer);
            System.out.println(endHor - endVer);
        }
    }

    public static void CurrentField() {
        // display current version of field;
        System.out.print("  ");
        for (String a : horAddress) {
            System.out.print(a + " ");
        }
        System.out.println("");

        for (int i = 0; i < battleField.length; i++) {
            System.out.print(verAddress.get(i));
            for (char b : battleField[i]) {
                System.out.print(" " + b);
            }
            System.out.println();
        }
    }
}


