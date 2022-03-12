import net.lingala.zip4j.core.ZipFile;
import java.io.File;

public class DecryptorThread extends Thread{

    private final File file;
    private final String password;
    private final GUIform guIform;

    public DecryptorThread(File file, String password, GUIform guIform){
        this.file = file;
        this.password = password;
        this.guIform = guIform;
    }

    @Override
    public void run() {
        String out = getOutputPath();
        try {
            guIform.progressBar.setValue(0);
            ZipFile zipFile = new ZipFile(file);
            zipFile.setPassword(password);
            zipFile.extractAll(out);
            guIform.term.insert("\nDecrypting file...", guIform.term.getText().length());
            guIform.term.insert("\nComplete! File decrypted!", guIform.term.getText().length());
            guIform.progressBar.setValue(100);
        } catch (Exception e){
            if(e.getMessage().contains("Wrong Password")){
                guIform.term.insert("\nError: Wrong password", guIform.term.getText().length());
            } else {
                guIform.term.insert("\nError: " + e.getMessage(), guIform.term.getText().length());
                guIform.clearField();
                e.printStackTrace();
            }
        }
    }

    private String getOutputPath(){
        String path = file.getAbsolutePath().replaceAll("\\.enc$", "");
        for(int i = 0;;i++){
            String number = i > 0 ? Integer.toString(i) : "";
            String outPath = path + number;
            if(!new File(path + number).exists()){
                return outPath;
            }

        }
    }

}
