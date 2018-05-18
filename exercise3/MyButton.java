package exercise3;


public class MyButton {

    private String label;
    private boolean active = false;


    public MyButton(String label)
    {
        this.label = label;
    }



    public void checkLabel(String label)
    {
        if(this.label == label)
        {
            this.active = true;
        }
        else
        {
            this.active = false;
        }
    }
    public boolean getActive()
    {
        return this.active;
    }
    public String getLabel()
    {
        return this.label;
    }



}

