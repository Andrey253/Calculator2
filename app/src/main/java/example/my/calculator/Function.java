package example.my.calculator;

public class Function {
    public boolean enableMin(String str)
    {
        char x = str.charAt ( str.length ()-1 );
        if ( x == ')' ||x == '.' || str.length ()==0)
        return true;

        return false;
    }
}
