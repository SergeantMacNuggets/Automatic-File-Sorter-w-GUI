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
    ChangePassword changePassword;
    JPanel leftPanel;
    JTextField username, newUserName;
    JPasswordField password, newPassword;
    JButton guestButton, loginButton, signupButton;
    JLabel forgotPassword;
    boolean signupState = false, loginState = true;
    static boolean running;
    Accounts account;
    public void start() {
        running = true;
        leftPanel = getLeftPanel();
        Accounts.clearInstance();
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

    public static boolean isRunning() {
        return running;
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
        Component[] components = {new JLabel("Username"), username = new JTextField(), new JLabel("Password"),
                password = new JPasswordField(), forgotPassword = new JLabel("Forgot Password")};

        forgotPassword.setFont(new Font("Ariel", Font.BOLD,10));
        forgotPassword.setForeground(Color.BLUE.darker());
        forgotPassword.addMouseListener(forgotPass());

        Border padding = BorderFactory.createEmptyBorder(5,5,20,5);
        p.setLayout(new GridLayout(5,1,0,5));
        p.setBorder(padding);

        for(Component c: components)
            p.add(c);

        return p;
    }

    private MouseAdapter forgotPass() {

        return new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e){
                super.mouseClicked(e);
                changePassword = ChangePassword.getInstance();
            }
        };
    }

    private JPanel getSignupPanel() {
        Component[] components = {new JLabel("New Username"), newUserName = new JTextField(),
                new JLabel("New Password"),
                newPassword = new JPasswordField(),new JLabel("ConfirmPassword"), new JPasswordField()};
        JPanel p = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(5,5,20,5);
        p.setLayout(new GridLayout(6,1,0,5));
        p.setBorder(padding);
        for(Component c: components)
            p.add(c);
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
                account = Admin.getInstance();
                running = false;
                Window.getInstance(account).start();
                this.setVisible(false);
                this.dispose();
            }

        };
    }

    private ActionListener guestIn() {
        return _ -> {
            account = Guest.getInstance();
            running = false;
            Window.getInstance(account).start();
            this.setVisible(false);
            this.dispose();
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

    public static AccountWindow getInstance() {
        if(accountWindow == null)
            accountWindow = new AccountWindow();

        return accountWindow;
    }

    public static boolean isNull() {
        return accountWindow==null;
    }

    public static void clearInstance() {
        accountWindow=null;
    }
}

class ChangePassword extends JFrame {
    private static ChangePassword changePassword;
    JPasswordField oldPass, newPass;
    JButton enter;

    ChangePassword() {
        this.setIconImage(new ImageIcon("src/res/icon.png").getImage());
        this.setLocationRelativeTo(null);
        this.setSize(200,200);
        this.setResizable(false);
        this.add(mainPanel());
        this.setVisible(true);
    }

    private JPanel mainPanel() {
        JPanel p = new JPanel();
        Component[] components = {new JLabel("Old Password"),oldPass = new JPasswordField(), new JLabel("New Password"),
                newPass = new JPasswordField(), enter = new JButton("Enter")};

        p.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        p.setLayout(new GridLayout(5,1,0,10));

        enter.addActionListener(enterButton());

        for(Component c: components)
            p.add(c);

        return p;
    }

    private ActionListener enterButton() {

        return _ -> {
            this.setVisible(false);
            this.dispose();
            ChangePassword.clearInstance();
        };
    }

    public static ChangePassword getInstance() {

        if(changePassword==null)
            changePassword=new ChangePassword();

        return changePassword;
    }

    public static void clearInstance() {
        changePassword=null;
    }
}
