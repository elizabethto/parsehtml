@Grab(group='org.jsoup', module='jsoup', version='1.7.2')

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

Document doc = Jsoup.connect("http://www.allmenus.com/ny/new-york/322071-xo-cafe-and-grill/menu/").get()
Elements links = doc.select("a[href]");
println links