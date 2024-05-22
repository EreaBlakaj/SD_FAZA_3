import java.net.*;
import java.io.*;
import java.security.*;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class GreetingServer {
    private static KeyPair keyPair;

    public static byte[] encryptAES(String message, byte[] key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return cipher.doFinal(message.getBytes());
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    public static String signMessage(byte[] message, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature rsa = Signature.getInstance("SHA256withRSA");
        rsa.initSign(privateKey);
        rsa.update(message);
        byte[] signature = rsa.sign();
        return Base64.getEncoder().encodeToString(signature);
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

    public static void main(String[] args) throws IOException {
        try {
            int port = 8088;
            keyPair = generateKeyPair();  // Generate RSA key pair

            int b = 3;  // Server's private key

            double clientP, clientG, clientA, B, Bdash;
            String Bstr;

            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            System.out.println("Just connected to " + server.getRemoteSocketAddress());

            DataInputStream in = new DataInputStream(server.getInputStream());

            clientP = Double.parseDouble(in.readUTF()); // to accept p
            System.out.println("From Client : P = " + clientP);

            clientG = Double.parseDouble(in.readUTF()); // to accept g
            System.out.println("From Client : G = " + clientG);

            clientA = Double.parseDouble(in.readUTF()); // to accept A
            System.out.println("From Client : Public Key = " + clientA);

            B = ((Math.pow(clientG, b)) % clientP); // calculation of B
            Bstr = Double.toString(B);

            OutputStream outToClient = server.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToClient);

            out.writeUTF(Bstr); // Sending B

            Bdash = ((Math.pow(clientA, b)) % clientP); // calculation of Bdash
            System.out.println("Secret Key to perform Symmetric Encryption = " + Bdash);

            byte[] sharedSecretKey = hashSharedSecret(Bdash); // Properly hashed shared secret for AES key
            byte[] encryptedMessage = encryptAES("Hellofromtheserver", sharedSecretKey);
            String encryptedMessageBase64 = Base64.getEncoder().encodeToString(encryptedMessage);

            System.out.println("Encrypted message (Base64): " + encryptedMessageBase64);

            // Hash the encrypted message before signing
            byte[] hashedMessage = hashMessage(encryptedMessage);

            System.out.println("Hashed message (Base64): " + Base64.getEncoder().encodeToString(hashedMessage));

            // Sign the hashed message
            String signature = signMessage(hashedMessage, keyPair.getPrivate());

            System.out.println("Signature: " + signature);

            // Send the encrypted message, signature, and public key
            PrintWriter outToClientMsg = new PrintWriter(server.getOutputStream(), true);
            outToClientMsg.println(encryptedMessageBase64);
            outToClientMsg.println(signature);
            outToClientMsg.println(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));

            server.close();
        } catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
