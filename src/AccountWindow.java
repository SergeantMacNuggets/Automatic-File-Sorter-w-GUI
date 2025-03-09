import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

abstract class Accounts {
    static Accounts account;

    public static void clearInstance() {
        account=null;
    }
}


class Admin extends Accounts{
    final private static String username="admin", password="admin";
    public static String getUsername() {
        return username;
    }
    public static String getPassword() {
        return password;
    }

    public static Accounts getInstance() {
        if(account == null)
            account = new Admin();

        return account;
    }

}

class Guest extends Accounts{
    public static Accounts getInstance() {
        if(account == null)
            account = new Guest();

        return account;
    }
}

public class AccountWindow extends JFrame {
    private static AccountWindow accountWindow;
    JPanel leftPanel;
    JTextField username, newUserName;
    JPasswordField password, newPassword;
    JButton guestButton, loginButton, signupButton;
    JLabel forgotPassword;
    Window window;
    boolean signupState = false, loginState = true;
    public void start() {
        leftPanel = getLeftPanel();
        this.setSize(400,200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Log in");
        this.setLayout(new GridLayout(1,2));
        this.setIconImage(new ImageIcon("src/res/icon.png").getImage());
        this.setLocationRelativeTo(null);
        this.add(leftPanel);
        this.add(getRightPanel());
        this.setResizable(false);
        this.setVisible(true);
    }

    private JPanel getLeftPanel() {
        JPanel p = new JPanel();
        p.setLayout(new CardLayout());
        p.add(getLoginPanel(), "Login");
        p.add(getSignupPanel(), "Signup");
        return p;
    }

    private JPanel getLoginPanel() {
        JPanel p = new JPanel();
        forgotPassword = new JLabel("Forgot Password");
        forgotPassword.setFont(new Font("Ariel", Font.BOLD,10));
        forgotPassword.setForeground(Color.BLUE.darker());
        forgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ChangePassword.getInstance();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                forgotPassword.setForeground(Color.blue.brighter());
            }
        });
        username = new JTextField();
        password = new JPasswordField();
        Border padding = BorderFactory.createEmptyBorder(5,5,20,5);
        p.setLayout(new GridLayout(5,1,0,5));
        p.setBorder(padding);
        p.add(new JLabel("Username"));
        p.add(username);
        p.add(new JLabel("Password"));
        p.add(password);
        p.add(forgotPassword);
        return p;
    }

    private JPanel getSignupPanel() {
        JPanel p = new JPanel();
        newUserName = new JTextField();
        newPassword = new JPasswordField();
        JPasswordField confirmPassword = new JPasswordField();
        Border padding = BorderFactory.createEmptyBorder(5,5,20,5);
        p.setLayout(new GridLayout(6,1,0,5));
        p.setBorder(padding);
        p.add(new JLabel("New Username"));
        p.add(newUserName);
        p.add(new JLabel("New Password"));
        p.add(newPassword);
        p.add(new JLabel("Confirm Password"));
        p.add(confirmPassword);
        return p;
    }

    private JPanel getRightPanel() {
        JPanel p = new JPanel();
        JPanel temp = new JPanel();
        JLabel image = new JLabel(new ImageIcon("src/res/account.png"));
        guestButton = new JButton("Enter as Guest");
        loginButton = new JButton("Login");
        signupButton = new JButton("Signup");
        temp.setLayout(new GridLayout(1,2,5,0));
        guestButton.setPreferredSize(new Dimension(150,28));
        image.setPreferredSize(new Dimension(100,64));
        Border padding = BorderFactory.createEmptyBorder(5,5,20,5);
        p.setBorder(padding);
        loginButton.addActionListener(logIn());
        signupButton.addActionListener(signUp());
        guestButton.addActionListener(guestIn());
        p.add(image);
        p.add(guestButton);
        temp.add(loginButton);
        temp.add(signupButton);
        p.add(temp);
        return p;
    }

    private ActionListener logIn() {
        return _ -> {
            CardLayout card = (CardLayout) (leftPanel.getLayout());
            String user = username.getText();
            String pass = String.copyValueOf(password.getPassword());

            if (!loginState) {
                card.previous(leftPanel);
                signupState = false;
                loginState = true;
                return;
            }

            if (user.equals(Admin.getUsername()) && pass.equals(Admin.getPassword())) {
                window = new Window(Admin.getInstance());
                window.start();
                this.setVisible(false);
                this.dispose();
            }

        };
    }

    private ActionListener signUp() {
        return _ -> {
            if(!signupState) {
                CardLayout card = (CardLayout) (leftPanel.getLayout());
                card.next(leftPanel);
                signupState=true;
                loginState=false;
            }

        };
    }

    private ActionListener guestIn() {
        return _ -> {
            window = new Window(Guest.getInstance());
            window.start();
            this.setVisible(false);
            this.dispose();
        };
    }

    public static AccountWindow getInstance() {
        if(accountWindow == null)
            accountWindow = new AccountWindow();

        return accountWindow;
    }

    public static void checkNull() throws NullPointerException{
        if(accountWindow==null) throw new NullPointerException();
    }
}

class ChangePassword extends JFrame {
    private static ChangePassword changePassword;
    JPasswordField oldPass, newPass;
    JButton enter;
    JPanel p;
    ChangePassword() {
        oldPass = new JPasswordField();
        newPass = new JPasswordField();
        enter = new JButton("Enter");
        enter.addActionListener(_ -> {
            this.setVisible(false);
            this.dispose();
        });
        p=new JPanel();
        p.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        this.setIconImage(new ImageIcon("src/res/icon.png").getImage());
        this.setLocationRelativeTo(null);
        this.setSize(200,200);
        this.setResizable(false);
        p.setLayout(new GridLayout(5,1,0,10));
        p.add(new JLabel("Old Password"));
        p.add(oldPass);
        p.add(new JLabel("New Password"));
        p.add(newPass);
        p.add(enter);
        add(p);
        this.setVisible(true);
    }

    public static ChangePassword getInstance() {
        if(changePassword==null)
            changePassword=new ChangePassword();
        return changePassword;
    }
}

