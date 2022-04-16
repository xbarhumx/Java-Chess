package com.chess.engine.piece;

import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtil;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece{

    private final int[] CANDIDATE_MOVE_COORDINATE = {7,8,9,16};
    public Pawn(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance,Piecetype.PAWN);
    }

    @Override
    public List<Move> calculatePossibleMoves(Board board) {

        List<Move> possibleMoves = new ArrayList<>();
        for(final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATE){
            int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getPieceDirection()*candidateCoordinateOffset);
            if(!BoardUtil.isValidCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            if(candidateCoordinateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isOccupied()){

                //TODO Needs more work here(create class PawnMove) deal with promotion
                possibleMoves.add(new Move.MajorMove(this,board,candidateDestinationCoordinate));
            }
            else if(candidateCoordinateOffset == 16 &&
                    this.isFirstMove() &&
                    BoardUtil.SECOND_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack() ||
                    BoardUtil.SECOND_RANK[this.piecePosition] && this.pieceAlliance.isWhite()){
                int coordinateBehindCandidateCoordinate = this.piecePosition + (this.pieceAlliance.getPieceDirection() * 8);
                if(!board.getTile(coordinateBehindCandidateCoordinate).isOccupied() && !board.getTile(candidateDestinationCoordinate).isOccupied()){
                    possibleMoves.add(new Move.MajorMove(this,board,candidateDestinationCoordinate));
                }
            }

            else if(candidateCoordinateOffset == 9 && !
                    (BoardUtil.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()
                    ||BoardUtil.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())){
                if(board.getTile(candidateDestinationCoordinate).isOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(pieceOnCandidate.pieceAlliance == this.pieceAlliance){
                        //TODO more work here EN PASSANT
                        possibleMoves.add(new Move.AttackMove(this,board,candidateDestinationCoordinate,pieceOnCandidate));
                    }
                }
            }
            else if(candidateCoordinateOffset == 7 && !
                    (BoardUtil.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()
                            ||BoardUtil.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())){
                if(board.getTile(candidateDestinationCoordinate).isOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(pieceOnCandidate.pieceAlliance == this.pieceAlliance){
                        possibleMoves.add(new Move.AttackMove(this,board,candidateDestinationCoordinate,pieceOnCandidate));
                    }
                }
            }
        }


        return ImmutableList.copyOf(possibleMoves);
    }

    @Override
    public String toString(){
        return Piecetype.PAWN.toString();
    }
    public Piece movePiece(Move move){
        return new Pawn(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
}
