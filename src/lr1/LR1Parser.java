package lr1;

import utilities.LRParser;
import utilities.ActionType;
import utilities.Action;
import utilities.Rule;
import utilities.Grammar;
import lr0.LR0Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
/**
 *
 * @author Usama Sarwar <UsamaSarwarOfficial@gmail.com>
 * @version v1.0.0
 * @since 2020
 *
 */
public class LR1Parser extends LRParser {

    private ArrayList<LR1State> canonicalCollection;
//constructor is call in 1st iteration
    public LR1Parser(Grammar grammar){
        //super keyword is call LRParser Constructor
        super(grammar);
        System.out.println("LR1Parser Constructor");
    }

    protected void createStatesForCLR1() {
        System.out.println("createStatesForCLR1");
        canonicalCollection = new ArrayList<>();
        HashSet<LR1Item> start = new HashSet<>();
        Rule startRule = grammar.getRules().get(0);
        //1st iteration 
        //createStatesForCLR1 start ruleS' -> S 
        System.out.println("createStatesForCLR1 start rule"+startRule);
        HashSet<String> startLockahead = new HashSet<>();
        startLockahead.add("$");
        /*
        1st iteration
        left side=S'
        right side=S
        0
        lookahead=$
        */
        start.add(new LR1Item(startRule.getLeftSide(),startRule.getRightSide(),0,startLockahead));
        System.out.println("start"+start);
        //1st iteration
        //start[S' -> .S , [$]]
        LR1State startState = new LR1State(grammar, start);
        /*
        1st iteration
        S' -> .S , [$]
        S -> .A a A b , [$]
        S -> .B b B a , [$]
        B -> .epsilon , [b]
        A -> .epsilon , [a]
        */
        
        System.out.println("start state = = ++++++++++++++++"+startState);
        canonicalCollection.add(startState);
        /*
        [
        S' -> .S , [$]
        S -> .A a A b , [$]
        S -> .B b B a , [$]
        B -> .epsilon , [b]
        A -> .epsilon , [a]
        ]
        */
        System.out.println("cannonical collection = ++++++++++++++++"+canonicalCollection);
        System.out.println("cannonical collection size "+canonicalCollection.size());

        for (int i = 0; i < canonicalCollection.size(); i++) {
            HashSet<String> stringWithDot = new HashSet<>();
            
            for (LR1Item item : canonicalCollection.get(i).getItems()) {
                System.out.println("cannonical collection current terminal= "+item);
                /*
                S' -> .S , [$]        1st iteration item.getCurrent()=S
                S -> .A a A b , [$]   2nd iteration item.getCurrent()=A
                S -> .B b B a , [$]   3rd iteration item.getCurrent()=B
                B -> .epsilon , [b]   4th iteration item.getCurrent()=epsilon
                A -> .epsilon , [a]   5th iteration item.getCurrent()=epsilon
                */
                if (item.getCurrent() != null) {
                    stringWithDot.add(item.getCurrent());
                }
            }
            //string with dot contains = [epsilon, A, B, S]
              System.out.println("string with dot contains = "+stringWithDot);
            for (String str : stringWithDot) {
                System.out.println("string with dot contains str== = "+str);
                /*
                1st iteration
                string with dot contains str== = epsilon
                */
                HashSet<LR1Item> nextStateItems = new HashSet<>();
                for (LR1Item item : canonicalCollection.get(i).getItems()) {
                    System.out.println("string with item contains = "+item);
                    /*1st iteration
                    S' -> .S , [$]
                    str contains epsilon so if condition is false
                    2nd iteration
                    S -> .A a A b , [$]
                    str contains epsilon so if condition is false
                    3rd iteration
                    S -> .B b B a , [$]
                    str contains epsilon so if condition is false
                    4th iteration
                    B -> .epsilon , [b]
                    str contains epsilon so if condition is true
                    */
                    if (item.getCurrent() != null && item.getCurrent().equals(str)) {
                        System.out.println("string with item contains = "+item);
                        
                        //1st iteration string with item contains = B -> .epsilon , [b]
                        //2nd iteration string with item contains = A -> .epsilon , [b]
                        LR1Item temp = new LR1Item(item.getLeftSide(),item.getRightSide(),item.getDotPointer()+1,item.getLookahead());
                        System.out.println("temp contains = "+temp);
                        //1st iteration
                        //temp contains = B -> epsilon. , [b]
                         //2nd iteration
                        //temp contains = A -> epsilon. , [a]
                        nextStateItems.add(temp);
                        System.out.println("nextStateItems contains = "+nextStateItems);
                        
                    }
                }
                System.out.println("nextStateItems contains = "+nextStateItems);
                //nextStateItems contains = [A -> epsilon. , [a], B -> epsilon. , [b]]
                LR1State nextState = new LR1State(grammar, nextStateItems);
                System.out.println("next state next state = "+nextState);
                /*
                next state next state = S' -> S. , [$]
                next state next state = S -> A a .A b , [$]
                next state next state = S -> B b .B a , [$]
                next state next state = A -> epsilon. , [b]
                next state next state = S -> A a A .b , [$]
                next state next state = B -> epsilon. , [a]
                next state next state = S -> B b B .a , [$]
                next state next state = S -> A a A b. , [$]
                next state next state = S -> B b B a. , [$]
                */
                boolean isExist = false;
                /*
                [
                S' -> .S , [$]
                S -> .A a A b , [$]
                S -> .B b B a , [$]
                B -> .epsilon , [b]
                A -> .epsilon , [a]
                , A -> epsilon. , [a]
                B -> epsilon. , [b]
                , S -> A .a A b , [$]
                , S -> B .b B a , [$]
                , S' -> S. , [$]
                , S -> A a .A b , [$]
                A -> .epsilon , [b]
                , S -> B b .B a , [$]
                B -> .epsilon , [a]
                , A -> epsilon. , [b]
                , S -> A a A .b , [$]
                , B -> epsilon. , [a]
                , S -> B b B .a , [$]
                , S -> A a A b. , [$]
                ]
                */
                System.out.println("canonical collection=== = "+canonicalCollection);
                for (int j = 0; j < canonicalCollection.size(); j++) {
                     System.out.println("canonical collection item ++++= "+canonicalCollection.get(j).getItems()+"next state item ++++="+nextState.getItems());
                    if (canonicalCollection.get(j).getItems().containsAll(nextState.getItems())
                            && nextState.getItems().containsAll(canonicalCollection.get(j).getItems())) {
                        isExist = true;
                        canonicalCollection.get(i).getTransition().put(str, canonicalCollection.get(j));
                    }
                }
                if (!isExist) {
                    canonicalCollection.add(nextState);
                    /*
                    {{{{{{{{ {epsilon=A -> epsilon. , [a]
                    B -> epsilon. , [b]
                    }
                    {{{{{{{{ {epsilon=A -> epsilon. , [a]
                    B -> epsilon. , [b]
                    , A=S -> A .a A b , [$]
                    }
                    {{{{{{{{ {epsilon=A -> epsilon. , [a]
                    B -> epsilon. , [b]
                    , A=S -> A .a A b , [$]
                    , B=S -> B .b B a , [$]
                    }
                    {{{{{{{{ {epsilon=A -> epsilon. , [b]
                    {{{{{{{{ {epsilon=B -> epsilon. , [a]
                    */
                    System.out.println("{{{{{{{{ "+canonicalCollection.get(i).getTransition());
                    canonicalCollection.get(i).getTransition().put(str, nextState);
                }
            }
        }
        System.out.println("canonical collection=== = last"+canonicalCollection);
        /*
        canonical collection=== = last[S' -> .S , [$]
S -> .A a A b , [$]
S -> .B b B a , [$]
B -> .epsilon , [b]
A -> .epsilon , [a]
, A -> epsilon. , [a]
B -> epsilon. , [b]
, S -> A .a A b , [$]
, S -> B .b B a , [$]
, S' -> S. , [$]
, S -> A a .A b , [$]
A -> .epsilon , [b]
, S -> B b .B a , [$]
B -> .epsilon , [a]
, A -> epsilon. , [b]
, S -> A a A .b , [$]
, B -> epsilon. , [a]
, S -> B b B .a , [$]
, S -> A a A b. , [$]
, S -> B b B a. , [$]
]
        */
        

    }

