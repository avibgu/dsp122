package utilities;

import java.io.File;

public class HTMLBuilder {
	
	public static void createHTMLFile(Object response, File outputFile){
		// TODO Auto-generated constructor stub
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(	"<html>" +
					"<title>Faces! - Avi & Batel</title>" +
					"<body>" );
		
//		for (Object obj : respone)
//			sb.append(	"<a href=\"" + href + "\">" +
//						"<img src=\"" + img + "\"/>" +
//						"</a>" );

		sb.append(	"</body>" +
					"</html>" );
	}
}
