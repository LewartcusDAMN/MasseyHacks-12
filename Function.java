
import java.util.ArrayList;

public class Function {
    public String base;
    public String exponent;
    public String coeff;
    ArrayList<String> terms;
    public Function(String func){
        base = "";
        exponent = "";
        coeff = "";
        terms = Utils.term_splitter(func);
        


        
        System.out.println(terms);
        // for (int i = 0; i < func.length(); i ++){
        //     char c = func.charAt(i);
        //     if (c > 47 && c < 58){
        //         coeff = coeff + c;
        //     }
        //     System.out.println(c);
        // }
        System.out.println(coeff);
    }



}
