package org.yanning.gradle.vcs_lib.terminal;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class LanternaDemo {
    private static volatile boolean isTerminalClosed = false;
    private static volatile float fps=60f;
    public static void main(String[] args) throws IOException {

        // Setup terminal and screen layers
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        terminal.setCursorVisible(true);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep((long) (1000/fps));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    terminal.flush();
                    if (!isTerminalClosed)
                        run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        terminal.addResizeListener((terminal1, newSize) -> {
            try {
                terminal1.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        // Create panel to hold components
        Panel root = new Panel();

        root.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        root.addComponent(userPanel());
        root.addComponent(userPanel2());

        // Create window to hold the panel
        BasicWindow window = new BasicWindow();
        window.setComponent(root);
        // Create gui and start gui
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        gui.addWindowAndWait(window);
        isTerminalClosed=true;
//        AppLauncher.Companion.startApp(args);
    }

    private static Panel userPanel2() {
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPreferredWidth(20);
        panel.addComponent(progressBar);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int i = 0;
                while (i < 100) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                    progressBar.setValue(i);
                }
            }
        }.start();
        return panel;
    }

    private static Panel userPanel() {
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));
        panel.addComponent(new Label("Forename"));
        panel.addComponent(new TextBox());
        panel.addComponent(new Label("Surname"));
        panel.addComponent(new TextBox());
        panel.addComponent(new EmptySpace(new TerminalSize(0, 0))); // Empty space underneath labels
        panel.addComponent(new Button("Submit", () -> {
            System.out.println("Submit");
        }));
        return panel;
    }
}
