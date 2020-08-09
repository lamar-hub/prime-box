package com.lamar.primebox.notification.sender.email;

import java.util.Map;

public interface TemplateResolver {

    String resolveHtmlTemplateToString(String template, Map<String, Object> templateModel);

}
