package pl.tscript3r.dbdd.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import static pl.tscript3r.dbdd.utils.AddressValidation.ipAddressValidate;
import static pl.tscript3r.dbdd.utils.FileIO.*;
import static pl.tscript3r.dbdd.utils.Logger.*;

@SuppressWarnings("FieldCanBeLocal")
public class DBDDownloader {

    private static final Integer CONNECTION_TIMEOUT = 15;

    private static final String STATUS_MENU_FILE = "Status";
    private static final String CONFIG_MENU = "Configuration Page";
    private static final String CONFIG_MENU_FILE = "Config";
    private static final String SUPPLIES_MENU = "Supplies Status";
    private static final String SUPPLIES_MENU_FILE = "Supplies";
    private static final String EVENT_LOG_MENU = "Event Log";
    private static final String EVENT_LOG_MENU_FILE = "EventLog";

    private static final String URL_PURE_PATTERN = "https://%s/";
    private static final String URL_PATTERN = "https://%s/hp/device/%s";

    private String configLink = "";
    private String suppliesLink = "";
    private String eventLogLink = "";
    private String hostnameFileName = "";
    private String savePath = "";
    private String hostname = "";

    public DBDDownloader() throws KeyManagementException, NoSuchAlgorithmException {
        OpenTrustManager.apply();
    }

    private Document downloadPage(String url) throws IOException {
        print("Downloading ", url);
        return Jsoup.connect(url).userAgent("Mozilla/5.0 Chrome/26.0.1410.64 Safari/537.31").timeout(CONNECTION_TIMEOUT * 1000)
                .followRedirects(true).maxBodySize(1024 * 1024 * 5).get();
    }

    private Elements extractElements(Document page) {
        Elements elements = page.getElementsByAttributeValueMatching("class", Pattern.compile("navigationControl"));
        if (elements.size() < 3) {
            elements = page.getElementsByAttributeValueMatching("class",
                    Pattern.compile("hpNavigationButtonUnselected"));
            if (elements.size() < 3) {
                elements = page.getElementsByAttributeValueMatching("id", Pattern.compile("InternalPages_Index_{0}"));
                if (elements.size() < 2)
                    return null;
            }
        }
        return elements;
    }

    private String getTitle(Element element) {
        String result = element.select("a[href]").text();
        if (result.length() < 3)
            result = element.select("a[href]").attr("title");
        return result;
    }

    private Boolean parseIndex(Document page) {
        if (page != null) {
            print("Parsing URLs");
            Elements elements = extractElements(page);
            if (elements != null)
                for (Element element : elements) {
                    String title = getTitle(element);

                    if (title.equals(CONFIG_MENU))
                        configLink = element.select("a[href]").attr("href");
                    if (title.equals(SUPPLIES_MENU) || title.equals(SUPPLIES_MENU + " Page"))
                        suppliesLink = element.select("a[href]").attr("href");
                    if (title.equals(EVENT_LOG_MENU) || title.equals(EVENT_LOG_MENU + " Page"))
                        eventLogLink = element.select("a[href]").attr("href");
                }

            if (!configLink.equals("") && !suppliesLink.equals("") && !eventLogLink.equals("")) {
                print("Parsing successful");
                return true;
            }
        }
        return false;
    }

    private void init() {
        configLink = "";
        suppliesLink = "";
        eventLogLink = "";
        disableUI();

    }

    private String generateLink(String sufix) {
        return String.format(URL_PATTERN, hostname, sufix);
    }

    private String generateLink() {
        return String.format(URL_PURE_PATTERN, hostname);
    }

    private void generateHostnameFileName() {
        if (ipAddressValidate(hostname)) {
            try {
                InetAddress addr;
                addr = InetAddress.getByName(hostname);
                hostnameFileName = addr.getHostName();
                print("Hostname was found as: ", hostnameFileName);
            } catch (UnknownHostException e) {
                hostnameFileName = hostname;
                print("Warning: could not get the hostname, file names will be saved using the IP address");
            }

        } else
            hostnameFileName = hostname;
        if (hostnameFileName.length() < 1)
            hostnameFileName = hostname;
    }

    private String generateSavePath(String sufix) {
        return savePath + hostnameFileName + "_" + sufix;
    }

    private Boolean downloadAndSavePage(String url, String fullSavePath) throws IOException {
        Document page = downloadPage(generateLink(url));
        updateProgressBar();
        if (page == null) {
            print("Error by downloading ", url);
            enableUI();
            return false;
        }
        savePage(page, fullSavePath);
        updateProgressBar();
        return true;
    }

    private Boolean validateInputData() {
        if (!isValidPath(savePath) || !isPathExisting(savePath)) {
            print("Save path does not exists");
            enableUI();
            return false;
        }
        updateProgressBar();

        if (!savePath.substring(savePath.length() - 1).equals("\\")) {
            print("Correcting the save path");
            savePath = savePath + "\\";
        }

        return true;
    }

    public Boolean execute(String hostname, String savePath) throws IOException {
        this.savePath = savePath;
        this.hostname = hostname;

        init();

        if (!validateInputData())
            return false;

        generateHostnameFileName();
        updateProgressBar();

        Document indexPage = downloadPage(generateLink());
        updateProgressBar();
        if (indexPage == null) {
            print("Error by downloading status page");
            enableUI();
            return false;
        }

        if (parseIndex(indexPage)) {
            updateProgressBar();

            savePage(indexPage, generateSavePath(STATUS_MENU_FILE));
            updateProgressBar();

            downloadAndSavePage(configLink, generateSavePath(CONFIG_MENU_FILE));
            downloadAndSavePage(suppliesLink, generateSavePath(SUPPLIES_MENU_FILE));
            downloadAndSavePage(eventLogLink, generateSavePath(EVENT_LOG_MENU_FILE));

            print("Pages saved successfully");
            enableUI();
            return true;

        } else
            print("Error by parsing index page");
        enableUI();
        return false;
    }

}
