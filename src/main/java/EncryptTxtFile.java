import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

public class EncryptTxtFile {
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EncryptTxtFile EncryptTxtFile = new EncryptTxtFile();

        //Example password, for demonstrative purposes.
        String password = new String("0123456789");

        EncryptTxtFile.setPassword(password);

        try {
            EncryptTxtFile.pack();
            System.out.println("File encrypted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pack() throws IOException {

        //Creating a sample txt file with random content
        String fileName = "clear_text_file.txt";
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit....";
        File txtFile = new File(fileName);
        FileOutputStream fos = new FileOutputStream(txtFile);
        fos.write(content.getBytes(StandardCharsets.UTF_8));
        fos.close();

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        // Below line is optional. AES 256 is used by default. You can override it to use AES 128. AES 192 is supported only for extracting.
        zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);

        List<File> filesToAdd = Arrays.asList(txtFile);
        String destinationZipFilePath = "txt_encrypted.zip";
        ZipFile zipFile = new ZipFile(destinationZipFilePath, this.password.toCharArray());
        zipFile.addFiles(filesToAdd, zipParameters);

        // Delete the text file
        txtFile.delete();
        // Closing the zip file...
        zipFile.close();
    }
}