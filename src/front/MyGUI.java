package front;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

public class MyGUI extends JFrame {
    private JTextArea textArea; // Declare textArea as a class variable to access it globally

    public MyGUI(String title, CountDownLatch latch, DataCallback callback) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Create a JTextArea
        textArea = new JTextArea();

        // Create a JButton
        JButton button = new JButton("OK");

        // Create a JPanel to hold the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button);

        // Set layout for the JFrame
        setLayout(new BorderLayout());

        // Add the components to the JFrame
        add(textArea, BorderLayout.CENTER); // Add JTextArea to the center
        add(buttonPanel, BorderLayout.SOUTH); // Add JButton to the bottom

        // Add ActionListener to the button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Access the content inside the JTextArea
                String text = textArea.getText();
                // Invoke the callback method with the data
                callback.onDataReceived(text);
                latch.countDown();
                // Close the JFrame
                //dispose();
                setVisible(false);
            }
        });

        setVisible(true);
    }
}
