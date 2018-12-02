package com.comoressoft.profile.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comoressoft.profile.model.UserLocation;
import com.comoressoft.profile.repository.UserLocationRepository;
import com.comoressoft.profile.utils.ConnectionUtils;

@Service
public class UserLocationCollector {

	@Autowired
	private UserLocationRepository locationRepository;

	public void collect(String[] urls) throws Exception {
		
		for (String url : urls) {
			ImmutablePair<String, Integer> data = ConnectionUtils.getAvailableData(url);
			Document rootDocument = Jsoup.parse(data.getLeft());

			Element element = rootDocument.getElementsByClass("lstvilles").first();
			for (Element el : element.getElementsByTag("li")) {
				String ville = el.text();
				String villeUrlDetail = el.getElementsByTag("a").attr("href");
				ImmutablePair<String, Integer> dataVille = ConnectionUtils
						.getAvailableData("http://www.geoguide.fr" + villeUrlDetail);

				Document docville = Jsoup.parse(dataVille.getLeft());

				// System.out.println(docville);
				Elements elementVille = docville.getElementsByTag("tbody").first().getElementsByTag("td");
				String lat = "", lng = "";
				for (Element elcord : elementVille) {
					String coordVilData = elcord.getElementsByAttributeValue("type", "text/javascript").first().data();
					if (coordVilData.contains("{")) {
						String coordville = coordVilData.substring(coordVilData.indexOf("{") + 1,
								coordVilData.indexOf("}") - 1);
						lat = coordville.split(",")[0].split(":")[1];
						lng = coordville.split(",")[1].split(":")[1];
						break;
					}
				}
				System.out.println("==> " + ville + " <== \n latitud :" + lat + " longitude :" + lng + "\n");
				this.saveLocation(new UserLocation(ville, Double.parseDouble(lat), Double.parseDouble(lng)));

			}

		}
	}

	private void saveLocation(UserLocation userLocation) {
		UserLocation userloc = this.locationRepository.findByCity(userLocation.getCity());
		if (userloc == null) {
			this.locationRepository.save(userLocation);
		}

	}

}