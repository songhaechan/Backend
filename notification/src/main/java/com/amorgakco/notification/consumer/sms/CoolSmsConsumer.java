package com.amorgakco.notification.consumer.sms;

import com.amorgakco.notification.dto.SmsMessageRequest;
import com.amorgakco.notification.consumer.slack.SlackSender;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CoolSmsConsumer implements SmsConsumer {

    private final DefaultMessageService messageService;
    private final String serverPhoneNumber;
    private final SlackSender slackSender;

    public CoolSmsConsumer(
            final DefaultMessageService messageService,
            @Value("${server-phone-number}") final String serverPhoneNumber,
            final SlackSender slackSender) {
        this.messageService = messageService;
        this.serverPhoneNumber = serverPhoneNumber;
        this.slackSender = slackSender;
    }

    @RabbitListener(queues = "sms")
    public void consume(
            final SmsMessageRequest request, final Channel channel, final Envelope envelope)
            throws IOException {
        slackSender.sendSmsMessage(request);
//        final long deliveryTag = envelope.getDeliveryTag();
//        try {
//            messageService.send(createMessage(request));
//            channel.basicAck(deliveryTag, false);
//        } catch (NurigoEmptyResponseException
//                | NurigoMessageNotReceivedException
//                | NurigoUnknownException e) {
//            channel.basicNack(deliveryTag, false, false);
//        }
    }

    private Message createMessage(final SmsMessageRequest request) {
        final Message message = new Message();
        message.setFrom(serverPhoneNumber);
        message.setTo(request.phoneNumber());
        message.setText(request.title() + "\n");
        message.setText(request.content());
        return message;
    }
}
