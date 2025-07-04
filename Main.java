package workingdsa3;

//Donation Exchange Platform for Community
/*
* The "Donation Exchange Platform for Community" is an innovative solution
*  that enables users to donate, claim items of their interest, 
*  fostering the reuse of goods, reducing waste, and promoting 
*  sustainability through responsible sharing and resource exchange.
* 
*/

import javax.swing.*;
import java.awt.*;
import java.util.*;

class User {
    String username, password, location, contact, gender;
    Stack<Donation> donationHistory = new Stack<>();
    Queue<Donation> claimedDonations = new LinkedList<>();
    ArrayList<Message> messages = new ArrayList<>();
    ArrayList<String> ratings = new ArrayList<>();

    public User(String username, String password, String location, String contact, String gender) {
        this.username = username;
        this.password = password;
        this.location = location;
        this.contact = contact;
        this.gender = gender;
    }
}

class Donation {
    String donorUsername, itemName, category, location;
    boolean isClaimed;

    public Donation(String donorUsername, String itemName, String category, String location) {
        this.donorUsername = donorUsername;
        this.itemName = itemName;
        this.category = category;
        this.location = location;
        this.isClaimed = false;
    }

    public String toString() {
        return "Item: " + itemName + ", Category: " + category + ", Location: " + location + ", Status: " + (isClaimed ? "Claimed" : "Available");
    }
}

class Message {
    String sender, receiver, content;
    Date timestamp;

    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = new Date();
    }

    public String toString() {
        return "[" + timestamp + "] " + sender + " to " + receiver + ": " + content;
    }
}

public class Main extends JFrame {
    HashMap<String, User> userMap = new HashMap<>();
    ArrayList<Donation> donations = new ArrayList<>();
    ArrayList<Message> allMessages = new ArrayList<>();
    User loggedInUser = null;

    public Main() {
        setTitle("Community Donation Exchange Platform");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(12, 1));

        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");
        JButton donateButton = new JButton("Post Donation");
        JButton claimButton = new JButton("Claim Donation");
        JButton viewButton = new JButton("View Available Donations");
        JButton claimedButton = new JButton("View Claimed Donations");
        JButton messageButton = new JButton("Send Message");
        JButton viewMessagesButton = new JButton("View Messages");
        JButton historyButton = new JButton("View Donation History");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(donateButton);
        buttonPanel.add(claimButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(claimedButton);
        buttonPanel.add(messageButton);
        buttonPanel.add(viewMessagesButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.CENTER);

        registerButton.addActionListener(e -> registerUser());
        loginButton.addActionListener(e -> loginUser());
        donateButton.addActionListener(e -> { if (checkLogin()) postDonation(); });
        viewButton.addActionListener(e -> viewAvailableDonations());
        claimButton.addActionListener(e -> { if (checkLogin()) claimDonation(); });
        messageButton.addActionListener(e -> { if (checkLogin()) sendMessage(); });
        viewMessagesButton.addActionListener(e -> { if (checkLogin()) viewMessages(); });
        historyButton.addActionListener(e -> { if (checkLogin()) viewDonationHistory(); });
        claimedButton.addActionListener(e -> { if (checkLogin()) viewClaimedDonations(); });
        logoutButton.addActionListener(e -> logout());

        setVisible(true);
    }

    private boolean checkLogin() {
        if (loggedInUser == null) {
            JOptionPane.showMessageDialog(this, "Please login to continue.");
            return false;
        }
        return true;
    }

    private void registerUser() {
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        if (username == null) return;
        if (userMap.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists.");
            return;
        }
        String password = JOptionPane.showInputDialog(this, "Enter password:");
        if (password == null) return;
        String location = JOptionPane.showInputDialog(this, "Enter location:");
        if (location == null) return;
        String contact = JOptionPane.showInputDialog(this, "Enter contact:");
        if (contact == null) return;
        String gender = JOptionPane.showInputDialog(this, "Enter gender:");
        if (gender == null) return;
        userMap.put(username, new User(username, password, location, contact, gender));
        JOptionPane.showMessageDialog(this, "User registered successfully.");
    }

