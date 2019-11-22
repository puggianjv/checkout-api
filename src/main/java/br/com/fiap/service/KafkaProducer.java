package br.com.fiap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer
{
	@Autowired
	//TODO retirar String do value quando for implementar um producer
	private KafkaTemplate<String, String> kafkaTemplateOffer;

	@Value( "${fiap.topic.checkout}" )
	String kafkaTopic = "poc.fiap.offer";

	public void send( String offer )
	{
		try
		{
			System.out.println( kafkaTopic + " -> sending data - offer " + offer );
			kafkaTemplateOffer.send( kafkaTopic, offer );
			System.out.println( " -> enviado para kafka offers " );
		}
		catch( Exception e )
		{
			System.out.println( " erro no sending - offer " + e.getMessage() ) ;
			System.out.println( " data= " + offer );

			e.printStackTrace();
		}
	}
}