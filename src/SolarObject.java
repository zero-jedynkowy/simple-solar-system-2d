import java.awt.Color;
import java.util.Random;

public class SolarObject
{
    private int relativeCenterX; //WZGL. LEWEGO ROGU
    private int relativeCenterY; //WZGL. LEWEGO ROGU
    private double centerX; //WZGL. SRODKA UKLADU WSPOL
    private double centerY; //WZGL. SRODKA UKLADU WSPOL
    private double radius; //W JEDNOSTKACH UKLADU WSPOLRZEDNEGO
    private double endX; //WZGL. SRODKA UKLADU WSPOL
    private double endY; //WZGL. SRODKA UKLADU WSPOL
    private double mass; //MASA OBIEKTU
    private double basicVelocity; //PODSTAWOWA PREDKOSC LINIOWA
    final public static double G = 6.67430E-11; //STALA GRAWITACJI
    public final static Color[] availableColors = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.ORANGE, Color.YELLOW, Color.PINK};
    private static int speed = 1; //PREDKOSC SYMULACJI
    private SolarObject parent; //RODZIC OBIEKTU
    private boolean isWork; //CZY JEST AKTUALIZOWANY
    private Color color; //KOLOR OBIEKTU

    public SolarObject()
    {
        this.relativeCenterX = 0;
        this.relativeCenterY = 0;
        this.centerX = 0;
        this.centerY = 0;
        this.radius = 0;
        this.mass = 20;
        this.basicVelocity = 50;
        this.color = availableColors[new Random().nextInt(availableColors.length - 1)];
    }

    void setIsWork(boolean newValue)
    {
        this.isWork = newValue;
    }

    public boolean getIsWork()
    {
        return this.isWork;
    }

    public Color getColor()
    {
        return this.color;
    }

    public void getColor(Color newValue)
    {
        this.color = newValue;
    }

    public static int getSpeed()
    {
        return speed;
    }

    public static void setSpeed(int newValue)
    {
        speed = newValue;
    }

    public SolarObject getParent()
    {
        return this.parent;
    }

    public void setParent(SolarObject newValue)
    {
        this.parent = newValue;
    }

    public void updateSimulation()
    {
        double moveX = 0;
        double moveY = 0;
        for(;;)
        {
            try
            {
                Thread.sleep(1);
                for(int i=0; i<speed; i++)
                {
                    if(!this.isWork) break;
                    
                    moveX = this.basicVelocityX();
                    moveY = this.basicVelocityY();
                    moveX = convertKMtoAU(moveX);
                    moveY = convertKMtoAU(moveY);
                    this.setCenterX(this.getCenterX() - moveX);
                    this.setCenterY(this.getCenterY() + moveY);
                    moveX = this.basicAccelerationX();
                    moveY = this.basicAccelerationY();
                    moveX = convertKMtoAU(moveX);
                    moveY = convertKMtoAU(moveY);
                    this.setCenterX(this.getCenterX() - moveX/2);
                    this.setCenterY(this.getCenterY() - moveY/2);
                }
                if(!this.isWork) break;
            }
            catch(Exception a)
            {} 
        }
    }

    static public double convertAUtoKM(double au)
    {
        return 149_597_871 * au;
    }

    static public double convertKMtoAU(double km)
    {
        return km/149_597_871;
    }

    public double roadBetweenParentAndChild()
    {
        return SolarSystem.calcRoadBetweenPoints(this.parent.getCenterX(), this.parent.getCenterY(), this.getCenterX(), this.getCenterY());
    }

    public double gravityForce()
    {
        double part1 = G * this.mass * this.parent.getMass();
        double part2 = this.roadBetweenParentAndChild();
        part2 = SolarObject.convertAUtoKM(part2)*1000;
        return part1/Math.pow(part2, 2);
    }

    public double basicAccelerationX() //KM/S
    {
        double deltaX = this.getCenterX() - this.parent.getCenterX();
        double sinus = deltaX/this.roadBetweenParentAndChild();
        return ((this.gravityForce()*sinus)/this.mass)/1000;
    }

    public double basicAccelerationY() // //KM/S
    {
        double deltaY = this.getCenterY() - this.parent.getCenterY();
        double cosinus = deltaY/this.roadBetweenParentAndChild();
        return ((this.gravityForce()*cosinus)/this.mass)/1000;
    }

    public double basicVelocityX()
    {
        double deltaY = this.getCenterY() - this.parent.getCenterY();
        double cosinus = deltaY/this.roadBetweenParentAndChild();
        return this.getBasicVelocity()*cosinus;
    }

    public double basicVelocityY()
    {
        double deltaX = this.getCenterX() - this.parent.getCenterX();
        double sinus = deltaX/this.roadBetweenParentAndChild();
        return this.getBasicVelocity()*sinus;
    }

    public void setBasicVelocity(double newValue)
    {
        this.basicVelocity = newValue;
    }

    public double getBasicVelocity()
    {
        return this.basicVelocity;
    }

    public boolean wasItInsideMe(double x, double y)
    {
        double part1 = Math.pow(x - this.centerX, 2);
        double part2 = Math.pow(y - this.centerY, 2);
        return (part1 + part2 < Math.pow(radius, 2) ? true:false);
    }

    public double getMass()
    {
        return this.mass;
    }

    public void setMass(double newValue)
    {
        this.mass = newValue;
    }

    public void setEndY(double newValue)
    {
        this.endY = newValue;
    }

    public double getEndY()
    {
        return this.endY;
    }

    public void setEndX(double newValue)
    {
        this.endX = newValue;
    }

    public double getEndX()
    {
        return this.endX;
    }

    public void setRadius(double newValue)
    {
        this.radius = newValue;
    }

    public double getRadius()
    {
        return this.radius;
    }

    public void setCenterY(double newValue)
    {
        this.centerY = newValue;
    }

    public double getCenterY()
    {
        return this.centerY;
    }

    public void setCenterX(double newValue)
    {
        this.centerX = newValue;
    }

    public double getCenterX()
    {
        return this.centerX;
    }

    public void setRelativeCenterX(int newValue)
    {
        this.relativeCenterX = newValue;
    }

    public int getRelativeCenterX()
    {
        return this.relativeCenterX;
    }

    public void setRelativeCenterY(int newValue)
    {
        this.relativeCenterY = newValue;
    }

    public int getRelativeCenterY()
    {
        return this.relativeCenterY;
    }
}