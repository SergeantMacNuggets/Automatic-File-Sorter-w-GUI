import javax.swing.*;
import java.awt.*;

class NorthPanel extends JPanel {
    JTextField fileFormat;
    JTextField location;
    JPanel left;
    JPanel right;
    NorthPanel() {
        fileFormat = new JTextField();
        location = new JTextField();
        left = new JPanel();
        right = new JPanel();
        setLayout(new GridLayout(1,2));
        setPreferredSize(new Dimension(100,120));
//        setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.lightGray));
        fileFormat.setPreferredSize(new Dimension(200,5));
        location.setPreferredSize(new Dimension(200,5));
        left.setLayout(new GridLayout(5,1));
        right.setLayout(new GridLayout(2,3,10,10));
        left.add(new JLabel("File Format"));
        left.add(fileFormat);
        left.add(new JLabel("Destination"));
        left.add(location);
        right.add(new JButton("Button1"));
        right.add(new JButton("Button2"));
        right.add(new JButton("Button3"));
        right.add(new JButton("Button4"));
        right.add(new JButton("Button5"));
        right.add(new JButton("Button6"));
        add(left);
        add(right);
    }
}

class SouthPanel extends JPanel {
    SouthPanel() {
        setPreferredSize(new Dimension(100,30));
//        setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.lightGray));
        add(new JLabel("All rights reserved©"));
    }
}

class LeftPanel extends JPanel {
    LeftPanel() {
        String week[]= { "Monday","Tuesday","Wednesday",
                "Thursday","Friday","Saturday","Sunday"};
        JList<String> list = new JList<>(week);
        list.setPreferredSize(new Dimension(330,600));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setCellRenderer(getRenderer());
        list.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.lightGray));
//        setBackground(Color.green);
        add(list);
    }

    private ListCellRenderer<? super String> getRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                                                          Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));
                return listCellRendererComponent;
            }
        };
    }

}

class RightPanel extends JPanel {
    RightPanel() {
        String week[]= { "Monday","Tuesday","Wednesday",
                "Thursday","Friday","Saturday","Sunday"};
        JList<String> list = new JList<>(week);
        list.setPreferredSize(new Dimension(330,600));
        list.setCellRenderer(getRenderer());
        list.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.lightGray));
//        setBackground(Color.magenta);
        add(list);
    }
    private ListCellRenderer<? super String> getRenderer() {
        return new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                                                          Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
                listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,Color.lightGray));
                return listCellRendererComponent;
            }
        };
    }


}

class CenterPanel extends JPanel {
    JButton addButton,removeButton,removeAll;
    JPanel p1,p2,p3;
    CenterPanel() {
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        removeAll = new JButton("Remove All");
        addButton.setPreferredSize(new Dimension(100,30));
        removeButton.setPreferredSize(new Dimension(100,30));
        removeAll.setPreferredSize(new Dimension(100,30));
        p1 = new JPanel();
        p2 = new JPanel();
        p3 = new JPanel();
        setLayout(new GridLayout(3,1,0,10));
        setPreferredSize(new Dimension(100,600));
        p1.add(addButton);
        p2.add(removeButton);
        p3.add(removeAll);
        add(p1);
        add(p2);
        add(p3);
    }
}

class Window extends JFrame {
    Window() {
        setSize(800,450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("File Sorter");
        setLayout(new BorderLayout());

        add(new NorthPanel(),BorderLayout.NORTH);
        add(new SouthPanel(),BorderLayout.SOUTH );
        add(new LeftPanel(),BorderLayout.WEST);
        add(new RightPanel(),BorderLayout.EAST);
        add(new CenterPanel(),BorderLayout.CENTER);
        setResizable(false);
        setVisible(true);
    }
}

public class Main {
    public static void main(String[] args) {
        new Window();
    }
}