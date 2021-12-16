package com.application.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class SendEmail {
	public static void main(String[] args) throws URISyntaxException {
		String subject = "Hallo%20Test";
		String body = "";
		String cc = "brath@inautix.co.in";

		try {
			Desktop.getDesktop()
					.mail(new URI("mailto:prrout@inautix.co.in?subject=" + subject + "&cc=" + cc + "&body=" + body));
		} catch (IOException ex) {
		}

	}
}