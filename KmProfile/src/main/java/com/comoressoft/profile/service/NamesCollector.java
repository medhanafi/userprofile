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

import com.comoressoft.profile.model.Names;
import com.comoressoft.profile.repository.NamesRepository;
import com.comoressoft.profile.utils.ConnectionUtils;

@Service
public class NamesCollector {

	@Autowired
	private NamesRepository namesRepository;

	public void collect(String url) throws Exception {
		List<String> urlParam = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
				"p", "r", "s", "t", "u", "v", "w", "y", "z");
		for (String param : urlParam) {
			ImmutablePair<String, Integer> data = ConnectionUtils.getAvailableData(url + param);
			Document rootDocument = Jsoup.parse(data.getLeft());
			if (param.equals("m")) {
				rootDocument = Jsoup.parse(ConnectionUtils.getData());
			}
			Elements elements = rootDocument.getElementsByAttributeValueMatching("class",
					"v_desktop col-md-12 col-lg-12");
			for (Element element : elements) {
				Elements fullNames = element.getElementsByClass("v_desktop");
				if (fullNames != null) {
					for (Element el : fullNames) {
						if (el != null) {
							Elements elNames = el.getElementsByTag("span");
							String name = this.getValue(elNames, 0);
							String gender = this.getValue(elNames, 1);
							String pronounce = this.getValue(elNames, 2);
							System.out.println(name + " => " + gender + " => " + pronounce);
							this.namesRepository.save(new Names(gender, name, pronounce));
						}

					}
				}
			}
			if (param.equals("m")) {
				break;
			}
		}

	}

	private String getValue(Elements elNames, int i) {
		if (elNames != null) {
			try {
				if (elNames.get(i) != null) {
					return elNames.get(i).text();
				}
			} catch (IndexOutOfBoundsException e) {
				return null;
			}
		}
		return null;
	}

}