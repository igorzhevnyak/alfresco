package com.epam.custom.webscripts;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import java.io.IOException;

public class GetNodeInfoWebScript extends AbstractWebScript {

    private NodeService nodeService;

    @Override
    public void execute(WebScriptRequest req, WebScriptResponse res) throws IOException {
        NodeRef nodeName = new NodeRef(req.getParameter("nodeName"));
        try {
            JSONObject json = new JSONObject();
            json.put("nodeRef", nodeService.getNodeRef(Long.valueOf(req.getParameter("nodeName"))));
            json.put("type", nodeService.getType(nodeName));
            json.put("aspects", nodeService.getAspects(nodeName));
            json.put("properties", nodeService.getProperties(nodeName));
            String jsonString = json.toString();
            res.getWriter().write(jsonString);
        } catch (JSONException e) {
            throw new WebScriptException("Unable to serialize JSON");
        }
    }
}
