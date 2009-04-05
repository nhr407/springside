package org.springside.modules.security.springsecurity;

import java.util.LinkedHashMap;
import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.ConfigAttributeEditor;
import org.springframework.security.intercept.web.RequestKey;

public class RequestMapFactoryBean implements FactoryBean {

	DefinitionService definitionService;

	public void setDefinitionService(DefinitionService definitionService) {
		this.definitionService = definitionService;
	}

	public Object getObject() throws Exception {
		LinkedHashMap<String, String> definitionMap = definitionService.getDefinitionMap();
		LinkedHashMap<RequestKey, ConfigAttributeDefinition> requestMap = new LinkedHashMap<RequestKey, ConfigAttributeDefinition>();
		ConfigAttributeEditor editor = new ConfigAttributeEditor();

		Set<String> paths = definitionMap.keySet();
		for (String path : paths) {
			RequestKey key = new RequestKey(path, null);
			editor.setAsText(definitionMap.get(path));
			requestMap.put(key, (ConfigAttributeDefinition) editor.getValue());
		}

		return requestMap;
	}

	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return LinkedHashMap.class;
	}

	public boolean isSingleton() {
		return true;
	}
}