
public class Main {
    public static void main(String[] args) {
//        AccountWindow accountWindow = AccountWindow.getInstance();
//        accountWindow.start();
        Window window = Window.getInstance(Admin.getInstance());
        window.start();
    }
}