package de.zigapeda.flowspring.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Compare {
	private static final String[] REPLACEMENT = new String[Character.MAX_VALUE];
	static {
	    for(int i=Character.MIN_VALUE;i<Character.MAX_VALUE;i++) {
	        REPLACEMENT[i] = Character.toString(Character.toLowerCase((char) i));
	    }
	    // substitute
	    REPLACEMENT['À'] = "a";
	    REPLACEMENT['Á'] = "a";
	    REPLACEMENT['Â'] = "a";
	    REPLACEMENT['Ã'] = "a";
	    REPLACEMENT['Ä'] = "a";
	    REPLACEMENT['Å'] = "a";
	    REPLACEMENT['Ç'] = "c";
	    REPLACEMENT['È'] = "e";
	    REPLACEMENT['É'] = "e";
	    REPLACEMENT['Ê'] = "e";
	    REPLACEMENT['Ë'] = "e";
	    REPLACEMENT['Ì'] = "i";
	    REPLACEMENT['Í'] = "i";
	    REPLACEMENT['Î'] = "i";
	    REPLACEMENT['Ï'] = "i";
	    REPLACEMENT['Ð'] = "d";
	    REPLACEMENT['Ñ'] = "n";
	    REPLACEMENT['Ò'] = "o";
	    REPLACEMENT['Ó'] = "o";
	    REPLACEMENT['Ô'] = "o";
	    REPLACEMENT['Õ'] = "o";
	    REPLACEMENT['Ö'] = "o";
	    REPLACEMENT['Ù'] = "u";
	    REPLACEMENT['Ú'] = "u";
	    REPLACEMENT['Û'] = "u";
	    REPLACEMENT['Ü'] = "u";
	    REPLACEMENT['Ý'] = "y";
	    REPLACEMENT['ß'] = "s";
	    REPLACEMENT['à'] = "a";
	    REPLACEMENT['á'] = "a";
	    REPLACEMENT['â'] = "a";
	    REPLACEMENT['ã'] = "a";
	    REPLACEMENT['ä'] = "a";
	    REPLACEMENT['å'] = "a";
	    REPLACEMENT['ç'] = "c";
	    REPLACEMENT['è'] = "e";
	    REPLACEMENT['é'] = "e";
	    REPLACEMENT['ê'] = "e";
	    REPLACEMENT['ë'] = "e";
	    REPLACEMENT['ì'] = "i";
	    REPLACEMENT['í'] = "i";
	    REPLACEMENT['î'] = "i";
	    REPLACEMENT['ï'] = "i";
	    REPLACEMENT['ð'] = "d";
	    REPLACEMENT['ñ'] = "n";
	    REPLACEMENT['ò'] = "o";
	    REPLACEMENT['ó'] = "o";
	    REPLACEMENT['ô'] = "o";
	    REPLACEMENT['õ'] = "o";
	    REPLACEMENT['ö'] = "o";
	    REPLACEMENT['ù'] = "u";
	    REPLACEMENT['ú'] = "u";
	    REPLACEMENT['û'] = "u";
	    REPLACEMENT['ü'] = "u";
	    REPLACEMENT['ý'] = "y";
	    REPLACEMENT['ÿ'] = "y";
	    REPLACEMENT['²'] =  "2";
	    REPLACEMENT['³'] =  "3";
	    // remove
	    REPLACEMENT['-'] =  "";
	    REPLACEMENT['_'] =  "";
	    REPLACEMENT['.'] =  "";
	    REPLACEMENT[','] =  "";
	    REPLACEMENT[':'] =  "";
	    REPLACEMENT[';'] =  "";
	    REPLACEMENT['!'] =  "";
	    REPLACEMENT['"'] =  "";
	    REPLACEMENT['§'] =  "";
	    REPLACEMENT['$'] =  "";
	    REPLACEMENT['%'] =  "";
	    REPLACEMENT['&'] =  "";
	    REPLACEMENT['/'] =  "";
	    REPLACEMENT['('] =  "";
	    REPLACEMENT[')'] =  "";
	    REPLACEMENT['='] =  "";
	    REPLACEMENT['?'] =  "";
	    REPLACEMENT['`'] =  "";
	    REPLACEMENT['´'] =  "";
	    REPLACEMENT['^'] =  "";
	    REPLACEMENT['°'] =  "";
	    REPLACEMENT['*'] =  "";
	    REPLACEMENT['+'] =  "";
	    REPLACEMENT['#'] =  "";
	    REPLACEMENT['~'] =  "";
	    REPLACEMENT['<'] =  "";
	    REPLACEMENT['>'] =  "";
	    REPLACEMENT['|'] =  "";
	    REPLACEMENT['\\'] =  "";
	    REPLACEMENT['}'] =  "";
	    REPLACEMENT[']'] =  "";
	    REPLACEMENT['['] =  "";
	    REPLACEMENT['{'] =  "";
	    REPLACEMENT['€'] =  "";
	    REPLACEMENT['@'] =  "";
	    REPLACEMENT['\''] =  "";
	    REPLACEMENT[' '] =  "";
	    REPLACEMENT['÷'] = "";
	    REPLACEMENT['ø'] = "";
	    REPLACEMENT['×'] = "";
	    REPLACEMENT['Ø'] = "";
	    // expand
	    REPLACEMENT['Æ'] = "ae";
	    REPLACEMENT['æ'] = "ae";
	    REPLACEMENT['þ'] = "th";
	}
	
	static final byte[] HEX_CHAR_TABLE = {
		    (byte)'0', (byte)'1', (byte)'2', (byte)'3',
		    (byte)'4', (byte)'5', (byte)'6', (byte)'7',
		    (byte)'8', (byte)'9', (byte)'a', (byte)'b',
		    (byte)'c', (byte)'d', (byte)'e', (byte)'f'
	}; 
	
	public static String getComparableString(String string) {
		if(string != null) {
		    StringBuilder sb = new StringBuilder(string.length());
		    for(int i=0;i<string.length();i++)
		        sb.append(REPLACEMENT[string.charAt(i)]);
		    return sb.toString();
		}
		return null;
	}
	
	public static String getMD5(String string) {
		if(string != null) {
			byte[] message = null;
			MessageDigest md = null;
			try {
				message = string.getBytes("UTF-8");
				md = MessageDigest.getInstance("MD5");
			} catch (Exception e) {
				e.printStackTrace();
			}
			byte[] md5 = md.digest(message);
			BigInteger bi=new BigInteger(1, md5);
            return bi.toString(16);
		}
		return null;
	}
	
	public static String getMD5(File file) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
	        InputStream is=new FileInputStream(file);
	        byte[] buffer=new byte[8192];
	        int read=0;
	        while( (read = is.read(buffer)) > 0)
	                md.update(buffer, 0, read);
			byte[] md5 = md.digest();
			BigInteger bi=new BigInteger(1, md5);
			is.close();
            return bi.toString(16);
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
