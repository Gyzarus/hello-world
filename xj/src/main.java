import java.io.*;  
import java.net.*;  
import javax.swing.*;  
import java.awt.*;  
import java.awt.event.*;
import java.sql.*;

public class main {
		JFrame frame ;
	  	JTextArea incoming;  
	    JTextField outgoing,oip;  
	    JLabel labelip;
	    JButton sendButton ,comm,output;
	    static String clientName;   
	    Socket sock;   
	    BufferedReader reader;  
	    PrintWriter writer;  
	    int flag=1;
	    public static void main(String[] args) throws Exception{
	    	main sc=new main("test");
		}
		//界面
	    public main(String str)throws Exception{
	    	clientName=str;
	    	
	        frame = new JFrame(clientName + "'s Chat Client");  
	        JPanel mainPanel = new JPanel();  
	      
	        incoming = new JTextArea(15,20);    
	        incoming.setLineWrap(true);  
	        incoming.setWrapStyleWord(true);  
	        incoming.setEditable(false);  
	  
	        JScrollPane qScroller = new JScrollPane(incoming);  
	        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);  
	        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);  
	  
	        outgoing = new JTextField(16);  
	        oip = new JTextField(10);  
	        labelip=new JLabel("发送至  ：");
	        sendButton = new JButton("Send");  
	        comm= new JButton("  公共频道    ");
	        output= new JButton("退出");
	        
	        sendButton.addActionListener(new SendButtonListener());  
	        comm.addActionListener(new commListener());  
	        output.addActionListener(new outputListener());  
	        
	        mainPanel.add(qScroller); 
	        mainPanel.add(outgoing);  
	        mainPanel.add(sendButton); 
	        mainPanel.add(labelip);
	        mainPanel.add(oip);  
	        mainPanel.add(comm);  
	        mainPanel.add(output);
	        incoming.append(" 未连接"+ "\n");
	        setUpNetworking(); 
	        Thread readerThread = new Thread(new IncomingReader());  
	        readerThread.start();  
	  
	        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);  
	        frame.setSize(300,450);  
	        frame.setVisible(true);  
	        
	    }

	  //设置socket
	    private void setUpNetworking() {   
	        try {  
	            sock = new Socket("127.0.0.1", 5000);  
	            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());  
	            reader = new BufferedReader(streamReader);  
	            writer = new PrintWriter(sock.getOutputStream());  
	            System.out.println("已连接");  
	            writer.println(clientName);
	            writer.flush();
	            incoming.setText("在线中 "+ "\n");
	        } catch(IOException ex) {  
	            ex.printStackTrace();  
	        }  
	    } 
	  //向服务器发送信息
	    public class SendButtonListener implements ActionListener {  
	        public void actionPerformed(ActionEvent ev) {  
	            try {  
	            	String s=caesar.code(clientName+":"+outgoing.getText());
	            	writer.println(oip.getText()+" "+s);  
	                writer.flush();  
	            } catch(Exception ex) {  
	                ex.printStackTrace();  
	            }  

	        }  
	    }
	    //公共频道
	    public class commListener implements ActionListener {  
	        public void actionPerformed(ActionEvent ev) {  
	        	try {
		        	ComClient client = new ComClient(clientName);  
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    }
	    //退出按钮方法
	    public class outputListener implements ActionListener {  
	        public void actionPerformed(ActionEvent ev) {  
	        	try {
			    	writer.println("退出");
		            writer.flush();
			    	sock.close();
			    	flag=0;
			    	frame.dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    }
	   //监控接受来自服务器的信息
	    public class IncomingReader implements Runnable {  
	        public void run() {  
	            String message = null;              
	            try {  
	                while ( flag!= 0) {      
	                	message = reader.readLine();
	                    System.out.println("read " + message);  
	                    String s=caesar.encode(message);
	                    incoming.append("--"+s + "\n"); 
	                }  
	            } catch(Exception ex) {ex.printStackTrace();}  
	        } 
	    }   
	    
	    protected void finalize () {
	    	try {
		    	writer.println("退出");
	            writer.flush();
		    	sock.close();
			} catch (Exception e) {
				System.out.println("0");
			}
	    }
}
