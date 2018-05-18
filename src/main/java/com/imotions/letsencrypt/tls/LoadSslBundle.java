package com.imotions.letsencrypt.tls;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.imotions.letsencrypt.core.LetsEncryptConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.jetty.HttpsConnectorFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Bundle that fetches the keystore file from a configured location on S3, which Dropwizard will use to
 * instantiate it's HttpsApplicationConnector.
 *
 * @param <T> Marker for interface and base Configuration to ensure that configurations needed to access S3
 *           and LetsEncrypt keystores are available.
 */
@Slf4j
public class LoadSslBundle<T extends Configuration & HasLetsEncryptConfiguration> implements ConfiguredBundle<T> {
    private String tempDir = ".";

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        tempDir = System.getProperty("java.io.tmpdir");
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        Optional<HttpsConnectorFactory> connector =
                LetsEncryptConfiguration.getHttpsConnector(configuration.getServerFactory());

        if (!connector.isPresent()) {
            log.info("Not using HTTPS, skipping LoadTLSBundle.");
            return;
        }

        log.info("Loading LoadTLSBundle");
        AmazonS3 s3Instance = configuration.getS3Config().createClient();
        String keystoreAlias = configuration.getLetsEncryptConfig().getDomainKeyStore().getName();


        Path toTempFile = Paths.get(tempDir, keystoreAlias);

        // cleanup if necessary
        if (toTempFile.toFile().exists()) {
            toTempFile.toFile().delete();
        }
        Path licenseKeystore = Files.createFile(toTempFile);

        String pathToKeyStore = configuration.getLetsEncryptConfig().getS3KeyStoreLocation() + keystoreAlias;
        GetObjectRequest getObjectRequest =
                new GetObjectRequest(
                        configuration.getS3Config().getBucket(),
                        pathToKeyStore
                );

        File licenseKeystoreTempFile = licenseKeystore.toFile();
        try {
            s3Instance.getObject(
                    getObjectRequest,
                    licenseKeystoreTempFile);

            log.info("Saved KeyStore file to: {}", licenseKeystoreTempFile.getAbsolutePath());
            log.info("Saved KeyStore file has size in bytes: {}", licenseKeystoreTempFile.length());
            connector.get().setKeyStorePath(licenseKeystoreTempFile.getAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve KeyStore: " + pathToKeyStore + " from S3", e);
        }
        log.info("KeyStore succesfully loaded from S3.");
    }
}
