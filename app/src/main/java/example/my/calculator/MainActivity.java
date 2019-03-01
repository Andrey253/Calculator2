package example.my.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;


public class MainActivity extends AppCompatActivity {
    private TextView    resultScreen;                         // View  результата
    private TextView    InScreen;                             // View вводимой формулы
    private String      resultText = "";                      // Текс результата
    public String       InText="";                            // Текст вводимой формулы
    private float    subresult;                               // не могу перекоммитить 2
    Stack<String>    stackOperator = new Stack<> ();          // Стек операций
    Stack<Integer>   stackNumber =   new Stack<> ();          // Стек оперендов

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

        if ( isOperator ( null,b ) &&  enableOperator ( InText,b)) InText +=  b.getText();       // Разрешение на ввод операторов
        if ( lastCharIsNum((String) b.getText ()) && enableNum( InText,b)) InText +=  b.getText();  // Разрешение на ввод цифр
        if ( b.getText ().equals ( "/" ) && enableDr   ( InText,b ) ) InText +=  b.getText();         // Разрешение на ввод "-"
        if ( b.getText ().equals ( "(" ) && enableSkOp ( InText,b ) ) InText +=  b.getText();       // Разрешение на ввод "("
        if ( b.getText ().equals ( ")" ) && enableSkCl ( InText,b ) ) InText +=  b.getText();       // Разрешение на ввод ")"
        if ( b.getText ().equals ( "." ) && enableTchk ( InText,b ) ) InText +=  b.getText();       // Разрешение на ввод "."
        if ( b.getText ().equals ( "0" ) && lastChar   ( InText ).equals ( "/" )
         ||  b.getText ().equals ( "0" ) && lastChar   ( InText ).equals ( "÷" )) InText += "0.";     // Добавление  "." если "0" после дроби

        updateIn();
    }
    public void onClickClear(View v){
        InText = "";
        updateIn();
    }
    public void setSubresult(float subresult) {
        this.subresult = subresult;
    }
   //////////////************** Чем является символ
public static boolean isOperator( String s,Button x)  // Проверка является ли последний символ оператором
{
    String Char;
    Char = (String) x.getText ();
    if (Char.equals ("-") || Char.equals ( "÷" ) || Char.equals ( "x" ) || Char.equals ( "+" )) return true;
    return  false;
    }
    public static boolean lastCharIsNum(String str) { // Является ли последний введеный символ цифрой/числом
        String x=lastChar ( str );
        try {
            Integer.parseInt(x);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
/*    public static boolean lastCharIsNum(String str)  // Проверка является ли последний символ числом. Аналог верхней функции.
    {
        String x=lastChar ( str );
        if (x.equals ( "0" ) || x.equals ( "1" ) || x.equals ( "2" ) || x.equals ( "3" ) || x.equals ( "4" ) || x.equals ( "5" )
                || x.equals ( "6" ) || x.equals ( "7" ) || x.equals ( "8" ) || x.equals ( "9" ))
        { return true;} else
        return  false;
    }*/
//////////////************** Чем является символ

////////////// ************* Проверка разрешения на ввод символа
public static boolean enableMin(String str)                             // Minus "-"
{
    char x;
    x = str.charAt ( str.length ()-1 );
    if ( x == ')' ||x == '.' || str.length ()==0 || lastCharIsNum ( str ))
        return true;
        return false;
}
    public static String lastChar(String x)                     // Возвращает последний символ из строки или "" в формате String
    {
        if (x.length ()<=0) return "";
        return String.valueOf(x.charAt ( x.length ()-1 ));
    }

    public static boolean enableNum(String str, Button b) // Разрешение на ввод цифр
    {
        String x = lastChar ( str );
        System.out.println("Длина  1 / ="+b.getText ().equals ( "0" ));
        System.out.println("Длина  2 / ="+lastChar ( str ));
        if (!( b.getText ().equals ( "0" ) && lastChar ( str ).equals ( "/" )))
            if (!( b.getText ().equals ( "0" ) && lastChar ( str ).equals ( "÷" )))
        if (x.equals ( "(" ) || x.equals ( "." ) || x.equals ( "/" ) || x.equals ( "" ) || lastCharIsNum ( str )
        || x.equals ( "x" ) || x.equals ( "-" ) || x.equals ( "÷" ) || x.equals ( "+" ) )
        return true;
        return false;
    }
    public static boolean enableDr(String str, Button b)        // Разрешение на ввод дроби
    {
        String x = lastChar ( str );
        if (x.equals ( ")" ) || lastCharIsNum ( str ) )
            return true;
        return false;
    }
    public static boolean enableSkOp(String str, Button b)      // Разрешение на ввод открытой скобки "("
    {
        String x = lastChar ( str );
        if (x.equals ( "" ) || x.equals ( "-" ) || x.equals ( "+" ) ||x.equals ( "x" ) ||x.equals ( "÷" ) ||x.equals ( "(" ))
            return true;
        return false;
    }
    public static boolean enableSkCl(String str, Button b)          // Разрешение на ввод закрытой скобки ")"
    {
        String x = lastChar ( str );
        if (countSymbol (str,'(' ) > countSymbol (str,')' ))
        if (x.equals ( ")" ) || lastCharIsNum ( str ))
            return true;
        return false;
    }
    public static boolean enableOperator(String str, Button btn)  // Разрешение на ввод оператора
    {
        //  System.out.println("Длина  cstr "+ (str.length ()-1));
        if (lastChar ( str ).equals ( ")" ) || lastCharIsNum ( str ) || (lastChar ( str ).equals ( "-" ) || lastChar ( str )=="" )) return true;
        return  false;

    }
    public  boolean enableTchk(String str, Button btn)               // ПРазрешение на ввод точки "."
    {
        //  System.out.println("Длина  cstr "+ (str.length ()-1));
        if (lastCharIsNum ( str ) ) return true;
        return  false;

    }
    ////////////// ************* Проверка разрешения на ввод символа
    ////////////// ************* Функция подчета числа фхождений needle в haystack
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
    ////////////// ************* Функция подчета числа фхождений needle в haystack

}
