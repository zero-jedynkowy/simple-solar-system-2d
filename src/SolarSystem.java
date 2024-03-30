import java.awt.event.MouseEvent;
import java.util.LinkedList;

class SolarSystem
{
    private final static double[] listOfScales = new double[]{0.2, 0.4, 0.6, 0.8, 1, 1.2, 1.4, 1.6, 1.8, 2, 2.2};; //LISTA SKAL
    private int actuallyChoosenScale; //AKTUALNIE WYBRANA SKALA
    final public static int MAX_CHOOSEN_INDEX_SCALE = 10; //MAX INDEKS WYBORU SKALI
    final public static int MIN_CHOOSEN_INDEX_SCALE = 0; //MIN INDEKS WYBORU SKALI
    private int relativeCenterX; //X ŚRODKA UKŁADU WSPÓŁRZĘDNYCH WZGLĘDEM LEWEGO ROGU CANVA
    private int relativeCenterY; //Y ŚRODKA UKŁADU WSPÓŁRZĘDNYCH WZGLĘDEM LEWEGO ROGU CANVA
    final public static int unit = 50;
    private int relativeMouseX; //X MYSZY WZGLĘDEM LEWEGO ROGU CANVA
    private int relativeMouseY; //Y MYSZY WZGLĘDEM LEWEGO ROGU CANVA
    private double scaledMouseX = 0; //X MYSZY NA PLANSZY WYSKALOWANE WZGLEDEM ŚRODKA WSPÓŁRZĘDNYCH
    private double scaledMouseY = 0; //Y MYSZY NA PLANSZY WYSKALOWANE WZGLEDEM ŚRODKA WSPÓŁRZĘDNYCH
    private int lastMouseClickRelativeX = 0; //WZGLĘDNE X MYSZY PO JEDNORAZOWYM KLIKNIĘCIU AKTUALIZOWANE PÓZNIEJ O KOLEJNE POZYCJE MYSZY W RUCHU
    private int lastMouseClickRelativeY = 0; //WZGLĘDNE Y MYSZY PO JEDNORAZOWYM KLIKNIĘCIU AKTUALIZOWANE PÓZNIEJ O KOLEJNE POZYCJE MYSZY W RUCHU
    private int mouseMoveDifferenceX = 0; //RÓŻNICA OSTATENIEGO POŁOŻENIA MYSZY I OBECNEGO - X; RÓZNICA lastMouseClickRelativeX i event.getX()
    private int mouseMoveDifferenceY = 0; //RÓŻNICA OSTATENIEGO POŁOŻENIA MYSZY I OBECNEGO - Y; RÓZNICA lastMouseClickRelativeY i event.getX()
    private boolean isAxisOn; //CZY OSIE SA WIDOCZNE
    private boolean isChartOn; //CZY SIATKA NA PLASZCZYZNIE JEST WIDOCZNA
    private boolean isCreateObjectMode;
    private LinkedList<SolarObject> listOfObjects;
    private SolarObject actuallyEditObject;
    private int clickCounter;
    private int choosenObject;
    private boolean isSimulationWork;
    private View view;

    public SolarSystem()
    {
        this.actuallyChoosenScale = 4;
        this.relativeCenterX = 0;
        this.relativeCenterY = 0;
        this.relativeMouseX = 0;
        this.relativeMouseY = 0;
        this.isAxisOn = true;
        this.isChartOn = false;
        this.isCreateObjectMode = false;
        this.listOfObjects = new LinkedList<SolarObject>();
        this.clickCounter = 0;
        this.actuallyEditObject = new SolarObject();
        this.isSimulationWork = false;
    }

    public void setView(View newValue)
    {
        this.view = newValue;
    }

    public void changeSpeed(int upOrDown)
    {
        if(upOrDown == -1)
        {
            if(SolarObject.getSpeed() < 1000)
            {
                SolarObject.setSpeed(1);
            }
            else
            {
                SolarObject.setSpeed(SolarObject.getSpeed() - 1000);
            }
        }
        else
        {
            if(SolarObject.getSpeed() == 1)
            {
                SolarObject.setSpeed(1000);
            }
            else if(SolarObject.getSpeed() < 500000)
            {
                SolarObject.setSpeed(SolarObject.getSpeed() + 1000);
            }
        }
    }

