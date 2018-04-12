package com.derek.rbml;

import com.derek.rbml.RelationshipRole;
import com.derek.rbml.StructuralRole;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SPS {

    //classifier roles are declared abstractly (as a classifier) but the actual role will be either a UMLInterface or UMLClass
    //I can use instanceof to further distinguish this, but realistically I shouldn't need to type check.
    private List<StructuralRole> classifierRoles;
    private List<RelationshipRole> associationRoles;
    private List<RelationshipRole> generalizationRoles;
    private List<RelationshipRole> dependencyRoles;
    private List<RelationshipRole> implementationRoles;

    public SPS(String descriptorFileName) {
        classifierRoles = new ArrayList<>();
        associationRoles= new ArrayList<>();
        generalizationRoles = new ArrayList<>();
        dependencyRoles = new ArrayList<>();
        implementationRoles = new ArrayList<>();
        parseRoles(descriptorFileName);
        for (StructuralRole strRole : classifierRoles){
            strRole.printSummary();
        }
    }

    private void parseRoles(String descriptorFileName){
        try{
            Scanner in = new Scanner(new File(descriptorFileName));
            while (in.hasNext()){
                String line = in.nextLine();
                parseRoleType(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseRoleType(String lineDescription){
        String type = lineDescription.split("\\ ")[0];
        switch (type){
            //type info
            case "Classifier":
            case "Class":
                classifierRoles.add(new StructuralRole(lineDescription));
                break;
            case "Association":
                associationRoles.add(new RelationshipRole(lineDescription));
                break;
            case "Generalization":
                generalizationRoles.add(new RelationshipRole(lineDescription));
                break;
            case "Dependency":
                dependencyRoles.add(new RelationshipRole(lineDescription));
                break;
            case "Implementation":
                implementationRoles.add(new RelationshipRole(lineDescription));
                break;
            default:
                System.out.println("did not fine an rbml role for the input. Debug parseRoleType. But likely the input was messed up. (sps text file)");
                System.exit(0);
        }
    }




}