package com.chess.engine.piece;
import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtil;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tiles;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    private static int[] CANDIDATE_MOVE_VECTOR_COORDINATE = {-9,-7,7,9};

    public Bishop(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance,Piecetype.BISHOP);
    }

    @Override
    public List<Move> calculatePossibleMoves(Board board) {
        List<Move> possibleMoves = new ArrayList<>();
        for(final int candidateCoordinateOffset:CANDIDATE_MOVE_VECTOR_COORDINATE){
            int coordinateOfDestination = this.piecePosition;
            while(BoardUtil.isValidCoordinate(coordinateOfDestination)){
                if(isFirstColumnExklusion(this.piecePosition,candidateCoordinateOffset)||
                   isEightColumnExklusion(this.piecePosition,candidateCoordinateOffset)){
                    break;
                }
                coordinateOfDestination +=candidateCoordinateOffset;
                if(BoardUtil.isValidCoordinate(coordinateOfDestination)){
                    final Tiles tileAtSetCoordinate = board.getTile(coordinateOfDestination);
                    if(!tileAtSetCoordinate.isOccupied()){
                        possibleMoves.add(new Move.MajorMove(this,board,coordinateOfDestination));
                    }
                    else{
                        final Piece pieceAtSetCoordinate = tileAtSetCoordinate.getPiece();
                        final Alliance AllianceOfPieceAtSetDestination = pieceAtSetCoordinate.pieceAlliance;
                        if(this.pieceAlliance != AllianceOfPieceAtSetDestination){
                            possibleMoves.add(new Move.AttackMove(this,board,coordinateOfDestination,pieceAtSetCoordinate));
                        }
                        break;
                    }
                }

            }

        }
        return ImmutableList.copyOf(possibleMoves);
    }

    private boolean isEightColumnExklusion(int piecePosition, int candidateCoordinateOffset) {
        return (BoardUtil.FIRST_COLUMN[piecePosition] && (candidateCoordinateOffset == 7 || candidateCoordinateOffset == -9));
    }

    private boolean isFirstColumnExklusion(int piecePosition, int candidateCoordinateOffset) {
        return (BoardUtil.FIRST_COLUMN[piecePosition] && (candidateCoordinateOffset == -7 || candidateCoordinateOffset == 9));
    }
    @Override
    public String toString(){
        return Piecetype.BISHOP.toString();
    }
    public Piece movePiece(Move move){
        return new Bishop(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
}
