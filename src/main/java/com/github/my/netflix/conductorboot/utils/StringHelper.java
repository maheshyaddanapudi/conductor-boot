package com.github.my.netflix.conductorboot.utils;

public class StringHelper
{
  public static String append(String string1, String string2, String string3)
  {
    StringBuffer buffer = new StringBuffer(string1);
    buffer.append(string2);
    buffer.append(string3);

    return buffer.toString();
  }

  public static String append(String string1, String string2, String string3, String string4)
  {
    StringBuffer buffer = new StringBuffer(string1);
    buffer.append(string2);
    buffer.append(string3);
    buffer.append(string4);

    return buffer.toString();
  }

  public static boolean areEqual(String thisString, String comparedString)
  {
    boolean result = false;

    if (thisString == null)
    {
      if (comparedString == null)
      {
        result = true;
      }
      else
      {
        result = false;
      }

    }
    else {
      result = thisString.equals(comparedString);
    }

    return result;
  }

  public static boolean contain(String containingString, String containedString)
  {
    boolean result = false;

    if (containingString != null)
    {
      result = containingString.indexOf(containedString) != -1;
    }

    return result;
  }

  public static boolean isValid(String value)
  {
    boolean validValue = false;

    if (value != null)
    {
      String trimmedString = value.trim();

      if ((trimmedString.length() > 0) && 
        (!(trimmedString.equalsIgnoreCase("null"))) && 
        (!(trimmedString.equalsIgnoreCase("?"))) && 
        (!(trimmedString.equalsIgnoreCase("-")))
        )
      {
        validValue = true;
      }
    }

    return validValue;
  }

  public static String removeAll(String str, char c)
  {
    StringBuffer buffer = new StringBuffer(str);

    for (int i = buffer.length() - 1; i >= 0; --i)
    {
      if (buffer.charAt(i) == c)
      {
        buffer.deleteCharAt(i);
      }
    }

    return buffer.toString();
  }

  public static String removeSuffix(String string, String suffix)
  {
    if (string.endsWith(suffix))
    {
      int suffixIndex = string.lastIndexOf(suffix);

      return string.substring(0, suffixIndex);
    }

    return string;
  }

  public static String replaceChar(String input, int pos, char c)
  {
    String result = null;

    if (pos == 0)
    {
      result = c + input.substring(1, input.length());
    }
    else if (pos == input.length())
    {
      result = input.substring(0, pos) + c;
    }
    else
    {
      result = input.substring(0, pos) + c + 
        input.substring(pos + 1, input.length());
    }

    return result;
  }

  public static Double toDouble(String stringValue)
  {
    Double doubleValue = null;

    if (stringValue != null)
    {
      String trimmedString = stringValue.trim();

      if (trimmedString.length() > 0)
      {
        doubleValue = new Double(trimmedString);
      }
    }

    return doubleValue;
  }

  public static Integer toInteger(String stringValue)
  {
    Integer integerValue = null;

    if (stringValue != null)
    {
      String trimmedString = stringValue.trim();

      if (trimmedString.length() > 0)
      {
        integerValue = new Integer(trimmedString);
      }
    }

    return integerValue;
  }

  public static Long toLong(String stringValue)
  {
    Long longValue = null;

    if (stringValue != null)
    {
      String trimmedString = stringValue.trim();

      if (trimmedString.length() > 0)
      {
        longValue = new Long(trimmedString);
      }
    }

    return longValue;
  }

  public static String toUpper(String str)
  {
    String returnStr = null;

    if (str != null)
    {
      returnStr = str.toUpperCase();
    }

    return returnStr;
  }

  public static String truncate(String value, int nChars)
  {
    String newValue = "";

    if ((value != null) && (value.length() > nChars))
    {
      newValue = value.substring(0, nChars);
    }
    else
    {
      newValue = value;
    }

    return newValue;
  }

  public static String valueOf(Object value)
  {
    String result = "";

    if (value != null)
    {
      result = value.toString();
    }

    return result;
  }
  
  public static boolean toBoolean(String value)
  {
	  boolean result = false;
	  
	  if(isValid(value) && "true".equalsIgnoreCase(value))
		  result = true;
	  
	  return result;
  }
  
  public static char toChar(String value)
  {
	  char result = ' ';
	  if(isValid(value) && value.length()==1)
	  {
		  result = value.toCharArray()[0];
	  }
	  return result;
  }
}