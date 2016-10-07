/*
 * Decompiled with CFR 0_115.
 */
package corelogic;

import corelogic.MustLandKings;
import corelogic.Player;
import java.io.Serializable;

public class RuleSet
implements Serializable {

    public static enum RuleSets {
        English(Player.Black, 8, false, false, true, MustLandKings.TurnStop, 12),
        International(Player.White, 10, true, true, true, MustLandKings.True, 20);
        
        public Player firstMove;
        public int boardSize;
        public boolean menCaptureBackwards;
        public boolean longRangeKings;
        public boolean mustCapture;
        public MustLandKings mustLandKings;
        public int startingPieces;

        private RuleSets(Player firstMove, int boardSize, boolean menCaptureBackwards, boolean longRangeKings, boolean mustCapture, MustLandKings mustLandKings, int startingPieces) {
            this.firstMove = firstMove;
            this.boardSize = boardSize;
            this.menCaptureBackwards = menCaptureBackwards;
            this.longRangeKings = longRangeKings;
            this.mustCapture = mustCapture;
            this.mustLandKings = mustLandKings;
            this.startingPieces = startingPieces;
        }

        public Player getFirstMove() {
            return this.firstMove;
        }

        public int getBoardSize() {
            return this.boardSize;
        }

        public boolean menCaptureBackwards() {
            return this.menCaptureBackwards;
        }

        public boolean longRangeKings() {
            return this.longRangeKings;
        }

        public boolean mustCapture() {
            return this.mustCapture;
        }

        public MustLandKings mustLandKings() {
            return this.mustLandKings;
        }

        public int startingPieces() {
            return this.startingPieces;
        }
    }

}

