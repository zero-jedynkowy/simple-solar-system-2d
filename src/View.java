import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Dialog.ModalityType;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class View extends JLayeredPane implements MouseMotionListener, MouseWheelListener, MouseListener, ComponentListener
{
    //LEWY PANEL
    private JLayeredPane mainPanel;
    private Canva canva;
    private JLabel cordsLabel;
    private String defualtCordsLabel;
    private SolarSystem model;
    private String defualtScaleLabel;
    private JLabel scaleLabel;
    private JButton startStopButton;
    private JLabel speedLabel;
    private String defaultSpeedLabel;

    //PRAWY PANEL; MENU
    private JPanel menu;
    private JButton hideShowButton;
    private boolean isMenuHide;
    private JPanel menuMainContent;
    private JLabel titleProgramme;
    private Icon hideIcon;
    private Icon showIcon;

    //CANVA
    private JPopupMenu rightClickMenu;
    private JMenuItem createObjectButton;
    private JCheckBoxMenuItem axisView;
    private JCheckBoxMenuItem graphView;

    //EDIT OBJECT
    private JPopupMenu objectMenu;
    private JMenuItem editButton;
    private JMenuItem removeButton;
    private JDialog editDialog;
    private JButton setEditButton;
    private HashMap<String, JTextField> editFields;
    private HashMap<String, JLabel> editLabels;
    private String[] listEditOptions;
    
    //SIM
    private Icon startIcon;
    private Icon stopIcon;

    //LISTA
    private JTable tabela;
    private JScrollPane lista;
    private String[] tableHeader={"INDEX", "X", "Y", "RADIUS", "VELOCITY", "COLOR"};
    private DefaultTableModel mod;
    
    public View(SolarSystem model)
    {
        super();
        this.model = model;
        this.mainPanel = new JLayeredPane();
        this.menu = new JPanel();
        this.isMenuHide = true;
        this.listEditOptions = new String[]{"Mass: [kg]", "Basic velocity: [km/s]"};
        addComponentListener(this);
        this.setOpaque(true);
        this.initMainPanel();
        this.initMenu();
        this.initRightClickMenu();
        this.initEditObjectMenu();
        this.mod = new DefaultTableModel(tableHeader, 0);
        this.tabela = new JTable(mod);
        this.lista = new JScrollPane(this.tabela);
        this.menuMainContent.add(this.lista);
        //String[] item={"A","B","C","D"};
        //mod.addRow(item);
    }

    //INICJACJA INTERFEJSU CAŁEGO
    private void initMainPanel()
    {
        this.defualtCordsLabel = "(%.1f; %.1f)";
        this.defualtScaleLabel = "Scale: %.1fx";
        this.defaultSpeedLabel = "Speed: %d";
        this.add(this.mainPanel, 1);
        this.canva = new Canva(this.model);
        this.cordsLabel = new JLabel(String.format(this.defualtCordsLabel, 0.0, 0.0));
        this.mainPanel.add(this.cordsLabel, 2);
        this.mainPanel.add(this.canva, 1);
        this.cordsLabel.setFont(new Font("Arial", Font.BOLD, 15));
        this.cordsLabel.setForeground(Color.BLACK);
        this.cordsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.cordsLabel.setVerticalAlignment(SwingConstants.CENTER);
        this.mainPanel.moveToFront(this.cordsLabel);
        this.cordsLabel.setBackground(Color.orange);
        this.cordsLabel.setOpaque(true);
        this.canva.setVisible(true);
        this.mainPanel.setVisible(true);
        this.canva.addMouseMotionListener(this);
        this.canva.addMouseWheelListener(this);
        this.canva.addMouseListener(this);
        this.scaleLabel = new JLabel(String.format(this.defualtScaleLabel, 1.0));
        this.mainPanel.add(this.scaleLabel, 3);
        this.scaleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        this.scaleLabel.setForeground(Color.BLACK);
        this.scaleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.scaleLabel.setVerticalAlignment(SwingConstants.CENTER);
        this.mainPanel.moveToFront(this.scaleLabel);
        this.scaleLabel.setBackground(Color.orange);
        this.scaleLabel.setOpaque(true);
        this.startStopButton = new JButton();
        this.mainPanel.add(this.startStopButton);
        this.startStopButton.setVisible(true);
        this.mainPanel.moveToFront(this.startStopButton);
        this.startStopButton.setBackground(Color.orange);
        this.startStopButton.setBorderPainted(false);
        this.startStopButton.setFocusPainted(false);
        ImageIcon icon = new ImageIcon(getClass().getResource("images/start.png")); //new ImageIcon("images/start.png");
        Image scaleImage = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        this.startIcon = new ImageIcon(scaleImage);
        this.startStopButton.setIcon(this.startIcon);
        icon = new ImageIcon(getClass().getResource("images/pause.png")); //new ImageIcon("images/pause.png");
        scaleImage = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        this.stopIcon = new ImageIcon(scaleImage);
        this.startStopButton.addActionListener(e -> this.startStopButtonAction());
        this.speedLabel = new JLabel(String.format(this.defaultSpeedLabel, 1));
        this.speedLabel.setBackground(Color.orange);
        this.speedLabel.setFont(new Font("Arial", Font.BOLD, 15));
        this.mainPanel.add(this.speedLabel);
        this.speedLabel.setForeground(Color.BLACK);
        this.speedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.speedLabel.setOpaque(true);
        this.speedLabel.setVerticalAlignment(SwingConstants.CENTER);
        this.speedLabel.addMouseWheelListener(this);
        this.mainPanel.moveToFront(this.speedLabel);
    }

    public DefaultTableModel getMod()
    {
        return this.mod;
    }

    public void startStopButtonAction()
    {
        this.model.setIsSimulationWork(!this.model.getIsSimulationWork());
        if(this.model.getIsSimulationWork())
        {
            this.startStopButton.setIcon(this.stopIcon);
            this.model.startSimulation();
        }
        else
        {
            this.startStopButton.setIcon(this.startIcon);
            this.model.stopSimulation();
        }
    }

    private void initMenu()
    {
        this.menu.setBackground(Color.WHITE);   
        this.hideShowButton = new JButton();
        this.add(this.menu, 2);
        this.menu.add(this.hideShowButton);
        this.menu.setLayout(null);
        this.moveToFront(this.menu);
        this.hideShowButton.addActionListener(e -> hideShowButtonAction());
        this.menu.setVisible(true);
        this.menu.addMouseMotionListener(this);
        this.hideShowButton.setBackground(Color.lightGray);
        this.hideShowButton.setBorderPainted(false);
        this.menuMainContent = new JPanel();
        this.menuMainContent.setLayout(null);
        this.menu.add(this.menuMainContent);
        this.initTitleLabel();
        this.menuMainContent.setVisible(true);
        this.menuMainContent.setBackground(Color.WHITE);
        ImageIcon icon = new ImageIcon(getClass().getResource("images/arrow_left.png")); //new ImageIcon("images/arrow_left.png");
        Image scaleImage = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        this.showIcon = new ImageIcon(scaleImage);
        icon = new ImageIcon(getClass().getResource("images/arrow_right.png")); //new ImageIcon("images/arrow_right.png");
        scaleImage = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        this.hideIcon = new ImageIcon(scaleImage);
        this.hideShowButton.setIcon(this.showIcon);
        this.hideShowButton.setFocusable(false);
    }

    public void initTitleLabel()
    {
        this.titleProgramme = new JLabel("Simulator");
        this.menuMainContent.add(this.titleProgramme);
        this.titleProgramme.setVisible(true);
        this.titleProgramme.setHorizontalAlignment(SwingConstants.CENTER);
        this.titleProgramme.setFont(new Font(this.titleProgramme.getFont().getName(), Font.BOLD, 25));
    }

    private void initRightClickMenu()
    {
        this.rightClickMenu = new JPopupMenu();
        this.createObjectButton = new JMenuItem("Create a new object");
        this.rightClickMenu.add(this.createObjectButton);
        this.axisView = new JCheckBoxMenuItem("Axis", true);
        this.graphView = new JCheckBoxMenuItem("Graph", false);
        this.rightClickMenu.add(this.axisView);
        this.rightClickMenu.add(this.graphView);
        this.axisView.addActionListener(e -> {this.model.setIsAxisOn(!this.model.getIsAxisOn());});
        this.graphView.addActionListener(e -> {this.model.setIsChartOn(!this.model.getIsChartOn());});
        this.createObjectButton.addActionListener(e -> this.model.setIsCreateObjectMode(true));
    }

    private void initEditObjectMenu()
    {
        this.objectMenu = new JPopupMenu();
        this.editButton = new JMenuItem("Edit an object");
        this.objectMenu.add(this.editButton);
        this.removeButton = new JMenuItem("Remove an object");
        this.objectMenu.add(this.removeButton);
        this.editDialog = new JDialog((JFrame)this.getParent(), "Edit an object", ModalityType.TOOLKIT_MODAL);
        this.editDialog.setBounds(0, 0, 200, 350);
        this.editFields = new HashMap<>();
        this.editLabels = new HashMap<>();
        for(String a : this.listEditOptions)
        {
            this.editFields.put(a, new JTextField());
            this.editLabels.put(a, new JLabel());
            this.editDialog.add(this.editLabels.get(a));
            this.editDialog.add(this.editFields.get(a));
        }
        this.editDialog.setLayout(null);
        int counter = 0;
        for(String a : this.listEditOptions)
        {
            this.editLabels.get(a).setBounds(0, counter, 200, 50);
            this.editLabels.get(a).setHorizontalAlignment(SwingConstants.CENTER);
            this.editLabels.get(a).setVerticalAlignment(SwingConstants.CENTER);
            this.editFields.get(a).setBounds(50, counter + 50, 100, 50);
            this.editLabels.get(a).setText(a);
            counter += 100;
            this.editLabels.get(a).setVisible(true);
            this.editFields.get(a).setVisible(true);
        }
        this.setEditButton = new JButton("Set");
        this.setEditButton.addActionListener(e -> this.setNewEditDialogStartValues());
        this.editDialog.add(this.setEditButton);
        this.editDialog.setResizable(false);
        this.setEditButton.setBounds(50, counter + 25, 100, 50);
        this.editDialog.setLocationRelativeTo(null);
        this.editButton.addActionListener(e -> this.editDialog.setVisible(true));
        this.removeButton.addActionListener(e -> {this.model.getObjects().remove(this.model.getChoosenObject()); this.updateTable();});
    }

    public void updateTable()
    {
        while(true)
        {
            if(this.mod.getRowCount() == 0) break;
            this.mod.removeRow(0);
        }
        
        for(int i=0; i<this.model.getObjects().size(); i++)
        {
            String x[] = {"", "", "", "", "", ""};
            x[0] = Integer.toString(i);
            x[1] = Double.toString(this.model.getObjects().get(i).getCenterX());
            x[2] = Double.toString(this.model.getObjects().get(i).getCenterY());
            x[3] = Double.toString(this.model.getObjects().get(i).getRadius());
            x[4] = Double.toString(this.model.getObjects().get(i).getBasicVelocity());
            x[5] = Double.toString(this.model.getObjects().get(i).getBasicVelocity());
            this.mod.addRow(x);
        }
    }

    public void setNewEditDialogStartValues()
    {
        try
        {
            this.model.getObjects().get(this.model.getChoosenObject()).setMass(Double.valueOf(this.editFields.get(this.listEditOptions[0]).getText()));
            this.model.getObjects().get(this.model.getChoosenObject()).setBasicVelocity(Double.valueOf(this.editFields.get(this.listEditOptions[1]).getText()));
        }
        catch(Exception e)
        {
            new JOptionPane().showMessageDialog(this.editDialog,"Wrong data in some field! Please check the entry data and try again!","Wrong data!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setStartEditDialogStartValues()
    {
        this.editFields.get(this.listEditOptions[0]).setText(Double.toString(this.model.getObjects().get(this.model.getChoosenObject()).getMass()));
        this.editFields.get(this.listEditOptions[1]).setText(Double.toString(this.model.getObjects().get(this.model.getChoosenObject()).getBasicVelocity()));
    }

    //INNE
    public void updateCordsLabel()
    {
        this.cordsLabel.setText(String.format(this.defualtCordsLabel, this.model.getScaledMouseX(), this.model.getScaledMouseY()));
        this.cordsLabel.setBounds(15, this.mainPanel.getHeight() - 50 - 15, this.cordsLabel.getText().length()*10, 50);
    }

    public void updateScaleLabel()
    {
        this.scaleLabel.setText(String.format(this.defualtScaleLabel, this.model.getScale()));
        this.scaleLabel.setBounds(15, 15, this.scaleLabel.getText().length()*10, 50);
    }

    public void updateResizeView()
    {
        this.mainPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.canva.setBounds(0, 0, this.mainPanel.getWidth(), this.mainPanel.getHeight());
        this.hideShowButton.setBounds(0, 0, 30, this.getHeight());
        if(this.isMenuHide)
        {
            this.menu.setBounds(this.getWidth() - 30, 0, 30, this.getHeight());
        }
        else 
        {
            this.menu.setBounds((int)(this.getWidth()*0.8), 0, (int)(this.getWidth()*0.2), this.getHeight());
        }
        this.menuMainContent.setBounds(this.hideShowButton.getWidth(), 0, this.menu.getWidth() - this.hideShowButton.getWidth(), this.getHeight());
        this.cordsLabel.setBounds(15, this.mainPanel.getHeight() - 50 - 15, this.cordsLabel.getText().length()*10, 50);
        this.scaleLabel.setBounds(15, 15, this.scaleLabel.getText().length()*10, 50);
        this.titleProgramme.setBounds(0, 0, this.menuMainContent.getWidth(), 50);
        this.axisView.setBounds(this.menuMainContent.getWidth()/2 - 50, this.titleProgramme.getHeight(), 100, 50);
        this.startStopButton.setBounds(this.mainPanel.getWidth() - this.menu.getWidth() - 65, this.mainPanel.getHeight() - 50 - 15, 50, 50);
        this.speedLabel.setBounds(15, this.mainPanel.getHeight() - 100 - 30, this.speedLabel.getText().length()*10, 50);
        this.lista.setBounds(15, this.titleProgramme.getHeight(), this.menuMainContent.getWidth() - 30, 300);
        this.tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    private void hideShowButtonAction()
    {
        this.isMenuHide = !this.isMenuHide;
        if(this.isMenuHide) this.hideShowButton.setIcon(this.showIcon);
        else this.hideShowButton.setIcon(this.hideIcon);
        this.updateResizeView();
    }

    //METEODY SŁUCHACZY?!
    @Override
    public void mouseDragged(MouseEvent e) 
    {
        if(e.getSource() == this.canva && !this.model.getIsCreateObjectMode())
        {
            this.model.updatePositionWholeSystem(e, true);
            
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) 
    {
        if(e.getSource() == this.canva)
        {
            this.model.setRelativeMouseX(e.getX());
            this.model.setRelativeMouseY(e.getY());
            this.updateCordsLabel();
        }
        this.model.updateObjectsRelativeCords();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) 
    {
        if(e.getSource() == this.speedLabel)
        {
            this.model.changeSpeed(e.getWheelRotation());
            this.speedLabel.setText(String.format(this.defaultSpeedLabel, SolarObject.getSpeed()));
        }
        else
        {
            if(!this.model.getIsCreateObjectMode())
            {
                this.model.setScale(e.getWheelRotation());
                this.model.updateObjectsRelativeCords();
                this.updateScaleLabel();
                
            }
            this.model.setRelativeMouseX(e.getX());
            this.model.setRelativeMouseY(e.getY());
            this.updateCordsLabel();
        }
        this.updateResizeView();
    }

    @Override
    public void mouseClicked(MouseEvent e) 
    {
        if(e.getSource() == this.canva && e.getButton() == 3 && !this.model.getIsCreateObjectMode())
        {
            boolean flag = true;
            for(int i=0; i<this.model.getObjects().size(); i++)
            {
                if(this.model.getObjects().get(i).wasItInsideMe(this.model.getScaledMouseX(), this.model.getScaledMouseY()))
                {
                    flag = !flag;
                    this.objectMenu.show(canva, e.getX(), e.getY());
                    this.model.setChoosenObject(i);
                    this.setStartEditDialogStartValues();
                }
                
            }
            if(flag) this.rightClickMenu.show(canva, e.getX(), e.getY());
        }
        else if(this.model.getIsCreateObjectMode() && e.getButton() == 3)
        {
            this.model.setIsCreateObjectMode(false);
            this.model.setClickCounter(0);
        }
        else if(this.model.getIsCreateObjectMode() && e.getButton() == 1)
        {
            this.model.createObject(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) 
    {}

    @Override
    public void mouseExited(MouseEvent e) 
    {}

    @Override
    public void mousePressed(MouseEvent e) 
    {
        this.model.updatePositionWholeSystem(e, false);
    }

    @Override
    public void mouseReleased(MouseEvent e) 
    {}

    @Override
    public void componentHidden(ComponentEvent e) 
    {}

    @Override
    public void componentMoved(ComponentEvent e) 
    {}

    @Override
    public void componentResized(ComponentEvent e)
    {
        try
        {
            this.updateResizeView();
        }
        catch(Exception x)
        {}
    } 

    @Override
    public void componentShown(ComponentEvent e)
    {}
}