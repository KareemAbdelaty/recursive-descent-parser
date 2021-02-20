//kareem abdelaty
//Cpsc 331
//Assignment 3
//resources 
//Tutorial 6 by Fatmeh P.
//HW3 by Hooman K.
//https://www.engr.mun.ca/~theo/Misc/exp_parsing.htm
//https://www.geeksforgeeks.org/tree-traversals-inorder-preorder-and-postorder/
package hw3.student;
import java.text.ParseException;
import hw3.student.ExpressionTreeInterface;


public class ExpressionTree implements ExpressionTreeInterface {
	
	private ExpressionTreeNode root;
	/*
	 * an char array to hold the input as it is being tokenized
	 * an int the acts as a pointer to the current char
	 * an int the acts as a maxlimit for the pointer to avoid an index out of bounds error
	 */
    String[] arr;
    int next;
    
    
	/**
	 * Constructor to build an empty parse tree.
	 */

	public ExpressionTree() {
	  root = null;
	  //intialize the pointer to zero
	  next =0;
	}

  /**
   * Build a parse tree from an expression.
   * @param line String containing the expression
   *
   * @throws ParseException If an error occurs during parsing. 
   */
	
	
	public void parse(String line) throws ParseException {
		//remove any all spaces from the input becuase it makes the input easier to parse
		arr = line.trim().split("\\s+");
		next = 0;
		//set the max length
		if (arr.length > 0) 
		{		
			try {
				root = E();
				
			}	catch (ArrayIndexOutOfBoundsException aie) 
				{	
					//if the pointer becomes larger than the array without termnainting then the input is incorrect
					throw new ParseException("The input is invalid.", next);
				}
			
				if (next < arr.length) {
					//if we terminate without reaching the end of the array then the input was incorrect
					throw new ParseException("Incorrect input", next);
				}		
			}		
		}
  
  /**
   * Evaluate the expression tree and return the integer result. An 
   * empty tree returns 0.
   */

	  public int evaluate() {
		  	//calculate the result starting at the root
		    return calculate(root);
		  }
		  
		  private int calculate(ExpressionTreeNode r) {
			  	//if the root is null then the tree is empty and return zero
			  	if(r !=null) {
			  		//if the root is not null check if the root contains an operation if it contains a number and recursivly slove sub roots
					 if(!(isOperator(r.el.charAt(0)))) {
						 return Integer.parseInt(r.el);
					 }
					int a = calculate(r.left);
					int b = calculate(r.right);
				    if (r.el.equals("+")) {
				        return a + b;
				     } 
				     if (r.el.equals("-")) {
				         return a - b;
				      } 
				     if (r.el.equals("*")) {
				         return a * b;
				      } 
				     if (r.el.equals("/")) {
				        return a / b;
				      } 
				    			 
				  }
			     return 0;
		  		  
} 
		  //function that checks if a char is an operator
		  private boolean isOperator(char c) { 
		      if (c == '+' || c == '-'
		              || c == '*' || c == '/') { 
		          return true; 
		      } 
		      return false; 
		  }		  
  /**
   * Simplifies the tree to a specified height h (h >= 0). After simplification, the tree 
   * has a height of h. Any subtree rooted at a height of h is replaced by a leaf node 
   * containing the evaluated result of that subtree.  
   * If the height of the tree is already less than the specified height, the tree is unaffected. 
   * 
   * @param h The height to simplify the tree to.
   */
  
  //function the checks both sides of the tree and simplifies the by calculating the the sub trees till the required height is reached
  public void simplify(int h, ExpressionTreeNode r) {
	  //if the root is null then the tree is empty and cant be simplfied
	  if (r!= null) 
	  {	 
		  //recursivly simplify both sides
		  if (h > 0) 
		  {
		  simplify(h-1,r.left);
		  simplify(h-1,r.right);
		  }
	     else  if (h == 0)
		 {
	    //when you reach the required height calculate the value of the node and set its children to null
		  int result = calculate(r);
		  r.el = String.valueOf(result);
		  r.left = null;
		  r.right = null;
		 }
	  }
  }
  
  
  public void simplify( int h ) {
		  simplify(h,root);
		  

  }
	
  /**
   * Returns a parentheses-free postfix representation of the expression. Tokens 
   * are separated by whitespace. An empty tree returns a zero length string.
   */
	public String postfix() {
		return postfix(root,"");
	}
	private String postfix(ExpressionTreeNode r,String current) {
		//if the root is null then this a leaf node so return it string
		if(r== null) {
			return current;
		}
		//for non leaf nodes recursivly go through till you reach a leaf node
        current = postfix(r.left, current);
        current =  postfix(r.right, current);
        current = current.concat(r.el + " ");
        return current;
		
		
	}
  
  /**
   * Returns a parentheses-free prefix representation of the expression. Tokens are 
   * separated by whitespace. An empty tree returns a zero length string.
   */

    
	public String prefix() {
		return prefix(root,"");
	}
	private String prefix(ExpressionTreeNode r, String current) {
		if(r == null) {
			return current;
		}
        current = current.concat(r.el + " ");
        current = prefix(r.left, current);
        current =  prefix(r.right, current);
        return current;
		
	}
	
	
	//Expression Tree Node for expression
	private ExpressionTreeNode E() throws ParseException {
		//left and right nodes
        ExpressionTreeNode node;
        //applying the Substiution rule E-> T
        node = T();
        String currentchar;
       //checking for the optional part {("+"||"-")T} 
        while (true) {
        	//while we are not at the end of the array continue
            if( this.next < this.arr.length ) 
            {
            	//read the next character
            	currentchar = this.arr[this.next];
                if(match("+","-")) 
                {
                    node = new ExpressionTreeNode(currentchar, node, T());
                } 
                else 
                {
                    return node;
                }
            } 
            else 
            {
                return node;
            }
        }
    }
	    
		private ExpressionTreeNode T() throws ParseException {
	        ExpressionTreeNode node;
	        node = F();
	        while(true) {
	            if (this.next < this.arr.length) 
	            {
	                String currentchar = this.arr[this.next];
	               if(match("*","/")) {
	                    node = new ExpressionTreeNode(currentchar, node, F());
	                } 
	                else 
	                {
	                    return node;
	                }
	            } 
	            else 
	            {
	                return node;
	            }
	        }
	    }
		private ExpressionTreeNode F() throws ParseException {
	        ExpressionTreeNode node;
	        if(match("(")) {
	        node = E();
	        if(!match(")")) { 
	    		throw new ParseException("unmatched paranthsis",0);
	        	}
	        }
	        
	        else 
	        {
	    	try {
	            	Integer.parseInt( this.arr[this.next] );
	            	node = new ExpressionTreeNode( this.arr[this.next++] );
	        	} 
	    			catch(NumberFormatException e) 
	    			{
	    				throw new ParseException( "non-numbers found", 0 );
	    			}
	    }
	        return node;

	    }
		private boolean match(String t) throws ParseException{
			if(arr[this.next].equals(t)) {
					this.next++;
				return true;
			}
			else
				return false;
			
		}
		private boolean match(String t,String x) throws ParseException{
			if((this.arr[this.next].equals(t))||(this.arr[this.next].equals(x))) {
					this.next++;
				return true;
			}
			else
				return false;

			
		}		
	}
