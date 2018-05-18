package exercise3;

public class Place extends Vertex
{
    private int tokens = 0;

    protected Place(int id, int marking, int xCoordinate, int yCoordinate, String label)
    {
        this(id, xCoordinate, yCoordinate, label);
        setTokens(marking);
    }

    protected Place(int id, int xCoordinate, int yCoordinate, String label)
    {
        super(id, xCoordinate, yCoordinate, label);
    }

    protected int getTokens()
    {
        return tokens;
    }

    protected void setTokens(int tokens) throws IllegalParameterException
    {
        if (tokens < 0)
            throw new IllegalParameterException("Place can not have negative number of tokens!");

        this.tokens = tokens;
    }

    protected void increaseMarking(int value) throws IllegalParameterException
    {
        setTokens(tokens + value);
    }

    protected void decreaseMarking(int value) throws IllegalArgumentException
    {
        setTokens(tokens - value);
    }


}
