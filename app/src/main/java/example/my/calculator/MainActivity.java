package example.my.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.SyncFailedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {
    private TextView    resultScreen;                         // View  результата
    private TextView    InScreen;                             // View вводимой формулы
    private String      resultText = "";                      // Текс результата
    public String       InText="";                            // Текст вводимой формулы
    private float    subresult;                               // не могу перекоммитить 2
    public Stack<Character> stackOperator =  new Stack ();// Стек операций
    public Stack<String> stackNumer = new Stack ();// Стек оперендов
    public int sumSkOp=0; // Количество открытых скобок

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        resultScreen = findViewById(R.id.textViewRes);// View  результата
        InScreen = findViewById(R.id.textViewIn);     // View вводимой формулы
        InScreen.setText(InText);
    }
    private void updateIn(){
        //result();
        InScreen.setText(InText);
        resultScreen.setText(resultText);
        resultText="";

    }
    private void clear(){
        InText = "";
        subresult = 0;
    }
    public void onClickBack(View v){

        InText =  InText.length () > 0 ? InText.substring(0, InText.length()-1) : "";
        updateIn();
    }
    public void onClickButton(View v){
        Button b = (Button) v;
        if ( IsNum      ((String) b.getText ()) && enableNum      ( InText,b)) {InText +=  b.getText(); }  // Разрешение на ввод цифр кроме "0"
        if ( isOperator ((String) b.getText ()) && enableOperator ( InText,b))  {InText +=  b.getText(); }   // Разрешение на ввод операторов
        if ( b.getText ().equals ( "(" ) && enableSkOp ( InText,b ) )  {InText +=  b.getText(); }        // Разрешение на ввод "("
        if ( b.getText ().equals ( ")" ) && enableSkCl ( InText,b ) )  {InText +=  b.getText(); }        // Разрешение на ввод ")"

        greateStacks();
        System.out.println("LOG onClickButton stackNumer ="+stackNumer);
        System.out.println("LOG onClickButton stackOperator ="+stackOperator);
        updateIn();
    }
    public void greateStacks(){
        int count = 0;
        String numbers = "";
        double numberD=0;
        stackNumer.clear();
        stackOperator.clear();
        for (int i=0 ; i < InText.length(); i++)
        {
            if (charIsNum(InText,i)){
                if (numbers.length()>0) stackNumer.pop ();
                numbers+=InText.charAt(i); // если цифра, то собираем строку числа
                stackNumer.push (numbers);
                resultText=calculator();
             }
            if ( charIsOperator ( InText , i ) ) {       /////////////// Если оператор, то в Стек операторов
                stackOperator.push ( InText.charAt ( i ) );//  то в Стек операторов
                numbers = "";
               // System.out.println ("LOG вызываем калькулятор");

            }                                           //**************** Если оператор, то в Стек операторов

        }

    }
    public void onClickClear(View v){
        double t=0.0;
        InText = "";
        updateIn();
    }
    public void setSubresult(float subresult) {
        this.subresult = subresult;
    }
   //////////////************** Чем является символ
public static boolean isOperator( String s)  // Проверка является ли последний символ оператором
{
    char x;
    x = s.charAt ( s.length ()-1 );
    if ( x == '-' ||x == '+'  ||x == '÷' ||x == 'x' ||x  == '/')
        return true;
    return false;
    }
public static boolean isOperatorChar( char s)  // Проверка является ли последний символ оператором
{
    if ( s == '-' ||s == '+'  ||s == '÷' ||s == 'x' ||s  == '/')
        return true;
    return false;
    }
    public static boolean isOperatorLast( String s)  // Проверка является ли последний символ оператором
    {
        String g=lastSymbolString(s);
        if (g.equals ("-") || g.equals ( "÷" ) || g.equals ( "x" ) || g.equals ( "+" )|| g.equals ( "/" )) return true;
        return  false;
    }
/*    public static boolean IsNum(String str) { // Является ли последний введеный символ цифрой/числом кроме 0
        if (str==".") return true;
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
       return true;
    }*/
    public static boolean IsNum(String s)  // Проверка является ли последний символ числом. Аналог верхней функции.
    {
        if (s.length()<=0) return false;
        try {
            char x;
            x = s.charAt ( s.length ()-1 );
            if ( x == '0' ||x == '1'  ||x == '2' ||x == '3' ||x == '4' ||x == '5'
                    ||x == '6' ||x == '7' ||x == '8' ||x == '9' ||x == '.' )
                return true;
        }catch (Exception r){
            System.out.println("LOG out IsNum");
        }

        return  false;
    }
