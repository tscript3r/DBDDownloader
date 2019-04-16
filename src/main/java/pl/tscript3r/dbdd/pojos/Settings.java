package pl.tscript3r.dbdd.pojos;

import javax.swing.*;
import java.io.Serializable;

public class Settings implements Serializable {

    private static final long serialVersionUID = 1L;
    private String hostname;
    private String savePath;
    private JComboBox<String> projectsComboBox;
    private String ticket;

    public Settings() {
    }

    public Settings(String hostname, String savePath, JComboBox<String> projectsComboBox, String ticket) {
        this.hostname = hostname;
        this.savePath = savePath;
        this.projectsComboBox = projectsComboBox;
        this.ticket = ticket;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public JComboBox<String> getProjectsComboBox() {
        return projectsComboBox;
    }

    public void setProjectsComboBox(JComboBox<String> projectsComboBox) {
        this.projectsComboBox = projectsComboBox;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "hostname='" + hostname + '\'' +
                ", savePath='" + savePath + '\'' +
                ", projectsComboBox=" + projectsComboBox +
                ", ticket='" + ticket + '\'' +
                '}';
    }
}
