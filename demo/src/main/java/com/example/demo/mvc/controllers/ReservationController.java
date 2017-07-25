package com.example.demo.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.ReservationService;

@Controller
public class ReservationController {
	
	private ReservationService reservationService;
	
	@Autowired
	public void setReservationService(ReservationService reservationService){
		this.reservationService = reservationService;
	}
	
	@RequestMapping("reservationshow")
		public String showReservation(Model model){
		System.out.println("In reservations ");
		model.addAttribute("reservations", reservationService.getAll());
		System.out.println("Returning reservations ");
		return "reservations";
	}
}
