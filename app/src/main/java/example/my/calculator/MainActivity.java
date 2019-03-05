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
    private TextView resultScreen;                         // View  результата
    private TextView InScreen;                             // View вводимой формулы
    private String resultText = "";                      // Текс результата
    public String InText = "";                            // Текст вводимой формулы
    private float subresult;                               // не могу перекоммитить 2
    public Stack<String> stackOperator = new Stack ();// Стек операций
    public Stack<String> stackNumer = new Stack ();// Стек оперендов
    public ArrayList<String> STAK = new ArrayList<String> ();
    public int sumSkOp = 0; // Количество открытых скобок

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        resultScreen = findViewById ( R.id.textViewRes );// View  результата
        InScreen = findViewById ( R.id.textViewIn );     // View вводимой формулы
        InScreen.setText ( InText );
    }

    private void updateIn() {
        //result();
        InScreen.setText ( InText );
        resultScreen.setText ( resultText );
        resultText = "";

    }

    private void clear() {
        InText = "";
        subresult = 0;
    }

    public void onClickBack(View v) {

        InText = InText.length () > 0 ? InText.substring ( 0 , InText.length () - 1 ) : "";
        updateIn ();
    }

    public void onClickButton(View v) {
        Button b = (Button) v;
        if ( stringIsNum ( (String) b.getText () ) && enableNum ( InText , b ) ) {
            InText += b.getText ();
        }  // Разрешение на ввод цифр кроме "0"
        if ( isOperator ( (String) b.getText () ) && enableOperator ( InText , b ) ) {
            InText += b.getText ();
        }   // Разрешение на ввод операторов
        if ( b.getText ().equals ( "(" ) && enableSkOp ( InText , b ) ) {
            InText += b.getText ();
        }        // Разрешение на ввод "("
        if ( b.getText ().equals ( ")" ) && enableSkCl ( InText , b ) ) {
            InText += b.getText ();
        }        // Разрешение на ввод ")"

        greateStacks ();
        System.out.println ( "LOG после калькулятора stackNumer =" + stackNumer );
        System.out.println ( "LOG после калькулятора stackOperator =" + stackOperator );
        updateIn ();
    }

    public void greateStacks() {
        int count = 0;
        String numbers = "";
        double numberD = 0;
        stackNumer.clear ();
        stackOperator.clear ();
        STAK.clear ();
        for (int i = 0; i < InText.length (); i++) {
            if ( charIsNum ( InText , i ) ) {
                if ( numbers.length () > 0 ) STAK.remove ( STAK.size () - 1 );
                numbers += InText.charAt ( i );                  // если цифра, то собираем строку числа
                STAK.add ( numbers ); //
            }
            if ( charIsOperator ( InText , i ) ) {       /////////////// Если оператор,
                STAK.add ( String.valueOf (InText.charAt ( i )) );        //  то в строку выражения
                numbers = "";
            }

        }
        resultText=calculator ();
        System.out.println ( "LOG " + STAK );
    }

    public void onClickClear(View v) {
        double t = 0.0;
        InText = "";
        STAK.clear ();
        updateIn ();
    }

    public void setSubresult(float subresult) {
        this.subresult = subresult;
    }

    //////////////************** Чем является символ
    public static boolean isOperator(String s)  // Проверка является ли последний символ оператором
    {
        char x;
        x = s.charAt ( s.length () - 1 );
        if ( x=='-' || x=='+' || x=='÷' || x=='x' || x=='/' )
            return true;
        return false;
    }

    public static boolean isOperatorChar(char s)  // Проверка является ли последний символ оператором
    {
        if ( s=='-' || s=='+' || s=='÷' || s=='x' || s=='/' )
            return true;
        return false;
    }

    public static boolean isOperatorLast(String s)  // Проверка является ли последний символ оператором
    {
        String g = lastSymbolString ( s );
        if ( g.equals ( "-" ) || g.equals ( "÷" ) || g.equals ( "x" ) || g.equals ( "+" ) || g.equals ( "/" ) )
            return true;
        return false;
    }
    public static boolean isBulFuncs(String s)  // Проверка является ли последний символ оператором
    {
        if ( s.equals ( "-" ) || s.equals ( "÷" ) || s.equals ( "x" ) || s.equals ( "+" ) || s.equals ( "/" ) )
            return true;
        return false;
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
    public static boolean stringIsNum(String s)  // Проверка является ли последний символ числом. Аналог верхней функции.
    {
        if ( s.length () <= 0 ) return false;
        try {
            char x;
            x = s.charAt ( s.length () - 1 );
            if ( x=='0' || x=='1' || x=='2' || x=='3' || x=='4' || x=='5'
                    || x=='6' || x=='7' || x=='8' || x=='9' || x=='.' )
                return true;
        } catch ( Exception r ) {
            System.out.println ( "LOG out IsNum" );
        }

        return false;
    }
    public static boolean stringIsOper(String s)  // Проверка является ли последний символ числом. Аналог верхней функции.
    {
        if ( s.length () <= 0 ) return false;
        try {
            char x;
            x = s.charAt ( s.length () - 1 );
            if ( x=='-' || x=='÷' || x=='x' || x=='+' || x=='/' || x=='(' || x==')'  )
                return true;
        } catch ( Exception r ) {
            System.out.println ( "LOG out IsNum" );
        }

        return false;
    }
    public static boolean stringIsSkCl(String s)  // Проверка является ли последний символ ")"
    {
        if ( s.length () <= 0 ) return false;
        try {
            char x;
            x = s.charAt ( s.length () - 1 );
            if (  x==')'  )
                return true;
        } catch ( Exception r ) {
            System.out.println ( "LOG out IsNum" );
        }

        return false;
    }
    public static boolean stringIsSkOp(String s)  // Проверка является ли последний символ ")"
    {
        if ( s.length () <= 0 ) return false;
        try {
            char x;
            x = s.charAt ( s.length () - 1 );
            if (  x=='('  )
                return true;
        } catch ( Exception r ) {
            System.out.println ( "LOG out IsNum" );
        }

        return false;
    }
//////////////************** Чем является символ

    ////////////// ************* Проверка разрешения на ввод символа
    public static boolean enableMin(String str)                             // Minus "-"
    {
        char x;
        x = str.charAt ( str.length () - 1 );
        if ( x==')' || x=='.' || str.length ()==0 || stringIsNum ( str ) )
            return true;
        return false;
    }
    public static String lastSymbolString(String x)                     // Возвращает последний символ из строки или "" в формате String
    {
        if ( x.length () <= 0 ) return "";
        return String.valueOf ( x.charAt ( x.length () - 1 ) );
    }

    public static boolean enableNum(String str , Button b) // Разрешение на ввод цифр
    {
        //  if (str.length()<0) return false;
        String x = lastSymbolString ( str );
        if ( x.equals ( "(" ) || x.equals ( "." ) || isOperatorLast ( str ) || x.equals ( "" ) || stringIsNum ( str ) )
            return true;
        return false;
    }

    public static boolean enableSkOp(String str , Button b)      // Разрешение на ввод открытой скобки "("
    {
        String x = lastSymbolString ( str );
        if ( x.equals ( "" ) || x.equals ( "-" ) || x.equals ( "+" ) || x.equals ( "x" ) || x.equals ( "÷" ) || x.equals ( "(" ) )
            return true;
        return false;
    }

    public static boolean enableSkCl(String str , Button b)          // Разрешение на ввод закрытой скобки ")"
    {
        String x = lastSymbolString ( str );
        if ( countSymbol ( str , '(' ) > countSymbol ( str , ')' ) )
            if ( x.equals ( ")" ) || stringIsNum ( str ) )
                return true;
        return false;
    }

    public static boolean enableOperator(String str , Button btn)  // Разрешение на ввод оператора
    {
        try {
            if ( lastSymbolString ( str ).equals ( ")" ) || stringIsNum ( str ) || str.equals ( "" ) )
                return true;
        } catch ( Exception r ) {
            System.out.println ( "LOG out enableOperator" );
        }
        return false;

    }

    ////////////// ************* Проверка разрешения на ввод символа
    ////////////// ************* Функция подчета числа фхождений needle в haystack
    public static int countSymbol(String haystack , char needle) {
        int count = 0;
        for (int i = 0; i < haystack.length (); i++) {
            if ( haystack.charAt ( i )==needle ) {
                count++;
            }
        }
        return count;
    }

    public static int countOper(String haystack) {
        int count = 0;
        for (int i = 0; i < haystack.length (); i++) {
            if ( haystack.charAt ( i )=='-' || haystack.charAt ( i )=='+' || haystack.charAt ( i )=='÷' || haystack.charAt ( i )=='x' || haystack.charAt ( i )=='/' )
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
    public boolean powerOperation(String fop1 , String fop2) {
        int f1 = 1;
        int f2 = 0;
        if ( fop1.equals ( "+" ) || fop1.equals ( "-" ) ) f1 = 2;
        if ( fop1.equals ( "÷" ) || fop1.equals ( "x" ) || fop1.equals ( "/" ) ) f1 = 3;
        if ( fop2.equals ( "+" ) || fop2.equals ( "-" ) ) f2 = 2;
        if ( fop2.equals ( "÷" ) || fop2.equals ( "x" ) || fop2.equals ( "/" ) ) f2 = 3;

        System.out.println("LOG powerOperation f1=  "+f1);
        System.out.println("LOG powerOperation f2=  "+f2);
        if ( f1 <= f2 )
            return true;
        return false;
    }

    public boolean charIsOperator(String s , int i)  // Проверка является ли последний символ оператором
    {
        char c;
        c = InText.charAt ( i );
        if ( c=='-' || c=='÷' || c=='x' || c=='+' || c=='/' || c=='(' || c==')' ) return true;
        return false;
    }

    public boolean charIsNum(String s , int i)  // Проверка является ли последний символ оператором
    {
        char c;
        c = InText.charAt ( i );
        if ( c=='.' || c=='0' || c=='1' || c=='2' || c=='3' || c=='4' || c=='5' || c=='6' || c=='7' || c=='8' || c=='9' )
            return true;
        return false;
    }

    public String calcOperation(String s , String d2 , String d1)  // Проверка является ли последний символ цифрой
    {
        double num1 = Double.valueOf ( d1 );
        double num2 = Double.valueOf ( d2 );
        if ( s.equals ( "x" ) ) return Double.toString ( num1 * num2 );
        if ( s.equals ( "-" ) ) return Double.toString ( num1 - num2 );
        if ( s.equals ( "/" ) ) if ( num2!=0 ) return Double.toString ( num1 / num2 );
        else return "Деление на ноль";
        if ( s.equals ( "+" ) ) return Double.toString ( num2 + num1 );
        if ( s.equals ( "÷" ) ) if ( num2!=0 ) return Double.toString ( num1 / num2 );
        else return "Деление на ноль";
        return "calcOperation не подсчитан";
    }

    public String calculator()  // Проверка является ли последний символ цифрой
    {
        String op1 = "";
        String op2 = "";
        String dig1 = "";
        String dig2 = "";
        int i = 0;
        int z=0;
        System.out.println ( "LOG в калькуляторе" + STAK );
        for (i = 0; i < STAK.size (); i++) {

            if ( stringIsOper ( STAK.get ( i ) ) )      /////////// Обработка получения операции
            {
                System.out.println ( "LOG 1" );
                stackOperator.push ( STAK.get ( i ) );
                while (z<70 && stackNumer.size () >= 2 && stackOperator.size () >= 2 ) {
                    op1 = stackOperator.pop ();
                    op2 = stackOperator.pop ();
                    dig1 = stackNumer.pop ();
                    dig2 = stackNumer.pop ();
                    if ( powerOperation ( op1 , op2 ) ) {
                        stackNumer.push ( calcOperation ( op2 , dig1 , dig2 ) ); // расчитываем и записываем в стек
                        stackOperator.push ( op1 );
                    }
                    else {
                        stackOperator.push (op2);
                        stackOperator.push (op1);
                        stackNumer.push (dig2);
                        stackNumer.push (dig1);break;
                    }z++;
                }
            }
            if ( stringIsNum ( STAK.get ( i ) ) ) {     //////////// Обработка получения числа
                stackNumer.push ( STAK.get ( i ) );
            }
            if ( stringIsSkOp ( STAK.get ( i ) ) ) {     //////////// Обработка получения "("
                stackNumer.push ( STAK.get ( i ) );
            }

        }
        ///////// Тлько операторы работают /8/////////////
        ///////// Если конец строки, то завершаем операции
        System.out.println ( "LOG конец строки STAK = " + STAK );
        ///////// Если конец строки, то завершаем операции
        return stackNumer.peek ();
    }
}
