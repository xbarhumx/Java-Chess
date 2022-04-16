package com.chess.engine.board;

import com.chess.engine.piece.Pawn;
import com.chess.engine.piece.Piece;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.piece.Rook;

public abstract class Move {
    final protected Piece movedPiece;
    final protected Board board;
    final protected int destinationCoordinate;

    private Move(final Piece movedPiece,
                 final Board board,
                 final int destinationCoordinate){
        this.movedPiece = movedPiece;
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getCurrentCoordinate(){
        return this.movedPiece.getPiecePosition();
    }

    public Board execute() {
        final Builder builder = new Builder();
        for(final Piece piece:this.board.currentPlayer().getActivePieces()){
            if(!piece.equals(movedPiece)){
                builder.setPiece(piece);
            }
        }
        for(final Piece piece:this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        builder.setPiece(movedPiece);
        builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }
    public abstract boolean isCastlingMove();

    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    public boolean isAttack(){
        return false;
    }

    public int hashCode(){
        int result = 1;
        final int prime = 31;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.destinationCoordinate;
        return result;
    }

    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }

        final Move otherObject = (Move) other;
        return getDestinationCoordinate() == otherObject.getDestinationCoordinate()&&
               getMovedPiece()== otherObject.getMovedPiece();
    }



    public static class MajorMove extends Move {

        public MajorMove(final Piece movedPiece,
                          final Board board,
                          final int destinationCoordinate) {
            super(movedPiece, board, destinationCoordinate);
        }

        @Override
        public boolean isCastlingMove() {
            return false;
        }
    }

    public static class AttackMove extends Move{
        private Piece attackedPiece;
        public AttackMove(final Piece movedPiece,
                           final Board board,
                           final int destinationCoordinate,
                           final Piece attackedPiece) {
            super(movedPiece, board, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }

        @Override
        public boolean isCastlingMove() {
            return false;
        }

        @Override
        public boolean isAttack(){
            return true;
        }
        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }
        @Override
        public boolean equals(Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof AttackMove)){
                return false;
            }

            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(other) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
    }

    public int getDestinationCoordinate(){
        return this.destinationCoordinate;
    }

    public static class PawnMove extends Move{


        private PawnMove(Piece movedPiece, Board board, int destinationCoordinate) {
            super(movedPiece, board, destinationCoordinate);
        }

        @Override
        public boolean isCastlingMove() {
            return false;
        }
    }

    public class PawnAttackMove extends AttackMove{

        public PawnAttackMove(Piece movedPiece, Board board, int destinationCoordinate, Piece attackedPiece) {
            super(movedPiece, board, destinationCoordinate, attackedPiece);
        }
    }

    public class PawnJump extends Move{

        private PawnJump(Piece movedPiece, Board board, int destinationCoordinate) {
            super(movedPiece, board, destinationCoordinate);
        }

        @Override
        public boolean isCastlingMove() {
            return false;
        }

        @Override
        public Board execute(){
            Builder builder = new Builder();
            for(Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!(this.getMovedPiece().equals(piece))){
                    builder.setPiece(piece);
                }
            }
            for(Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            Pawn movedPawn  = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static abstract class CastleMove extends Move{
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        public CastleMove(Piece movedPiece, Board board, int destinationCoordinate, Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(movedPiece, board, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            //TODO do more work for isFirstMove() investigate
            builder.setPiece(new Rook(this.castleRookDestination,this.castleRook.getPieceAlliance()));
            builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }




    public static  class KingSideCastleMove extends CastleMove{
        public KingSideCastleMove(Piece movedPiece, Board board, int destinationCoordinate, Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(movedPiece, board, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);

        }
    }
    public static  class QueenSideCastleMove extends CastleMove{
        public QueenSideCastleMove(Piece movedPiece, Board board, int destinationCoordinate, Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(movedPiece, board, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }
    }
    private static class NullMove extends Move{
        public NullMove(){
            super(null,null,-1);
        }
        public Board execute(){
            throw new RuntimeException("cannot execute this method!");
        }

        @Override
        public boolean isCastlingMove(){
            return false;
        }

    }

    public static abstract class MoveFactory{

        private static final Move NULL_MOVE = new NullMove();

        private MoveFactory() {
            throw new RuntimeException("Not extensible!");
        }

        public static Move createMove(Board board,
                                int destinationCoordinate,
                                int currentCoordinate){

           for(Move move : board.getAllLegalMoves()){
               if(move.getCurrentCoordinate() == currentCoordinate &&
                  move.getDestinationCoordinate() == destinationCoordinate ){
                   return move;
               }
           }
           return NULL_MOVE;
        }
    }


}
