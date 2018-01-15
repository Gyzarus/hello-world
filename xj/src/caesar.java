


public class caesar {
	
    public static String code(String str){
    	String coded="";
    	for(int i=0;i<str.length();i++){
    		coded+=(char)(str.charAt(i)+3);
    	}
    	return coded;
    }
	  public static String encode(String str){
	  	String coded="";
	  	for(int i=0;i<str.length();i++){
	  		coded+=(char)(str.charAt(i)-3);
	  	}
	  	return coded;
	  }
}
