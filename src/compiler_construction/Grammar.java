package compiler_construction;

import java.util.ArrayList;// import the ArrayList class
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Usama Sarwar <UsamaSarwarOfficial@gmail.com>
 * @version v1.0.0
 * @since 2020
 *
 */
public class Grammar {

    private ArrayList<Rule> rules;//create an arraylist object called rules 
    private HashSet<String> terminals;//(terminals cannot change hashset is used because no duplication of value)
    private HashSet<String> variables;/*values on the left side in a grammar also called non-terminals (can change)*/
    private String startVariable;//s
    private HashMap<String, HashSet<String>> firstSets;//Create a HashMap object called firstset that will store String keys and HashSettype values:
    private HashMap<String, HashSet<String>> fallowSets;//Create a HashMap object called fallowset that will store String keys and HashSettype values:
/*
S -> A B
A -> a B
B -> s
*/
    public Grammar(String s)//constructor s stores grammer
    {
        System.out.println("Grammer \n"+s);
        rules = new ArrayList<>();
        terminals = new HashSet<>();
        variables = new HashSet<>();
        int line = 0;
        for(String st : s.split("\n"))
        {
            String[] sides = st.split("->");
            String leftSide = sides[0].trim();//The trim() method removes whitespace from both ends of a string.
            System.out.println(leftSide);
            variables.add(leftSide);
         System.out.println(sides[1]);
            String[] rulesRightSide = sides[1].trim().split("\\|");
            for (String rule : rulesRightSide) 
            {
                System.out.println("rule"+rule); 
                String[] rightSide = rule.trim().split("\\s+");
                for (String terminal : rightSide) {
                    System.out.println("terminal and right side is 111111111 "+terminal);
                    terminals.add(terminal);
                }

                if (line == 0) 
                {
                    startVariable = leftSide;
                    //this function is call only first time
                    //call rule class cunstructor and add new rule
                 //  rules.add(new Rule("S'", new String[]{startVariable}));
                    for (int i=0 ; i<rules.size() ; i++) {
                System.out.println(i+" :if line =0  " +rules.get(i));
        }
                }
                
                //call rule class cunstructor and add new rule
                rules.add(new Rule(leftSide, rightSide));
                for (int i=0 ; i<rules.size() ; i++) {
                System.out.println(i+" : " +rules.get(i));
        }
                line++;
              for (String asd : rightSide) {
                System.out.println("right side ++++++++++++--------------"+asd);
        }  
            }
        }
        //remove non terminals from rightside
        for (String variable : variables) {
            terminals.remove(variable);
        }
        //terminals after remove non terminals=[a, s]
        System.out.println("terminals after remove non terminals="+terminals);
        System.out.println("Rules221: ");
        for (int i=0 ; i<rules.size() ; i++) {
            System.out.println(i+" : " +rules.get(i));
        }
        

        
        computeFirstSets();
        computeFollowSet();
    }
     private void computeFirstSets() {
         System.out.println("Start firs set ");
        firstSets = new HashMap<>();
        for (String s : variables) {

            HashSet<String> temp = new HashSet<>();
            firstSets.put(s, temp);
           
        }
             
         for (String i : firstSets.keySet()) {
      System.out.println("key : " + i + " value:  " + firstSets.get(i));
      }
         /*
       key : A value:  []
       key : B value:  []
       key : S value:  []
      */ 
        
        while (true) {
            boolean isChanged = false;
            for (String variable : variables) {
                
                HashSet<String> firstSet = new HashSet<>();
               for (Rule rule : rules) {
                  System.out.println("left side is .......1234567"+rule.getLeftSide());
                    
                    // left side contain S' S A and B and variable also condition is true
                    if (rule.getLeftSide().equals(variable)) {
                        //get right side contain Capital S ,A B,a B,s
                       for (String i : rule.getRightSide()) {
                          System.out.println("right side is ......."+i);
                             }
                        HashSet<String> addAll = computeFirst(rule.getRightSide(), 0);
                        firstSet.addAll(addAll);
                        System.out.println("first set is ......."+firstSet);
                    }
                    
                    
                    
                    
                }
                if (!firstSets.get(variable).containsAll(firstSet)) {
                    isChanged = true;System.out.println("is this running or not is this running or not");
                    firstSets.get(variable).addAll(firstSet);
                    for (String i : firstSets.keySet()) {
      System.out.println("key: " + i + " value: " + firstSets.get(i));
      }
                }

            }
            if (!isChanged) {
                break;
            }
        }
     //   firstSets.put("S'", firstSets.get(startVariable));
        for (String i : firstSets.keySet()) {
      System.out.println("key: " + i + " value: " + firstSets.get(i));
      }
    }
     //string contain a B  
     //index is 0
     public HashSet<String> computeFirst(String[] string, int index) {
         for (String element: string) {
            System.out.println(element);
        }
         System.out.println("string length is "+string.length);
        HashSet<String> first = new HashSet<>();
        
        if (index == string.length) {
            
            return first;
        }
       // 1st iteration a is added 
       // 2nd iteration s is added
        if (terminals.contains(string[index])) {
            first.add(string[index]);
             System.out.println("string index 000000000000000000000"+first);
            return first;
        }
       // use this code in 3rd iteration (this is only execute when non terminals come on right side)
       //string index is A
       System.out.println("string index 2"+string[index]);
        if (variables.contains(string[index])) {
            for (String str : firstSets.get(string[index])) {
                System.out.println("first set 33333333333333333333"+str);
                first.add(str);
            }
        }

        if (first.contains("ε")) {
            if (index != string.length - 1) {
                first.addAll(computeFirst(string, index + 1));
                first.remove("ε");
            }
        }
        return first;
    }
    //method for rules
    public ArrayList<Rule> getRules() {
        return rules;
    }
   
