/*
 * Decompiled with CFR 0_115.
 */
package ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class AboutScreen
extends JFrame {
    private static AboutScreen _instance = null;

    public static void showAbout() {
        if (_instance == null) {
            AboutScreen about = new AboutScreen();
            about.setVisible(true);
        }
    }

    private AboutScreen() {
        this.setResizable(false);
        this.setSize(new Dimension(250, 250));
        this.setTitle("About");
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent evt) {
                AboutScreen.this.formWindowClosing(evt);
            }
        });
        GridLayout layout = new GridLayout(0, 1);
        this.getContentPane().setLayout(layout);
        JLabel l = new JLabel("Created by Group 4:");
        this.getContentPane().add(l);
        l = new JLabel("");
        this.getContentPane().add(l);
        l = new JLabel("Abhishek Nandi");
        this.getContentPane().add(l);
        l = new JLabel("Christian O'Connell");
        this.getContentPane().add(l);
        l = new JLabel("Roston Pereira");
        this.getContentPane().add(l);
        l = new JLabel("Callum Stocker");
        this.getContentPane().add(l);
        l = new JLabel("Le Wang-Riches");
        this.getContentPane().add(l);
        l = new JLabel("Piers Williams");
        this.getContentPane().add(l);
        l = new JLabel("");
        this.getContentPane().add(l);
        l = new JLabel("For CE903 Group Project");
        this.getContentPane().add(l);
    }

    private void formWindowClosing(WindowEvent evt) {
        _instance = null;
    }

}

