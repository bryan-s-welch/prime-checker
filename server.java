import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class server {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ServerGUI serverGUI = new ServerGUI();
            serverGUI.setVisible(true);

            new Thread(() -> {
                try {
                    startServer(serverGUI);
                } catch (IOException e) {
                    serverGUI.updateStatus("Error: " + e.getMessage());
                }
            }).start();
        });
    }

    private static void startServer(ServerGUI serverGUI) throws IOException {
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverGUI.updateStatus("Server is listening on port " + port);

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                    int number = input.readInt();
                    serverGUI.updateStatus("Received number: " + number);

                    boolean isPrime = isPrime(number);
                    output.writeUTF(isPrime ? "yes" : "no");
                }
            }
        }
    }

    private static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    static class ServerGUI extends JFrame {
        private final JTextArea statusArea;

        public ServerGUI() {
            setTitle("Prime Number Server");
            setSize(400, 200);
            setResizable(false);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            statusArea = new JTextArea();
            statusArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(statusArea);

            panel.add(scrollPane, BorderLayout.CENTER);

            add(panel);
        }

        public void updateStatus(String status) {
            SwingUtilities.invokeLater(() -> statusArea.append(status + "\n"));
        }
    }
}
