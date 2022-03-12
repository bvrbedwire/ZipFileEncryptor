import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;

import java.io.File;

public class EncryptorThread extends Thread{

    private final File file;
    private final ZipParameters parameters;
    private final GUIform guIform;

    public EncryptorThread(File file, ZipParameters parameters, String password, GUIform guIform){
        this.file = file;
        this.parameters = parameters;
        this.guIform = guIform;
        parameters.setPassword(password);
    }

    @Override
    public void run() {
        try{
            String archiveName = getArchiveName();
            ZipFile zipFile = new ZipFile(archiveName);
            guIform.term.insert("\nEncrypting file...", guIform.term.getText().length());
            if(file.isDirectory()){
                zipFile.addFolder(file, parameters);
            } else {
                zipFile.addFile(file, parameters);
            }
            guIform.term.insert("\nComplete! File encrypted!", guIform.term.getText().length());
            guIform.progressBar.setValue(100);
        } catch (Exception e){
            e.printStackTrace();
        }
        onFinish();
    }

    private String getArchiveName(){
        for(int i = 0;;i++){
            String number = i > 0 ? Integer.toString(i) : "";
            String archiveName = file.getAbsolutePath() + number + ".enc";
            if(!new File(archiveName).exists()){
                return archiveName;
            }
        }
    }

    private void onFinish(){parameters.setPassword("");}
}
