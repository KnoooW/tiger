package lexer;

import java.io.InputStream;

import java.util.*;
 
 

import lexer.Token.*;
 

public class Lexer
{
  String      fname; 	// the input file name to be compiled
  InputStream fstream; 	// input stream for the above file

  Integer     LineNum ;
  Integer	  ColumnNum ;
  
  Map<String, Token.Kind> map  ;
  
  
  public Lexer(String fname, InputStream fstream)
  {
    this.fname 	 = fname;
    this.fstream = fstream;
    
    this.LineNum 	 = 1 ;
    this.ColumnNum   = 1 ;
    
    this.map 		 = InitHashMap() ;
  }
 
  
  private Map<String, Token.Kind> InitHashMap()
  {
	  Map<String, Token.Kind> x = new HashMap<String, Token.Kind>();
	 
	  x.put("boolean", 	Kind.TOKEN_BOOLEAN);
	  x.put("class", 	Kind.TOKEN_CLASS) ;
	  x.put("else", 	Kind.TOKEN_ELSE) ;
	  x.put("extends", 	Kind.TOKEN_EXTENDS) ;
	  x.put("false",	Kind.TOKEN_FALSE) ;
	  x.put("if", 		Kind.TOKEN_IF) ;
	  x.put("int", 		Kind.TOKEN_INT) ;
	  x.put("length", 	Kind.TOKEN_LENGTH) ;
	  x.put("main", 	Kind.TOKEN_MAIN) ;
	  x.put("new", 		Kind.TOKEN_NEW) ;
	  x.put("out", 		Kind.TOKEN_OUT) ;
	  x.put("println", 	Kind.TOKEN_PRINTLN) ;
	  x.put("public", 	Kind.TOKEN_PUBLIC) ;
	  x.put("return", 	Kind.TOKEN_RETURN) ;
	  x.put("static", 	Kind.TOKEN_STATIC) ;
	  x.put("String", 	Kind.TOKEN_STRING) ;
	  x.put("System", 	Kind.TOKEN_SYSTEM) ;
	  x.put("this", 	Kind.TOKEN_THIS) ;
	  x.put("true", 	Kind.TOKEN_TRUE) ;
	  x.put("void", 	Kind.TOKEN_VOID) ;
	  x.put("while", 	Kind.TOKEN_WHILE) ;
	  
	  return x ;
  }
 
 
  // I define this function
  private int getnextchar() throws Exception
  {
	  this.ColumnNum ++ ;
	  return this.fstream.read(); 
  }
 

