package com.udc.master.tfm.tracksports.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import androidx.annotation.NonNull;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class performs encryption and decryption for plain texts.
 * Note that this class is <strong>not thread-safe</strong> so you have to lock calling methods
 * explicitly.
 * https://github.com/gfx/Android-EncryptUtils
 */
@SuppressLint("Assert")
public class CryptoUtils {

	/** The default private key */
	private static final String DEFAULT_PRIVATE_KEY = "vYvgTRUhtQB5mZ98";
	/** The default security provider, "AndroidOpenSSL", which is not available on Android 2.3.x. */
	private static final String DEFAULT_PROVIDER = "AndroidOpenSSL";
	/** The default algorithm mode, "AES/CBC/PKCS5Padding" */
    public static final String DEFAULT_ALGORITHM_MODE =  "AES/CBC/PKCS5Padding";
    
    private static final Charset CHARSET = Charset.forName("UTF-8");
    public static final int KEY_LENGTH = 128 / 8;
    
    private static SecretKeySpec secretKeySpec;
    private static Cipher cipher;
    
    private CryptoUtils() {}
    
    /**
     * Metodo para obtener la clase encargada de obtener el cifrado
     * @return
     */
    private static Cipher getCipher() {
    	if (cipher == null) {
    		cipher = getDefaultCipher();
    	}
    	return cipher;
    }
    
    /**
     * Metodo para obtener la clase secreta
     * @return
     */
    private static SecretKeySpec getSecretKey() {
    	if (secretKeySpec == null) {
    		secretKeySpec = createKeySpec(getCipher(), DEFAULT_PRIVATE_KEY.getBytes());
    	}
    	return secretKeySpec;
    }
    
    /**
     * @return A {@link Cipher} instance with "AES/CBC/PKC5Padding" transformation.
     */
    @NonNull
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static Cipher getDefaultCipher() {
        try {
            return Cipher.getInstance(DEFAULT_ALGORITHM_MODE, DEFAULT_PROVIDER);
        } catch (NoSuchProviderException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new AssertionError(e);
        }
    }

    @NonNull
    private static SecretKeySpec createKeySpec(@NonNull Cipher cipher, @NonNull byte[] privateKey) {
        if (privateKey.length < KEY_LENGTH) {
            throw new IllegalArgumentException("private key is too short."
                    + " Expected=" + KEY_LENGTH + " but got=" + privateKey.length);
        } else if (privateKey.length > KEY_LENGTH) {
            throw new IllegalArgumentException("private key is too long."
                    + " Expected=" + KEY_LENGTH + " but got=" + privateKey.length);
        }
        return new SecretKeySpec(privateKey, cipher.getAlgorithm());
    }

    /**
     * Metodo que encripta una contrasena
     * @param plainText
     * @return
     */
    @NonNull
    public static String encrypt(@NonNull String plainText) {
        byte[] encrypted;

        try {
            getCipher().init(Cipher.ENCRYPT_MODE, getSecretKey());
            encrypted = getCipher().doFinal(plainText.getBytes(CHARSET));
        } catch (Exception e) {
        	Log.e("Crypt", e.toString(), e);
            return null;
        }
        byte[] iv = getCipher().getIV();

        byte[] buffer = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, buffer, 0, iv.length);
        System.arraycopy(encrypted, 0, buffer, iv.length, encrypted.length);
        return Base64.encodeToString(buffer, Base64.NO_WRAP);
    }

    /**
     * Metodo que desencripta una contrasena
     * @param encrypted
     * @return
     */
    @NonNull
    public static String decrypt(@NonNull String encrypted) {
        byte[] buffer = Base64.decode(encrypted.getBytes(CHARSET), Base64.NO_WRAP);
        byte[] decrypted;

        try {
        	getCipher().init(Cipher.DECRYPT_MODE, getSecretKey(),
                    new IvParameterSpec(buffer, 0, KEY_LENGTH));
            decrypted = getCipher().doFinal(buffer, KEY_LENGTH, buffer.length - KEY_LENGTH);
        } catch (Exception e) {
        	Log.e("Crypt", e.toString(), e);
            return null;
        }
        return new String(decrypted, CHARSET);
    }
}