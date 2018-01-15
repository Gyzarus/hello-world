
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;

public class Server {
    ArrayList<clientsc> clientOutputStreams;  
	Server(){
//172.30.16.232
        Thread login = new Thread(new login_S());  
        login.start();
        Thread conn = new Thread(new conn());  
        conn.start(); 
		System.out.println("服务端已运行");
	}
	public static void main(String[] arges) throws Exception{
		new Server();
	}
	//监听来自客户端的连接
	public  class conn implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
            clientOutputStreams = new ArrayList<clientsc>();  
            try{  
                ServerSocket serverSock = new ServerSocket(5000);  
                while(true){
                    Socket clientSocket = serverSock.accept();  
                    InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());  
                    BufferedReader reader;  
                    reader = new BufferedReader(isReader);  
                    clientsc c = new clientsc(reader.readLine(),clientSocket);
                    clientOutputStreams.add(c);  
                    System.out.println(c.cname);
                    Thread t = new Thread(new ClientHandler(clientSocket));  
                    t.start();  
                    System.out.println("got a connection");  
                }  
            }catch(Exception ex){  
                ex.printStackTrace();  
        		System.out.println("0");
            }  
        }  
	}
		
	
	//监听来自客户端的信息
    public class ClientHandler implements Runnable{  
        BufferedReader reader;  
        PrintWriter writer;
        Socket sock;  
        public ClientHandler(Socket clientSocket){  
            try{  
                sock = clientSocket;  
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());  
                writer = new PrintWriter(sock.getOutputStream());    
                reader = new BufferedReader(isReader);  
            }catch(Exception ex){  
                ex.printStackTrace();  
        		System.out.println("1");
            }  
        }  
        @Override  
        public void run() {  
            String message;  
            try{  
                while((message = reader.readLine()) != null){  
                	if(message.equals("退出")){
                		sock.close();
                		break;
                	}
                    System.out.println("read " + message);  
            		String str[] = message.split(" ");
            		int i=clientOutputStreams.size()-1;
                	while(i>=0){
                		System.out.println(str[0]);
                		System.out.println(i);
                		if(str[0].equals(clientOutputStreams.get(i).getname()))
                			break;
                		i--;
                	}
                	if(i>=0){
                	PrintWriter writerto = new PrintWriter(clientOutputStreams.get(i)
                			.getsock().getOutputStream());
                			System.out.println("2");
    	                    String s = caesar.code(str[1]);
                            writerto.println(str[1]);  
                            writerto.flush();  
                            s = caesar.code(str[0]+"已接收信息: "+str[1]);
                            writer.println(s);  
                            writer.flush();  
                    }
                	else{
	                    String s = caesar.code("用户\""+str[0]+"\"不存在或者不在线！");
                        writer.println(s);  
                        writer.flush();  
                	}
                }  
            }catch(Exception ex){  
                ex.printStackTrace();  
            }  
        }  
    }        
	
  //处理来自客户端请求登陆、注册
	public  class login_S implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; //加载JDBC驱动
				String dbURL = "jdbc:sqlserver://localhost:4815; DatabaseName=xvj"; //连接服务器和数据库test
				String userName = "sa"; 
				String userPwd = "123456"; 
				Connection dbConn=null;
				Statement stmt=null;
				Class.forName(driverName);
				int port = 8887;
				ServerSocket mySocket = new ServerSocket(port);
				
				while(true){
				Socket sk = mySocket.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(sk.getInputStream()));
				PrintWriter out = new PrintWriter(sk.getOutputStream(),true);
				// System.out.println("客户端信息:"+in.readLine ());
				String ss;
				ss = in.readLine();
				System.out.println("客户端信息: " + ss);
				String str[] = ss.split(" ");
				try {
				  dbConn = DriverManager.getConnection(dbURL,userName, userPwd);
				  stmt=dbConn.createStatement();
				  if(str[0].equals("确定")){
						String query=
								"SELECT * FROM login where cname ='"+str[1]
								+"' and cpassw ='"+str[2]+"'";
						ResultSet rs=stmt.executeQuery(query);
						if (rs.next()) {
							System.out.println("OK");
					        out.println("登陆成功");  
						}else {
							System.out.println("no");
							out.println("登陆失败");
						}
				  }
				  else if(str[0].equals("注册")){
						 String upd=
								 "INSERT INTO login (cname,cpassw) values('"
								 +str[1]+"','"+str[2]+"')";
						 stmt=dbConn.createStatement();
						 stmt.executeUpdate(upd);
						 out.println("注册成功");
				  }
				} catch (Exception e) {
				  e.printStackTrace();
				}	finally {
					try {
						if(stmt!=null)
							stmt.close();
						if(dbConn!=null)
							dbConn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				sk.close();
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
	}
}