package br.com.fiap.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig
{
	@Value( "${fiap.kafka.bootstrap-servers}" )
	private String bootstrapServer;

	@Bean
	//TODO trocar value para objeto de destino
	public ProducerFactory<String, String> producerFactory( )
	{
		Map<String, Object> configProps = new HashMap<>( );
		configProps.put( ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer );
		configProps.put( ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class );
		configProps.put( ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class );
		return new DefaultKafkaProducerFactory<>( configProps );
	}

	//TODO trocar value para objeto de destino
	@Bean public KafkaTemplate<String, String> kafkaTemplateOffer( )
	{
		return new KafkaTemplate<>( producerFactory( ) );
	}
}