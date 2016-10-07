/*
 * Decompiled with CFR 0_115.
 */
package corelogic;

import corelogic.Piece;
import corelogic.Player;
import java.io.Serializable;

public final class Square
implements Serializable {
    public static Square EmptySquare = new Square(null, Piece.Empty, 0);
    public static Square[] StandardMan = new Square[]{new Square(Player.Black, Piece.StandardMan, 1), new Square(Player.White, Piece.StandardMan, 2)};
    public static Square[] King = new Square[]{new Square(Player.Black, Piece.King, 3), new Square(Player.White, Piece.King, 4)};
    public static Square[] All = new Square[]{EmptySquare, StandardMan[0], StandardMan[1], King[0], King[1]};
    private Player player;
    private Piece piece;
    private int value;

    private Square(Player player, Piece piece, int value) {
        this.player = player;
        this.piece = piece;
        this.value = value;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public int getValue() {
        return this.value;
    }
}

