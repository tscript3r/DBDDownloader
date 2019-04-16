package pl.tscript3r.dbdd.windows;

import pl.tscript3r.dbdd.pojos.Settings;
import pl.tscript3r.dbdd.utils.DBDDownloader;
import pl.tscript3r.dbdd.utils.Logger;
import pl.tscript3r.dbdd.utils.SettingsLoader;
import pl.tscript3r.dbdd.utils.SystemClipboard;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static pl.tscript3r.dbdd.utils.Logger.print;
import static pl.tscript3r.dbdd.utils.Logger.setTextArea;

public class MainWindow {

    private final String DAILY_PATTERN = "%s	%s	BS	Billing as time/material: 5 Min.		%s	-	-	x	-	-	-	-	Check for DBD.";

    private SettingsLoader settingsLoader;
    private JFrame frmDbdd;
    private JTextField hostnameField;
    private JTextField savePathField;
    private JTextField ticketField;
    private JComboBox<String> comboBox;
    private DBDDownloader dbdd;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SettingsLoader settingsLoader = new SettingsLoader();
                    MainWindow window = new MainWindow(settingsLoader);
                    window.frmDbdd.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private MainWindow(SettingsLoader settingsLoader) {
        this.settingsLoader = settingsLoader;
        initialize();
    }

    private static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    private String spaceEraser(String input) {
        return input.replace(" ", "");
    }

    private FocusAdapter getSpaceEraserLostFocusAdapter(final JTextField textField) {
        return new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                textField.setText(spaceEraser(textField.getText()));
            }
        };
    }

    /**
     * Initialise the contents of the frame.
     */
    private void initialize() {
        Settings settings = settingsLoader.getSettings();

        frmDbdd = new JFrame();
        frmDbdd.setTitle("DBDD");
        frmDbdd.setBounds(100, 100, 437, 479);
        frmDbdd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmDbdd.getContentPane().setLayout(new BorderLayout(0, 0));
        frmDbdd.setResizable(false);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frmDbdd.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frmDbdd.getHeight()) / 2);

        frmDbdd.setLocation(x, y);

        JPanel panel = new JPanel();
        panel.setBounds(10, 0, 319, 177);
        panel.setBackground(Color.WHITE);
        frmDbdd.getContentPane().add(panel);
        panel.setLayout(null);

        JScrollPane scroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(10, 226, 412, 130);
        panel.add(scroll);

        JTextArea taOutput = new JTextArea();
        taOutput.setWrapStyleWord(true);
        taOutput.setLineWrap(true);
        scroll.setViewportView(taOutput);
        taOutput.setEditable(false);
        taOutput.updateUI();
        DefaultCaret caret = (DefaultCaret) taOutput.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        setTextArea(taOutput); // adding jTextArea reference to the custom static logger

        JLabel lblHostname = new JLabel("Hostname:");
        lblHostname.setBounds(10, 10, 219, 14);
        panel.add(lblHostname);

        hostnameField = new JTextField();
        hostnameField.setBounds(10, 23, 412, 28);
        hostnameField.addFocusListener(getSpaceEraserLostFocusAdapter(hostnameField));
        hostnameField.setText(settings.getHostname());
        hostnameField.setColumns(10);
        panel.add(hostnameField);

        JLabel lblSavePath = new JLabel("Save path:");
        lblSavePath.setBounds(10, 58, 208, 23);
        panel.add(lblSavePath);

        savePathField = new JTextField();
        savePathField.setBounds(10, 75, 412, 28);
        panel.add(savePathField);
        savePathField.setText(settings.getSavePath());
        savePathField.setColumns(10);

        JButton btnExecute = new JButton("Download DBD");
        btnExecute.setBounds(10, 400, 412, 28);
        panel.add(btnExecute);
        Logger.setButtonExecute(btnExecute);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setBounds(10, 431, 412, 9);
        panel.add(progressBar);
        Logger.setProgressBar(progressBar);

        JLabel lblProject = new JLabel("Project:");
        lblProject.setBounds(10, 114, 208, 23);
        panel.add(lblProject);
        comboBox = settings.getProjectsComboBox();
        comboBox.setBounds(10, 132, 412, 28);
        comboBox.setEditable(true);
        panel.add(comboBox);

        JLabel lblTicket = new JLabel("Ticket:");
        lblTicket.setBounds(10, 170, 208, 23);
        panel.add(lblTicket);

        ticketField = new JTextField();
        ticketField.setText(settings.getTicket());
        ticketField.setColumns(10);
        ticketField.setBounds(10, 187, 412, 28);
        ticketField.addFocusListener(getSpaceEraserLostFocusAdapter(ticketField));
        panel.add(ticketField);

        JButton btnCopyDaily = new JButton("Copy Daily");
        btnCopyDaily.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                SystemClipboard.copy(String.format(DAILY_PATTERN, String.valueOf(comboBox.getSelectedItem()),
                        ticketField.getText(), hostnameField.getText()));
                print("Daily copied");
                try {
                    updateSettings();
                } catch (IOException e) {
                    print(getStackTrace(e));
                }
            }

        });
        btnCopyDaily.setBounds(10, 370, 412, 28);
        panel.add(btnCopyDaily);

        try {
            dbdd = new DBDDownloader();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            Logger.enableUI();
            print(getStackTrace(e));
        }

        btnExecute.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                try {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                dbdd.execute(hostnameField.getText(), savePathField.getText());
                                updateSettings();
                            } catch (IOException e) {
                                Logger.enableUI();
                                print(getStackTrace(e));
                            }
                        }

                    }).start();
                } catch (Exception e) {
                    print(getStackTrace(e));
                }
            }

        });
    }

    private void updateSettings() throws IOException {
        Settings settings = settingsLoader.getSettings();
        settings.setHostname(hostnameField.getText());
        settings.setTicket(ticketField.getText());
        settings.setSavePath(savePathField.getText());
        settings.setProjectsComboBox(comboBox);
        settingsLoader.save();
    }

}
