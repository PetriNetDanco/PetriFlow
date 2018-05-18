package exercise3;

import java.util.Set;
import java.util.HashSet;

public class Transition extends Vertex
{
    private Set<PlaceToTransition> inputArcs = new HashSet<>();
    private Set<TransitionToPlace> outputArcs = new HashSet<>();

    protected Transition(int id, int xCoordinate, int yCoordinate, String label)
    {
        super(id, xCoordinate, yCoordinate, label);
    }

    protected boolean canFire()
    {
        for (PlaceToTransition inputArc : inputArcs)
        {
            if (!inputArc.isSatisfied())
            {
                return false;
            }
        }
        return true;
    }

    protected void fire()
    {
        if (!canFire())
            throw new CannotFireTransitionException("Transition " + getLabel() + "can not fire!");

        for (PlaceToTransition inputArc : inputArcs)
        {
            inputArc.consume();
        }

        for (TransitionToPlace outputArc : outputArcs)
        {
            outputArc.produce();
        }
    }

    protected void addInputArc(PlaceToTransition arc) throws IllegalParameterException
    {
        inputArcs.add(arc);
    }

    protected void addOutputArc(TransitionToPlace arc) throws IllegalParameterException
    {
        outputArcs.add(arc);
    }

    protected Set<PlaceToTransition> getInputArcs()
    {
        return inputArcs;
    }

    protected Set<TransitionToPlace> getOutputArcs()
    {
        return outputArcs;
    }
}
