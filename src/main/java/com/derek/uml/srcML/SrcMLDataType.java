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
package com.derek.uml.srcML;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLDataType extends SrcMLNode{
    private List<String> specifiers;
    private List<String> modifiers;
    //not sure if I'll need argumentlist, but at least it is there.
    private SrcMLArgumentList argumentList;
    @Setter
    private SrcMLName name;
    private String optionalAttribute;

    public SrcMLDataType(Element typeEle){
        super(typeEle);
    }

    protected void parse(){
        specifiers = parseSpecifiers();
        modifiers = parseModifiers();
        argumentList = parseArgumentList();
        name = parseName();
        if (name == null){
            //happens when vars are declared using commas (int r,g,b)
            optionalAttribute = element.getAttribute("ref");
        }else{
            //init it so it isn't null
            optionalAttribute = "";
        }
    }
    public String getName(){
        if (optionalAttribute.equals("prev")){
            //declared at a previous step, so I need to delay setting (and returning) name until late.r
            return "prevDecl";
        }
        return name.getName();
    }

    public SrcMLName getNameObj(){
        return name;
    }
}
