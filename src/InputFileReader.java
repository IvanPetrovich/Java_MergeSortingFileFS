import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class InputFileReader {

	public BufferedReader bufReader;
	public Integer i;
	public String s;
	public String fileName;

	public InputFileReader(String fileName, BufferedReader bufReader) throws FileNotFoundException {

		this.bufReader = bufReader;
		this.fileName = fileName;
		this.i = null;
		this.s = null;

	}

}
