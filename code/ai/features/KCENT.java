/*
 * Decompiled with CFR 0_115.
 */
package ai.features;

import ai.features.Feature;
import corelogic.CheckerBoard;
import corelogic.Piece;
import corelogic.Player;
import corelogic.Square;
import java.io.Serializable;

public class KCENT
implements Feature,
Serializable {
    double[] coeff;

    public KCENT(double[] coeff) {
        this.coeff = coeff;
    }

    @Override
    public double getValue(CheckerBoard b, Player active, int phase) {
        double score = 0.0;
        if (b.getLocation(2, 5).getPlayer() != active && b.getLocation(2, 5).getPiece() == Piece.King) {
            score += 1.0;
        }
        if (b.getLocation(0, 5).getPlayer() != active && b.getLocation(0, 5).getPiece() == Piece.King) {
            score += 1.0;
        }
        if (b.getLocation(3, 4).getPlayer() != active && b.getLocation(3, 4).getPiece() == Piece.King) {
            score += 1.0;
        }
        if (b.getLocation(1, 4).getPlayer() != active && b.getLocation(1, 4).getPiece() == Piece.King) {
            score += 1.0;
        }
        if (b.getLocation(0, 3).getPlayer() != active && b.getLocation(0, 3).getPiece() == Piece.King) {
            score += 1.0;
        }
        if (b.getLocation(0, 2).getPlayer() != active && b.getLocation(0, 2).getPiece() == Piece.King) {
            score += 1.0;
        }
        if (b.getLocation(1, 2).getPlayer() != active && b.getLocation(1, 2).getPiece() == Piece.King) {
            score += 1.0;
        }
        if (b.getLocation(6, 1).getPlayer() != active && b.getLocation(6, 1).getPiece() == Piece.King) {
            score += 1.0;
        }
        return score * this.coeff[phase];
    }
}

