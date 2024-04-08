package com.algaworks.algafood.api;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HostCheckeController {

	@GetMapping("/hostcheck")
	public String checkHost() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress()
				+ " - "  + InetAddress.getLocalHost().getHostName();
	}
}