    private void loginUser() {
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        if (username == null) return;
        String password = JOptionPane.showInputDialog(this, "Enter password:");
        if (password == null) return;
        User user = userMap.get(username);
        if (user != null && user.password.equals(password)) {
            loggedInUser = user;
            JOptionPane.showMessageDialog(this, "Login successful. Welcome, " + username + "!");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again or register.");
        }
    }

    private void logout() {
        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(this, "Logged out: " + loggedInUser.username);
            loggedInUser = null;
        } else {
            JOptionPane.showMessageDialog(this, "No user is currently logged in.");
        }
    }

    private void postDonation() {
        String item = JOptionPane.showInputDialog(this, "Enter item to donate:");
        if (item == null) return;
        String category = JOptionPane.showInputDialog(this, "Enter category:");
        if (category == null) return;
        String location = JOptionPane.showInputDialog(this, "Enter location:");
        if (location == null) return;
        Donation newDonation = new Donation(loggedInUser.username, item, category, location);
        donations.add(newDonation);
        loggedInUser.donationHistory.push(newDonation);
        JOptionPane.showMessageDialog(this, "Donation posted successfully.");
    }

    private void viewAvailableDonations() {
        StringBuilder output = new StringBuilder("Available Donations:\n");
        for (Donation d : donations) {
            if (!d.isClaimed) output.append(d).append("\n");
        }
        JOptionPane.showMessageDialog(this, donations.size() > 0 ? output.toString() : "No donations available.");
    }

    private void claimDonation() {
        String item = JOptionPane.showInputDialog(this, "Enter item name to claim:");
        if (item == null) return;
        for (Donation d : donations) {
            if (d.itemName.equalsIgnoreCase(item) && !d.isClaimed) {
                d.isClaimed = true;
                loggedInUser.claimedDonations.add(d);
                JOptionPane.showMessageDialog(this, "Successfully claimed: " + item);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Donation not found or already claimed.");
    }

    private void viewDonationHistory() {
        StringBuilder history = new StringBuilder("Donation History (Stack):\n");
        Stack<Donation> temp = new Stack<>();
        while (!loggedInUser.donationHistory.isEmpty()) {
            Donation d = loggedInUser.donationHistory.pop();
            history.append(d).append("\n");
            temp.push(d);
        }
        while (!temp.isEmpty()) loggedInUser.donationHistory.push(temp.pop());
        JOptionPane.showMessageDialog(this, loggedInUser.donationHistory.size() > 0 ? history.toString() : "No donation history.");
    }

    private void viewClaimedDonations() {
        StringBuilder claimed = new StringBuilder("Claimed Donations (Queue):\n");
        Queue<Donation> temp = new LinkedList<>();
        while (!loggedInUser.claimedDonations.isEmpty()) {
            Donation d = loggedInUser.claimedDonations.remove();
            claimed.append(d).append("\n");
            temp.add(d);
        }
        while (!temp.isEmpty()) loggedInUser.claimedDonations.add(temp.remove());
        JOptionPane.showMessageDialog(this, loggedInUser.claimedDonations.size() > 0 ? claimed.toString() : "No claimed donations.");
    }

    private void sendMessage() {
        String receiver = JOptionPane.showInputDialog(this, "Enter receiver username:");
        if (receiver == null || receiver.equals(loggedInUser.username)) {
            JOptionPane.showMessageDialog(this, "Invalid receiver.");
            return;
        }
        User receiverUser = userMap.get(receiver);
        if (receiverUser == null) {
            JOptionPane.showMessageDialog(this, "Receiver does not exist.");
            return;
        }
        String content = JOptionPane.showInputDialog(this, "Enter your message:");
        if (content == null) return;
        Message msg = new Message(loggedInUser.username, receiver, content);
        loggedInUser.messages.add(msg);
        receiverUser.messages.add(msg);
        allMessages.add(msg);
        JOptionPane.showMessageDialog(this, "Message sent.");
    }

    private void viewMessages() {
        StringBuilder output = new StringBuilder("Your Messages:\n");
        for (Message m : loggedInUser.messages) output.append(m).append("\n");
        JOptionPane.showMessageDialog(this, loggedInUser.messages.size() > 0 ? output.toString() : "No messages.");
    }



    public static void main(String[] args) {
        new Main();
    }
}
