import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Main {
	
	public static ArrayList<Artist> artistList;
	public static int count=0;

	public static void main(String[] args) throws IOException {
		 
		File dir = new File(".");
		String loc = dir.getCanonicalPath() + File.separator + "record.txt";
		
		File file = new File(loc);
		file.delete();
		
		FileWriter fstream = new FileWriter(loc, true);
		BufferedWriter out = new BufferedWriter(fstream);
		out.newLine();
		out.close();
		
		artistList = new ArrayList<Artist>();
 
		processPage("http://g1.globo.com/musica/agenda.html", 1);
		
		//writeArtistList();
 
		/*file = new File(loc);
 
		if (file.delete()) {
 
		}*/
	}
 
	// givn a String, and a File
	// return if the String is contained in the File
	public static boolean checkExist(String s, File fin) throws IOException {
 
		FileInputStream fis = new FileInputStream(fin);
		// //Construct the BufferedReader object
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));
 
		String aLine = null;
		while ((aLine = in.readLine()) != null) {
			// //Process each line
			if (aLine.trim().contains(s)) {
				//System.out.println("contains " + s);
				in.close();
				fis.close();
				return true;
			}
		}
 
		// do not forget to close the buffer reader
		in.close();
		fis.close();
 
		return false;
	}
 
	public static void processPage(String URL, int round) throws IOException {
 
		boolean found = false;
		
		File dir = new File(".");
		String loc = dir.getCanonicalPath() + File.separator + "record.txt";
		
		int nextRound = round;
 
		// invalid link
		if (URL.contains(".pdf") || URL.contains("@") 
				|| URL.contains("adfad") || URL.contains(":80")
				|| URL.contains("fdafd") || URL.contains(".jpg")
				|| URL.contains(".pdf") || URL.contains(".jpg")
				 || URL.contains(".exe"))
			return;
 
		// process the url first
		if(URL.contains("musica/show/www."))return;
		if(URL.contains("www.blueticket.com.br"))return;
		
		if(URL.contains("/musica/show/")) found = true;
		//found=true;
		if ( (URL.contains("g1.globo.com/") && URL.contains("musica/agenda") && !URL.endsWith("/")) || found) {
 
		} else if( (URL.contains("http://g1.globo.com/")&& URL.contains("musica/agenda") && URL.endsWith("/")) || found){
			URL = URL.substring(0, URL.length()-1);
		}else{
			// url of other site -> do nothing
			return;
		}
 
		File file = new File(loc);
 
		// check existance
		boolean e = checkExist(URL, file);
		if (!e) {
			if(round == 1) openArtistJson();
			//System.out.println("------ :  " + URL);
			// insert to file
			FileWriter fstream = new FileWriter(loc, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(URL);
			out.newLine();
			out.close();
 
			Document doc = null;
			try {
				doc = Jsoup.connect(URL).timeout(120*1000).get();
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
 
			if (doc.text().contains("Agenda de Shows")) {
				//System.out.println(URL);
			}
			if(found){

				nextRound++;
				
				System.out.println(URL);
				ArrayList<String> listInfo = new ArrayList<String>();
				
				String[] splitData = doc.text().split(" - ");
				listInfo.add(splitData[0]);
				listInfo.add(splitData[1]);
				listInfo.add(splitData[2]);
				listInfo.add(splitData[3]);
				listInfo.add(splitData[4]);
				
				String info = "", ingressos="", classificacao="";
				
				String[] splitInfo = doc.text().split("Informações");
				if(splitInfo[1].contains("Ingressos")){
					String[] splitIngressos = splitInfo[1].split("Ingressos");
					info = splitIngressos[0];
					if(splitIngressos[1].contains("Classificação")){
						String[] splitClassificacao = splitIngressos[1].split("Classificação");
						ingressos = splitClassificacao[0];
						if(splitClassificacao[1].contains("Info")){
							classificacao = splitClassificacao[1].substring(0, splitClassificacao[1].indexOf("Info"));
						}
					}else{
						ingressos = splitIngressos[1].split("Info")[0];
					}
				}else if(splitInfo[1].contains("Classificação")){
					String[] splitClassificacao = splitInfo[1].split("Classificação");
					info = splitClassificacao[0];
					if(splitClassificacao[1].contains("Info")){
						classificacao = splitClassificacao[1].substring(0, splitClassificacao[1].indexOf("Info"));
					}
				}else{
					info = splitInfo[1].split("Info")[0];
				}
				
				if(info.length()>1)info = info.trim();
				listInfo.add(info);
				if(ingressos.length()>1)ingressos = ingressos.substring(1).trim();
				listInfo.add(ingressos);
				if(classificacao.length()>1)classificacao = classificacao.trim();
				listInfo.add(classificacao);
				
				//System.out.println("");
				Artist artist = new Artist();
				System.out.println("contator: "+count);
				artist = getArtist(listInfo);
				//artistList.add(artist);
				writeArtist(artist, count);
				for(int i=0; i<listInfo.size(); i++){	
					System.out.println(listInfo.get(i));
				}
				System.out.println(artist.toString());
				System.out.println("");
				
				//System.out.println(doc.text());
				/*int size = doc.text().length();
				int step = 120;
				for(int i=0; i<size; i+=step){
					int nextIndex=i+step;
					if(nextIndex>size)nextIndex = size-1;
					String textLine = doc.text().substring(i, nextIndex);
					System.out.println(textLine);
					
				}*/

				//System.out.println("");
				//while(true);
				/*String[] split = doc.text().split("Agenda de Shows");
				for(int i=0; i<split.length; i++){
					if(i==0 || i==(split.length-1))System.out.println(split[i]);
				}*/
				
			}
 
			Elements questions = doc.select("a[href]");
			for (Element link : questions) {
				//System.out.println(link.attr("abs:href"));
				/*if(link.attr("abs:href")=="http://www.vagalume.com.br/jorge-e-mateus/"){
					
				}*/
				processPage(link.attr("abs:href"), nextRound);
				
			}
			
			if(round==1)closeArtistJson();
		} else {
			// do nothing
			return;
		}
 
	}
	
	public static Artist getArtist(ArrayList<String> list){
		ArrayList<Artist> artistList = new ArrayList<Artist>();
		Artist artist = new Artist();
		
		artist.setNome(list.get(0));
		artist.setNomeShow(list.get(1));
		artist.setCidade(list.get(2));
		artist.setEstado(list.get(3));
		artist.setDataShow(list.get(4));
		artist.setLocal(list.get(5));
		artist.setIngressos(list.get(6));
		artist.setClassificacao(list.get(7));
		String date = getDate();
		artist.setDataExtracao(date.substring(0, 10));
		artist.setHoraExtracao(date.substring(11));
		
		/*try {
			String rank = processPageVagalume("http://www.vagalume.com.br/"+ artist.getNome().replace(" ", "-").replace("&", "e").toLowerCase() +"/popularidade/");
			artist.setRank(rank);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return artist;
		
	}
	
	public static String getDate(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime());
		
		return date;
	}
	
	public static void openArtistJson(){
		File dir = new File(".");
		String loc;
		
		FileWriter fstream;
		try {
			loc = dir.getCanonicalPath() + File.separator + "artistList.txt";
			File file = new File(loc);
			file.delete();
			
			fstream = new FileWriter(loc, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("[");
			//out.newLine();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void closeArtistJson(){
			
		File dir = new File(".");
		String loc;
		
		FileWriter fstream;
		try {
			loc = dir.getCanonicalPath() + File.separator + "artistList.txt";
			
			fstream = new FileWriter(loc, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("{} ]");
			//out.newLine();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void writeArtist(Artist artist, int count){
		File dir = new File(".");
		String loc;
		
		System.out.println("start writting:");
		System.out.println("");
		
		try {
			loc = dir.getCanonicalPath() + File.separator + "artistList.txt";

			FileWriter fstream = new FileWriter(loc, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(artist.toString() + ",");
			//out.newLine();
			out.close();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	public static void writeArtistList(){
		File dir = new File(".");
		String loc;
		
		System.out.println("start writting:");
		System.out.println("");
		
		try {
			loc = dir.getCanonicalPath() + File.separator + "artistList.txt";
			File file = new File(loc);
			file.delete();
			
			for(int i=0; i<artistList.size(); i++){
				String toWrite;
				if(i==0)toWrite = "[" + artistList.get(i).toString() + ",";
				else if(i<artistList.size()-1)toWrite = artistList.get(i).toString() + ",";
				else toWrite = artistList.get(i).toString();
				
				FileWriter fstream = new FileWriter(loc, true);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(toWrite);
				out.newLine();
				out.close();
				
				System.out.println(toWrite);
				
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	public static String processPageVagalume(String URL) throws IOException {
		 String returnStr = "";
		
		boolean foundAgenda = false;
		boolean foundMusicas = false;
		boolean foundPopularidade = false;
		
		/*File dir = new File(".");
		String loc = dir.getCanonicalPath() + File.separator + "record.txt";*/
 
		// invalid link
		if (URL.contains(".pdf") || URL.contains("@") 
				|| URL.contains("adfad") || URL.contains(":80")
				|| URL.contains("fdafd") || URL.contains(".jpg")
				|| URL.contains(".pdf") || URL.contains(".jpg")
				 || URL.contains(".exe"))
			return "";
 
		// process the url first
		if(URL.contains("agenda/")) foundAgenda = true;
		if(URL.contains("popularidade/"))foundPopularidade = true;
		if (URL.contains("www.vagalume.com.br") && !URL.endsWith("/")) {
 
		} else if(URL.contains("http://www.vagalume.com.br") && URL.endsWith("/")){
			URL = URL.substring(0, URL.length()-1);
		}else{
			// url of other site -> do nothing
			return "";
		}

 
			Document doc = null;
			try {
				doc = Jsoup.connect(URL).timeout(120*1000).get();
			} catch (IOException e1) {
				e1.printStackTrace();
				return "";
			}
 
			if (doc.text().contains("Agenda de Shows")) {
		}
		if(foundPopularidade){
			System.out.println(URL);
			if(doc.text().contains("popularidade em seu site.")){
				String[] splitSite = doc.text().split("popularidade em seu site.");
				String rank = splitSite[1].split("ranking")[0];
				returnStr = rank;
				System.out.println(rank);
			}
			
			
		}
 
		return returnStr.trim();
 
	}
	
	
	public static String getArtistName(String url){
		String aux = url.substring(url.indexOf("www"));
		String name = aux.split("/")[1];
		System.out.println("nome"+name);
		
		return name;
		
	}
	
}
