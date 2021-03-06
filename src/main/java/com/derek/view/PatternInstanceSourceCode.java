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
package com.derek.view;

import com.derek.Main;
import com.derek.model.patterns.PatternInstance;
import com.google.common.graph.MutableGraph;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class PatternInstanceSourceCode {

    private JFrame frame;
    private PatternInstance patternInstance;
    private MutableGraph<PatternInstance> patternEvolution;

    public PatternInstanceSourceCode(PatternInstance pi, MutableGraph<PatternInstance> patternEvolution){
        this.patternInstance = pi;
        this.patternEvolution = patternEvolution;
        frame = new JFrame("Source code for pattern instance " + pi);
        frame.setSize(800, 800);
        frame.setLayout(new BorderLayout());

        frame.add(sourceCodePane());

        frame.setVisible(true);
    }

    public JTabbedPane sourceCodePane(){
        JTabbedPane tabbedPane = new JTabbedPane();


        for (PatternInstance pi : patternEvolution.nodes()){
            tabbedPane.addTab(pi.getSoftwareVersion().getVersionNum() + "", getPanelSourceCode(pi));
        }


        return tabbedPane;
    }
    private Component getPanelSourceCode(PatternInstance pi){
        JPanel pane = new JPanel();

        for (Pair<String, String> role : pi.getListOfPatternRoles()){
            pane.add(getRoleAsJPanel(role, pi.getSoftwareVersion().getVersionNum()));
        }


        return pane;
    }

    private JPanel getRoleAsJPanel(Pair<String, String> role, int versionNum ){
        JPanel toRet = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JButton roleButton = new JButton(role.getKey());

        roleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame codeFrame = new JFrame();
                codeFrame.add(new JTextField(getSourceCodeFromFile(role.getValue(), versionNum)));
            }
        });
        toRet.add(roleButton, c);

        return toRet;
    }

    private String getSourceCodeFromFile(String roleToFind, int versionNum){
        try {
            String sourceCode = "";

            String optionalMethodName = "";
            //at this point and as far as I can tell there are 3 keywords:
            //$ denotes a method under a class
            //:: denotes a concrete class, or a class that extends an abstract class
            //: denotes the return object and/or return value of the concrete class from above. Idk if : can appears if :: does not appear.
            //Beware because all three above can exist in a file. And multiple times for each. I need to watch out for this.
            if (roleToFind.contains("$")){
                //role exists within a method, not a class. So I need to strip it out
                roleToFind = roleToFind.split("$")[0];
                optionalMethodName = roleToFind.split("$")[1];
            }

            File f = getFileDir(roleToFind, versionNum);

            BufferedReader bf = new BufferedReader(new FileReader(f));

            String line;
            while ((line = bf.readLine()) != null){
                System.out.println(line);
            }

            return sourceCode;

        }catch (Exception e){
            e.printStackTrace();
        }
        return "file not corrrectly parsed";
    }

    private File getFileDir(String getRoleToFind, int ver){
        try {
            File f;
            String stringFileBuilder = "";

            stringFileBuilder = Main.workingDirectory + ver + Main.interVersionKey;
            stringFileBuilder += (ver + ".0/");
            stringFileBuilder += Main.interProjectKey;

            getRoleToFind = getRoleToFind.replace('.', '/');
            stringFileBuilder += getRoleToFind;
            stringFileBuilder += Main.projectLanguage;

            f = new File(stringFileBuilder);
            return f;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
