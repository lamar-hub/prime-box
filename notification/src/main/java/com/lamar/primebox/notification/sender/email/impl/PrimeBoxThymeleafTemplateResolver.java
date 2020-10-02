package com.lamar.primebox.notification.sender.email.impl;

import com.lamar.primebox.notification.sender.email.TemplateResolver;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Map;

@Component
public class PrimeBoxThymeleafTemplateResolver implements TemplateResolver {

    private final SpringTemplateEngine thymeleafTemplateEngine;

    public PrimeBoxThymeleafTemplateResolver(SpringTemplateEngine thymeleafTemplateEngine) {
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }

    @Override
    public String resolveHtmlTemplateToString(String template, Map<String, Object> templateModel) {
        final Context thymeleafContext = new Context();

        thymeleafContext.setVariables(templateModel);
        return thymeleafTemplateEngine.process(template, thymeleafContext);
    }

}
