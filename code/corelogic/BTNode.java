/*
 * Decompiled with CFR 0_115.
 */
package corelogic;

class BTNode<T> {
    T data;
    BTNode<T> left;
    BTNode<T> right;

    BTNode(T o) {
        this.data = o;
        this.right = null;
        this.left = null;
    }
}

