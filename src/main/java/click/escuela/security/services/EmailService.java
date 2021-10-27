package click.escuela.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.security.connector.ProcessorConnector;

@Service
public class EmailService {
	
	@Autowired
	private ProcessorConnector processorConnector;
	
	public void sendEmail(String password, String userName, String email, String schoolId) {
		processorConnector.sendEmail(password, userName, email, schoolId);
	}

}
