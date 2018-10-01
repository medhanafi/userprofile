package com.kmsoft.userprofile.utils;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * (Description)
 *
 * @since 29 août 2016
 * @author MHA14633
 */
public class DateFormatLoader implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6772922457331124589L;
    public static final Logger LOGGER = LoggerFactory.getLogger(DateFormatLoader.class);

    private List<String> dateFormats = new ArrayList<>();

    /**
     * Constructor
     *
     */
    public DateFormatLoader() {
        init();
    }

    private void init() {

        this.dateFormats.add("yyyy-MM-dd'T'HH:mm:ss.SSS=((\\d{4}-\\d{2}-\\d{2}T\\d{2}):(\\d{2}:\\d{2}.\\d{2}-\\d{2}:\\d{2}))=fr");
        this.dateFormats.add("yyyy-MM-dd HH:mm:ss.S=(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}.\\d{3}-\\d{4})=fr");
        this.dateFormats.add("yyyy-MM-dd'T'HH:mm:ss.SSSXXX=(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}-\\d{2}:\\d{2})=en");
        this.dateFormats.add("MMM dd, yyyy hh:mm:ss a=((([\\D]{3,})\\s\\d{2},\\s\\d{4})\\s(\\d{2}:\\d{2}:\\d{2}\\s(AM|PM)))=en");
        this.dateFormats.add("yyyy-MM-dd'T'HH:mm:ss'Z'=(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2,}Z)=en");
        this.dateFormats.add("dd-MM-yyyy=((0[1-9]|[12]\\d|3[01])-(0[13578]|1[02])-((19|[2-9]\\d)\\d{2}))=fr");
        this.dateFormats.add("dd MM yyyy=(\\d{2}\\s\\d{2}\\s\\d{4})=fr");
        this.dateFormats.add("dd MMMM yyyy=((\\d{1,2})\\s([a-zA-ZûéèÛ]{3,})\\s(\\d{4}))=fr");
        this.dateFormats.add("MMMM dd, yyyy=(([a-zA-Z]{3,})\\s\\d{1,2},\\s\\d{4})=en");
        this.dateFormats.add("yyyy-MM-dd'T'HH:mm:ss=(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}-\\d{2}:\\d{2})=en");
        this.dateFormats.add("yyyy-MM-dd=(\\d{4}-\\d{2}-\\d{2})=en");
        this.dateFormats.add("d 'de' MMMM 'de' yyyy=(\\d{1,}\\s[\\D]{2}\\s[\\D]{3,}\\s\\d{4})=pt");
//d de mmmm de yyyy
    }

    public List<Triple<String, Pattern, Locale>> getDateRessource() {
        List<Triple<String, Pattern, Locale>> listRessource = new ArrayList<>();
        boolean errorFormat = false;
        for (String lineformat : this.dateFormats) {
            if (StringUtils.contains(lineformat, "=")) {
                final String[] str = lineformat.split("=");
                listRessource.add(Triple.of(str[0], Pattern.compile(str[1]), getLocal(str[2])));
            } else {
                errorFormat = true;
            }
        }
        if (errorFormat) {
            LOGGER.error("Error: Date format, Regex and Local format should separated by '=' ");
        }

        return listRessource;
    }

    public Locale getLocal(final String contryCode) {
        if (contryCode.equals("fr")) {
            return Locale.FRANCE;
        }else  if (contryCode.equals("en")) {
            return Locale.ENGLISH;
        }else  if (contryCode.equals("pt")) {
            return new Locale("pt", "BR");
        }else
        if (contryCode.equals("es")) {
            return new Locale("es", "ES");
        }
        return Locale.US;
    }
}
