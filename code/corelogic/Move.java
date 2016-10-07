/*
 * Decompiled with CFR 0_115.
 */
package corelogic;

import java.awt.Point;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

public final class Move
implements Serializable {
    public LinkedList<Point> moveChain = new LinkedList();

    public Move() {
    }

    public Move(int fromX, int fromY) {
        this.moveChain.add(new Point(fromX, fromY));
    }

    public Move(int fromX, int fromY, int destinationX, int destinationY) {
        this.moveChain.add(new Point(fromX, fromY));
        this.moveChain.add(new Point(destinationX, destinationY));
    }

    public Move(Point from) {
        this.moveChain.add(from);
    }

    public Move(Point from, Point destination) {
        this.moveChain.add(from);
        this.moveChain.add(destination);
    }

    public Point getFrom() {
        return this.moveChain.getFirst();
    }

    public Point getDestination() {
        return this.moveChain.getLast();
    }

    public void addWayPoint(int x, int y) {
        this.addWayPoint(new Point(x, y));
    }

    public void addWayPoint(Point point) {
        this.moveChain.add(point);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Move move = (Move)o;
        if (!this.getDestination().equals(move.getDestination())) {
            return false;
        }
        if (!this.getFrom().equals(move.getFrom())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = Arrays.hashCode(new int[]{this.getFrom().x, this.getFrom().y});
        result = 31 * result + Arrays.hashCode(new int[]{this.getDestination().x, this.getDestination().y});
        return result;
    }

    public String toString() {
        return "Move{(" + this.getFrom().getX() + "," + this.getFrom().getY() + ") --> (" + this.getDestination().getX() + "," + this.getDestination().getY() + ")}";
    }

    public static void main(String[] args) {
        Move temp = new Move(1, 2, 3, 4);
        System.out.println(temp);
    }
}

