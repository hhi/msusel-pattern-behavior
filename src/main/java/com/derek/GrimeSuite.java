package com.derek;

import com.derek.rbml.IPS;
import com.derek.rbml.RBMLMapping;
import com.derek.rbml.SPS;
import com.derek.uml.Relationship;
import com.derek.uml.RelationshipType;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class GrimeSuite {

    private PatternMapper patternMapper;
    private List<RBMLMapping> rbmlStructuralMappings;
    private SPS sps;
    private List<Pair<UMLOperation, BehaviorConformance>> rbmlBehavioralMappings;
    private IPS ips;

    //count of behavioral repetition grime
    private int repetitionGrimeCount = 0;

    //count of improper order of sequences grime
    private int improperOrderGrimeCount = 0;

    //total behavioral grime.
    private int totalBehavioralGrimeCount = 0;

    //structural class grime counts
    private int dipgGrimeCount = 0;
    private int disgGrimeCount = 0;
    private int depgGrimeCount = 0;
    private int desgGrimeCount = 0;
    private int iipgGrimeCount = 0;
    private int iisgGrimeCount = 0;
    private int iepgGrimeCount = 0;
    private int iesgGrimeCount = 0;

    //structural modular grime instances (persistent grime). When I want to calculate I will just call this.size();
    private List<Relationship> piGrimeInstances;
    private List<Relationship> peaGrimeInstances;
    private List<Relationship> peeGrimeInstances;

    //structural modular grime instances (temporary grime). When I want to calculate I will just call this.size();
    private List<Relationship> tiGrimeInstances;
    private List<Relationship> teaGrimeInstances;
    private List<Relationship> teeGrimeInstances;


    public GrimeSuite(PatternMapper patternMapper, List<RBMLMapping> rbmlStructuralMappings, SPS sps, List<Pair<UMLOperation, BehaviorConformance>> rbmlBehavioralMappings, IPS ips) {
        this.patternMapper = patternMapper;
        this.rbmlStructuralMappings = rbmlStructuralMappings;
        this.sps = sps;
        this.rbmlBehavioralMappings = rbmlBehavioralMappings;
        this.ips = ips;
        calculateModularGrime();
    }

    private void calculateModularGrime(){
        //class variable initializations
        piGrimeInstances = new ArrayList<>();
        peaGrimeInstances = new ArrayList<>();
        peeGrimeInstances = new ArrayList<>();
        tiGrimeInstances = new ArrayList<>();
        teaGrimeInstances = new ArrayList<>();
        teeGrimeInstances = new ArrayList<>();
        findModularGrime(piGrimeInstances, peaGrimeInstances, peeGrimeInstances, RelationshipType.ASSOCIATION);
        findModularGrime(tiGrimeInstances, teaGrimeInstances, teeGrimeInstances, RelationshipType.DEPENDENCY);
    }

    private void findModularGrime(List<Relationship> internal, List<Relationship> afferent, List<Relationship> efferent, RelationshipType relationshipType){
        List<Relationship> validRBMLMappings = new ArrayList<>();

        for (RBMLMapping structuralMapping : rbmlStructuralMappings){
            Relationship relationship = structuralMapping.getRelationshipArtifact();
            if (relationship != null){
                //have a valid relationship
                validRBMLMappings.add(relationship);
            }
        }

        for (Relationship patternRelationship : patternMapper.getUniqueRelationshipsFromPatternClassifiers(relationshipType)){
            //3 cases - relationship is between pattern classes (could be internal grime) (could be external and efferent) (could be external and afferent)
            //pi first
            boolean relationshipExistsInRBML = false;
            for (Relationship validRBMLMapping : validRBMLMappings){
                if (patternRelationship.equals(validRBMLMapping)){
                    relationshipExistsInRBML = true;
                }
            }
            if (!relationshipExistsInRBML){
                //logic for the 3 cases starts here.
                if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternRelationship.getFrom())){
                    if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternRelationship.getTo())){
                        //pi.
                        internal.add(patternRelationship);
                        System.out.println("Added internal grime: " + patternRelationship.getFrom().getName() + " to " + patternRelationship.getTo().getName() + " as " + relationshipType);
                    } else {
                        //relationship points from pattern member -> non-pattern member. (efferent)
                        efferent.add(patternRelationship);
                        System.out.println("Added efferent grime: " + patternRelationship.getFrom().getName() + "  to " + patternRelationship.getTo().getName() + " as " + relationshipType);
                    }
                } else {
                    //relationship points from non-pattern member -> pattern member (afferent)
                    afferent.add(patternRelationship);
                    System.out.println("Added afferent grime: " + patternRelationship.getFrom().getName() + "  to " + patternRelationship.getTo().getName() + " as " + relationshipType);
                }
            }
        }
    }
}
