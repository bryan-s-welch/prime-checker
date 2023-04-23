import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class client {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ClientGUI().setVisible(true);
        });
    }

    static class ClientGUI extends JFrame {
        private final JTextField numberField;
        private final JButton checkButton;
        private final JLabel resultLabel;

        public ClientGUI() {
            setTitle("Prime Number Checker");
            setSize(300, 150);
            setResizable(false);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 1));

            numberField = new JTextField();
            checkButton = new JButton("Check if Prime");
            resultLabel = new JLabel("", SwingConstants.CENTER);

            checkButton.addActionListener(new CheckButtonListener());

            panel.add(new JLabel("Enter a number:", SwingConstants.CENTER));
            panel.add(numberField);
            panel.add(checkButton);
            panel.add(resultLabel);

            add(panel);
        }

        private class CheckButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                int number;
                try {
                    number = Integer.parseInt(numberField.getText());
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Invalid. Enter an integer.");
                    return;
                }

                try (Socket socket = new Socket("localhost", 5000)) {
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                    output.writeInt(number);
                    String response = input.readUTF();

                    resultLabel.setText("Server response: " + response);
                } catch (IOException ex) {
                    resultLabel.setText("Error connecting to server");
                }
            }
        }
    }
}
