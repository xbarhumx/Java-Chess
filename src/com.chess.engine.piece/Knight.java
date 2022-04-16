package com.chess.engine.piece;
import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtil;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tiles;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece{

    private static int[] CANIDATE_MOVES = {-17,-15,-10,-6,6,10,15,17};


    public Knight(int pieceCoordinate, Alliance pieceAlliance){
        super(pieceCoordinate,pieceAlliance,Piecetype.KNIGHT);
    }


    @Override
    public List<Move> calculatePossibleMoves(Board board) {
        List<Move> possibleMoves = new ArrayList<>();
        int CoordinateOfDestination;
        for(int currentCandidate : CANIDATE_MOVES){
            CoordinateOfDestination = this.piecePosition + currentCandidate;
            if(firstColumnExklusion(this.piecePosition,currentCandidate)||
                    secondColumnExklusion(this.piecePosition,currentCandidate)||
                    seventhColumnExklusion(this.piecePosition,currentCandidate)||
                    eightColumnExklusion(this.piecePosition,currentCandidate)){
                continue;
            }
            if(BoardUtil.isValidCoordinate(CoordinateOfDestination)){
                final Tiles tileAtSetCoordinate = board.getTile(CoordinateOfDestination);
                if(!tileAtSetCoordinate.isOccupied()){
                    possibleMoves.add(new Move.MajorMove(this,board,CoordinateOfDestination));
                }
                else{
                    final Piece pieceAtSetCoordinate = tileAtSetCoordinate.getPiece();
                    final Alliance AllianceOfPieceAtSetDestination = pieceAtSetCoordinate.pieceAlliance;
                    if(this.pieceAlliance != AllianceOfPieceAtSetDestination){
                        possibleMoves.add(new Move.AttackMove(this,board,CoordinateOfDestination,pieceAtSetCoordinate));
                    }
                }
            }
        }

        return ImmutableList.copyOf(possibleMoves);
    }
    public boolean firstColumnExklusion(int piecePosition,int currentCandidate){
        return (BoardUtil.FIRST_COLUMN[piecePosition]&&(currentCandidate==-17
                            || currentCandidate==-10
                            || currentCandidate==6
                            || currentCandidate == 15));
    }
    public boolean secondColumnExklusion(int piecePosition,int currentCandidate){
        return (BoardUtil.SECOND_COLUMN[piecePosition]&&
                (currentCandidate==10
                || currentCandidate==-6));
    }
    public boolean seventhColumnExklusion(int piecePosition,int currentCandidate){
        return (BoardUtil.SEVENTH_COLUMN[piecePosition]&&
                (currentCandidate == 6 ||
                 currentCandidate == -10));
    }
    public boolean eightColumnExklusion(int piecePosition,int currentCandidate){
        return (BoardUtil.EIGHT_COLUMN[piecePosition]&&(currentCandidate==17
                || currentCandidate==10
                || currentCandidate==-6
                || currentCandidate == -15));
    }

    @Override
    public String toString(){
        return Piecetype.KNIGHT.toString();
    }
    public Piece movePiece(Move move){
        return new Knight(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
}
