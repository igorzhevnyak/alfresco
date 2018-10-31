package com.epam.custom.webscripts;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

public class DeleteNodeWebScript extends AbstractWebScript {

    @Autowired
    private NodeService nodeService;

    @Override
    public void execute(WebScriptRequest req, WebScriptResponse res) {
        JSONObject json = (JSONObject) req.parseContent();
        NodeRef nodeRef = new NodeRef(json.getString("nodeRef"));
        if (nodeService.exists(nodeRef)) {
            nodeService.deleteNode(nodeRef);
        } else {
            throw new WebScriptException("Failed to delete selected node");
        }

    }
}
