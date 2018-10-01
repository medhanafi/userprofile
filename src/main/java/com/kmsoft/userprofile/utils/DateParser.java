package com.kmsoft.userprofile.utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.datetime.DateFormatter;

/**
 * 
 * (Description)
 *
 * @since 29 août 2016
 * @author MHA14633
 */
public class DateParser implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4442478725575405320L;
	public static final Logger LOGGER = LoggerFactory.getLogger(DateParser.class);
	private DateFormatLoader dateLoader;
	private List<Triple<String, Pattern, Locale>> ressources;

	public DateParser() {
		this.dateLoader = new DateFormatLoader();
		this.ressources = this.dateLoader.getDateRessource();

	}

	public Date parse(String inputDate) {
		inputDate = this.cleanFrInputDate(inputDate);
		Date outputDate = null;
		for (int i = 0; i < this.ressources.size(); i++) {
			Triple<String, Pattern, Locale> ressource = this.ressources.get(i);
			Matcher matcher = ressource.getMiddle().matcher(inputDate);
			if (matcher.find()) {
				if (!containsFrMonth(inputDate) && ressource.getRight().toString().equalsIgnoreCase("fr_FR")) {
					outputDate = this.parseDate(outputDate, matcher.group(), new DateFormatter(ressource.getLeft()),
							Locale.ENGLISH);
				} else {
					outputDate = this.parseDate(outputDate, matcher.group(), new DateFormatter(ressource.getLeft()),
							ressource.getRight());
				}
				if (outputDate != null)
					break;
			}
		}

		return outputDate;
	}

	private boolean containsFrMonth(String month) {
		List<String> months = new ArrayList<>();
		months.add("janvier");
		months.add("février");
		months.add("mars");
		months.add("avril");
		months.add("mai");
		months.add("juin");
		months.add("juillet");
		months.add("août");
		months.add("septembre");
		months.add("octobre");
		months.add("novembre");
		months.add("décembre");
		for (String m : months) {
			if (month.contains(m)) {
				return true;
			}
		}
		return false;
	}

	public String parseToString(String inputDate, Locale local) {
		inputDate = this.cleanFrInputDate(inputDate);
		Date outputDate = null;
		for (int i = 0; i < this.ressources.size(); i++) {
			Triple<String, Pattern, Locale> ressource = this.ressources.get(i);
			Matcher matcher = ressource.getMiddle().matcher(inputDate);
			if (matcher.find()) {
				outputDate = this.parseDate(outputDate, matcher.group(), new DateFormatter(ressource.getLeft()),
						ressource.getRight());
				if (outputDate != null)
					break;
			}
		}
		String format = "yyyy-MM-dd";
		if (local != null) {
			format = local.toString().equals("fr") ? "dd/MM/yyyy" : "yyyy-MM-dd";
		}
		return new SimpleDateFormat(format).format(outputDate);

	}

	public String localFormat(Date date) {
		if (date != null) {
			DateFormat formatter = DateFormat.getDateTimeInstance();
			return formatter.format(date);
		}
		return "";
	}

	private Date parseDate(Date outputDate, final String inputDate, final DateFormatter simpleDateFormat,
			final Locale locale) {
		if (outputDate == null) {
			try {
				outputDate = simpleDateFormat.parse(inputDate, locale);
			} catch (ParseException e) {
				if (locale.getLanguage().equals("pt")) {
					try {
						outputDate = simpleDateFormat.parse(inputDate, new Locale("es", "ES"));
					} catch (ParseException e1) {
						LOGGER.error("Unparseable Date from {} to {} using Local "+e1 + locale, inputDate, outputDate);

					}
				}else {
					LOGGER.error("Unparseable Date from {} to {} using Local "+e + locale, inputDate, outputDate);
				}
			}
		}

		return outputDate;
	}

	private String cleanFrInputDate(String inputDate) {
		if (inputDate.contains("/")) {
			inputDate = inputDate.replaceAll("/", " ");
		}
		return inputDate;
	}

	/**
	 * Calculates the number of (TimeUnit) between two dates
	 * 
	 * @param timeUnit
	 *            The time unit (NANOSECONDS aren't supported)
	 * @param startDate
	 *            The beginning date
	 * @param endDate
	 *            The ending date
	 * @return number of (TimeUnit) between startDate and endDate
	 */
	public long between(final TimeUnit timeUnit, final Date startDate, final Date endDate) {
		final Calendar startCalendar = Calendar.getInstance();
		final Calendar endCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		endCalendar.setTime(endDate);

		return between(timeUnit, startCalendar, endCalendar);
	}

	/**
	 * Calculates the number of (TimeUnit) between two dates
	 * 
	 * @param timeUnit
	 *            The time unit (NANOSECONDS aren't supported)
	 * @param startDate
	 *            The beginning date
	 * @param endDate
	 *            The ending date
	 * @return number of (TimeUnit) between startDate and endDate
	 */
	public long between(final TimeUnit timeUnit, final Calendar startDate, final Calendar endDate) {
		final long start = startDate.getTimeInMillis();
		final long end = endDate.getTimeInMillis();
		long diff = end - start;

		boolean revert = false;
		if (diff < 0) {
			revert = true;
			diff = Math.abs(diff);
		}

		if (TimeUnit.DAYS.equals(timeUnit)) {
			diff = TimeUnit.MILLISECONDS.toDays(diff);
		} else if (TimeUnit.HOURS.equals(timeUnit)) {
			diff = TimeUnit.MILLISECONDS.toHours(diff);
		} else if (TimeUnit.MINUTES.equals(timeUnit)) {
			diff = TimeUnit.MILLISECONDS.toMinutes(diff);
		} else if (TimeUnit.SECONDS.equals(timeUnit)) {
			diff = TimeUnit.MILLISECONDS.toSeconds(diff);
		}

		if (revert) {
			diff = -diff;
		}

		return diff;
	}

}
