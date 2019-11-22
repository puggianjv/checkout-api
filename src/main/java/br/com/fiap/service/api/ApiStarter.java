package br.com.fiap.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.apache.log4j.Logger.getLogger;

public class ApiStarter extends ApiConfig
{
	private static final Logger LOG = getLogger( ApiStarter.class.getName( ) );

	public ApiStarter( )
	{
		super.getToken( ) ;
	}

	public void execute( Object offer )
	{
		String offerAsString = null ;
		try
		{
			LOG.info( " :: ApiStarter.execute() starting ::" );
			System.out.println( " :: ApiStarter.execute() starting ::" );

			// uri information
			final UriComponents uri = UriComponentsBuilder.fromUriString( BASEURL )
					.path( URICREATEOFFER ).build( ).encode( );

			// SSL security
			SSLContextBuilder sslcontext = new SSLContextBuilder( );
			sslcontext.loadTrustMaterial( null, new TrustSelfSignedStrategy( ) );
			CloseableHttpClient httpClient = HttpClients.custom( )
					.setSSLContext( sslcontext.build( ) )
					.setSSLHostnameVerifier( NoopHostnameVerifier.INSTANCE ).build( );
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory( );
			requestFactory.setHttpClient( httpClient );

			// body information
			ObjectMapper objectMapper = new ObjectMapper( );
			offerAsString = objectMapper.writeValueAsString( offer );

			System.out.println( "----------------------------------------------------------------------------------------------------------------------------------" ) ;
			System.out.println( "  REST API  :: ApiStarter.execute() body :: " + uri.toString() + " - "  + offerAsString );
			System.out.println( "----------------------------------------------------------------------------------------------------------------------------------" ) ;

			LOG.debug( "########################## :: ApiStarter.execute() body :: " + uri.toString() + " - "  + offerAsString );

			// send Rest
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate
					.postForEntity( uri.toString( ), getRequest( offerAsString ), String.class );

			System.out.println( "----------------------------------------------------------------------------------------------------------------------------------" ) ;
			System.out.println( "  STATUS: << OK >>  ApiStarter.execute()" + offerAsString ) ;
			System.out.println( "----------------------------------------------------------------------------------------------------------------------------------" ) ;

			System.out.println( "##########################  :: ApiStarter.execute() OK ::" + offerAsString );
			LOG.debug( "##########################  :: ApiStarter.execute() OK ::" + offerAsString );

		}
		catch (HttpClientErrorException e)
		{
			LOG.error( " :: ApiStarter.execute() :: HttpClientErrorException :: " + e.getStatusCode( ) + " :: " + e.getResponseBodyAsString( ) );
			LOG.error( "Error :: HttpClientErrorException :: ApiStarter.execute() ", e ) ;
			System.out.println( " :: ApiStarter.execute() :: HttpClientErrorException :: " + e.getStatusCode( ) + " :: " + e.getResponseBodyAsString( ) );

			// se o error for 422 com errorCode == 1 entao a Offer ja existe
//			DataStore store = null;
			try
			{
//				store = MongoDataStore.getInstanceError( );
//				store.storeRawEventError( "{ \"error\": \"" + e.getMessage() + " Create-Update Offer \", \"offerId\":\""
//						+ offer.getOffer() + "\",\"message\":"+ e.getResponseBodyAsString( )
//						+ ", \"offer\":" + offerAsString + "}") ;
			}
			catch (Exception exception)
			{
				LOG.error( "Error ApiStarter.exception() ", exception );
				exception.printStackTrace( );
			}
		}
		catch (Exception e)
		{
			LOG.error( " Error :: Exception :: ApiStarter.execute() ", e );
			System.out.println( " ################ ApiStarter.execute() :: HttpClientErrorException :: " + e.getMessage( ) );
		}
	}
}