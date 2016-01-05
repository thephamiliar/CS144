import java.security.*;
import java.io.*;
import java.nio.file.*;
import java.util.Formatter;

public class ComputeSHA {
	public static void main(String[] args) throws IOException {
		Path path = null;
		MessageDigest md;

		try {
			path = Paths.get(args[0]);
			byte[] input = Files.readAllBytes(path);
			md = MessageDigest.getInstance("SHA1");

			md.update(input);
			byte[] output = md.digest();
			System.out.println(bytesToHex(output));
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}

	private static String bytesToHex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}

		String result = formatter.toString();
		formatter.close();
		return result;
	}
}