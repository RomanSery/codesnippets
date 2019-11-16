package org.coderdreams.shiro;

import java.util.Arrays;

import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import org.apache.wicket.util.crypt.Base64;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.HKDFParameters;
import org.bouncycastle.crypto.params.KeyParameter;


class CbcAeCipherService extends AesCipherService {
	
	//DO NOT CHANGE THESE
	private final int MAC_BYTE_SIZE = 32;
    private final String ENCRYPTION_INFO = "PIPS|KeyForEncryption";
    private final String AUTHENTICATION_INFO = "PIPS|KeyForAuthentication";
	
    //cipherKey = Base64.decode(key) where 'key' is a string that is provided to you
	public String encrypt(String secretString, byte[] cipherKey) {
		
		//create the IV+ciphertext using the encryption sub-key
		byte[] encryptedBytes = this.encrypt(CodecSupport.toBytes(secretString), getEncryptionSubKey(cipherKey)).getBytes();
		
		//calculate the hmac of the IV+encrypted using the authentication sub-key
		byte[] auth = calculateHmac(encryptedBytes, getAuthenticationSubKey(cipherKey));		

		byte[] finalCipher = new byte[auth.length + encryptedBytes.length];
		System.arraycopy(auth, 0, finalCipher, 0, auth.length);
		System.arraycopy(encryptedBytes, 0, finalCipher, auth.length, encryptedBytes.length);
		
		//return the final result hmac+IV+ciphertext
		return new Base64(true).encodeToString(finalCipher);
	}
	
		
	//cipherKey = Base64.decode(key) where 'key' is a string that is provided to you
	public String decrypt(String base64EncodedEncryptedString, byte[] cipherKey) throws Exception {
		byte[] encryptedBytes = new Base64(false).decode(base64EncodedEncryptedString);
				
		// Extract the HMAC from the front of encryptedBytes.	
		byte[] hmac = Arrays.copyOfRange(encryptedBytes, 0, MAC_BYTE_SIZE);
		byte[] ciphertext = Arrays.copyOfRange(encryptedBytes, MAC_BYTE_SIZE, encryptedBytes.length);
        
        if(!verifyHMAC(hmac, ciphertext, getAuthenticationSubKey(cipherKey))) {
        	throw new Exception("verifyHMAC failed");
        }		
		
		ByteSource decrypted = this.decrypt(ciphertext, getEncryptionSubKey(cipherKey));
		return CodecSupport.toString(decrypted.getBytes());
	}
	
	private boolean verifyHMAC(byte[] correct_hmac, byte[] message, byte[] key) {				
		byte[] message_hmac = calculateHmac(message, key);
		if(message_hmac.length != correct_hmac.length) return false;
                
        byte[] blind = this.generateNewKey().getEncoded();        
        byte[] message_compare = calculateHmac(message_hmac, blind);
        byte[] correct_compare = calculateHmac(correct_hmac, blind);
        return Arrays.equals(correct_compare, message_compare);
	}
	
	
	
	
	
	//helpers
	
	private byte[] calculateHmac(byte[] message, byte[] key) {
		HMac hmac = new HMac(new SHA256Digest());
		byte[] hmac_out = new byte[hmac.getMacSize()];
		
		hmac.init(new KeyParameter(key));
		hmac.update(message, 0, message.length);
		hmac.doFinal(hmac_out, 0);	
		
		return hmac_out;
	}
	
	
	private byte[] getEncryptionSubKey(byte[] baseKey) {
        byte[] eKey = new byte[16];
        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
        hkdf.init(new HKDFParameters(baseKey, null, ENCRYPTION_INFO.getBytes()));
        hkdf.generateBytes(eKey, 0, 16);   
        return eKey;
	}
	
	private byte[] getAuthenticationSubKey(byte[] baseKey) {
        byte[] aKey = new byte[16];
        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
        hkdf.init(new HKDFParameters(baseKey, null, AUTHENTICATION_INFO.getBytes()));
        hkdf.generateBytes(aKey, 0, 16);   
        return aKey;
	}

	
}