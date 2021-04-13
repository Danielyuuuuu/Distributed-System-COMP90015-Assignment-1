package client;

@SuppressWarnings("serial")
public class ExceptionHandling extends Exception{

	//    public ExceptionHandling(String s)
	//    {
	//        super(s);
	//    }

	String s1, s2;
	
	public ExceptionHandling(String s3, String s4) {
		s1 = s3;
		s2 = s4;
	} 
	
	@Override
	public String toString() { 
		return (s1 + ": " + s2);
	}
}