import java.net.*;
import java.io.*;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class GreetingClient {
    public static String decryptAES(byte[] encryptedMessage, byte[] key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedMessage);
        return new String(decryptedBytes);
    }

    public static boolean verifySignature(byte[] message, String signature, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature rsa = Signature.getInstance("SHA256withRSA");
        rsa.initVerify(publicKey);
        rsa.update(message);
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return rsa.verify(signatureBytes);
    }

    public static PublicKey getPublicKeyFromBase64(String base64PublicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    public static byte[] hashSharedSecret(double sharedSecret) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(Double.toString(sharedSecret).getBytes());
        byte[] key = new byte[16]; // Use the first 16 bytes (128 bits) for AES key
        System.arraycopy(hash, 0, key, 0, 16);
        return key;
    }

    public static byte[] hashMessage(byte[] message) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(message);
    }

    public static void main(String[] args) {
        try {
            String pstr, gstr, Astr;
            String serverName = "localhost";
            int port = 8088;

            int p = 23;
            int g = 9;
            int a = 4;
            double Adash, serverB;

            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());

            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            pstr = Integer.toString(p);
            out.writeUTF(pstr); // Sending p

            gstr = Integer.toString(g);
            out.writeUTF(gstr); // Sending g

            double A = ((Math.pow(g, a)) % p); // calculation of A
            Astr = Double.toString(A);
            out.writeUTF(Astr); // Sending A

            System.out.println("From Client : Private Key = " + a);

            DataInputStream inFromServer = new DataInputStream(client.getInputStream());

            serverB = Double.parseDouble(inFromServer.readUTF());
            System.out.println("From Server : Public Key = " + serverB);

            Adash = ((Math.pow(serverB, a)) % p); // calculation of Adash
            System.out.println("Secret Key to perform Symmetric Encryption = " + Adash);

            BufferedReader inFromServerMsg = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String encryptedMessageBase64 = inFromServerMsg.readLine();
            String signature = inFromServerMsg.readLine();
            String base64PublicKey = inFromServerMsg.readLine();

            PublicKey publicKey = getPublicKeyFromBase64(base64PublicKey);

            byte[] encryptedMessage = Base64.getDecoder().decode(encryptedMessageBase64);

            byte[] hashedMessage = hashMessage(encryptedMessage);

            boolean isVerified = verifySignature(hashedMessage, signature, publicKey);
            if (isVerified) {
                System.out.println("Signature valid. Trusted communication established.");
                byte[] sharedSecretKey = hashSharedSecret(Adash);
                String decryptedMessage = decryptAES(encryptedMessage, sharedSecretKey);
                System.out.println("Decrypted message from server: " + decryptedMessage);
            } else {
                System.out.println("Signature invalid. Communication not trusted.");
            }

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
