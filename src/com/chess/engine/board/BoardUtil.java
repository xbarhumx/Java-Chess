package com.chess.engine.board;

public class BoardUtil {
    public static int NUM_TILES = 64;
    public static int NUM_TILES_PER_ROW = 8;

    public BoardUtil(){
        throw new RuntimeException("Don't instantiate this class");
    }

    public static boolean isValidCoordinate(int CoordinateOfDestination){
        return CoordinateOfDestination >=0 && CoordinateOfDestination < 64;
    }

    public static final boolean[]  FIRST_COLUMN = initColumn(0);
    public static final boolean[]  SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHT_COLUMN = initColumn(7);

    public static final boolean[] EIGHT_RANK = initRow(0);
    public static final boolean[] SEVENTH_RANK = initRow(8);
    public static final boolean[] SIXTH_RANK = initRow(16);
    public static final boolean[] FIFTH_RANK = initRow(24);
    public static final boolean[] FORTH_RANK = initRow(32);
    public static final boolean[] THIRD_RANK = initRow(40);
    public static final boolean[] SECOND_RANK = initRow(48);
    public static final boolean[] FIRST_RANK = initRow(56);

    private static boolean[] initRow(int rowCounter){
        final boolean[] row = new boolean[NUM_TILES];
        do{
            row[rowCounter] = true;
            rowCounter++;
        }while(rowCounter % NUM_TILES_PER_ROW != 0);
        return row;
    }

    private static boolean[] initColumn(int columnCounter) {
        final boolean[] column = new boolean[NUM_TILES];
        do{
            column[columnCounter] = true;
            columnCounter +=NUM_TILES_PER_ROW;
        }while(columnCounter < NUM_TILES);
        return column;
    }
}