    public int findRuleIndex(Rule rule){
        for(int i=0 ; i<rules.size();i++){
            if(rules.get(i).equals(rule)){
                return i;
            }
        }
        return -1;
    }
    //method for get variable (non-terminals)
    public HashSet<String> getVariables() {
        return variables;
    }
//method for start variable
    public String getStartVariable() {
        return startVariable;
    }

   

    private void computeFollowSet() {
         System.out.println("Start follow set ");
        fallowSets = new HashMap<>();
        for (String s : variables) {
            HashSet<String> temp = new HashSet<>();
            fallowSets.put(s, temp);
        }
       
        HashSet<String> start = new HashSet<>();
        start.add("$");
        fallowSets.put("S", start);
       
         for (String i : fallowSets.keySet()) {
      System.out.println("key : " + i + " value:  " + fallowSets.get(i));
      }
         /* after first iteration result will be
         key : A value:  []
         key : B value:  []
         key : S value:  []
         key : S' value:  [$]
         */

        while (true) {
            boolean isChange = false;
            /*  For A
                A comes in first iteration in variable
                */
            /*  For B
                B comes in 2nd iteration in variable
                */
            for (String variable : variables) {
                System.out.println("follow variable is "+variable);
                
                /*       For A
                        1st iteration rule is S'->S
                        2nd iteration rule is S->A B
                        3rd iteration rule is A->a B 
                        4th iteration rule is B->s

                        */
                /*       For B
                        1st iteration rule is S'->S
                        2nd iteration rule is S->A B
                        3rd iteration rule is A->a B 
                        4th iteration rule is B->s

                        */
                for (Rule rule : rules) {
                    System.out.println("follow rule is "+rule);
                    /*  For A
                        1st iteration we get S from rules right side
                        2nd iteration we get AB from rules right side 
                        3rd iteration we get aB from rules right side
                        4th iteration we get s from rules right side
                        */
                    /*  For B
                        1st iteration we get S from rules right side
                        2nd iteration we get AB from rules right side 
                        3rd iteration we get aB from rules right side
                        4th iteration we get s from rules right side
                        */
                    for (int i = 0; i < rule.getRightSide().length; i++) {
                                    /* For A
                            1st iteration if condition is false because variable contains A right now
                            2nd iteration if condition is true AB is store in getright side
                            3rdd iteration if condition is false aB is store in getright side
                            */
                            /* For B
                            1st iteration if condition is false because variable contains B right now
                            2nd iteration if condition is true AB is store in getright side
                            3rdd iteration if condition is false aB is store in getright side
                            */
                        if (rule.getRightSide()[i].equals(variable)) {
                             System.out.println("follow rule right side is follow rule right side is"+rule.getRightSide()[i]+variable);
                             System.out.println("follow rule right side length is"+rule.getRightSide().length);
                             System.out.println("i number = "+i);
                             // For A         condition is false when A is come because i=0 and length is 2-1
                             // For B         condition is true when B is come because i=1 and length is 2-1
                            if (i == rule.getRightSide().length - 1) {
                                for (int j = 0; j <= rule.leftSide.length(); j++) {System.out.println("i number = "+fallowSets.get(rule.leftSide));
                                System.out.println("i number = "+fallowSets.get(variable));
                                }
                                
                                fallowSets.get(variable).addAll(fallowSets.get(rule.leftSide));
                            } else {
                                HashSet<String> first = computeFirst(rule.getRightSide(), i + 1);
                                if (first.contains("ε")) {
                                    first.remove("ε");
                                    first.addAll(fallowSets.get(rule.leftSide));
                                }
                                // condition is true for capital A
                                if (!fallowSets.get(variable).containsAll(first)) {
                                    isChange = true;
                                    System.out.println("i boolean = "+isChange);
                                    fallowSets.get(variable).addAll(first);
                                }
                                
                            }
                        }
                    }
                    for (String i : fallowSets.keySet()) {
      System.out.println("key: " + i + " value: " + fallowSets.get(i));
      }
                }
            }
            if (!isChange) {
                break;
            }
        }
        for (String i : fallowSets.keySet()) {
      System.out.println("key: " + i + " value: " + fallowSets.get(i));
      }
    }

   

    public HashSet<Rule> getRuledByLeftVariable(String variable) {
        HashSet<Rule> variableRules = new HashSet<>();
        for (Rule rule : rules) {
            if (rule.getLeftSide().equals(variable)) {
                variableRules.add(rule);
            }
        }
        return variableRules;
    }

    public boolean isVariable(String s) {
        return variables.contains(s);
    }
//getfirstsets method is used in firstandfollow.java form
    public HashMap<String, HashSet<String>> getFirstSets() {
        return firstSets;
    }
//getfallowsets method is used in firstandfollow.java form
    public HashMap<String, HashSet<String>> getFallowSets() {
        return fallowSets;
    }

    public HashSet<String> getTerminals() {
        return terminals;
    }

   /* @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.rules);
        hash = 37 * hash + Objects.hashCode(this.terminals);
        hash = 37 * hash + Objects.hashCode(this.variables);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Grammar other = (Grammar) obj;
        if (!Objects.equals(this.rules, other.rules)) {
            return false;
        }
        if (!Objects.equals(this.terminals, other.terminals)) {
            return false;
        }
        if (!Objects.equals(this.variables, other.variables)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String str = "";
        for(Rule rule: rules){
            str += rule + "\n";
        }
        return str;
    }
*/
}
