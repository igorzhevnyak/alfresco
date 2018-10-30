package com.epam.custom.webscripts;


import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.extensions.webscripts.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NodeHandlerWebScript extends AbstractWebScript {

    private NodeService nodeService;

    public void execute(WebScriptRequest req, WebScriptResponse res) {
        Map<String, String> data = parseJson(req);
        NodeRef parentNodeRef = new NodeRef(data.get("parentNodeRef"));
        NodeRef nodeRef = new NodeRef(data.get("nodeName"));
        Map<QName, Serializable> properties = new HashMap<>();
        properties.put(QName.createQName("propertyName"), QName.createQName(data.get("propertyName")));
        properties.put(QName.createQName("propertyValue"), QName.createQName(data.get("propertyValue")));
        Map<QName, Serializable> aspectProperties = new HashMap<>();
        aspectProperties.put(QName.createQName("aspPropName"), QName.createQName(data.get("aspPropName")));
        aspectProperties.put(QName.createQName("aspPropValue"), QName.createQName(data.get("aspPropValue")));
        if (!nodeService.exists(nodeRef)) {
            nodeService.createNode(parentNodeRef, null, null, null, properties);
            nodeService.setType(nodeRef, QName.createQName(data.get("typeName")));
            nodeService.addAspect(nodeRef, QName.createQName(data.get("aspectName")), aspectProperties);
        } else {
            nodeService.setType(nodeRef, QName.createQName(data.get("typeName")));
            nodeService.addAspect(nodeRef, QName.createQName(data.get("aspectName")), aspectProperties);
            nodeService.addProperties(nodeRef, properties);
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
        for (int i = 0; i < aspArr.length(); i++) {
            result.put("aspectName", propArr.getJSONObject(i).getString("name"));
        }
        JSONArray aspPropArr = aspArr.getJSONArray(1);
            for (int k = 0; k < aspPropArr.length(); k++) {
                result.put("aspPropName", propArr.getJSONObject(k).getString("name"));
                result.put("aspPropValue", propArr.getJSONObject(k).getString("value"));
            }
        return result;
    }
}

