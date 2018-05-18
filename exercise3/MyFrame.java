package exercise3;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;


public class MyFrame extends Frame implements ActionListener
{
    exercise3_generated.Document document;
    JAXBContext jaxbContext;
    File file;
    private MyCanvas myCanvas;
    private PetriNet petriNet;
    private MenuItem openFile;
    private MenuItem saveFile;

    MyFrame(String name)
    {
        super(name);

        myCanvas = new MyCanvas();
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("XML File");
        openFile = new MenuItem("Open");
        saveFile = new MenuItem("Save");
        Panel panel = new Panel();

        setMenuBar(menuBar);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);

        file.add(openFile);
        file.add(saveFile);
        menuBar.add(file);

        panel.add(myCanvas.addButton("Increase token"));
        panel.add(myCanvas.addButton("Decrease token"));
        panel.add(myCanvas.addButton("Add place"));
        panel.add(myCanvas.addButton("Add transition"));
        panel.add(myCanvas.addButton("Add arc"));
        panel.add(myCanvas.addButton("Delete"));
        panel.add(myCanvas.addButton("Move"));
        panel.add(myCanvas.addButton("Fire"));

        add("North", panel);
        add("Center", myCanvas);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

        setSize(900, 900);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == openFile)
        {
            FileDialog fileDialog = new FileDialog(this, "Open file", FileDialog.LOAD);
            fileDialog.setVisible(true);

            if(fileDialog.getFile() != null)
            {
                try
                {
                    String fileDirectory = fileDialog.getDirectory() + fileDialog.getFile();
                    file = new File(fileDirectory);
                    jaxbContext = JAXBContext.newInstance(exercise3_generated.Document.class);
                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    document = (exercise3_generated.Document) jaxbUnmarshaller.unmarshal(file);

                    myCanvas.clearScreen();
                    petriNet = new PetriNet();
                    Set<Integer> transitionIDs = new HashSet<>();
                    Set<Integer> placeIDs = new HashSet<>();

                    parsePlaces(document, petriNet, placeIDs);
                    parseTransitions(document, petriNet, transitionIDs);
                    parseArcs(document, petriNet, transitionIDs);
                    transitionArt(transitionIDs, petriNet);

                    myCanvas.setPetriNet(petriNet);
                    myCanvas.setTransitionIDs(transitionIDs);
                    myCanvas.setPlaceIDs(placeIDs);


                } catch (IllegalParameterException | WrongConnectObjectException | JAXBException ignored)
                {
                    System.out.println(ignored.getMessage());
                }
            }
        }

        if (e.getSource()==saveFile)
        {
            FileDialog SaveDialog = new FileDialog(this,"Save as",FileDialog.SAVE);
            SaveDialog.setVisible(true);
            if (SaveDialog.getFile()!=null) {
                try {
                    exercise3_generated_to_xml.Document doc = savePetriNet();
                    jaxbContext = JAXBContext.newInstance(exercise3_generated_to_xml.Document.class);
                    Marshaller marshaller = jaxbContext.createMarshaller();
                    String fileDirectory = SaveDialog.getDirectory() + SaveDialog.getFile();
                    marshaller.marshal(doc, new File(fileDirectory + ".xml"));

                } catch (JAXBException ignored) {
                    System.out.println(ignored.getMessage());
                }
            }



        }
    }

    private void parsePlaces(exercise3_generated.Document document, PetriNet petriNet, Set<Integer> placeIDs)
    {
        for(exercise3_generated.Place place : document.getPlace())
        {
            myCanvas.drawPlace(petriNet.createNewPlace(place.getId(), place.getTokens(), place.getX(), place.getY(), place.getLabel()));
            placeIDs.add((int) place.getId());
        }

    }

    private void parseTransitions(exercise3_generated.Document document, PetriNet petriNet, Set<Integer> transitionIDs)
    {
        for(exercise3_generated.Transition transition : document.getTransition())
        {
            petriNet.createNewTransition(transition.getId(), transition.getX(), transition.getY(), transition.getLabel());
            transitionIDs.add((int) transition.getId());
        }
    }

    private void parseArcs(exercise3_generated.Document document, PetriNet petriNet, Set<Integer> transitionIDs)
    {
        for (exercise3_generated.Arc arc : document.getArc())
        {
            petriNet.createNewArc(arc.getSourceId(), arc.getDestinationId(), arc.getMultiplicity());
        }

        for(int i : transitionIDs)
        {
            myCanvas.drawArcs(petriNet.findTransition(i).getInputArcs(), petriNet.findTransition(i).getOutputArcs());
        }
    }

    private void transitionArt(Set<Integer> transitionIDs, PetriNet petriNet)
    {
        for(int i : transitionIDs)
        {
            myCanvas.drawTransition(petriNet.findTransition(i));
        }
    }

    private exercise3_generated_to_xml.Document savePetriNet()
    {
        exercise3_generated_to_xml.Document document = new exercise3_generated_to_xml.Document();
        SaveDocument saveDocument = new SaveDocument();
        saveDocument.savePlaces(document,petriNet);
        saveDocument.saveTransitions(document,petriNet);
        saveDocument.saveArcs(document,petriNet);

        return document;

    }
}
