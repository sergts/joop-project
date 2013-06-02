
package project.client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.swing.*;

import project.messages.*;
import project.utils.ByteConverter;


/**
 * GUI layout inspired by http://cs.berry.edu/~nhamid/p2p/framework-java.html 
 *  GUI for file sharing application, it has the files, users, logs list
 *  it allows to download, search files, send personal messages
 *
 */
@SuppressWarnings("serial")
public class GUI extends JFrame{

	private static final int FRAME_WIDTH = 700, FRAME_HEIGHT = 650;
	private JPanel filesPanel, usersPanel, logsPanel, lowerFilesPanel, lowerUsersPanel;
	private DefaultListModel<String> filesModel, usersModel, logsModel;
	private JList<String> filesList, usersList, logsList;
	private JButton downloadFilesButton, shareFilesButton, searchFilesButton, setNameButton, refreshDataButton, PMButton;
	private JTextField shareTextField, searchTextField, setNameTextField, PMTextField;
	private Client client;
	
	/**
	 * Choose server address and port
	 */
	private static final String servAddress = "localhost";
	private static final int servPort = 8888;
	
	
	public static void main(String[] args) throws IOException{

		new GUI();

	}


	private GUI(){
		
		client = new Client(servAddress, servPort);
		
		

		downloadFilesButton = new JButton("Download");
		downloadFilesButton.addActionListener(new DownloadListener());
		refreshDataButton = new JButton("Refresh data");
		refreshDataButton.addActionListener(new RefreshDataListener());
		shareFilesButton = new JButton("Share");
		shareFilesButton.addActionListener(new ShareListener());
		searchFilesButton = new JButton("Search");
		searchFilesButton.addActionListener(new SearchListener());
		setNameButton = new JButton("Set Name");
		setNameButton.addActionListener(new SetNameListener());
		PMButton = new JButton("Send PM");
		PMButton.addActionListener(new PMListener());
		shareTextField = new JTextField(15);
		searchTextField = new JTextField(15);
		setNameTextField = new JTextField(15);
		PMTextField = new JTextField(15);
		setupFrame(this);

		(new Thread() { public void run() {  }}).start();

		new javax.swing.Timer(1000, new RefreshListener()).start();
		
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
		    @Override
		    public void run()
		    {
		        client.getOut().addMessage(new ExitMsg());
		        client.stopRunning();
		    }
		});
		

		
	}

	
	private void setupFrame(JFrame frame){


		frame = new JFrame(client.getGUILabel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		JPanel upperPanel = new JPanel();
		JPanel midPanel = new JPanel();
		JPanel lowerPanel = new JPanel();	
		upperPanel.setLayout(new GridLayout(1, 2));
		upperPanel.setPreferredSize(new Dimension(FRAME_WIDTH, (FRAME_HEIGHT * 1/2)));
		midPanel.setLayout(new GridLayout(1, 2));
		lowerPanel.setLayout(new GridLayout(1, 0));

		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

		filesModel = new DefaultListModel<String>();
		filesList = new JList<String>(filesModel);
		usersModel = new DefaultListModel<String>();
		usersList = new JList<String>(usersModel);
		logsModel = new DefaultListModel<String>();
		logsList = new JList<String>(logsModel);
		filesPanel = initPanel(new JLabel("Files"), filesList, 300, 200, false);
		usersPanel = initPanel(new JLabel("Users"), usersList, 170, 200, false);
		logsPanel = initPanel(new JLabel("Logs"), logsList, 580, 200, true);
		lowerFilesPanel = new JPanel();
		lowerUsersPanel = new JPanel();

		filesPanel.add(downloadFilesButton);
		filesPanel.add(refreshDataButton);
		
		lowerFilesPanel.add(shareTextField);
		lowerFilesPanel.add(shareFilesButton);
		lowerFilesPanel.add(searchTextField);
		lowerFilesPanel.add(searchFilesButton);
		
		
		lowerUsersPanel.add(PMTextField);
		lowerUsersPanel.add(PMButton);
		
		lowerUsersPanel.add(setNameTextField);
		lowerUsersPanel.add(setNameButton);
		
		

		upperPanel.add(filesPanel);
		upperPanel.add(usersPanel);
		midPanel.add(lowerFilesPanel);
		midPanel.add(lowerUsersPanel);
		lowerPanel.add(logsPanel);


		frame.add(upperPanel, BorderLayout.NORTH);
		frame.add(midPanel, BorderLayout.CENTER);
		frame.add(lowerPanel, BorderLayout.SOUTH);
		
		refreshDataButton.setVisible(false);
		downloadFilesButton.setVisible(false);
		setNameTextField.setVisible(false);
		setNameButton.setVisible(false);
		searchTextField.setVisible(false);
		searchFilesButton.setVisible(false);
		PMButton.setVisible(false);
		PMTextField.setVisible(false);
		

		frame.setVisible(true);

	}

	
	private JPanel initPanel(JLabel textField,JList<String> list, int x, int y, boolean bottom){
		JPanel panel = new JPanel();
		panel.add(textField);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(list);
		if(bottom){
			scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
		        public void adjustmentValueChanged(AdjustmentEvent e) {  
		            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
		        }
		    });
		}
		scrollPane.setPreferredSize(new Dimension(x, y));
		panel.add(scrollPane);
		return panel;
	}
	
	/**
	 * Updates list of logs. New logs are appended to the bottom.
	 */
	private void updateLogsList() {
		Iterator<String> logs = client.getLogger().iterator();
		String log;
		int index = 0;
		while(logs.hasNext()){
			log = logs.next();
			if(index >= logsModel.getSize()){
				logsModel.addElement(log);
			}
			index++;
			
		}	
	}

	/**
	 * Updates list of files from hashmap on clients side. This hashmap is also updated periodically.
	 */
	private void updateFileList() {
		String selected = "null";
		if(filesList.getSelectedValue() != null){
			selected = filesList.getSelectedValue();
		}
		int ind = 0;
		filesModel.removeAllElements();
		if(client.getFilesOnServer() != null){
			String element;
			for (String filename : client.getFilesOnServer().keySet()) {
				element = filename + " : "+ ByteConverter.formatSize(client.getFilesOnServer().get(filename).getSize());
				filesModel.addElement(element);
				if(element.equals(selected))
					filesList.setSelectedIndex(ind);
				ind++;
			}
		}
	}


	/**
	 * Updates list of users. User list is stored on clients side and is refereshed periodically.
	 */
	private void updateUserList(){
		String selected = "null";
		if(usersList.getSelectedValue() != null){
			selected = usersList.getSelectedValue();
		}
		int ind = 0;

		usersModel.removeAllElements();
		if(client.getUsersOnServer() != null){
			
			for (String username : client.getUsersOnServer()) {
				if(username.equals(client.getName())) username+=" (you)";
				usersModel.addElement(username);
				if(username.equals(selected))
					usersList.setSelectedIndex(ind);
				ind++;
			}
		}
	}

	/**
	 * 
	 * @author Serge
	 * IF file is selected and DOWNLOAD button is pressed, then query about downloading 
	 * is sent to the server.
	 *
	 */
	class DownloadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(filesList.getSelectedValue() != null){
				String selected = filesList.getSelectedValue().toString();
				String filename = selected.substring(0, selected.indexOf(':') - 1);
				String sub = (selected.substring(selected.indexOf(':') + 1,selected.length()));
				String owner = sub.substring(0, sub.indexOf(':')).trim();
				client.getOut().addMessage(new DownloadMessage(filename, owner));
			}
		}
	}
	/**
	 * 
	 * @author Serge
	 * Allows to set and reset a shared folder. 
	 *
	 */
	class ShareListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String dir = shareTextField.getText().trim();
			if(dir.length() > 0){
				if(!client.isInitDir() || client.getClientState() == 3){
					if(!client.isInitDir()){
						client.setDirectory(dir);
						client.setState(0);
					}else{
						if (!dir.equals("")) {
							client.resetDir(dir);
						}
						shareTextField.requestFocusInWindow();
						
						updateFileList();
					}
				}
			}
			
			
			
		}
	}

	/**
	 * 
	 * @author Serge
	 * Send query to show only these files, which have substring of search value.
	 *
	 */
	class SearchListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String search = searchTextField.getText().trim();
			if(search.length() > 0 ) client.getOut().addMessage(new FilesQuery(search));
			searchTextField.requestFocusInWindow();
			searchTextField.setText("");
			
		}
	}
	/**
	 * Sets name at the start of the program
	 * @author Serge
	 *
	 */
	class SetNameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String name = setNameTextField.getText().trim();
			if(name.length() > 0){
				Pattern p = Pattern.compile("[^a-zA-Z0-9]");
				if(p.matcher(name).find()){
					client.getLogger().add("Illegal name, should be alphanumeric");
				}
				else if(client.getClientState() == 0 || client.getClientState() == 2){
					client.getOut().addMessage(new InitName(name));
					client.setState(1);
					client.setName(name);
				}
			}
			
			setNameTextField.requestFocusInWindow();
			setNameTextField.setText("");
			
			
			
			
		}
	}
	/**
	 * If some user if selected, it then sends a private message to him.
	 * @author Serge
	 *
	 */
	class PMListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String msg = PMTextField.getText().trim();
			String thisUsr = client.getName() + " (you)";
			if(usersList.getSelectedValue().equals(thisUsr) && msg.length() > 0){
				client.getLogger().add("I'm sorry, " +client.getName()+", I'm afraid I can't do that.");
			}
			else if(usersList.getSelectedValue() != null && msg.length() > 0){
				String message = "("+client.getName()+"): "+msg;
				String to  = usersList.getSelectedValue();
				client.getOut().addMessage(new PersonalMessage(message, to));
				client.getLogger().add("Message sent to " + to);
				
			}
			
			PMTextField.requestFocusInWindow();
			PMTextField.setText("");
			
			
			
			
		}
	}

	/**
	 * Refreshed data in GUI also queries new user list.
	 * @author Serge
	 *
	 */
	class RefreshListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if(client.isInitDir() && client.getClientState() != 3){
				setNameTextField.setVisible(true);
				setNameButton.setVisible(true);
			}
			if(client.getClientState() == 3){
				setNameTextField.setVisible(false);
				setNameButton.setVisible(false);
				searchTextField.setVisible(true);
				searchFilesButton.setVisible(true);
				refreshDataButton.setVisible(true);
				downloadFilesButton.setVisible(true);
				PMButton.setVisible(true);
				PMTextField.setVisible(true);
				client.getOut().addMessage(new WhoMessage());
			}
			updateFileList();
			updateUserList();
			updateLogsList();
			
		}
	}
	/**
	 * Queries new data from the server and refreshes it in GUI
	 * @author Serge
	 *
	 */
	class RefreshDataListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			client.getOut().addMessage(new FilesQuery());
			client.getOut().addMessage(new WhoMessage());
			updateFileList();
			updateUserList();
			updateLogsList();
			
		}
	}
	
	


	
}
