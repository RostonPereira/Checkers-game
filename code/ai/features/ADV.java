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

public class ADV
implements Feature,
Serializable {
    double[] coeff;

    public ADV(double[] coeff) {
        this.coeff = coeff;
    }

    @Override
    public double getValue(CheckerBoard b, Player active, int phase) {
        double score = 0.0;
        for (int y = 2; y < 6; ++y) {
            for (int x = 0; x < b.getBoardSize(); ++x) {
                if (b.getLocation(x, y).getPiece() == Piece.Empty || b.getLocation(x, y).getPlayer() == active) continue;
                score += y == 2 || y == 3 ? 1.0 : -1.0;
            }
        }
        return score * this.coeff[phase];
    }
}

