package gpovallas.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Security {

	public static String HashMD5(String s) {

		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			byte[] bytes = s.getBytes();

	        byte[] digest = m.digest(bytes);
	        return new BigInteger(1, digest).toString(16);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static boolean isNifNie(String nif){

		//si es NIE, eliminar la x,y,z inicial para tratarlo como nif
		if (nif.toUpperCase().startsWith("X")||nif.toUpperCase().startsWith("Y")||nif.toUpperCase().startsWith("Z"))
		nif = nif.substring(1);

		Pattern nifPattern = Pattern.compile("(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])");
		Matcher m = nifPattern.matcher(nif);
		if(m.matches()){
			String letra = m.group(2);
			//Extraer letra del NIF
			String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
			int dni = Integer.parseInt(m.group(1));
			dni = dni % 23;
			String reference = letras.substring(dni,dni+1);
			if (reference.equalsIgnoreCase(letra)){
				//_log.debug("son iguales. Es NIF. "+letra+" "+reference);
				return true;
			}else{
				//_log.debug("NO son iguales. NO es NIF. "+letra+" "+reference);
				return false;
			}
		}else
			return false;
	}

}

