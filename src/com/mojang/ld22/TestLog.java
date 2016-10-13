package com.mojang.ld22;

import java.util.logging.*;
import java.io.*;

public class TestLog {

public static Logger logger;

	static {
	    try {
	      boolean append = true;
	      FileHandler fh = new FileHandler("TestLog.log", append);
	      //fh.setFormatter(new XMLFormatter());
	      fh.setFormatter(new SimpleFormatter());
	      logger = Logger.getLogger("TestLog");
	      logger.addHandler(fh);
	    }
	    catch (IOException e) {
	      e.printStackTrace();
	    }
	}
}