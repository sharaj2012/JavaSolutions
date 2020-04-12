package passwordgenerator;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Set;

import org.owasp.esapi.StringUtilities;
import org.owasp.esapi.util.CollectionsUtil;


//it creates a random password 
//finally working
public class GenerateRandomPassword {

	public static String getRandomPassword(int length, char[] charArray , char[] lowerArray , char[] upperArray , char[] digitArray)
			throws NoSuchAlgorithmException, NoSuchProviderException {
		String nonce = null;

		SecureRandom securerandom = SecureRandom.getInstance("SHA1PRNG");

		StringBuilder sb = new StringBuilder();

		sb.append(lowerArray[securerandom.nextInt(lowerArray.length)]);
		sb.append(upperArray[securerandom.nextInt(upperArray.length)]);
		sb.append(digitArray[securerandom.nextInt(digitArray.length)]);

		for (int loop = 0; loop < length-3; loop++) {
			int index = securerandom.nextInt(charArray.length);
			sb.append(charArray[index]);
		}
		nonce = sb.toString();

		return nonce;
	}


	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
		// TODO Auto-generated method stub

		char[] CHAR_LOWERS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
				's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
		char[] CHAR_UPPERS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
				'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		char[] CHAR_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		char[] CHAR_LETTERS = StringUtilities.union(CHAR_LOWERS, CHAR_UPPERS, CHAR_DIGITS);
		Set<Character> PASSWORD_LETTERS = CollectionsUtil.arrayToSet(CHAR_LETTERS);

		char[] CHAR_ARRAY = new char[PASSWORD_LETTERS.size()];

		int i = 0;
		for (char randChar : PASSWORD_LETTERS) {
			CHAR_ARRAY[i++] = randChar;
		}

		String randomPassword = getRandomPassword(10, CHAR_ARRAY, CHAR_LOWERS, CHAR_UPPERS, CHAR_DIGITS) ;

		System.out.println(randomPassword);



	}

}
