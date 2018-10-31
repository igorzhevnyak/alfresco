package com.epam.custom.webscripts;


import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.*;
import org.alfresco.model.ContentModel;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NodeHandlerWebScript extends AbstractWebScript {

    @Autowired
    private NodeService nodeService;

    public void execute(WebScriptRequest req, WebScriptResponse res) {
        Map<String, String> data = parseJson(req);
        Map<QName, Serializable> properties = new HashMap<>();
        properties.put(QName.createQName("name"), QName.createQName(data.get("propertyName")));
        properties.put(QName.createQName("value"), QName.createQName(data.get("propertyValue")));
        Map<QName, Serializable> aspectProperties = new HashMap<>();
        aspectProperties.put(QName.createQName("name"), QName.createQName(data.get("aspPropName")));
        aspectProperties.put(QName.createQName("value"), QName.createQName(data.get("aspPropValue")));
        NodeRef parentNodeRef = new NodeRef(data.get("parentNodeRef"));
        String nodeName = data.get("nodeName");
        NodeRef node = nodeService.getChildByName(parentNodeRef, ContentModel.ASSOC_REFERENCES, nodeName);
        if (!nodeService.exists(node)) {
            NodeRef newNode = new NodeRef(nodeName);
            nodeService.createNode(newNode, ContentModel.ASSOC_REFERENCES, QName.createQName("parent"), QName.createQName(data.get("typeName")), properties);
            nodeService.addAspect(newNode, QName.createQName(data.get("aspectName")), aspectProperties);
        } else {
            nodeService.setType(node, QName.createQName(data.get("typeName")));
            nodeService.addAspect(node, QName.createQName(data.get("aspectName")), aspectProperties);
            nodeService.addProperties(node, properties);
        }
    }

    private Map<String, String> parseJson(WebScriptRequest req) {
        JSONObject json = (JSONObject) req.parseContent();
        Map<String, String> result = new HashMap<>();
        result.put("parentNodeRef", json.getString("parentNodeRef"));
        result.put("nodeName", json.getString("nodeName"));
        result.put("typeName", json.getJSONObject("type").getString("name"));
        JSONArray propArr = json.getJSONObject("type").getJSONArray("properties");
        for (int i = 0; i < propArr.length(); i++) {
            result.put("propertyName", propArr.getJSONObject(i).getString("name"));
            result.put("propertyValue", propArr.getJSONObject(i).getString("value"));
        }
        JSONArray aspArr = json.getJSONArray("aspects");
        JSONArray aspPropArr = aspArr.getJSONObject(0).getJSONArray("properties");
        for (int i = 0; i < aspArr.length(); i++) {
            result.put("aspectName", propArr.getJSONObject(i).getString("name"));
            for (int k = 0; k < aspPropArr.length(); k++) {
                result.put("aspPropName", aspPropArr.getJSONObject(k).getString("name"));
                result.put("aspPropValue", aspPropArr.getJSONObject(k).getString("value"));
            }
        }
        return result;
    }
}

