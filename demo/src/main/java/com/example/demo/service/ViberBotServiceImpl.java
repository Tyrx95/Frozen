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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.example.demo.database.Reservation;
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

	@Autowired
	private ReservationService reservationService;

	private String roomNumber;
	private String roomName;
	private LocalDate date;
	private LocalTime rTime;
	private String viberId;
	private Long resId;

	@Override
	public void onMessageReceived(ViberBot bot) {

		bot.onMessageReceived((new OnMessageReceived() {
			@Override
			public void messageReceived(IncomingMessageEvent event, Message message, Response response) {

				String msg = message.getTrackingData().get("message").toString();
				String messageText = message.getMapRepresentation().get("text").toString();

				if (messageText.equals("Cancel")) {
					response.send(welcomeMessage(event.getSender().getName()));
				}

				else if (msg.equals("welcome_message")) {

					if (messageText.equals("Reserve room")) {
						response.send(createRoomTextMessage(""));
					} else if (messageText.equals("See previous reservations")) {
						viberId = event.getSender().getId();
						response.send(createShowResMessage(""));

					}

				}

				else if (msg.equals("get_rooms")) {

					if (messageText.split("\\s+")[0].equals("choose_room")) {
						Map<String, Object> trDataHMap = new HashMap<>();
						trDataHMap.put("message", "enter_date");
						roomName = messageText.split("\\s+")[1];
						roomNumber = messageText.split("\\s+")[2];
						System.out.println(roomNumber);
						TrackingData trData = new TrackingData(trDataHMap);

						response.send(new TextMessage("Enter the desired date in format : dd.mm.yyyy",
								createCancelKeyboard(), trData, new Integer(1)));
					}

					else {
						response.send(createRoomTextMessage("Please use the rooms provided for selection. "));

					}

				}

				else if (msg.equals("enter_date")) {

					try {

						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
						date = LocalDate.parse(messageText, formatter);
						response.send(createTimeTextMessage(""));

					} catch (Exception e) {

						response.send(
								new TextMessage("Invalid format. Please enter the date again in format : dd.mm.yyyy",
										createCancelKeyboard(), message.getTrackingData(), new Integer(1)));
					}

				}

				else if (msg.equals("choose_time")) {

					if (messageText.split("\\s+")[0].equals("-=choose_time=-")) {

						Map<String, Object> trDataHMap = new HashMap<>();
						trDataHMap.put("message", "confirm_reservation");
						TrackingData trData = new TrackingData(trDataHMap);
						rTime = LocalTime.parse(messageText.split("\\s+")[1], DateTimeFormatter.ISO_LOCAL_TIME);
						response.send(new TextMessage(
								"Do you want to confirm your reservation? \nReservation details: \n" + "Room name: "
										+ roomName + "\nRoom number: " + roomNumber + "\nReservation date: "
										+ date.toString() + "\nReservation time: " + rTime.toString(),
								createConfirmationKeyboard(), trData, new Integer(1)));
					}

					else {

						response.send(createTimeTextMessage("Please use the time slots provided. "));

					}

				}

				else if (msg.equals("confirm_reservation")) {

					if (messageText.equals("#A3#Confirm#A3#")) {
						Map<String, Object> trDataHMap = new HashMap<>();
						trDataHMap.put("message", "end");
						TrackingData trData = new TrackingData(trDataHMap);
						User user = userService.getByViberId(event.getSender().getId());
						Room room = roomService.getByNumber(roomNumber);
						reservationService.reserve(new Reservation(user, room, date, rTime));
						response.send(new TextMessage("Reservation confirmed, press Home to go back to the main menu.",
								createHomeKeyboard(), trData, new Integer(1)));
					}
					else {
						
						Map<String, Object> trDataHMap = new HashMap<>();
						trDataHMap.put("message", "confirm_reservation");
						TrackingData trData = new TrackingData(trDataHMap);
						response.send(new TextMessage(
								"Please use one of the options below: ",
								createConfirmationKeyboard(), trData, new Integer(1)));
						
					}

				}

				else if (msg.equals("show_reservations")) {

					if (messageText.split("\\s+")[0].equals("-=#choose-res#=-")) {
						Map<String, Object> trDataHMap = new HashMap<>();
						trDataHMap.put("message", "confirm_cancel");
						resId = Long.parseLong(messageText.split("\\s+")[1]);
						TrackingData trData = new TrackingData(trDataHMap);
						response.send(new TextMessage(
								"Reservation details are listed above. Choose one of the available " + "options:",
								createCancelReservationKeyboard(), trData, new Integer(1)));
					}

					else {

						response.send(createShowResMessage(
								"Please use the slots provided below for choosing reservations. "));

					}

				}

				else if (msg.equals("confirm_cancel")) {

					if (messageText.equals("#0A4CancelReservation4A0#")) {

						Map<String, Object> trDataHMap = new HashMap<>();
						trDataHMap.put("message", "res_cancel");
						TrackingData trData = new TrackingData(trDataHMap);
						response.send(new TextMessage(
								"Press confirm to confirm the cancellation. Press Cancel to go to the main menu.",
								createConfirmationKeyboard(), trData, new Integer(1)));
					}

					else {
						Map<String, Object> trDataHMap = new HashMap<>();
						trDataHMap.put("message", "confirm_cancel");
						TrackingData trData = new TrackingData(trDataHMap);
						response.send(new TextMessage("Please use one of the options available: ",
								createCancelReservationKeyboard(), trData, new Integer(1)));

					}

				}

				else if (msg.equals("res_cancel")) {

					if (messageText.equals("#A3#Confirm#A3#")) {
						reservationService.delete(resId);
						Map<String, Object> trDataHMap = new HashMap<>();
						trDataHMap.put("message", "show_end");
						TrackingData trData = new TrackingData(trDataHMap);
						response.send(new TextMessage(
								"You have successfully canceled your reservation. Press Home to go to the main menu ",
								createHomeKeyboard(), trData, new Integer(1)));
					}

					else {

						Map<String, Object> trDataHMap = new HashMap<>();
						trDataHMap.put("message", "res_cancel");
						TrackingData trData = new TrackingData(trDataHMap);
						response.send(new TextMessage("Please use one of the options available below: ",
								createConfirmationKeyboard(), trData, new Integer(1)));

					}

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
				userService.unsubscribe(viberId);

			}

		}));

	}

	@Override
	public void onConversationStarted(ViberBot bot) {

		bot.onConversationStarted((new OnConversationStarted() {
			@Override
			public Future<Optional<Message>> conversationStarted(IncomingConversationStartedEvent event) {

				return Futures.immediateFuture(Optional.of(welcomeMessage(event.getUser().getName())));
			}

		}));

	}

	private MessageKeyboard createRoomKeyboard() {

		Map<String, Object> keyboardMap = new HashMap<>();
		keyboardMap.put("Type", "keyboard");
		List<Room> rooms = roomService.listAll();
		List<Map> buttons = new ArrayList<>();

		Map<String, Object> cancelButton = new HashMap<>();
		cancelButton.put("Text", "Cancel");
		cancelButton.put("BgColor", "#ee6c4d");
		cancelButton.put("ActionBody", "Cancel");
		buttons.add(cancelButton);

		for (Room room : rooms) {

			String roomOnViber = room.getName() + " " + room.getNumber();
			Map<String, Object> button = new HashMap<>();
			button.put("BgColor", "#afe1f5");
			button.put("Text", roomOnViber);
			button.put("ActionBody", "choose_room " + roomOnViber);
			buttons.add(button);

		}

		keyboardMap.put("Buttons", buttons);

		return new MessageKeyboard(keyboardMap);

	}

	private MessageKeyboard createCancelKeyboard() {

		return createOneButtonKeyboard("Cancel", "#ee6c4d");

	}

	private MessageKeyboard createHomeKeyboard() {

		return createOneButtonKeyboard("<font color=\"#ffffff\">Home </font>", "#665cac");

	}

	private MessageKeyboard createOneButtonKeyboard(String text, String color) {
		Map<String, Object> keyboardMap = new HashMap<>();
		keyboardMap.put("Type", "keyboard");

		List<Map> buttons = new ArrayList<>();

		Map<String, Object> cancelButton = new HashMap<>();
		cancelButton.put("Text", text);
		cancelButton.put("BgColor", color);
		cancelButton.put("ActionBody", "Cancel");
		buttons.add(cancelButton);
		keyboardMap.put("Buttons", buttons);

		return new MessageKeyboard(keyboardMap);

	}

	private MessageKeyboard createTimeKeyboard() {

		Map<String, Object> keyboardMap = new HashMap<>();
		keyboardMap.put("Type", "keyboard");
		List<Map> buttons = new ArrayList<>();

		Map<String, Object> cancelButton = new HashMap<>();
		cancelButton.put("Text", "Cancel");
		cancelButton.put("BgColor", "#ee6c4d");
		cancelButton.put("ActionBody", "Cancel");
		buttons.add(cancelButton);

		LocalTime start = roomService.getByNumber(roomNumber).getStartWorkTime();
		LocalTime end = roomService.getByNumber(roomNumber).getEndWorkTime();

		HashSet<LocalTime> timeSet = new HashSet<>();
		Iterator<LocalTime> it = reservationService.getFreeRoomCapacitiesOnDate(roomNumber, date).iterator();
		while (it.hasNext()) {

			LocalTime tim = it.next();
			System.out.println("in loop " + tim);
			timeSet.add(tim);
		}

		System.out.println("HashSet: " + timeSet);

		for (LocalTime time = start; time.isBefore(end); time = time.plusHours(1)) {

			if (!timeSet.contains(time)) {
				Map<String, Object> button = new HashMap<>();
				button.put("BgColor", "#afe1f5");
				button.put("Text", time.toString());
				button.put("ActionBody", "-=choose_time=- " + time.toString());
				buttons.add(button);
			}
		}

		keyboardMap.put("Buttons", buttons);

		return new MessageKeyboard(keyboardMap);

	}

	private TextMessage welcomeMessage(String userName) {

		String JSONString = "{\"Type\": \"keyboard\",\"Buttons\": [{\"Columns\": 3,\"Rows\": 2,\"Text\": \"<font color=\\\"#ffffff\\\">Reserve room </font><br><br>\",\"TextSize\": \"medium\",\"TextHAlign\": \"center\",\"TextVAlign\": \"bottom\",\"ActionType\": \"reply\",\"ActionBody\": \"Reserve room\",\"BgColor\": \"#665cac\"}, {\"Columns\": 3,\"Rows\": 2,\"Text\": \"<font color=\\\"#ffffff\\\">See previous reservations</font><br><br>\",\"TextSize\": \"medium\",\"TextHAlign\": \"center\",\"TextVAlign\": \"bottom\",\"ActionType\": \"reply\",\"ActionBody\": \"See previous reservations\",\"BgColor\": \"#665cac\"}]}";
		String keyboardMessageStr = JSONString;

		try {
			MessageKeyboard messageKeyboard = new MessageKeyboard(
					new ObjectMapper().readValue(keyboardMessageStr, new TypeReference<Map<String, Object>>() {
					}));

			Map<String, Object> trDataHMap = new HashMap<>();
			trDataHMap.put("message", "welcome_message");

			TrackingData trData = new TrackingData(trDataHMap);
			String text = "Hello, " + userName
					+ ". Welcome to ViberBot room reservation service. Please select one of the following options:";
			Integer minApiVersion = new Integer(1);
			TextMessage txtMessage = new TextMessage(text, messageKeyboard, trData, minApiVersion);
			return txtMessage;

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

	private MessageKeyboard createConfirmationKeyboard() {

		Map<String, Object> keyboardMap = new HashMap<>();
		keyboardMap.put("Type", "keyboard");

		List<Map> buttons = new ArrayList<>();

		Map<String, Object> confirmButton = new HashMap<>();
		confirmButton.put("Text", "<font color=\"#ffffff\">Confirm</font>");
		confirmButton.put("BgColor", "#665cac");

		confirmButton.put("ActionBody", "#A3#Confirm#A3#");
		buttons.add(confirmButton);

		Map<String, Object> cancelButton = new HashMap<>();
		cancelButton.put("Text", "Cancel");
		cancelButton.put("BgColor", "#ee6c4d");
		cancelButton.put("ActionBody", "Cancel");
		buttons.add(cancelButton);

		keyboardMap.put("Buttons", buttons);

		return new MessageKeyboard(keyboardMap);

	}

	private MessageKeyboard createReservationsKeyboard() {

		Map<String, Object> keyboardMap = new HashMap<>();
		keyboardMap.put("Type", "keyboard");

		List<Map> buttons = new ArrayList<>();

		Map<String, Object> cancelButton = new HashMap<>();
		cancelButton.put("Text", "Cancel");
		cancelButton.put("BgColor", "#ee6c4d");
		cancelButton.put("ActionBody", "Cancel");
		buttons.add(cancelButton);

		Iterator<Reservation> it = reservationService.getByUser(viberId).iterator();
		while (it.hasNext()) {

			Reservation res = it.next();

			String resText = "Room name: " + res.getRoomId().getName() + "\nRoom number: " + res.getRoomId().getNumber()
					+ "\nDate: " + res.getDate().toString() + "\nTime: " + res.getTime().toString();

			Map<String, Object> resButton = new HashMap<>();
			resButton.put("Columns", 3);
			resButton.put("Rows", 2);
			resButton.put("Text", resText);
			resButton.put("BgColor", "#afe1f5");
			resButton.put("ActionBody", "-=#choose-res#=- " + res.getId());
			resButton.put("TextSize", "small");
			buttons.add(resButton);

		}

		keyboardMap.put("Buttons", buttons);

		return new MessageKeyboard(keyboardMap);

	}

	private MessageKeyboard createCancelReservationKeyboard() {

		Map<String, Object> keyboardMap = new HashMap<>();
		keyboardMap.put("Type", "keyboard");

		List<Map> buttons = new ArrayList<>();

		Map<String, Object> cancelButton = new HashMap<>();
		cancelButton.put("Text", "<font color=\"#ffffff\">Home </font>");
		cancelButton.put("BgColor", "#665cac");
		cancelButton.put("ActionBody", "Cancel");
		buttons.add(cancelButton);

		Map<String, Object> cancelResButton = new HashMap<>();
		cancelResButton.put("Text", "Cancel Reservation");
		cancelResButton.put("BgColor", "#ee6c4d");
		cancelResButton.put("ActionBody", "#0A4CancelReservation4A0#");
		buttons.add(cancelResButton);
		keyboardMap.put("Buttons", buttons);

		return new MessageKeyboard(keyboardMap);

	}

	private TextMessage createRoomTextMessage(String addText) {

		MessageKeyboard roomKeyboard = createRoomKeyboard();
		Map<String, Object> trDataHMap = new HashMap<>();
		trDataHMap.put("message", "get_rooms");
		TrackingData trData = new TrackingData(trDataHMap);

		return new TextMessage(addText + "Please select one of the available rooms.", roomKeyboard, trData,
				new Integer(1));
	}

	private TextMessage createTimeTextMessage(String addText) {
		Map<String, Object> trDataHMap = new HashMap<>();
		trDataHMap.put("message", "choose_time");
		TrackingData trData = new TrackingData(trDataHMap);

		return new TextMessage(addText + "Pick desired time of reservation: ", createTimeKeyboard(), trData,
				new Integer(1));
	}

	private TextMessage createShowResMessage(String string) {

		Map<String, Object> trDataHMap = new HashMap<>();
		trDataHMap.put("message", "show_reservations");
		TrackingData trData = new TrackingData(trDataHMap);
		return new TextMessage(string + "Your previous reservations are shown on the keyboard:",
				createReservationsKeyboard(), trData, new Integer(1));
	}

}
