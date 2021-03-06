package springboot.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import springboot.web.dto.HelloResponseDto;

@RestController
public class HelloController {
	
	// @RequestMapping(value = "/hello", method = RequestMethod.GET)
	// @RequestMapping("/hello")
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
	
	@GetMapping("/hello/dto")
	public HelloResponseDto helloDto(@RequestParam String name, @RequestParam int amount) {
		return new HelloResponseDto(name, amount);
	}
}