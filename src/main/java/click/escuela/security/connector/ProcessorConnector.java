package click.escuela.security.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.security.feign.ProcessorApiController;

@Service
public class ProcessorConnector {
	
	@Autowired
	private ProcessorApiController processorApiController;
	
	public void sendEmail(String password, String userName, String email, String schoolId) {
		processorApiController.sendEmail(password, userName, email, schoolId);
	}
}
