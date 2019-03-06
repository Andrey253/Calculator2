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
        // resultText = "";

    }
    private void clear() {
        InText = "";
        subresult = 0;
    }
    public void onClickBack(View v) {
        InText = InText.length () > 0 ? InText.substring ( 0 , InText.length () - 1 ) : ""; // Тенарный оператор. Кнопка НАЗАД
        updateIn ();
    }
    public void onClickButton(View v) {
        Button b = (Button) v;
        if ( enableNum ( InText , b ) )     InText += b.getText ();  // Разрешение на ввод цифр //кроме "0"
        makeStacks ();
        resultText = calculatorOperation ();
        updateIn ();
    }
    public void onClickSk(View v) {
        Button b = (Button) v;
        // Разрешение на ввод "("
        if ( enableSkCl ( InText , b ) || enableSkOp( InText , b) )             InText += b.getText ();       // Разрешение на ввод ")"
        makeStacks ();
        resultText = calculatorOperation ();
        updateIn ();
    }
    public void onClickOperation(View v) {
        Button b = (Button) v;
        if (  enableOperator ( InText , b ) )       InText += b.getText ();       // Разрешение на ввод операторов
        makeStacks ();
        resultText = calculatorOperation ();
//        System.out.println ( "LOG 4 стек  до изъятия операторов"+stackOperator );
//        System.out.println ( "LOG 4 стек  до изъятия операторов"+stackNumer );
        updateIn ();
    }
    public String calculatorOperation()  // Проверка является ли последний символ цифрой
    {
        String op1 = "";
        String op2 = "";
        String dig1 = "";
        String dig2 = "";
        String StrItem;
        String subresultat;
        int k = 0;
        int z=0;
        while (STAK.size ()>k)
        {
            StrItem=STAK.get (k);
            k++;
            if ( stringIsOper ( StrItem ))      /////////// Обработка получения операции
            {
                stackOperator.push ( StrItem);
                System.out.println ( "LOG 0 " );


                if ( stackNumer.size () >= 2 && stackOperator.size ()>=2) {
                    op1 = stackOperator.pop ();
                    op2 = stackOperator.pop ();

                    System.out.println ( "LOG 1 " );
                 //   if (powerOperation ( op1 , op2 )) {
                        while (powerOperation ( op1 , op2 )) {
                            System.out.println ( "LOG 2 powerOperation ( "+op1+" , "+op2+ " )  "+powerOperation ( op1 , op2 ) );

                            dig1 = stackNumer.pop ();
                            dig2 = stackNumer.pop ();
                                stackNumer.push ( calcOperation ( op2 , dig1 , dig2 ) ); // расчитываем и записываем в стек
                                System.out.println ( "LOG 3  "+stackOperator );
                                System.out.println ( "LOG 3  "+stackNumer );
                                op2 = stackOperator.pop ();
                            }
                        System.out.println ( "LOG 5 Возврат операций " );
                        stackOperator.push ( op2 );
                        stackOperator.push ( op1 );

                 }
            }
            //System.out.println ( "LOG 3 StrItem "+StrItem );
             if ( stringIsNum ( StrItem ) ) {     //////////// Обработка получения числа
                            stackNumer.push ( StrItem );

                        }
             if ( stringIsSkOp ( StrItem ) ) {     //////////// Обработка получения числа
                            stackOperator.push ( StrItem );

                        }
            if ( stringIsSkCl ( StrItem ) ) {     //////////// Обработка получения ")"


                if ( stackNumer.size () >= 2 && stackOperator.size ()>=2) {
                    op1 = stackOperator.pop ();
                    op2 = stackOperator.pop ();
                    System.out.println ( "LOG 22 powerOperation  "+op1+" , "+op2+ "  "+powerOperation ( op1 , op2 ) );
                    while (isOperator ( op1 ) && isOperator ( op2 )) {
                        dig1 = stackNumer.pop ();
                        dig2 = stackNumer.pop ();

                        if(powerOperation ( op2,op1 )){
                            stackNumer.push ( calcOperation ( op1 , dig1 , dig2 ) ); // расчитываем и записываем в стек
                            stackOperator.push ( op1 );
                        }
                        if(powerOperation ( op1,op2 )){
                            stackNumer.push ( calcOperation ( op2 , dig1 , dig2 ) ); // расчитываем и записываем в стек
                            stackOperator.push ( op2 );
                        }
                        System.out.println ( "LOG 23  "+stackOperator );
                        System.out.println ( "LOG 23  "+stackNumer );


                    }
                    if (isOperator ( op1 ) && op2.equals ( "(" )) {

                        dig1 = stackNumer.pop ();
                        dig2 = stackNumer.pop ();
                        stackNumer.push ( calcOperation ( op1 , dig1 , dig2 ) ); // расчитываем и записываем в стек
                        System.out.println ( "LOG 13  "+stackOperator );
                        System.out.println ( "LOG 13  "+stackNumer );

                    }else {
                    System.out.println ( "LOG 15 Возврат операций " );
                    stackOperator.push ( op2 );
                    stackOperator.push ( op1 );}
                }

            }
//            if (STAK.size ()-1==k && countSymbol (InText,')' ) == countSymbol ( InText,'(' )){ ///Досчитываем
//                try {
//                    if (isOperator ( lastSymbolString ( InText ))) stackOperator.pop ();
//                } catch ( Exception e ) {
//                    e.printStackTrace ();
//                }
//                while (!stackOperator.empty () && stackNumer.size ()>=2){
//                    op2 = stackOperator.pop ();
//                    dig1 = stackNumer.pop ();
//                    dig2 = stackNumer.pop ();
//                    stackNumer.push ( calcOperation ( op2 , dig1 , dig2 ) ); // расчитываем и записываем в стек
//                }
//            }

        }
        ///////// Если конец строки, то завершаем операции
        System.out.println ( "LOG 12 Конец calculatorOperation "+stackOperator );
        System.out.println ( "LOG 12  Конец calculatorOperation "+stackNumer );
        try {
            return stackNumer.peek ();
        } catch ( Exception e ) {
            e.printStackTrace ();
        }
return "";
    }

    public void makeStacks() {
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
            if ( x=='-' || x=='÷' || x=='x' || x=='+' || x=='/' )
                return true;
        } catch ( Exception r ) {
        }
        return false;
    }
    public static boolean stringIsSkCl(String s)  // Проверка является ли последний символ ")"
    {
        try {
            if (  s.equals ( ")" ) )
                return true;
        } catch ( Exception r ) {

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
        String x = lastSymbolString ( str );
        if ( x.equals ( "(" ) || x.equals ( "." ) || isOperatorLast ( str ) || x.equals ( "" ) || stringIsNum ( str ) || x.equals ( "" ))
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
        if (!isOperator ( fop1 )) return false;
        if (!isOperator ( fop2 )) return false;
        int f1 = 1;
        int f2 = 0;
        if ( fop1.equals ( "+" ) || fop1.equals ( "-" ) ) f1 = 2;
        if ( fop1.equals ( "÷" ) || fop1.equals ( "x" ) || fop1.equals ( "/" ) ) f1 = 3;
        if ( fop2.equals ( "+" ) || fop2.equals ( "-" ) ) f2 = 2;
        if ( fop2.equals ( "÷" ) || fop2.equals ( "x" ) || fop2.equals ( "/" ) ) f2 = 3;

        if ( f1 <= f2 )  return true;
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

}
