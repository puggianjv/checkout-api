package br.com.fiap.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static org.apache.log4j.Logger.getLogger;

@Service
@Configuration
public class KafkaConsumer
{
	private static final Logger LOG = getLogger( KafkaConsumer.class.getName( ) );

	@KafkaListener( topics = "${fiap.topic.checkout}", groupId = "${fiap.topic.group-id}" )
	public void processMessage( ConsumerRecord<String, String> offerAvro )
	{
		LOG.info( "KafkaConsumerOffer :: received content offer ::  " + offerAvro  );
		System.out.println( "KafkaConsumerOffer :: received content offer ::  " + offerAvro  );

		System.out.println(offerAvro);

//		DataStore store = null;
		try
		{
//			store = MongoDataStore.getInstanceOffer( );
//			ObjectMapper objectMapper = new ObjectMapper( );
//			String offerModelAsString = objectMapper.writeValueAsString( offerModel );
//			store.storeRawEventOffer( offerModelAsString );
		}
		catch (Exception e)
		{
			LOG.error( "Error KafkaConsumerOffer.processMessage() ", e );
			e.printStackTrace( );
		}
	}
}