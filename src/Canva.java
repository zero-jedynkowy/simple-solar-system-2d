import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Canva extends JPanel
{
    private SolarSystem model;
    private int xAxisCounterRight;
    private int xAxisCounterLeft;
    private int yAxisCounterTop;
    private int yAxisCounterBottom;
    
    public Canva(SolarSystem model)
    {
        super();
        this.model = model;
        this.xAxisCounterLeft = 0;
        this.xAxisCounterRight = 0;
        this.yAxisCounterTop = 0;
        this.yAxisCounterBottom = 0;
        this.setBackground(Color.BLACK);
        this.setLayout(null);
    }
    
    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        if(this.model.getIsCreateObjectMode()) this.drawCreateObject(g);
        if(this.model.getIsChartOn()) this.drawNet(g);
        this.drawObjects(g);
        if(this.model.getIsAxisOn()) this.drawAxis(g);
    }

    private void drawCreateObject(Graphics g)
    {
        double r = 0;
        if(this.model.getClickCounter() == 1)
        {
            g.setColor(Color.GRAY);
            r = SolarSystem.calcRoadBetweenPoints(this.model.getActuallyEditObject().getRelativeCenterX(), this.model.getActuallyEditObject().getRelativeCenterY(), this.model.getRelativeMouseX(), this.model.getRelativeMouseY());
            g.fillOval(this.model.getActuallyEditObject().getRelativeCenterX() - (int)r, this.model.getActuallyEditObject().getRelativeCenterY() - (int)r, (int)(2*r), (int)(2*r));
            g.setColor(Color.red);
            g.drawLine(this.model.getActuallyEditObject().getRelativeCenterX(), this.model.getActuallyEditObject().getRelativeCenterY(), this.model.getRelativeMouseX(), this.model.getRelativeMouseY());
        }
        else if(this.model.getClickCounter() == 2)
        {
            r = SolarSystem.calcRoadBetweenPoints(this.model.getActuallyEditObject().getRelativeCenterX(), this.model.getActuallyEditObject().getRelativeCenterY(), this.model.getRelativeMouseX(), this.model.getRelativeMouseY());
            this.model.getActuallyEditObject().setRadius(r);
        }
        else
        {
            g.setColor(Color.RED);
            g.fillOval(this.model.getRelativeMouseX() - 4, this.model.getRelativeMouseY() - 4, 8, 8);
        }
    }

    public void drawObjects(Graphics g)
    {
        for(int i=0; i<this.model.getObjects().size(); i++)
        {
            g.setColor(this.model.getObjects().get(i).getColor());
            int x = (int)(this.model.getObjects().get(i).getRelativeCenterX());
            int y = (int)(this.model.getObjects().get(i).getRelativeCenterY());
            int r = (int)(this.model.getObjects().get(i).getRadius()*this.model.getScaledUnit());
            g.fillOval(x - r, y - r, 2*r, 2*r);
        }
    }

    private void drawAxis(Graphics g)
    {
        g.setColor(Color.red);
        
        //OGOLNE OSIE X I Y
        g.drawLine(this.model.getRelativeCenterX(), 0, this.model.getRelativeCenterX(), this.getHeight());
        g.drawLine(0, this.model.getRelativeCenterY(), this.getWidth(), this.model.getRelativeCenterY());

        //PRAWA OŚ I LICZBY
        this.xAxisCounterRight = 1;
        for(int i=this.model.getScaledUnit() + this.model.getRelativeCenterX(); i<this.getWidth(); i = i + this.model.getScaledUnit())
        {
            g.drawLine(i, this.model.getRelativeCenterY() - 5, i, this.model.getRelativeCenterY() + 5);
            if(this.model.getActuallyChoosenScale() > SolarSystem.MIN_CHOOSEN_INDEX_SCALE + 1)
            {
                g.drawString(Integer.toString(this.xAxisCounterRight), i + 10, this.model.getRelativeCenterY() + 20);
            }
            this.xAxisCounterRight += 1;
        }

        //LEWA OŚ I LICZBY
        xAxisCounterLeft = -1;
        for(int i=this.model.getRelativeCenterX() - this.model.getScaledUnit(); i>=0; i = i - this.model.getScaledUnit())
        {
            g.drawLine(i, this.model.getRelativeCenterY() - 5, i, this.model.getRelativeCenterY() + 5);
            if(this.model.getActuallyChoosenScale() > SolarSystem.MIN_CHOOSEN_INDEX_SCALE + 1)
            {
                g.drawString(Integer.toString(this.xAxisCounterLeft), i + 10, this.model.getRelativeCenterY() + 20);
            }
            this.xAxisCounterLeft -= 1;
        }
        
        //DOLNA OŚ I LICZBY
        this.yAxisCounterBottom = -1;
        for(int i = this.model.getScaledUnit() + this.model.getRelativeCenterY(); i < this.getHeight(); i = i + this.model.getScaledUnit())
        {
            g.drawLine(this.model.getRelativeCenterX() - 5, i, this.model.getRelativeCenterX() + 5, i);
            if(this.model.getActuallyChoosenScale() > SolarSystem.MIN_CHOOSEN_INDEX_SCALE + 1)
            {
                g.drawString(Integer.toString(this.yAxisCounterBottom), this.model.getRelativeCenterX() + 5, i - 10);
            }
            this.yAxisCounterBottom -= 1;
        }

        //GORNA OŚ I LICZBY
        this.yAxisCounterTop = 1;
        for(int i = this.model.getRelativeCenterY() - this.model.getScaledUnit(); i > 0; i = i - this.model.getScaledUnit())
        {
            g.drawLine(this.model.getRelativeCenterX() - 5, i, this.model.getRelativeCenterX() + 5, i);
            if(this.model.getActuallyChoosenScale() > SolarSystem.MIN_CHOOSEN_INDEX_SCALE + 1)
            {
                g.drawString(Integer.toString(this.yAxisCounterTop), this.model.getRelativeCenterX() + 5, i - 10);
            }
            this.yAxisCounterTop += 1;
        }
    }

    private void drawNet(Graphics g)
    {
        g.setColor(Color.GRAY);

        //LEWO
        for(int i=this.model.getRelativeCenterX() - this.model.getScaledUnit(); i>0; i = i - this.model.getScaledUnit())
        {
            g.drawLine(i, 0, i, this.getHeight());
        }

        //PRAWO
        for(int i=this.model.getRelativeCenterX() + this.model.getScaledUnit(); i<this.getWidth(); i = i + this.model.getScaledUnit())
        {
            g.drawLine(i, 0, i, this.getHeight());
        }

        //DÓŁ
        for(int i = this.model.getScaledUnit() + this.model.getRelativeCenterY(); i < this.getHeight(); i = i + this.model.getScaledUnit())
        {
            g.drawLine(0, i, this.getWidth(), i);
        }

        //GÓRA
        for(int i = this.model.getRelativeCenterY() - this.model.getScaledUnit(); i > 0; i = i - this.model.getScaledUnit())
        {
            g.drawLine(0, i, this.getWidth(), i);
        }

        //OGOLNE OSIE X I Y
        g.drawLine(this.model.getRelativeCenterX(), 0, this.model.getRelativeCenterX(), this.getHeight());
        g.drawLine(0, this.model.getRelativeCenterY(), this.getWidth(), this.model.getRelativeCenterY());
    }
}
