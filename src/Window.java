import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EmptyStackException;
import java.util.Stack;

interface MainPanels {
    NorthPanel northPanel = new NorthPanel();
    SouthPanel southPanel = new SouthPanel();
    LeftPanel leftPanel = new LeftPanel();
    RightPanel rightPanel = new RightPanel();
    CenterPanel centerPanel = new CenterPanel();
    JButton[] b = {centerPanel.getButton(Button.ADD),centerPanel.getButton(Button.REMOVE),
            northPanel.clearButton(),centerPanel.getButton(Button.UNDO)};

}

interface Buttons {
    JButton add = new JButton("Add");
    JButton remove = new JButton("Remove");
    JButton undo = new JButton("Undo");
    JButton sort = new JButton("Sort");
    JButton clear = new JButton("Clear");
}

enum Input {
    FILE,
    LOCATION,
    SPECIFIC_LOCATION,
    DATE
}

enum Button {
    ADD,
    REMOVE,
    CLEAR,
    UNDO,
    SORT
}

//Window nga musdisplay output sa OS
public class Window extends JFrame implements MainPanels {
    Window() {

        setSize(800,550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Automatic File Sorter");
        setJMenuBar(new MenuBar());
        add(getMain());
        clickListener(b);
        setContentPane(getMain());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void clickListener(JButton[] b) {
        JList<String> tempLeft = leftPanel.getList();
        JList<String> tempRight = rightPanel.getList();
        DefaultListModel<String> modelLeft = (DefaultListModel<String>) tempLeft.getModel();
        DefaultListModel<String> modelRight = (DefaultListModel<String>) tempRight.getModel();
        Stack <String> stackLeft = new Stack<>();
        Stack <String> stackRight = new Stack<>();
        b[0].addActionListener(e -> {
            JTextField[] t = {northPanel.getTextInput(Input.LOCATION)
                    ,northPanel.getTextInput(Input.SPECIFIC_LOCATION), northPanel.getTextInput(Input.DATE)};

            String comboBox = northPanel.getComboBox();
            String specificFile = comboBox + " " + t[2].getText() + " " + t[1].getText();

            if (t[0].getText().isEmpty()
                    || (northPanel.isSpecificTrue() && t[1].getText().isEmpty())
                    || (northPanel.isRadioTrue() && t[2].getText().isEmpty())) return;

            leftPanel.setList(specificFile);
            rightPanel.setList(t[0].getText());
            for(JTextField text: t) text.setText("");
        });
        b[1].addActionListener(e -> {
            int index = tempLeft.getSelectedIndex();
            stackLeft.push(modelLeft.get(index));
            stackRight.push(modelRight.get(index));

            if(index != -1) {
                modelLeft.remove(index);
                modelRight.remove(index);
            }

        });
        b[2].addActionListener(e -> {
            DefaultListModel<String> modelLeft1 = (DefaultListModel<String>) tempLeft.getModel();
            DefaultListModel<String> modelRight1 = (DefaultListModel<String>) tempRight.getModel();
            modelLeft1.removeAllElements();
            modelRight1.removeAllElements();
        });
        b[3].addActionListener(e -> {
            try {
                leftPanel.setList(stackLeft.peek());
                rightPanel.setList(stackRight.peek());
                stackLeft.pop();
                stackRight.pop();
            } catch(EmptyStackException s) {
                System.out.println(s.getMessage());
            }

        });
    }

    private JPanel getMain() {
        JPanel main = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(0,5,0,5);
        setIconImage(new ImageIcon("src/res/icon.png").getImage());
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

class NorthPanel extends JPanel implements ItemListener {
    JTextField location, specificLocation,dateText;
    JComboBox fileFormat;
    JCheckBox specificLoc;
    JButton clear;
    boolean radioState=false;

    NorthPanel() {

        setLayout(new GridLayout(1,2,50,0));
        add(getLeftPanel());
        add(getRightPanel());
    }

    private JPanel inTextLocation(JTextField t, JButton b) {
        JPanel p = new JPanel(new BorderLayout());
        t.setPreferredSize(new Dimension(330,5));
        b.setPreferredSize(new Dimension(30,5));
        b.addActionListener(getFilePath(t));

        p.add(t, BorderLayout.WEST);
        p.add(b, BorderLayout.EAST);
        return p;
    }

    private JPanel getLeftPanel() {
        JPanel left = new JPanel();
        location = new JTextField();
        String[] formats = {".mp4", ".mp3", ".docx", ".pptx"};
        fileFormat = new JComboBox(formats);

        Component[] components = {new JLabel("File Format"),
                fileFormat, new JLabel("Destination"), inTextLocation(location, new JButton("..."))};
        fileFormat.setPreferredSize(new Dimension(200,5));
        left.setLayout(new GridLayout(5,1));

        for(Component c: components)
            left.add(c);

        return left;
    }

    public JTextField getTextInput(Input in) {
        return switch(in) {
            case LOCATION -> location;
            case SPECIFIC_LOCATION -> specificLocation;
            case DATE -> dateText;
            default -> null;
        };
    }

    public String getComboBox() {
        return fileFormat.getSelectedItem().toString();
    }

    public boolean isSpecificTrue() {
        return specificLoc.isSelected();
    }

    private JPanel getRightPanel() {
        JPanel right = new JPanel();
        JPanel p1 = new JPanel();
        JPanel datePanel = new JPanel(new GridLayout(1,2));
        JPanel temp = new JPanel();
        JButton choose = new JButton("...");
        clear = new JButton("Clear");
        dateText = new JTextField();
        dateText.setEnabled(false);
        specificLocation = new JTextField();
        specificLocation.setEnabled(false);
        choose.setEnabled(false);
        temp.setLayout(new BorderLayout());
        ButtonGroup groupRadio = new ButtonGroup();
        specificLoc = new JCheckBox("Specified Location");
        specificLoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox temp = (JCheckBox) e.getSource();
                if(temp.isSelected()) {
                    specificLocation.setEnabled(true);
                    choose.setEnabled(true);
                }
                else {
                    specificLocation.setEnabled(false);
                    choose.setEnabled(false);
                }
            }
        });
        JRadioButton file = new JRadioButton("File");
        JRadioButton date = new JRadioButton("Date");
        date.addItemListener(this::itemStateChanged);
        file.setSelected(true);
        right.setLayout(new GridLayout(5,1));
        p1.setLayout(new GridLayout(1,4,15,0));
        datePanel.add(date);
        datePanel.add(dateText);
        p1.add(datePanel);
        p1.add(new JLabel(""));
        p1.add(clear);
        right.add(specificLoc);
        right.add(inTextLocation(specificLocation,choose));
        right.add(file);
        right.add(p1);
        groupRadio.add(file);
        groupRadio.add(date);
        return right;
    }

    public JButton clearButton() {
        return clear;
    }


    private ActionListener getFilePath(JTextField t) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                if(chooser.showOpenDialog(null) == 0) {
                    t.setText(chooser.getSelectedFile().getAbsolutePath());
                }

            }
        };
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            dateText.setEnabled(true);
            radioState = true;
        }
        else if (e.getStateChange() == ItemEvent.DESELECTED) {
            dateText.setEnabled(false);
            radioState = false;
        }
    }
    public boolean isRadioTrue() {
        return radioState;
    }
}

