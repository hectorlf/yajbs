
/* 
 * Copyright (c) 2005 Michael Eddington
 * Copyright (c) 2004 IOActive Inc. 
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy  
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights  
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  
 * copies of the Software, and to permit persons to whom the Software is  
 * furnished to do so, subject to the following conditions: 
 * 
 * The above copyright notice and this permission notice shall be included in  
 * all copies or substantial portions of the Software. 
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,  
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER  
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE. 
 * 
 * Authors:
 *   Michael Eddington (meddington@gmail.com)
 *
 * $Id: Reform.java,v 1.2 2006/11/05 10:13:39 meddingt Exp $
 */

package org.owasp.reform;

public class Reform
{
	public static String HtmlEncode(String str)
	{
		return HtmlEncode(str, null);
	}
	public static String HtmlEncode(String str, String def)
	{
		if(str == null || str.length() == 0)
		{
			str = (def == null ? "" : def);
		}
		
		int len = str.length();
		StringBuffer out = new StringBuffer((int)(len*1.5));
		
		// Allow: a-z A-Z 0-9 SPACE , .
		// Allow (dec): 97-122 65-90 48-57 32 44 46
		
		for(int cnt = 0; cnt < len; cnt++)
		{
			char c = str.charAt(cnt);
			if( (c >= 97 && c <= 122) ||
				(c >= 65 && c <= 90 ) ||
				(c >= 48 && c <= 57 ) ||
				c == 32 || c == 44 || c == 46 )
			{
				out.append(c);
			}
			else
			{
				out.append("&#").append((int)c).append(';');
			}
		}
		
		return out.toString();
	}
	
	public static String HtmlAttributeEncode(String str)
	{
		return HtmlAttributeEncode(str, null);
	}
	public static String HtmlAttributeEncode(String str, String def)
	{
		if(str == null || str.length() == 0)
		{
			str = (def == null ? "" : def);
		}
		
		int len = str.length();
		StringBuffer out = new StringBuffer((int)(len*1.5));
		
		// Allow: a-z A-Z 0-9
		// Allow (dec): 97-122 65-90 48-57
		
		for(int cnt = 0; cnt < len; cnt++)
		{
			char c = str.charAt(cnt);
			if( (c >= 97 && c <= 122) ||
				(c >= 65 && c <= 90 ) ||
				(c >= 48 && c <= 57 ) )
			{
				out.append(c);
			}
			else
			{
				out.append("&#").append((int)c).append(';');
			}
		}
		
		return out.toString();
	}
	
	public static String XmlEncode(String str)
	{
		return HtmlEncode(str, null);
	}
	public static String XmlEncode(String str, String def)
	{
		return HtmlEncode(str, def);
	}
	
	public static String XmlAttributeEncode(String str)
	{
		return HtmlAttributeEncode(str, null);
	}
	public static String XmlAttributeEncode(String str, String def)
	{
		return HtmlAttributeEncode(str, def);
	}
	
	public static String JsString(String str)
	{
		return JsString(str, null);
	}
	public static String JsString(String str, String def)
	{
		if(str == null || str.length() == 0)
		{
			str = (def == null ? "" : def);
		}
		
		int len = str.length();
		StringBuffer out = new StringBuffer((int)(len*1.5));
		out.append('\'');
		
		// Allow: a-z A-Z 0-9 SPACE , .
		// Allow (dec): 97-122 65-90 48-57 32 44 46
		
		for(int cnt = 0; cnt < len; cnt++)
		{
			char c = str.charAt(cnt);
			if( (c >= 97 && c <= 122) ||
				(c >= 65 && c <= 90 ) ||
				(c >= 48 && c <= 57 ) ||
				c == 32 || c == 44 || c == 46 )
			{
				out.append(c);
			}
			else if( c <= 127 )
			{
				out.append("\\x");
				
				String hex = Integer.toString(c, 16);
				if( hex.length() < 2 )
				{
					out.append('0');
				}
				
				out.append(hex);
			}
			else
			{
				out.append("\\u");
				
				String hex = Integer.toString(c, 16);
				for(int i = hex.length(); i<4; i++)
				{
					out.append('0');
				}
				
				out.append(hex);
			}
		}
		
		return out.append('\'').toString();
	}
	
	public static String VbsString(String str)
	{
		return VbsString(str, null);
	}
	public static String VbsString(String str, String def)
	{
		if(str == null || str.length() == 0)
		{
			str = (def == null ? "" : def);
			
			if(str.length() == 0)
				return "\"\"";
		}
		
		boolean inStr = false;
		int len = str.length();
		StringBuffer out = new StringBuffer((int)(len*1.5));
		
		// Allow: a-z A-Z 0-9 SPACE , .
		// Allow (dec): 97-122 65-90 48-57 32 44 46
		
		for(int cnt = 0; cnt < len; cnt++)
		{
			char c = str.charAt(cnt);
			if( (c >= 97 && c <= 122) ||
				(c >= 65 && c <= 90 ) ||
				(c >= 48 && c <= 57 ) ||
				c == 32 || c == 44 || c == 46 )
			{
				if(!inStr)
				{
					inStr = true;
					out.append("&\"");
				}
				
				out.append(c);
			}
			else
			{
				if(!inStr)
				{
					out.append("&chrw(").append((int)c).append(')');
				}
				else
				{
					out.append("\"&chrw(").append((int)c).append(')');
					inStr = false;
				}
			}
		}
		
		if( out.charAt(0) == '&' )
		{
			out.deleteCharAt(0);
		}
		
		return out.append(inStr ? "\"" : "").toString();
	}
}

// end
