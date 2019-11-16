package org.coderdreams.shiro;

import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.crypto.DefaultBlockCipherService;
import org.apache.shiro.crypto.OperationMode;
import org.apache.shiro.crypto.PaddingScheme;
import org.apache.shiro.util.ByteSource;
import org.apache.wicket.util.crypt.Base64;

class GCMCipherService extends DefaultBlockCipherService {

    private static final String ALGORITHM_NAME = "AES";

    GCMCipherService() {
        super(ALGORITHM_NAME);
        setMode(OperationMode.GCM);
        setPaddingScheme(PaddingScheme.NONE);
    }

    String encrypt(String secretString, byte[] cipherKey) {

        byte[] secretBytes = CodecSupport.toBytes(secretString);
        ByteSource encryptedBytes = this.encrypt(secretBytes, cipherKey);
        return new Base64(true).encodeToString(encryptedBytes.getBytes());
    }

    String decrypt(String base64EncodedEncryptedString, byte[] cipherKey) throws Exception {
        byte[] encryptedEncodedBytes = CodecSupport.toBytes(base64EncodedEncryptedString);
        byte[] encryptedBytes = new Base64(true).decode(encryptedEncodedBytes);

        ByteSource decrypted = this.decrypt(encryptedBytes, cipherKey);
        return CodecSupport.toString(decrypted.getBytes());
    }

}
