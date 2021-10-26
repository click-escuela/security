package click.escuela.security.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(name = "processor")
public interface ProcessorApiController {

	// EmailController
	@PostMapping(value = "/school/{schoolId}/email")
	public String sendEmail(@RequestParam(value = "password") String password,
			@RequestParam("userName") String userName, @RequestParam("email") String email,
			@PathVariable("schoolId") String schoolId);


}
