package com.daizhihua.core.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加解密工具类，实现公钥加密私钥解密和私钥解密公钥解密
 */
public class RsaUtils {

    private static final String src = "123456";

    private static String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+vf/5GS3fIjN3iYc5h1QcqAWjYXfJFdvPGhqguHnkBjd9m2pOGapu/F9cGzCthT3sEj1qgPd9qJA6PovOuykkyHu4/lvg61RcoN/pjqqlGxUIMTBpDt14WLWIEY0a+cQwwyWMCgEIUfgdSBHmPW6hZhk1McRVzd8qIlqpcUYKhQIDAQAB";
    private static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL69//kZLd8iM3eJhzmHVByoBaNhd8kV288aGqC4eeQGN32bak4Zqm78X1wbMK2FPewSPWqA932okDo+i867KSTIe7j+W+DrVFyg3+mOqqUbFQgxMGkO3XhYtYgRjRr5xDDDJYwKAQhR+B1IEeY9bqFmGTUxxFXN3yoiWqlxRgqFAgMBAAECgYEAmQLX/Z6qQq4v9TuZA5hA7EAJTdgZfHERhKCfLcDu3vHxv8xVydMi+qdvY/bX5MrXMrIrHG3am64GDQMxqiRPyrLB0s2lvymirJCWgug022KQbFRcyw5Delfex3rCPNhk+5KszV+SVtODGRDjbTS2OpZNz3Yt0IjTOKf3kcFmREECQQD0jveQ+D7muxXvnqY7S0lwdoYCB3FAXcb1+IGZ3v/OH+J2R3HvmmfprDEXMd6AcLqiIPUY0Yfl5vrHBFSxUSORAkEAx6p8I1ZV3jz9a8SX/J19Lj7R8QVRIsfuw1ZbUcNoKuNNPEuNOp+MVN4yeed+nV4f5BTkuQx7bzz/Te98Qq0VtQJAU5lnsYva3L7Jcd8WziAfW614g8sNgMZN1Bl+HB5p7YlivbIQlap/qRZutZIbkGZ4tiF0B2bhAMsjoNKvLOoisQJAU6m/LHtvrZi2w6Jz4RkIrAkMpUaKEd3e0SDtUNxlWJs38Mzjl63k+mbElcoHht8607JhiJyPWDQh8kEoOzQVhQJAD2E1ofiZdM38lSFrLJM4wmmEzuCplA1UsdDYjJbYkLGkV/fDI+UFn87GPtOgzTej4kNpV3AunbWyU8xO4trGSA==";


    public static void main(String[] args) throws Exception {
        System.out.println("\n");
        RSAKeyPair keyPair = generateKeyPair();
        System.out.println("公钥：" + publickey);
        System.out.println("私钥：" + privateKey);
        System.out.println("\n");
        test1(keyPair, src);
        System.out.println("\n");
        test2(keyPair, src);
        System.out.println("\n");
    }

    /**
     * 公钥加密私钥解密
     */
    private static void test1(RSAKeyPair keyPair, String source) throws Exception {
        System.out.println("***************** 公钥加密私钥解密开始 *****************");

        String text1 = encryptByPublicKey(publickey, source);
        String text2 = decryptByPrivateKey(privateKey, text1);
        System.out.println("加密前：" + source);
        System.out.println("加密后：" + text1);
        System.out.println("解密后：" + text2);
        if (source.equals(text2)) {
            System.out.println("解密字符串和原始字符串一致，解密成功");
        } else {
            System.out.println("解密字符串和原始字符串不一致，解密失败");
        }
        System.out.println("***************** 公钥加密私钥解密结束 *****************");
    }

    /**
     * 私钥加密公钥解密
     *
     * @throws Exception
     */
    private static void test2(RSAKeyPair keyPair, String source) throws Exception {
        System.out.println("***************** 私钥加密公钥解密开始 *****************");
        String text1 = encryptByPrivateKey(privateKey, source);
        String text2 = decryptByPublicKey(publickey, text1);
        System.out.println("加密前：" + source);
        System.out.println("加密后：" + text1);
        System.out.println("解密后：" + text2);
        if (source.equals(text2)) {
            System.out.println("解密字符串和原始字符串一致，解密成功");
        } else {
            System.out.println("解密字符串和原始字符串不一致，解密失败");
        }
        System.out.println("***************** 私钥加密公钥解密结束 *****************");
    }

    /**
     * 公钥解密
     *
     * @param publicKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String publicKeyText, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 私钥加密
     *
     * @param privateKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String privateKeyText, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String privateKeyText, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyText
     * @param text
     * @return
     */
    public static String encryptByPublicKey(String publicKeyText, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 构建RSA密钥对
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static RSAKeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = Base64.encodeBase64String(rsaPublicKey.getEncoded());
        String privateKeyString = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
        RSAKeyPair rsaKeyPair = new RSAKeyPair(publicKeyString, privateKeyString);
        return rsaKeyPair;
    }


    /**
     * RSA密钥对对象
     */
    public static class RSAKeyPair {

        private String publicKey;
        private String privateKey;

        public RSAKeyPair(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

    }

}