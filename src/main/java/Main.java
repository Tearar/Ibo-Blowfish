import security.KeyGeneration;


public class Main {
    public static void main(String[] args) throws Exception {
        KeyGeneration keygen = new KeyGeneration();
        for (int i = 0; i < Math.pow(2, 16); i++) {
            keygen.generateBlowfishKeys();
            // TODO encrypt all plaintext headers 2^16 times
            // TODO save (ciphertext, key) pairs in hashtables
        }
    }
}
