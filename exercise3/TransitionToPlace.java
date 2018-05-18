package exercise3;

public class TransitionToPlace extends Arc
{

    TransitionToPlace(Transition source, Place destination, int weightOfArc) throws IllegalParameterException
    {
        super(source, destination, weightOfArc);
    }

    public void produce()
    {
        ((Place) getDestination()).increaseMarking(getWeightOfArc());
    }

    @Override
    public Place getPlace()
    {
        if(getDestination() instanceof Place)
            return (Place) getDestination();

        return null;
    }

    @Override
    public Transition getTransition()
    {
        if(getSource() instanceof Transition)
            return (Transition) getSource();

        return null;
    }
}
