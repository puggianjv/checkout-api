//package br.com.hitbra.dao;
//
//import br.com.hitbra.service.ServiceEventRequest;
//import com.mongodb.*;
//import com.mongodb.util.JSON;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.apache.log4j.Logger.*;
//
//@Configuration
//public class MongoDataStore implements DataStore
//{
//	private static final Logger LOG = getLogger( MongoDataStore.class.getName( ) );
//
//	private static DataStore mongoDataStore;
//	private static DBCollection rawEventsCollOffer;
//	private static DBCollection rawEventsCollShop;
//	private static DBCollection rawEventsCollError;
//	private static DBCollection rawEventsCollTracking ;
//
//	@Value( "${hmk.mongodb.uri.primary}" )
//	private static String MONGO_URI_HOST_PRIMARY ;
//
//	@Value( "${hmk.mongodb.uri.secondary}" )
//	private static String MONGO_URI_HOST_SECONDARY ;
//
//	@Value( "${hmk.mongodb.collection.shop}" )
//	private static String SHOP_COLLECTION;
//
//	@Value( "${hmk.mongodb.collection.offer}" )
//	private static String OFFER_COLLECTION;
//
//	@Value( "${hmk.mongodb.collection.error}" )
//	private static String ERROR_COLLECTION;
//
//	@Value( "${hmk.mongodb.collection.tracking}" )
//	private static String TRACKING_COLLECTION;
//
//	@Value( "${hmk.mongodb.database}" )
//	private static String MONGO_DATABASE ;
//
//	public MongoDataStore( )
//	{
//		System.out.println( " :::  MongoDataStore Configuration ::: " ) ;
//
//		if ( MONGO_URI_HOST_PRIMARY == null )
//		{
//			System.out.println( " Configuration is null " ) ;
//			MONGO_URI_HOST_PRIMARY = "mongodb://hitbra:12345@ds239372-a0-external.gxl53.fleet.mlab.com:39372/hitbra_develop?ssl=true" ;
//			MONGO_URI_HOST_SECONDARY = "mongodb://hitbra:12345@ds239372-a1-external.gxl53.fleet.mlab.com:39372/hitbra_develop?ssl=true" ;
//			SHOP_COLLECTION = "event_shop_outbound" ;
//			OFFER_COLLECTION = "event_offer_outbound" ;
//			ERROR_COLLECTION = "event_errors" ;
//			TRACKING_COLLECTION= "event_tracking" ;
//			MONGO_DATABASE="hitbra_develop" ;
//		}
//		System.out.println( " MONGO_URI_HOST_PRIMARY -> " + MONGO_URI_HOST_PRIMARY ) ;
//		System.out.println( " MONGO_URI_HOST_SECONDARY -> " + MONGO_URI_HOST_SECONDARY ) ;
//		System.out.println( " MONGO_DATABASE -> " + MONGO_DATABASE ) ;
//		System.out.println( " SHOP_COLLECTION -> " + SHOP_COLLECTION ) ;
//		System.out.println( " OFFER_COLLECTION -> " + OFFER_COLLECTION ) ;
//		System.out.println( " ERROR_COLLECTION -> " + ERROR_COLLECTION ) ;
//		System.out.println( " TRACKING_COLLECTION -> " + TRACKING_COLLECTION ) ;
//	}
//
//	public static DataStore getInstanceShop( )
//	{
//		LOG.info( "MongoDataStore.getInstanceShop starting " );
//		System.out.println( "MongoDataStore.getInstanceShop starting " ) ;
//		try
//		{
//			synchronized (MongoDataStore.class)
//			{
//				if (mongoDataStore == null)
//				{
//					mongoDataStore = new MongoDataStore( );
//				}
//				MongoClientURI uri = new MongoClientURI( MONGO_URI_HOST_PRIMARY ) ;
//				MongoClient mongoClient = new MongoClient(uri);
//				DB db = mongoClient.getDB( MONGO_DATABASE );
//
//				rawEventsCollShop = db.getCollection( SHOP_COLLECTION );
//			}
//		}
//		catch( Exception exception )
//		{
//			mongoDataStore = null ;
//			LOG.error( "Error DataStore.getInstanceShop() ", exception );
//		}
//		return mongoDataStore;
//	}
//
//	public static DataStore getInstanceOffer( )
//	{
//		LOG.info( "MongoDataStore.getInstanceOffer starting " );
//		System.out.println( "MongoDataStore.getInstanceOffer starting " ) ;
//		try
//		{
//			synchronized (MongoDataStore.class)
//			{
//				if (mongoDataStore == null)
//				{
//					mongoDataStore = new MongoDataStore( );
//				}
//				MongoClientURI uri = new MongoClientURI( MONGO_URI_HOST_PRIMARY );
//				MongoClient mongoClient = new MongoClient( uri );
//				DB db = mongoClient.getDB( MONGO_DATABASE );
//				rawEventsCollOffer = db.getCollection( OFFER_COLLECTION );
//			}
//		}
//		catch( Exception exception )
//		{
//			mongoDataStore = null ;
//			LOG.error( "Error DataStore.getInstanceOffer() ", exception );
//		}
//		return mongoDataStore;
//	}
//
//	public static DataStore getInstanceError( )
//	{
//		LOG.info( "MongoDataStore.getInstanceError starting " );
//		System.out.println( "MongoDataStore.getInstanceError starting " ) ;
//		try
//		{
//			synchronized (MongoDataStore.class)
//			{
//				if (mongoDataStore == null)
//				{
//					mongoDataStore = new MongoDataStore( );
//				}
//				MongoClientURI uri = new MongoClientURI( MONGO_URI_HOST_PRIMARY ) ;
//				MongoClient mongoClient = new MongoClient(uri);
//				DB db = mongoClient.getDB( MONGO_DATABASE );
//
//				rawEventsCollError = db.getCollection( ERROR_COLLECTION );
//			}
//		}
//		catch( Exception exception )
//		{
//			mongoDataStore = null ;
//			LOG.error( "Error DataStore.getInstanceShop() ", exception );
//		}
//		return mongoDataStore;
//	}
//
//	public static DataStore getInstanceTracking( )
//	{
//		LOG.info( "MongoDataStore.getInstanceTracking starting " );
//		System.out.println( "MongoDataStore.getInstanceTracking starting " ) ;
//		try
//		{
//			synchronized (MongoDataStore.class)
//			{
//				if (mongoDataStore == null)
//				{
//					mongoDataStore = new MongoDataStore( );
//				}
//				MongoClientURI uri = new MongoClientURI( MONGO_URI_HOST_PRIMARY ) ;
//				MongoClient mongoClient = new MongoClient(uri);
//				DB db = mongoClient.getDB( MONGO_DATABASE );
//
//				rawEventsCollTracking = db.getCollection( TRACKING_COLLECTION );
//			}
//		}
//		catch( Exception exception )
//		{
//			mongoDataStore = null ;
//			LOG.error( "Error DataStore.getInstanceTracking() ", exception );
//		}
//		return mongoDataStore;
//	}
//
//	public boolean storeRawEventOffer( String jsonData )
//	{
//		LOG.info( "MongoDataStore.storeRawEventOffer starting " );
//		System.out.println( "MongoDataStore.storeRawEventOffer starting " ) ;
//
//		DBObject rawEvent = ( DBObject ) JSON.parse( jsonData );
//		LOG.info( "persist offer " + rawEvent + " :: " + rawEventsCollOffer );
//		boolean success = false;
//
//		if (rawEventsCollOffer.insert( rawEvent ) != null)
//		{
//			success = true;
//		}
//		return success;
//	}
//
//	public boolean storeRawEventError( String jsonData )
//	{
//		LOG.info( "MongoDataStore.storeRawEventError starting " );
//		System.out.println( "MongoDataStore.storeRawEventError starting " ) ;
//
//		DBObject rawEvent = ( DBObject ) JSON.parse( jsonData );
//		LOG.info( "persist error " + rawEvent + " :: " + rawEventsCollError );
//		boolean success = false;
//
//		if (rawEventsCollError.insert( rawEvent ) != null)
//		{
//			success = true;
//		}
//		return success;
//	}
//
//	public boolean storeRawEventShop( String jsonData )
//	{
//		LOG.info( "MongoDataStore.storeRawEventShop starting " );
//		System.out.println( "MongoDataStore.storeRawEventShop starting " ) ;
//
//		DBObject rawEvent = ( DBObject ) JSON.parse( jsonData );
//		LOG.info( "persist shop " + rawEvent + " :: " + rawEventsCollShop );
//		boolean success = false;
//
//		if (rawEventsCollShop.insert( rawEvent ) != null)
//		{
//			success = true;
//		}
//		return success;
//	}
//
//	public boolean storeRawEventTracking( String jsonData )
//	{
//		LOG.info( "MongoDataStore.storeRawEventTracking starting " );
//		System.out.println( "MongoDataStore.storeRawEventTracking starting " ) ;
//
//		DBObject rawEvent = ( DBObject ) JSON.parse( jsonData );
//		LOG.info( "persist tracking " + rawEvent + " :: " + rawEventsCollTracking );
//		boolean success = false;
//
//		if (rawEventsCollTracking.insert( rawEvent ) != null)
//		{
//			success = true;
//		}
//		return success;
//	}
//
//	public boolean removeRawEventOffer( DBObject rawEvent )
//	{
//		LOG.info( "remove offer " + rawEvent + " :: " + rawEventsCollOffer );
//		System.out.println( "remove offer " + rawEvent + " :: " + rawEventsCollOffer );
//
//		boolean success = false;
//
//		if (rawEventsCollOffer.remove( rawEvent ) != null)
//		{
//			success = true;
//		}
//		return success;
//	}
//
//	public boolean removeRawEventShop( DBObject rawEvent )
//	{
//		LOG.info( "remove shop " + rawEvent + " :: " + rawEventsCollShop );
//		System.out.println( "remove shop " + rawEvent + " :: " + rawEventsCollShop );
//
//		boolean success = false;
//
//		if (rawEventsCollShop.remove( rawEvent ) != null)
//		{
//			success = true;
//		}
//		return success;
//	}
//
//	public boolean removeRawEventTracking( DBObject rawEvent )
//	{
//		LOG.info( "remove tracking " + rawEvent + " :: " + rawEventsCollTracking );
//		System.out.println( "remove tracking " + rawEvent + " :: " + rawEventsCollTracking );
//
//		boolean success = false;
//
//		if (rawEventsCollTracking.remove( rawEvent ) != null)
//		{
//			success = true;
//		}
//		return success;
//	}
//
//	@Override
//	public List<ServiceEventRequest> getAllOffer( )
//	{
//		LOG.info( "MongoDataStore.getAllOffer starting " );
//		System.out.println( "MongoDataStore.getAllOffer starting " ) ;
//
//		BasicDBObject query = new BasicDBObject( );
//		DBCursor cursor = rawEventsCollOffer.find( query );
//		DBObject data = null;
//		List<ServiceEventRequest> list = new ArrayList<ServiceEventRequest>( );
//		while (cursor.hasNext( ))
//		{
//			data = cursor.next( );
//			ServiceEventRequest request = new ServiceEventRequest( );
//			request.setName( ( String ) data.get( "name" ) );
//			list.add( request );
//			LOG.info( "print offer :: getAllOffer() :: " + data ) ;
//		}
//		return list;
//	}
//
//	@Override
//	public List<ServiceEventRequest> getAllShop( )
//	{
//		LOG.info( "MongoDataStore.getAllShop starting " );
//		System.out.println( "MongoDataStore.getAllShop starting " ) ;
//
//		BasicDBObject query = new BasicDBObject( );
//		DBCursor cursor = rawEventsCollShop.find( query );
//		DBObject data = null;
//		List<ServiceEventRequest> list = new ArrayList<ServiceEventRequest>( );
//		while (cursor.hasNext( ))
//		{
//			data = cursor.next( );
//
//			ServiceEventRequest request = new ServiceEventRequest( );
//			request.setName( ( String ) data.get( "name" ) );
//			list.add( request );
//			LOG.info( "print shop :: getAllShop() :: " + data ) ;
//
//		}
//		return list;
//	}
//
//	@Override
//	public List<DBObject> getAllObjectsOffer( )
//	{
//		LOG.info( "MongoDataStore.getAllObjectsOffer starting " );
//		System.out.println( "MongoDataStore.getAllObjectsOffer starting " ) ;
//
//		DBObject removeIdProjection = new BasicDBObject("_id", 0);
//		BasicDBObject query = new BasicDBObject( );
//		DBCursor cursor = rawEventsCollOffer.find( query, removeIdProjection );
//		DBObject data = null;
//		List<DBObject> list = new ArrayList<DBObject>( );
//		while (cursor.hasNext( ))
//		{
//			data = cursor.next( );
//			LOG.info( "print all json objects :: getAllObjectsOffer() ::  " + JSON.serialize( data )  ) ;
//			list.add( data );
//		}
//		return list;
//	}
//
//	@Override
//	public List<DBObject> getAllObjectsShop( )
//	{
//		LOG.info( "MongoDataStore.getAllObjectsShop starting " );
//		System.out.println( "MongoDataStore.getAllObjectsShop starting " ) ;
//
//		DBObject removeIdProjection = new BasicDBObject("_id", 0);
//		BasicDBObject query = new BasicDBObject( );
//		DBCursor cursor = rawEventsCollShop.find( query, removeIdProjection );
//		DBObject data = null;
//		List<DBObject> list = new ArrayList<DBObject>( );
//		while (cursor.hasNext( ))
//		{
//			data = cursor.next( );
//			LOG.info( "print all json objects :: getAllObjectsShop() ::  " + JSON.serialize( data )  ) ;
//			list.add( data );
//		}
//		return list;
//	}
//
//	@Override
//	public List<DBObject> getAllObjectsTracking( )
//	{
//		LOG.info( "MongoDataStore.getAllObjectsTracking starting " );
//		System.out.println( "MongoDataStore.getAllObjectsTracking starting " ) ;
//
//		DBObject removeIdProjection = new BasicDBObject("_id", 0);
//		BasicDBObject query = new BasicDBObject( );
//		DBCursor cursor = rawEventsCollTracking.find( query, removeIdProjection );
//		DBObject data = null;
//		List<DBObject> list = new ArrayList<DBObject>( );
//		while (cursor.hasNext( ))
//		{
//			data = cursor.next( );
//			LOG.info( "print all json objects :: getAllObjectsTracking() ::  " + JSON.serialize( data )  ) ;
//			list.add( data );
//		}
//		return list;
//	}
//}