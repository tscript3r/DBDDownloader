package pl.tscript3r.dbdd.utils;

import pl.tscript3r.dbdd.pojos.Settings;

import javax.swing.*;
import java.io.*;

public class SettingsLoader {

    private static final File SETTINGS_FILE = new File("settings.cfg");
    private static final String[] PROJECTS_SHORT_LIST = {"HP_TPMDE", "HPI_TPM", "HP_TPMUK", "HP_TPMFR", "HP_PMKIT",
            "HPEON_IMAC2", "HPEON_IMACGSEM", "None"};

    private Settings settings = new Settings();

    public SettingsLoader() throws IOException, ClassNotFoundException {
        if (!(SETTINGS_FILE.exists()))
            setDefaultSettings();
        else
            loadSettings();
    }

    private void setDefaultSettings() {
        settings.setHostname("");
        settings.setSavePath("\\\\Client\\Z$\\C\\log");
        settings.setTicket("");
        settings.setProjectsComboBox(new JComboBox<>(PROJECTS_SHORT_LIST));
    }

    private void loadSettings() throws IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream(SETTINGS_FILE);
        ObjectInputStream oi = new ObjectInputStream(fi);
        try {
            settings = (Settings) oi.readObject();
        } finally {
            oi.close();
            fi.close();
        }
    }

    public void save() throws IOException {
        FileOutputStream f = new FileOutputStream(SETTINGS_FILE);
        ObjectOutputStream o = new ObjectOutputStream(f);
        try {
            o.writeObject(settings);
        } finally {
            o.close();
            f.close();
        }
    }

    public Settings getSettings() {
        return settings;
    }

}
