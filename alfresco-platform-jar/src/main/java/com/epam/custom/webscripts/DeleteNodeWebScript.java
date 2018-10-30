package com.epam.custom.webscripts;

import org.alfresco.service.cmr.repository.InvalidNodeRefException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

public class DeleteNodeWebScript extends AbstractWebScript {

    private NodeService nodeService;

    public void execute(WebScriptRequest req, WebScriptResponse res){
        NodeRef nodeRef = new NodeRef(req.getParameterValues("nodeName").toString());
        if(nodeRef != null){
            if (nodeService.exists(nodeRef)) {
                nodeService.deleteNode(nodeRef);
            } else {
                throw new InvalidNodeRefException(nodeRef);
            }
        } else {
            throw new WebScriptException("oops");
        }

    }
}
