package battleship;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static int countHitFirstPlayer;

    private static int countHitSecondPlayer;

    private static int countSankShipFirstPlayer;

    private static int countSankShipSecondPlayer;

    public static void main(String[] args) {
        System.out.println("Player 1, place your ships on the game field");
        String[][] field = printAndGetFieldOnStart(true);
        String[][] secondField = null;
        Map<Integer, Integer> mapCoordinates = new HashMap<>();
        int[] ships = new int[]{5, 4, 3, 3, 2};
        for (int temp = 0; temp < 2; temp++) {
            int counterError = 0;
            int indexShip = 0;
            if (temp == 1) {
                System.out.println("Player 2, place your ships to the game field");
                secondField = printAndGetFieldOnStart(true);
            }
            for (int i = 0; i < 5; i++) {
                int option = temp == 0 ? getCoordinatesFromUser(ships[i], field, counterError, mapCoordinates, indexShip) : getCoordinatesFromUser(ships[i], secondField, counterError, mapCoordinates, indexShip);
                if (option == 0) {
                    if (temp == 0) {
                        justPrintField(field);
                    } else justPrintField(secondField);
                } else if (option == 1) {
                    i--;
                    System.out.println("Error! Wrong length of the Submarine! Try again:");
                    counterError++;
                    continue;
                } else if (option == 2) {
                    i--;
                    System.out.println("Error! Wrong ship location! Try again:");
                    counterError++;
                    continue;
                } else if (option == 3) {
                    i--;
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    counterError++;
                    continue;
                }
                counterError = 0;
                indexShip++;
            }
            promptEnterKey();
        }
        countHitFirstPlayer = 0;
        countHitSecondPlayer = 0;
        countSankShipFirstPlayer = 0;
        countSankShipSecondPlayer = 0;
        int counter = 0;
        String[][] firstPlayerClearField = printAndGetFieldOnStart(false);
        String[][] secondPlayerClearField = printAndGetFieldOnStart(false);
        do {
            if (counter != 0) promptEnterKey();
            if (counter % 2 == 0) {
                justPrintField(firstPlayerClearField);
                System.out.println("---------------------");
                justPrintField(field);
            } else {
                justPrintField(secondPlayerClearField);
                System.out.println("---------------------");
                justPrintField(secondField);
            }
            if (counter % 2 == 0) System.out.println("Player 1, it's your turn:");
            else System.out.println("Player 2, it's your turn:");
            int error;
            do {
                if (counter % 2 == 0) error = gameIsStart(secondField, firstPlayerClearField, true);
                else error = gameIsStart(field, secondPlayerClearField, false);
            } while (error != 0);
            counter++;
        } while (countHitFirstPlayer < 17 && countHitSecondPlayer < 17);
    }

    public static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            while ((char) System.in.read() != '\n') {
                continue;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int gameIsStart(String[][] field, String[][] clearField, boolean isFirst) {
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        int indexOfDigit = -1;
        String[] splitUserInput = userInput.split("");
        String[] digits = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        boolean firsDigit = true;
        for (String s : letters) {
            if (s.equals(splitUserInput[0])) {
                firsDigit = false;
                break;
            }
        }
        String digit = "";
        String letter = "";
        if (firsDigit) {
            if (splitUserInput.length > 2) {
                for (int i = 0; i < 2; i++) {
                    digit += splitUserInput[i];
                }
                letter = splitUserInput[2];
            } else {
                digit = splitUserInput[0];
                letter = splitUserInput[1];
            }
        } else {
            if (splitUserInput.length > 2) {
                for (int i = 1; i < 3; i++) {
                    digit += splitUserInput[i];
                }
                letter = splitUserInput[0];
            } else {
                letter = splitUserInput[0];
                digit = splitUserInput[1];
            }
        }
        for (int i = 0; i < field[0].length; i++) {
            if (digit.equals(field[0][i])) {
                indexOfDigit = i;
                break;
            }
        }
        int indexOfLetter = -1;
        for (int i = 0; i < field.length; i++) {
            if (letter.equals(field[i][0])) {
                indexOfLetter = i;
                break;
            }
        }

        if (indexOfLetter == -1 || indexOfDigit == -1) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return 1;
        }

        switch (field[indexOfLetter][indexOfDigit]) {
            case "O" -> {
                if (isFirst) {
                    field[indexOfLetter][indexOfDigit] = "X";
                    clearField[indexOfLetter][indexOfDigit] = "X";
                    justPrintField(clearField);
                    countHitFirstPlayer++;
                } else {
                    field[indexOfLetter][indexOfDigit] = "X";
                    clearField[indexOfLetter][indexOfDigit] = "X";
                    justPrintField(clearField);
                    countHitSecondPlayer++;
                }
                if (!isSankShip(field, indexOfLetter, indexOfDigit)) {
                    System.out.println("You hit a ship!");
                } else {
                    if (isFirst) {
                        countSankShipFirstPlayer++;
                        if (countSankShipFirstPlayer < 5) System.out.println("You sank a ship! Specify a new target:");
                        else System.out.println("You sank the last ship. You won. Congratulations!");
                    } else {
                        countSankShipSecondPlayer++;
                        if (countSankShipSecondPlayer < 5) System.out.println("You sank a ship! Specify a new target:");
                        else System.out.println("You sank the last ship. You won. Congratulations!");
                    }
                }
            }
            case "~" -> {
                field[indexOfLetter][indexOfDigit] = "M";
                clearField[indexOfLetter][indexOfDigit] = "M";
                justPrintField(clearField);
                System.out.println("You missed!");
            }
            case "X", "M" -> {
                System.out.println("Please enter a new coordinate");
                justPrintField(clearField);
            }
        }
        return 0;
    }

    public static boolean isSankShip(String[][] field, int indexOfLetter, int indexOfDigit) {
        boolean result = true;
        if (indexOfDigit > 3) {
            if (field[indexOfLetter][indexOfDigit - 2].equals("O"))
                result = false;
        }
        if (indexOfDigit <= field[indexOfLetter].length - 3) {
            if (field[indexOfLetter][indexOfDigit + 2].equals("O"))
                result = false;
        }
        if (indexOfLetter >= 2) {
            if (field[indexOfLetter - 1][indexOfDigit].equals("O"))
                result = false;
        }
        if (indexOfLetter <= field.length - 2) {
            if (field[indexOfLetter + 1][indexOfDigit].equals("O"))
                result = false;
        }
        return result;
    }

    public static int getCoordinatesFromUser(int cell, String[][] field, int counterError, Map<Integer, Integer> mapCoordinates, int indexShip) {
        cell--;
        String[] digits = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String[] shipsNames = new String[]{"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
        if (counterError == 0)
            System.out.println("Enter the coordinates of the " + shipsNames[indexShip] + " (" + (cell + 1) + " cells):");
        Scanner scanner = new Scanner(System.in);
        String userCoordinates = scanner.nextLine();
        String[] splitUserCoordinates = userCoordinates.split(" ");
        String firstLetter = splitUserCoordinates[0].charAt(0) + "";
        String secondLetter = splitUserCoordinates[1].charAt(0) + "";
        String firstDigit = "";
        String secondDigit = "";
        for (int i = 1; i < splitUserCoordinates[0].length(); i++) {
            firstDigit += splitUserCoordinates[0].charAt(i) + "";
        }
        for (int i = 1; i < splitUserCoordinates[1].length(); i++) {
            secondDigit += splitUserCoordinates[1].charAt(i) + "";
        }
        if (!firstLetter.equals(secondLetter) && !firstDigit.equals(secondDigit)) return 2;
        boolean isSameUserInputAndCell = false;
        int firstIndex = 0;
        int secondIndex = 0;
        int justIndex = 0;
        boolean isDigit = true;
        if (!firstLetter.equals(secondLetter)) {
            isDigit = false;
            int firstIndexLetter = 0;
            int secondIndexLetter = 0;
            int counter = 0;
            for (String s : letters) {
                if (s.equals(firstLetter)) firstIndexLetter = counter;
                if (s.equals(secondLetter)) secondIndexLetter = counter;
                counter++;
            }
            if (firstIndexLetter - secondIndexLetter == cell) {
                isSameUserInputAndCell = true;
                secondIndex = firstIndexLetter;
                firstIndex = secondIndexLetter;
            }
            if (secondIndexLetter - firstIndexLetter == cell) {
                isSameUserInputAndCell = true;
                firstIndex = firstIndexLetter;
                secondIndex = secondIndexLetter;
            }
            counter = 0;
            for (String s : digits) {
                if (s.equals(firstDigit)) {
                    justIndex = counter;
                    break;
                }
                counter++;
            }
        } else {
            int firstIndexDigit = 0;
            int secondIndexDigit = 0;
            int counter = 0;
            for (String s : digits) {
                if (s.equals(firstDigit)) firstIndexDigit = counter;
                if (s.equals(secondDigit)) secondIndexDigit = counter;
                counter++;
            }
            if (firstIndexDigit - secondIndexDigit == cell) {
                isSameUserInputAndCell = true;
                firstIndex = secondIndexDigit;
                secondIndex = firstIndexDigit;
            }
            if (secondIndexDigit - firstIndexDigit == cell) {
                isSameUserInputAndCell = true;
                secondIndex = secondIndexDigit;
                firstIndex = firstIndexDigit;
            }
            counter = 0;
            for (String s : letters) {
                if (s.equals(firstLetter)) {
                    justIndex = counter;
                    break;
                }
                counter++;
            }
        }
        if (isSameUserInputAndCell) {
            boolean temp = fillField(field, justIndex, firstIndex, secondIndex, isDigit, mapCoordinates);
            if (!temp) return 3;
        } else {
            return 1;
        }
        return 0;
    }

    public static boolean fillField(String[][] field, int justIndex, int firstIndex, int secondIndex, boolean isDigit, Map<Integer, Integer> mapCoordinates) {
        if (isDigit) {
            if (firstIndex == 7) {
                firstIndex += 6;
                secondIndex += 6;
            }
            if (firstIndex >= 4 && firstIndex <= 7) {
                firstIndex += 4;
                secondIndex += 4;
            }
            if (firstIndex == 3) {
                firstIndex += 2;
                secondIndex += 2;
            }
            if (firstIndex == 2) {
                firstIndex++;
                secondIndex++;
            }
            firstIndex += 2;
            secondIndex += 3;
            justIndex++;
            int tempCounter = firstIndex;
            if (!mapCoordinates.isEmpty()) {
                for (int i = firstIndex; i < secondIndex; i++) {
                    if (field[justIndex][tempCounter].equals("~") || field[justIndex][tempCounter].equals("O")) {
                        if (field[justIndex][tempCounter].equals("O")) {
                            return false;
                        }
                        if (field.length > justIndex + 1) {
                            if (field[justIndex + 1][tempCounter].equals("O")) {
                                return false;
                            }
                        }
                        if (field[justIndex - 1][tempCounter].equals("O")) {
                            return false;
                        }
                    } else i--;
                    tempCounter++;
                }
            }
            tempCounter = firstIndex;
            for (int i = firstIndex; i < secondIndex; i++) {
                if (field[justIndex][tempCounter].equals("~")) {
                    field[justIndex][tempCounter] = "O";
                    mapCoordinates.put(justIndex, tempCounter);
                } else i--;
                tempCounter++;
            }
        } else {
            switch (justIndex) {
                case 10 -> justIndex += 10;
                case 9 -> justIndex += 9;
                case 8 -> justIndex += 8;
                case 7 -> justIndex += 7;
                case 6 -> justIndex += 6;
                case 5 -> justIndex += 5;
                case 4 -> justIndex += 4;
                case 3 -> justIndex += 3;
                case 2 -> justIndex += 2;
                case 1 -> justIndex++;
            }
            justIndex += 2;
            firstIndex++;
            secondIndex++;
            if (!mapCoordinates.isEmpty()) {
                for (int i = firstIndex; i <= secondIndex; i++) {
                    if (field[i][justIndex].equals("O")) {
                        return false;
                    }
                    if (field[i].length > justIndex + 2) {
                        if (field[i][justIndex + 2].equals("O")) {
                            return false;
                        }
                    }
                    if (justIndex - 2 >= 0) {
                        if (field[i][justIndex - 2].equals("O")) {
                            return false;
                        }
                    }
                    if (field.length > i + 1) {
                        if (field[i + 1][justIndex].equals("O"))
                            return false;
                    }
                    if (i - 1 >= 0) {
                        if (field[i - 1][justIndex].equals("O"))
                            return false;
                    }
                }
            }
            for (int i = firstIndex; i <= secondIndex; i++) {
                field[i][justIndex] = "O";
                mapCoordinates.put(i, justIndex);
            }
        }
        return true;
    }

    public static String[][] printAndGetFieldOnStart(boolean doNeedPrintField) {
        String[][] field = new String[11][22];
        field[0][0] = " ";
        field[0][1] = " ";
        int tempNumber = 1;
        for (int i = 2; i < 22; i++) {
            if (i % 2 == 0) {
                field[0][i] = tempNumber + "";
                tempNumber++;
            } else field[0][i] = " ";
        }
        char tempLetter = 'A';
        for (int i = 1; i < 11; i++) {
            field[i][0] = tempLetter + "";
            tempLetter++;
            field[i][1] = " ";
        }
        for (int i = 1; i < 11; i++) {
            for (int j = 2; j < 22; j++) {
                if (j % 2 == 0) field[i][j] = "~";
                else field[i][j] = " ";
            }
        }
        if (doNeedPrintField)
            justPrintField(field);
        return field;
    }

    public static void justPrintField(String[][] field) {
        for (String[] strings : field) {
            for (String s : strings) {
                System.out.print(s);
            }
            System.out.println();
        }
    }
}
