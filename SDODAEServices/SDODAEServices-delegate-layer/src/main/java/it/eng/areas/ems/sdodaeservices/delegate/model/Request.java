
package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "application",
    "auth",
    "notifications"
})
public class Request {

    @JsonProperty("application")
    private String application;
    @JsonProperty("auth")
    private String auth;
    @JsonProperty("notifications")
    private List<Notification> notifications = new ArrayList<Notification>();


    /**
     * 
     * @return
     *     The application
     */
    @JsonProperty("application")
    public String getApplication() {
        return application;
    }

    /**
     * 
     * @param application
     *     The application
     */
    @JsonProperty("application")
    public void setApplication(String application) {
        this.application = application;
    }

    /**
     * 
     * @return
     *     The auth
     */
    @JsonProperty("auth")
    public String getAuth() {
        return auth;
    }

    /**
     * 
     * @param auth
     *     The auth
     */
    @JsonProperty("auth")
    public void setAuth(String auth) {
        this.auth = auth;
    }

    /**
     * 
     * @return
     *     The notifications
     */
    @JsonProperty("notifications")
    public List<Notification> getNotifications() {
        return notifications;
    }

    /**
     * 
     * @param notifications
     *     The notifications
     */
    @JsonProperty("notifications")
    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

}
