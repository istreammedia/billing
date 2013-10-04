package com.journaldev.exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ExceptionHandling {

public static void main(String[] args) throws FileNotFoundException, IOException {
try{
testException(-5);
testException(-10);
}catch(Exception exception){
	Throwable throwable=exception.getCause();
System.out.println(exception);	
}
}
public static void testException(int i) throws FileNotFoundException, IOException{
if(i < 0){
FileNotFoundException myException = new FileNotFoundException("Negative Integer "+i);
throw myException;
}else if(i > 10){
	            throw new IOException("Only supported for index 0 to 10");
	        }

}

	}