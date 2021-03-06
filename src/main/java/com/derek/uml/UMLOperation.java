/**
 * MIT License
 *
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.derek.uml;

import org.apache.commons.lang3.tuple.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UMLOperation {

    private String name;
    //list of pairs, where each pair corresponds to a datatype and a name.
    private List<Pair<String,String>> stringParameters;

    //list of parameters referencing actual uml classifier objects.
    //at the time of writing this code I am not parsing ALL umlclassifiers, the ones not being parsed are specifically third party libs
    //and generics. Because of this there may be null elements in teh list.
    @Setter
    private List<UMLClassifier> parameters;

    //datatype of return value, empty string if void., "null" as string if constructor. I mikght need to change this in the future though.
    private String stringReturnDataType;
    private Visibility visibility;
    private boolean isStatic;

    @Setter
    private CallTreeNode<String> callTreeString;

    //type will be set after the first passthrough.
    @Setter
    private UMLClassifier type;

    //names of the variables that are used in this method.
    private List<String> variableUsages;

    //names of the variables thar are declared in this method.
    private List<String> variableDeclarations;

    public UMLOperation(String name, List<Pair<String, String>> stringParameters, String stringReturnDataType, Visibility visibility) {
        this.name = name;
        this.stringParameters = stringParameters;
        this.stringReturnDataType = stringReturnDataType;
        this.visibility = visibility;
    }

    /***
     * Constructor without method visibility
     * @param name function name
     * @param stringParameters list of pairs of strings for function's params
     * @param stringReturnDataType function return data type
     */
    public UMLOperation(String name, List<Pair<String, String>> stringParameters, String stringReturnDataType) {
        this.name = name;
        this.stringParameters = stringParameters;
        this.stringReturnDataType = stringReturnDataType;
    }

    public String buildParamsForPlantUMLDiagram(){
        StringBuilder s = new StringBuilder();
        //we have params
        for (int i = 0; i < stringParameters.size(); i++){
            Pair<String, String> param = stringParameters.get(i);
            if (i == stringParameters.size() - 1){
                if (param.getKey() == ""){
                    //no params - this will happen during the first iteration of the loop and only if there are no params.
                    return "";
                }else {
                    //last param in param list
                    s.append(param.getKey() + " " + param.getValue());
                }
            }else{
                //defualt case in a sense; multiple params and we are not on the last one
                s.append(param.getKey() + " " + param.getValue() + ", ");
            }
        }
        return s.toString();
    }

    private void findVariableUsages(){
        variableUsages = new ArrayList<>();
        for (CallTreeNode<String> callTreeNode : this.getCallTreeString().convertMeToOrderedList()){
            if (callTreeNode.isCall()){
                variableUsages.add(callTreeNode.parseVarNameFromCall());
                if (this.getName().equals("update")){
                    this.callTreeString.printTree();
                }
            }
        }
    }

    /***
     * method that finds all variable declarations (as strings, not UMLClassifiers) from the entire call tree under this method.
     */
    private void findVariableDeclarations(){
        variableDeclarations = new ArrayList<>();
        for (CallTreeNode<String> callTreeNode : this.getCallTreeString().convertMeToOrderedList()){
            if (callTreeNode.isDecl()){
                variableDeclarations.add(callTreeNode.parseVarNameFromCall());
            }
        }
    }

    public List<String> getVariableDeclarations(){
        if (variableDeclarations == null){
            variableDeclarations = new ArrayList<>();
            findVariableDeclarations();
        }
        return variableDeclarations;
    }

    public List<String> getVariableUsages(){
        if (variableUsages == null){
            variableUsages = new ArrayList<>();
            findVariableUsages();
        }
        return variableUsages;
    }
}
