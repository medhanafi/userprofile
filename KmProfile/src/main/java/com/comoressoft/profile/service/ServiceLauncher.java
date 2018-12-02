package com.comoressoft.profile.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.comoressoft.profile.model.KmUser;
import com.comoressoft.profile.model.Names;
import com.comoressoft.profile.model.UserLocation;
import com.comoressoft.profile.repository.KmUserRepository;

@Service
public class ServiceLauncher {

	@Autowired
	private UserLocationCollector ct;
	@Autowired
	private NamesCollector nc;

	@Autowired
	private ServiceCore phoneGerator;

	@Autowired
	private KmUserRepository kmUserRepo;

	@Scheduled(cron = "0/5 * * * * ?")
	public void profileGenerator() throws IOException {
		for (int i = 0; i < 100; i++) {
			this.phoneGerator.setCities();
			this.phoneGerator.setNames();
			List<String> names = this.phoneGerator.getAllNames();
			List<Names> firstNames = this.phoneGerator.getAllFirstNames();
			List<String> emails = this.phoneGerator.getEmails();
			List<UserLocation> cities = this.phoneGerator.getCities();

			int indexName = ThreadLocalRandom.current().nextInt(0, names.size() - 1);
			int indexFirstName = ThreadLocalRandom.current().nextInt(0, firstNames.size() - 1);
			int indexCities = ThreadLocalRandom.current().nextInt(0, cities.size() - 1);
			int indexEmails = ThreadLocalRandom.current().nextInt(0, emails.size() - 1);

			String gender = firstNames.get(indexFirstName).getGender();
			String name = names.get(indexName);
			String firstname = firstNames.get(indexFirstName).getName();
			String email = name + "." + firstname + "@" + emails.get(indexEmails);
			String birth = this.phoneGerator.getBirth();
			String cell = this.phoneGerator.getNextPhone();
			String citie = cities.get(indexCities).getCity();

			System.out.println("N° " + i + " <=======================>");
			System.out.println("Genre: " + gender);
			System.out.println("Nom: " + name);
			System.out.println("Prenom: " + firstname);
			System.out.println("date de naissance: " + birth);
			System.out.println("Email: " + email);
			System.out.println("Téléphone: " + cell);
			System.out.println("Adress :" + citie);

			KmUser km = new KmUser();
			km.setBirth(birth);
			km.setCell(cell);
			km.setCity(citie);
			km.setEmail(email.toLowerCase());
			km.setFirstname(firstname);
			km.setGender(gender);
			km.setLocation(cities.get(indexCities));

			this.kmUserRepo.save(km);
		}

	}

	// @Scheduled(cron = "0/5 * * * * ?")
	public void launch() throws IOException {
		String[] urls = { "http://www.geoguide.fr/region_villes_liste.aspx?CC1=CN&ADM=01&p=1",
				"http://www.geoguide.fr/region_villes_liste.aspx?CC1=CN&ADM=01&p=2",
				"http://www.geoguide.fr/region_villes_liste.aspx?CC1=CN&ADM=02&p=1",
				"http://www.geoguide.fr/region_villes_liste.aspx?CC1=CN&ADM=02&p=2",
				"http://www.geoguide.fr/region_villes_liste.aspx?CC1=CN&ADM=02&p=3",
				"http://www.geoguide.fr/region_villes_liste.aspx?CC1=CN&ADM=03&p=1" };

		String urlnc = "https://swadrii.com/orelc/ShikomoriNames/getNamesByLetter/";

		try {
			nc.collect(urlnc);
			ct.collect(urls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("END");
	}

}