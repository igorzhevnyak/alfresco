package com.epam.custom.webscripts;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import java.io.IOException;

public class GetNodeInfoWebScript extends AbstractWebScript {

    @Autowired
    private NodeService nodeService;

    @Override
    public void execute(WebScriptRequest req, WebScriptResponse res) throws IOException {
        String parameter = req.getParameter("nodeRef");
        NodeRef nodeRef = new NodeRef(parameter);
        if (nodeService.exists(nodeRef)) {
            try {
                JSONObject json = new JSONObject();
                json.put("nodeRef", nodeRef);
                json.put("nodeName", nodeService.getNodeRef(nodeService.getNodeAclId(nodeRef)).getId());
                json.put("type", nodeService.getType(nodeRef));
                json.put("aspects", nodeService.getAspects(nodeRef));
                json.put("properties", nodeService.getProperties(nodeRef));
                String jsonString = json.toString();
                res.getWriter().write(jsonString);
            } catch (JSONException e) {
                throw new WebScriptException("Unable to serialize JSON");
            }
        } else {
            throw new WebScriptException("Requested node in not found");
        }
    }
}
