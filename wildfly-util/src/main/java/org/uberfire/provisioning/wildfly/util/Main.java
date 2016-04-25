/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.wildfly.util;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

/**
 *
 * @author salaboy
 */
public class Main {

    public static void main(String[] args) throws IOException {
        HttpHost target = new HttpHost("localhost", 9990, "http");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(target.getHostName(), target.getPort()),
                new UsernamePasswordCredentials("salaboy", "salaboy123$"));
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();

        try {

            // Create AuthCache instance
//            AuthCache authCache = new BasicAuthCache();
//            // Generate DIGEST scheme object, initialize it and add it to the local
//            // auth cache
//            DigestScheme digestAuth = new DigestScheme();
//
//            authCache.put(target, digestAuth);
//
//            // Add AuthCache to the execution context
//            HttpClientContext localContext = HttpClientContext.create();
//            localContext.setAuthCache(authCache);

            HttpPost httppost = new HttpPost("http://localhost:9990/management/add-content");

            httppost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());

            StringBody stringBody = new StringBody("/Users/salaboy/MyApps/kie-wb-testing/wildfly-8.2.1.Final/standalone/deployments/kie-wb.war", ContentType.TEXT_PLAIN);

            
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setContentType(ContentType.MULTIPART_FORM_DATA);
            builder.addPart("file", stringBody);

            HttpEntity entity = builder.build();

            httppost.setEntity(entity);

            System.out.println("Executing request " + httppost.getRequestLine() + " to target " + target);

//            for (int i = 0; i < 3; i++) {
                CloseableHttpResponse response = httpclient.execute(target, httppost);
                try {
                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    System.out.println(EntityUtils.toString(response.getEntity()));
                    System.out.println("Headers:");
                    for(Header h : response.getAllHeaders()){
                        System.out.println("H name: "+h.getName());
                        System.out.println("H value: "+h.getValue());
                    }
                } finally {
                    response.close();
                }
//            }
        } finally {
            httpclient.close();
        }

    }
}
