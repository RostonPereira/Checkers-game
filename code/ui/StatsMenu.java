/*
 * Decompiled with CFR 0_115.
 */
package ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import runtime.Settings;

public class StatsMenu
extends JFrame {
    private static StatsMenu _instance = null;
    private Settings settings;
    private JLabel w;
    private JLabel l;
    private JLabel wl;
    private JLabel s;
    private JLabel lws;
    private JLabel lls;

    public static void showStats(Settings settings) {
        if (_instance == null) {
            StatsMenu stats = new StatsMenu(settings);
            stats.setVisible(true);
        }
    }

    public StatsMenu(Settings settings) {
        this.settings = settings;
        this.setResizable(false);
        this.setSize(new Dimension(250, 250));
        this.setTitle("Statistics");
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent evt) {
                StatsMenu.this.formWindowClosing(evt);
            }
        });
        GridLayout layout = new GridLayout(0, 1);
        this.getContentPane().setLayout(layout);
        this.w = new JLabel();
        this.getContentPane().add(this.w);
        this.l = new JLabel();
        this.getContentPane().add(this.l);
        this.wl = new JLabel();
        this.getContentPane().add(this.wl);
        this.s = new JLabel();
        this.getContentPane().add(this.s);
        this.lws = new JLabel();
        this.getContentPane().add(this.lws);
        this.lls = new JLabel();
        this.getContentPane().add(this.lls);
        JButton reset = new JButton("Reset Stats");
        reset.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                StatsMenu.this.settings.resetStats();
                StatsMenu.this.setValues();
            }
        });
        this.getContentPane().add(reset);
        this.setValues();
    }

    private void formWindowClosing(WindowEvent evt) {
        _instance = null;
    }

    private void setValues() {
        this.w.setText("Wins: " + this.settings.getWins());
        this.l.setText("Losses: " + this.settings.getLosses());
        this.wl.setText("Win/Loss Ratio: " + (this.settings.getLosses() > 0 ? Integer.valueOf(this.settings.getWins() / this.settings.getLosses()) : "0"));
        this.s.setText("Current Streak: " + Math.abs(this.settings.getStreak()) + (this.settings.getStreak() >= 0 ? " Wins" : " Losses"));
        this.lws.setText("Longest Winning Streak: " + this.settings.getLongestWinStreak());
        this.lls.setText("Longest Losing Streak: " + this.settings.getLongestLoseStreak());
    }

}

