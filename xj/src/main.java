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
		//����
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
	        labelip=new JLabel("������  ��");
	        sendButton = new JButton("Send");  
	        comm= new JButton("  ����Ƶ��    ");
	        output= new JButton("�˳�");
	        
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
	        incoming.append(" δ����"+ "\n");
	        setUpNetworking(); 
	        Thread readerThread = new Thread(new IncomingReader());  
	        readerThread.start();  
	  
	        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);  
	        frame.setSize(300,450);  
	        frame.setVisible(true);  
	        
	    }

	  //����socket
	    private void setUpNetworking() {   
	        try {  
	            sock = new Socket("127.0.0.1", 5000);  
	            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());  
	            reader = new BufferedReader(streamReader);  
	            writer = new PrintWriter(sock.getOutputStream());  
	            System.out.println("������");  
	            writer.println(clientName);
	            writer.flush();
	            incoming.setText("������ "+ "\n");
	        } catch(IOException ex) {  
	            ex.printStackTrace();  
	        }  
	    } 
	  //�������������Ϣ
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
	    //����Ƶ��
	    public class commListener implements ActionListener {  
	        public void actionPerformed(ActionEvent ev) {  
	        	try {
		        	ComClient client = new ComClient(clientName);  
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    }
	    //�˳���ť����
	    public class outputListener implements ActionListener {  
	        public void actionPerformed(ActionEvent ev) {  
	        	try {
			    	writer.println("�˳�");
		            writer.flush();
			    	sock.close();
			    	flag=0;
			    	frame.dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    }
	   //��ؽ������Է���������Ϣ
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
		    	writer.println("�˳�");
	            writer.flush();
		    	sock.close();
			} catch (Exception e) {
				System.out.println("0");
			}
	    }
}
