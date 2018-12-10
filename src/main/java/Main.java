import security.KeyGeneration;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) throws Exception {

        //Reads a .pcapng file
        ArrayList<String> packetList = ReadDump.getDump();

        String plaintextHeader = "<?xml ve";
        HashMap<String, SecretKeySpec> keyMap = new HashMap<>();

        KeyGeneration keygen = new KeyGeneration();
        System.out.println("Encrypting plaintext...");

        //Key generation and encryption
        for (int i = 0; i < Math.pow(2, 16); i++) {
            SecretKey secretKey = keygen.generateBlowfishKey();
            SecretKeySpec key = new SecretKeySpec(secretKey.getEncoded(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte [] encrypted = cipher.doFinal(plaintextHeader.getBytes());

            String s = getByteStream(encrypted);
            keyMap.put(s, key);

        }
        int countMatches = 0;
        //Searching for matches, decryption if a match was found
        for (String packet : packetList){
            String completePacket = packet.substring(84);
            packet = packet.substring(84, 100);
            if (keyMap.containsKey(packet)){
                System.out.println("Found: " + keyMap.get(packet));
                Cipher cipher = Cipher.getInstance("Blowfish");
                cipher.init(Cipher.DECRYPT_MODE, keyMap.get(packet));
                byte [] decrypted = cipher.doFinal(hexStringToByteArray(completePacket));
                System.out.println(new String(decrypted));
                countMatches++;

            }
        }
        System.out.println("Found matches: " + countMatches);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String getByteStream(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
