package cz.uhk.fim.bs.pickngo_mobile_be.CustomUser;

import java.util.Map;

public class AzureUserInfo {
    private Map<String, Object> attributes;

    public AzureUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return (String) attributes.get("sub");
    }

    public String getName() {
        return (String) attributes.get("name");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getEmailShort(){
        return (String) attributes.get("preferred_username");
    }
}
