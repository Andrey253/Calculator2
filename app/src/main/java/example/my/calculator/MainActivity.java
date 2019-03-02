package example.my.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcel;
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
    public Stack<String>    stackOperator = new Stack<> ();          // Стек операций
    public Stack<Double>   stackNumber =   new Stack<> ();          // Стек оперендов

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
       // resultScreen.setText(resultText);
        result();
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

        if ( isOperatorBtn ( null,b ) &&  enableOperator ( InText,b)) InText +=  b.getText();       // Разрешение на ввод операторов
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
        double t=0.0;
        if (!stackNumber.empty()) t=stackNumber.pop();
        System.out.println( " t "+ t);
        InText = "";
        updateIn();
    }
    public void setSubresult(float subresult) {
        this.subresult = subresult;
    }
   //////////////************** Чем является символ
public static boolean isOperatorBtn( String s,Button x)  // Проверка является ли последний символ оператором
{
    String Char;
    Char = (String) x.getText ();
    if (Char.equals ("-") || Char.equals ( "÷" ) || Char.equals ( "x" ) || Char.equals ( "+" )) return true;
    return  false;
    }
    public static boolean isOperator( String s)  // Проверка является ли последний символ оператором
    {
        String c="";

        if (c.equals ("-") || c.equals ( "÷" ) || c.equals ( "x" ) || c.equals ( "+" )) return true;
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
        //tem.out.println("Длина  1 / ="+b.getText ().equals ( "0" ));
       // System.out.println("Длина  2 / ="+lastChar ( str ));
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
        if (lastChar ( str ).equals ( ")" ) || lastCharIsNum ( str ) ) return true;
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
    public int powerOperation(String pop){
        switch(pop) {
            case "+": return 2;
            case "-": return 2;
            case "÷": return 3;
            case "x": return 3;
            case "/": return 3;
            default:  return 0;
        }
    }
    public boolean inStackOper(String div){
    if (powerOperation(div) != 0) return true;
    return false;
    }
    public void result(){
        int count = 0;
        String numbers = "";
        double numberD=0;
        stackOperator.clear();
        stackNumber.clear();
        for (int i=0 ; i < InText.length(); i++)
        {
            if ( charIsOperator(InText,i) && InText.charAt(i)!='.') {       // Если оператор, то в Стек операторов
                stackOperator.push(String.valueOf(InText.charAt(i)));//  то в Стек операторов
                numbers="";
            }
            if (charIsNum(InText,i) || InText.charAt(i)=='.'){            // и
                if (numbers.length()>0 && InText.charAt(i)!='.') stackNumber.pop();
                numbers+=InText.charAt(i); // если цифра, то собираем строку числа
                if (InText.charAt(i)!='.'){
                    numberD = Double.valueOf(numbers); // То в Стек чисел
                    stackNumber.push(numberD);
                }
            }
        }

        calculator(stackNumber,stackOperator);
    }
    public boolean charIsOperator(String s, int i)  // Проверка является ли последний символ оператором
    {
        char c;
        c = InText.charAt(i);
        if (c=='-' || c=='÷' || c=='x' || c=='+' || c=='/' || c=='.' || c=='(' || c==')' ) return true;
        return  false;
    }
    public boolean charIsNum(String s, int i)  // Проверка является ли последний символ оператором
    {
        char c;
        c = InText.charAt(i);
        if (c=='0' || c=='1' || c=='2' || c=='3' || c=='4' || c=='5' || c=='6' || c=='7'  || c=='8'  || c=='9' ) return true;
        return  false;
    }
    public boolean charIsDigit(String s, int i)  // Проверка является ли последний символ цифрой
    {
        String c;
        c = String.valueOf(InText.charAt(i));
        if (lastCharIsNum(c)) {return true;}
        return  false;
    }
    public double calcOperation (String s,double d1, double d2)  // Проверка является ли последний символ цифрой
    {
        if (s.equals("x")) return (d1*d2);
        if (s.equals("-")) return (d1-d2);
        if (s.equals("/")) if (d2!=0)return (d1/d2); else resultText="Деление на ноль";
        if (s.equals("+")) return (d1+d2);
        if (s.equals("÷")) if (d2!=0)return (d1/d2); else resultText="Деление на ноль";
        return d2;
    }
    public void calculator(Stack<Double> dStack, Stack<String> opStack)  // Проверка является ли последний символ цифрой
    {
        String op1 = "";
        String op2 = "";
        double dig1=0; double dig2=0;
        double resDouble;
        String resString = "565656";
        resultScreen.setText(resString);
        int sumSkOp=0; // Количество открытых скобок
        while (!opStack.empty()){

            if (!dStack.empty()) {dig1=dStack.pop();System.out.println("LOG 1");}
            if (!dStack.empty()) dig2=dStack.pop(); else dig2=0;
            op1 = opStack.pop();
            System.out.println("LOG op1 =  " +op1);
            if (op1.equals("(")){ // If "("
                System.out.println("LOG Открытая скобка = ");
                sumSkOp++;
                op1="";
            }System.out.println("LOG sumSkOp = "+sumSkOp);
            while (op1.equals(")")){
                sumSkOp--;
                op1 = opStack.pop();
                if (opStack.empty() && sumSkOp == 0) resultScreen.setText(resString);
            }
            while (isOperator(op1)){
                if (opStack.empty()) {resultScreen.setText(Double.toString(calcOperation(op1,dig1,dig2)));
                    resultScreen.setText("Пустой стек операций");}
            }
        }
    }
}
