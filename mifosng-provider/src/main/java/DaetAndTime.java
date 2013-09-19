import java.math.BigDecimal;



public class DaetAndTime {
	
	
	public static void main(String[] args){
	
		BigDecimal bigDecimal=BigDecimal.ONE.setScale(2);
		System.out.println(bigDecimal.multiply(new BigDecimal(100)));
	}

}
