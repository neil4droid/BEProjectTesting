package test;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class OpenFile {
	static void open(String fileName) {
		ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\Sublime Text 3\\sublime_text.exe", "H:\\Chinmay\\workspace\\SyntacticReconstruction\\"+fileName);
		try {
			pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}