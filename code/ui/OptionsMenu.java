/*
 * Decompiled with CFR 0_115.
 */
package ui;

import ai.AIPlayer;
import corelogic.RuleSet;
import java.awt.Color;
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
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import runtime.Settings;
import ui.UIFrame;

public class OptionsMenu
extends JFrame {
    private static OptionsMenu _instance = null;
    UIFrame parent;
    JLabel lbl;
    Settings settings;
    ColourPopup whitePiece;
    ColourPopup blackPiece;
    ColourPopup board1;
    ColourPopup board2;
    JComboBox rules;
    JComboBox difficulty;

    public static void showOptions(UIFrame parent) {
        if (_instance == null) {
            _instance = new OptionsMenu(parent);
            _instance.setVisible(true);
        }
    }

    private OptionsMenu(UIFrame parent) {
        this.parent = parent;
        this.settings = parent.settings;
        this.setSize(new Dimension(250, 250));
        this.setTitle("Options");
        this.setResizable(false);
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent evt) {
                OptionsMenu.this.formWindowClosing(evt);
            }
        });
        GridLayout layout = new GridLayout(0, 2);
        this.getContentPane().setLayout(layout);
        this.lbl = new JLabel("Player Piece Colour:");
        this.getContentPane().add(this.lbl);
        this.whitePiece = new ColourPopup(this.settings.getWhitePieceColor());
        this.getContentPane().add(this.whitePiece);
        this.lbl = new JLabel("Opponent Piece Colour:");
        this.getContentPane().add(this.lbl);
        this.blackPiece = new ColourPopup(this.settings.getBlackPieceColor());
        this.getContentPane().add(this.blackPiece);
        this.lbl = new JLabel("Board Colour 1:");
        this.getContentPane().add(this.lbl);
        this.board1 = new ColourPopup(this.settings.getWhiteSquareColor());
        this.getContentPane().add(this.board1);
        this.lbl = new JLabel("Board Colour 2:");
        this.getContentPane().add(this.lbl);
        this.board2 = new ColourPopup(this.settings.getBlackSquareColor());
        this.getContentPane().add(this.board2);
        this.lbl = new JLabel("Rules:");
        this.getContentPane().add(this.lbl);
        this.rules = new JComboBox();
        this.rules.setModel(new DefaultComboBoxModel<RuleSet.RuleSets>((E[])RuleSet.RuleSets.values()));
        this.rules.setSelectedItem((Object)this.settings.getRuleSet());
        this.getContentPane().add(this.rules);
        this.lbl = new JLabel("Difficulty:");
        this.getContentPane().add(this.lbl);
        this.difficulty = new JComboBox();
        this.difficulty.setModel(new DefaultComboBoxModel<AIPlayer.Difficulty>((E[])AIPlayer.Difficulty.values()));
        this.difficulty.setSelectedItem((Object)this.settings.getDifficulty());
        this.getContentPane().add(this.difficulty);
        JButton reset = new JButton("Restore Defaults");
        reset.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                OptionsMenu.this.settings.restoreDefaults();
                OptionsMenu.this.whitePiece.setColour(OptionsMenu.this.settings.getWhitePieceColor());
                OptionsMenu.this.blackPiece.setColour(OptionsMenu.this.settings.getBlackPieceColor());
                OptionsMenu.this.board1.setColour(OptionsMenu.this.settings.getWhiteSquareColor());
                OptionsMenu.this.board2.setColour(OptionsMenu.this.settings.getBlackSquareColor());
                OptionsMenu.this.rules.setSelectedItem((Object)OptionsMenu.this.settings.getRuleSet());
                OptionsMenu.this.difficulty.setSelectedItem((Object)OptionsMenu.this.settings.getDifficulty());
                OptionsMenu.this.parent.repaint();
            }
        });
        this.getContentPane().add(reset);
        this.pack();
    }

    private void updateSettings() {
        this.settings.setWhitePieceColor(this.whitePiece.getColour());
        this.settings.setBlackPieceColor(this.blackPiece.getColour());
        this.settings.setWhiteSquareColor(this.board1.getColour());
        this.settings.setBlackSquareColor(this.board2.getColour());
        this.settings.setRuleSet((RuleSet.RuleSets)((Object)this.rules.getSelectedItem()));
        this.settings.setDifficulty((AIPlayer.Difficulty)((Object)this.difficulty.getSelectedItem()));
        this.settings.saveSettings();
        this.parent.repaint();
    }

    private void formWindowClosing(WindowEvent evt) {
        this.updateSettings();
        _instance = null;
    }

    class ColourPopup
    extends JButton {
        public ColourPopup(Color colour) {
            this.setBackground(colour);
            this.setSize(this.getWidth(), this.getWidth());
            this.addActionListener(new ActionListener(OptionsMenu.this){
                final /* synthetic */ OptionsMenu val$this$0;

                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    Color startColour = ColourPopup.this.getBackground();
                    Color setColour = JColorChooser.showDialog(OptionsMenu.this, "Set Colour", startColour);
                    if (setColour != null) {
                        ColourPopup.this.setBackground(setColour);
                    }
                    OptionsMenu.this.updateSettings();
                }
            });
        }

        public Color getColour() {
            return this.getBackground();
        }

        public void setColour(Color colour) {
            this.setBackground(colour);
        }

    }

}

