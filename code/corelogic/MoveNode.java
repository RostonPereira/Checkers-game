/*
 * Decompiled with CFR 0_115.
 */
package corelogic;

import corelogic.Move;
import java.util.LinkedList;

public class MoveNode {
    Move data;
    LinkedList<MoveNode> childNodes;

    MoveNode(Move move) {
        this.data = move;
        this.childNodes = new LinkedList();
    }

    public Move getMove() {
        return this.data;
    }

    public MoveNode insert(Move move) {
        MoveNode newM = new MoveNode(move);
        this.childNodes.add(newM);
        return newM;
    }

    public LinkedList<MoveNode> getChildNodes() {
        return this.childNodes;
    }

    public String toString() {
        String rtn = "MoveNode{" + this.data;
        for (MoveNode moveNode : this.childNodes) {
            rtn = rtn + ", " + moveNode;
        }
        return rtn + "}";
    }
}