    public void startSimulation()
    {
        for(int i=1; i<this.listOfObjects.size(); i++)
        {
            this.listOfObjects.get(i).setParent(this.listOfObjects.getFirst());
            this.listOfObjects.get(i).setIsWork(true);
        }
        
        for(int i=1; i<this.listOfObjects.size(); i++)
        {
            new Thread(this.listOfObjects.get(i)::updateSimulation).start();
        }
    }

    public void stopSimulation()
    {
        for(int i=1; i<this.listOfObjects.size(); i++)
        {
            this.listOfObjects.get(i).setIsWork(false);
        }
    }

    public boolean getIsSimulationWork()
    {
        return this.isSimulationWork;
    }

    public void setIsSimulationWork(boolean newValue)
    {
        this.isSimulationWork = newValue;
    }

    public int getChoosenObject()
    {
        return this.choosenObject;
    }

    public void setChoosenObject(int newValue)
    {
        this.choosenObject = newValue;
    }

    public void setScaledMouseY(double newValue)
    {
        this.scaledMouseY = newValue;
    }

    public double getScaledMouseY()
    {
        return this.scaledMouseY;
    }

    public void setScaledMouseX(double newValue)
    {
        this.scaledMouseX = newValue;
    }

    public double getScaledMouseX()
    {
        return this.scaledMouseX;
    }

    public void createObject(MouseEvent e)
    {
        if(this.getClickCounter() == 1)
        {
            this.getActuallyEditObject().setEndX(this.scaledMouseX);
            this.getActuallyEditObject().setEndY(this.scaledMouseY);
            double r = SolarSystem.calcRoadBetweenPoints(this.getActuallyEditObject().getCenterX(), this.getActuallyEditObject().getCenterY(), this.getActuallyEditObject().getEndX(), this.getActuallyEditObject().getEndY());
            this.getActuallyEditObject().setRadius(r);
            this.setIsCreateObjectMode(false);
            this.setClickCounter(0);
            this.listOfObjects.add(this.actuallyEditObject);
            // this.record.add(Integer.toString(this.listOfObjects.size()-1));
            // this.record.add(Double.toString(this.actuallyEditObject.getCenterX()));
            // this.record.add(Double.toString(this.actuallyEditObject.getCenterY()));
            // this.record.add(Double.toString(this.actuallyEditObject.getRadius()));
            // this.record.add(Double.toString(this.actuallyEditObject.getBasicVelocity()));
            // this.record.add(this.actuallyEditObject.getColor().toString());
            // this.view.getMod().addRow(this.record.toArray());
            // this.record.removeAll(this.record);
            this.view.updateTable();
            this.actuallyEditObject = new SolarObject();
        }
        else
        {
            this.setClickCounter(this.getClickCounter() + 1);
            this.getActuallyEditObject().setRelativeCenterX(e.getX());
            this.getActuallyEditObject().setRelativeCenterY(e.getY());
            this.getActuallyEditObject().setCenterX(this.scaledMouseX);
            this.getActuallyEditObject().setCenterY(this.scaledMouseY);
        }
    }

    public void updatePositionWholeSystem(MouseEvent e, boolean mode)
    {
        if(mode)
        {
            this.setMouseMoveDifferenceX(this.getlastMouseClickRelativeX() - e.getX());
            this.setMouseMoveDifferenceY(this.getlastMouseClickRelativeY() - e.getY());
            this.setRelativeCenterX(this.getRelativeCenterX() - this.getMouseMoveDifferenceX());
            this.setRelativeCenterY(this.getRelativeCenterY() - this.getMouseMoveDifferenceY());
            this.updateObjectsRelativeCords();
            this.setlastMouseClickRelativeX(e.getX());
            this.setlastMouseClickRelativeY(e.getY());
        }
        else
        {
            this.setlastMouseClickRelativeX(e.getX());
            this.setlastMouseClickRelativeY(e.getY());
        }
    }

