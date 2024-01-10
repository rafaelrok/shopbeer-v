package br.com.rafaelvieira.shopbeer.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

//@Profile("dev")
@Configuration
@PropertySource(value = { "file://${HOME}/.shopbeer-s3.properties" }, ignoreResourceNotFound = true)
public class S3Config {

	@Value("${aws_access_key_id}")
	private String awsKeyId;

	@Value("${aws_access_secret_key}")
	private String awsSecretKey;

	@Value("${aws_region}")
	private String awsRegion;

	@Bean
	public AmazonS3 amazonS3() {
		AWSCredentials credenciais = new BasicAWSCredentials(
				Objects.requireNonNull(awsKeyId), Objects.requireNonNull(awsSecretKey));
		AmazonS3 amazonS3 = new AmazonS3Client(credenciais, new ClientConfiguration());
		Region region = Region.getRegion(Regions.US_WEST_1);
		amazonS3.setRegion(region);
		return amazonS3;
	}

//	@Bean
//	public AmazonS3 s3client() {
//		BasicAWSCredentials awsCred = new BasicAWSCredentials(awsKeyId, awsSecretKey);
//		return AmazonS3ClientBuilder.standard()
//				.withRegion(Regions.fromName(awsRegion))
//				.withCredentials(new AWSStaticCredentialsProvider(awsCred))
//				.build();
//	}
	
}
