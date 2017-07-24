package com.example.demo.service;

import com.viber.bot.Response;
import com.viber.bot.api.ViberBot;
import com.viber.bot.event.incoming.IncomingMessageEvent;
import com.viber.bot.event.incoming.IncomingSubscribedEvent;
import com.viber.bot.event.incoming.IncomingUnsubscribeEvent;
import com.viber.bot.message.Message;

public interface ViberBotService {

	public void onMessageReceived(ViberBot bot);
	public void subscribe(ViberBot bot);
	public void unsubscribe(ViberBot bot);
	public void onConversationStarted(ViberBot bot);

}
