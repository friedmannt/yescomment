package yescomment.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Encoder {

	

		public static String encodeHex(String password)
				throws NoSuchAlgorithmException, UnsupportedEncodingException {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
	        md.update(password.getBytes("UTF-8"));
	 
	        byte byteData[] = md.digest();
	 
	 
	        //convert the byte to hex format method 2
	        StringBuffer hexString = new StringBuffer();
	    	for (int i=0;i<byteData.length;i++) {
	    		String hex=Integer.toHexString(0xff & byteData[i]);
	   	     	if(hex.length()==1) hexString.append('0');
	   	     	hexString.append(hex);
	    	}
	    	
	    	String encodedResult = hexString.toString();
			return encodedResult;
		}
		
		
		
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		for (String arg:args) {
			System.out.println(arg+":"+encodeHex(arg));
			
		}
	}

}
