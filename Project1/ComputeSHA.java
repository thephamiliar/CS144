import java.security.*;
import java.io.*;
import java.nio.file.*;
import java.util.Formatter;

public class ComputeSHA {
	public static void main(String[] args) throws IOException {
		Path path = null;
		MessageDigest md;

		try {
			// get bytes from input
			path = Paths.get(args[0]);
			byte[] input = Files.readAllBytes(path);

			// convert to SHA-1 encoding
			md = MessageDigest.getInstance("SHA1");
			md.update(input);

			// print bytes to hex format
			byte[] output = md.digest();
			System.out.println(bytesToHex(output));
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}

	private static String bytesToHex(byte[] bytes) {
		Formatter formatter = new Formatter();
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}

		String result = formatter.toString();
		formatter.close();
		return result;
	}
}