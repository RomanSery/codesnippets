package org.coderdreams.webapp;

import org.coderdreams.service.ApplicationDetailsService;
import org.wicketstuff.rest.annotations.MethodMapping;
import org.wicketstuff.rest.contenthandling.json.objserialdeserial.JacksonObjectSerialDeserial;
import org.wicketstuff.rest.contenthandling.json.webserialdeserial.JsonWebSerialDeserial;
import org.wicketstuff.rest.resource.AbstractRestResource;


public class VersionDetailsEndpoints extends AbstractRestResource<JsonWebSerialDeserial> {

    public VersionDetailsEndpoints() {
        super(new JsonWebSerialDeserial(new JacksonObjectSerialDeserial()));
    }

    @MethodMapping(value = "/")
    public VersionInfo details() {
        getCurrentWebResponse().addHeader("Access-Control-Allow-Origin", "*");
        return new VersionInfo(ApplicationDetailsService.getBranch(), ApplicationDetailsService.getVersion());
    }

    private static final class VersionInfo {
        private final String branch;
        private final String version;

        private VersionInfo(String branch, String version) {
            this.branch = branch;
            this.version = version;
        }

        public String getBranch() { return branch; }
        public String getVersion() { return version; }
    }
}
