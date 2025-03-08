import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

abstract class Accounts {
    String username = "admin", password="admin";

}

public class AccountWindow extends JFrame {
    JTextField username, password;
    JButton guestButton, loginButton;
    public void start() {
        this.setSize(400,200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Log in");
        this.setLayout(new GridLayout(1,2));
        this.setIconImage(new ImageIcon("src/res/icon.png").getImage());
        this.setLocationRelativeTo(null);
        this.add(getleftPanel());
        this.add(getRightPanel());
        this.setResizable(false);
        this.setVisible(true);
    }

    private JPanel getleftPanel() {
        JPanel p = new JPanel();
        username = new JTextField();
        password = new JTextField();
        Border padding = BorderFactory.createEmptyBorder(5,5,20,5);
        p.setLayout(new GridLayout(4,1,0,5));
        p.setBorder(padding);
        p.add(new JLabel("Username"));
        p.add(username);
        p.add(new JLabel("Password"));
        p.add(password);
        return p;
    }

    private JPanel getRightPanel() {
        JPanel p = new JPanel();
        JLabel image = new JLabel(new ImageIcon("src/res/account.png"));
        guestButton = new JButton("Enter as Guest");
        loginButton = new JButton("Login");
        guestButton.setPreferredSize(new Dimension(150,28));
        loginButton.setPreferredSize(new Dimension(150,28));
        image.setPreferredSize(new Dimension(100,64));
        Border padding = BorderFactory.createEmptyBorder(5,5,20,5);
        p.setBorder(padding);
        loginButton.addActionListener(logIn());
        guestButton.addActionListener(guestIn());
        p.add(image);
        p.add(guestButton);
        p.add(loginButton);
        return p;
    }

    private ActionListener logIn() {
        return e -> {
            if(username.getText().equals("admin") && password.getText().equals("admin")) {
                new Window(new Admin());
                this.setVisible(false);
                this.dispose();
            }
        };
    }

    private ActionListener guestIn() {
        return e -> {
            new Window(new Guest());
            this.setVisible(false);
            this.dispose();
        };
    }

}


class Admin extends Accounts{
    private static Accounts admin;

    public static Accounts getInstance() {
        if(admin == null)
            admin = new Admin();

        return admin;
    }
}

class Guest extends Accounts{
    private static Accounts guest;

    public static Accounts getInstance() {
        if(guest == null)
            guest = new Guest();

        return guest;
    }
}
