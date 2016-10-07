/*
 * Decompiled with CFR 0_115.
 */
package corelogic;

import corelogic.Move;
import corelogic.MoveNode;
import java.awt.Point;
import java.io.PrintStream;
import java.util.LinkedList;

public class MoveTree {
    private MoveNode root = null;

    public MoveNode insert(Move move) {
        if (this.root == null) {
            this.root = new MoveNode(move);
            return this.root;
        }
        return this.insert(this.root, move);
    }

    public MoveNode insert(MoveNode parent, Move move) {
        return parent.insert(move);
    }

    public boolean contains(Move check) {
        for (MoveNode n : this.getRoot().getChildNodes()) {
            if (!this.contains(check, n)) continue;
            return true;
        }
        return false;
    }

    private boolean contains(Move check, MoveNode r) {
        if (r.getMove().getFrom().equals(check.getFrom()) && r.getMove().getDestination().equals(check.getDestination()) || r.getMove().getFrom().equals(check.getDestination()) && r.getMove().getDestination().equals(check.getFrom())) {
            return true;
        }
        for (MoveNode n : r.getChildNodes()) {
            if (!this.contains(check, n)) continue;
            return true;
        }
        return false;
    }

    public MoveNode getRoot() {
        return this.root;
    }

    public LinkedList<MoveNode> getLeaves() {
        LinkedList<MoveNode> leaves = new LinkedList<MoveNode>();
        this.getLeaves(leaves, this.getRoot());
        return leaves;
    }

    private void getLeaves(LinkedList<MoveNode> leaves, MoveNode root) {
        LinkedList<MoveNode> nodes = root.getChildNodes();
        if (nodes.isEmpty()) {
            leaves.add(root);
            return;
        }
        for (MoveNode m : nodes) {
            this.getLeaves(leaves, m);
        }
    }

    public String toString() {
        return "MoveTree{root=" + this.root + '}';
    }

    public static void main(String[] args) {
        MoveTree tree = new MoveTree();
        tree.insert(new Move(1, 2, 3, 0));
        tree.insert(new Move(3, 0, 1, 6));
        tree.insert(new Move(3, 0, 5, 6));
        tree.insert(new Move(5, 6, 7, 7));
        System.out.println(tree);
    }
}

