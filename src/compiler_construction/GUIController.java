package compiler_construction;

import java.net.URL;
import javafx.fxml.FXML;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import java.util.StringTokenizer;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.util.Stack;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usama Sarwar <UsamaSarwarOfficial@gmail.com>
 * @version v1.0.0
 * @since 2020
 *
 */
public class GUIController implements Initializable {

    static String[] productions;
    static String tempNT = "";
    static HashMap<String, String[]> hmap = new HashMap<String, String[]>();

    protected ArrayList<String> keyWords = new ArrayList<>(Arrays.asList("program", "var", "integer", "real", "boolean", "procedure", "begin",
            "end", "if", "then", "else", "while", "do", "not"));

    protected ArrayList<String> delimiters = new ArrayList<>(Arrays.asList(";", ".", ":", "(", ")", ","));
    protected ArrayList<String> operatiorsR = new ArrayList<>(Arrays.asList("=", "<", ">", "<=", ">=", "<>"));
    protected ArrayList<String> operatiorsA = new ArrayList<>(Arrays.asList("+", "-", "or"));
    protected ArrayList<String> operatiorsM = new ArrayList<>(Arrays.asList("*", "/", "and"));
    protected ArrayList<String> booleanDigits = new ArrayList<>(Arrays.asList("true", "false"));

    protected String attributtion = ":=";
    protected String identifiers = "([a-z]|[A-Z])([0-9]|[a-z]+|[A-Z]|_)*";
    protected String intDigits = "[0-9]+";
    protected String floatDigits = "[0-9]+[.][0-9]+";

    protected ArrayList<String> myProgram = null;