class SouthPanel extends JPanel {
    SouthPanel() {
        setPreferredSize(new Dimension(100,30));
        add(new JLabel("All rights reserved©"));
    }
}

class LeftPanel extends JPanel {
    JList<String> list;
    DefaultListModel<String> listD;
    LeftPanel() {
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
    JList<String> list;
    DefaultListModel<String> listD;
    RightPanel() {
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
    JButton[] buttons = {createButton("Add"),createButton("Remove"),
            createButton("Undo"),createButton("Sort")};
    CenterPanel() {
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(100,600));
        for (JButton b: buttons) add(getPanel(b));
    }

    private JButton createButton(String s) {
        JButton b1 = new JButton(s);
        b1.setPreferredSize(new Dimension(80,25));
        return b1;
    }

    public JButton getButton(Button b) {
        return switch (b) {
            case ADD -> buttons[0];
            case REMOVE -> buttons[1];
            case UNDO -> buttons[2];
            case SORT -> buttons[3];
            default -> null;
        };
    }

    private JPanel getPanel(JButton b1) {
        JPanel p1=new JPanel();
        p1.add(b1);
        return p1;
    }
}

class MenuBar extends JMenuBar {
    MenuBar() {
        JMenu[] menu = {getFileMenu(), new JMenu("Edit"),
                new JMenu("View"), new JMenu("Window"), getHelp()};

        for(JMenu m: menu) add(m);
    }

    //Open file folder

    private JMenu getFileMenu() {
        JMenu file = new JMenu("File");
        JMenuItem[] subItem = {new JMenuItem("Print"), new JMenuItem("Quit")};

        subItem[0].addActionListener(e -> {
            //Isulat code nimo diri mer
        });

        subItem[1].addActionListener(e -> {
            System.exit(0);
        });
        for(JMenuItem i: subItem) file.add(i);

        return file;
    }

    private JMenu getHelp() {
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        help.add(about);
        return help;
    }

}