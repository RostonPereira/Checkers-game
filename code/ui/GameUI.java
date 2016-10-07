/*
 * Decompiled with CFR 0_115.
 */
package ui;

import corelogic.CheckerBoard;
import corelogic.Move;
import corelogic.Player;
import corelogic.Square;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseListener;
import java.awt.geom.Path2D;
import java.util.LinkedList;
import javax.swing.JPanel;
import runtime.Settings;
import ui.GameMouseListener;

public class GameUI
extends JPanel {
    private CheckerBoard board;
    private CheckerBoard last = null;
    private int tileSize;
    private Point offset;
    private Point moveStart;
    private Point selectedPiece;
    private Move hint = null;
    private State state;
    private LinkedList<Move> moves;
    private LinkedList<Point> highlightSquares;
    private Settings settings;

    public GameUI(Settings settings) {
        this.settings = settings;
        this.setPreferredSize(new Dimension(600, 550));
        this.tileSize = 60;
        this.offset = new Point(50, 50);
        this.addMouseListener(new GameMouseListener(this));
        this.setupGame(false);
    }

    public void setupGame(boolean forceNewGame) {
        if (this.settings.getSavedGame() == null || forceNewGame) {
            this.settings.setSaveGame(new CheckerBoard(this.settings));
            this.settings.getSavedGame().setUI(this);
            new Thread(this.settings.getSavedGame()).start();
        }
        this.board = this.settings.getSavedGame();
        this.highlightSquares = new LinkedList();
        this.state = State.Select;
        this.moveStart = null;
        this.selectedPiece = null;
        this.hint = null;
        this.repaint();
    }

    public void undo() {
        CheckerBoard previous = this.board.getPreviousState();
        if (previous != null) {
            this.board.stop();
            this.board = previous;
            this.settings.setSaveGame(this.board);
            new Thread(this.settings.getSavedGame()).start();
            this.repaint();
        }
    }

    public void showHint() {
        this.hint = this.board.getHint();
        this.repaint();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clicked(Point clickedPoint) {
        if (this.board.currentGo != Player.White) {
            return;
        }
        int xTile = (clickedPoint.x - this.offset.x) / this.tileSize;
        int yTile = (int)((double)(clickedPoint.y - this.offset.y) / ((double)this.tileSize * 0.8));
        if (xTile >= 0 && xTile < this.board.getBoardSize() && yTile >= 0 && yTile < this.board.getBoardSize()) {
            switch (this.state) {
                case Select: {
                    if (this.board.getLocation(xTile, yTile).getPlayer() != Player.White) break;
                    this.selectPiece(xTile, yTile);
                    break;
                }
                case Move: {
                    Point newPoint = new Point(xTile, yTile);
                    boolean moved = false;
                    for (Move m : this.moves) {
                        if (!this.moveStart.equals(m.getFrom()) || !newPoint.equals(m.getDestination())) continue;
                        LinkedList<Move> linkedList = this.board.pMove;
                        synchronized (linkedList) {
                            this.board.pMove.add(m);
                            this.board.pMove.notify();
                        }
                        this.finaliseMove();
                        moved = true;
                        this.hint = null;
                        break;
                    }
                    this.finaliseMove();
                    if (moved) break;
                    this.selectPiece(xTile, yTile);
                }
            }
        }
    }

    private void selectPiece(int xTile, int yTile) {
        this.moves = this.board.getLegalMoves(Player.White, xTile, yTile);
        if (this.moves.size() != 0) {
            for (Move m : this.moves) {
                this.highlightSquares.add(m.getDestination());
            }
            this.moveStart = new Point(xTile, yTile);
            this.state = State.Move;
            this.selectedPiece = new Point(xTile, yTile);
            this.repaint();
        }
    }

    private void finaliseMove() {
        if (this.state == State.Move) {
            this.moves = null;
            this.highlightSquares.clear();
            this.state = State.Select;
            this.selectedPiece = null;
            this.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        super.repaint();
        int xSize = this.getWidth() - 100;
        int ySize = this.getHeight() - 100;
        ySize = (int)((double)ySize / ((double)this.board.getBoardSize() * 0.8));
        this.tileSize = Math.min(xSize /= this.board.getBoardSize(), ySize);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        Move previous = this.board.getPreviousMove();
        for (int x = 0; x < this.board.getBoardSize(); ++x) {
            block7 : for (int y = 0; y < this.board.getBoardSize(); ++y) {
                g.setColor(y % 2 == x % 2 ? this.settings.getWhiteSquareColor() : this.settings.getBlackSquareColor());
                Point p = new Point(x, y);
                if (previous != null) {
                    if (p.equals(previous.getFrom())) {
                        g.setColor(Color.RED);
                    } else {
                        for (Point m : previous.moveChain) {
                            if (!p.equals(m)) continue;
                            g.setColor(Color.ORANGE);
                        }
                    }
                }
                if (this.hint != null && (p.equals(this.hint.getFrom()) || p.equals(this.hint.getDestination()))) {
                    g.setColor(Color.yellow);
                }
                if (this.selectedPiece != null && this.selectedPiece.x == x && this.selectedPiece.y == y) {
                    g.setColor(Color.CYAN);
                }
                if (this.highlightSquares.contains(p)) {
                    g.setColor(Color.GREEN);
                }
                g.fillRect(x * this.tileSize + this.offset.x, (int)((double)(y * this.tileSize) * 0.8 + (double)this.offset.y), this.tileSize, (int)((double)this.tileSize * 0.8));
                g.setColor(Color.BLACK);
                g.drawRect(x * this.tileSize + this.offset.x, (int)((double)(y * this.tileSize) * 0.8 + (double)this.offset.y), this.tileSize, (int)((double)this.tileSize * 0.8));
                switch (this.board.getLocation(x, y).getValue()) {
                    case 1: {
                        this.drawPiece(g, this.settings.getBlackPieceColor(), this.settings.getWhitePieceColor(), x, y);
                        continue block7;
                    }
                    case 2: {
                        this.drawPiece(g, this.settings.getWhitePieceColor(), this.settings.getBlackPieceColor(), x, y);
                        continue block7;
                    }
                    case 3: {
                        this.drawKing(g, this.settings.getBlackPieceColor(), this.settings.getWhitePieceColor(), x, y);
                        continue block7;
                    }
                    case 4: {
                        this.drawKing(g, this.settings.getWhitePieceColor(), this.settings.getBlackPieceColor(), x, y);
                    }
                }
            }
        }
        Player winner = this.board.getWinner();
        if (winner != null) {
            g.drawString((Object)((Object)winner) + " wins!", this.getWidth() / 2, this.getHeight() - 35);
        }
    }

    private void drawPiece(Graphics g, Color mainColour, Color outlineColour, int x, int y) {
        Path2D.Float piece = new Path2D.Float();
        Path2D.Float extraCurve = new Path2D.Float();
        double bottomLeftX = (double)(x * this.tileSize + this.offset.x) + (double)this.tileSize * 0.1;
        double bottomLeftY = (double)(y * this.tileSize) * 0.8 + (double)this.offset.y + (double)this.tileSize * 0.55;
        double bottomRightX = bottomLeftX + (double)this.tileSize * 0.8;
        double bottomRightY = bottomLeftY;
        double topRightX = bottomRightX;
        double topRightY = bottomRightY - (double)this.tileSize * 0.25;
        double topLeftX = topRightX - (double)this.tileSize * 0.8;
        double topLeftY = topRightY;
        piece.moveTo(bottomLeftX, bottomLeftY);
        piece.curveTo(bottomLeftX, bottomLeftY + (double)this.tileSize * 0.2, bottomRightX, bottomRightY + (double)this.tileSize * 0.2, bottomRightX, bottomRightY);
        piece.lineTo(topRightX, topRightY);
        piece.curveTo(topRightX, topRightY - (double)this.tileSize * 0.2, topLeftX, topLeftY - (double)this.tileSize * 0.2, topLeftX, topLeftY);
        piece.lineTo(bottomLeftX, bottomLeftY);
        piece.moveTo(bottomLeftX, bottomLeftY);
        piece.closePath();
        extraCurve.moveTo(topLeftX, topLeftY);
        extraCurve.curveTo(topLeftX, topLeftY + (double)this.tileSize * 0.2, topRightX, topRightY + (double)this.tileSize * 0.2, topRightX, topRightY);
        extraCurve.moveTo(topLeftX, topLeftY);
        extraCurve.closePath();
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(mainColour);
        g2d.fill(piece);
        g2d.setColor(outlineColour);
        g2d.draw(piece);
        g2d.draw(extraCurve);
    }

    private void drawKing(Graphics g, Color mainColour, Color outlineColour, int x, int y) {
        Path2D.Float piece = new Path2D.Float();
        Path2D.Float extraCurve1 = new Path2D.Float();
        Path2D.Float extraCurve2 = new Path2D.Float();
        double bottomLeftX = (double)(x * this.tileSize + this.offset.x) + (double)this.tileSize * 0.1;
        double bottomLeftY = (double)(y * this.tileSize) * 0.8 + (double)this.offset.y + (double)this.tileSize * 0.6;
        double bottomRightX = bottomLeftX + (double)this.tileSize * 0.8;
        double bottomRightY = bottomLeftY;
        double topRightX = bottomRightX;
        double topRightY = bottomRightY - (double)this.tileSize * 0.44;
        double topLeftX = topRightX - (double)this.tileSize * 0.8;
        double topLeftY = topRightY;
        double middleLeftX = bottomLeftX;
        double middleLeftY = bottomLeftY - (double)this.tileSize * 0.22;
        double middleRightX = bottomRightX;
        double middleRightY = bottomRightY - (double)this.tileSize * 0.22;
        piece.moveTo(bottomLeftX, bottomLeftY);
        piece.curveTo(bottomLeftX, bottomLeftY + (double)this.tileSize * 0.2, bottomRightX, bottomRightY + (double)this.tileSize * 0.2, bottomRightX, bottomRightY);
        piece.lineTo(topRightX, topRightY);
        piece.curveTo(topRightX, topRightY - (double)this.tileSize * 0.2, topLeftX, topLeftY - (double)this.tileSize * 0.2, topLeftX, topLeftY);
        piece.lineTo(bottomLeftX, bottomLeftY);
        piece.moveTo(bottomLeftX, bottomLeftY);
        piece.closePath();
        extraCurve1.moveTo(topLeftX, topLeftY);
        extraCurve1.curveTo(topLeftX, topLeftY + (double)this.tileSize * 0.2, topRightX, topRightY + (double)this.tileSize * 0.2, topRightX, topRightY);
        extraCurve1.moveTo(topLeftX, topLeftY);
        extraCurve1.closePath();
        extraCurve2.moveTo(middleLeftX, middleLeftY);
        extraCurve2.curveTo(middleLeftX, middleLeftY + (double)this.tileSize * 0.2, middleRightX, middleRightY + (double)this.tileSize * 0.2, middleRightX, middleRightY);
        extraCurve2.moveTo(middleLeftX, middleLeftY);
        extraCurve2.closePath();
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(mainColour);
        g2d.fill(piece);
        g2d.setColor(outlineColour);
        g2d.draw(piece);
        g2d.draw(extraCurve1);
        g2d.draw(extraCurve2);
    }

    private static enum State {
        Select,
        Move;
        

        private State() {
        }
    }

}

