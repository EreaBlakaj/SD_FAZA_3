import java.net.*;
import java.io.*;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class GreetingClient {
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public static String decrypt(String cipherText, int shiftKey) {
        cipherText = cipherText.toLowerCase();
        StringBuilder message = new StringBuilder();
        for (int ii = 0; ii < cipherText.length(); ii++) {
            int charPosition = ALPHABET.indexOf(cipherText.charAt(ii));
            int keyVal = (charPosition - shiftKey) % 26;
            if (keyVal < 0) {
                keyVal = ALPHABET.length() + keyVal;
            }
            char replaceVal = ALPHABET.charAt(keyVal);
            message.append(replaceVal);
        }
        return message.toString();
    }

    public static boolean verifySignature(String message, String signature, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature rsa = Signature.getInstance("SHA256withRSA");
        rsa.initVerify(publicKey);
        rsa.update(message.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return rsa.verify(signatureBytes);
    }

    public static PublicKey getPublicKeyFromBase64(String base64PublicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    public static void main(String[] args) {
        try {
            String pstr, gstr, Astr;
            String serverName = "localhost";
            int port = 8088;

            // Declare p, g, and Key of client
            int p = 23;
            int g = 9;
            int a = 4;
            double Adash, serverB;

            // Established the connection
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());

            // Sends the data to client
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            pstr = Integer.toString(p);
            out.writeUTF(pstr); // Sending p

            gstr = Integer.toString(g);
            out.writeUTF(gstr); // Sending g

            double A = ((Math.pow(g, a)) % p); // calculation of A
            Astr = Double.toString(A);
            out.writeUTF(Astr); // Sending A

            // Client's Private Key
            System.out.println("From Client : Private Key = " + a);

            // Accepts the data
            DataInputStream inFromServer = new DataInputStream(client.getInputStream());

            serverB = Double.parseDouble(inFromServer.readUTF());
            System.out.println("From Server : Public Key = " + serverB);

            Adash = ((Math.pow(serverB, a)) % p); // calculation of Adash

            System.out.println("Secret Key to perform Symmetric Encryption = " + Adash);

            // Create input stream to receive messages from the server
            BufferedReader inFromServerMsg = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Read encrypted message, signature, and public key from the server
            String encryptedMessage = inFromServerMsg.readLine();
            String signature = inFromServerMsg.readLine();
            String base64PublicKey = inFromServerMsg.readLine();

            // Get the server's public key
            PublicKey publicKey = getPublicKeyFromBase64(base64PublicKey);

            // Verify the signature
            boolean isVerified = verifySignature(encryptedMessage, signature, publicKey);
            if (isVerified) {
                System.out.println("Signature valid. Trusted communication established.");
                int i = (int) Adash;
                String decryptedMessage = decrypt(encryptedMessage, i);
                System.out.println("Encrypted message from server: " + encryptedMessage);
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
