package com.example.demo.service;

import com.viber.bot.Response;
import com.viber.bot.api.ViberBot;
import com.viber.bot.event.callback.OnConversationStarted;
import com.viber.bot.event.callback.OnSubscribe;
import com.viber.bot.event.callback.OnUnsubscribe;
import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.event.incoming.IncomingSubscribedEvent;
import com.viber.bot.event.incoming.IncomingUnsubscribeEvent;

import java.util.Optional;
import java.util.concurrent.Future;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.database.User;
import com.example.demo.database.UserRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.Futures;
import com.viber.bot.message.Message;
import com.viber.bot.message.MessageKeyboard;
import com.viber.bot.message.TextMessage;
import com.viber.bot.message.TrackingData;
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

				if (userService.getByViberId(userPr.getId()) == null) {
					userService.add(new User(userPr.getId(), userPr.getName(), true));
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

		bot.onConversationStarted((new OnConversationStarted() {
			@Override
			public Future<Optional<Message>> conversationStarted(IncomingConversationStartedEvent event) {

				String JSONString = "{\"Type\": \"keyboard\",\"Buttons\": [{\"Columns\": 3,\"Rows\": 2,\"Text\": \"<font color=\\\"#494E67\\\">Smoking</font><br><br>\",\"TextSize\": \"medium\",\"TextHAlign\": \"center\",\"TextVAlign\": \"bottom\",\"ActionType\": \"reply\",\"ActionBody\": \"Smoking\",\"BgColor\": \"#f7bb3f\",\"Image\": \"https://s12.postimg.org/ti4alty19/smoke.png\"}, {\"Columns\": 3,\"Rows\": 2,\"Text\": \"<font color=\\\"#494E67\\\">Non Smoking</font><br><br>\",\"TextSize\": \"medium\",\"TextHAlign\": \"center\",\"TextVAlign\": \"bottom\",\"ActionType\": \"reply\",\"ActionBody\": \"Non smoking\",\"BgColor\": \"#f6f7f9\",\"Image\": \"https://s14.postimg.org/us7t38az5/Nonsmoke.png\"}]}";
				String keyboardMessageStr = JSONString;
				System.out.println("onConversation in. ");

				try {
					MessageKeyboard messageKeyboard = new MessageKeyboard(
							new ObjectMapper().readValue(keyboardMessageStr, new TypeReference<Map<String, Object>>() {
							}));
					System.out.println("onConversation parsed. ");

					// bot.onConversationStarted(event ->
					// Futures.immediateFuture(Optional.of(
					// new TextMessage("Đez baaaa tebraaa , " +
					// event.getUser().getName(),messageKeyboard, null, 0))));
					
					System.out.println(messageKeyboard);
					Map<String, Object> trDataHMap = new HashMap<>();
					trDataHMap.put("message", "reply");
					
					TrackingData trData=new TrackingData(trDataHMap);
					System.out.println("Tracking data created: ");
					
					String text="Đez baaaa tebraaa , " + event.getUser().getName();
					System.out.println("Text created: ");
					Integer minApiVersion=new Integer(1);
					System.out.println("MinApi version created : ");
					
					System.out.println("Creating TextMessage : ");
					TextMessage txtMessage = new TextMessage(text,
							messageKeyboard, trData,minApiVersion);
					
					System.out.println("Textmessage created: "+txtMessage);
					
					return Futures.immediateFuture(Optional.of(txtMessage));

				} catch (JsonParseException e) {
					System.out.println("Error in parsing");
					e.printStackTrace();
				} catch (JsonMappingException e) {
					System.out.println("Error in mapping");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("Error in mapping");
					e.printStackTrace();
				}
				System.out.println("onC error");
				return null;
			}

		}));

	}

}
