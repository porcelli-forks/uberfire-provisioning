/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.wildfly.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jboss.dmr.ModelNode;

/**
 *
 * @author salaboy Based on: https://github.com/heiko-braun/http-upload
 */
public class WildflyRemoteClient {

    public void deploy(String user, String password, String host, int port, String filePath) {

        // the digest auth backend
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope("localhost", 9990),
                new UsernamePasswordCredentials(user, password));

        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();

        HttpPost post = new HttpPost("http://" + host + ":" + port + "/management-upload");

        post.addHeader("X-Management-Client-Name", "HAL");

        // the file to be uploaded
        File file = new File(filePath);
        FileBody fileBody = new FileBody(file);

        // the DMR operation
        ModelNode operation = new ModelNode();
        operation.get("address").add("deployment", file.getName());
        operation.get("operation").set("add");
        operation.get("runtime-name").set(file.getName());
        operation.get("enabled").set(true);
        operation.get("content").add().get("input-stream-index").set(0);  // point to the multipart index used

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            operation.writeBase64(bout);
        } catch (IOException ex) {
            Logger.getLogger(WildflyRemoteClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        // the multipart
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("uploadFormElement", fileBody);
        builder.addPart("operation", new ByteArrayBody(bout.toByteArray(), ContentType.create("application/dmr-encoded"), "blob"));
        HttpEntity entity = builder.build();

        //entity.writeTo(System.out);
        post.setEntity(entity);

        try {
            HttpResponse response = httpclient.execute(post);
            System.out.println(">>> Deploying Response Entity: "+response.getEntity());
            System.out.println(">>> Deploying Response Satus: "+response.getStatusLine().getStatusCode());
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(WildflyRemoteClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
