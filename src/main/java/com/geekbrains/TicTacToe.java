package com.geekbrains;

import java.util.Scanner;

public class TicTacToe {
    private static final int SIZE = 3;
    private static final char DOT_X = 'X';
    private static final char DOT_O = 'O';
    private static final int DOTS_TO_WIN = 3;
    private static final char DOT_EMPTY = '.';
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final char[][] MAP = new char[SIZE][SIZE];

    //Ради интереса реализован алгоритм minimax, но он долго работает на поле больше 3-х
    public static void main(String[] args) {

        initMap();
        printMap();

        while (true) {
            humanTurn();
            if (checkWin(DOT_X)) {
                printMap();
                System.out.println("Человек победил");
                break;
            }

            if (isMapFull()) {
                printMap();
                System.out.println("Ничья");
                break;
            }

            aiTurn();
            printMap();
            if (checkWin(DOT_O)) {
                System.out.println("AI победил");
                break;
            }

            if (isMapFull()) {
                System.out.println("Ничья");
                break;
            }
        }

    }

    private static void humanTurn() {
        int x, y;
        do {
            System.out.print("Введите X:");
            x = SCANNER.nextInt() - 1;
            System.out.print("Введите Y:");
            y = SCANNER.nextInt() - 1;
        } while (!isValidCell(x, y));
        MAP[y][x] = DOT_X;
    }

    private static void aiTurn() {
        int x = -1, y = -1;

        int max_score = Integer.MIN_VALUE;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidCell(j, i)) {
                    MAP[i][j] = DOT_O;
                    int score = minimax(DOT_X);
                    MAP[i][j] = DOT_EMPTY;

                    if (score > max_score) {
                        max_score = score;
                        x = j;
                        y = i;
                    }
                }
            }
        }

        if (isValidCell(x, y)) MAP[y][x] = DOT_O;

    }

    private static int minimax(char symbol) {
        if (checkWin(DOT_X)) {
            //победил человек
            return -1;
        } else if (checkWin(DOT_O)) {
            //победил AI
            return 1;
        } else if (isMapFull()) {
            //Ничья
            return 0;
        }

        int max_score;
        if (symbol == DOT_O) {
            //ход AI - нужен выгодный ход
            max_score = Integer.MIN_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (isValidCell(j, i)) {
                        MAP[i][j] = DOT_O;
                        int score = minimax(DOT_X);
                        MAP[i][j] = DOT_EMPTY;
                        max_score = Integer.max(max_score, score);
                    }
                }
            }
        } else {
            //человек выберет невыгодный ход для AI
            max_score = Integer.MAX_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (isValidCell(j, i)) {
                        MAP[i][j] = DOT_X;
                        int score = minimax(DOT_O);
                        MAP[i][j] = DOT_EMPTY;
                        max_score = Integer.min(max_score, score);
                    }
                }
            }
        }

        return max_score;
    }

    public static boolean checkWin(char symbol) {
        //Перебираем все возможные вписанные квадраты, чтобы найти победу
        //Метод сработает и для поля 5X5 с линией из 4х элементов

        for (int y = 0; y <= SIZE - DOTS_TO_WIN; y++) {
            for (int x = 0; x <= SIZE - DOTS_TO_WIN; x++) {
                //Линий по горизонтали
                for (int i = y; i < y + DOTS_TO_WIN; i++) {
                    int score = 0;
                    for (int j = x; j < x + DOTS_TO_WIN; j++) {
                        if (MAP[i][j] == symbol) score++;
                    }
                    if (score == DOTS_TO_WIN) return true;
                }

                //Линий по вертикали
                for (int i = x; i < x + DOTS_TO_WIN; i++) {
                    int score = 0;
                    for (int j = y; j < y + DOTS_TO_WIN; j++) {
                        if (MAP[j][i] == symbol) score++;
                    }
                    if (score == DOTS_TO_WIN) return true;
                }

                // проверяем обе диагонали
                int scoreD1 = 0;
                int scoreD2 = 0;
                for (int i = 0; i < DOTS_TO_WIN; i++) {
                    //Диагональ - верхний левый - правый нижний
                    if (MAP[y + i][x + i] == symbol)
                        scoreD1++;
                    //Диагональ - нижний левый - правый верхний
                    if (MAP[y + i][x + DOTS_TO_WIN - i - 1] == symbol) scoreD2++;

                    if (scoreD1 == DOTS_TO_WIN || scoreD2 == DOTS_TO_WIN) return true;
                }
            }
        }
        return false;
    }

    private static boolean isValidCell(int x, int y) {
        if ((x > MAP.length) || (x < 0) || (y > MAP.length) || (y < 0)) return false;

        //x и y точно внутри игрового поля, так что проверяем, что поле не занято
        return MAP[y][x] == DOT_EMPTY;

    }

    private static boolean isMapFull() {
        for (int y = 0; y < MAP.length; y++) {
            for (int x = 0; x < MAP.length; x++) {
                if (MAP[x][y] == DOT_EMPTY) return false;
            }
        }
        return true;
    }

    private static void initMap() {
        for (int y = 0; y < MAP.length; y++) {
            for (int x = 0; x < MAP.length; x++) {
                MAP[x][y] = DOT_EMPTY;
            }
        }
    }


    private static void printMap() {
        for (int x = 0; x <= MAP.length; x++) {
            System.out.print(x + " ");
        }
        System.out.println();

        for (int y = 0; y < MAP.length; y++) {
            System.out.print((y + 1) + " ");
            for (int x = 0; x < MAP.length; x++) {
                System.out.print(MAP[y][x] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}