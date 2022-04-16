package com.chess.gui;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtil;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tiles;
import com.chess.engine.piece.Piece;
import com.chess.engine.player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private final static String defaultPieceImagesPath = "fancy/";
    private Board chessBoard;
    private final JFrame gameFrame;
    private BoardPanel boardPanel;
    private static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private static Dimension TITLE_PANEL_DIMENSION = new Dimension(10,10);
    private static Color lightTileColor =  Color.decode("#FFFACD");
    private static Color darkTileColor =  Color.decode("#593E1A");
    private  Tiles sourceTile;
    private  Tiles destinationTile;
    private  Piece humanMovedPiece;

    public Table(){
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = populateMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
        boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
        this.gameFrame.setVisible(true);


    }

    public JMenuBar populateMenuBar(){
        JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;

    }

    public JMenu createFileMenu(){
        final JMenu fileMenu = new JMenu("File");
        JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.print("open up that PGN file!");
            }
        });
        fileMenu.add(openPGN);
        JMenuItem exitMenu = new JMenuItem("Exit");
        exitMenu.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {System.exit(0);};
        });
        fileMenu.add(exitMenu);
        return fileMenu;
    }

    public class BoardPanel extends JPanel{
      public List<TilePanel> boardTiles;

      public BoardPanel(){
          super(new GridLayout(8,8));
          this.boardTiles = new ArrayList<>();
          for(int i = 0; i< BoardUtil.NUM_TILES; i++){
              final TilePanel titlePanel = new TilePanel(this , i);
              this.boardTiles.add(titlePanel);
              add(titlePanel);
          }
          setPreferredSize(BOARD_PANEL_DIMENSION);
          validate();
      }

        public void drawBoard(final Board chessBoard) {

          removeAll();
          for(final TilePanel tilePanel: this.boardTiles){
              tilePanel.drawTiles(chessBoard);
              add(tilePanel);
          }
          validate();
          repaint();
        }

        public void remove_all(){
          chessBoard = null;
        }
    }

    public class TilePanel extends JPanel{
        private int tileId;
        public TilePanel(final BoardPanel boardPanel,
                         final int titleId){
            super(new GridBagLayout());
            this.tileId = titleId;
            setPreferredSize(TITLE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePiece(chessBoard);
            addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(e)) {
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if(humanMovedPiece == null){
                                sourceTile = null;
                            }
                        }else{
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = Move.MoveFactory.createMove(chessBoard,
                                                                    destinationTile.getTileCoordinate(),
                                                                     sourceTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone()){
                                chessBoard = transition.getTransitionBoard();
                            }
                            System.out.print(destinationTile.getTileCoordinate());
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable(){

                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            validate();
        }

        public void assignTileColor(){
            if(BoardUtil.EIGHT_RANK[this.tileId]||
                    BoardUtil.SIXTH_RANK[this.tileId]||
                    BoardUtil.FORTH_RANK[this.tileId]||
                    BoardUtil.SECOND_RANK[this.tileId]){
                setBackground(this.tileId % 2 == 0? lightTileColor : darkTileColor);
            }
            else if (BoardUtil.SEVENTH_RANK[this.tileId]||
                    BoardUtil.FIFTH_RANK[this.tileId]||
                    BoardUtil.THIRD_RANK[this.tileId]||
                    BoardUtil.FIRST_RANK[this.tileId]){
                setBackground(this.tileId % 2 == 0 ? darkTileColor : lightTileColor);
            }
        }

        public void assignTilePiece(Board board){
            removeAll();
            if(board.getTile(this.tileId).isOccupied()){

                try{
                    BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath+ board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0,1)
                                                                        + board.getTile(this.tileId).getPiece().toString().substring(0,1) + ".gif"));

                    add(new JLabel(new ImageIcon(image)));
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            validate();
        }

        public void drawTiles(final Board chessBoard) {
            assignTileColor();
            assignTilePiece(chessBoard);
            validate();
            repaint();
        }
    }
}
