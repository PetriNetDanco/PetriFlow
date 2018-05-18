package exercise3;

public abstract class Arc
{
    private Vertex source;
    private Vertex destination;
    private int weightOfArc = 1;

    protected Arc(Vertex source, Vertex destination, int weightOfArc) throws IllegalParameterException
    {
        this(source, destination);
        setWeightOfArc(weightOfArc);
    }

    protected Arc(Vertex source, Vertex destination)
    {
        if (source == null || destination == null)
            throw new IllegalParameterException("Can not create arc from/to null vertex!");

        this.source = source;
        this.destination = destination;
    }

    protected int getWeightOfArc()
    {
        return weightOfArc;
    }

    protected void setWeightOfArc(int weightOfArc) throws IllegalParameterException
    {
        if (weightOfArc < 1)
            throw new IllegalParameterException("Multiplicity can not be less than 1.");

        this.weightOfArc = weightOfArc;
    }

    protected Vertex getSource()
    {
        return source;
    }

    protected Vertex getDestination()
    {
        return destination;
    }

    protected abstract Place getPlace();

    protected abstract Transition getTransition();

}
