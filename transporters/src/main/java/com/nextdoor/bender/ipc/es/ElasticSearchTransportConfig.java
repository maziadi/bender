package com.nextdoor.bender.ipc.es;

import java.io.UnsupportedEncodingException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDefault;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.nextdoor.bender.ipc.TransportConfig;
import com.nextdoor.bender.utils.Passwords;

@JsonTypeName("ElasticSearch")
@JsonSchemaDescription("Writes events into a self-hosted ElasticSearch. This transport does not"
    + "work with AWS hosted ElasticSearch. Instead use Firehose transport with a destination of "
    + "your cluster. Note that events must be JSON serialized or transport will fail. Only "
    + "tested with version 2.3.x but may work with 5.x.")
public class ElasticSearchTransportConfig extends TransportConfig {

  @JsonSchemaDescription("HTTP auth username.")
  @JsonSchemaDefault("false")
  @JsonProperty(required = false)
  private String username = null;

  @JsonSchemaDescription("HTTP auth password.")
  @JsonSchemaDefault("false")
  @JsonProperty(required = false)
  private String password = null;

  @JsonSchemaDescription("ElasticSearch HTTP endpoint hostname.")
  @JsonProperty(required = true)
  private String hostname = null;

  @JsonSchemaDescription("ElasticSearch HTTP endpoint port.")
  @JsonSchemaDefault(value = "9200")
  @JsonProperty(required = false)
  @Min(1)
  @Max(65535)
  private Integer port = 9200;

  @JsonSchemaDescription("Use SSL connections (certificates are not validated).")
  @JsonSchemaDefault(value = "false")
  @JsonProperty(required = false)
  private Boolean useSSL = false;

  @JsonSchemaDescription("Use GZIP compression on HTTP calls.")
  @JsonSchemaDefault(value = "false")
  @JsonProperty(required = false)
  private Boolean useGzip = false;

  @JsonSchemaDescription("Index to write to.")
  @JsonProperty(required = true)
  private String index;

  @JsonSchemaDescription("ElasticSearch document type.")
  @JsonProperty(required = true)
  private String type;

  @JsonSchemaDescription("Maximum number of documents in  bulk api call.")
  @JsonSchemaDefault(value = "500")
  @JsonProperty(required = false)
  @Min(500)
  @Max(100000)
  private Integer batchSize = 500;

  @JsonSchemaDescription("Java time format to append to index name.")
  @JsonProperty(required = false)
  private String indexTimeFormat = null;

  @JsonSchemaDescription("Use hash id generated by Bender as document id.")
  @JsonSchemaDefault(value = "false")
  @JsonProperty(required = false)
  private Boolean useHashId = false;

  @JsonSchemaDescription("Socket timeout on HTTP connection.")
  @JsonSchemaDefault(value = "40000")
  @JsonProperty(required = false)
  @Min(1000)
  @Max(300000)
  private Integer timeout = 40000;

  @JsonSchemaDescription("Number of retries to make when a put failure occurs.")
  @JsonSchemaDefault(value = "0")
  @JsonProperty(required = false)
  @Min(0)
  @Max(10)
  private Integer retryCount = 0;

  @JsonSchemaDescription("Initial delay between retries. If more than one retries specified exponential backoff is used.")
  @JsonSchemaDefault(value = "1000")
  @JsonProperty(required = false)
  @Min(1)
  @Max(60000)
  private Long retryDelay = 1000l;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    /*
     * If password uses KMS then decrypt it.
     */
    if (this.password != null) {
      try {
        return Passwords.getPassword(this.password);
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
    }
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public Boolean isUseSSL() {
    return useSSL;
  }

  public void setUseSSL(Boolean useSSL) {
    this.useSSL = useSSL;
  }

  public Boolean isUseGzip() {
    return useGzip;
  }

  public void setUseGzip(Boolean useGzip) {
    this.useGzip = useGzip;
  }

  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getBatchSize() {
    return batchSize;
  }

  public void setBatchSize(Integer batchSize) {
    this.batchSize = batchSize;
  }

  public String getIndexTimeFormat() {
    return indexTimeFormat;
  }

  public void setIndexTimeFormat(String indexTimeFormat) {
    this.indexTimeFormat = indexTimeFormat;
  }

  @JsonProperty("use_hashid")
  public Boolean isUseHashId() {
    return useHashId;
  }

  @JsonProperty("use_hashid")
  public void setUseHashId(Boolean useHashId) {
    this.useHashId = useHashId;
  }

  public Integer getTimeout() {
    return timeout;
  }

  public void setTimeout(Integer timeout) {
    this.timeout = timeout;
  }

  public Integer getRetryCount() {
    return retryCount;
  }

  public void setRetryCount(Integer retryCount) {
    this.retryCount = retryCount;
  }

  public Long getRetryDelay() {
    return retryDelay;
  }

  public void setRetryDelay(Long retryDelay) {
    this.retryDelay = retryDelay;
  }

  @Override
  public Class<?> getFactoryClass() {
    return ElasticSearchTransportFactory.class;
  }
}