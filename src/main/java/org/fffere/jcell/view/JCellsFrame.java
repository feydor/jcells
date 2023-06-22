package org.fffere.jcell.view;

import org.fffere.jcell.GridEnvironment;
import org.fffere.jcell.parser.RleFile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/** The main frame */
public class JCellsFrame extends JFrame {
    private final GridEnvironment gEnv;
    private final JButton openButton;
    private final JComboBox<String> dropdown;
    private final JFileChooser fileChooser = new JFileChooser(ResourceConstants.EXAMPLES_DIR);

    public JCellsFrame(GridEnvironment gridEnvironment) {
        this.gEnv = gridEnvironment;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("JCells");

        var menuBar = new JMenuBar();
        var menuLayout = new FlowLayout();
        menuLayout.setAlignment(FlowLayout.LEFT);
        menuBar.setLayout(menuLayout);
        setJMenuBar(menuBar);

        var playButton = new JButton("Play", new ImageIcon(ResourceConstants.IMG_PLAY));
        playButton.addActionListener(e -> {
            gEnv.toggleRunning();
            repaint();
        });
        menuBar.add(playButton);

        var advanceButton = new JButton("Advance", new ImageIcon(ResourceConstants.IMG_RIGHT_ARROW));
        advanceButton.addActionListener(e -> {
            gridEnvironment.run();
            repaint();
        });
        menuBar.add(advanceButton);

        var resetButton = new JButton("Reset", new ImageIcon(ResourceConstants.IMG_REFRESH_ARROW));
        resetButton.addActionListener(e -> {
            gEnv.resetState();
            repaint();
        });
        menuBar.add(resetButton);

        openButton = new JButton("Open a pattern...", new ImageIcon(ResourceConstants.IMG_OPEN_FILE));
        openButton.addActionListener(this::handleFileOpenButton);
        menuBar.add(openButton);

        dropdown = new JComboBox<>(new DefaultComboBoxModel<>());
        var ruleNames = Arrays.stream(gEnv.getAllRules())
                .map(r -> RleFile.displayName(r.name(), r.ruleString())).toList();
        for (var name : ruleNames)
            dropdown.addItem(name);
        dropdown.setSelectedIndex(0);
        dropdown.addActionListener(e -> {
            String ruleDisplayName = (String) dropdown.getSelectedItem();
            assert ruleDisplayName != null;
            gEnv.changeRule(RleFile.extractRuleStringFromDisplayName(ruleDisplayName));
        });
        menuBar.add(new JLabel("Rule: "));
        menuBar.add(dropdown);

        JPanel mainPane = new JPanel(new GridLayout(0, 1, 10, 10));
        int margins = 20;
        mainPane.setBorder(new EmptyBorder(margins, margins, margins, margins));
        getContentPane().add(mainPane);

        mainPane.add(new GridPane(gEnv), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleFileOpenButton(ActionEvent e) {
        if (e.getSource() == openButton) {
            int returnVal = fileChooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                var file = fileChooser.getSelectedFile();
                System.out.println("Opening: " + file.getName());
                var optPatternFile = gEnv.loadPatternFile(file);
                if (optPatternFile.isPresent()) {
                    var patternFile = optPatternFile.get();
                    dropdown.addItem(patternFile.displayName());
                    dropdown.setSelectedItem(patternFile.displayName());
                }
                repaint();
            } else {
                System.out.println("Open command cancelled by user");
            }
        }
    }
}
