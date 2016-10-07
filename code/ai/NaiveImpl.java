/*
 * Decompiled with CFR 0_115.
 */
package ai;

import ai.AIPlayer;
import ai.features.DIFF;
import ai.features.Feature;
import corelogic.CheckerBoard;
import corelogic.Player;
import java.io.Serializable;

public class NaiveImpl
extends AIPlayer
implements Serializable {
    private Feature diff = new DIFF(new double[]{1.0});

    public NaiveImpl(CheckerBoard board) {
        super(board);
    }

    @Override
    double stateScore(CheckerBoard b, Player p) {
        return this.diff.getValue(b, p, 0);
    }
}

