/*
 * Decompiled with CFR 0_115.
 */
package ai.features;

import ai.features.Feature;
import corelogic.CheckerBoard;
import corelogic.Move;
import corelogic.Piece;
import corelogic.Player;
import corelogic.Square;
import java.io.Serializable;
import java.util.LinkedList;

public class MOB
implements Feature,
Serializable {
    double[] coeff;

    public MOB(double[] coeff) {
        this.coeff = coeff;
    }

    @Override
    public double getValue(CheckerBoard b, Player active, int phase) {
        double score = 0.0;
        for (int y = 0; y < b.getBoardSize(); ++y) {
            for (int x = 0; x < b.getBoardSize(); ++x) {
                if (b.getLocation(x, y).getPlayer() != active || b.getLocation(x, y).getPiece() == Piece.Empty) continue;
                score += (double)b.getLegalMoves(active, x, y).size();
            }
        }
        return score * this.coeff[phase];
    }
}

