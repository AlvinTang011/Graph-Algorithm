package Interface;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import hospitalPathFinder.hospitalPathFinder;

import javax.swing.border.EtchedBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class App {
    private JFrame frmBfsHospitalSearcher;
    private String graphDirectory;
    private String hospDirectory;
    private int kValue;
    private JTextField kValueTF;
    private JButton submitBtn;
    private JPanel kValuePanel;
    private JLabel lblEnterKValue;
    private JLabel lblError;
    private JPanel hospPanel;
    private JLabel lblHospFile;
    private JLabel lblHospFileDirectory;
    private JButton btnChooseHosp;
    private JPanel graphPanel;
    private JLabel lblEdgeFile;
    private JLabel lblgraphFileDirectory;
    private JButton btnChooseGraph;
    private JButton btnNewButton;
    private JTextArea outputField;
    private JLabel lblOutput;
    private JButton openOutputBtn;
    private JScrollPane scrollPane;


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    App window = new App();
                    window.frmBfsHospitalSearcher.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public App() {
        initialize();
    }

    private void initialize() {
        frmBfsHospitalSearcher = new JFrame();
        frmBfsHospitalSearcher.setResizable(false);
        frmBfsHospitalSearcher.setTitle("BFS Hospital Searcher");
        frmBfsHospitalSearcher.setBounds(100, 100, 702, 596);
        frmBfsHospitalSearcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmBfsHospitalSearcher.getContentPane().setLayout(null);

        // Components to display output
        outputField = new JTextArea();
        outputField.setOpaque(true);
        outputField.setBackground(UIManager.getColor("Button.light"));
        outputField.setEditable(false);
        outputField.setLineWrap(true);
        outputField.setBounds(13, 327, 658, 159);
        outputField.setText("Initializing...");
        frmBfsHospitalSearcher.getContentPane().add(outputField);
        scrollPane = new JScrollPane(outputField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(13, 330, 660, 180);
        scrollPane.setVisible(false);
        frmBfsHospitalSearcher.getContentPane().add(scrollPane);

        lblOutput = new JLabel("Output:");
        lblOutput.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblOutput.setBounds(13, 305, 74, 25);
        lblOutput.setVisible(false);
        frmBfsHospitalSearcher.getContentPane().add(lblOutput);

        openOutputBtn = new JButton("Open Output File");
        openOutputBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
        openOutputBtn.setBounds(483, 517, 188, 30);
        openOutputBtn.setVisible(false);
        frmBfsHospitalSearcher.getContentPane().add(openOutputBtn);
        openOutputBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFileDirectory();
            }
        });

        // Submit button for data processing
        submitBtn = new JButton("Submit for processing");
        submitBtn.setEnabled(false);
        submitBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
        submitBtn.setBounds(483, 272, 188, 30);
        frmBfsHospitalSearcher.getContentPane().add(submitBtn);
        submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String output = null;
                try {
                    output = processData();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                lblOutput.setVisible(true);
                scrollPane.setVisible(true);
                String currentText = outputField.getText();
                String newText = "\n" + currentText + "\n" + output;
                outputField.setText(newText);
                openOutputBtn.setVisible(true);
            }
        });

        // "Enter k value" components
        kValuePanel = new JPanel();
        kValuePanel.setLayout(null);
        kValuePanel.setBorder(new TitledBorder(
                new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Step 3",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        kValuePanel.setBounds(13, 202, 660, 60);
        frmBfsHospitalSearcher.getContentPane().add(kValuePanel);

        lblEnterKValue = new JLabel("Enter k value:");
        lblEnterKValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblEnterKValue.setBounds(36, 18, 96, 30);
        kValuePanel.add(lblEnterKValue);

        lblError = new JLabel("");
        lblError.setForeground(Color.RED);
        lblError.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblError.setBounds(239, 18, 377, 30);
        kValuePanel.add(lblError);

        kValueTF = new JTextField();
        kValueTF.setBounds(133, 18, 96, 30);
        kValueTF.setEditable(false);
        kValuePanel.add(kValueTF);
        kValueTF.setColumns(10);
        kValueTF.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                check();
            }

            public void removeUpdate(DocumentEvent e) {
                check();
            }

            public void insertUpdate(DocumentEvent e) {
                check();
            }

            public void check() {
                boolean isInt;
                try {
                    Integer.parseInt(kValueTF.getText());
                    isInt = true;
                } catch (NumberFormatException nfe) {
                    isInt = false;
                }

                if (!kValueTF.getText().isEmpty() && isInt == false) {
                    lblError.setText("Not integer value");
                    submitBtn.setEnabled(false);
                } else if (kValueTF.getText().isEmpty()) {
                    lblError.setText("Field is empty");
                    submitBtn.setEnabled(false);
                } else {
                    lblError.setText(" ");
                    submitBtn.setEnabled(true);
                }

            }
        });

        // "Select hospital file" components
        hospPanel = new JPanel();
        hospPanel.setLayout(null);
        hospPanel.setBorder(new TitledBorder(
                new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Step 2",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        hospPanel.setBounds(13, 120, 660, 60);
        frmBfsHospitalSearcher.getContentPane().add(hospPanel);

        lblHospFile = new JLabel("Choose Hosp File:");
        lblHospFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblHospFile.setBounds(11, 18, 127, 30);
        hospPanel.add(lblHospFile);

        lblHospFileDirectory = new JLabel("");
        lblHospFileDirectory.setOpaque(true);
        lblHospFileDirectory.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblHospFileDirectory.setBackground(Color.WHITE);
        lblHospFileDirectory.setBounds(133, 18, 377, 30);
        hospPanel.add(lblHospFileDirectory);

        btnChooseHosp = new JButton("Select File");
        btnChooseHosp.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnChooseHosp.setBounds(520, 18, 127, 30);
        btnChooseHosp.setEnabled(false);
        hospPanel.add(btnChooseHosp);
        btnChooseHosp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String result = selectFile();
                lblHospFileDirectory.setText(result);
                if (!lblHospFileDirectory.getText().isEmpty()) {
                    kValueTF.setEditable(true);
                }
            }
        });

        // "Select graph file" components
        graphPanel = new JPanel();
        graphPanel.setBorder(new TitledBorder(
                new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Step 1",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        graphPanel.setBounds(13, 38, 660, 60);
        frmBfsHospitalSearcher.getContentPane().add(graphPanel);
        graphPanel.setLayout(null);

        lblEdgeFile = new JLabel("Choose Graph File:");
        lblEdgeFile.setBounds(11, 18, 127, 30);
        graphPanel.add(lblEdgeFile);
        lblEdgeFile.setFont(new Font("Tahoma", Font.PLAIN, 14));

        lblgraphFileDirectory = new JLabel("");
        lblgraphFileDirectory.setBounds(133, 18, 377, 30);
        graphPanel.add(lblgraphFileDirectory);
        lblgraphFileDirectory.setOpaque(true);
        lblgraphFileDirectory.setBackground(Color.WHITE);
        lblgraphFileDirectory.setFont(new Font("Tahoma", Font.PLAIN, 14));

        btnChooseGraph = new JButton("Select File");
        btnChooseGraph.setBounds(520, 18, 127, 30);
        graphPanel.add(btnChooseGraph);
        btnChooseGraph.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnChooseGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String result = selectFile();
                lblgraphFileDirectory.setText(result);
                if (!lblgraphFileDirectory.getText().isEmpty()) {
                    btnChooseHosp.setEnabled(true);
                }
            }
        });

        // For ease of testing only // to be deleted
        btnNewButton = new JButton("Quick Test");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnNewButton.setBounds(250, 272, 188, 30);
        frmBfsHospitalSearcher.getContentPane().add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String output = null;
                try {
                    output = processData2();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                lblOutput.setVisible(true);
                scrollPane.setVisible(true);
                String currentText = outputField.getText();
                String newText = "\n" + currentText + "\n" + output;
                outputField.setText(newText);
                openOutputBtn.setVisible(true);
            }
        });

        // Menu components
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 698, 22);
        frmBfsHospitalSearcher.getContentPane().add(menuBar);

        JMenu Menumn = new JMenu("Menu");
        menuBar.add(Menumn);

        JMenuItem resetMenu = new JMenuItem("Reset");
        Menumn.add(resetMenu);
        resetMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblgraphFileDirectory.setText("");
                lblHospFileDirectory.setText("");
                kValueTF.setText("");
                lblError.setText("");
                btnChooseGraph.setEnabled(true);
                btnChooseHosp.setEnabled(false);
                kValueTF.setEnabled(false);
                submitBtn.setEnabled(false);
                scrollPane.setVisible(false);
                lblOutput.setVisible(false);
                openOutputBtn.setVisible(false);
                return;
            }
        });
    }

    // Open file selector
    public String selectFile() {
        String directory = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt", "text"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(frmBfsHospitalSearcher);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            directory = selectedFile.getAbsolutePath();
        }
        return directory;
    }

    public void openFileDirectory() {
        String a="output.txt";
        try {
            Runtime.getRuntime().exec("cmd /c start "+a);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Initiate processing methods in hospitalPathFinder.java
    public String processData() throws FileNotFoundException{
        App a = new App();
        a.setkValue(Integer.parseInt(kValueTF.getText()));
        a.setGraphDirectory(lblgraphFileDirectory.getText());
        a.setHospDirectory(lblHospFileDirectory.getText());
        String output = hospitalPathFinder.main(a);
        return output;
    }

    // For ease of testing only // to be deleted
    public String processData2()throws FileNotFoundException {
        System.out.println("a");
        App a = new App();
        a.setGraphDirectory("file1.txt");
        a.setHospDirectory("file2.txt");
        a.setkValue(Integer.parseInt(kValueTF.getText()));
        String output = hospitalPathFinder.main(a);
        return output;
    }


    // get - set methods
    public String getGraphDirectory() {
        return graphDirectory;
    }

    public void setGraphDirectory(String graphDirectory) {
        this.graphDirectory = graphDirectory;
    }

    public String getHospDirectory() {
        return hospDirectory;
    }

    public void setHospDirectory(String hospDirectory) {
        this.hospDirectory = hospDirectory;
    }

    public int getkValue() {
        return kValue;
    }

    public void setkValue(int kValue) {
        this.kValue = kValue;
    }
}