    @FXML
    private TextField input;
    @FXML
    private TextArea output;
    @FXML
    private TextArea output_FF;
    @FXML
    private TextArea input_FF;
    @FXML
    private TextArea output_LR;
    @FXML
    private TextArea input_LR;
    @FXML
    private TextField input_LR_count;
    @FXML
    private TextField regexp_;
    @FXML
    private TextField testString_;
    @FXML
    private Circle validCheck;
    @FXML
    private Circle invalidCheck;
    @FXML
    public TextArea tokens_;
    @FXML
    private TextArea javaCode_;
    @FXML
    private TextArea output_FFF;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        checkRegularExpression();
        startTokenization();
    }

    @FXML
    private void startFA_(ActionEvent event) {
        try {
            GUI.Main.main(productions);
        } catch (InterruptedException ex) {
            // Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void tokenization_(KeyEvent event) {
        startTokenization();
    }

    void startTokenization() {
        tokens_.setText("");
        String strLine = javaCode_.getText();
        ArrayList<String> lines = new ArrayList<>();
        lines.add(strLine);
        myProgram = lines;
        int resp = lexicAnalizer();
    }

    void checkRegularExpression() {
        String regexexp = regexp_.getText();
        String regulars = testString_.getText();
        Boolean reg = Pattern.matches(regexexp, regulars);
        if (reg) {
            validCheck.setVisible(true);
            invalidCheck.setVisible(false);
        } else {
            validCheck.setVisible(false);
            invalidCheck.setVisible(true);
        }
    }

    @FXML
    private void checkRegEx_(KeyEvent event) {
        checkRegularExpression();
    }

    @FXML
    private void checkString_(KeyEvent event) {
        checkRegularExpression();
    }

    @FXML
    private void lr0Fun(ActionEvent event) {
        lr0.Main.main(productions);

    }

    @FXML
    private void clr1Fun(ActionEvent event) {
        clr1.CLR1.main(productions);
    }

    @FXML
    private void lr1Fun(ActionEvent event) {
        lr1_main.LALR1.main(productions);
    }

    @FXML
    private void slr1Fun(ActionEvent event) {
        slr1.SLR1.main(productions);
    }

    @FXML
    private void leftFactoringFun(ActionEvent event) {
        commonPrefixes(input.getText());
    }

    @FXML
    private void firstFollowFun(ActionEvent event) {
        Grammar grammar = new Grammar(input_FF.getText());
        String first = "";
        String follow = "";
        for (String s : grammar.getFirstSets().keySet()) {
            first += s + " : " + grammar.getFirstSets().get(s) + "\n";
        }
        for (String s : grammar.getFallowSets().keySet()) {
            follow += s + " : " + grammar.getFallowSets().get(s) + "\n";
        }
        output_FF.setText("First\n==========\n");
        output_FF.appendText(first);
        output_FF.appendText("\n\nFollow\n==========\n");
        output_FF.appendText(follow);
    }

    @FXML
    private void leftRecursionFun(ActionEvent event) {
        int noProductions = Integer.parseInt(input_LR_count.getText());

        productions = new String[noProductions];
        productions = input_LR.getText().split("\n");

        output_LR.setText("\nGiven Productions\n");
        printProductions();
        findNonTerminals();
        initHash();
        printHashMap();

        for (int i = 0; i < tempNT.length(); i++) {
            if (hmap.containsKey(tempNT.charAt(i) + "")) {
                removeImmediate((tempNT.charAt(i) + ""), hmap.get(tempNT.charAt(i) + ""));
            }
        }

        while (removeIndirect()) {
        }

        for (int i = 0; i < tempNT.length(); i++) {
            if (hmap.containsKey(tempNT.charAt(i) + "")) {
                removeImmediate((tempNT.charAt(i) + ""), hmap.get(tempNT.charAt(i) + ""));
            }
        }

        output_LR.appendText("\n\nAfter removing Left Recursions");
        printHashMap();

        output_LR.appendText("\n");

        // First and Follow Code here
        Grammar grammar = new Grammar(input_LR.getText());
        String first = "";
        String follow = "";
        for (String s : grammar.getFirstSets().keySet()) {
            first += s + " : " + grammar.getFirstSets().get(s) + "\n";
        }
        for (String s : grammar.getFallowSets().keySet()) {
            follow += s + " : " + grammar.getFallowSets().get(s) + "\n";
        }
        output_FFF.setText("First\n==========\n");
        output_FFF.appendText(first);
        output_FFF.appendText("\n\nFollow\n==========\n");
        output_FFF.appendText(follow);

    }

    void initHash() {

        Stack<String> stack[] = new Stack[tempNT.length()];
        String[] hmapString;

        for (int i = 0; i < stack.length; i++) {
            stack[i] = new Stack<String>();
        }

        for (int i = 0; i < productions.length; i++) {
            stack[tempNT.indexOf(productions[i].charAt(0))].push(productions[i].substring(3));
        }

        for (int i = 0; i < stack.length; i++) {

            hmapString = new String[stack[i].size()];

            if (!stack[i].empty()) {

                for (int j = 0; !stack[i].empty(); j++) {
                    hmapString[j] = stack[i].pop();
                }

                hmap.put((tempNT.charAt(i) + ""), hmapString);
            }
        }
    }

    void printProductions() {

        for (int i = 0; i < productions.length; i++) {

            output_LR.appendText(productions[i] + "\n");
        }
    }

    void removeImmediate(String indexName, String[] q) {

        Stack<String> q1 = new Stack<String>();
        Stack<String> q2 = new Stack<String>();

        for (int i = 0; i < q.length; i++) {

            if (indexName.equals(q[i].charAt(0) + "")) {
                q1.push(q[i]);				//Recursive productions
            } else {
                q2.push(q[i]);
            }
        }

        if (!q1.empty()) {

            hmap.remove(indexName);
            String[] hmapString = new String[2 * q2.size()];

            int i = 0;
            while (!q2.empty()) {

                hmapString[i++] = q2.peek();
                hmapString[i++] = (q2.pop() + "k" + indexName);
            }
            hmap.put(indexName, hmapString);

            hmapString = new String[2 * q1.size()];
            i = 0;
            while (!q1.empty()) {

                hmapString[i++] = q1.peek().substring(1);
                hmapString[i++] = (q1.pop().substring(1) + "k" + indexName);
            }
            hmap.put(("k" + indexName), hmapString);
        }
    }

    void printHashMap() {

        Set set = hmap.entrySet();
        Iterator i = set.iterator();
        String keyName;
        String keyValues[];

        while (i.hasNext()) {

            Map.Entry me = (Map.Entry) i.next();
            keyName = me.getKey().toString();
            keyValues = hmap.get(keyName);

            output_LR.appendText("\n" + keyName + " -> ");
            for (int j = 0; j < keyValues.length; j++) {

                output_LR.appendText(keyValues[j] + " ");
            }
        }
    }

    void findNonTerminals() {

        for (int i = 0; i < productions.length; i++) {
            for (int j = 0; j < productions[i].length(); j++) {
                if ((productions[i].charAt(j) >= 'A' && productions[i].charAt(j) <= 'Z') && (tempNT.indexOf(productions[i].charAt(j)) == -1)) {
                    tempNT += productions[i].charAt(j);
                }
            }
        }

        output_LR.appendText("\n\nNon Terminals : " + tempNT);
    }

    boolean removeIndirect() {

        Set set = hmap.entrySet();
        Iterator i = set.iterator();
        String keyName;
        String keyValues[];
        Stack<String> tempp = new Stack<String>();

        while (i.hasNext()) {

            Map.Entry me = (Map.Entry) i.next();
            keyName = me.getKey().toString();
            keyValues = hmap.get(keyName);

            for (int j = 0; j < keyValues.length; j++) {
                if (keyValues[j].charAt(0) >= 'A' && keyValues[j].charAt(0) <= 'Z' && hmap.containsKey(keyValues[j].charAt(0) + "")) {
                    if (tempNT.indexOf(keyName.charAt(0)) > tempNT.indexOf(keyValues[j].charAt(0))) {
                        String[] sub = hmap.get(keyValues[j].charAt(0) + "");
                        for (int z = 0; z < keyValues.length; z++) {
                            if (z != j) {
                                tempp.push(keyValues[z]);
                            }
                        }

                        for (int z = 0; z < sub.length; z++) {
                            tempp.push(sub[z] + keyValues[j].substring(1));
                        }

                        String[] hmapString = new String[tempp.size()];
                        for (int z = 0; !tempp.empty(); z++) {
                            hmapString[z] = tempp.pop();
                        }

                        hmap.put(keyName, hmapString);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    void commonPrefixes(String line) {
        System.out.println("Input : " + line);
        //Input : S=iEtS|iEtSes|a  1st iteration
        System.out.println("LEFT FACTORING : ");
        output.appendText("LEFT FACTORING : ");
        StringTokenizer tokenizer = new StringTokenizer(line, "=|");
        // split grammmer on the base of equal and or operator or both

        String first = tokenizer.nextToken();
        System.out.println("first : " + first);
        output.appendText("\n" + "first : " + first);
        // first contains first : S
        String commonPrefixes = "";

        ArrayList<String> tokens = new ArrayList<String>();

        tokens.clear();
        /*
        iEtS
        iEtSes
        a
         */
        while (tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken());

        }
        System.out.println("tokens : " + tokens);
        output.appendText("\n" + "tokens : " + tokens);
        //tokens : [iEtS, iEtSes, a]
        int max = 0;
        System.out.println("tokens size : " + tokens.size());
        output.appendText("\n" + "tokens size : " + tokens.size());
        for (int i = 0; i < tokens.size() - 1; i++) {
            for (int j = i + 1; j < tokens.size(); j++) {
                /*
                1st itration tokens.get(i)=iEtS and tokens.get(j)= iEtSes
                 */
                System.out.println("tokens.size(i) : " + tokens.get(i) + "tokens.size(j) :" + tokens.get(j));
                output.appendText("\n" + "tokens.size(i) : " + tokens.get(i) + "tokens.size(j) :" + tokens.get(j));
                // find longest commonPrefixes using LongestCommon method
                int max2 = LongestCommon(tokens.get(i), tokens.get(j));
                // now max2=4 max=0
                if (max2 > max) {

                    max = max2;
                    commonPrefixes = tokens.get(i).substring(0, max);
                }
                System.out.println("commonPrefixes =" + commonPrefixes);
                output.appendText("\n" + "commonPrefixes =" + commonPrefixes);
                //
            }
        }

        CompilerConstruction fact = new CompilerConstruction();
        /*
        first=S
        commonPrefixes =iEtS
        tokens : [iEtS, iEtSes, a]
         */
        factor(first, commonPrefixes, tokens);

    }

    public int LongestCommon(String str1, String str2) {
        /*
        str1 =: iEtS         
        str2= : iEtSes
         */
        System.out.println("str1 =: " + str1 + "str2= : " + str2);
        output.appendText("\n" + "str1 =: " + str1 + "str2= : " + str2);
        if (str1 == null || str2 == null) {
            return 0;
        }
        int len = 0;
        int loopcount;
        /*
        1st time if condition is true
         */
        if (str1.length() < str2.length()) {
            loopcount = str1.length();
        } else {
            loopcount = str2.length();
        }
        /*
        loopcount=4
        str1 =: iEtS length=4
        str2= : iEtSes length=6
         */
        for (int i = 0; i < loopcount; i++) {
            if (str1.charAt(i) == str2.charAt(i)) {
                len++;
            } else {
                return len;
            }
        }

        return len;
    }

    void factor(String first, String commonPrefixes, ArrayList<String> tokens) {
        // Left Factring
        String next = new String();
        ArrayList<String> suffix = new ArrayList<String>();
        ArrayList<String> prefix = new ArrayList<String>();

        int tokensize;
        for (tokensize = 0; tokensize < tokens.size(); tokensize++) {
            next = tokens.get(tokensize);
            //next=iEtS
            System.out.println("next=" + next);
            output.appendText("\n" + "next=" + next);
            /*
            if condition is true 1st time
            if condition is false in 2nd iteration
             */
            if (next.equals(commonPrefixes)) {
                suffix.add("ε");
                prefix.add(commonPrefixes);

            } //else if condition is true in 2nd iteration
            else if (next.startsWith(commonPrefixes)) {
                prefix.add(commonPrefixes);
                char[] charArray = next.toCharArray();
                String x = new String(charArray, commonPrefixes.length(), next.length() - commonPrefixes.length());
                System.out.println("string x =" + x);
                output.appendText("\n" + "string x =" + x);
                //string x=es
                suffix.add(x);

            } else {
                prefix.add(next);

            }
        }

        int i;
        /*
        S=iEtSS`|a
         */
        String firstoutput = first + "=" + commonPrefixes + first + "`|";
        for (i = 0; i < prefix.size(); i++) {
            if (i == prefix.size() - 1) {
                break;
            }
            if (prefix.get(i) != commonPrefixes) {
                firstoutput += prefix.get(i);
                firstoutput += "|";
            }
        }
        firstoutput += prefix.get(i);
        System.out.println(firstoutput);
        output.appendText("\n" + firstoutput);
        /*
        S`=ε|es
         */
        String secondoutput = first + "`=";
        i = 0;
        for (i = 0; i < suffix.size() - 1; i++) {
            secondoutput += suffix.get(i);
            secondoutput += "|";
        }
        secondoutput += suffix.get(i);
        System.out.println(secondoutput);
        output.appendText("\n" + secondoutput);
        System.out.println("\n");
        output.appendText("\n" + "\n");
    }

    public int lexicAnalizer() {

//        GUIController gc= new GUIController();
        for (int i = 0; i < this.myProgram.size(); i++) {
            String line = discardComments(this.myProgram.get(i));
            String[] splited = line.split("\\s+");

            for (int j = 0; j < splited.length; j++) {
                String token = splited[j];
                String result = checkToken(token);
                if (!result.equals("unknown")) {
//                    System.out.println(token + " | " + result + " |" + i);
                    tokens_.appendText(token + "\t\t|\t" + result + "\n");

                } else {
                    String subToken = token.substring(0, token.length() - 1);
                    result = checkToken(subToken);
                    if (!result.equals("unknown")) {
//                        System.out.println(subToken + " | " + result + " |" + i);
                        tokens_.appendText(subToken + "\t\t|\t" + result + "\n");

                        String lastToken = token.substring(token.length() - 1, token.length());
                        result = checkToken(lastToken);
                        if (!result.equals("unknown")) {
//                            System.out.println(lastToken + " | " + result + " |" + i);
                            tokens_.appendText(lastToken + "\t\t|\t" + result + "\n");
                        } else {
                            System.err.println("Error: Does not belong to language!");
                            System.err.println("Line: " + i + ", Status: " + result + " -> " + subToken);
//                            return 0;
                        }
                    } else {
                        System.err.println("Error: Does not belong to language!");
                        System.err.println("Line: " + i + ", Status: " + result + " -> " + subToken);
//                        return 0;
                    }
                }
            }
        }
        return 1;
    }

    String discardComments(String line) {
        return line.replaceAll("\\{(?s).*?}", "");
    }

    String checkToken(String split) {

        if (this.keyWords.contains(split)) {
            return "Keyword";
        } else if (this.delimiters.contains(split)) {
            return "Delimiter";
        } else if (this.operatiorsR.contains((split))) {
            return "Comparison Operator";
        } else if (this.operatiorsA.contains(split)) {
            return "Additive Operator";
        } else if (this.operatiorsM.contains(split)) {
            return "Multiplicative Operator";
        } else if (split.matches(this.intDigits)) {
            return "Integer Digit";
        } else if (split.matches(this.floatDigits)) {
            return "Floating Point Digit";
        } else if (split.matches(this.identifiers)) {
            if (this.booleanDigits.contains(split)) {
                return "Boolean Digit";
            }
            return "Identifier";
        } else if (split.matches("\\{(?s).*?")) {
            return "unknown";
        }
        return "unknown";
    }

}
