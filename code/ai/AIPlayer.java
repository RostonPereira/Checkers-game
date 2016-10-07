/*
 * Decompiled with CFR 0_115.
 */
package ai;

import corelogic.CheckerBoard;
import corelogic.Move;
import corelogic.Player;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import runtime.Settings;

public abstract class AIPlayer
implements Serializable {
    CheckerBoard board;
    private static Random r = new Random(System.currentTimeMillis());
    LinkedList<Move> bestMove;

    public AIPlayer(CheckerBoard board) {
        this.board = board;
    }

    public Move getBestMove(CheckerBoard board) {
        return this.run(true);
    }

    public Move getHint(CheckerBoard board) {
        return this.run(false);
    }

    private Move run(boolean isAI) {
        this.bestMove = new LinkedList();
        this.getBestMoves(this.board, null, this.board.getSettings().getDifficulty().getValue(), -1.7976931348623157E308, Double.MAX_VALUE, true, isAI ? Player.Black : Player.White, Player.Black);
        return this.bestMove.get(r.nextInt(this.bestMove.size()));
    }

    private double getBestMoves(CheckerBoard state, Move move, int depth, double alpha, double beta, boolean maxPlayer, Player currentP, Player rootPlayer) {
        CheckerBoard newState;
        Move t;
        boolean isRoot = false;
        if (move != null) {
            newState = state.getDeepCopy();
            newState.applyMove(move);
        } else {
            newState = state;
            isRoot = true;
        }
        Player winner = newState.getWinner();
        if (winner != null) {
            if (winner == rootPlayer) {
                return 100.0;
            }
            if (winner != rootPlayer) {
                return -100.0;
            }
        }
        if (depth <= 0) {
            return this.stateScore(newState, currentP);
        }
        if (maxPlayer) {
            for (Move t2 : this.getLegalMovesFor(currentP, newState)) {
                double r = this.getBestMoves(newState, t2, depth - 1, alpha, beta, false, currentP == Player.Black ? Player.White : Player.Black, Player.Black);
                if (r >= alpha) {
                    if (isRoot) {
                        if (r > alpha) {
                            this.bestMove.clear();
                        }
                        this.bestMove.add(t2);
                    }
                    alpha = r;
                }
                if (beta > alpha) continue;
                break;
            }
            return alpha;
        }
        Iterator<Move> i$ = this.getLegalMovesFor(currentP, newState).iterator();
        while (i$.hasNext() && (beta = Math.min(beta, this.getBestMoves(newState, t = i$.next(), depth - 1, alpha, beta, true, currentP == Player.Black ? Player.White : Player.Black, Player.Black))) > alpha) {
        }
        return beta;
    }

    private LinkedList<Move> getLegalMovesFor(Player p, CheckerBoard c) {
        LinkedList<Move> rtn = new LinkedList<Move>();
        for (int y = 0; y < c.getBoardSize(); ++y) {
            for (int x = 0; x < c.getBoardSize(); ++x) {
                for (Move t : c.getLegalMoves(p, x, y)) {
                    rtn.add(t);
                }
            }
        }
        return rtn;
    }

    abstract double stateScore(CheckerBoard var1, Player var2);

    public static enum Difficulty {
        Easy(2),
        Medium(4),
        Hard(6);
        
        private int ply;

        private Difficulty(int ply) {
            this.ply = ply;
        }

        public int getValue() {
            return this.ply;
        }
    }

}

