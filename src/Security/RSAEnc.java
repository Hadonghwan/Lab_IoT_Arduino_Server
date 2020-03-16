package Security;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

public class RSAEnc {
	/*암호화*/
    public static String rsaEncryption(String plainData, String stringPublicKey) throws BadPaddingException,
            IllegalBlockSizeException, InvalidKeySpecException,
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        // 평문으로 전달받은 'String공개키'를 '공개키 객체'로 만드는 과정
        byte[] bytePublicKey = Base64.decodeBase64(stringPublicKey.getBytes());
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // 만들어진 공개키객체를 기반으로 암호화모드로 설정하는 과정
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey); // 암호화 준비

        // 암호화 진행
        byte[] byteEncryptedData = cipher.doFinal(plainData.getBytes());

        // 암호화 데이터, 인코딩 후 'String'으로 반환
        
        return Base64.encodeBase64String(byteEncryptedData);
    }

    /*복호화*/
    public static String rsaDecryption(String encryptedData, String stringPrivateKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        // 평문으로 전달받은 'String개인키'를 '개인키 객체'로 만드는 과정
        byte[] bytePrivateKey =  Base64.decodeBase64(stringPrivateKey.getBytes());
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        // 만들어진 개인키객체를 기반으로 복호화모드로 설정하는 과정
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey); // 복호화 준비

        // 암호화된 인코딩 데이터를 디코딩 진행
        byte[] byteEncryptedData = Base64.decodeBase64(encryptedData.getBytes());

        // 복호화 진행
        byte[] byteDecryptedData = cipher.doFinal(byteEncryptedData);

        // 복호화 후 'String'으로 반환
        return new String(byteDecryptedData);
    }
}
