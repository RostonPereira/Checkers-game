/*
 * Decompiled with CFR 0_115.
 */
package ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import ui.GameUI;

public class GameMouseListener
implements MouseListener {
    private GameUI game;

    public GameMouseListener(GameUI game) {
        this.game = game;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        this.game.clicked(me.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}

