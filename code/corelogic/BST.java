/*
 * Decompiled with CFR 0_115.
 */
package corelogic;

import corelogic.BTNode;
import java.util.Stack;

public class BST<T> {
    private BTNode<T> root = null;

    public boolean insert(T i) {
        BTNode<T> parent = this.root;
        BTNode<T> child = this.root;
        boolean goneLeft = false;
        while (child != null) {
            parent = child;
            if (!goneLeft) {
                child = child.left;
                goneLeft = true;
                continue;
            }
            child = child.right;
            goneLeft = false;
        }
        if (child != null) {
            return false;
        }
        BTNode<T> leaf = new BTNode<T>(i);
        if (parent == null) {
            this.root = leaf;
        } else if (goneLeft) {
            parent.left = leaf;
        } else {
            parent.right = leaf;
        }
        return true;
    }

    public int leaves() {
        int leafCount = 0;
        if (this.root == null) {
            return leafCount;
        }
        Stack unexpandedNodes = new Stack();
        unexpandedNodes.add(this.root);
        while (unexpandedNodes.size() != 0) {
            BTNode current = (BTNode)unexpandedNodes.pop();
            ++leafCount;
            if (current.right != null) {
                unexpandedNodes.push(current.right);
            }
            if (current.left == null) continue;
            unexpandedNodes.push(current.left);
        }
        return leafCount;
    }
}

