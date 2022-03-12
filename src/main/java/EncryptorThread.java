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
            guIform.progressBar.setValue(0);
            guIform.setEncType();
            guIform.setCompType();

            String archiveName = getArchiveName();
            ZipFile zipFile = new ZipFile(archiveName);

            guIform.term.insert("\nEncrypting file... \n(Encryption: "
                    + getEncName() + "; Compression: "
                    + getCompName() + ")", guIform.term.getText().length());

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

    private String getEncName(){
        if (parameters.getEncryptionMethod() == 0){
            return "standart encryption";
        } else if (parameters.getEncryptionMethod() == 99){
            switch (parameters.getAesKeyStrength()) {
                case 1:
                    return "AES_128";
                case 3:
                    return "AES_256";
            }
        }
        return "";
    }

    private String getCompName(){
        if (parameters.getCompressionLevel() == 1){
            return "fastest";
        } else if(parameters.getCompressionLevel() == 3){
            return "fast";
        } else if(parameters.getCompressionLevel() == 5){
            return "normal";
        } else if(parameters.getCompressionLevel() == 7){
            return "maximum";
        } else if(parameters.getCompressionLevel() == 9){
            return "ultra";
        }

        return "";
    }

    private void onFinish(){parameters.setPassword("");}
}