  // When called, return the next token (refer to the code "Token.java")
  // from the input stream.
  // Return TOKEN_EOF when reaching the end of the input stream.
  private Token nextTokenInternal() throws Exception
  {
    int c = getnextchar();
    
   // if (-1 == c)
      // The value for "lineNum" is now "null",
      // you should modify this to an appropriate
      // line number for the "EOF" token.
    //  return new Token(Kind.TOKEN_EOF, LineNum) ;

    // skip all kinds of "blanks"
    while (' ' == c || '\t' == c || '\n' == c || '/' == c || '\r' == c) 
    {
    	if(c == '\n') 
    	{
    		this.LineNum ++ ;   // Line Number 
    		this.ColumnNum = 1 ;
   		}
    	else if(c == '\t')
    	{
    		this.ColumnNum += 3 ; // '\t' == 4 spaces
    	}
    	else if(c == '/')
    	{
    		c = getnextchar();
    		
    		if(c == '/')
    		{
    			// we got '//', skip comments line
    			while((c = getnextchar()) != '\n') ;
    			this.LineNum ++ ;
    			this.ColumnNum = 1 ;
    		}
    		else if(c == '*')
    		{
    			// here we got "/*"
    			Integer curline = this.LineNum ;
    			
    			// this while check for '*/'
    			while(c != -1)
    			{
	    			c = getnextchar() ;
	
	    			if(c == '*')
	    			{
		    			c = getnextchar() ;
		    	 
		    			if(c == '/')
		    				break ;
	    			}
	    			if(c == '\n') 
	    			{
	    				this.ColumnNum = 1 ;
	    				this.LineNum++ ;
	    			}
    			}
    			if(c == -1)
    			{
    				// cannot find '*/'
	   		    	 System.out.println("[ERROR in lexer]" + "[line:" + curline + 
	    			 					"'/*']" + "[we don't find '*/' after here]")  ;  
	   		    	 return null ;
    			}
    		}
    		else
    		{
    			// here we didn't got '//' or '/*' , Error occur
		    	 System.out.println("[ERROR in lexer]" + "[line:" + LineNum + 
		    			 			" c=\'" + String.valueOf((char)c) + "\']" + 
		    			 			"[we expected '/' or '*' here]")  ;   
				 return null ; 			
    		}
    	}// c == '/'
    	
    	c = getnextchar();
    }
    if (-1 == c)
      return new Token(Kind.TOKEN_EOF, LineNum, ColumnNum);
 
    // Now c Is NOt '\n','\t',' ', EOF and comments are all dropped
    
    switch (c) {
    
	    case '+':
	    	return new Token(Kind.TOKEN_ADD, LineNum, ColumnNum);
	    case '-':
	    	return new Token(Kind.TOKEN_SUB, LineNum, ColumnNum);
	    case '*':
	    	return new Token(Kind.TOKEN_TIMES, LineNum, ColumnNum);
	    case '<':
	    	return new Token(Kind.TOKEN_LT, LineNum, ColumnNum);
	    case ',':
	    	return new Token(Kind.TOKEN_COMMER, LineNum, ColumnNum);
	    case '.':
	    	return new Token(Kind.TOKEN_DOT, LineNum, ColumnNum);
	    case '(':
	    	return new Token(Kind.TOKEN_LPAREN, LineNum, ColumnNum);
	    case ')':
	    	return new Token(Kind.TOKEN_RPAREN, LineNum, ColumnNum);
	    case '[':
	    	return new Token(Kind.TOKEN_LBRACK, LineNum, ColumnNum);
	    case ']':
	    	return new Token(Kind.TOKEN_RBRACK, LineNum, ColumnNum);
	    case '{':
	    	return new Token(Kind.TOKEN_LBRACE, LineNum, ColumnNum);
	    case '}':
	    	return new Token(Kind.TOKEN_RBRACE, LineNum, ColumnNum);
	    case ';':
	    	return new Token(Kind.TOKEN_SEMI, LineNum, ColumnNum);
	    case '!':
	    	return new Token(Kind.TOKEN_NOT, LineNum, ColumnNum);
	    case '=':
	    	return new Token(Kind.TOKEN_ASSIGN, LineNum, ColumnNum);
	    case '&':
	    	  this.fstream.mark(1) ;  // remember position for back one character
		      c = getnextchar();
		      if(c != '&')
		      {
		    	  this.fstream.reset() ; // back one into stream
		    	  this.ColumnNum--;
		    	  System.out.println("[ERROR in lexer]" + "[line:" + LineNum + 
 			  			 			 " c=\'" + String.valueOf((char)c) + "\']" +
		    			             "[we expected '&' here]")  ;  
		    	  return null ;
		      }
		      return new Token(Kind.TOKEN_AND, LineNum, ColumnNum);
	          
	    default:
	      // Lab 1, exercise 2: supply missing code to
	      // lex other kinds of tokens.
	      // Hint: think carefully about the basic
	      // data structure and algorithms. The code
	      // is not that much and may be less than 50 lines. If you
	      // find you are writing a lot of code, you
	      // are on the wrong way.
	    	
	    
	      if(c <= '9' && c >= '0')
	      {
	    	  // Here is for Number check
	    	  String Num = new String() ;
	    	  
	    	  do{
	    		  Num = Num.concat(String.valueOf((char)c)) ;
	    		  this.fstream.mark(1) ;
	    		  c = getnextchar() ;
	    	  } while(c <= '9' && c >= '0') ;
	    	 this.fstream.reset() ;
	    	 this.ColumnNum--;
	    	 return new Token(Kind.TOKEN_NUM, LineNum, ColumnNum, Num) ;
	      }
	      else if((c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A'))
	      {
	    	  // Here is for ID And Reserved words Check
	    	  String IdName = new String() ;
	    	  
	    	  do {
	    		  IdName = IdName.concat(String.valueOf((char)c)) ; // =

	    		  this.fstream.mark(1) ;
	    		  c = getnextchar() ;
	    	  } while((c <= 'z' && c >= 'a') || 
	    			  (c <= 'Z' && c >= 'A') ||
	    			  (c <= '9' && c >= '0') || 
	    			   c == '_') ;
 
	    	 this.fstream.reset() ;
	    	 this.ColumnNum--;
	    	 // map is HashTable , f(name) = Kind
	    	 Kind  tokenKind = map.get(IdName) ;
	    	 if(tokenKind == null)
	    	 {
	    		 // Not a reserved word
	    		 return new Token(Kind.TOKEN_ID, LineNum, ColumnNum, IdName) ;
	    	 }
	    	 else
	    	 {
	    		 // reserved word/key word found
	    		 return new Token(tokenKind, LineNum, ColumnNum, null) ;
	    	 }
	      }
	      else
	      {
	    	  // invalid character
	    	  System.out.println("[ERROR in lexer]" + "[line:" + LineNum + 
			  			 		 " c=\'" + String.valueOf((char)c) + "\']" + 
	    			  			 "[invalid Character here]")  ;   
	    	  return null;
	      }
    }
  }

  
  public Token nextToken()
  {
    Token t = null;

    try {
      t = this.nextTokenInternal();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    if (control.Control.lex && t != null)
      System.out.println(t.toString());
    return t;
  }
}
