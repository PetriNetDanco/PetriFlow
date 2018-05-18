package exercise3;

import java.util.*;

public class PetriNet
{
    private Map<Integer, Place> places = new HashMap<>();
    private Map<Integer, Transition> transitions = new HashMap<>();

    protected Place createNewPlace(int id, int tokens, int xCoordinate, int yCoordinate, String label)
    {
        Place place = new Place(id, tokens, xCoordinate, yCoordinate, label);
        places.put(id, place);
        return place;
    }

    protected void createNewTransition(int id, int xCoordinate, int yCoordinate, String label)
    {
        Transition transition = new Transition(id, xCoordinate, yCoordinate, label);
        transitions.put(id, transition);
    }

    protected void createNewArc(int sourceID, int destinationID, int weightOfArc) throws WrongConnectObjectException
    {
        Place place;
        Transition transition;

        if(findPlace(sourceID) != null && findPlace(destinationID) != null)     throw new WrongConnectObjectException("Two places cannot be connected!");
        if(findTransition(sourceID) != null && findTransition(destinationID) != null)      throw new WrongConnectObjectException("Two transition cannot be connected!");

        place = findPlace(sourceID);
        transition = findTransition(destinationID);

        if(place != null && transition != null)
        {
            createPlaceToTransition(place, transition, weightOfArc);
        }

        place = findPlace(destinationID);
        transition = findTransition(sourceID);

        if(transition != null && place != null)
        {
            createTransitionToPlace(transition, place, weightOfArc);
        }
    }

    protected void createPlaceToTransition(Place place, Transition transition, int weightOfArc)
    {
        PlaceToTransition placeToTransition = new PlaceToTransition(place, transition, weightOfArc);
        transition.addInputArc(placeToTransition);
    }

    protected void createTransitionToPlace(Transition transition, Place place, int weightOfArc)
    {
        TransitionToPlace transitionToPlace = new TransitionToPlace(transition, place, weightOfArc);
        transition.addOutputArc(transitionToPlace);
    }

    protected Place findPlace(int id)
    {
        return places.get(id);
    }

    protected Transition findTransition(int id)
    {
        return transitions.get(id);
    }

    protected void deleteTransition(int id)
    {
        transitions.remove(id);
    }

    protected void deletePlace(Place place, int id)
    {
        for (Transition transition : transitions.values())
        {
            List<PlaceToTransition> outgoingArcs = new ArrayList<>(transition.getInputArcs());
            for (int i = 0; i < outgoingArcs.size(); ++i)
            {
                if (place.equals(outgoingArcs.get(i).getSource()))
                {
                    transition.getInputArcs().remove(outgoingArcs.get(i));
                }
            }

            List<TransitionToPlace> ingoingArcs = new ArrayList<>(transition.getOutputArcs());
            for (int i = 0; i < ingoingArcs.size(); ++i)
            {
                if (place.equals(ingoingArcs.get(i).getDestination()))
                {
                    transition.getOutputArcs().remove(ingoingArcs.get(i));
                }
            }
        }
        places.remove(id);
    }


    protected Set<Transition> getTransitions()
    {
        Set<Transition> transitions1 = new HashSet<>();
        for(int i : transitions.keySet())
        {
            transitions1.add(findTransition(i));
        }

        return transitions1;
    }

    protected Set<Place> getPlaces()
    {
        Set<Place> places1 = new HashSet<>();
        for(int i : places.keySet())
        {
            places1.add(findPlace(i));
        }

        return places1;
    }

    protected Set<PlaceToTransition> getInputArcs(Transition transition) {  return transition.getInputArcs();   }
    protected Set<TransitionToPlace> getOutputArcs(Transition transition) { return transition.getOutputArcs();  }
}