    public boolean parseCLR1(){
        createStatesForCLR1();
        createGoToTable();
        return createActionTable();
    }

    public boolean parseLALR1(){
        createStatesForLALR1();
        createGoToTable();
        return createActionTable();
    }

    public void createStatesForLALR1(){
        createStatesForCLR1();
        ArrayList<LR1State> temp = new ArrayList<>();
        for (int i = 0; i < canonicalCollection.size(); i++) {
            HashSet<LR0Item> itemsi = new HashSet<>();
            for(LR1Item item:canonicalCollection.get(i).getItems()){
                itemsi.add(new LR0Item(item.getLeftSide(),item.getRightSide(),item.getDotPointer()));
            }
            for (int j = i+1; j < canonicalCollection.size(); j++) {
                HashSet<LR0Item> itemsj = new HashSet<>();
                for(LR1Item item:canonicalCollection.get(j).getItems()){
                    itemsj.add(new LR0Item(item.getLeftSide(),item.getRightSide(),item.getDotPointer()));
                }
                if(itemsi.containsAll(itemsj) && itemsj.containsAll(itemsi)){
                    for(LR1Item itemi : canonicalCollection.get(i).getItems()){
                        for(LR1Item itemj : canonicalCollection.get(j).getItems()){
                            if(itemi.equalLR0(itemj)){
                                itemi.getLookahead().addAll(itemj.getLookahead());
                                break;
                            }
                        }
                    }
                    for (int k = 0; k < canonicalCollection.size(); k++) {
                        for(String s : canonicalCollection.get(k).getTransition().keySet()){
                            if(canonicalCollection.get(k).getTransition().get(s).getItems().containsAll(canonicalCollection.get(j).getItems()) &&
                                    canonicalCollection.get(j).getItems().containsAll(canonicalCollection.get(k).getTransition().get(s).getItems())){
                                canonicalCollection.get(k).getTransition().put(s,canonicalCollection.get(i));
                            }
                        }
                    }
                    canonicalCollection.remove(j);
                    j--;

                }
            }
            temp.add(canonicalCollection.get(i));
        }
        canonicalCollection = temp;
    }

