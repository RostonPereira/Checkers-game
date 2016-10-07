/*
 * Decompiled with CFR 0_115.
 */
package runtime;

import corelogic.CheckerBoard;
import javax.swing.SwingUtilities;
import runtime.Settings;
import ui.GameUI;
import ui.UIFrame;

public class Main {
    private static Settings settings;
    private static UIFrame ui;

    public static void main(String[] args) {
        settings = Settings.loadSettings();
        if (settings.getSavedGame() == null) {
            settings.setSaveGame(new CheckerBoard(settings));
        }
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                ui = new UIFrame(settings);
                settings.getSavedGame().setUI(Main.access$000().game);
            }
        });
        new Thread(settings.getSavedGame()).start();
    }

    static /* synthetic */ UIFrame access$000() {
        return ui;
    }

}

