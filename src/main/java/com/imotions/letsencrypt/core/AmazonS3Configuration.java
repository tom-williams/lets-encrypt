package com.imotions.letsencrypt.core;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import io.dropwizard.validation.ValidationMethod;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;

import java.net.URI;

@Slf4j
@Getter
@Setter
public class AmazonS3Configuration {
    @NotEmpty
    private String accessKeyId = null;
    @NotEmpty
    private String secretKey = null;
    @NotEmpty
    private String bucket = null;
    private String region = null;
    private URI endpoint = null;

    public AWSCredentialsProvider getCredentials() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretKey));
    }

    public Regions getTypedRegion() {
        return Regions.fromName(region);
    }

    public Bucket getTypedBucket() {
        return new Bucket(bucket);
    }

    /**
     * Only for tests.
     */
    public PublicS3Url getUrlWithBucket() {
        Preconditions.checkState(region == null);
        return new PublicS3Url(endpoint.toString() + "/" + bucket + "/");
    }

    public AmazonS3 createClient() {
        AmazonS3ClientBuilder s3 = AmazonS3ClientBuilder.standard().withCredentials(getCredentials());
        if (region != null) {
            log.info("Connecting to S3 bucket '{}' in region '{}'.", bucket, region);
            s3.withRegion(getTypedRegion());
        } else {
            log.info("Connecting to fake S3 bucket '{}' via test endpoint '{}'.", bucket, endpoint);
            s3.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint.toString(), null));
            // See https://github.com/jubos/fake-s3/wiki/Supported-Clients#java
            s3.withPathStyleAccessEnabled(true);
        }
        return s3.build();
    }

    @ValidationMethod(message = "either region or endpoint must be specified")
    public boolean isRegionOrEndpoint() {
        return !Strings.isNullOrEmpty(region) || endpoint == null || !Strings.isNullOrEmpty(endpoint.toString());
    }
}
