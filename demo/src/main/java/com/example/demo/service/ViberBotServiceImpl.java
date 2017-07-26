package com.example.demo.service;

import com.viber.bot.Response;
import com.viber.bot.api.ViberBot;
import com.viber.bot.event.callback.OnConversationStarted;
import com.viber.bot.event.callback.OnMessageReceived;
import com.viber.bot.event.callback.OnSubscribe;
import com.viber.bot.event.callback.OnUnsubscribe;
import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.event.incoming.IncomingMessageEvent;
import com.viber.bot.event.incoming.IncomingSubscribedEvent;
import com.viber.bot.event.incoming.IncomingUnsubscribeEvent;

import java.util.Optional;
import java.util.concurrent.Future;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.database.Room;
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
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoomService roomService;



	@Override
	public void onMessageReceived(ViberBot bot) {

		bot.onMessageReceived((new OnMessageReceived() {
			@Override
			public void messageReceived(IncomingMessageEvent event, Message message, Response response) {
				
				
				if(message.getTrackingData().get("message").equals("welcome_message")) {
					
					if(message.getMapRepresentation().get("text").equals("Reserve room")){
							MessageKeyboard roomKeyboard = createRoomKeyboard();
							Map<String, Object> trDataHMap = new HashMap<>();
							trDataHMap.put("message", "get_rooms");
							TrackingData trData=new TrackingData(trDataHMap);

							response.send(new TextMessage("Please select one of the available rooms."
									,roomKeyboard, trData, new Integer(1))
									);
					}
					else if(message.getMapRepresentation().get("text").equals("See previous reservations")){
							
							}
					
				}
				
				
				else if(message.getTrackingData().get("message").equals("get_rooms")) {
					
					
					System.out.println("Listing rooms.. ");
					
					
					
					
				}				
				
				
				
				
				

			}

		}));

	}

	@Override
	public void subscribe(ViberBot bot) {

		bot.onSubscribe((new OnSubscribe() {
			@Override
			public void subscribe(IncomingSubscribedEvent event, Response response) {
				UserProfile userPr = event.getUser();

				if (userService.getByViberId(userPr.getId()) == null) {
					userService.add(new User(userPr.getId(), userPr.getName(), true));
					
				}

				else {

					userService.subscribe(userPr.getId());

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

				String JSONString = "{\"Type\": \"keyboard\",\"Buttons\": [{\"Columns\": 3,\"Rows\": 2,\"Text\": \"<font color=\\\"#494E67\\\">Reserve room </font><br><br>\",\"TextSize\": \"medium\",\"TextHAlign\": \"center\",\"TextVAlign\": \"bottom\",\"ActionType\": \"reply\",\"ActionBody\": \"Reserve room\",\"BgColor\": \"#f7bb3f\"}, {\"Columns\": 3,\"Rows\": 2,\"Text\": \"<font color=\\\"#494E67\\\">See previous reservations</font><br><br>\",\"TextSize\": \"medium\",\"TextHAlign\": \"center\",\"TextVAlign\": \"bottom\",\"ActionType\": \"reply\",\"ActionBody\": \"See previous reservations\",\"BgColor\": \"#f6f7f9\"}]}";
				String keyboardMessageStr = JSONString;
				

				try {
					MessageKeyboard messageKeyboard = new MessageKeyboard(
							new ObjectMapper().readValue(keyboardMessageStr, new TypeReference<Map<String, Object>>() {
							}));
					
					Map<String, Object> trDataHMap = new HashMap<>();
					trDataHMap.put("message", "welcome_message");
					
					TrackingData trData=new TrackingData(trDataHMap);
					String text="Hello  , " + event.getUser().getName()+
							". Welcome to ViberBot room reservation service .Please select one of the following options.";
					Integer minApiVersion=new Integer(1);
					TextMessage txtMessage = new TextMessage(text,
							messageKeyboard, trData,minApiVersion);
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

	private MessageKeyboard createRoomKeyboard() {
		
		Map<String,Object> keyboardMap=new HashMap<>();
		keyboardMap.put("Type","keyboard");
		List<Room> rooms=roomService.listAll();
		List<Map> buttons = new ArrayList<>();

		Map<String, Object> cancelButton  = new HashMap<>();
		cancelButton.put("Text", "Cancel");
		cancelButton.put("BgColor", "#f00b0b");
		cancelButton.put("ActionBody", "Cancel");
    	buttons.add(cancelButton);
		
		for(Room room: rooms) {
			
			String roomOnViber=room.getName()+" "+room.getNumber();
			Map<String, Object> button  = new HashMap<>();
	    	button.put("BgColor", "#2db9b9");
	    	button.put("Text", roomOnViber);
	    	button.put("ActionBody", roomOnViber);
	    	buttons.add(button);
				
		}
		
		keyboardMap.put("Buttons", buttons);
		
		return new MessageKeyboard(keyboardMap);
		
	}	
	
	
}
