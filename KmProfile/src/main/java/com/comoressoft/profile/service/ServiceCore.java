package com.comoressoft.profile.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comoressoft.profile.model.Names;
import com.comoressoft.profile.model.UserLocation;
import com.comoressoft.profile.repository.NamesRepository;
import com.comoressoft.profile.repository.UserLocationRepository;
import com.fasterxml.jackson.datatype.jsr310.DecimalUtils;

@Service
public class ServiceCore {
	@Autowired
	private NamesRepository namesRepository;

	@Autowired
	private UserLocationRepository locationRepository;

	private List<String> allNames = new ArrayList<>();
	private List<Names> allFirstNames = new ArrayList<>();

	private List<UserLocation> cities = new ArrayList<>();

	private List<String> femPictures;

	private List<String> homPictures;

	private List<String> allPictures;

	public String getNextPhone() {

		int num1 = ThreadLocalRandom.current().nextInt(330, 340);
		int num2 = ThreadLocalRandom.current().nextInt(1, 99);
		int num3 = ThreadLocalRandom.current().nextInt(1, 99);
		return ("(+269) ") + num1 + "" + this.asString(num2) + "" + this.asString(num3);
	}

	private String asString(int num) {
		String oct = String.valueOf(num);
		if (oct.length() < 2) {
			return "0" + oct;
		} else {
			return oct;
		}
	}

	public List<String> getEmails() throws IOException {
		String fileName = this.getClass().getClassLoader().getResource("domain.txt").getPath();
		List<String> emails = new ArrayList<>();
		Path path = Paths.get(fileName);
		try (BufferedReader reader = Files.newBufferedReader(path)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				emails.add(line);
			}
		}
		return emails;

	}

	public void setNames() {
		List<Names> names = this.namesRepository.findAll();
		for (Names name : names) {
			if (name.getGender() != null && name.getGender().contains("masc")) {
				this.allNames.add(name.getName());
				if (name.getPronouce() != null) {
					this.allNames.add(name.getPronouce());
				}
			}

			if (name.getPronouce() != null) {
				this.allFirstNames.add(name);
			}
			this.allFirstNames.add(name);
		}

	}

	public List<Names> getAllFirstNames() {
		return allFirstNames;
	}

	public List<String> getAllNames() {
		return allNames;
	}

	public void setCities() {
		this.cities.addAll(this.locationRepository.findAll());
	}

	public List<UserLocation> getCities() {
		return this.cities;
	}

	public String getBirth() {

		GregorianCalendar gc = new GregorianCalendar();
		int year = randBetween(1970, 2012);
		gc.set(Calendar.YEAR, year);
		int dayOfYear = randBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));
		gc.set(Calendar.DAY_OF_YEAR, dayOfYear);
		return asString(gc.get(Calendar.DAY_OF_MONTH)) + "/" + asString((gc.get(Calendar.MONTH) + 1)) + "/"
				+ gc.get(Calendar.YEAR);

	}

	public static int randBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

	public void initPictures() {
		try {
			this.allPictures = this.getPictureSources();
					
					
			this.homPictures = new ArrayList<>();
			this.femPictures = new ArrayList<>();

			for (String path : allPictures) {
				if (path.contains("fem")) {
					this.femPictures.add(path);
				} else {
					this.homPictures.add(path);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<String> getPictureSources() throws IOException {
		Document doc= Jsoup.connect("https://km.comoressoft.com/km/").get();
		List<String> urls=new ArrayList<>();
			Elements els=doc.getElementsByTag("li");
			for(Element el:els) {
				urls.add(el.text());
			}
		/*return Files.walk(Paths.get("/home/mha14633/Bureau/picture")).filter(Files::isRegularFile)
				.collect(Collectors.toList());*/
			return urls;
	}

	/**
	 * @return
	 */
	public List<String> getPicture(String gender) {
		
		if (gender != null && gender.contains("fem")) {
			return this.femPictures;
		} else {
			return this.allPictures;
		}
	}
}
