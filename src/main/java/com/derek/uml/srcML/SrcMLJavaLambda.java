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
import lombok.Getter;
import org.w3c.dom.Element;

@Getter
public class SrcMLJavaLambda extends SrcMLNode{
    //might not need to use this class.
    private SrcMLParameterList parameterList;
    private SrcMLBlock block;
    private CallTreeNode<SrcMLNode> callTree;

    public SrcMLJavaLambda(Element lambdaEle) {
        super(lambdaEle);
    }

    protected void parse() {
        parameterList = parseParameterList();
        block = parseBlock();

        callTree = new CallTreeNode<>(this, "lambda");
        if (block != null) {
            block.fillCallTree(callTree);
        }
    }

    public String toString(){
        return "{lambda}";
    }

}
