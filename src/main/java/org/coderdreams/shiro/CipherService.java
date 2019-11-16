package org.coderdreams.shiro;

import java.security.Key;
import java.security.Security;

import org.apache.shiro.codec.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

@Service
public class CipherService {

	private GCMCipherService gcmCipher = new GCMCipherService();
	private CbcAeCipherService cbcAeCipher = new CbcAeCipherService();

	private byte[] cipherKeyBytes;

	static {
		if(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			BouncyCastleProvider provider = new BouncyCastleProvider();
			Security.addProvider(provider);
		}
	}

	public String encryptAndBase64Encode(String secretString, EncryptionType type) {
		return encryptAndBase64Encode(secretString, cipherKeyBytes, type);
	}
	public String encryptAndBase64Encode(String secretString, byte[] secretKey, EncryptionType type) {
		if(type == EncryptionType.CBCAE) {
			return cbcAeCipher.encrypt(secretString, secretKey).replace("\r\n", "");
		} else if(type == EncryptionType.GCM) {
			return gcmCipher.encrypt(secretString, secretKey).replace("\r\n", "");
		}
		return null;
	}


	public String base64DecodeAndDecrypt(String base64EncodedEncryptedString, EncryptionType type) throws Exception {
		return base64DecodeAndDecrypt(base64EncodedEncryptedString, cipherKeyBytes, type);
	}
	public String base64DecodeAndDecrypt(String base64EncodedEncryptedString, byte[] secretKey, EncryptionType type) throws Exception {
		if(type == EncryptionType.CBCAE) {
			return cbcAeCipher.decrypt(base64EncodedEncryptedString, secretKey);
		} else if(type == EncryptionType.GCM) {
			return gcmCipher.decrypt(base64EncodedEncryptedString, secretKey);
		}
		return null;
	}

	/**
	 * Use to create a random secret key
	 */
	private String generateNewKey() {
		Key key = gcmCipher.generateNewKey();
		byte[] keyBytes = key.getEncoded();
		return Base64.encodeToString(keyBytes);
	}

	public byte[] getCipherKeyBytes() {
		return cipherKeyBytes;
	}

	public void setCipherKeyBytes(byte[] cipherKeyBytes) {
		this.cipherKeyBytes = cipherKeyBytes;
	}
}