    public void updateObjectsRelativeCords()
    {
        double x = 0;
        double y = 0;
        for(int i=0; i<this.listOfObjects.size(); i++)
        {
            x = this.listOfObjects.get(i).getCenterX();
            y = this.listOfObjects.get(i).getCenterY();
            x *= this.getScale()*SolarSystem.unit;
            y *= this.getScale()*SolarSystem.unit;
            this.listOfObjects.get(i).setRelativeCenterX((int)(this.relativeCenterX + x));
            this.listOfObjects.get(i).setRelativeCenterY((int)(this.relativeCenterY - y));
        }
    }

    public static double calcRoadBetweenPoints(double x1, double y1, double x2, double y2)
    {
        double part1 = Math.pow(x1 - x2, 2);
        double part2 = Math.pow(y1 - y2, 2);
        return Math.sqrt(part1 + part2);
    }

    public LinkedList<SolarObject> getObjects()
    {
        return this.listOfObjects;
    }

    public SolarObject getActuallyEditObject()
    {
        return this.actuallyEditObject;
    }

    public int getClickCounter()
    {
        return this.clickCounter;
    }

    public void setClickCounter(int newValue)
    {
        this.clickCounter = newValue;
    }

    public void setIsCreateObjectMode(boolean newValue)
    {
        this.isCreateObjectMode = newValue;
    }

    public boolean getIsCreateObjectMode()
    {
        return this.isCreateObjectMode;
    }

    public void setIsAxisOn(boolean newValue)
    {
        this.isAxisOn = newValue;
    }

    public boolean getIsAxisOn()
    {
        return this.isAxisOn;
    }

    public void setIsChartOn(boolean newValue)
    {
        this.isChartOn = newValue;
    }

    public boolean getIsChartOn()
    {
        return this.isChartOn;
    }

    public int getlastMouseClickRelativeY()
    {
        return this.lastMouseClickRelativeY;
    }

    public void setlastMouseClickRelativeY(int newValue)
    {
        this.lastMouseClickRelativeY = newValue;
    }

    public int getlastMouseClickRelativeX()
    {
        return this.lastMouseClickRelativeX;
    }

    public void setlastMouseClickRelativeX(int newValue)
    {
        this.lastMouseClickRelativeX = newValue;
    }

    public int getMouseMoveDifferenceY()
    {
        return this.mouseMoveDifferenceY;
    }

    public void setMouseMoveDifferenceY(int newValue)
    {
        this.mouseMoveDifferenceY = newValue;
    }

    public int getMouseMoveDifferenceX()
    {
        return this.mouseMoveDifferenceX;
    }

    public void setMouseMoveDifferenceX(int newValue)
    {
        this.mouseMoveDifferenceX = newValue;
    }

    public int getActuallyChoosenScale()
    {
        return this.actuallyChoosenScale;
    }

    public int getScaledUnit()
    {
        return (int)(unit * this.getScale());
    }

    public int getRelativeMouseY()
    {
        return this.relativeMouseY;
    }

    public void setRelativeMouseY(int newValue)
    {
        this.relativeMouseY = newValue;
        this.scaledMouseY = this.relativeCenterY - this.relativeMouseY;
        this.scaledMouseY /= (this.getScale()*unit);
    }

    public int getRelativeMouseX()
    {
        return this.relativeMouseX;
    }

    public void setRelativeMouseX(int newValue)
    {
        this.relativeMouseX = newValue;
        this.scaledMouseX = this.relativeMouseX - this.relativeCenterX;
        this.scaledMouseX /= (this.getScale()*unit);
    }

    public int getRelativeCenterY()
    {
        return this.relativeCenterY;
    }

    public void setRelativeCenterY(int newValue)
    {
        this.relativeCenterY = newValue;
    }

    public int getRelativeCenterX()
    {
        return this.relativeCenterX;
    }

    public void setRelativeCenterX(int newValue)
    {
        this.relativeCenterX = newValue;
    }

    public double getScale()
    {
        return this.listOfScales[this.actuallyChoosenScale];
    }

    public void setScale(int upOrDown)
    {
        if(upOrDown == -1)
        {
            if(this.actuallyChoosenScale < MAX_CHOOSEN_INDEX_SCALE) this.actuallyChoosenScale++;
        }
        else
        {
            if(this.actuallyChoosenScale > MIN_CHOOSEN_INDEX_SCALE) this.actuallyChoosenScale--;
        }
    }
}