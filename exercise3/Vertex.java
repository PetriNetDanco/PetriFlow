package exercise3;

public abstract class Vertex
{
    private int id;
    private int xCoordinate;
    private int yCoordinate;
    private String label;

    protected Vertex(int id, int xCoordinate, int yCoordinate, String label)
    {
        setLabel(label);
        setXCoordinate(xCoordinate);
        setYCoordinate(yCoordinate);
        setID(id);

    }

    public int getID()
    {
        return id;
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public int getXCoordinate()
    {
        return xCoordinate;
    }

    public void setXCoordinate(int xCoordinate)
    {
        this.xCoordinate = xCoordinate;
    }

    public int getYCoordinate()
    {
        return yCoordinate;
    }

    public void setYCoordinate(int yCoordinate)
    {
        this.yCoordinate = yCoordinate;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

}