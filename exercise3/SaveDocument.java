package exercise3;

public class SaveDocument extends MyCanvas
{
    public SaveDocument() {
    }

       protected void savePlaces(exercise3_generated_to_xml.Document document, PetriNet petriNet)
    {
        if(document.getPlace().isEmpty())
        {
            for (Place place : petriNet.getPlaces())
            {
                document.getPlace().add(parsePlaceToXML(place));
            }
        }
        else
        {
            document.getPlace().clear();
            for (Place place : petriNet.getPlaces())
            {
                document.getPlace().add(parsePlaceToXML(place));
            }
        }

    }

    protected void saveTransitions(exercise3_generated_to_xml.Document document, PetriNet petriNet)
    {
        if (document.getTransition().isEmpty())
        {
            for (Transition transition : petriNet.getTransitions())
            {
                document.getTransition().add(parseTransitionToXML(transition));
            }
        }
        else
        {
            document.getTransition().clear();
            for (Transition transition : petriNet.getTransitions())
            {
                document.getTransition().add(parseTransitionToXML(transition));
            }
        }
    }

    protected void saveArcs(exercise3_generated_to_xml.Document document, PetriNet petriNet)
    {
        for (Transition transition : petriNet.getTransitions()) {

                for (Arc arc : petriNet.getInputArcs(transition)) {
                    document.getArc().add(parseArcToXML(arc));
                }
                for (Arc arc : petriNet.getOutputArcs(transition)) {
                    document.getArc().add(parseArcToXML(arc));
                }
        }
    }

    private exercise3_generated_to_xml.Place parsePlaceToXML(Place place)
    {
        exercise3_generated_to_xml.Place place1 = new exercise3_generated_to_xml.Place();
        place1.setId((short) place.getID());
        place1.setLabel(place.getLabel());
        place1.setTokens((short) place.getTokens());
        place1.setX((short) place.getXCoordinate());
        place1.setY((short) place.getYCoordinate());
        place1.setStatic(false);
        return place1;
    }

    private exercise3_generated_to_xml.Transition parseTransitionToXML(Transition transition)
    {
        exercise3_generated_to_xml.Transition transition1 = new exercise3_generated_to_xml.Transition();
        transition1.setId((short) transition.getID());
        transition1.setLabel(transition.getLabel());
        transition1.setX((short) transition.getXCoordinate());
        transition1.setY((short) transition.getYCoordinate());
        return transition1;
    }

    private exercise3_generated_to_xml.Arc parseArcToXML(Arc arc)
    {
        exercise3_generated_to_xml.Arc arc1 = new exercise3_generated_to_xml.Arc();
        arc1.setSourceId((short) arc.getSource().getID());
        arc1.setDestinationId((short) arc.getDestination().getID());
        arc1.setMultiplicity((short) arc.getWeightOfArc());
        arc1.setType("regular");
        return arc1;
    }
}