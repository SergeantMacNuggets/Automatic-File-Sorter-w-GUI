import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class NorthPanel extends JPanel {
    JTextField fileFormat, location;
    NorthPanel() {
        setLayout(new GridLayout(1,2,50,0));
        setPreferredSize(new Dimension(100,120));
        add(getLeftPanel());
        add(getRightPanel());
    }
    private JPanel getLeftPanel() {
        JPanel left = new JPanel();
        JPanel temp = new JPanel();
        JButton choose = new JButton("...");
        fileFormat = new JTextField();
        location = new JTextField();
        fileFormat.setPreferredSize(new Dimension(200,5));
        location.setPreferredSize(new Dimension(330,5));
        choose.setPreferredSize(new Dimension(30,5));
        left.setLayout(new GridLayout(5,1));
        temp.setLayout(new BorderLayout());
        choose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if(chooser.showOpenDialog(null) == 0) {
                    location.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        left.add(new JLabel("File Format"));
        left.add(fileFormat);
        left.add(new JLabel("Destination"));
        temp.add(location,BorderLayout.WEST);
        temp.add(choose,BorderLayout.EAST);
        left.add(temp);
        return left;
    }

    public JTextField getTextField(String name) {
        return switch(name) {
            case "file" -> fileFormat;
            case "location" -> location;
            default -> null;
        };
    }

    private JPanel getRightPanel() {
        JPanel right = new JPanel();
        JPanel p1 = new JPanel();
        JButton save = new JButton("Save");
        JButton load = new JButton("Load");
        JTextField location = new JTextField();
        location.setEnabled(false);
        ButtonGroup groupRadio = new ButtonGroup();
        JCheckBox specificLoc = new JCheckBox("Specified Location");
        specificLoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox temp = (JCheckBox) e.getSource();
                if(temp.isSelected()) {
                    location.setEnabled(true);
                }
                else {
                    location.setEnabled(false);
                }
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.showOpenDialog(null);
            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.showOpenDialog(null);
            }
        });
        JRadioButton file = new JRadioButton("File");
        JRadioButton date = new JRadioButton("Date");
        right.setLayout(new GridLayout(5,1));
        p1.setLayout(new GridLayout(1,3,20,0));
        p1.add(file);
        p1.add(save);
        p1.add(load);
        right.add(specificLoc);
        right.add(location);
        right.add(date);
        right.add(p1);
        groupRadio.add(file);
        groupRadio.add(date);
        return right;
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
    ArrayList<String> files;
    JList<String> list;
    DefaultListModel<String> listD;
    LeftPanel() {
        files = new ArrayList<>();
        listD = new DefaultListModel<>();
        list = new JList<>(listD);
        list.setPreferredSize(new Dimension(330,600));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setCellRenderer(getRenderer());
        list.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.lightGray));
        add(list);
    }
    public void setList(String s) {
        listD.addElement(s);
        files.add(s);
    }

    public JList<String> getList() {
        return list;
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
    ArrayList<String> files;
    JList<String> list;
    DefaultListModel<String> listD;
    RightPanel() {
        files = new ArrayList<>();
        listD = new DefaultListModel<>();
        list = new JList<>(listD);
        list.setPreferredSize(new Dimension(330,600));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setCellRenderer(getRenderer());
        list.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.lightGray));
        add(list);
    }

    public void setList(String s) {
        listD.addElement(s);
        files.add(s);
    }

    public JList<String> getList() {
        return list;
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
    JButton addButton,removeButton,removeAll,sort;
    CenterPanel() {
        addButton = createButton("Add");
        removeButton = createButton("Remove");
        removeAll = createButton("Reset");
        sort = createButton("Sort");
        setLayout(new GridLayout(3,1,0,10));
        setPreferredSize(new Dimension(100,600));
        add(getPanel(addButton));
        add(getPanel(removeButton,removeAll));
        add(getPanel("",sort));
    }

    private JButton createButton(String s) {
        JButton b1 = new JButton(s);
        b1.setPreferredSize(new Dimension(80,25));
        return b1;
    }

    public JButton getButton(String name) {
        return switch (name) {
            case "add" -> addButton;
            case "remove" -> removeButton;
            case "clear" -> removeAll;
            case "sort" -> sort;
            default -> null;
        };
    }

    private JPanel getPanel(JButton b1) {
        JPanel p1=new JPanel();
        p1.add(b1);
        return p1;
    }
    private JPanel getPanel(JButton b1,JButton b2) {
        JPanel p1 = new JPanel();
        p1.add(b1);
        p1.add(b2);
        return p1;
    }
    private JPanel getPanel(String l1,JButton b1) {
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(2,1));
        p1.add(new JLabel(l1));
        p1.add(b1);
        return p1;
    }
}

class Window extends JFrame {
    NorthPanel northPanel;
    SouthPanel southPanel;
    LeftPanel leftPanel;
    RightPanel rightPanel;
    CenterPanel centerPanel;
    Window() {
        northPanel = new NorthPanel();
        southPanel = new SouthPanel();
        leftPanel = new LeftPanel();
        rightPanel = new RightPanel();
        centerPanel = new CenterPanel();
        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("File Sorter");
        add(getMain());
        clickListener(centerPanel.getButton("add"),northPanel.getTextField("file"),northPanel.getTextField("location"));
        clickListener(centerPanel.getButton("remove"));
        clickListener(centerPanel.getButton("clear"));
        this.setContentPane(getMain());
        setResizable(false);
        setVisible(true);
    }

    private void clickListener(JButton b1,JTextField t1,JTextField t2) {
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leftPanel.setList(t1.getText());
                rightPanel.setList(t2.getText());
                t1.setText("");
                t2.setText("");
            }
        });
    }

    private void clickListener(JButton b1) {
        JList<String> tempLeft = leftPanel.getList();
        JList<String> tempRight = rightPanel.getList();
        if(b1.getText().equals("Remove")) {
            b1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DefaultListModel<String> modelLeft = (DefaultListModel<String>) tempLeft.getModel();
                    DefaultListModel<String> modelRight = (DefaultListModel<String>) tempRight.getModel();
                    int index = tempLeft.getSelectedIndex();
                    if(index != -1) {
                        modelLeft.remove(index);
                        modelRight.remove(index);
                    }
                }
            });
        }
        else if(b1.getText().equals("Reset")) {
            b1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DefaultListModel<String> modelLeft = (DefaultListModel<String>) tempLeft.getModel();
                    DefaultListModel<String> modelRight = (DefaultListModel<String>) tempRight.getModel();
                    modelLeft.removeAllElements();
                    modelRight.removeAllElements();
                }
            });
        }

    }

    private JPanel getMain() {
        JPanel main = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(5,5,0,5);
        main.setBorder(padding);
        main.setLayout(new BorderLayout());
        main.add(northPanel,BorderLayout.NORTH);
        main.add(southPanel,BorderLayout.SOUTH);
        main.add(leftPanel,BorderLayout.WEST);
        main.add(rightPanel,BorderLayout.EAST);
        main.add(centerPanel,BorderLayout.CENTER);
        return main;
    }
}

public class Main {
    public static void main(String[] args) {
        new Window();
    }
}