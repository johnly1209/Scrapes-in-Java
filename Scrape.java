import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.*;
import java.util.regex.*;

import javax.swing.*;
import javax.swing.table.*;

public class Scrape extends JFrame {
    // components
    JPanel mainPanel = new JPanel();
    JTextField tfURL = new JTextField();
    // JTextField tfREGEX = new JTextField(20);

    String[] REGEXOPTIONS = {
            "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+", /* emails */
            "[0-9]{3}-[0-9]{3}-[0-9]{4}", /* phone numbers */
            "<[^>]+>", /* HTML elements */
            "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b" /* IP addresses */
    };
    JComboBox<String> cbREGEX = new JComboBox<String>(REGEXOPTIONS);
    JButton btClick = new JButton("Click me!");
    JTable table;
    DefaultTableModel tm;

    // methods
    public void GameOn(ActionEvent e) throws Exception {
        URL url = new URL(tfURL.getText());
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                Matcher m = Pattern.compile(cbREGEX.getSelectedItem().toString()).matcher(line);
                while (m.find()) {
                    tm.insertRow(table.getRowCount(), new Object[] { table.getRowCount() + 1, m.group() });
                }
            }
        }
    }

    // ctor
    public Scrape() {
        mainPanel.setLayout(new BorderLayout());
        /////////////////// NORTH
        mainPanel.add(tfURL, BorderLayout.NORTH);

        /////////////////// CENTER
        tm = new DefaultTableModel();
        table = new JTable(tm);

        tm.addColumn(" #");
        tm.addColumn("RESULTS");

        JScrollPane jsp = new JScrollPane(table);
        add(jsp);

        mainPanel.add(jsp, BorderLayout.CENTER);
        /////////////////// SOUTH
        JPanel southPanel = new JPanel();
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        southPanel.add(cbREGEX);
        southPanel.add(btClick);
        btClick.addActionListener(e -> {
            try {

                GameOn(e);
            } catch (Exception eee) {
                JOptionPane.showMessageDialog(null, eee.getMessage());
            }
        }

        );

        setContentPane(mainPanel);
        setTitle("Scrape the Internet");//
        setSize(600, 400);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public static void main(String[] args) {
        Scrape s = new Scrape();
    }
}