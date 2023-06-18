package org.fffere.jcell.view;

import javax.swing.*;
import java.util.function.Supplier;

public class TickButton extends JButton {
    public final static int WIDTH = 200;
    public final static int HEIGHT = 100;

    public TickButton(Supplier<Void> tickHandler) {
        setText("Advance");
        setBounds(0, 0, WIDTH, HEIGHT);
        addActionListener(e -> tickHandler.get());
    }
}
