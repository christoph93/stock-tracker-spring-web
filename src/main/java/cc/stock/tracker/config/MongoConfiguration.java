package cc.stock.tracker.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import javax.annotation.PostConstruct;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    /*
    spring.data.mongodb.uri=mongodb+srv://admin:admin@christoph-fqybv.mongodb.net/sample_mflix?retryWrites=true&w=majority
    spring.data.mongodb.repositories.enabled=true
    spring.data.mongodb.host=127.0.0.1
    spring.data.mongodb.port=27017

     */

    @Value("${spring.data.mongodb.database}")
    private String database;


    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Autowired
    private MappingMongoConverter mongoConverter;

    // Converts . into a mongo friendly char
    @PostConstruct
    public void setUpMongoEscapeCharacterConversion() {
        mongoConverter.setMapKeyDotReplacement("_");
    }    

    @Override
    protected String getDatabaseName() {    	
        return database;
    }

	@Override
	public MongoClient mongoClient() {		
		return MongoClients.create(uri);
	}
}
