package com.derek.rbml;

import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * class reponsible for holding
 */
@Getter
public class StructuralRole extends Role{


    protected String type;
    //how many instances of class x we can have
    private Pair<Integer, Integer> multiplicity;
    private List<RoleAttribute> attributes;
    private List<RoleOperation> operations;

    public StructuralRole(String lineDescription){
        super(lineDescription);
    }

    /***
     * input: Classifier |Receiver,1..*[]{|Action():void,1..*}
     * @param lineDescription
     */
    @Override
    protected void parseLineDescription(String lineDescription) {
        //https://stackoverflow.com/questions/237061/using-regular-expressions-to-extract-a-value-in-java
        Pattern p = Pattern.compile("([a-zA-Z]+)\\s(\\|[a-zA-Z]+),(\\d\\.\\.[\\d|\\*])(\\[.*\\])(\\{.*\\})");
        Matcher m = p.matcher(lineDescription);

        if (m.find()) {
            //group counts can include index length of array.
            type = m.group(1);
            name = m.group(2);
            multiplicity = findMultiplicity(m.group(3));
            buildAttribues(m.group(4));
            buildOperations(m.group(5));
        }
    }

    /***
     * input will be [|state:|Receiver,1..1], and potentially [|state:|Receiver,1..1;|2:|CLASSIFIER,1..1]
     * @param s
     */
    private void buildAttribues(String s){
        attributes = new ArrayList<>();
        if (s.equals("[]")){
            //no attributes
            return;
        }else{
            if (s.contains(";")){
                //contains more than 1 attribute.
                String[] splitter = s.split(";");
                for (int i = 0; i < splitter.length; i++){
                    attributes.add(new RoleAttribute(splitter[i]));
                }
            }else{
                attributes.add(new RoleAttribute(s));
            }
        }
    }

    private void buildOperations(String s){
        operations = new ArrayList<>();
        if (s.equals("{}")){
            //no operations
            return;
        }else{
            if (s.contains(";")){
                String[] splitter = s.split(";");
                for (int i = 0; i < splitter.length; i++){
                    operations.add(new RoleOperation(splitter[i]));
                }
            }else{
                operations.add(new RoleOperation(s));
            }
        }
    }

    @Override
    protected void printSummary() {
        System.out.println("Structural Aspect");
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Multiplicities: " + multiplicity.getKey() + ".." + multiplicity.getValue());
        for (RoleAttribute attr : attributes){
            attr.printSummary();
        }
        for (RoleOperation op : operations){
            op.printSummary();
        }
    }
}