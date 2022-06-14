package model;

import java.util.Random;

public interface Quotable {

	public default String randomQuote() {
		String quote1 = "Whether you think you can or think you can’t, you are right. - Henry Ford"; //Henry Ford
		String quote2 = "It always seems impossible until its done - Nelson Mandela"; //Nelson Mandela 
		String quote3 = "Be mindful. Be grateful. Be positive. Be true. Be kind. - Roy T. Bennett"; // Roy T Bennett
		String quote4 = "Always do your best. What you plant now, you will harvest later. - Og Mandino"; //Og Mandino
		String quote5 = "Do one thing every day that scares you. - Eleanor Roosevelt"; //Eleonor Roosevelt
		String quote6 ="Be brave to stand for what you believe in even if you stand alone. - Roy T. Bennett."; //Roy T Bennett
		String [] quoteBank = {quote1, quote2, quote3, quote4, quote5, quote6};
		Random randum = new Random();
		//choose a random number from the quote bank choices
		int randNum = randum.nextInt(quoteBank.length);
		//set the random quote and return it
		String randomQuote = quoteBank[randNum];
		
		return randomQuote;
	}
}