//////////////************** Чем является символ

////////////// ************* Проверка разрешения на ввод символа
public static boolean enableMin(String str)                             // Minus "-"
{
    char x;
    x = str.charAt ( str.length ()-1 );
    if ( x == ')' ||x == '.' || str.length ()==0 || IsNum ( str ))
        return true;
        return false;
}
    public static String lastSymbolString(String x)                     // Возвращает последний символ из строки или "" в формате String
    {
        if (x.length ()<=0) return "";
        return String.valueOf(x.charAt ( x.length ()-1 ));
    }

    public static boolean enableNum(String str, Button b) // Разрешение на ввод цифр
    {
      //  if (str.length()<0) return false;
        String x = lastSymbolString ( str );
        if (x.equals ( "(" ) || x.equals ( "." ) || isOperatorLast(str) || x.equals ( "" ) || IsNum ( str ))
        return true;
        return false;
    }
    public static boolean enableDr(String str, Button b)        // Разрешение на ввод дроби
    {
        String x = lastSymbolString ( str );
        if (x.equals ( ")" ) || IsNum ( str ) )
            return true;
        return false;
    }
    public static boolean enableSkOp(String str, Button b)      // Разрешение на ввод открытой скобки "("
    {
        String x = lastSymbolString ( str );
        if (x.equals ( "" ) || x.equals ( "-" ) || x.equals ( "+" ) ||x.equals ( "x" ) ||x.equals ( "÷" ) ||x.equals ( "(" ))
            return true;
        return false;
    }
    public static boolean enableSkCl(String str, Button b)          // Разрешение на ввод закрытой скобки ")"
    {
        String x = lastSymbolString ( str );
        if (countSymbol (str,'(' ) > countSymbol (str,')' ))
        if (x.equals ( ")" ) || IsNum ( str ))
            return true;
        return false;
    }
    public static boolean enableOperator(String str, Button btn)  // Разрешение на ввод оператора
    {
        try {
            if (lastSymbolString ( str ).equals ( ")" ) || IsNum ( str )  || str.equals("")) return true;
        }catch (Exception r) {
            System.out.println("LOG out enableOperator");
        }
        return  false;

    }
    public  boolean enableTchk(String str, Button btn)               // ПРазрешение на ввод точки "."
    {
        //  System.out.println("Длина  cstr "+ (str.length ()-1));
        if (IsNum ( str ) ) return true;
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
    public static int countOper(String haystack)
    {
        int count = 0;
        for (int i=0; i < haystack.length(); i++)
        {
            if ( haystack.charAt ( i ) == '-' ||haystack.charAt ( i ) == '+'  ||haystack.charAt ( i ) == '÷' ||haystack.charAt ( i ) == 'x' ||haystack.charAt ( i )  == '/')
                count++;
        }
        return count;
    }
    ////////////// ************* Функция подчета числа фхождений needle в haystack
/*    public boolean powerOperation(String fop1, String fop2){
        int f1 = 0; int f2=0;
        switch(fop1) {
            case "+": f1=2;
            case "-": f1=2;
            case "÷": f1=3;
            case "x": f1=3;
            case "/": f1=3;
        }
        switch(fop2) {
            case "+": f2=2;
            case "-": f2=2;
            case "÷": f2=3;
            case "x": f2=3;
            case "/": f2=3;
        }
        if (f1>f2) return true;
        return false;
    }*/
    public boolean powerOperation(char fop1, char fop2){
        int f1 = 0; int f2=0;
        if (fop1=='+' || fop1=='-'  ) f1=2;
        if (fop1=='÷' || fop1=='x' || fop1=='/'  ) f1=3;
        if (fop2=='+' || fop2=='-'  ) f2=2;
        if (fop2=='÷' || fop2=='x' || fop2=='/'  ) f2=3;

/*        System.out.println("LOG powerOperation f1=  "+f1);
        System.out.println("LOG powerOperation f2=  "+f2);*/
        if (f1<=f2)
            return true;
        return false;
    }
        public boolean charIsOperator(String s, int i)  // Проверка является ли последний символ оператором
    {
        char c;
        c = InText.charAt(i);
        if (c=='-' || c=='÷' || c=='x' || c=='+' || c=='/' || c=='(' || c==')' ) return true;
        return  false;
    }
    public boolean charIsNum(String s, int i)  // Проверка является ли последний символ оператором
    {
        char c;
        c = InText.charAt(i);
        if (c=='.' || c=='0' || c=='1' || c=='2' || c=='3' || c=='4' || c=='5' || c=='6' || c=='7'  || c=='8'  || c=='9' ) return true;
        return  false;
    }
    public boolean charIsDigit(String s, int i)  // Проверка является ли последний символ цифрой
    {
        String c;
        c = String.valueOf(InText.charAt(i));
        if (IsNum(c)) {return true;}
        return  false;
    }
    public String calcOperation (char s,String d2, String d1)  // Проверка является ли последний символ цифрой
    {
        double num1 = Double.valueOf(d1);
        double num2 = Double.valueOf(d2);
        if (s=='x') return Double.toString(num1*num2);
        if (s=='-') return Double.toString(num1-num2);
        if (s=='/') if (num2!=0)return Double.toString(num1/num2); else return "Деление на ноль";
        if (s=='+') return Double.toString(num2+num1);
        if (s=='÷') if (num2!=0)return Double.toString(num1/num2); else return "Деление на ноль";
        return "calcOperation не подсчитан";
    }
    public String calculator()  // Проверка является ли последний символ цифрой
    {
        char op1 = ' ';
        char op2 = ' ';
        String dig1 = "";
        String dig2 = "";
        int i = 0;
        try {
            if (stackOperator.peek() == '(') return stackNumer.peek();// Не чго считать
        } catch (Exception e) {
            return "Не чего считать на входе ( или операторов нет";
        }//***********************************************************// Не чго считать
        System.out.println("LOG stackNumer.на входе калькулятора = "+stackNumer);
        System.out.println("LOG stackOperator.на входе калькулятора = "+stackOperator);
        try {
            while (!stackOperator.empty ()) {
                //if (stackNumer.size ()==1) return stackNumer.peek (); //Если одно число в стеке, то не считаем
                op1 = stackOperator.peek ();
                if ( op1=='(' )   return stackNumer.peek (); // Если открытая скобка, ни чего не считаем
                if ( op1==')' ) {
                    stackOperator.pop ();
                    if (stackOperator.peek ()=='(' )
                        {
                            stackOperator.pop ();
                            return stackNumer.peek ();
                        }   else {

                        stackNumer.push ( calcOperation ( stackOperator.pop () , stackNumer.pop () , stackNumer.pop () ) ); // расчитываем и записываем в стек
                        stackOperator.push ( ')' );    /// Ложим обратно скобку
                        continue;
                    }}

                if ( !stackOperator.empty () && stackNumer.size ()>=2)/////////////IF
                {
                    op1=stackOperator.pop ();
                    dig1=stackNumer.pop ();
                    if (!stackOperator.empty ())
                        op2 = stackOperator.peek ();
                        if (isOperatorChar ( op2 )){
                        if ( powerOperation ( op1 , op2 ) ) {
                            stackNumer.push ( calcOperation ( op2 , dig1 , stackNumer.pop () ) ); // расчитываем и записываем в стек
                            System.out.println("LOG 1 = "+stackNumer.peek ());
                            stackOperator.pop ();
                            stackOperator.push ( op2 );
                            return stackNumer.peek ();
                        } else
                            stackNumer.push ( calcOperation ( op1 , dig1 , stackNumer.pop () ) ); // расчитываем и записываем в стек
                        System.out.println("LOG 2 = "+stackNumer.peek ());
                            stackOperator.pop ();
                        stackOperator.push ( op1 );
                            return stackNumer.peek ();
                    }
                }

                else
                    dig1=stackNumer.pop ();
                    System.out.println("LOG op1= "+op1+" dig1= "+dig1+" op1= "+stackNumer.peek ());

                System.out.println("LOG 3 = "+stackNumer.peek ());
                resultText = calcOperation ( op1 , dig1 , stackNumer.peek () ); // расчитываем и записываем в стек
                stackOperator.push ( op1 );
                stackNumer.push ( dig1 );


                return resultText;
                // / *********************************////////////// END IF
                }//if (isOperatorChar(op1)) END`

        }
        catch (Exception e) { return "TRY CATCH";        }

        return stackNumer.peek()+" на выходе кальк";
    }
}
