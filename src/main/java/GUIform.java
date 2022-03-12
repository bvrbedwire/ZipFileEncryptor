import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class GUIform {
    protected JPanel panel1;
    private JTextField filePath;
    private JButton openButton;
    protected JTextArea term;
    private JButton decryptButton;
    private JButton encryptButton;
    protected JProgressBar progressBar;
    private JScrollPane scroll;

    private File selectedFile;

    ZipParameters parameters = new ZipParameters();

    public GUIform(){
        filePath.setBorder(new LineBorder(new Color(93, 89, 86), 1));
        term.setBorder(new LineBorder(new Color(93, 89, 86), 0));
        scroll.setBorder(new LineBorder(new Color(93, 89, 86), 1));

        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);

        Font font = new Font("Noto Sans Mono", Font.PLAIN, 12);
        UIManager.put("OptionPane.messageFont", font);

        openButton.addActionListener(new Action() {
            @Override
            public Object getValue(String s) {return null;}
            @Override
            public void putValue(String s, Object o) {}
            @Override
            public void setEnabled(boolean b) {}
            @Override
            public boolean isEnabled() {return false;}
            @Override
            public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {}
            @Override
            public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {}

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.showOpenDialog(panel1);
                selectedFile = chooser.getSelectedFile();
                progressBar.setValue(0);

                if (selectedFile == null){
                    clearField();
                    return;
                }

                filePath.setText(selectedFile.getAbsolutePath());

                try {
                    ZipFile zipFile = new ZipFile(selectedFile);

                    if (zipFile.isValidZipFile() && zipFile.isEncrypted()){
                        decryptButton.setEnabled(true);
                        encryptButton.setEnabled(false);
                    } else {
                        decryptButton.setEnabled(false);
                        encryptButton.setEnabled(true);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        encryptButton.addActionListener(new Action() {
            @Override
            public Object getValue(String s) {return null;}
            @Override
            public void putValue(String s, Object o) {}
            @Override
            public void setEnabled(boolean b) {}
            @Override
            public boolean isEnabled() {return false;}
            @Override
            public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {}
            @Override
            public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {}

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(selectedFile == null){return;}

                String password = getPassword();

                if (password == null || password.length() == 0){
                    term.insert("\nError: password doesn't set", term.getText().length());
                    return;
                }
                encryptButton.setEnabled(false);
                encryptFile(password);
                encryptButton.setEnabled(true);
            }
        });

        decryptButton.addActionListener(new Action() {
            @Override
            public Object getValue(String s) {return null;}
            @Override
            public void putValue(String s, Object o) {}
            @Override
            public void setEnabled(boolean b) {}
            @Override
            public boolean isEnabled() {return false;}
            @Override
            public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {}
            @Override
            public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {}

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(selectedFile == null){return;}

                String password = getPassword();

                if (password == null || password.length() == 0){
                    term.insert("\nError: password doesn't set", term.getText().length());
                    return;
                }
                decryptButton.setEnabled(false);
                decryptFile(password);
                decryptButton.setEnabled(true);
            }
        });
    }

    public JPanel getRootPanel(){
        return panel1;
    }

    private void encryptFile(String password){
        EncryptorThread thread = new EncryptorThread(selectedFile, parameters, password, this);
        thread.start();
    }

    private void decryptFile(String password){
        DecryptorThread thread = new DecryptorThread(selectedFile, password, this);
        thread.start();
    }

    private static String getPassword(){
        return JOptionPane.showInputDialog("Enter the password: ");
    }

    protected void clearField(){
        decryptButton.setEnabled(false);
        encryptButton.setEnabled(false);
        filePath.setText("");
    }
}
