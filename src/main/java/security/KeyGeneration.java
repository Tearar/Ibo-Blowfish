package security;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;

public class KeyGeneration {

    public KeyGeneration() {

    }

    public void generateBlowfishKeys() throws Exception {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
        keyGenerator.init(32);

        SecretKey key = keyGenerator.generateKey();

        System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
    }


}
