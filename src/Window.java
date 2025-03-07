import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EmptyStackException;
import java.util.Stack;

enum Input {
    FILE,
    LOCATION,
    SPECIFIC_LOCATION,
    DATE
}

enum Panels {
    NORTH_PANEL,
    SOUTH_PANEL,
    LEFT_PANEL,
    RIGHT_PANEL,
    CENTER_PANEL
}

enum Button {
    ADD,
    REMOVE,
    CLEAR,
    UNDO,
    SORT
}
interface MainPanels {
    NorthPanel northPanel = new NorthPanel();
    SouthPanel southPanel = new SouthPanel();
    LeftPanel leftPanel = new LeftPanel();
    RightPanel rightPanel = new RightPanel();
    CenterPanel centerPanel = new CenterPanel();
}

class MainPanel implements MainPanels {

    public JButton getButton(Button b) {
        return switch (b) {
            case ADD -> centerPanel.getButton()[0];

            case REMOVE -> centerPanel.getButton()[1];

            case CLEAR -> northPanel.clearButton();

            case UNDO -> centerPanel.getButton()[2];

            case SORT -> null;

        };
    }

    @SuppressWarnings("unchecked")
    public <T> T getPanel(Panels p) {
        return switch (p) {
            case NORTH_PANEL -> (T) northPanel;

            case SOUTH_PANEL -> (T) southPanel;

            case LEFT_PANEL -> (T) leftPanel;

            case RIGHT_PANEL -> (T) rightPanel;

            case CENTER_PANEL -> (T) centerPanel;

        };
    }

    public String getInput(Input i) throws NullPointerException {
        if(northPanel.getLocationInput().getText().isEmpty() && northPanel.getLocationInput().isEnabled())
            throw new NullPointerException();

        if(northPanel.getSpecificLocation().getText().isEmpty() && northPanel.getSpecificLocation().isEnabled())
            throw new NullPointerException();

        if(northPanel.getDateText().getText().isEmpty() && northPanel.getDateText().isEnabled())
            throw new NullPointerException();

        return switch (i) {
            case FILE -> northPanel.getFileFormat();

            case LOCATION -> northPanel.getLocationInput().getText();

            case SPECIFIC_LOCATION -> northPanel.getSpecificLocation().getText();

            case DATE -> northPanel.getDateText().getText();

        };
    }

    public JTextField getField(Input i) {
        return switch (i) {
            case LOCATION -> northPanel.getLocationInput();

            case SPECIFIC_LOCATION -> northPanel.getSpecificLocation();

            case DATE -> northPanel.getDateText();

            default -> null;
        };
    }


}

//Window nga musdisplay output sa OS
public class Window extends JFrame {
    MainPanel main;
    Window() {
        main = new MainPanel();
        this.setSize(800,550);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Automatic File Sorter");
        this.setJMenuBar(new MenuBar());
        this.add(getMain(main));
        this.clickListener(main);
        this.setContentPane(getMain(main));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void clickListener(MainPanel main) {
        LeftPanel left = main.getPanel(Panels.LEFT_PANEL);
        RightPanel right = main.getPanel(Panels.RIGHT_PANEL);
        JList<String> tempLeft = left.getList();
        JList<String> tempRight = right.getList();
        DefaultListModel<String> modelLeft = (DefaultListModel<String>) tempLeft.getModel();
        DefaultListModel<String> modelRight = (DefaultListModel<String>) tempRight.getModel();
        Stack <String> stackLeft = new Stack<>();
        Stack <String> stackRight = new Stack<>();

        main.getButton(Button.ADD).addActionListener(e -> {
            try {
                JTextField[] t = {main.getField(Input.LOCATION)
                        ,main.getField(Input.SPECIFIC_LOCATION), main.getField(Input.DATE)};

                String specificFile = main.getInput(Input.FILE) + " " + main.getInput(Input.DATE) + " " +
                        main.getInput(Input.SPECIFIC_LOCATION);

                left.setList(specificFile);
                right.setList(main.getInput(Input.LOCATION));

                for(JTextField text: t) text.setText("");

            } catch (NullPointerException n) {
                System.out.println("Error");
            }
        });

        main.getButton(Button.REMOVE).addActionListener(e -> {
            int index = tempLeft.getSelectedIndex();
            stackLeft.push(modelLeft.get(index));
            stackRight.push(modelRight.get(index));

            if(index != -1) {
                modelLeft.remove(index);
                modelRight.remove(index);
            }

        });

        main.getButton(Button.CLEAR).addActionListener(e -> {
            DefaultListModel<String> modelLeft1 = (DefaultListModel<String>) tempLeft.getModel();
            DefaultListModel<String> modelRight1 = (DefaultListModel<String>) tempRight.getModel();
            modelLeft1.removeAllElements();
            modelRight1.removeAllElements();
        });

        main.getButton(Button.UNDO).addActionListener(e -> {
            try {
                left.setList(stackLeft.peek());
                right.setList(stackRight.peek());
                stackLeft.pop();
                stackRight.pop();
            } catch(EmptyStackException s) {
                System.out.println(s.getMessage());
            }

        });
    }

    private JPanel getMain(MainPanel mainP) {
        JPanel main = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(0,5,0,5);
        setIconImage(new ImageIcon("src/res/icon.png").getImage());
        main.setBorder(padding);
        main.setLayout(new BorderLayout());

        main.add(mainP.getPanel(Panels.NORTH_PANEL),BorderLayout.NORTH);
        main.add(mainP.getPanel(Panels.SOUTH_PANEL),BorderLayout.SOUTH);
        main.add(mainP.getPanel(Panels.LEFT_PANEL),BorderLayout.WEST);
        main.add(mainP.getPanel(Panels.RIGHT_PANEL),BorderLayout.EAST);
        main.add(mainP.getPanel(Panels.CENTER_PANEL),BorderLayout.CENTER);
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
        String[] formats = {"Videos",".mp4", ".mp3","Documents" ,".docx", ".pptx"};
        location = new JTextField();
        fileFormat = new JComboBox(formats);
        fileFormat.setEditable(true);
        fileFormat.setPreferredSize(new Dimension(200,5));
        fileFormat.setSelectedIndex(-1);

        Component[] components = {new JLabel("File Format"),
                fileFormat, new JLabel("Destination"), inTextLocation(location, new JButton("..."))};
        left.setLayout(new GridLayout(5,1));

        for(Component c: components)
            left.add(c);

        return left;
    }

    public JTextField getLocationInput() {
        return location;
    }

    public JTextField getSpecificLocation() {
        return specificLocation;
    }
    public JTextField getDateText() {
        return dateText;
    }

    public String getFileFormat() {
        return fileFormat.getSelectedItem().toString();
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

        for (JButton b: buttons)
            add(getPanel(b));
    }

    private JButton createButton(String s) {
        JButton b1 = new JButton(s);
        b1.setPreferredSize(new Dimension(80,25));
        return b1;
    }

    public JButton[] getButton() {
        return buttons;
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