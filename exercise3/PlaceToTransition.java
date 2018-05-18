package exercise3;

public class PlaceToTransition extends Arc
{

    protected PlaceToTransition(Place source, Transition destination, int weighOfArc) throws IllegalParameterException
    {
        super(source, destination, weighOfArc);
    }

    protected boolean isSatisfied()
    {
        if(this.getPlace().getTokens() >= this.getWeightOfArc())
            return true;
        else
            return false;
    }

    protected void consume()
    {
        if(this.getPlace().getTokens() >= this.getWeightOfArc())
            this.getPlace().decreaseMarking(this.getWeightOfArc());
    }

    @Override
    protected Place getPlace()
    {

        if(getSource() instanceof Place)
            return (Place) getSource();

        return null;
    }

    @Override
    protected Transition getTransition()
    {
        if(getDestination() instanceof Transition)
            return (Transition) getDestination();

        return null;
    }
}
