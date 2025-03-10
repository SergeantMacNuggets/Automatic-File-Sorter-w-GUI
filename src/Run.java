import javax.swing.*;

//Window nga musdisplay output sa OS
class Window extends JFrame {
    private static Accounts account;
    private static Window window;
    public void start() {
        if(AccountWindow.isRunning()) {
            Window.clearInstance();
            Accounts.clearInstance();
            return;
        } else if(AccountWindow.isNull() && window != null) {
            Window.clearInstance();
            AccountWindow.getInstance().start();
        }
        try {
            if(AccountWindow.isNull() || Window.isNull())
                throw new NullPointerException();
            MainPanel main = new MainPanel(account);
            this.setSize(800,550);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setIconImage(new ImageIcon("src/res/icon.png").getImage());
            this.setTitle("Automatic File Sorter");
            this.setJMenuBar(new MenuBar());
            this.add(main.getMain());
            this.setContentPane(main.getMain());
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setVisible(true);
        }
        catch (NullPointerException e) {
            Window.clearInstance();
            AccountWindow.getInstance().start();
        }
    }


    public static Window getInstance(Accounts account) {
        if(window == null) {
            Window.account = account;
            window = new Window();
        }
        return window;
    }

    public static void clearInstance() {
        window.dispose();
        window.setVisible(false);
        window=null;
    }

    public static boolean isNull() {
        return Window.window==null;
    }

}

public class Run {
    public static void main(String[] args) {
        AccountWindow.getInstance().start();
    }
}