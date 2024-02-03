package front;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    private JTextArea textArea;

    public MyFrame(String initialText) {
        setTitle("Text Area Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Create the JTextArea
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        textArea.setEditable(true); // Allow editing
        textArea.setText(initialText);

        // Add the JTextArea to the frame
        add(textArea, BorderLayout.CENTER);

        setVisible(true);
    }

    // Method to retrieve the text from the JTextArea
    public String getText() {
        return textArea.getText();
    }
}
