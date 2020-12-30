package lr1;

import utilities.Grammar;
import utilities.Rule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
/**
 *
 * @author Usama Sarwar <UsamaSarwarOfficial@gmail.com>
 * @version v1.0.0
 * @since 2020
 *
 */
public class LR1State {
    private LinkedHashSet<LR1Item> items;
    private HashMap<String,LR1State> transition;
/*
    1st iteration
    core itemes=[S' -> .S , [$]]
    */
    public LR1State(Grammar grammar,HashSet<LR1Item> coreItems){
        items = new LinkedHashSet<>(coreItems);
        transition = new HashMap<>();
        closure(grammar);
        System.out.println("item item item item"+items);
        //item item item item[S' -> .S , [$], S -> .A a A b , [$], S -> .B b B a , [$], B -> .epsilon , [b], A -> .epsilon , [a]]
    }

    private void closure(Grammar grammar) {
        boolean changeFlag = false;
        do {
            changeFlag = false;
            HashSet<LR1Item> temp = new HashSet<>();
            /*
            1st iteration
            itemes=[S' -> .S , [$]]
            condition is true
            */
            /*
            2nd iteration
            itemA -> epsilon. , [a]
            condition is false
            */
             /*
            2nd iteration
            item=B -> epsilon. , [b]
            condition is false
            */
            for(LR1Item item : items){
                System.out.println("item ="+item);
                System.out.println("dot pointer"+item.getDotPointer());System.out.println("right side length= "+item.getRightSide().length+"current terminal "+item.getCurrent());
                    /*1st iteration
                    dot pointer=0 
                    right side length= 1
                    current terminal =S
                    condition is true
                    */
                if(item.getDotPointer() != item.getRightSide().length && grammar.isVariable(item.getCurrent())){
                    System.out.println(" condition is true ");
                    HashSet<String> lookahead = new HashSet<>();
                    if(item.getDotPointer() == item.getRightSide().length - 1){
                         System.out.println(" condition is true 1");
                        lookahead.addAll(item.getLookahead());
                        System.out.println(" condition is true lookahead"+lookahead);
                        //1st iteration
                        // condition is true lookahead=[$]
                    }else{
                        HashSet<String> firstSet = grammar.computeFirst(item.getRightSide(),item.getDotPointer()+1);
                        if(firstSet.contains("epsilon")){
                            firstSet.remove("epsilon");
                            firstSet.addAll(item.getLookahead());
                        }
                        lookahead.addAll(firstSet);
                    }
                    HashSet<Rule> rules = grammar.getRuledByLeftVariable(item.getCurrent());
                    //1st iteration
                    // rules contains = [S -> A a A b , S -> B b B a ]
                    System.out.println("rules contains = "+rules);
                    for(Rule rule : rules){
                        /*1st iteration
                        first rules = S -> A a A b 
                        */
                        System.out.println("first rules = "+rule);
                        /*
                        1st iteration
                        left side=S
                        right side=A a A b 
                        0
                        lookahead=$
                        */
                        temp.add(new LR1Item(rule.getLeftSide(),rule.getRightSide(),0,lookahead));
                        //1st iteration
                        //first temp= = [S -> .A a A b , [$], S -> .B b B a , [$]]
                        System.out.println("first temp= = "+temp);
                    }
                }
            }
            if(!items.containsAll(temp)){
                items.addAll(temp);
                changeFlag = true;
            }
             System.out.println("start state = = ++++++++++++++++"+items);
             /*2nd iteration
             start state = = ++++++++++++++++[A -> epsilon. , [a], B -> epsilon. , [b]]
             */
             
        } while (changeFlag);
        

    }

    public HashMap<String, LR1State> getTransition() {
        return transition;
    }

    public LinkedHashSet<LR1Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        String s = "";
        for(LR1Item item:items){
            s += item + "\n";
        }
        return s;
    }

}
