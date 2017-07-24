package com.example.demo.service;

import com.viber.bot.Response;
import com.viber.bot.api.ViberBot;
import com.viber.bot.event.callback.OnSubscribe;
import com.viber.bot.event.callback.OnUnsubscribe;
import com.viber.bot.event.incoming.IncomingSubscribedEvent;
import com.viber.bot.event.incoming.IncomingUnsubscribeEvent;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.database.User;
import com.example.demo.database.UserRepository;
import com.google.common.util.concurrent.Futures;
import com.viber.bot.message.TextMessage;
import com.viber.bot.profile.UserProfile;

import ch.qos.logback.core.net.SyslogOutputStream;

@Service
public class ViberBotServiceImpl implements ViberBotService {

	private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

	@Override
	public void onMessageReceived(ViberBot bot) {

		bot.onMessageReceived((event, message, response) -> response.send("Alooo momaak"));
	}

	@Override
	public void subscribe(ViberBot bot) {

		bot.onSubscribe((new OnSubscribe() {
			@Override
			public void subscribe(IncomingSubscribedEvent event, Response response) {
				UserProfile userPr = event.getUser();

				if (userService.getByViberId(userPr.getId()) == null ) {    
					userService.add(new User(userPr.getId(),userPr.getName(),true));
					response.send("Vi ste novi.");
				}
				
				
				else { 
					
					userService.subscribe(userPr.getId());
					response.send("Vec ste bili ovdje ");
				}
			}
			
		}));
		
	}

	@Override
	public void unsubscribe(ViberBot bot) {
		
		bot.onUnsubscribe((new OnUnsubscribe() {
			@Override
			public void unsubscribe(IncomingUnsubscribeEvent event) {
				
				String viberId = event.getUserId();
				
				System.out.println("User ID is : " + viberId);		
				
				userService.unsubscribe(viberId);
					
			}
			
		}));

	}

	@Override
	public void onConversationStarted(ViberBot bot) {
		bot.onConversationStarted(event -> Futures.immediateFuture(Optional.of( // send 'Hi UserName' when conversation
																				// is started
				new TextMessage("Đez baaaa tebraaa  ,  " + event.getUser().getName()))));
	}

}
