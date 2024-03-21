package org.darkmindfx.httpserver;

import java.io.*;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

public class ResourceCache implements Runnable  {

    class ResourceRecord {

        private LocalDateTime expires;

        private LocalDateTime lastModified;

        private byte[] content;

        public ResourceRecord(LocalDateTime expires, LocalDateTime lastModified, byte[] content) {
            this.setContent(content);
            this.setExpires(expires);
            this.setLastModified(lastModified);
        }

        public LocalDateTime getExpires() { return this.expires; }

        public void setExpires( LocalDateTime newExpires ) { this.expires = newExpires; }

        public LocalDateTime getLastModified() { return this.lastModified; }

        public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }

        public byte[] getContent() { return this.content; }

        public void setContent(byte[] content) { this.content = content; }
    }

    private Dictionary<String, ResourceRecord> resources;
    private Integer cacheValidationInterval = 0;
    private Integer cacheExpirationInterval = 3600;
    private String rootFolder = "";

    public ResourceCache(Properties serverProperties) {
        this.resources = new Hashtable<>();
        this.cacheValidationInterval = Integer.parseInt(serverProperties.getProperty("CACHE_VALIDATION_INTERVAL_SEC"));
        this.cacheExpirationInterval = Integer.parseInt(serverProperties.getProperty("CACHE_EXP_INTERVAL_SEC"));
        this.rootFolder = serverProperties.getProperty("ROOT_FOLDER");
    }

    @Override
    public void run() {

    }

    public byte[] getResourceContent(String resource) throws FileNotFoundException, IOException {
        byte[] content = null;
        ResourceRecord record = this.resources.get(resource.toLowerCase());

        if(record == null || record.expires.isBefore(LocalDateTime.now())) {
            record = reloadResource(resource);
        }

        content = record.getContent();

        return content;

    }

    private ResourceRecord reloadResource(String resource) throws FileNotFoundException, IOException {

        String path = Path.of(this.rootFolder, resource).toString();
        File file = new File(path);
        if(file.exists()) {
            byte[] content = new byte[(int)file.length()];
            InputStream is = new FileInputStream(file);
            int bytesRead = is.read(content);

            LocalDateTime expires = LocalDateTime.now().plusSeconds(this.cacheExpirationInterval);
            LocalDateTime lastModified = Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime();

            ResourceRecord record = new ResourceRecord( expires,
                                                        lastModified,
                                                        content);

            synchronized (this.resources) {
                this.resources.remove(resource.toLowerCase());
                this.resources.put(resource.toLowerCase(), record);
            }

            return record;
        }
        else {
            throw new FileNotFoundException(String.format("Resource not found: %s", resource));
        }

    }



}
