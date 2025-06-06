package vu.oop.passwordmanager.util;

/**
 * HelperDomainObject is a simple Java class that represents a domain object
 * with properties such as index, domain name, username, and password.
 * It implements Serializable and Cloneable interfaces for object serialization
 * and cloning capabilities.
 * @author Dovydas Kelečius
 * Contact: kelecius.dovydas@gmail.com
 * @version 1.1
 * @since 2025-05-31
 * * This class is part of the vu.oop.passwordmanager.util package,
 */
public class HelperDomainObject implements java.io.Serializable, Cloneable {
    private Integer index;
    private String entryName;
    private String domainName;
    private String domainUsername;
    private String domainPassword;

    public HelperDomainObject(Integer index, String entryName, String domainName, String domainUsername, String domainPassword) {
        this.index = index;
        this.entryName = entryName;
        this.domainName = domainName;
        this.domainUsername = domainUsername;
        this.domainPassword = domainPassword;
    }

    public Integer getIndex() {
        return index;
    }

    public String getEntryName() {
        return entryName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainUsername() {
        return domainUsername;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public void setDomainUsername(String domainUsername) {
        this.domainUsername = domainUsername;
    }

    public String getDomainPassword() {
        return domainPassword;
    }

    public void setDomainPassword(String domainPassword) {
        this.domainPassword = domainPassword;
    }

    @Override
    public String toString() {
        return "HelperDomainObject{" +
                "index=" + index +
                ", entryName='" + entryName + '\'' +
                ", domainName='" + domainName + '\'' +
                ", domainUsername='" + domainUsername + '\'' +
                ", domainPassword='" + domainPassword + '\'' +
                '}';
    }

    @Override
    public HelperDomainObject clone() {
        try {
            return (HelperDomainObject) super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }
}
