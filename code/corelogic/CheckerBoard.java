/*
 * Decompiled with CFR 0_115.
 */
package corelogic;

import ai.AIPlayer;
import ai.NaiveImpl;
import corelogic.Move;
import corelogic.MoveNode;
import corelogic.MoveTree;
import corelogic.Piece;
import corelogic.Player;
import corelogic.RuleSet;
import corelogic.Square;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.LinkedList;
import javax.swing.SwingUtilities;
import runtime.Settings;
import ui.GameUI;

public class CheckerBoard
implements Serializable,
Runnable {
    private Square[][] board;
    private int size;
    private Settings settings;
    private Move previousMove = null;
    public Player currentGo;
    private Player nextGo;
    private AIPlayer opponent;
    private GameUI ui;
    public LinkedList<Move> pMove = new LinkedList();
    private CheckerBoard previousState;
    private boolean isRun = true;

    public CheckerBoard(Settings settings) {
        this.settings = settings;
        this.opponent = new NaiveImpl(this);
        this.size = settings.getRuleSet().boardSize;
        this.board = new Square[this.size][this.size];
        if (settings.getRuleSet().startingPieces == 20) {
            this.initialiseBoardInternationalCheckers();
        } else {
            this.initialiseBoardEnglishDraughts();
        }
    }

    @Override
    public void run() {
        while (this.getWinner() == null && this.isRun) {
            if (this.currentGo == Player.Black) {
                this.applyMove(this.opponentMove());
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        CheckerBoard.this.ui.repaint();
                    }
                });
            } else {
                Move p = this.playerMove();
                this.previousState = this.getDeepCopy();
                this.applyMove(p);
            }
            this.currentGo = this.currentGo == Player.Black ? Player.White : Player.Black;
            this.nextGo = this.currentGo == Player.Black ? Player.White : Player.Black;
        }
        Player winner = this.getWinner();
        if (winner != null) {
            if (winner == Player.White) {
                this.settings.addWin();
            } else {
                this.settings.addLoss();
            }
            this.settings.setSaveGame(null);
            this.settings.saveSettings();
        }
    }

    public void setUI(GameUI ui) {
        this.ui = ui;
    }

    public void stop() {
        this.isRun = false;
    }

    public CheckerBoard getPreviousState() {
        return this.previousState;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Move playerMove() {
        Move rtn;
        LinkedList<Move> linkedList = this.pMove;
        synchronized (linkedList) {
            try {
                while (this.pMove.isEmpty()) {
                    this.pMove.wait();
                }
            }
            catch (Exception ex) {
                // empty catch block
            }
            rtn = this.pMove.removeFirst();
        }
        return rtn;
    }

    public Settings getSettings() {
        return this.settings;
    }

    private void initialiseBoardEnglishDraughts() {
        int x;
        for (x = 0; x < this.size; ++x) {
            for (int y = 0; y < this.size; ++y) {
                this.board[x][y] = Square.EmptySquare;
            }
        }
        for (x = 1; x < this.size; x += 2) {
            this.board[x][0] = Square.StandardMan[Player.Black.ordinal()];
            this.board[x - 1][1] = Square.StandardMan[Player.Black.ordinal()];
            this.board[x][2] = Square.StandardMan[Player.Black.ordinal()];
            this.board[x - 1][this.size - 1] = Square.StandardMan[Player.White.ordinal()];
            this.board[x][this.size - 2] = Square.StandardMan[Player.White.ordinal()];
            this.board[x - 1][this.size - 3] = Square.StandardMan[Player.White.ordinal()];
        }
        this.currentGo = this.settings.getRuleSet().firstMove;
        this.nextGo = this.currentGo == Player.Black ? Player.White : Player.Black;
    }

    private void initialiseBoardInternationalCheckers() {
        int x;
        for (x = 0; x < this.size; ++x) {
            for (int y = 0; y < this.size; ++y) {
                this.board[x][y] = Square.EmptySquare;
            }
        }
        for (x = 1; x < this.size; x += 2) {
            this.board[x][0] = Square.StandardMan[Player.Black.ordinal()];
            this.board[x - 1][1] = Square.StandardMan[Player.Black.ordinal()];
            this.board[x][2] = Square.StandardMan[Player.Black.ordinal()];
            this.board[x - 1][3] = Square.StandardMan[Player.Black.ordinal()];
            this.board[x - 1][this.size - 1] = Square.StandardMan[Player.White.ordinal()];
            this.board[x][this.size - 2] = Square.StandardMan[Player.White.ordinal()];
            this.board[x - 1][this.size - 3] = Square.StandardMan[Player.White.ordinal()];
            this.board[x][this.size - 4] = Square.StandardMan[Player.White.ordinal()];
        }
        this.currentGo = this.settings.getRuleSet().firstMove;
        this.nextGo = this.currentGo == Player.Black ? Player.White : Player.Black;
    }

    public void applyMove(Move move) {
        Player currentPlayer = this.board[move.getFrom().x][move.getFrom().y].getPlayer();
        Player nextPlayer = currentPlayer == Player.Black ? Player.White : Player.Black;
        this.previousMove = move;
        this.board[move.getDestination().x][move.getDestination().y] = this.board[move.getFrom().x][move.getFrom().y];
        this.board[move.getFrom().x][move.getFrom().y] = Square.EmptySquare;
        for (int i = 1; i < move.moveChain.size(); ++i) {
            Point from = move.moveChain.get(i - 1);
            Point to = move.moveChain.get(i);
            if (Math.sqrt((from.x - to.x) * (from.x - to.x) + (from.y - to.y) * (from.y - to.y)) <= 2.0) continue;
            Point hopped = new Point((to.x + from.x) / 2, (to.y + from.y) / 2);
            if (this.board[hopped.x][hopped.y].getPlayer() != nextPlayer) continue;
            this.board[hopped.x][hopped.y] = Square.EmptySquare;
        }
        if (this.board[move.getDestination().x][move.getDestination().y].getPiece() != Piece.King) {
            int kingsRow;
            int n = kingsRow = currentPlayer == Player.Black ? this.size - 1 : 0;
            if (move.getDestination().y == kingsRow) {
                this.board[move.getDestination().x][move.getDestination().y] = Square.King[currentPlayer.ordinal()];
            }
        }
    }

    public Square getLocation(int x, int y) {
        return this.board[x][y];
    }

    public Player getWinner() {
        for (int x = 0; x < this.size; ++x) {
            for (int y = 0; y < this.size; ++y) {
                if (this.board[x][y].getPlayer() != this.currentGo || this.getLegalMoves(this.currentGo, x, y).isEmpty()) continue;
                return null;
            }
        }
        return this.nextGo;
    }

    public Move getPreviousMove() {
        return this.previousMove;
    }

    public Move getHint() {
        return this.opponent.getHint(this);
    }

    public CheckerBoard getDeepCopy() {
        CheckerBoard rtn = null;
        try {
            ByteArrayOutputStream serial = new ByteArrayOutputStream();
            ObjectOutputStream outStream = new ObjectOutputStream(serial);
            outStream.writeObject(this);
            outStream.close();
            ObjectInputStream inStream = new ObjectInputStream(new ByteArrayInputStream(serial.toByteArray()));
            rtn = (CheckerBoard)inStream.readObject();
            inStream.close();
        }
        catch (Exception ex) {
            System.err.println("Error copying board: " + ex);
        }
        return rtn;
    }

    public int getBoardSize() {
        return this.size;
    }

    public Move opponentMove() {
        return this.opponent.getBestMove(this);
    }

    public LinkedList<Move> getLegalMoves(Player p, int x, int y) {
        int direction;
        LinkedList<Move> validMoves = new LinkedList<Move>();
        if (this.board[x][y].getPiece() == Piece.Empty || this.board[x][y].getPlayer() != p) {
            return validMoves;
        }
        int n = direction = this.board[x][y].getPlayer() == Player.Black ? 1 : -1;
        if (y + direction >= 0 && y + direction < this.getBoardSize()) {
            if (x - 1 >= 0 && this.board[x - 1][y + direction].getPiece() == Piece.Empty) {
                validMoves.add(new Move(x, y, x - 1, y + direction));
            }
            if (x + 1 < this.getBoardSize() && this.board[x + 1][y + direction].getPiece() == Piece.Empty) {
                validMoves.add(new Move(x, y, x + 1, y + direction));
            }
        }
        if (this.board[x][y].getPiece() == Piece.King && y - direction >= 0 && y - direction < this.getBoardSize()) {
            if (x - 1 >= 0 && this.board[x - 1][y - direction].getPiece() == Piece.Empty) {
                validMoves.add(new Move(x, y, x - 1, y - direction));
            }
            if (x + 1 < this.getBoardSize() && this.board[x + 1][y - direction].getPiece() == Piece.Empty) {
                validMoves.add(new Move(x, y, x + 1, y - direction));
            }
        }
        for (Move m : this.getValidCaptures(p, x, y)) {
            validMoves.add(m);
        }
        return validMoves;
    }

    private LinkedList<Move> getValidCaptures(Player p, int x, int y) {
        MoveTree validCaptureTree = new MoveTree();
        validCaptureTree.insert(new Move(x, y));
        this.getValidCaptures(p, validCaptureTree, validCaptureTree.getRoot());
        LinkedList<Move> rtn = this.flattenTree(validCaptureTree.getRoot());
        return rtn;
    }

    private void getValidCaptures(Player p, MoveTree tree, MoveNode localRoot) {
        LinkedList<Move> localJumps = this.getLocalCaptures(localRoot.getMove().getDestination(), p, this.board[tree.getRoot().getMove().getFrom().x][tree.getRoot().getMove().getFrom().y].getPiece() == Piece.King);
        for (Move move : localJumps) {
            if (tree.contains(move)) continue;
            MoveNode n = localRoot.insert(move);
            this.getValidCaptures(p, tree, n);
        }
    }

    private LinkedList<Move> getLocalCaptures(Point p, Player pl, boolean isKing) {
        LinkedList<Move> movesThisLoop = new LinkedList<Move>();
        int x = p.x;
        int y = p.y;
        Player nextPl = pl == Player.Black ? Player.White : Player.Black;
        int yDirection = pl == Player.Black ? 1 : -1;
        try {
            if (this.board[x - 1][y + yDirection].getPlayer() == nextPl && this.board[x - 2][y + yDirection * 2].getPiece() == Piece.Empty) {
                movesThisLoop.add(new Move(x, y, x - 2, y + yDirection * 2));
            }
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            // empty catch block
        }
        try {
            if (this.board[x + 1][y + yDirection].getPlayer() == nextPl && this.board[x + 2][y + yDirection * 2].getPiece() == Piece.Empty) {
                movesThisLoop.add(new Move(x, y, x + 2, y + yDirection * 2));
            }
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            // empty catch block
        }
        if (this.settings.getRuleSet().menCaptureBackwards || isKing) {
            try {
                if (this.board[x - 1][y - yDirection].getPlayer() == nextPl && this.board[x - 2][y - yDirection * 2].getPiece() == Piece.Empty) {
                    movesThisLoop.add(new Move(x, y, x - 2, y - yDirection * 2));
                }
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                // empty catch block
            }
            try {
                if (this.board[x + 1][y - yDirection].getPlayer() == nextPl && this.board[x + 2][y - yDirection * 2].getPiece() == Piece.Empty) {
                    movesThisLoop.add(new Move(x, y, x + 2, y - yDirection * 2));
                }
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                // empty catch block
            }
        }
        return movesThisLoop;
    }

    LinkedList<Move> flattenTree(MoveNode root) {
        LinkedList<Move> moves = new LinkedList<Move>();
        for (MoveNode m : root.getChildNodes()) {
            Move path = new Move(root.getMove().getFrom());
            this.flattenTree(moves, m, path);
        }
        return moves;
    }

    private void flattenTree(LinkedList<Move> moves, MoveNode currentPos, Move path) {
        path.addWayPoint(currentPos.getMove().getDestination());
        moves.add(path);
        for (MoveNode m : currentPos.getChildNodes()) {
            Move newPath = new Move();
            for (Point p : path.moveChain) {
                newPath.addWayPoint(p.x, p.y);
            }
            this.flattenTree(moves, m, newPath);
        }
    }

}

