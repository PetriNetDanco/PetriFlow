package exercise3;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.text.AttributedString;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class MyCanvas extends Canvas implements MouseListener, ActionListener, MouseMotionListener
{
    private PetriNet petriNet;
    private Set<Integer> transitionIDs;
    private Set<Integer> placeIDs;
    private List<MyButton> buttons =  new ArrayList<MyButton>();
    private List<Integer> vertices = new ArrayList<Integer>() ;
    private int counter = 0;
    private int count = 0;
    private int moveID = -1;
    private Map<PlaceToTransition, Line2D.Double> canvasInput = new ConcurrentHashMap<>();
    private Map<TransitionToPlace, Line2D.Double> canvasOutput = new ConcurrentHashMap<>();

    MyCanvas()
    {
        Dimension d = getSize();
        setSize(d.width, d.height);
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paint(Graphics g)
    {
        try {
            for (int i : transitionIDs) {
                drawTransition(petriNet.findTransition(i));
                drawArcs(petriNet.findTransition(i).getInputArcs(), petriNet.findTransition(i).getOutputArcs());
            }
            for (int i : placeIDs) {
                drawPlace(petriNet.findPlace(i));
            }
            reDrawVertex();
        }catch (Exception paint){  }

    }

    protected void drawPlace(Place place)
    {
        Graphics g = getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.clearRect(place.getXCoordinate(), place.getYCoordinate(), 38, 38);
        g2.setColor(Color.BLACK);
        g2.drawOval(place.getXCoordinate(), place.getYCoordinate(), 40, 40);
        printOutTokenStatus(place);
    }

    protected void drawTransition(Transition transition)
    {
        Graphics g = getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.clearRect(transition.getXCoordinate(), transition.getYCoordinate(), 41, 41);

        if(!transition.canFire())
        {
            g.setColor(Color.RED);
            g.drawRect(transition.getXCoordinate(), transition.getYCoordinate(), 40, 40);
        }
        else if(transition.canFire())
        {
            g.setColor(Color.GREEN);
            g.fillRect(transition.getXCoordinate(), transition.getYCoordinate(), 40, 40);
        }
    }

    protected void drawArcs(Set<PlaceToTransition> placeToTransition, Set<TransitionToPlace> transitionToPlace)
    {
        AffineTransform tx;
        Line2D.Double line;
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(0,5);
        arrowHead.addPoint(-5,-5);
        arrowHead.addPoint(5,-5);
        Graphics g = getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for(PlaceToTransition placeToTransition2 : placeToTransition)
        {
            tx = new AffineTransform();
            line = new Line2D.Double(placeToTransition2.getPlace().getXCoordinate()+35, placeToTransition2.getPlace().getYCoordinate()+19, placeToTransition2.getTransition().getXCoordinate() + 20, placeToTransition2.getTransition().getYCoordinate()-3);
            drawArrowHead(line, tx, arrowHead);

            if(placeToTransition2.getWeightOfArc()>1)
                g.drawString(String.valueOf(placeToTransition2.getWeightOfArc()),(placeToTransition2.getPlace().getXCoordinate() + placeToTransition2.getTransition().getXCoordinate())/2, (placeToTransition2.getPlace().getYCoordinate() + placeToTransition2.getTransition().getYCoordinate())/2);
            g.setColor(Color.BLACK);
            line = new Line2D.Double(placeToTransition2.getPlace().getXCoordinate() + 20, placeToTransition2.getPlace().getYCoordinate() + 20, placeToTransition2.getTransition().getXCoordinate() + 20, placeToTransition2.getTransition().getYCoordinate() + 20);
            g.drawLine(placeToTransition2.getPlace().getXCoordinate() + 20, placeToTransition2.getPlace().getYCoordinate() + 20, placeToTransition2.getTransition().getXCoordinate() + 20, placeToTransition2.getTransition().getYCoordinate() + 20);

            canvasInput.put(placeToTransition2, line);
        }

        for(TransitionToPlace transitionToPlace2 : transitionToPlace)
        {
            tx = new AffineTransform();
            line = new Line2D.Double(transitionToPlace2.getTransition().getXCoordinate()+35, transitionToPlace2.getTransition().getYCoordinate()+19, transitionToPlace2.getPlace().getXCoordinate() + 20, transitionToPlace2.getPlace().getYCoordinate()-3);

            drawArrowHead(line, tx, arrowHead);

            if(transitionToPlace2.getWeightOfArc()>1)
                g.drawString(String.valueOf(transitionToPlace2.getWeightOfArc()),(transitionToPlace2.getPlace().getXCoordinate() + transitionToPlace2.getTransition().getXCoordinate())/2, (transitionToPlace2.getPlace().getYCoordinate() + transitionToPlace2.getTransition().getYCoordinate())/2);            g.setColor(Color.BLACK);
            line = new Line2D.Double(transitionToPlace2.getPlace().getXCoordinate() + 20, transitionToPlace2.getPlace().getYCoordinate() + 20, transitionToPlace2.getTransition().getXCoordinate() + 20, transitionToPlace2.getTransition().getYCoordinate() + 20);

            g.drawLine(transitionToPlace2.getPlace().getXCoordinate() + 20, transitionToPlace2.getPlace().getYCoordinate() + 20, transitionToPlace2.getTransition().getXCoordinate() + 20, transitionToPlace2.getTransition().getYCoordinate() + 20);
            canvasOutput.put(transitionToPlace2, line);
        }

    }



    protected void printOutTokenStatus(Place place)
    {
        Graphics g = getGraphics();
        if(place.getTokens() < 10)  drawTokens(place.getTokens(),place.getXCoordinate(), place.getYCoordinate() , g);
        else {
            Font font = new Font("TimesNewRoman", Font.PLAIN, 17);
            String txt = Integer.toString(place.getTokens());
            AttributedString number = new AttributedString(txt);
            number.addAttribute(TextAttribute.FONT, font);
            g.drawString(number.getIterator(), place.getXCoordinate() + 14, place.getYCoordinate() + 27);
        }
    }

    protected  void clearScreen()
    {
        Graphics g = getGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());
    }

    private void drawArrowHead(Line2D.Double line, AffineTransform tx, Polygon arrowHead) {
        tx.setToIdentity();
        double angle = Math.atan2(line.y2-line.y1, line.x2-line.x1);
        tx.translate(line.x2, line.y2);
        tx.rotate((angle-Math.PI/2d));

        Graphics2D g = (Graphics2D) getGraphics();
        g.setTransform(tx);
        g.fill(arrowHead);
        g.dispose();
    }

    //  BONUS

    public void drawTokens(int tokens, int xCoordinate, int yCoordinate, Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if(tokens == 1)  g.fillOval(xCoordinate +18, yCoordinate + 18, 7, 7);
            if(tokens == 2) {g.fillOval(xCoordinate +7, yCoordinate + 28, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 7, 7, 7);}
            if(tokens == 3) {g.fillOval(xCoordinate +7, yCoordinate + 30, 7, 7);
                             g.fillOval(xCoordinate +18, yCoordinate + 18, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 7, 7, 7);}
            if(tokens == 4) {g.fillOval(xCoordinate +7, yCoordinate + 28, 7, 7);
                             g.fillOval(xCoordinate +7, yCoordinate + 7, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 7, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 28, 7, 7);}
            if(tokens == 5) {g.fillOval(xCoordinate +7, yCoordinate + 28, 7, 7);
                             g.fillOval(xCoordinate +7, yCoordinate + 7, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 7, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 28, 7, 7);
                             g.fillOval(xCoordinate +18, yCoordinate + 18, 7, 7);}
            if(tokens == 6) {g.fillOval(xCoordinate +7, yCoordinate + 28, 7, 7);
                             g.fillOval(xCoordinate +7, yCoordinate + 7, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 7, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 28, 7, 7);
                             g.fillOval(xCoordinate +7, yCoordinate + 18, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 18, 7, 7);}
            if(tokens == 7) {g.fillOval(xCoordinate +7, yCoordinate + 28, 7, 7);
                             g.fillOval(xCoordinate +7, yCoordinate + 7, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 7, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 28, 7, 7);
                             g.fillOval(xCoordinate +7, yCoordinate + 18, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 18, 7, 7);
                             g.fillOval(xCoordinate +18, yCoordinate + 18, 7, 7);}
            if(tokens == 8) {g.fillOval(xCoordinate +7, yCoordinate + 28, 7, 7);
                             g.fillOval(xCoordinate +7, yCoordinate + 7, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 7, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 28, 7, 7);
                             g.fillOval(xCoordinate +7, yCoordinate + 18, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 18, 7, 7);
                             g.fillOval(xCoordinate +18, yCoordinate + 7, 7, 7);
                             g.fillOval(xCoordinate +18, yCoordinate + 28, 7, 7);}
            if(tokens == 9) {g.fillOval(xCoordinate +7, yCoordinate + 28, 7, 7);
                             g.fillOval(xCoordinate +7, yCoordinate + 7, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 7, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 28, 7, 7);
                             g.fillOval(xCoordinate +7, yCoordinate + 18, 7, 7);
                             g.fillOval(xCoordinate +28, yCoordinate + 18, 7, 7);
                             g.fillOval(xCoordinate +18, yCoordinate + 7, 7, 7);
                             g.fillOval(xCoordinate +18, yCoordinate + 28, 7, 7);
                             g.fillOval(xCoordinate +18, yCoordinate + 18, 7, 7);}
    }

    private int clickOnTransition(int x, int y)
    {
        for(int i : this.transitionIDs)
        {
            if(locationFireTransition(this.petriNet.findTransition(i), x, y) != -1)
                return petriNet.findTransition(i).getID();
        }
        return -1;
    }

    private int locationFireTransition(Transition transition, int x, int y)
    {
        for(int k = (transition.getXCoordinate()) ; k <= (transition.getXCoordinate()+40); ++k )
        {
            for (int j = (transition.getYCoordinate()) ; j <= (transition.getYCoordinate()+40); ++j)
            {
                if(x == k && y == j)
                {
                    if(checkButtonID("Fire"))
                    transition.fire();
                    reDrawVertex();
                    return transition.getID();
                }

            }
        }
        return -1;
    }

    private void reDrawVertex()
    {
        for (int i : transitionIDs)

            drawTransition(this.petriNet.findTransition(i));

        for(int i : this.placeIDs)

            drawPlace(petriNet.findPlace(i));
    }

    private int clickOnPlace(int x, int y)
    {
        for(int i : this.placeIDs)
        {
            if(locationPlace(this.petriNet.findPlace(i), x, y) != -1)
             return petriNet.findPlace(i).getID();
        }
        return -1;

    }

    private int locationPlace(Place place, int x, int y)
    {
        for(int k = (place.getXCoordinate()) ; k <= (place.getXCoordinate()+40); ++k )
        {
            for (int j = (place.getYCoordinate()) ; j <= (place.getYCoordinate()+40); ++j)
            {
                if(x == k && y == j)
                {
                    clickedOnPlaceAfterButton(place);
                    return place.getID();
                }

            }
        }
        return -1;
    }

    private void clickedOnPlaceAfterButton(Place place)
    {
        if(checkButtonID("Increase token"))
        {
            place.increaseMarking(1);
            reDrawVertex();
        }
        if(checkButtonID("Decrease token"))
        {
            place.decreaseMarking(1);
            reDrawVertex();
        }
    }

    private void createArc(int x, int y)
    {
        int IDtransition = clickOnTransition(x, y);
        int IDplace = clickOnPlace(x, y);

        if(IDtransition > 0 ) { vertices.add(petriNet.findTransition(IDtransition).getID());
            counter++;  }

        else if(IDplace > 0) {  vertices.add(petriNet.findPlace(IDplace).getID());
            counter++;  }
        else { counter++; }

        if(counter == 0 || ( vertices.size() == 1 && counter > 1)) {    vertices.clear();
            counter = 0;    }

        if(vertices.size() == 2)
        {
            try
            {
                petriNet.createNewArc(vertices.get(0),vertices.get(1),1);
            }
            catch (WrongConnectObjectException ex)
            {
                vertices.clear();
                counter = 0;
                System.out.println(ex.getMessage());
            }
            counter = 0;
            vertices.clear();
        }
    }

    private void deleteComponent(int x, int y)
    {
        int IDtransition = clickOnTransition(x, y);
        int IDplace = clickOnPlace(x, y);
        try{deleteArc(x, y);} catch (Exception e ){}
        if(IDtransition > 0)
        {
            petriNet.deleteTransition(IDtransition);
            transitionIDs.remove(IDtransition);
            ++this.count;
        }
        else if(IDplace > 0)
        {
            petriNet.deletePlace(petriNet.findPlace(IDplace), IDplace);
            placeIDs.remove(IDplace);
            ++this.count;
        }


    }

    protected void deleteArc(int x, int y)
    {
        for(PlaceToTransition placeToTransition : canvasInput.keySet())
        {
            Line2D.Double inputArc = canvasInput.get(placeToTransition);
            if(inputArc.intersects(x,y,10,10))
            {
                for(Transition transition : petriNet.getTransitions())
                {
                    for(PlaceToTransition placeToTransition1 : transition.getInputArcs())
                    {
                        if(placeToTransition.equals(placeToTransition1))
                        {
                            transition.getInputArcs().remove(placeToTransition1);
                            canvasInput.remove(placeToTransition);
                        }
                    }
                }
            }
        }

        for (TransitionToPlace transitionToPlace : canvasOutput.keySet())
        {
            Line2D.Double outputArc = canvasOutput.get(transitionToPlace);
            if (outputArc.intersects(x,y,10,10))
            {
                for(Transition transition : petriNet.getTransitions())
                {
                    for (TransitionToPlace transitionToPlace1 : transition.getOutputArcs())
                    {
                        if(transitionToPlace.equals(transitionToPlace1))
                        {
                            transition.getOutputArcs().remove(transitionToPlace1);
                            canvasOutput.remove(transitionToPlace);
                        }
                    }
                }
            }
        }
    }


    private boolean checkButtonID(String label)
    {
        for(MyButton butt : buttons)
        {
            if(label.equals(butt.getLabel()))
            {
                return butt.getActive();
            }
        }
        return false;
    }

    protected Button addButton(String name)
    {
        Button button = new Button(name);
        buttons.add(new MyButton(name));
        button.addActionListener(this);
        return button;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        try {
            if (checkButtonID("Fire"))
            {
                if(clickOnTransition(e.getX(), e.getY()) == -1)  throw new NoVertexException ("This is not a transition.");
            }
            else if (checkButtonID("Increase token"))
            {
                if(clickOnPlace(e.getX(), e.getY()) == -1)   throw new NoVertexException ("This is not a place.");
            }

            else if (checkButtonID("Decrease token"))
            {
                if(clickOnPlace(e.getX(), e.getY()) == -1)   throw new NoVertexException ("This is not a place.");
            }
            else if(checkButtonID("Add place"))
            {
                petriNet.createNewPlace(placeIDs.size()+ transitionIDs.size()+100+this.count+1,0,e.getX(),e.getY(),"");
                placeIDs.add(placeIDs.size()+ transitionIDs.size()+100+this.count+1);
            }
            else if(checkButtonID("Add transition"))
            {
                petriNet.createNewTransition(placeIDs.size()+ transitionIDs.size()+100+this.count+1,e.getX(),e.getY(),"");
                transitionIDs.add(placeIDs.size()+ transitionIDs.size()+100+this.count+1);
            }

            else if(checkButtonID("Add arc"))
            {
               createArc(e.getX(),e.getY());
            }
            else if(checkButtonID("Delete"))
            {
                deleteComponent(e.getX(),e.getY());
            }
            repaint();

        }catch (CannotFireTransitionException | IllegalParameterException | NoVertexException el)
        {
            System.out.println(el.getMessage());
        }
    }


    @Override
    public void mousePressed(MouseEvent e)
    {
        if (checkButtonID("Move")) {
            int IDplace = clickOnPlace(e.getX(), e.getY());
            int IDtransition = clickOnTransition(e.getX(), e.getY());
            if (IDtransition > 0) {
                moveID = IDtransition;
            } else if (IDplace > 0) {
                moveID = IDplace;
            }
        }

    }

    private void moveComponent(int x, int y) {

        int IDplace = clickOnPlace(x, y);
        int IDtransition = clickOnTransition(x, y);

        if (IDplace > 0 && IDplace == moveID) {
            int index = petriNet.findPlace(moveID).getID();
            petriNet.findPlace(index).setXCoordinate((short) (x - 18));
            petriNet.findPlace(index).setYCoordinate((short) (y - 18));
        }
        if (IDtransition > 0 && IDtransition == moveID) {
            int index = petriNet.findTransition(moveID).getID();
            petriNet.findTransition(index).setXCoordinate((short) (x - 18));
            petriNet.findTransition(index).setYCoordinate((short) (y - 18));
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (checkButtonID("Move"))
        moveComponent(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public void setPetriNet(PetriNet petriNet) {
        this.petriNet = petriNet;
    }

    public void setTransitionIDs(Set<Integer> transitionIDs) {
        this.transitionIDs = transitionIDs;
    }

    public void setPlaceIDs(Set<Integer> placeIDs) {
        this.placeIDs = placeIDs;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        for(MyButton butt : buttons)
        {
            butt.checkLabel(e.getActionCommand());
        }
    }
}
