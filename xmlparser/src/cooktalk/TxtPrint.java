package cooktalk;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TxtPrint {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("UserActs.txt"));
			
			for(int i=0;i<args.length;i++)
			{
				out.write(args[i]);
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		;
	}

}
