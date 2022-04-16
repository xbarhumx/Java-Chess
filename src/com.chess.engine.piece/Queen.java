package com.chess.engine.piece;
import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtil;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tiles;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{
    private final int[] CANDIDATE_MOVE_VECTOR_COORDINATE = {-9,-8,-7,-1,1,7,8,9};
    public Queen(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance,Piecetype.QUEEN);
    }

    @Override
    public List<Move> calculatePossibleMoves(Board board) {
        List<Move> possibleMove = new ArrayList<>();
        for(final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATE){
            int coordinateDestination = this.piecePosition;
            while(BoardUtil.isValidCoordinate(coordinateDestination)){
                if(isFirstColumnExklusion(this.piecePosition,candidateCoordinateOffset)||
                   isEightColumnExklusion(this.piecePosition,candidateCoordinateOffset)){
                    break;
                }

                coordinateDestination += candidateCoordinateOffset;
                if(BoardUtil.isValidCoordinate(coordinateDestination)){
                    final Tiles tileAtSetCoordinate = board.getTile(coordinateDestination);
                    if(!tileAtSetCoordinate.isOccupied()){
                        possibleMove.add(new Move.MajorMove(this,board,coordinateDestination));
                    }
                    else{
                        final Piece pieceAtSetCoordinate = tileAtSetCoordinate.getPiece();
                        final Alliance AllianceOfPieceAtSetDestination = pieceAtSetCoordinate.pieceAlliance;
                        if(this.pieceAlliance != AllianceOfPieceAtSetDestination){
                            possibleMove.add(new Move.AttackMove(this,board,coordinateDestination,pieceAtSetCoordinate));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(possibleMove);
    }

    private boolean isEightColumnExklusion(int piecePosition, int coordinateDestination) {
        return (BoardUtil.FIRST_COLUMN[piecePosition]&&(coordinateDestination == 7
                                                        || coordinateDestination == -9
                                                        || coordinateDestination == -1 ));
    }

    private boolean isFirstColumnExklusion(int piecePosition, int coordinateDestination) {
        return (BoardUtil.FIRST_COLUMN[piecePosition]&&(coordinateDestination == -7
                || coordinateDestination == 9
                || coordinateDestination == 1 ));
    }
    @Override
    public String toString(){
        return Piecetype.QUEEN.toString();
    }
    public Piece movePiece(Move move){
        return new Queen(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
}
