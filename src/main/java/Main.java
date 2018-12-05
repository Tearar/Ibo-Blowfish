import security.KeyGeneration;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<String> packetList = ReadDump.getDump();
        //byte [] plaintextHeader = hexStringToByteArray("<?xml version=");
        String plaintextHeader = "<?xml v"; //Sollten 8 Zeichen sein (2Byte pro Zeichen *8 = 64 bit Blockgröße bei Blowfish)
        HashMap<String, String> keyMap = new HashMap<>();

        KeyGeneration keygen = new KeyGeneration();
        for (int i = 0; i < Math.pow(2, 16); i++) {
            SecretKey key = keygen.generateBlowfishKeys();
            Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte [] encrypted = cipher.doFinal(plaintextHeader.getBytes("UTF8"));

            String s = getByteStream(encrypted);
            keyMap.put(s, Base64.getEncoder().encodeToString(key.getEncoded()));

            // TODO encrypt all plaintext headers 2^16 times
            // TODO save (ciphertext, key) pairs in hashtables
        }
        int countPackets = 0;
        int countMatches = 0;
        for (String packet : packetList){
            packet = packet.substring(84, 100);
            if (keyMap.containsKey(packet) == true){
                System.out.println("Found: " + keyMap.get(packet));
                countMatches++;
            }

            System.out.println("Packet " + countPackets + ": " + packet);
            countPackets++;
        }

        System.out.println("Found matches: " + countMatches);
    }

    /*public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }*/

    public static String getByteStream(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
