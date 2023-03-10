package com.sivalabs.myapp;

import io.micronaut.http.annotation.*;

@Controller("/info")
public class InfoController {

	@Get(uri = "/", produces = "text/plain")
	public String index() {
		return "Micronaut JPA Demo App";
	}

}