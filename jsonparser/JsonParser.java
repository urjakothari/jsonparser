import java.io.*;
import java.lang.*;
import java.util.Scanner;
class JsonParser
{
	StringBuilder jsonString;
	int i=0;
	JsonParser(StringBuilder jsonString)
	{
		this.jsonString=jsonString;
	}
	void validate()
	{
		try
		{
			//call object() function for validation
			Boolean var=object();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	Boolean object() throws Exception
	{
		//It checks whether string contains only {} or not
		if(jsonString.toString().equals("{}\n"))
			return true;

		if(match('{'))		
		{
			// skip new linescheckBlankLine();
			// It checks for example like { \n\n\n } 
			if(match('}'))
				return true;

			members();
			checkBlankLine();
			if(match('}'))
			{
				return true;
			}
			else
			{
				throw new Exception("SyntaxError : Expected ',' or '}' at end  of Json Data ");
			}
		}
		else
		{
			throw new Exception("SyntaxError : Expected starting '{' of json Data");
		}
	}
	void members() throws Exception
	{
		checkBlankLine();

		//It will validate all key:value pair

		pair();
		if(match(','))
		{
			checkBlankLine();
			//it will call members for cheking more pairs in one object
			members();
		}
	}
	void pair() throws Exception
	{
		//String checking for key
		stringCheck();
		if(match(':'))
		{
			checkBlankLine();
			//Validation for value
			value();
		}
		else
			throw new Exception("SyntaxError : expected ':' after property name");
	}
	void stringCheck() throws Exception
	{
		checkBlankLine();		
		if(match('"'))
		{
			chars();
		}
		else
			throw new Exception("SyntaxError : Unexpected character of the Json Data");
		if(match('"'))
		{
			return;
		}
		if(match('\n'))
		{
			throw new Exception("SyntaxError : Bad Control Character in String");
		}
	}
	void chars()
	{
		checkBlankLine();
		char c;
		c=findChar();
		while(c != '"' && c != '\n')
		{ 
			c=findChar();
		}
		i--;
		return;
	}
	void numberCheck()
	{
		numbers();
		return;
	}
	void numbers()
	{
		checkBlankLine();
		char c=findChar();
		while(c >= '1' && c <= '9')
		{
			c=findChar();
		}
		i--;
		return;
	}
	void array() throws Exception
	{
		checkBlankLine();
		if(match('['))
		{
			checkBlankLine();
			if(match(']'))
				return;
			else
				elements();
		}
		checkBlankLine();
		if(match(']'))
		{
			return;
		}
		else
			throw new Exception("Syntax Error :  Missing ']' in json data");
		
	}
	void elements() throws Exception
	{
		value();
		if(match(','))
		{
			checkBlankLine();
			elements();
		}
	}				
	void value() throws Exception
	{
		if(isString())
		{
			stringCheck();
			return;
		}
		else if(isNumber())
		{
			numberCheck();
			return;
		}
		else if(isObject())
		{
			object();
			return;
		}
		else if(isArray())
		{
			array();
			return;
		}
		else
		{
			if(booleanCheck())
				return;
			else
				throw new Exception("SyntaxError : Expected Property value before ',' or '}'");
		}
	}
	Boolean isString()
	{
		checkBlankLine();
		if(match('"'))
		{
			i--;		
			return true;
		}
		else
			return false;
	}
	Boolean isNumber()
	{
		checkBlankLine();
		char c;
		for(c='1';c <= '9'; c++)
		{
			if(match(c))
			{
				i--;
				break;
			}
		}
		if(c <= '9')
			return true;
		else
			return false;
	}
	Boolean isObject()
	{
		checkBlankLine();
		if(match('{'))
		{
			i--;
			return true;
		}
		else
			return false;
	}
	Boolean isArray()
	{
		checkBlankLine();
		char c=findChar();
		i--;
		if(match('['))
		{	
			i--;
			return true;
		}
		else
			return false;
	}
	Boolean booleanCheck()
	{
		checkBlankLine();
		String s="";
		char c=findChar();
		while(c != '\n' && c != ',' && c != '}')
		{
			s+=c;
			c=findChar();
		}
		i--;
		if(s.equals("true"))
			return true;
		else if(s.equals("false"))
			return true;
		else if(s.equals("null"))
			return true;
		else
			return false;
	}

	
	//It will return character at i th position
	char findChar()
	{
		char c=jsonString.charAt(i);
		i++;
		return c;
		
	}

	// IT will match single character in String return true if match else return false
	Boolean match(char c)
	{
		
		while(jsonString.charAt(i) == ' '  || jsonString.charAt(i) == '\t')
		{	
			i++;
		}
		if(jsonString.charAt(i) == c)
		{
			i++;
			return true;
		}
		else
			return false;

	}
	void checkBlankLine()
	{
		while(match('\n'));
	}
	public static void main(String []args)
	{
		StringBuilder jsonString;
		jsonString=new StringBuilder(); 
		Scanner input = new Scanner(System.in);
       		while (input.hasNext()) 
		{
			String temp=input.nextLine();
			jsonString.append(temp);
			jsonString.append("\n");
	        }
		JsonParser jsonParser=new JsonParser(jsonString);
		jsonParser.validate();
	}
	
}
