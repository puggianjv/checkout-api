package br.com.fiap.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.apache.log4j.Logger.*;

public class ApiConfig
{
	private static final Logger LOG = getLogger( ApiConfig.class.getName( ) );

	protected static final String AUTHORIZATION = "Authorization";
	protected static final String AUTHORIZATIONUSER = "fiap";
	protected static final String AUTHORIZATIONPASSWORD = "123456";

	private static final String formUsername = "username";
	private static final String formPassword = "password";
	private static final String formGrantType = "grant_type";
	private static final String formScope = "scope";

	private   static final String userName = "fiap";
	protected static final String password = "1234";
	protected static final String grantType = "password";
	protected static final String scope = "basic";

	protected static final String BASEURL =  "https://localhost:8080" ;
	protected static final String URITOKEN =  "/v1/partner/fiap/authorization/token" ;

	// endPoint - Offer
	protected static final String URICREATEOFFER =  "/v1/partner/fiap/avros/offer/create" ;

	protected Token token;

	public void getToken( )
	{
		try
		{
			LOG.info( " :: ApiConfig.getToken() starting ::" );
			System.out.println( " :: ApiConfig.getToken() starting ::" );

			// uri information
			final UriComponents uri = UriComponentsBuilder.fromUriString( BASEURL ).path( URITOKEN )
					.build( ).encode( );

			// SSL security
			SSLContextBuilder sslcontext = new SSLContextBuilder( );
			sslcontext.loadTrustMaterial( null, new TrustSelfSignedStrategy( ) );
			CloseableHttpClient httpClient = HttpClients.custom( )
					.setSSLContext( sslcontext.build( ) )
					.setSSLHostnameVerifier( NoopHostnameVerifier.INSTANCE ).build( );
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory( );
			requestFactory.setHttpClient( httpClient );
			RestTemplate restTemplate = new RestTemplate( requestFactory );

			// authorization
			restTemplate.getInterceptors( )
					.add( new BasicAuthorizationInterceptor( AUTHORIZATIONUSER,
							AUTHORIZATIONPASSWORD ) );

			// body information
			MultiValueMap<String, String> form = new LinkedMultiValueMap<>( );
			form.add( formUsername, userName );
			form.add( formPassword, password );
			form.add( formGrantType, grantType );
			form.add( formScope, scope );

			// send Rest
			ResponseEntity<String> result = restTemplate
					.postForEntity( uri.toString( ), form, String.class );

			if (( result != null ) && ( result.getBody( ) != null ))
			{
				// json to object
				ObjectMapper objectMapper = new ObjectMapper( );
				token = new Token( );
				token = objectMapper.readValue( result.getBody( ), Token.class );
			}
		}
		catch (Exception e)
		{
			LOG.error( "Error getToken() ", e );
			System.out.println( "Error getToken() " + e.getMessage( ) );
		}
	}

	protected HttpEntity<String> getRequest( String body )
	{
		return new HttpEntity<>( body, getHttpHeaders( ) );
	}

	protected HttpHeaders getHttpHeaders( )
	{
		HttpHeaders headers = new HttpHeaders( );
		headers.setContentType( MediaType.APPLICATION_JSON );
		headers.add( AUTHORIZATION, token.getToken_type( ) + " " + token.getAccess_token( ) );

		System.out.println( " header.content " + headers.get( "Content-Type" ) );
		System.out.println( " header.authorization " + headers.get( AUTHORIZATION ) );

		return headers;
	}
}