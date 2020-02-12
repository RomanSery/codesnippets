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
    public String details() {
        getCurrentWebResponse().addHeader("Access-Control-Allow-Origin", "*");
        return ApplicationDetailsService.getBranch();
    }
}
