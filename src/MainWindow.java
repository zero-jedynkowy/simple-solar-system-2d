import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame
{   
    private int defaultWidthScreen;
    private int defaultHeightScreen;
    private SolarSystem model;
    private View view;
    private Thread updater;

    public MainWindow()
    {
        super("Simulator");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.model = new SolarSystem();
        this.setWindowSizes();
        this.initView();
        this.setVisible(true);
        this.model.setRelativeCenterX(this.view.getWidth()/2);
        this.model.setRelativeCenterY(this.view.getHeight()/2);
        this.updater = new Thread(this::updateLoop);
        this.updater.start();
        this.model.setView(this.view);
    }

    private void updateLoop()
    {
        for(;;)
        {
            try
            {
                this.repaint();
                this.model.updateObjectsRelativeCords();
                Thread.sleep(1);
            }
            catch(Exception e){};
        }
    }

    private void initView()
    {
        this.view = new View(this.model);
        this.add(this.view);
        this.view.updateResizeView();
        this.view.setVisible(true);
    }

    private void setWindowSizes()
    {
        Dimension screenSizes = Toolkit.getDefaultToolkit().getScreenSize();
        this.defaultWidthScreen = (int)screenSizes.getWidth();
        this.defaultHeightScreen = (int)screenSizes.getHeight();
        this.setMinimumSize(new Dimension(defaultWidthScreen/2, defaultHeightScreen/2));
        this.setLocation((defaultWidthScreen - defaultWidthScreen/2)/2, (defaultHeightScreen - defaultHeightScreen/2)/2);
        this.setSize(defaultWidthScreen/2, defaultHeightScreen/2);
    }
  
    public static void main(String[] args)
    {  
        new MainWindow();
    }
}
