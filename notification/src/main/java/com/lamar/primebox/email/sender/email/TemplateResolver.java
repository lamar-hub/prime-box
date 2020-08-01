package com.lamar.primebox.email.sender.email;

import java.util.Map;

public interface TemplateResolver {

    String resolveHtmlTemplateToString(String template, Map<String, Object> templateModel);

}
