



import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.*;
import javax.swing.*;
import java.sql.*;

public class login extends JFrame implements ActionListener {
	Label statc;
	Button signin,signup;
	TextField T1,T2;
	
	public login() {
		super("�û���¼");
		this.setLayout(new BorderLayout());
		Panel P1 = new Panel();
		Panel P2 = new Panel();
		signin = new Button("ȷ��");
		signup = new Button("ע��");
		P2.setLayout(new GridLayout(2, 2, 2, 2));
		
		this.setSize(270, 110);
		this.setLocation(300, 240);
		this.setBackground(Color.lightGray);
		this.add(P1, "South");
		this.add(P2, "Center");
		P1.add(signin);
		P1.add(signup);
		statc = new Label("���¼");
		P1.add(statc);
		
		P2.add(new Label("�û���"));
		T1 = new TextField("user", 10);
		P2.add(T1);
		P2.add(new Label("����"));
		T2 = new TextField(10);
		P2.add(T2);

		signin.addActionListener(this);
		signup.addActionListener(this);
		
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ev) {
		  try {
			   InetAddress addr = InetAddress.getByName(null);
			   Socket sk = new Socket(addr, 8887);
			   BufferedReader in = new BufferedReader(new InputStreamReader(sk
			     .getInputStream()));
			   PrintWriter out = new PrintWriter(sk.getOutputStream(), true);
			   out.println(ev.getActionCommand()+" "+T1.getText()+" "+T2.getText());
			   String s= in.readLine();
			   statc.setText(s);
			   if(s.equals("��½�ɹ�")){
				   new main(T1.getText());
				   dispose();
				   }
			   sk.close();
			  } catch (Exception e) {
			   System.out.println("�޷����ӵ�����������\n�������������ݿ�˿ڿ��ܲ���ȷ��\n���߷��������ڻ���û�ж�Ӧ���ݿ�");
			  }
	}
	
	public static void main(String[] args) {
		new login();
	}
}
