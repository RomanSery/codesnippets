package org.coderdreams.service;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.coderdreams.shiro.CipherService;
import org.coderdreams.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Encryptor {
    private @Autowired CipherService cipherService;

    public <T extends WebPage> String createEncryptedUrl(Class<T> pageClass, PageParameters params) {
        String secretStr = Utils.mapToString(params);
        String encryptedStr = cipherService.encryptAndBase64Encode(secretStr);

        Url url = RequestCycle.get().mapUrlFor(pageClass, new PageParameters().add("enc", encryptedStr));
        return RequestCycle.get().getUrlRenderer().renderFullUrl(url);
    }

    public PageParameters decryptUrl(String encryptedStr) throws Exception {
        String decryptedStr = cipherService.base64DecodeAndDecrypt(encryptedStr);
        return Utils.stringToParams(decryptedStr);
    }
}
