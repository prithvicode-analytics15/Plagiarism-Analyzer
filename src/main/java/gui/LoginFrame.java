
package gui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private final Color background = new Color(18, 24, 38);
    private final Color accent = new Color(79, 140, 255);

    public LoginFrame() {
        setTitle("PlagiaCheck - Login");
        setSize(450, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(background);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(340, 370));
        card.setBackground(new Color(30, 39, 56));
        card.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("PlagiaCheck");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign in to continue");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = createLabel("Username");
        usernameField = new JTextField();
        styleField(usernameField);

        JLabel passLabel = createLabel("Password");
        passwordField = new JPasswordField();
        styleField(passwordField);

        JCheckBox showPassword = new JCheckBox("Show password");
        showPassword.setForeground(Color.LIGHT_GRAY);
        showPassword.setBackground(card.getBackground());
        showPassword.setFocusPainted(false);
        showPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        showPassword.addActionListener(e -> {
            passwordField.setEchoChar(
                showPassword.isSelected() ? (char) 0 : '•'
            );
        });

        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(accent);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(280, 42));

        loginButton.addActionListener(e -> login());

        JLabel footer = new JLabel("Secure Document Similarity Checker");
        footer.setFont(new Font("Arial", Font.PLAIN, 10));
        footer.setForeground(Color.GRAY);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(30));
        card.add(userLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(18));
        card.add(passLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(8));
        card.add(showPassword);
        card.add(Box.createVerticalStrut(22));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(20));
        card.add(footer);

        mainPanel.add(card);
        add(mainPanel);

        getRootPane().setDefaultButton(loginButton);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(280, 38));
        field.setPreferredSize(new Dimension(280, 38));
        field.setBackground(new Color(242, 245, 250));
        field.setForeground(Color.BLACK);
        field.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.equals("admin") && password.equals("admin123")) {
            JOptionPane.showMessageDialog(
                this, "Login successful!", "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            dispose();
            new MainFrame().setVisible(true);

        } else {
            JOptionPane.showMessageDialog(
                this, "Invalid username or password!",
                "Login Failed", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
