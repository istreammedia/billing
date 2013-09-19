import java.text.SimpleDateFormat;
import java.util.Date;


public class NewDate {
	
	public static void main(String[] args)   {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String  s = formatter.format(new Date());
		  System.out.println(s);

    }

}
