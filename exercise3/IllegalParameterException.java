package exercise3;

public class IllegalParameterException extends IllegalArgumentException
{
    IllegalParameterException(String message)
    {
        super(message);
    }
}