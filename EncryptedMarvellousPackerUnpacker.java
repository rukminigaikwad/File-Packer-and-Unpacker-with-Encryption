///////////////////////////////////////////////////////////////////////
//
//  Function Name :     encrypt
//  Description   :     Encrypts a given byte array using AES encryption.
//  Input         :     byte[] data – Plain file data to be encrypted.
//  Output        :     byte[] – Encrypted file data.
//  Author        :     Rukmini Gaikwad
//  Date          :     17/7/2025
//
///////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

class EncryptedPacker
{
    private String DirectoryName;
    private String PackFileName;
    private static final String SECRET_KEY = "Marvellous123456"; // 16-char AES key

    public EncryptedPacker(String dir, String packName)
    {
        this.DirectoryName = dir;
        this.PackFileName = packName;
    }

    private byte[] encrypt(byte[] data) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public void PackingActivity()
    {
        File dirObj = new File(DirectoryName);
        FileOutputStream outStream = null;
        int iCountFile = 0;

        try
        {
            System.out.println("--------------------------------------------------------");
            System.out.println("---------- Encrypted Marvellous Packer -----------------");
            System.out.println("--------------------------------------------------------");
            System.out.println("------------------- Packing Activity -------------------");
            System.out.println("--------------------------------------------------------");

            if(!dirObj.exists() || !dirObj.isDirectory())
            {
                System.out.println("Invalid Directory Name.");
                return;
            }

            File packObj = new File(PackFileName);
            packObj.createNewFile();
            outStream = new FileOutputStream(packObj);

            File files[] = dirObj.listFiles();

            System.out.println("Scanning directory: " + DirectoryName);
            System.out.println("--------------------------------------------------------");

            for(File file : files)
            {
                if(file.isFile())
                {
                    String Header = file.getName() + " " + file.length();
                    for(int i = Header.length(); i < 100; i++)
                    {
                        Header += " ";
                    }

                    byte headerBytes[] = Header.getBytes();
                    outStream.write(headerBytes, 0, headerBytes.length);

                    FileInputStream inStream = new FileInputStream(file);
                    byte buffer[] = new byte[(int)file.length()];
                    inStream.read(buffer, 0, buffer.length);

                    // Encrypt file data before writing
                    byte encryptedData[] = encrypt(buffer);
                    outStream.write(encryptedData, 0, encryptedData.length);
                    inStream.close();

                    System.out.println("Packed (Encrypted) file : " + file.getName() + " (" + file.length() + " bytes)");
                    iCountFile++;
                }
            }

            System.out.println("--------------------------------------------------------");
            System.out.println("Total files packed & encrypted : " + iCountFile);
            System.out.println("Packed file created : " + PackFileName);
            System.out.println("--------------------------------------------------------");
            System.out.println("--------- Thank you for using our application ----------");
            System.out.println("--------------------------------------------------------");

            outStream.close();
        }
        catch(Exception e)
        {
            System.out.println("Error during packing: " + e);
        }
    }
}

///////////////////////////////////////////////////////////////////////
//
//  Function Name :     decrypt
//  Description   :     Decrypts a given byte array using AES decryption.
//  Input         :     byte[] data – Encrypted file data.
//  Output        :     byte[] – Decrypted (original) file data.
//
///////////////////////////////////////////////////////////////////////

class EncryptedUnpacker
{
    private String PackName;
    private static final String SECRET_KEY = "Marvellous123456"; // Same key as packer

    public EncryptedUnpacker(String A)
    {
        this.PackName = A;
    }

    private byte[] decrypt(byte[] data) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

///////////////////////////////////////////////////////////////////////
//
//  Function Name :     UnpackingActivity
//  Description   :     Extracts and decrypts individual files from the 
//                      packed encrypted file created by EncryptedPacker.
//  Input         :     None (Uses instance variable PackName)
//  Output        :     Creates decrypted original files in the current directory.
//
////////////////////////////////////////////////////////////////////////

    public void UnpackingActivity()
    {
        try
        {
            System.out.println("--------------------------------------------------------");
            System.out.println("---------- Encrypted Marvellous Unpacker ---------------");
            System.out.println("--------------------------------------------------------");
            System.out.println("----------------- UnPacking Activity -------------------");
            System.out.println("--------------------------------------------------------");

            File fobj = new File(PackName);

            if(!fobj.exists())
            {
                System.out.println("Unable to access Packed file");
                return;
            }

            System.out.println("Packed file successfully opened.");

            FileInputStream fiobj = new FileInputStream(fobj);
            byte HeaderBuffer[] = new byte[100];
            int iRet = 0, iCountFile = 0;

            while((iRet = fiobj.read(HeaderBuffer, 0, 100)) != -1)
            {
                String Header = new String(HeaderBuffer).trim();
                if(Header.isEmpty()) break;

                String Tokens[] = Header.split(" ");
                String FileName = Tokens[0];
                int FileSize = Integer.parseInt(Tokens[1]);

                // Note: File data was encrypted, so we must read encrypted bytes
                byte EncryptedBuffer[] = new byte[FileSize];
                fiobj.read(EncryptedBuffer, 0, FileSize);

                // Decrypt data
                byte DecryptedData[] = decrypt(EncryptedBuffer);

                File newFile = new File(FileName);
                FileOutputStream foobj = new FileOutputStream(newFile);
                foobj.write(DecryptedData, 0, DecryptedData.length);
                foobj.close();

                System.out.println("Unpacked & Decrypted file : " + FileName + " (" + FileSize + " bytes)");
                iCountFile++;
            }

            System.out.println("--------------------------------------------------------");
            System.out.println("Total files unpacked & decrypted : " + iCountFile);
            System.out.println("--------------------------------------------------------");
            System.out.println("--------- Thank you for using our application ----------");
            System.out.println("--------------------------------------------------------");

            fiobj.close();
        }
        catch(Exception e)
        {
            System.out.println("Error during unpacking: " + e);
        }
    }
}

///////////////////////////////////////////////////////////////////////
//
//  Function Name :     main
//  Description   :     Provides a menu-driven interface to perform
//                      either packing/encryption or unpacking/decryption.
//  Input         :     String[] args – Command-line arguments.
//  Output        :     Executes user's selected packing or unpacking operation.
//
//////////////////////////////////////////////////////////////////////

public class EncryptedMarvellousPackerUnpacker
{
    public static void main(String A[])
    {
        Scanner sobj = new Scanner(System.in);

        try
        {
            System.out.println("--------------------------------------------------------");
            System.out.println("-------- Encrypted Marvellous Packer Unpacker ----------");
            System.out.println("--------------------------------------------------------");
            System.out.println("1 : Pack & Encrypt files from directory");
            System.out.println("2 : Unpack & Decrypt files from packed file");
            System.out.println("--------------------------------------------------------");
            System.out.print("Enter your choice : ");

            int choice = sobj.nextInt();
            sobj.nextLine(); // clear buffer

            switch(choice)
            {
                case 1:
                    System.out.print("Enter directory name to pack : ");
                    String dirName = sobj.nextLine();

                    System.out.print("Enter name for packed file : ");
                    String packedFile = sobj.nextLine();

                    EncryptedPacker pobj = new EncryptedPacker(dirName, packedFile);
                    pobj.PackingActivity();
                    break;

                case 2:
                    System.out.print("Enter the name of packed file to unpack : ");
                    String packName = sobj.nextLine();

                    EncryptedUnpacker uobj = new EncryptedUnpacker(packName);
                    uobj.UnpackingActivity();
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e);
        }
        finally
        {
            sobj.close();
        }
    }
}
