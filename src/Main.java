import javax.swing.*;
import java.awt.*;

class Window extends JFrame {
    Window() {
        setSize(new Dimension(800,800));
        setTitle("File Sorter");
        setVisible(true);
    }
}

public class Main {
    public static void main(String[] args) {
        new Window();
    }
}