import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


//Window nga musdisplay output sa OS
public class Window extends JFrame {
    NorthPanel northPanel;
    SouthPanel southPanel;
    LeftPanel leftPanel;
    RightPanel rightPanel;
    CenterPanel centerPanel;
    Data data;
    Window() {
        northPanel = new NorthPanel();
        southPanel = new SouthPanel();
        leftPanel = new LeftPanel();
        rightPanel = new RightPanel();
        centerPanel = new CenterPanel();
        data = new Data();
        JButton[] b = {centerPanel.getButton("add"),centerPanel.getButton("remove"),
                centerPanel.getButton("clear")};
        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("File Sorter");
        add(getMain());
        clickListener(b);
        this.setContentPane(getMain());
        setResizable(false);
        setVisible(true);
    }

    private void clickListener(JButton[] b) {
        JList<String> tempLeft = leftPanel.getList();
        JList<String> tempRight = rightPanel.getList();
        b[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField[] t = {northPanel.getTextField("file"),northPanel.getTextField("location")
                        ,northPanel.getTextField("specific"), northPanel.getTextField("date")};
                String specificFile=t[0].getText() + " ";
                specificFile = specificFile.concat((northPanel.isRadioTrue()) ?
                        t[3].getText()+ " " + (northPanel.isSpecificTrue() ?  t[2].getText() + " ": "")
                        : northPanel.isSpecificTrue() ?  t[2].getText() + " ": "");

                if(t[0].getText().isEmpty() || t[1].getText().isEmpty()
                        || (northPanel.isSpecificTrue() && t[2].getText().isEmpty())
                        || (northPanel.isRadioTrue() && t[3].getText().isEmpty())) return;

                data.setMap(specificFile, t[1].getText());
                leftPanel.setList(specificFile);
                rightPanel.setList(t[1].getText());
                for(JTextField text: t) {
                    text.setText("");
                }
            }
        });
        b[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel<String> modelLeft = (DefaultListModel<String>) tempLeft.getModel();
                DefaultListModel<String> modelRight = (DefaultListModel<String>) tempRight.getModel();
                int index = tempLeft.getSelectedIndex();
                if(index != -1) {
                    data.removeData(modelLeft.get(index));
                    data.printMap();
                    modelLeft.remove(index);
                    modelRight.remove(index);
                }
            }
        });
        b[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel<String> modelLeft = (DefaultListModel<String>) tempLeft.getModel();
                DefaultListModel<String> modelRight = (DefaultListModel<String>) tempRight.getModel();
                modelLeft.removeAllElements();
                modelRight.removeAllElements();
            }
        });
    }

    private JPanel getMain() {
        JPanel main = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(5,5,0,5);
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
    JTextField fileFormat, location, specificLocation,dateText;
    JCheckBox specificLoc;
    boolean radioState=false;
    NorthPanel() {
        setLayout(new GridLayout(1,2,50,0));
        setPreferredSize(new Dimension(100,120));
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
        fileFormat = new JTextField();
        location = new JTextField();
        Component[] components = {new JLabel("File Format"),
                fileFormat, new JLabel("Destination"), inTextLocation(location, new JButton("..."))};
        fileFormat.setPreferredSize(new Dimension(200,5));
        left.setLayout(new GridLayout(5,1));
        for(Component c: components)
            left.add(c);
        return left;
    }

    public JTextField getTextField(String name) {
        return switch(name) {
            case "file" -> fileFormat;
            case "location" -> location;
            case "specific" -> specificLocation;
            case "date" -> dateText;
            default -> null;
        };
    }

    public boolean isSpecificTrue() {
        return specificLoc.isSelected();
    }

    private JPanel getRightPanel() {
        JPanel right = new JPanel();
        JPanel p1 = new JPanel();
        JPanel datePanel = new JPanel(new GridLayout(1,2));
        JPanel temp = new JPanel();
        JButton save = new JButton("Save");
        JButton load = new JButton("Load");
        JButton choose = new JButton("...");
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
        save.addActionListener(openFile());
        load.addActionListener(openFile());
        JRadioButton file = new JRadioButton("File");
        JRadioButton date = new JRadioButton("Date");
        date.addItemListener(this::itemStateChanged);
        file.setSelected(true);
        right.setLayout(new GridLayout(5,1));
        p1.setLayout(new GridLayout(1,3,15,0));
        datePanel.add(date);
        datePanel.add(dateText);
        p1.add(datePanel);
        p1.add(save);
        p1.add(load);
        right.add(specificLoc);
        right.add(inTextLocation(specificLocation,choose));
        right.add(file);
        right.add(p1);
        groupRadio.add(file);
        groupRadio.add(date);
        return right;
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

    private ActionListener openFile() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.showOpenDialog(null);
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
            createButton("Clear"),createButton("Sort")};
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

    public JButton getButton(String name) {
        return switch (name) {
            case "add" -> buttons[0];
            case "remove" -> buttons[1];
            case "clear" -> buttons[2];
            case "sort" -> buttons[3];
            default -> null;
        };
    }

    private JPanel getPanel(JButton b1) {
        JPanel p1=new JPanel();
        p1.add(b1);
        return p1;
    }
}

