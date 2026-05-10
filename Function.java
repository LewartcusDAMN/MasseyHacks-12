
import java.util.ArrayList;

public class Function {
    public ArrayList<String> terms;
    String expression;

    public Function(String func){
        expression = func;

        if (Utils.term_splitter(func).size() == 1){
            terms = new ArrayList<>();
            terms.add(func);
        } else {
            terms = Utils.term_splitter(func);
        }
        System.out.println(terms);
       
        
        output();
    }

    public int output(){
        int output = 0;
        for (String term : terms){
            int coeff = 1;
            for (int i = 0; i < term.length()-1; i ++){
                char c = term.charAt(i);
                if (c >= 48 && c < 58){
                    System.out.println("."+term.substring(0, i)+".");
                    //coeff = Integer.parseInt(term.substring(0, i));
                    break;
                }
            }
            //System.out.println(coeff);
        }
        return output;
    }

    @Override
    public String toString(){
        return this.expression;
    }

}
