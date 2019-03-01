package example.my.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private TextView  resultScreen;                         // View  результата
    private TextView  InScreen;                             // View вводимой формулы
    private String resultText = "Здесь будет результат";    // Текс результата
    public String InText="";             // Текст вводимой формулы
    private float subresult;// не могу перекоммитить 2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        resultScreen = findViewById(R.id.textViewRes);// View  результата
        resultScreen.setText(resultText);
        InScreen = findViewById(R.id.textViewIn);     // View вводимой формулы
        InScreen.setText(InText);
    }
    private void updateIn(){
        InScreen.setText(InText);
    }
    private void clear(){
        InText = "";
        resultText = "";
        subresult = 0;
    }
    public void onClickBack(View v){

        InText =  InText.length () > 0 ? InText.substring(0, InText.length()-1) : "";
        updateIn();
    }
    public void onClickButton(View v){
        Button b = (Button) v;
        if ( isOperator ( null,b ) &&  enableOperator ( InText,b)) InText +=  b.getText(); // Разрешение на ввод операторов
        if ( lastCharIsNum((String) b.getText ()) && enableNum( InText,b)) InText +=  b.getText(); // Разрешение на ввод цифр
        if (((String) b.getText ()).equals ( "/" ) && enableDr ( InText,b ) ) InText +=  b.getText(); // Разрешение на ввод "-"
        if (((String) b.getText ()).equals ( "(" ) && enableSkOp ( InText,b ) ) InText +=  b.getText(); // Разрешение на ввод "("
        if (((String) b.getText ()).equals ( ")" ) && enableSkCl ( InText,b ) ) InText +=  b.getText(); // Разрешение на ввод ")"
 //       if (b.getText().equals ( ")" ) && enableSkCl ( InText )) InText +=  b.getText(); // Разрешение на ввод скобки закрытия
 //       if (b.getText().equals ( "(" ) ) InText +=  b.getText(); // Разрешение на ввод скобки открытия

        System.out.println("Длина  Последний символ = "+lastCharIsNum((String) b.getText ()));
        System.out.println("Длина  Последний символ = "+lastCharIsNum(InText));

        updateIn();
    }

    public void onClickClear(View v){
        InText = "";
        updateIn();
    }
    public void setSubresult(float subresult) {
        this.subresult = subresult;
    }
    public static int countSymbol(String haystack, char needle)
    {
        int count = 0;
        for (int i=0; i < haystack.length(); i++)
        {
            if (haystack.charAt(i) == needle)
            {
                count++;
            }
        }
        return count;
    }

   //////////////************** Чем является символ
public static boolean isOperator( String s,Button x)  // Проверка является ли последний символ оператором
{
    String Char;
    Char = (String) x.getText ();
    if (Char.equals ("-") || Char.equals ( "÷" ) || Char.equals ( "x" ) || Char.equals ( "+" )) return true;
    return  false;
    }
/*    public static boolean lastCharIsNum(String str) {
        String x=lastChar ( str );
        try {
            Integer.parseInt(x);
        } catch (Exception e) {
            return false;
        }
        return true;
    }*/
    public static boolean lastCharIsNum(String str)  // Проверка является ли последний символ числом
    {
        String x=lastChar ( str );
        if (x.equals ( "0" ) || x.equals ( "1" ) || x.equals ( "2" ) || x.equals ( "3" ) || x.equals ( "4" ) || x.equals ( "5" )
                || x.equals ( "6" ) || x.equals ( "7" ) || x.equals ( "8" ) || x.equals ( "9" ))
        {System.out.println("Длина  lastChar = true"); return true;} else
        return  false;
    }
//////////////************** Чем является символ

////////////// ************* Проверка разрешения на ввод символа
public static boolean enableMin(String str) // Minus "-"
{
    char x;
    x = str.charAt ( str.length ()-1 );
    if ( x == ')' ||x == '.' || str.length ()==0 || lastCharIsNum ( str ))
        return true;
        return false;
}
    public static String lastChar(String x) // Minus "-"
    {
        if (x.length ()<=0) return "";
        return String.valueOf(x.charAt ( x.length ()-1 ));
    }

    public static boolean enableNum(String str, Button b) // Num "1,2,3,4,5,6,7,8,9,0"
    {
        String x = lastChar ( str );
        if (x.equals ( "(" ) || x.equals ( "." ) || x.equals ( "/" ) || x.equals ( "" ) || lastCharIsNum ( str )
        || x.equals ( "x" ) || x.equals ( "-" ) || x.equals ( "÷" ) || x.equals ( "+" ) )
        return true;
        return false;
    }
    public static boolean enableDr(String str, Button b) // Разрешение на ввод дроби
    {
        String x = lastChar ( str );
        if (x.equals ( ")" ) || lastCharIsNum ( str ) )
            return true;
        return false;
    }
    public static boolean enableSkOp(String str, Button b) // Разрешение на ввод открытой скобки "("
    {
        String x = lastChar ( str );
        if (x.equals ( "" ) || x.equals ( "-" ) || x.equals ( "+" ) ||x.equals ( "x" ) ||x.equals ( "÷" ) ||x.equals ( "(" ))
            return true;
        return false;
    }
    public static boolean enableSkCl(String str, Button b)  // Проверка является ли последний символ числом
    {
        String x = lastChar ( str );
        if (countSymbol (str,'(' ) > countSymbol (str,')' ))
        if (x.equals ( ")" ) || lastCharIsNum ( str ))
            return true;
        return false;
    }
    public static boolean enableOperator(String str, Button btn)  // Проверка является ли последний символ числом
    {
        //  System.out.println("Длина  cstr "+ (str.length ()-1));
        if (lastChar ( str ).equals ( ")" ) || lastCharIsNum ( str )) return true;
        return  false;

    }
}
