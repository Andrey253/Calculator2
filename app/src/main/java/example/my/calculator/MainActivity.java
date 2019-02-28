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
    private String InText = "Вводимая формула";             // Текст вводимой формулы
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

    public void onClickSkCl(View v){ // Проверка разрешенияя на ввод " ) "
        Button b = (Button) v;
        InText = countSymbol (InText,'(' ) > countSymbol (InText, ')' ) //  Если количество скобок " ( " не больше " ) "
                && lastCharIsNum(InText) ? InText+b.getText(): InText;                  // то ввод " ) " запрещен
        updateIn();
    }
    public void onClickSkOp(View v){
        Button b = (Button) v;
        InText = enableSkOp(InText)? InText+b.getText(): InText;
        updateIn();
    }
    public void onClickBack(View v){

        InText =  InText.length ()!= 0 ? InText.substring(0, InText.length()-1) : InText;
        updateIn();
    }
    public void onClickOperator(View v){
        if(resultText != ""){
            clear();
            updateIn();
        }
        Button b = (Button) v;
        InText += (String) b.getText();
        updateIn();
    }
    public void onClickTchk(View v){
        if(resultText != ""){
            clear();
            updateIn();
        }
        Button b = (Button) v;
        InText += b.getText();
        updateIn();
    }
    public void onClickClear(View v){
        InText = "";
        updateIn();
    }
    public void onClickDr(View v){
        if(resultText != ""){
            clear();
            updateIn();
        }
        Button b = (Button) v;
        InText += b.getText();
        updateIn();
    }

    public void onClickNumber(View v){
        if(resultText != ""){
            clear();
            updateIn();
        }
        Button b = (Button) v;
        InText += b.getText();
        updateIn();
    }
    public float getSubresult() {
        return subresult;
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
    public static boolean lastCharIsNum(String text)  // Проверка является ли последний символ числом
    {
        char Char;
        Char=text.charAt(text.length() - 1);
        if (Char=='0' || Char=='1' ||Char=='2' ||Char=='3' ||Char=='4' ||Char=='5' ||Char=='6' ||Char=='7'|| Char=='8' ||Char=='9')
        return true;
        return  false;
    }
    public static boolean enableSkOp(String text)  // Проверка является ли последний символ числом
    {
        char Char;
        Char=text.charAt(text.length() - 1);
        if (text.length()==0 || Char=='+' ||Char=='-' ||Char=='x' ||Char=='÷')
            return true;
        return  false;
    }

}
