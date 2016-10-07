/*
 * Decompiled with CFR 0_115.
 */
package ai;

import ai.AIPlayer;
import ai.features.ADV;
import ai.features.DIFF;
import ai.features.Feature;
import ai.features.KCENT;
import ai.features.MOB;
import corelogic.CheckerBoard;
import corelogic.Piece;
import corelogic.Player;
import corelogic.RuleSet;
import corelogic.Square;
import runtime.Settings;

public class SamuelsStaticImpl
extends AIPlayer {
    Feature[] features = new Feature[]{new DIFF(new double[]{0.8, 0.8, 0.95}), new KCENT(new double[]{0.2, 0.85, 0.4}), new MOB(new double[]{0.2, 0.2, 0.7}), new ADV(new double[]{0.4, 0.3, 0.1})};

    public SamuelsStaticImpl(CheckerBoard board) {
        super(board);
    }

    @Override
    double stateScore(CheckerBoard b, Player p) {
        double score = 0.0;
        int phase = this.phase(b);
        for (int i = 0; i < this.features.length; ++i) {
            score += this.features[i].getValue(b, p, phase);
        }
        return score;
    }

    private int phase(CheckerBoard b) {
        int count = 0;
        for (int y = 0; y < b.getBoardSize(); ++y) {
            for (int x = 0; x < b.getBoardSize(); ++x) {
                if (b.getLocation(x, y).getPiece() == Piece.Empty) continue;
                ++count;
            }
        }
        int boundary = (int)Math.floor(this.board.getSettings().getRuleSet().startingPieces * 2 / 3);
        int p = 3 - (int)Math.floor(count / boundary);
        if (p < 0) {
            p = 0;
        }
        if (p > 2) {
            p = 2;
        }
        return p;
    }
}

