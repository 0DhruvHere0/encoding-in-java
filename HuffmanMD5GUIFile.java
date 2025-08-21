import java.awt.*;
import java.io.*;
import java.security.*;
import java.util.*;
import javax.swing.*;
class HuffmanNode {
    char ch;
    int freq;
    HuffmanNode left, right;
}
class HuffmanComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode x, HuffmanNode y) {
        return x.freq - y.freq;
    }
}
public class HuffmanMD5GUIFile extends JFrame {
    static Map<Character, String> huffmanCodeMap;
    static Map<String, String> storage = new HashMap<>();
    static final String FILE_NAME = "md5_storage.txt";
    JTextArea textArea;
    JButton encryptBtn, decryptBtn;
    JTextField hashField;
    JLabel resultLabel;
    public HuffmanMD5GUIFile() {
        setTitle("Huffman Encoder & MD5 Hash with File Storage");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        textArea = new JTextArea(5, 35);
        encryptBtn = new JButton("Encrypt Message");
        decryptBtn = new JButton("Decrypt Message");
        hashField = new JTextField(30);
        resultLabel = new JLabel("Result: ");
        add(new JLabel("Enter Message (for Encrypt) or MD5 Hash (for Decrypt):"));
        add(textArea);
        add(encryptBtn);
        add(new JLabel("Enter MD5 Hash to Decrypt:"));
        add(hashField);
        add(decryptBtn);
        add(resultLabel);
        encryptBtn.addActionListener(e -> encryptAction());
        decryptBtn.addActionListener(e -> decryptAction());
        loadStorageFromFile();
    }
    public static HuffmanNode buildHuffmanTree(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray())
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(new HuffmanComparator());
        for (var entry : freqMap.entrySet()) {
            HuffmanNode node = new HuffmanNode();
            node.ch = entry.getKey();
            node.freq = entry.getValue();
            pq.add(node);
        }
        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode parent = new HuffmanNode();
            parent.freq = left.freq + right.freq;
            parent.left = left;
            parent.right = right;
            pq.add(parent);
        }
        return pq.poll();
    }
    public static void generateCodes(HuffmanNode root, String code) {
        if (root == null) return;
        if (root.left == null && root.right == null)
            huffmanCodeMap.put(root.ch, code);
        generateCodes(root.left, code + "0");
        generateCodes(root.right, code + "1");
    }
    public static String getMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash)
                hexString.append(String.format("%02x", b));
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    public void encryptAction() {
        String text = textArea.getText().trim();
        if (text.isEmpty()) {
            resultLabel.setText("Please enter a message!");
            return;
        }
        huffmanCodeMap = new HashMap<>();
        HuffmanNode root = buildHuffmanTree(text);
        generateCodes(root, "");
        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray())
            encoded.append(huffmanCodeMap.get(c));
        String md5 = getMD5Hash(text);
        storage.put(md5, text);
        saveToFile(md5, text);
        resultLabel.setText("<html>MD5 Key: <b>" + md5 + "</b><br>Huffman Code: " + encoded + "</html>");
    }
    public void decryptAction() {
        String md5Key = hashField.getText().trim();
        if (storage.containsKey(md5Key)) {
            String originalText = storage.get(md5Key);
            resultLabel.setText("Decrypted Message: " + originalText);
        } else {
            resultLabel.setText("Invalid MD5 Key or Not Found!");
        }
    }
    public void saveToFile(String md5, String message) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true); BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(md5 + ":" + message);
            writer.newLine(); // Ensures the new entry is saved on a new line
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadStorageFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    storage.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HuffmanMD5GUIFile().setVisible(true));
    }
}