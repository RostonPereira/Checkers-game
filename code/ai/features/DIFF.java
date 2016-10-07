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

public class DIFF
implements Feature,
Serializable {
    double[] coeff;

    public DIFF(double[] coeff) {
        this.coeff = coeff;
    }

    @Override
    public double getValue(CheckerBoard b, Player active, int phase) {
        double score = 0.0;
        for (int y = 0; y < b.getBoardSize(); ++y) {
            for (int x = 0; x < b.getBoardSize(); ++x) {
                if (b.getLocation(x, y).getPiece() == Piece.Empty) continue;
                score += b.getLocation(x, y).getPlayer() == active ? (double)(3 + (b.getLocation(x, y).getPiece() == Piece.King ? 2 : 0)) : (double)(-3 - (b.getLocation(x, y).getPiece() == Piece.King ? 2 : 0));
            }
        }
        return score * this.coeff[phase];
    }
}

