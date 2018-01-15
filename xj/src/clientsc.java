import java.net.Socket;

public class clientsc {
	String cname;
    Socket sock;  
	public clientsc(String cname,Socket sock){
		this.cname=cname;
		this.sock=sock;
	}
	public String getname(){
		return cname;
	}
	public Socket getsock(){
		return sock;
	}
}
