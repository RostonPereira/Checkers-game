/*
 * Decompiled with CFR 0_115.
 */
package runtime;

import ai.AIPlayer;
import corelogic.CheckerBoard;
import corelogic.RuleSet;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;

public class Settings
implements Serializable {
    private static String storeLocation = "settings.dsg";
    private int wins;
    private int losses;
    private int streak;
    private int longestWinStreak;
    private int longestLoseStreak;
    private Color whitePiece;
    private Color blackPiece;
    private Color whiteSquare;
    private Color blackSquare;
    private CheckerBoard saveGame;
    private RuleSet.RuleSets ruleSet;
    private AIPlayer.Difficulty difficulty;

    private Settings() {
        this.restoreDefaults();
        this.resetStats();
    }

    public void restoreDefaults() {
        this.whitePiece = Color.white;
        this.blackPiece = Color.black;
        this.whiteSquare = Color.white;
        this.blackSquare = Color.black;
        this.ruleSet = RuleSet.RuleSets.English;
        this.difficulty = AIPlayer.Difficulty.Easy;
    }

    public void resetStats() {
        this.wins = 0;
        this.losses = 0;
        this.longestWinStreak = 0;
        this.longestLoseStreak = 0;
        this.streak = 0;
    }

    public int getWins() {
        return this.wins;
    }

    public int getLosses() {
        return this.losses;
    }

    public void addWin() {
        ++this.wins;
        if (this.streak < 0) {
            if (this.streak < this.longestLoseStreak) {
                this.longestLoseStreak = this.streak;
            }
            this.streak = 1;
        } else {
            ++this.streak;
        }
    }

    public void addLoss() {
        ++this.losses;
        if (this.streak > 0) {
            if (this.streak > this.longestWinStreak) {
                this.longestWinStreak = this.streak;
            }
            this.streak = -1;
        } else {
            --this.streak;
        }
    }

    public int getLongestWinStreak() {
        return this.longestWinStreak;
    }

    public int getLongestLoseStreak() {
        return this.longestLoseStreak;
    }

    public int getStreak() {
        return this.streak;
    }

    public Color getWhitePieceColor() {
        return this.whitePiece;
    }

    public Color getBlackPieceColor() {
        return this.blackPiece;
    }

    public Color getWhiteSquareColor() {
        return this.whiteSquare;
    }

    public Color getBlackSquareColor() {
        return this.blackSquare;
    }

    public void setWhitePieceColor(Color colour) {
        this.whitePiece = colour;
    }

    public void setBlackPieceColor(Color colour) {
        this.blackPiece = colour;
    }

    public void setWhiteSquareColor(Color colour) {
        this.whiteSquare = colour;
    }

    public void setBlackSquareColor(Color colour) {
        this.blackSquare = colour;
    }

    public CheckerBoard getSavedGame() {
        return this.saveGame;
    }

    public void setSaveGame(CheckerBoard game) {
        this.saveGame = game;
    }

    public RuleSet.RuleSets getRuleSet() {
        return this.ruleSet;
    }

    public void setRuleSet(RuleSet.RuleSets rules) {
        this.ruleSet = rules;
    }

    public AIPlayer.Difficulty getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(AIPlayer.Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void saveSettings() {
        try {
            FileOutputStream out = new FileOutputStream(storeLocation);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(this);
            oos.close();
        }
        catch (IOException ex) {
            System.err.println("Could not save settings: " + ex);
        }
    }

    public static Settings loadSettings() {
        try {
            FileInputStream fis = new FileInputStream(new File(storeLocation));
            ObjectInputStream ois = new ObjectInputStream(fis);
            Settings s = (Settings)ois.readObject();
            ois.close();
            return s;
        }
        catch (Exception ex) {
            System.out.println("Could not load settings file, loading default: " + ex);
            return new Settings();
        }
    }
}

