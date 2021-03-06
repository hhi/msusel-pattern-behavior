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

import com.derek.uml.CallTreeNode;
import com.google.common.graph.MutableGraph;
import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLInit extends SrcMLNode{
    //there are a few different types of init, but they generally have the form: (decl|expr)(","(decl|expr))* or some degree
    private List<SrcMLDecl> declarations;
    private List<SrcMLExpression> expressions;
    // i dont; think i need a call tree with init. i think its implied. I do think I need fillcallTree though.
    //private CallTreeNode<SrcMLNode> callTree;

    public SrcMLInit(Element initEle) {
        super(initEle);
    }
    protected void parse(){
        declarations = parseDecls();
        for (int i = 0; i < declarations.size(); i++){
            SrcMLDecl decl  = declarations.get(i);
            if (decl.getType().getName().equals("prevDecl")){
                //comma was used to delininate, so need to set to previous type name.
                decl.getType().setName(declarations.get(i-1).getType().getNameObj());
            }
            decl.buildCallTree();
        }
        expressions = parseExpressions();
    }

    public void fillCallTree(CallTreeNode<SrcMLNode> callTree){
        for (SrcMLExpression srcMLExpression : expressions){
            buildCallTree(callTree, srcMLExpression);
        }
        for (SrcMLDecl srcMLDecl : declarations){
            callTree.addChild(srcMLDecl.getCallTree());
        }
    }

    public String toString(){
        return "new";
    }

}
