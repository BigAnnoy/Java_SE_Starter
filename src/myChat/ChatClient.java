package myChat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 
 * @author BigAnnoy Last_update 2018年9月11日下午11:10:36
 *
 */
public class ChatClient extends Frame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8168517896749301316L;
	private Socket socket = null;
	private TextArea textArea = new TextArea();
	private TextField textField = new TextField();
	private DataOutputStream dataOutputStream = null;
	private DataInputStream dataInputStream = null;
	private boolean connected = false; 
//	private String str;
	
	public static void main(String[] args) {
		new ChatClient().showMainFrame();
	}

	public void showMainFrame() {
		this.setLocation(1280, 768);
		this.setSize(1280, 768);
		this.add(textArea, BorderLayout.NORTH);
		this.add(textField, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
				System.exit(0);
			}
		});
		
		connect();
	}

	public void connect() {
		try {
			socket = new Socket("127.0.0.1", 2345);
			connected = true;
			new Thread(new ReceiveText()).start();
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			textField.addActionListener(new TfListener());
		} catch (IOException e) {
			System.out.println("No server available!");
			e.printStackTrace();
		}
		
	}

	public void disconnect() {
		try {
			dataOutputStream.close();
			dataOutputStream = null;
			dataInputStream.close();
			dataInputStream =null;
			socket.close();
			socket = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class TfListener implements ActionListener {

		public void actionPerformed(ActionEvent e) { 
			// TODO Auto-generated method stub
			String str = textField.getText().trim();
			textField.setText("");
			try {
				System.out.println(str);
				dataOutputStream.writeUTF(str);
				dataOutputStream.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}
	class ReceiveText implements Runnable{
		String recText = null;
		public ReceiveText() {
			try {
				dataInputStream = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			while (connected) {
				try {
					recText = dataInputStream.readUTF();
					textArea.setText(textArea.getText() + recText + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
