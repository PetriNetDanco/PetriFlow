package exercise3;

public class CannotFireTransitionException extends RuntimeException
{
    CannotFireTransitionException(String message)
    {
        super(message);
    }
}