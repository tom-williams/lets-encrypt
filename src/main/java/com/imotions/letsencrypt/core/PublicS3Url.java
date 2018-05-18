package com.imotions.letsencrypt.core;

import io.dropwizard.validation.ValidationMethod;
import lombok.Getter;

import java.net.URI;

/**
 * The URL where files that are uploaded to S3 can be accessed.
 *
 * In production we do not want to use the raw S3 bucket name, as we would rather go through Cloudfront for caching and to have a consistent URL scheme.
 * Therefore we configure this to point to e.g. "https://my.imotions.com" and configure Cloudfront to forward requests with URLs like "/api/content/*" to the S3 bucket.
 *
 * When developing we don't have Cloudfront (and don't want to set up Charles to mimic it) and therefore set it to point directly at the fake S3 development server which includes the bucket name.
 */
@Getter
public class PublicS3Url {
    private URI url;

    public PublicS3Url(String url) {
        this.url = URI.create(url);
    }

    @ValidationMethod(message = "public s3 url must end with a /")
    public boolean isTrailingSlash() {
        return url.toString().endsWith("/");
    }

    @Override
    public String toString() {
        return url.toString();
    }
}
