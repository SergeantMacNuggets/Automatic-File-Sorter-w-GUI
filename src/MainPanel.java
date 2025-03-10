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

public class MainPanel implements MainPanels {
    final private JList<String> tempLeft, tempRight;
    final private DefaultListModel<String> modelLeft,modelRight;
    final private Stack<String> stackLeft, stackRight;
    final private Accounts account;

    MainPanel(Accounts account) {
        this.account = account;
        tempLeft = leftPanel.getList();
        tempRight = rightPanel.getList();
        modelLeft = (DefaultListModel<String>) tempLeft.getModel();
        modelRight = (DefaultListModel<String>) tempRight.getModel();
        stackLeft = new Stack<>();
        stackRight = new Stack<>();
        allButton(account);
    }

    public JPanel getMain() {
        JPanel main = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(0,5,0,5);
        main.setBorder(padding);
        main.setLayout(new BorderLayout());

        main.add(getPanel(Panels.NORTH_PANEL),BorderLayout.NORTH);
        main.add(getPanel(Panels.SOUTH_PANEL),BorderLayout.SOUTH);
        main.add(getPanel(Panels.LEFT_PANEL),BorderLayout.WEST);
        main.add(getPanel(Panels.RIGHT_PANEL),BorderLayout.EAST);
        main.add(getPanel(Panels.CENTER_PANEL),BorderLayout.CENTER);
        return main;
    }

    private void allButton(Accounts acc) {
        JButton[] b = {getButton(Button.ADD), getButton(Button.REMOVE),getButton(Button.CLEAR),
                getButton(Button.UNDO),getButton(Button.SORT)};

        ActionListener[] events = {addListenerButton(),removeListenerButton(),
                clearListenerButton(),undoListenerButton()};

        if (acc instanceof Guest) {
            for (JButton button: b)
                button.setEnabled(false);
        }

        else if(acc instanceof Admin) {
            for(JButton button: b) {
                button.setEnabled(true);
            }
            for (int i=0;i<4;i++)
                b[i].addActionListener(events[i]);
        }
    }

    public JButton getButton(Button b) {
        return switch (b) {
            case ADD -> centerPanel.getButton()[0];

            case REMOVE -> centerPanel.getButton()[1];

            case CLEAR -> northPanel.clearButton();

            case UNDO -> centerPanel.getButton()[2];

            case SORT -> centerPanel.getButton()[3];
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

    public String getInput(Input i) {

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

    private ActionListener addListenerButton() {
        return e -> {
            try {
                JTextField[] t = {getField(Input.LOCATION)
                        ,getField(Input.SPECIFIC_LOCATION), getField(Input.DATE)};

                String specificFile = getInput(Input.FILE) + " " + getInput(Input.DATE) + " " +
                        getInput(Input.SPECIFIC_LOCATION);

                leftPanel.setList(specificFile);
                rightPanel.setList(getInput(Input.LOCATION));

                for(JTextField text: t) text.setText("");

            } catch (NullPointerException n) {
                System.out.println("Error");
            }
        };
    }

    private ActionListener removeListenerButton() {
        return e -> {
            try {
                int index = tempLeft.getSelectedIndex();
                stackLeft.push(modelLeft.get(index));
                stackRight.push(modelRight.get(index));

                if(index != -1) {
                    modelLeft.remove(index);
                    modelRight.remove(index);
                }
            } catch (ArrayIndexOutOfBoundsException s) {
                System.out.println("No Remove");
            }
        };
    }

    private ActionListener clearListenerButton() {
        return e -> {
            DefaultListModel<String> modelLeft1 = (DefaultListModel<String>) tempLeft.getModel();
            DefaultListModel<String> modelRight1 = (DefaultListModel<String>) tempRight.getModel();
            modelLeft1.removeAllElements();
            modelRight1.removeAllElements();
        };
    }

    private ActionListener undoListenerButton() {
        return e -> {
            try {
                leftPanel.setList(stackLeft.peek());
                rightPanel.setList(stackRight.peek());
                stackLeft.pop();
                stackRight.pop();
            } catch(EmptyStackException s) {
                System.out.println(s.getMessage());
            }
        };
    }

}

class NorthPanel extends JPanel implements ItemListener {
    JTextField location, specificLocation,dateText;
    JComboBox fileFormat;
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

    public JTextField getLocationInput() throws NullPointerException{

        if(location.getText().isEmpty() && location.isEnabled())
            throw new NullPointerException();

        return location;
    }

    public JTextField getSpecificLocation() throws NullPointerException {

        if(specificLocation.getText().isEmpty() && specificLocation.isEnabled())
            throw new NullPointerException();

        return specificLocation;
    }
    public JTextField getDateText() throws NullPointerException{

        if(dateText.getText().isEmpty() && dateText.isEnabled())
            throw new NullPointerException();

        return dateText;
    }

    public String getFileFormat() throws NullPointerException {
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
        temp.setLayout(new BorderLayout());
        ButtonGroup groupRadio = new ButtonGroup();
        JRadioButton file = new JRadioButton("File");
        JRadioButton date = new JRadioButton("Date");
        date.addItemListener(this);
        file.setSelected(true);
        right.setLayout(new GridLayout(5,1));
        p1.setLayout(new GridLayout(1,4,15,0));
        datePanel.add(date);
        datePanel.add(dateText);
        p1.add(datePanel);
        p1.add(new JLabel(""));
        p1.add(clear);
        right.add(new JLabel("Source Folder"));
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
    final private JList<String> list;
    final private DefaultListModel<String> listD;
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
    final private JButton[] buttons = {createButton("Add"),createButton("Remove"),
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
                new JMenu("View"), getAccount(), getHelp()};

        for(JMenu m: menu) add(m);
    }

    //Open file folder

    private JMenu getFileMenu() {
        JMenu file = new JMenu("File");
        JMenuItem[] subItem = {new JMenuItem("Print"), new JMenuItem("Quit")};

        subItem[0].addActionListener(e -> {
            //Isulat code nimo diri mer
        });

        subItem[1].addActionListener(e -> System.exit(0));
        for(JMenuItem i: subItem) file.add(i);

        return file;
    }

    private JMenu getAccount() {
        JMenu accountMenu = new JMenu("Account");
        JMenuItem[] subItem = {new JMenuItem("Sign out")};
        subItem[0].addActionListener(_->{
            Window.clearInstance();
            AccountWindow.clearInstance();
            AccountWindow.getInstance().start();
        });
        for(JMenuItem i: subItem) accountMenu.add(i);
        return accountMenu;
    }

    private JMenu getHelp() {
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        help.add(about);
        return help;
    }

}