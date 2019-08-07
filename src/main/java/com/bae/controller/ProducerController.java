package com.bae.controller;

import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bae.models.Account;
import com.bae.models.SentAccount;
import com.bae.service.AccountService;

@RestController
@RequestMapping("/")
public class ProducerController {

	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	private static String subject = "myFirstQueue";

	@Autowired
	private AccountService service;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private JmsTemplate jmsTemplate;

	@PostMapping("/run")
	public String sendMessage(@RequestBody Object messageToQueue) throws JMSException {

		// Setup the connection
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		Connection connection = connectionFactory.createConnection();
		connection.start();

		// Creating a non transactional session to send/receive JMS message.
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// Destination represents here our queue 'JCG_QUEUE' on the JMS server.
		// The queue will be created automatically on the server.
		Destination destination = session.createQueue(subject);

		// MessageProducer is used for sending messages to the queue.
		MessageProducer producer = session.createProducer(destination);

		// We will send a small text message saying 'Hello World!!!'
		Message message = session.createMessage();
		message.setObjectProperty("account", messageToQueue);

		// Here we are sending our message!
		producer.send(message);

		System.out.println("JCG printing@@ '" + message.getObjectProperty("account") + "'");
		connection.close();
		return "The following has been added to the queue: " + messageToQueue.toString();
	}

	@GetMapping("/all")
	public List<Account> getAccounts() {
		return service.getAccounts();
	}

	@PostMapping("/createAccount")
	public Account createAccount(@RequestBody Account account) {
		// account = setAccountNumberAndPrize(account);
		sendToQueue(account);
		return service.addAccount(account);
	}

	private Account setAccountNumberAndPrize(Account account) {
//		String generatedAccountNum = restTemplate.getForObject(generatorURL + accountNumGeneratorPath, String.class);
//		Prize prizeWon = restTemplate.getForObject(prizeURL + determinePrizePath + generatedAccountNum, Prize.class);

//		account.setAccountNumber(generatedAccountNum);
//		account.setPrize(prizeWon);
		return account;
	}

	private void sendToQueue(Account account) {
		SentAccount accountToStore = new SentAccount(account);
		jmsTemplate.convertAndSend("AccountQueue", accountToStore);
	}

}