    @Override
    protected void createGoToTable() {
        System.out.println("create go to table start from here");
        goToTable = new HashMap[canonicalCollection.size()];
        for (int i = 0; i < goToTable.length; i++) {
            goToTable[i] = new HashMap<>();
            System.out.println("key: " + i + " value: " + goToTable[i]);
        }
        /*
        key: 0 value: {}
key: 1 value: {}
key: 2 value: {}
key: 3 value: {}
key: 4 value: {}
key: 5 value: {}
key: 6 value: {}
key: 7 value: {}
key: 8 value: {}
key: 9 value: {}
key: 10 value: {}
key: 11 value: {}
key: 12 value: {}

        */
          for (int i = 0; i < canonicalCollection.size(); i++) {
        for (String s :canonicalCollection.get(i).getTransition().keySet()) {
            System.out.println("Canonical collection s  = "+i+"  ="+s);
        }
        }
          /*
          Canonical collection s  = 0  =epsilon
Canonical collection s  = 0  =A
Canonical collection s  = 0  =B
Canonical collection s  = 0  =S
Canonical collection s  = 2  =a
Canonical collection s  = 3  =b
Canonical collection s  = 5  =epsilon
Canonical collection s  = 5  =A
Canonical collection s  = 6  =epsilon
Canonical collection s  = 6  =B
Canonical collection s  = 8  =b
Canonical collection s  = 10  =a
          */
        for (int i = 0; i < canonicalCollection.size(); i++) {
            for (String s : canonicalCollection.get(i).getTransition().keySet()) {
                for(int j=0;j<1;j++)
                {
                    System.out.println("Canonical collection s contains = "+s);
                }
                if (grammar.isVariable(s)) {
                    goToTable[i].put(s, findStateIndex(canonicalCollection.get(i).getTransition().get(s)));
                }
            }
        }
    }

    private int findStateIndex(LR1State state) {
        for (int i = 0; i < canonicalCollection.size(); i++) {
            if (canonicalCollection.get(i).equals(state)) {
                return i;
            }
        }
        return -1;
    }

    private boolean createActionTable() {
        actionTable = new HashMap[canonicalCollection.size()];
        for (int i = 0; i < goToTable.length; i++) {
            actionTable[i] = new HashMap<>();
        }
        for (int i = 0; i < canonicalCollection.size(); i++) {
            for (String s : canonicalCollection.get(i).getTransition().keySet()) {
                if (grammar.getTerminals().contains(s)) {
                    actionTable[i].put(s, new Action(ActionType.SHIFT, findStateIndex(canonicalCollection.get(i).getTransition().get(s))));
                }
            }
        }
        for (int i = 0; i < canonicalCollection.size(); i++) {
            for (LR1Item item : canonicalCollection.get(i).getItems()) {
                if (item.getDotPointer() == item.getRightSide().length) {
                    if (item.getLeftSide().equals("S'")) {
                        actionTable[i].put("$", new Action(ActionType.ACCEPT, 0));
                    } else {
                        Rule rule = new Rule(item.getLeftSide(), item.getRightSide().clone());
                        int index = grammar.findRuleIndex(rule);
                        Action action = new Action(ActionType.REDUCE, index);
                        for (String str : item.getLookahead()) {
                            if (actionTable[i].get(str) != null) {
                                System.out.println("it has a REDUCE-" + actionTable[i].get(str).getType() + " confilct in state " + i);
                                return false;
                            } else {
                                actionTable[i].put(str, action);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public String canonicalCollectionStr() {
        String str = "Canonical Collection : \n";
        for (int i = 0; i < canonicalCollection.size(); i++) {
            str += "State " + i + " : \n";
            str += canonicalCollection.get(i)+"\n";
        }
        return str;
    }

}
