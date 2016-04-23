package com.GenomeData.Main;

import com.GenomeData.Controller.Controller;
import com.GenomeData.View.UserInterface;

public class MainActivityUI {

	public static void main(String[] args) {
		Controller mController = new Controller();
		UserInterface ui = new UserInterface(mController);
	}

}
