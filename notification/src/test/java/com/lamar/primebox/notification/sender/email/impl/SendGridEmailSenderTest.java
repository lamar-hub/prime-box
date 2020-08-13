package com.lamar.primebox.notification.sender.email.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
@Slf4j
class SendGridEmailSenderTest {

//    @Mock
//    private SendGrid sendGrid;
//
//    @Mock
//    private TemplateResolver templateResolver;
//
//    @InjectMocks
//    private SendGridEmailSender sendGridEmailSender;

    @Test
    public void test() {
        Assert.assertEquals(2, 2);
    }

    @Test
    void sendEmailRequest() throws IOException {
//        final SendGrid sendGrid = new SendGrid("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");
//
//        final Email from = new Email(PrimeBoxEmailConstants.EMAIL_FROM, PrimeBoxEmailConstants.EMAIL_FROM_NAME);
//        final String subject = "PRIME BOX";
//        final Email to = new Email("marinkovic.lazar29@gmail.com");
//        //language=HTML
//        final String html = "<html>\n" +
//                "<head>\n" +
//                "    <title>Dessi" +
//                "</title>\n" +
//                "</head>\n" +
//                "<body>\n" +
//                "<h1>Hello Everybody</h1>\n" +
//                "</body>\n" +
//                "</html>";
//        final Content content = new Content(ContentType.TEXT_HTML.getMimeType(), html);
//        final Mail mail = new Mail(from, subject, to, content);
//
//        final Request request = new Request();
//        request.setMethod(Method.POST);
//        request.setEndpoint(PrimeBoxEmailConstants.ENDPOINT);
//        request.setBody(mail.build());
//
//        final Long timestamp = System.currentTimeMillis();
//        final Response response = sendGrid.api(request);
//        System.out.println(System.currentTimeMillis() - timestamp);
//        log.info(response.toString());
//        log.info(String.valueOf(response.getStatusCode()));
//        log.info(String.valueOf(response.getHeaders()));
//        log.info(response.getBody());
    }
}