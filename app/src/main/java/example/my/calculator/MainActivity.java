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
//        System.out.println("LOG вводимый символ = "+b.getText ());
//        if ( b.getText ().equals ( "0" ) && (lastSymbolString   ( InText ).equals ( "/" ) || lastSymbolString   ( InText ).equals ( "÷" )))
//            InText += "0.";     // Добавление  "." если "0" после дроби
        if ( IsNum      ((String) b.getText ()) && enableNum      ( InText,b)) {InText +=  b.getText(); }  // Разрешение на ввод цифр кроме "0"
        if ( isOperator ((String) b.getText ()) && enableOperator ( InText,b))  {InText +=  b.getText(); }   // Разрешение на ввод операторов
        if ( b.getText ().equals ( "(" ) && enableSkOp ( InText,b ) )  {InText +=  b.getText(); }        // Разрешение на ввод "("
        if ( b.getText ().equals ( ")" ) && enableSkCl ( InText,b ) )  {InText +=  b.getText(); }        // Разрешение на ввод ")"

        greateStacks();
        System.out.println("LOG onClickButton stackNumer ="+stackNumer);
        System.out.println("LOG onClickButton stackOperator ="+stackOperator);
       // calculator();
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
                System.out.println ("LOG stackNumer.size ()= "+stackNumer.size ());

                if (countOper ( InText )==1 && stackNumer.size ()==2) {// Если оператор в строке один то выполняем его/////////////////// если один оператор в строке
                    try {
                        System.out.println ( "LOG если число операторов = 1 " + countOper ( InText ) );
                        resultText = calcOperation ( stackOperator.peek () , stackNumer.pop () , stackNumer.pop () );
                    } catch ( Exception e ) {
                        System.out.println ( "LOG если число операторов = 1 ОШИБКА" );
                        System.out.println ( "LOG stackNumer" + stackNumer );
                    }
                } /////////////////// если один оператор в строке
            }
            if (InText.charAt(i)==')'){      ///////////////  Удаляем двойные скобки
                try {
                    if (stackOperator.peek ()=='(') stackOperator.pop (); //Удаляем парные скобки которые вместе
                    else stackOperator.push ( InText.charAt(i) );
                } catch (Exception e) {}continue;
            }                                ///////////////  Удаляем двойные скобки
            if ( charIsOperator ( InText , i ) ) {       // Если оператор, то в Стек операторов
                try {
                    if ( powerOperation ( InText.charAt ( i ) , stackOperator.peek () ) ) {
                        stackNumer.push ( calcOperation ( InText.charAt ( i ) , stackNumer.pop () , stackNumer.pop () ) );
                        System.out.println ( "LOG если на входе оператор " );
                    }
                } catch ( Exception e ) {
                }
                stackOperator.push ( InText.charAt ( i ) );//  то в Стек операторов
                numbers = "";
            }

            try { resultText = stackNumer.peek () ;} catch (Exception e) {}

        }
    }
    public void calculator(){

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
        switch(fop1) {
            case '+': f1=2;
            case '-': f1=2;
            case '÷': f1=3;
            case 'x': f1=3;
            case '/': f1=3;
        }
        switch(fop2) {
            case '+': f2=2;
            case '-': f2=2;
            case '÷': f2=3;
            case 'x': f2=3;
            case '/': f2=3;
        }
        if (f1>f2) return true;
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
        if (s=='x') {System.out.println("LOG "+num1+"x"+num2+" = "+(num1*num2)); return Double.toString(num1*num2);}
        if (s=='-') {System.out.println("LOG "+num1+"-"+num2+" = "+(num1-num2));return Double.toString(num1-num2);}
        if (s=='/') {System.out.println("LOG "+num1+"/"+num2+" = "+(num1/num2));if (num2!=0)return Double.toString(num1/num2); else return "Деление на ноль";}
        if (s=='+') {System.out.println("LOG "+num1+"+"+num2+" = "+(num1+num2));return Double.toString(num2+num1);}
        if (s=='÷') {System.out.println("LOG "+num1+"÷"+num2+" = "+(num1/num2));if (num2!=0)return Double.toString(num1/num2); else return "Деление на ноль";}
        return "calcOperation не подсчитан";
    }
   /* public void calculator()  // Проверка является ли последний символ цифрой
    {
        String op1 = "";     String op2 = "";
        String dig1=""; String dig2="";
        int i = 0;
        if (stackNumer.size()==1) resultText =stackNumer.get(0);
        System.out.println("LOG  на вхаде калькулятора (i+sumSkOp) =     "+(i+sumSkOp));
            while (stackOperator.size()>0 && Collections.frequency(stackOperator, "(")==Collections.frequency(stackOperator, ")")){
                op1 = stackOperator.get(i+sumSkOp);
                if (isOperator(op1)){
                    try {
                        op2=stackOperator.get(i+sumSkOp+1);
                    } catch (Exception e) {
                        System.out.println("LOG Второго оператора нет в Стеке");
                    }
                    if (powerOperation (op1,op2)){// Если надо, выполняем операцию, удаляем вторую запись из стека чисел и записываем в первое значение
                        calcOperation ( op1,dig1, dig2 );
                    }
                    try {
                        stackOperator.remove(i+sumSkOp);
                        System.out.println("LOG Удаляем оператор = "+stackOperator);
                    } catch (Exception e) {
                        System.out.println("LOG Ошибка удаления stackOperator "+stackOperator.get(i+sumSkOp));
                    }
                    continue;
                }

                stackOperator.clear ();
                *//*if (stackOperator.get(i+sumSkOp).equals("(") ){ // If "("

                    if (stackOperator.size()>(i+sumSkOp+1)){
                        if (stackOperator.get(i+sumSkOp+1).equals ( ")" )){
                            System.out.println("LOG удаляем (    "+stackOperator.get(i+sumSkOp+1));
                            System.out.println("LOG удаляем )    "+stackOperator.get(i+sumSkOp));
                            stackOperator.remove(i+sumSkOp+1);
                            stackOperator.remove(i+sumSkOp);
                            sumSkOp--;
                            continue;
                        }}
                    sumSkOp++;
                    System.out.println("LOG количество ковычек (  = "+sumSkOp);
                    continue;
                 }
                System.out.println("LOG i = " +i+ "     sumSkOp = "+sumSkOp);
                if (stackOperator.size()>(i+sumSkOp))
                if (isOperator(stackOperator.get(i+sumSkOp))){
                    try {
                        stackOperator.remove(i+sumSkOp);
                        System.out.println("LOG Удаляем оператор = "+stackOperator);
                        if (stackOperator.size()==0) break;
                    } catch (Exception e) {
                        System.out.println("LOG Ошибка удаления stackOperator "+stackOperator.get(i+sumSkOp));
                    }continue;
                    }
                System.out.println("LOG калькулятор на выходе ="+stackOperator);
                System.out.println("LOG калькулятор на выходе  sumSkOp = "+sumSkOp);*//*
            } // while
    }*/
}
