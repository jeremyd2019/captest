/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2015-11-16 19:10:01 UTC)
 * on 2016-01-05 at 20:54:32 UTC 
 * Modify at your own risk.
 */

package com.appspot.captest.alerts;

/**
 * Service definition for Alerts (v1).
 *
 * <p>
 * Alerts API v1.
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link AlertsRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Alerts extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.21.0 of the alerts library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://captest.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "alerts/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Alerts(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  Alerts(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * An accessor for creating requests from the AlertsOperations collection.
   *
   * <p>The typical use is:</p>
   * <pre>
   *   {@code Alerts alerts = new Alerts(...);}
   *   {@code Alerts.AlertsOperations.List request = alerts.alerts().list(parameters ...)}
   * </pre>
   *
   * @return the resource collection
   */
  public AlertsOperations alerts() {
    return new AlertsOperations();
  }

  /**
   * The "alerts" collection of methods.
   */
  public class AlertsOperations {

    /**
     * Create a request for the method "alerts.getAlerts".
     *
     * This request holds the parameters needed by the alerts server.  After setting any optional
     * parameters, call the {@link GetAlerts#execute()} method to invoke the remote operation.
     *
     * @return the request
     */
    public GetAlerts getAlerts() throws java.io.IOException {
      GetAlerts result = new GetAlerts();
      initialize(result);
      return result;
    }

    public class GetAlerts extends AlertsRequest<com.appspot.captest.alerts.model.CAPAlertCollection> {

      private static final String REST_PATH = "alerts";

      /**
       * Create a request for the method "alerts.getAlerts".
       *
       * This request holds the parameters needed by the the alerts server.  After setting any optional
       * parameters, call the {@link GetAlerts#execute()} method to invoke the remote operation. <p>
       * {@link
       * GetAlerts#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
       * must be called to initialize this instance immediately after invoking the constructor. </p>
       *
       * @since 1.13
       */
      protected GetAlerts() {
        super(Alerts.this, "GET", REST_PATH, null, com.appspot.captest.alerts.model.CAPAlertCollection.class);
      }

      @Override
      public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
        return super.executeUsingHead();
      }

      @Override
      public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
        return super.buildHttpRequestUsingHead();
      }

      @Override
      public GetAlerts setAlt(java.lang.String alt) {
        return (GetAlerts) super.setAlt(alt);
      }

      @Override
      public GetAlerts setFields(java.lang.String fields) {
        return (GetAlerts) super.setFields(fields);
      }

      @Override
      public GetAlerts setKey(java.lang.String key) {
        return (GetAlerts) super.setKey(key);
      }

      @Override
      public GetAlerts setOauthToken(java.lang.String oauthToken) {
        return (GetAlerts) super.setOauthToken(oauthToken);
      }

      @Override
      public GetAlerts setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (GetAlerts) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public GetAlerts setQuotaUser(java.lang.String quotaUser) {
        return (GetAlerts) super.setQuotaUser(quotaUser);
      }

      @Override
      public GetAlerts setUserIp(java.lang.String userIp) {
        return (GetAlerts) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key("ugc_in")
      private java.util.List<java.lang.String> ugcIn;

      /**

       */
      public java.util.List<java.lang.String> getUgcIn() {
        return ugcIn;
      }

      public GetAlerts setUgcIn(java.util.List<java.lang.String> ugcIn) {
        this.ugcIn = ugcIn;
        return this;
      }

      @com.google.api.client.util.Key("fips_in")
      private java.util.List<java.lang.String> fipsIn;

      /**

       */
      public java.util.List<java.lang.String> getFipsIn() {
        return fipsIn;
      }

      public GetAlerts setFipsIn(java.util.List<java.lang.String> fipsIn) {
        this.fipsIn = fipsIn;
        return this;
      }

      @Override
      public GetAlerts set(String parameterName, Object value) {
        return (GetAlerts) super.set(parameterName, value);
      }
    }

  }

  /**
   * Builder for {@link Alerts}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link Alerts}. */
    @Override
    public Alerts build() {
      return new Alerts(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link AlertsRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setAlertsRequestInitializer(
        AlertsRequestInitializer alertsRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(alertsRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}