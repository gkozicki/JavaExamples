package gamemaker;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;
import testMode.TestPaint;

public class CPaint {

// =================================================
//    CLASS VARIABLES
// =================================================

	private XmlManager xmlManager;
	private TestPaint paintTest;
	private GraphicPool graphicPool;
	private Data data;
	private StartingClassGameMaker startingClass;
	private int oldWidth, oldHeight;
	private int leftMenu_NumberOfElements;
	private Image image;
	private Graphics second;
	private int instructionCounter = -1;
	private int animationDelayCounter = 0;
	private int animationInt_StandLeft,
				animationInt_StandRight,
				animationInt_StandUp,
				animationInt_StandDown,
				animationInt_MoveLeft,
				animationInt_MoveRight,
				animationInt_MoveUp,
				animationInt_MoveDown,
				animationInt_DeathLeft,
				animationInt_DeathRight,
				animationInt_DeathUp,
				animationInt_DeathDown,
				animationInt_MainLeft,
				animationInt_MainRight,
				animationInt_MainUp,
				animationInt_MainDown,
				animationInt_Sub1Left,
				animationInt_Sub1Right,
				animationInt_Sub1Up,
				animationInt_Sub1Down,
				animationInt_Sub2Left,
				animationInt_Sub2Right,
				animationInt_Sub2Up,
				animationInt_Sub2Down,
				animationInt_ProjMoveLeft,
				animationInt_ProjMoveRight,
				animationInt_ProjMoveUp,
				animationInt_ProjMoveDown,
				animationInt_ProjHitLeft,
				animationInt_ProjHitRight,
				animationInt_ProjHitUp,
				animationInt_ProjHitDown,
				animationInt_ObjectMain,
				animationInt_ObjectDestroy;

// =================================================
//    PUBLIC METHODS
// =================================================

	// These methods set the references for the appropriate variables
	// NOTE: Some of these may not currently be used or needed
	public void setCPaintTest(TestPaint CPT) { paintTest = CPT; }
	public void setGraphicPool(GraphicPool GP) { graphicPool = GP; }
	public void setData(Data D) { data = D; }
	public void setStartingClass(StartingClassGameMaker SCGM) { startingClass = SCGM; }

	// This method simply captures the applications starting size (the variables are used
	// within the Update method)
	public void initialize() {
		oldWidth = startingClass.getWidth();
		oldHeight = startingClass.getHeight();
		resetAnimationCounters();
	}

	public void resetAnimationCounters()
	{
		animationDelayCounter = Constants.previewAnimation_Delay;
		animationInt_StandLeft 		= 0;
		animationInt_StandRight 	= 0;
		animationInt_StandUp 		= 0;
		animationInt_StandDown 		= 0;
		animationInt_MoveLeft 		= 0;
		animationInt_MoveRight 		= 0;
		animationInt_MoveUp 		= 0;
		animationInt_MoveDown 		= 0;
		animationInt_DeathLeft 		= 0;
		animationInt_DeathRight 	= 0;
		animationInt_DeathUp 		= 0;
		animationInt_DeathDown 		= 0;
		animationInt_MainLeft 		= 0;
		animationInt_MainRight 		= 0;
		animationInt_MainUp 		= 0;
		animationInt_MainDown 		= 0;
		animationInt_Sub1Left 		= 0;
		animationInt_Sub1Right 		= 0;
		animationInt_Sub1Up 		= 0;
		animationInt_Sub1Down 		= 0;
		animationInt_Sub2Left 		= 0;
		animationInt_Sub2Right 		= 0;
		animationInt_Sub2Up 		= 0;
		animationInt_Sub2Down 		= 0;
		animationInt_ProjMoveLeft 	= 0;
		animationInt_ProjMoveRight 	= 0;
		animationInt_ProjMoveUp 	= 0;
		animationInt_ProjMoveDown 	= 0;
		animationInt_ProjHitLeft 	= 0;
		animationInt_ProjHitRight 	= 0;
		animationInt_ProjHitUp 		= 0;
		animationInt_ProjHitDown 	= 0;
		animationInt_ObjectMain 	= 0;
		animationInt_ObjectDestroy 	= 0;
	}

	// This method overrides the update method
	// NOTE: This is redirected from StartingClassGameMaker.java
	public void update(Graphics g) {
		if (image == null 
				|| (startingClass.getWidth() != oldWidth)
				|| (startingClass.getHeight() != oldHeight)) {
			image = startingClass.createImage(startingClass.getWidth(), startingClass.getHeight());
			second = image.getGraphics();
		} // end if

		second.setColor(startingClass.getBackground());
		second.fillRect(
				0, 							// X coordinate
				0, 							// Y coordinate
				startingClass.getWidth(), 	// width
				startingClass.getHeight());	// height
		second.setColor(startingClass.getForeground());
		paint(second);

		g.drawImage(image, 0, 0, startingClass);
	}

	// This method overrides the paint method. 
	// It calls the appropriate paint methods based on the current application states
	// NOTE: This is redirected from StartingClassGameMaker.java
	public void paint(Graphics g) {
		switch (data.mainState) {
		case Constants.mainState_SplashScrolling:
			paintSplashScrolling(g);
			break;
		case Constants.mainState_SplashMenu:
			paintSplashMenu(g);
			break;
		case Constants.mainState_SplashMenuLoad:
			paintSplashLoad(g);
			break;
		case Constants.mainState_BuilderMenus:
		case Constants.mainState_BuilderWindow:
			paintBuilderBackground(g);
			paintTopMenu(g);
			paintLeftMenu(g);
			paintWindow(g);
			paintBuilderCursors(g);
			paintBuilderArrows(g);
			paintProjectDescriptionTexts(g);
			break;
		case Constants.mainState_Credits:
			paintSplashCredits(g);
		case Constants.mainState_Test:
			paintTest.paint(g);
			break;
		default:
			// ignore invalid cases
			break;
		} // end switch
	}

	// This method calculates the appropriate resize ratio (for width) and returns it as a double
	public double resizeRatioWidth() {
		return ((double) startingClass.getWidth() / Constants.screen_HorizontalDimension);
	}

	// This method calculates the appropriate resize ratio (for height) and returns it as a double
	public double resizeRatioHeight() {
		return ((double) startingClass.getHeight() / Constants.screen_VerticalDimension);
	}

	// This method calculates the appropriate resize ratio (for font) and returns it as a double
	public double resizeRatioFont() {
		double temp = ((double) startingClass.getWidth() / Constants.screen_VerticalDimension);
		temp = Math.pow(temp, .975);
		return temp;
	}

// =================================================
//  PRIVATE METHODS
// =================================================

	// Paints the scrolling animation for the splash screen
	private void paintSplashScrolling(Graphics g) {
		if (data.splashMenuSliderCount > Constants.splashScreenImage_StartingY) {
			g.drawImage(
					graphicPool.splashScreen, 											// Image
					(int) (resizeRatioWidth() * Constants.splashScreenImage_StartingX), // X coordinate
					(int) (resizeRatioHeight() * data.splashMenuSliderCount), 			// Y coordinate
					(int) (resizeRatioWidth() * Constants.screen_HorizontalDimension), 	// width
					(int) (resizeRatioHeight() * (Constants.screen_VerticalDimension 
							- Constants.splashScreenImage_StartingY)), 					// height
					startingClass); 													// ImageObserver
			data.splashMenuSliderCount -= resizeRatioHeight()
					* Constants.splashScreenImage_ScrollSpeed;
		} // end if
		else {
			data.mainState = Constants.mainState_SplashMenu;
		} // end else
	}
	
	// Paints the scrolling animation for the splash screen
		private void paintSplashCredits(Graphics g) {
			
				g.drawImage(
						graphicPool.splashCredits, 											// Image
						(int) (resizeRatioWidth() * Constants.splashScreenImage_StartingX), // X coordinate
						(int) (resizeRatioHeight() * Constants.splashScreenImage_StartingY), 			// Y coordinate
						(int) (resizeRatioWidth() * Constants.screen_HorizontalDimension), 	// width
						(int) (resizeRatioHeight() * (Constants.screen_VerticalDimension 
								- Constants.splashScreenImage_StartingY)), 					// height
						startingClass);
		}

	// paints the graphics for the splash screen main menu
	private void paintSplashMenu(Graphics g) {
		// adjust the graphics to the correct position in case we skipped the splash scrolling animation
		while ((resizeRatioHeight() * data.splashMenuSliderCount) < (resizeRatioHeight() * 240)) {
			data.splashMenuSliderCount += resizeRatioHeight() * 1.75;
		} // end while
		// display menu
		g.drawImage(
				graphicPool.splashScreen, 												// Image
				(int) (resizeRatioWidth() * Constants.splashScreenImage_StartingX),		// X coordinate
				(int) (resizeRatioHeight() * Constants.splashScreenImage_StartingY),	// Y coordinate
				(int) (resizeRatioWidth() * Constants.screen_HorizontalDimension), 		// width
				(int) (resizeRatioHeight() * (Constants.screen_VerticalDimension 
						+ (Constants.splashScreenImage_StartingY * -1))), 				// height
				startingClass); 														// ImageObserver
		g.drawImage(
				graphicPool.newGameButtonIdle, 												// Image
				(int) (resizeRatioWidth() * Constants.splashScreenButton_NewStartingX), 	// X coordinate
				(int) (resizeRatioHeight() * Constants.splashScreenButton_NewStartingY),	// Y coordinate
				(int) (resizeRatioWidth() * Constants.splashScreenButtonWidth), 			// width
				(int) (resizeRatioHeight() * Constants.splashScreenButtonHeight), 			// height
				startingClass); 															// ImageObserver
		g.drawImage(
				graphicPool.loadGameButtonIdle, 											// Image
				(int) (resizeRatioWidth() * Constants.splashScreenButton_LoadStartingX), 	// X coordinate
				(int) (resizeRatioHeight() * Constants.splashScreenButton_LoadStartingY),	// Y coordinate
				(int) (resizeRatioWidth() * Constants.splashScreenButtonWidth), 			// width
				(int) (resizeRatioHeight() * Constants.splashScreenButtonHeight), 			// height
				startingClass); 															// ImageObserver
/*
		g.drawImage(
				graphicPool.optionsButtonIdle, 													// Image
				(int) (resizeRatioWidth() * Constants.splashScreenButton_OptionsStartingX), 	// X coordinate
				(int) (resizeRatioHeight() * Constants.splashScreenButton_OptionsStartingY), 	// Y coordinate
				(int) (resizeRatioWidth() * Constants.splashScreenButtonWidth), 				// width
				(int) (resizeRatioHeight() * Constants.splashScreenButtonHeight), 				// height
				startingClass); 																// ImageObserver
*/
		g.drawImage(
				graphicPool.creditsButtonIdle, 													// Image
				(int) (resizeRatioWidth() * Constants.splashScreenButton_CreditsStartingX), 	// X coordinate
				(int) (resizeRatioHeight() * Constants.splashScreenButton_CreditsStartingY), 	// Y coordinate
				(int) (resizeRatioWidth() * Constants.splashScreenButtonWidth), 				// width
				(int) (resizeRatioHeight() * Constants.splashScreenButtonHeight), 				// height
				startingClass); 																// ImageObserver
		g.drawImage(
				graphicPool.exitGameButtonIdle, 											// Image
				(int) (resizeRatioWidth() * Constants.splashScreenButton_ExitStartingX), 	// X coordinate
				(int) (resizeRatioHeight() * Constants.splashScreenButton_ExitStartingY),	// Y coordinate
				(int) (resizeRatioWidth() * Constants.splashScreenButtonWidth), 			// width
				(int) (resizeRatioHeight() * Constants.splashScreenButtonHeight), 			// height
				startingClass); 															// ImageObserver
		// Now we need to paint the graphic for the selected menu item
		paintSplashMenuSelected(g);
	}

	// paints the graphics for the selected splash screen main menu item
	private void paintSplashMenuSelected(Graphics g) {
		switch (data.splashCursor.getPosition()) {
		case Constants.cursorSplashIndex_NewGame:
			g.drawImage(
					graphicPool.newGameButtonPresd, 											// Image
					(int) (resizeRatioWidth() * Constants.splashScreenButton_NewStartingX), 	// X coordinate
					(int) (resizeRatioHeight() * Constants.splashScreenButton_NewStartingY),	// Y coordinate
					(int) (resizeRatioWidth() * Constants.splashScreenButtonWidth), 			// width
					(int) (resizeRatioHeight() * Constants.splashScreenButtonHeight), 			// height
					startingClass); 															// ImageObserver
			break;
		case Constants.cursorSplashIndex_LoadGame:
			g.drawImage(
					graphicPool.loadGameButtonPresd, 											// Image
					(int) (resizeRatioWidth() * Constants.splashScreenButton_LoadStartingX), 	// X coordinate
					(int) (resizeRatioHeight() * Constants.splashScreenButton_LoadStartingY),	// Y coordinate
					(int) (resizeRatioWidth() * Constants.splashScreenButtonWidth), 			// width
					(int) (resizeRatioHeight() * Constants.splashScreenButtonHeight), 			// height
					startingClass); 															// ImageObserver
			break;
/*
		case Constants.cursorSplashIndex_Options:
			g.drawImage(
					graphicPool.optionsButtonPresd, 												// Image
					(int) (resizeRatioWidth() * Constants.splashScreenButton_OptionsStartingX), 	// X coordinate
					(int) (resizeRatioHeight() * Constants.splashScreenButton_OptionsStartingY),	// Y coordinate
					(int) (resizeRatioWidth() * Constants.splashScreenButtonWidth), 				// width
					(int) (resizeRatioHeight() * Constants.splashScreenButtonHeight), 				// height
					startingClass); 																// ImageObserver
			break;
*/
		case Constants.cursorSplashIndex_Exit:
			g.drawImage(
					graphicPool.exitGameButtonPresd, 											// Image
					(int) (resizeRatioWidth() * Constants.splashScreenButton_ExitStartingX), 	// X coordinate
					(int) (resizeRatioHeight() * Constants.splashScreenButton_ExitStartingY),	// Y coordinate
					(int) (resizeRatioWidth() * Constants.splashScreenButtonWidth), 			// width
					(int) (resizeRatioHeight() * Constants.splashScreenButtonHeight), 			// height
					startingClass); 															// ImageObserver
			break;
		case Constants.cursorSplashIndex_Credits:
			g.drawImage(
					graphicPool.creditsButtonPresd, 												// Image
					(int) (resizeRatioWidth() * Constants.splashScreenButton_CreditsStartingX), 	// X coordinate
					(int) (resizeRatioHeight() * Constants.splashScreenButton_CreditsStartingY),	// Y coordinate
					(int) (resizeRatioWidth() * Constants.splashScreenButtonWidth), 				// width
					(int) (resizeRatioHeight() * Constants.splashScreenButtonHeight), 				// height
					startingClass); 																// ImageObserver
			break;
		default:
			// ignore invalid cases
			break;
		} // end switch
	}

	// paints the graphics for the splash screen load project menu
	private void paintSplashLoad(Graphics g) {
		// display menu
		g.drawImage(
				graphicPool.splashScreen, 												// Image
				(int) (resizeRatioWidth() * Constants.splashScreenImage_StartingX), 	// X coordinate
				(int) (resizeRatioHeight() * Constants.splashScreenImage_StartingY),	// Y coordinate
				(int) (resizeRatioWidth() * Constants.screen_HorizontalDimension), 		// width
				(int) (resizeRatioHeight() * (Constants.screen_VerticalDimension 
						+ (Constants.splashScreenImage_StartingY * -1))), 				// height
				startingClass); 														// ImageObserver
		// draw the prompt
		g.setFont(g
				.getFont()
				.deriveFont((float) (resizeRatioFont() * Constants.splashLoad_PromptTextSize)));
		g.drawString(
				Constants.splashLoad_Prompt,
				(int) (resizeRatioWidth() * Constants.splashLoad_PromptStartingX),
				(int) (resizeRatioHeight() * Constants.splashLoad_PromptStartingY));
		// draw the arrows
		int numberOfElements = graphicPool.splashLoadVec.size();
		Image tempImage;
		if (numberOfElements == 0 || data.splashLoadShiftOffset == 0) {
			tempImage = graphicPool.listUpArrowGray;
		} // end if
		else {
			tempImage = graphicPool.listUpArrow;
		} // end else
		g.drawImage(
				tempImage, 															// Image
				(int) (resizeRatioWidth() * Constants.arrowSplashStartingX), 		// X coordinate
				(int) (resizeRatioHeight() * Constants.arrowSplash_UpStartingY),	// Y coordinate
				(int) (resizeRatioWidth() * Constants.arrowSplashWidth), 			// width
				(int) (resizeRatioHeight() * Constants.arrowSplashHeight), 			// height
				startingClass); 													// ImageObserver
		if (numberOfElements == 0
				|| data.splashLoadShiftOffset >= data.splashLoadCursor.getVerticalMax()
						- Constants.splashLoad_ItemMaxShownOnScreen + 1) {
			tempImage = graphicPool.listDownArrowGray;
		} // end if
		else {
			tempImage = graphicPool.listDownArrow;
		} // end else
		g.drawImage(
				tempImage, 															// Image
				(int) (resizeRatioWidth() * Constants.arrowSplashStartingX), 		// X coordinate
				(int) (resizeRatioHeight() * Constants.arrowSplash_DownStartingY),	// Y coordinate
				(int) (resizeRatioWidth() * Constants.arrowSplashWidth), 			// width
				(int) (resizeRatioHeight() * Constants.arrowSplashHeight), 			// height
				startingClass); 													// ImageObserver
		if (numberOfElements != 0) {
			// draw the idle buttons
			if ((numberOfElements - data.splashLoadShiftOffset) > Constants.splashLoad_ItemMaxShownOnScreen) {
				numberOfElements = data.splashLoadShiftOffset + Constants.splashLoad_ItemMaxShownOnScreen;
			} // end if
			int menuY = Constants.splashLoad_ItemStartingY;
			for (int i = data.splashLoadShiftOffset; i < numberOfElements; ++i) {
				g.drawImage(
						graphicPool.splashLoadVec.get(i).getImage(), 						// Image
						(int) (resizeRatioWidth() * Constants.splashLoad_ItemStartingX),	// X coordinate
						(int) (resizeRatioHeight() * menuY), 								// Y coordinate
						(int) (resizeRatioWidth() * Constants.splashLoad_ItemWidth), 		// width
						(int) (resizeRatioHeight() * Constants.splashLoad_ItemHeight), 		// height
						startingClass); 													// ImageObserver
				menuY += (Constants.splashLoad_ItemOffsetBetween + Constants.splashLoad_ItemHeight);
			} // end for
			// draw the cursor
			g.drawImage(data.splashLoadCursor.getImage(),									// Image
					(int) (resizeRatioWidth() * data.splashLoadCursor.getPixelOffsetX()),	// X coordinate
					(int) (resizeRatioHeight() * data.splashLoadCursor.getPixelOffsetY()), 	// Y coordinate
					(int) (resizeRatioWidth() * data.splashLoadCursor.getWidth()), 			// width
					(int) (resizeRatioHeight() * data.splashLoadCursor.getHeight()), 		// height
					startingClass); 														// ImageObserver
			// draw the button text
			menuY = Constants.splashLoad_ItemStartingY;
			for (int i = data.splashLoadShiftOffset; i < numberOfElements; ++i) {
				g.setFont(g.getFont().deriveFont(
						(float) (resizeRatioFont() * Constants.splashLoad_ButtonTextSize)));
				g.drawString(
						FileHelper.returnOnlyName(graphicPool.splashLoadVec.get(i).getXmlPath()),
						(int) (resizeRatioWidth() * (Constants.splashLoad_ItemStartingX 
								+ Constants.splashLoad_ButtonTextOffsetX)),
						(int) (resizeRatioHeight() * (menuY + Constants.splashLoad_ButtonTextOffsetY)));
				menuY += (Constants.splashLoad_ItemOffsetBetween + Constants.splashLoad_ItemHeight);
			} // end for
		} // end if
	}

	// paints the graphic for the main background screen inside the builder application
	private void paintBuilderBackground(Graphics g) {
		g.drawImage(
				graphicPool.builderBackground, 										// Image
				0, 																	// X coordinate
				0, 																	// Y coordinate
				(int) (resizeRatioWidth() * Constants.screen_HorizontalDimension),	// width
				(int) (resizeRatioHeight() * Constants.screen_VerticalDimension), 	// height
				startingClass); 													// ImageObserver
	}

	// paints the graphic for the top menu inside the builder application
	private void paintTopMenu(Graphics g) {
		int menuX = Constants.topMenu_ItemStartingX;
		int topMenu_NumberOfElements = graphicPool.topMenu.length;
		if ((topMenu_NumberOfElements - data.topMenuShiftOffset) > Constants.topMenu_ItemMaxShownOnScreen) {
			topMenu_NumberOfElements = data.topMenuShiftOffset + Constants.topMenu_ItemMaxShownOnScreen;
		} // end if
		// add all 7 main menu options to the top menu
		for (int i = data.topMenuShiftOffset; i < topMenu_NumberOfElements; ++i) {
			g.drawImage(
					graphicPool.topMenu[i], 										// Image
					(int) (resizeRatioWidth() * menuX), 							// X coordinate
					(int) (resizeRatioHeight() * Constants.topMenu_ItemStartingY),	// Y coordinate
					(int) (resizeRatioWidth() * Constants.topMenu_ItemWidth), 		// width
					(int) (resizeRatioHeight() * Constants.topMenu_ItemHeight), 	// height
					startingClass); 												// ImageObserver
			menuX += (Constants.topMenu_ItemOffsetBetween + Constants.topMenu_ItemWidth);
		} // end for
	}

	// this function handles the printing of the left hand list (4 at a time) inside of the builder
	// application and can handle a list of elements greater than 4 with a scrolling feature
	private void paintLeftMenu(Graphics g) {
		switch (data.leftMenuState) {
		case Constants.leftMenuState_ManageResources:
			paintLeftMenu(g, graphicPool.leftMenu_ManageResources);
			break;
		case Constants.leftMenuState_EditObjects:
			paintLeftMenu(g, graphicPool.leftMenu_EditObjects);
			break;
		case Constants.leftMenuState_ManagePlayers:
			paintLeftMenu(g, graphicPool.leftMenu_ManagePlayers);
			break;
		case Constants.leftMenuState_LevelSelect:
			paintLeftMenu(g, graphicPool.leftMenu_LevelSelect);
			paintLeftMenuText(g, graphicPool.leftMenu_LevelSelect, 2);
			break;
		case Constants.leftMenuState_EditLevel:
			paintLeftMenu(g, graphicPool.leftMenu_EditLevel);
			break;
		case Constants.leftMenuState_PlaceObjects:
			paintLeftMenu(g, graphicPool.leftMenu_PlaceObjects);
			break;
		case Constants.leftMenuState_WinLoseEvents:
			paintLeftMenu(g, graphicPool.leftMenu_WinLoseEvents);
			break;
		case Constants.leftMenuState_TestLevel:
			paintLeftMenu(g, graphicPool.leftMenu_Test);
			break;
		case Constants.leftMenuState_ListAllLevelBackdrops:
			paintLeftMenu(g, graphicPool.leftMenu_LevelBackdrops);
			break;
		case Constants.leftMenuState_ListUserLevelBackdrops:
			paintLeftMenu(g, graphicPool.leftMenu_UserLevelBackdrops);
			break;
		case Constants.leftMenuState_ListUserLevelBackdropsWithExtra:
			paintLeftMenu(g, graphicPool.leftMenu_UserLevelBackdropsExtra);
			break;
		case Constants.leftMenuState_ListAllPlayers:
			paintLeftMenu(g, graphicPool.leftMenu_Players);
			break;
		case Constants.leftMenuState_ListUserPlayers:
			paintLeftMenu(g, graphicPool.leftMenu_UserPlayers);
			break;
		case Constants.leftMenuState_ListUserPlayersWithExtra:
			paintLeftMenu(g, graphicPool.leftMenu_UserPlayersExtra);
			break;
		/*
		case Constants.leftMenuState_ListAllNPCs:
			paintLeftMenu(g, graphicPool.leftMenu_NPCs);
			break;
		case Constants.leftMenuState_ListUserNPCs:
			paintLeftMenu(g, graphicPool.leftMenu_UserNPCs);
			break;
		case Constants.leftMenuState_ListUserNPCsWithExtra:
			paintLeftMenu(g, graphicPool.leftMenu_UserNPCsExtra);
			break;
		*/
		case Constants.leftMenuState_ListAllObjects:
			paintLeftMenu(g, graphicPool.leftMenu_Objects);
			break;
		case Constants.leftMenuState_ListUserObjects:
			paintLeftMenu(g, graphicPool.leftMenu_UserObjects);
			break;
		case Constants.leftMenuState_ListUserObjectsWithExtra:
			paintLeftMenu(g, graphicPool.leftMenu_UserObjectsExtra);
			break;
		case Constants.leftMenuState_ListAllTerrains:
			paintLeftMenu(g, graphicPool.leftMenu_Terrains);
			break;
		case Constants.leftMenuState_ListUserTerrains:
			paintLeftMenu(g, graphicPool.leftMenu_UserTerrains);
			break;
		case Constants.leftMenuState_ListUserTerrainsWithExtra:
			paintLeftMenu(g, graphicPool.leftMenu_UserTerrainsExtra);
			break;
		/*
		case Constants.leftMenuState_ListAllProjectiles:
			paintLeftMenu(g, graphicPool.leftMenu_Projectiles);
			break;
		case Constants.leftMenuState_ListUserProjectiles:
			paintLeftMenu(g, graphicPool.leftMenu_UserProjectiles);
			break;
		case Constants.leftMenuState_ListUserProjectilesWithExtra:
			paintLeftMenu(g, graphicPool.leftMenu_UserProjectilesExtra);
			break;
		*/
		case Constants.leftMenuState_ListAllMusic:
			paintLeftMenu(g, graphicPool.leftMenu_Music);
			paintLeftMenuText(g, graphicPool.leftMenu_Music);
			break;
		case Constants.leftMenuState_ListUserMusic:
			paintLeftMenu(g, graphicPool.leftMenu_UserMusic);
			paintLeftMenuText(g, graphicPool.leftMenu_UserMusic, 0);
			break;
		case Constants.leftMenuState_ListUserMusicWithExtra:
			paintLeftMenu(g, graphicPool.leftMenu_UserMusicExtra);
			paintLeftMenuText(g, graphicPool.leftMenu_UserMusicExtra, 1);
			break;
		case Constants.leftMenuState_ListAllSounds:
			paintLeftMenu(g, graphicPool.leftMenu_Sounds);
			paintLeftMenuText(g, graphicPool.leftMenu_Sounds);
			break;
		case Constants.leftMenuState_ListUserSounds:
			paintLeftMenu(g, graphicPool.leftMenu_UserSounds);
			paintLeftMenuText(g, graphicPool.leftMenu_UserSounds, 0);
			break;
		case Constants.leftMenuState_ListUserSoundsWithExtra:
			paintLeftMenu(g, graphicPool.leftMenu_UserSoundsExtra);
			paintLeftMenuText(g, graphicPool.leftMenu_UserSoundsExtra, 1);
			break;
		case Constants.leftMenuState_FourPlayers:
			paintLeftMenu(g, graphicPool.leftMenu_ManagePlayers);
			break;
		case Constants.leftMenuState_Layers:
			paintLeftMenu(g, graphicPool.leftMenu_PlaceObjectLayer);
			break;
		case Constants.leftMenuState_ListBasicMenu:
			paintLeftMenu(g, graphicPool.leftMenu_EditCharacteristicsBasic); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_ListSimpleMenu:
			paintLeftMenu(g, graphicPool.leftMenu_EditCharacteristicsSimple); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_ListAdvancedMenu:
			paintLeftMenu(g, graphicPool.leftMenu_EditCharacteristicsAdvanced); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxVelocity:
			paintLeftMenu(g, graphicPool.leftMenu_2Direction); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxDefense:
			paintLeftMenu(g, graphicPool.leftMenu_EditDirection); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxPower:
			paintLeftMenu(g, graphicPool.leftMenu_EditDirection); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_isAffectedBy:
			paintLeftMenu(g, graphicPool.leftMenu_EditAffectedBy); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxVelocityUp_Select:
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxVelocityDown_Select:
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxVelocityLeft_Select:
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxVelocityRight_Select:
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxHP_Select:
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxDefenseUp_Select:
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxDefenseDown_Select://
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxDefenseLeft_Select:
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxDefenseRight_Select:
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxPowerUp_Select:
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxPowerDown_Select:
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxPowerLeft_Select:
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxPowerRight_Select:
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_VertGravity_Select:
			paintLeftMenu(g, graphicPool.leftMenu_TrueFalse); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_HorzGravity_Select:
			paintLeftMenu(g, graphicPool.leftMenu_TrueFalse); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_XTraction_Select:
			paintLeftMenu(g, graphicPool.leftMenu_TrueFalse); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_YTraction_Select:
			paintLeftMenu(g, graphicPool.leftMenu_TrueFalse); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_Traction_Select:
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		case Constants.leftMenuState_MaxAcceleration_Select:
			paintLeftMenu(g, graphicPool.leftMenu_UpDown); //this is where it repoaints left menu leftMenu_EditObjects
			break;
		default:
			// ignore invalid cases
			break;
		} // end switch
	}

	// paints the graphic for the left menu inside the builder application, using an Image[] argument
	private void paintLeftMenu(Graphics g, Image[] array) {
		int y = Constants.leftMenu_ItemStartingY;
		leftMenu_NumberOfElements = array.length;
		if ((leftMenu_NumberOfElements - data.leftMenuShiftOffset) > Constants.leftMenu_ItemMaxShownOnScreen) {
			leftMenu_NumberOfElements = data.leftMenuShiftOffset + Constants.leftMenu_ItemMaxShownOnScreen;
		} // end if
		for (int i = data.leftMenuShiftOffset; i < leftMenu_NumberOfElements; ++i) {
			g.drawImage(
					array[i], 															// Image
					(int) (resizeRatioWidth() * data.leftMenuCursor.getPixelOffsetX()),	// X coordinate
					(int) (resizeRatioHeight() * y), 									// Y coordinate
					(int) (resizeRatioWidth() * data.leftMenuCursor.getWidth()), 		// Width
					(int) (resizeRatioHeight() * data.leftMenuCursor.getHeight()), 		// Height
					startingClass); 													// ImageObserver
			y += Constants.leftMenu_ItemOffsetBetween + data.leftMenuCursor.getHeight();
		} // end for
	}

	// paints the graphic for the left menu inside the builder application, using a GraphicObject[] argument
	private void paintLeftMenu(Graphics g, GraphicObject[] array) {
		int y = Constants.leftMenu_ItemStartingY;
		leftMenu_NumberOfElements = array.length;
		if ((leftMenu_NumberOfElements - data.leftMenuShiftOffset) > Constants.leftMenu_ItemMaxShownOnScreen) {
			leftMenu_NumberOfElements = data.leftMenuShiftOffset + Constants.leftMenu_ItemMaxShownOnScreen;
		} // end if
		for (int i = data.leftMenuShiftOffset; i < leftMenu_NumberOfElements; ++i) {
			g.drawImage(
					array[i].getImage(), 												// Image
					(int) (resizeRatioWidth() * data.leftMenuCursor.getPixelOffsetX()),	// X coordinate
					(int) (resizeRatioHeight() * y), 									// Y coordinate
					(int) (resizeRatioWidth() * data.leftMenuCursor.getWidth()), 		// Width
					(int) (resizeRatioHeight() * data.leftMenuCursor.getHeight()), 		// Height
					startingClass); 													// ImageObserver
			y += Constants.leftMenu_ItemOffsetBetween + data.leftMenuCursor.getHeight();
		} // end for
	}

	// paints the graphic for the left menu inside the builder application, using a Vector<GraphicObject> argument
	private void paintLeftMenu(Graphics g, Vector<GraphicObject> vector) {
		int y = Constants.leftMenu_ItemStartingY;
		leftMenu_NumberOfElements = vector.size();
		if ((leftMenu_NumberOfElements - data.leftMenuShiftOffset) > Constants.leftMenu_ItemMaxShownOnScreen) {
			leftMenu_NumberOfElements = data.leftMenuShiftOffset + Constants.leftMenu_ItemMaxShownOnScreen;
		} // end if
		for (int i = data.leftMenuShiftOffset; i < leftMenu_NumberOfElements; ++i) {
			g.drawImage(
					vector.get(i).getImage(), 											// Image
					(int) (resizeRatioWidth() * data.leftMenuCursor.getPixelOffsetX()),	// X coordinate
					(int) (resizeRatioHeight() * y), 									// Y coordinate
					(int) (resizeRatioWidth() * data.leftMenuCursor.getWidth()), 		// Width
					(int) (resizeRatioHeight() * data.leftMenuCursor.getHeight()),		// Height
					startingClass); 													// ImageObserver
			y += Constants.leftMenu_ItemOffsetBetween + data.leftMenuCursor.getHeight();
		} // end for
	}

	// paints the text for the left menu items inside the builder application, using a Vector<GraphicObject> argument
	private void paintLeftMenuText(Graphics g, Vector<GraphicObject> vector, int textStartIndex) {
		g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.leftMenu_TextSize)));
		int itemY = Constants.leftMenu_ItemStartingY;
		leftMenu_NumberOfElements = vector.size();
		if ((leftMenu_NumberOfElements - data.leftMenuShiftOffset) > Constants.leftMenu_ItemMaxShownOnScreen) {
			leftMenu_NumberOfElements = data.leftMenuShiftOffset + Constants.leftMenu_ItemMaxShownOnScreen;
		} // end if
		for (int i = data.leftMenuShiftOffset; i < leftMenu_NumberOfElements; ++i) {
			if (i >= textStartIndex) {
				g.drawString(
						FileHelper.returnOnlyName(vector.get(i).getXmlPath()),
						(int) (resizeRatioWidth() * (Constants.leftMenu_ItemSetStartXWide 
								+ Constants.leftMenu_TextOffsetX)),
						(int) (resizeRatioHeight() * (itemY + Constants.leftMenu_TextOffsetY)));
			}
			itemY += Constants.leftMenu_ItemOffsetBetween + data.leftMenuCursor.getHeight();
		} // end for
	}

	// paints the text for the left menu items inside the builder application, using a GraphicObject[] argument
	private void paintLeftMenuText(Graphics g, GraphicObject[] array) {
		g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.leftMenu_TextSize)));
		int itemY = Constants.leftMenu_ItemStartingY;
		leftMenu_NumberOfElements = array.length;
		if ((leftMenu_NumberOfElements - data.leftMenuShiftOffset) > Constants.leftMenu_ItemMaxShownOnScreen) {
			leftMenu_NumberOfElements = data.leftMenuShiftOffset + Constants.leftMenu_ItemMaxShownOnScreen;
		} // end if
		for (int i = data.leftMenuShiftOffset; i < leftMenu_NumberOfElements; ++i) {
			g.drawString(
					FileHelper.returnOnlyName(array[i].getXmlPath()),
					(int) (resizeRatioWidth() * (Constants.leftMenu_ItemSetStartXWide 
							+ Constants.leftMenu_TextOffsetX)),
					(int) (resizeRatioHeight() * (itemY + Constants.leftMenu_TextOffsetY)));
			itemY += Constants.leftMenu_ItemOffsetBetween + data.leftMenuCursor.getHeight();
		} // end for
	}

	// paints the level window and its objects by redirecting to more specific methods
	private void paintWindow(Graphics g) {
		switch (data.topMenuState)
		{
		case Constants.topMenu_IndexManageResources:
			instructionCounter = 0;
			paintWindowBackground(g);
			paintWindowPreview(g);
			break;
		case Constants.topMenu_IndexEditObjects:
			instructionCounter = -1;
			data.windowState = Constants.windowState_Edit;
			paintWindowEditObjects(g);
			//break;

			//paintWindowPreview(g);
			//instructionCounter = 1;
			break;
		case Constants.topMenu_IndexManagePlayers:
			instructionCounter = 2;
			paintWindowBackground(g);
			paintWindowPreview(g);
			break;
		case Constants.topMenu_IndexLevelSelect:
			paintWindowPreview(g);
			instructionCounter = 3;
			break;
		case Constants.topMenu_IndexEditLevel:
			instructionCounter = -1;
			paintWindowBackground(g);
			paintWindowPreview(g);
			break;
		case Constants.topMenu_IndexPlaceObject:
			instructionCounter = -1;
			paintWindowBackground(g);
			paintLevelObjects(g);
			break;
/*		case Constants.topMenu_IndexWinLoseEvents:
			instructionCounter = -1;
			paintWindowBackground(g);
			paintLevelObjects(g);
			break;*/
		case Constants.topMenu_IndexTestLevel:
			instructionCounter = -1;
			break;
		default:
			//ignore invalid cases
			break;
		} // end switch
	}

	// paints the level window background inside the builder application
	private void paintWindowBackground(Graphics g) {
		Image tmpImage = null;
		switch (data.windowState)
		{
		case Constants.windowState_LevelMap:
			tmpImage = data.currentLevelData.getBackgroundImage().getImage();
			break;
		case Constants.windowState_PreviewAnimation:
			tmpImage = graphicPool.builderPreviewWindow;
			break;
		default:
			// ignore invalid cases
			break;
		} // end switch
		if (tmpImage != null) {
			g.drawImage(
					tmpImage, 														// Image
					(int) (resizeRatioWidth() * Constants.window_MainImageStartingX),	// X coordinate
					(int) (resizeRatioHeight() * Constants.window_MainImageStartingY),	// Y coordinate
					(int) (resizeRatioWidth() * Constants.window_MainImageWidth), 		// width
					(int) (resizeRatioHeight() * Constants.window_MainImageHeight), 	// height
					startingClass); 													// ImageObserver
		} // end if
	}

	private void paintWindowPreview(Graphics g)
	{
		paintWindowPreviewTitle(g);
		switch (data.leftMenuState)
		{
		case Constants.leftMenuState_ListAllLevelBackdrops:
		case Constants.leftMenuState_ListUserLevelBackdrops:
		case Constants.leftMenuState_ListUserLevelBackdropsWithExtra:
			paintWindowPreviewLevelBackdrop(g);
			break;
		case Constants.leftMenuState_ListAllTerrains:
		case Constants.leftMenuState_ListUserTerrains:
		case Constants.leftMenuState_ListUserTerrainsWithExtra:
			paintWindowPreviewTerrains(g);
			break;
		//case Constants.leftMenuState_ManagePlayers:
		//case Constants.leftMenuState_ListAllNPCs:
		case Constants.leftMenuState_ListAllPlayers:
		//case Constants.leftMenuState_ListUserNPCs:
		//case Constants.leftMenuState_ListUserNPCsWithExtra:
		case Constants.leftMenuState_ListUserPlayers:
		case Constants.leftMenuState_ListUserPlayersWithExtra:
			paintWindowPreviewCharacters(g);
			break;
		/*
		case Constants.leftMenuState_ListAllProjectiles:
		case Constants.leftMenuState_ListUserProjectiles:
		case Constants.leftMenuState_ListUserProjectilesWithExtra:
			paintWindowPreviewProjectiles(g);
			break;
		*/
		case Constants.leftMenuState_ListAllObjects:
		case Constants.leftMenuState_ListUserObjects:
		case Constants.leftMenuState_ListUserObjectsWithExtra:
			paintWindowPreviewObjects(g);
			break;
		case Constants.leftMenuState_ListAllMusic:
		case Constants.leftMenuState_ListAllSounds:
		case Constants.leftMenuState_ListUserMusic:
		case Constants.leftMenuState_ListUserMusicWithExtra:
		case Constants.leftMenuState_ListUserSounds:
		case Constants.leftMenuState_ListUserSoundsWithExtra:
			paintWindowPreviewSounds(g);
			break;
		case Constants.leftMenuState_EditLevel:
			if (data.leftMenuCursor.getIndexVertical() == Constants.leftMenuList_EditLevelSetBackdrop)
			{
				paintWindowPreviewLevelBackdrop(g);
			}
			else if (data.leftMenuCursor.getIndexVertical() == Constants.leftMenuList_EditLevelSetMusic)
			{
				paintWindowPreviewSounds(g);
			}
			break;
		default:
			// draws instructions when nothing is selected in the first level menu;
			// 		also ignore invalid cases
			if(instructionCounter != -1){
				g.drawImage(
						graphicPool.instruction[instructionCounter], 	// Image
						(int) (resizeRatioWidth() * 160),				// X coordinate
						(int) (resizeRatioHeight() * 96),				// Y coordinate
						(int) (resizeRatioWidth() * 640), 				// width
						(int) (resizeRatioHeight() * 384), 				// height
						startingClass); 	
			}
			break;
		} // end switch
		switch (data.leftMenuState)
		{
		case Constants.leftMenuState_ListAllLevelBackdrops:
		case Constants.leftMenuState_ListAllTerrains:
		//case Constants.leftMenuState_ListAllNPCs:
		case Constants.leftMenuState_ListAllPlayers:
		//case Constants.leftMenuState_ListAllProjectiles:
		case Constants.leftMenuState_ListAllObjects:
		case Constants.leftMenuState_ListAllMusic:
		case Constants.leftMenuState_ListAllSounds:
			paintWindowPreviewBottomText(g);
			break;
		case Constants.leftMenuList_ManagePlayersP1:
		default:
			// ignore invalid cases
			break;
		} // end switch
	}

	private void paintWindowPreviewTitle(Graphics g)
	{
		// paint the title
		int startingX = 0;
		String title = "";
		g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.windowPreviewTitle_TextSize)));
		switch (data.leftMenuState)
		{
		case Constants.leftMenuState_ListAllLevelBackdrops:
		case Constants.leftMenuState_ListUserLevelBackdrops:
		case Constants.leftMenuState_ListUserLevelBackdropsWithExtra:
		case Constants.leftMenuState_ListAllTerrains:
		case Constants.leftMenuState_ListUserTerrains:
		case Constants.leftMenuState_ListUserTerrainsWithExtra:
			startingX = Constants.windowPreviewTitle_ImageStartingX;
			title = Constants.windowPreviewTitle_Image;
			break;
		//case Constants.leftMenuState_ManagePlayers:
		//case Constants.leftMenuState_ListAllNPCs:
		case Constants.leftMenuState_ListAllPlayers:
		//case Constants.leftMenuState_ListUserNPCs:
		//case Constants.leftMenuState_ListUserNPCsWithExtra:
		case Constants.leftMenuState_ListUserPlayers:
		case Constants.leftMenuState_ListUserPlayersWithExtra:
		//case Constants.leftMenuState_ListAllProjectiles:
		//case Constants.leftMenuState_ListUserProjectiles:
		//case Constants.leftMenuState_ListUserProjectilesWithExtra:
		case Constants.leftMenuState_ListAllObjects:
		case Constants.leftMenuState_ListUserObjects:
		case Constants.leftMenuState_ListUserObjectsWithExtra:
			startingX = Constants.windowPreviewTitle_AnimationsStartingX;
			title = Constants.windowPreviewTitle_Animations;
			break;
		case Constants.leftMenuState_ListAllMusic:
		case Constants.leftMenuState_ListAllSounds:
		case Constants.leftMenuState_ListUserMusic:
		case Constants.leftMenuState_ListUserMusicWithExtra:
		case Constants.leftMenuState_ListUserSounds:
		case Constants.leftMenuState_ListUserSoundsWithExtra:
			startingX = Constants.windowPreviewTitle_SoundStartingX;
			title = Constants.windowPreviewTitle_SoundPreview;
			break;
		case Constants.leftMenuState_EditLevel:
			if (data.leftMenuCursor.getIndexVertical() == Constants.leftMenuList_EditLevelSetBackdrop)
			{
				startingX = Constants.windowPreviewTitle_ImageStartingX;
				title = Constants.windowPreviewTitle_Image;
			}
			else if (data.leftMenuCursor.getIndexVertical() == Constants.leftMenuList_EditLevelSetMusic)
			{
				startingX = Constants.windowPreviewTitle_SoundStartingX;
				title = Constants.windowPreviewTitle_SoundPreview;
			}
			break;
		default:
			// ignore invalid cases
			break;
		} // end switch
		g.drawString(
				title,
				(int) (resizeRatioWidth() * startingX),
				(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
	}

	private void paintWindowPreviewBottomText(Graphics g)
	{
		// paint the delete text
		g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.windowPreviewBottomText_TextSize)));
		String str = "";
		int startX = 0;
		switch (data.resourceManager_ItemState)
		{
		case Constants.windowPreviewBottomText_StateNewAdded:
			str = Constants.windowPreviewBottomText_StrNewAdded;
			startX = Constants.windowPreviewBottomText_StartingXNew;
			break;
		case Constants.windowPreviewBottomText_StateAlreadyAdded:
			str = Constants.windowPreviewBottomText_StrAlreadyAdded;
			startX = Constants.windowPreviewBottomText_StartingXAlready;
			break;
		default:
			// ignore invalid cases
			break;
		} // end switch
		g.drawString(
				str,
				(int) (resizeRatioWidth() * startX),
				(int) (resizeRatioHeight() * Constants.windowPreviewBottomText_StartingY));
	}

	private void paintWindowPreviewLevelBackdrop(Graphics g)
	{
		if (!((data.leftMenuState == Constants.leftMenuState_ListUserLevelBackdropsWithExtra) && 
				(data.leftMenuCursor.getIndexVertical() == 0)))
		{
			// draw the border
			g.drawImage(
					graphicPool.cursor, 													// Image
					(int) (resizeRatioWidth() * Constants.windowPreviewLevel_StartingX),	// X coordinate
					(int) (resizeRatioHeight() * Constants.windowPreviewLevel_StartingY),	// Y coordinate
					(int) (resizeRatioWidth() * Constants.windowPreviewLevel_SizeX), 		// width
					(int) (resizeRatioHeight() * Constants.windowPreviewLevel_SizeY), 		// height
					startingClass); 														// ImageObserver
			// draw the image
			Image tmpImage = null;
			int index = data.leftMenuCursor.getIndexVertical();
			switch (data.leftMenuState)
			{
			case Constants.leftMenuState_ListAllLevelBackdrops:
				tmpImage = graphicPool.leftMenu_LevelBackdrops[index].getImage();
				break;
			case Constants.leftMenuState_ListUserLevelBackdrops:
				tmpImage = graphicPool.leftMenu_UserLevelBackdrops.get(index).getImage();
				break;
			case Constants.leftMenuState_ListUserLevelBackdropsWithExtra:
				if (index > 0)
				{
					tmpImage = graphicPool.leftMenu_UserLevelBackdropsExtra.get(index).getImage();
				} // end if
				else
				{
					tmpImage = graphicPool.previewNA;
				} // end else
				break;
			case Constants.leftMenuState_EditLevel:
				if (data.currentLevelData.getBackgroundImage().getXmlPath().equalsIgnoreCase(""))
				{
					tmpImage = graphicPool.previewNA;
				}
				else
				{
					tmpImage = data.currentLevelData.getBackgroundImage().getImage();
				}
				break;
			default:
				// ignore invalid cases
				break;
			} // end switch
			if (tmpImage != null)
			{
				g.drawImage(
						tmpImage, 													// Image
						(int) (resizeRatioWidth() * (Constants.windowPreviewLevel_StartingX 
								+ Constants.windowPreviewLevel_BorderSize)),		// X coordinate
						(int) (resizeRatioHeight() * (Constants.windowPreviewLevel_StartingY
								+ Constants.windowPreviewLevel_BorderSize)), 		// Y coordinate
						(int) (resizeRatioWidth() * (Constants.windowPreviewLevel_SizeX
								- Constants.windowPreviewLevel_BorderSize * 2)),	// width
						(int) (resizeRatioHeight() * (Constants.windowPreviewLevel_SizeY
								- Constants.windowPreviewLevel_BorderSize * 2)),	// height
						startingClass); 											// ImageObserver
			} // end if
		} // end if
	}

	private void paintWindowPreviewTerrains(Graphics g)
	{
		if (!((data.leftMenuState == Constants.leftMenuState_ListUserTerrainsWithExtra) && 
				(data.leftMenuCursor.getIndexVertical() == 0)))
		{
			// draw the border
			g.drawImage(
					graphicPool.cursor, 														// Image
					(int) (resizeRatioWidth() * Constants.windowPreviewTerrains_StartingX),		// X coordinate
					(int) (resizeRatioHeight() * Constants.windowPreviewTerrains_StartingY),	// Y coordinate
					(int) (resizeRatioWidth() * Constants.windowPreviewTerrains_SizeX), 		// width
					(int) (resizeRatioHeight() * Constants.windowPreviewTerrains_SizeY), 		// height
					startingClass); 															// ImageObserver
			// draw the image
			Image tmpImage = graphicPool.previewNA;
			switch (data.leftMenuState)
			{
			case Constants.leftMenuState_ListAllTerrains:
				tmpImage = graphicPool.leftMenu_Terrains[data.leftMenuCursor.getIndexVertical()].getImage();
				break;
			case Constants.leftMenuState_ListUserTerrains:
				tmpImage = graphicPool.leftMenu_UserTerrains.get(data.leftMenuCursor.getIndexVertical()).getImage();
				break;
			case Constants.leftMenuState_ListUserTerrainsWithExtra:
				if (data.leftMenuCursor.getIndexVertical() > 0)
				{
					tmpImage = graphicPool.leftMenu_UserTerrainsExtra.get(data.leftMenuCursor.getIndexVertical()).getImage();
				}
				break;
			default:
				// ignore invalid cases
				break;
			} // end switch
			g.drawImage(
					tmpImage, 													// Image
					(int) (resizeRatioWidth() * (Constants.windowPreviewTerrains_StartingX 
							+ Constants.windowPreviewTerrains_BorderSize)),		// X coordinate
					(int) (resizeRatioHeight() * (Constants.windowPreviewTerrains_StartingY
							+ Constants.windowPreviewTerrains_BorderSize)), 	// Y coordinate
					(int) (resizeRatioWidth() * (Constants.windowPreviewTerrains_SizeX
							- Constants.windowPreviewTerrains_BorderSize * 2)),	// width
					(int) (resizeRatioHeight() * (Constants.windowPreviewTerrains_SizeY
							- Constants.windowPreviewTerrains_BorderSize * 2)),	// height
					startingClass); 											// ImageObserver
		} // end if
	}

	private void paintWindowPreviewCharacters(Graphics g)
	{
		if (!((/*(data.leftMenuState == Constants.leftMenuState_ListUserNPCsWithExtra) 
				|| */(data.leftMenuState == Constants.leftMenuState_ListUserPlayersWithExtra)) && 
				(data.leftMenuCursor.getIndexVertical() == 0)))
		{
			// draw the borders
			for (int x = 0; x < Constants.windowPreviewCharacters_NumOnScreenX; ++x)
			{
				for (int y = 0; y < Constants.windowPreviewCharacters_NumOnScreenY; ++y)
				{
					g.drawImage(
							graphicPool.cursor, 													// Image
							(int) (resizeRatioWidth() * (Constants.windowPreviewCharacters_StartingX
									+ (x * (Constants.windowPreviewCharacters_SizeX 
									+ Constants.windowPreviewCharacters_BetweenGapX)))),			// X coordinate
							(int) (resizeRatioHeight() * (Constants.windowPreviewCharacters_StartingY
									+ (y * (Constants.windowPreviewCharacters_SizeY
									+ Constants.windowPreviewCharacters_BetweenGapY)))),			// Y coordinate
							(int) (resizeRatioWidth() * Constants.windowPreviewCharacters_SizeX), 	// width
							(int) (resizeRatioHeight() * Constants.windowPreviewCharacters_SizeY), 	// height
							startingClass); 														// ImageObserver
				} // end for
			} // end for
			// draw the strings
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.windowPreviewCharacters_TextSize)));
			String str = "";
			for (int x = 0; x < Constants.windowPreviewCharacters_NumOnScreenX; ++x)
			{
				for (int y = 0; y < Constants.windowPreviewCharacters_NumOnScreenY; ++y)
				{
					if ((x == 0) && (y == 0)) {str = Constants.windowPreviewCharacters_Str0_0;}
					else if ((x == 0) && (y == 1)) {str = Constants.windowPreviewCharacters_Str0_1;}
					else if ((x == 0) && (y == 2)) {str = Constants.windowPreviewCharacters_Str0_2;}
					else if ((x == 0) && (y == 3)) {str = Constants.windowPreviewCharacters_Str0_3;}
					else if ((x == 1) && (y == 0)) {str = Constants.windowPreviewCharacters_Str1_0;}
					else if ((x == 1) && (y == 1)) {str = Constants.windowPreviewCharacters_Str1_1;}
					else if ((x == 1) && (y == 2)) {str = Constants.windowPreviewCharacters_Str1_2;}
					else if ((x == 1) && (y == 3)) {str = Constants.windowPreviewCharacters_Str1_3;}
					else if ((x == 2) && (y == 0)) {str = Constants.windowPreviewCharacters_Str2_0;}
					else if ((x == 2) && (y == 1)) {str = Constants.windowPreviewCharacters_Str2_1;}
					else if ((x == 2) && (y == 2)) {str = Constants.windowPreviewCharacters_Str2_2;}
					else if ((x == 2) && (y == 3)) {str = Constants.windowPreviewCharacters_Str2_3;}
					else if ((x == 3) && (y == 0)) {str = Constants.windowPreviewCharacters_Str3_0;}
					else if ((x == 3) && (y == 1)) {str = Constants.windowPreviewCharacters_Str3_1;}
					else if ((x == 3) && (y == 2)) {str = Constants.windowPreviewCharacters_Str3_2;}
					else if ((x == 3) && (y == 3)) {str = Constants.windowPreviewCharacters_Str3_3;}
					else if ((x == 4) && (y == 0)) {str = Constants.windowPreviewCharacters_Str4_0;}
					else if ((x == 4) && (y == 1)) {str = Constants.windowPreviewCharacters_Str4_1;}
					else if ((x == 4) && (y == 2)) {str = Constants.windowPreviewCharacters_Str4_2;}
					else if ((x == 4) && (y == 3)) {str = Constants.windowPreviewCharacters_Str4_3;}
					else if ((x == 5) && (y == 0)) {str = Constants.windowPreviewCharacters_Str5_0;}
					else if ((x == 5) && (y == 1)) {str = Constants.windowPreviewCharacters_Str5_1;}
					else if ((x == 5) && (y == 2)) {str = Constants.windowPreviewCharacters_Str5_2;}
					else if ((x == 5) && (y == 3)) {str = Constants.windowPreviewCharacters_Str5_3;}
					else {str = "";}
					g.drawString(
							str, 
							(int) (resizeRatioWidth() * (Constants.windowPreviewCharacters_StartingX
									+ (x * (Constants.windowPreviewCharacters_SizeX 
									+ Constants.windowPreviewCharacters_BetweenGapX)))),		// X coordinate
							(int) (resizeRatioHeight() * (Constants.windowPreviewCharacters_StartingY
									+ (y * (Constants.windowPreviewCharacters_SizeY
									+ Constants.windowPreviewCharacters_BetweenGapY))) - 5));	// Y coordinate
				} // end for
			} // end for
			// draw the actual animations
			if (animationDelayCounter != Constants.previewAnimation_Delay) { ++animationDelayCounter; }
			else { animationDelayCounter = 0; }
			Image tmpImage = null;
			for (int x = 0; x < Constants.windowPreviewCharacters_NumOnScreenX; ++x)
			{
				for (int y = 0; y < Constants.windowPreviewCharacters_NumOnScreenY; ++y)
				{
					if ((x == Constants.windowPreviewCharacters_WalkX) && (y == Constants.windowPreviewCharacters_WalkY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_StandLeft;
							if (animationInt_StandLeft == graphicPool.preview_Walk.size()) { animationInt_StandLeft = 0; }
						} // end if
						tmpImage = graphicPool.preview_Walk.get(animationInt_StandLeft);
					} // end if
					else if ((x == Constants.windowPreviewCharacters_StandX) && (y == Constants.windowPreviewCharacters_StandY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_StandRight;
							if (animationInt_StandRight == graphicPool.preview_Stand.size()) { animationInt_StandRight = 0; }
						} // end if
						tmpImage = graphicPool.preview_Stand.get(animationInt_StandRight);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_StandUpX) && (y == Constants.windowPreviewCharacters_StandUpY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_StandUp;
							if (animationInt_StandUp == graphicPool.preview_StandUp.size()) { animationInt_StandUp = 0; }
						} // end if
						tmpImage = graphicPool.preview_StandUp.get(animationInt_StandUp);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_StandDownX) && (y == Constants.windowPreviewCharacters_StandDownY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_StandDown;
							if (animationInt_StandDown == graphicPool.preview_StandDown.size()) { animationInt_StandDown = 0; }
						} // end if
						tmpImage = graphicPool.preview_StandDown.get(animationInt_StandDown);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_JumpFallX) && (y == Constants.windowPreviewCharacters_JumpFallY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_MoveLeft;
							if (animationInt_MoveLeft == graphicPool.preview_JumpFall.size()) { animationInt_MoveLeft = 0; }
						} // end if
						tmpImage = graphicPool.preview_JumpFall.get(animationInt_MoveLeft);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_JumpUpX) && (y == Constants.windowPreviewCharacters_JumpUpY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_MoveRight;
							if (animationInt_MoveRight == graphicPool.preview_JumpUp.size()) { animationInt_MoveRight = 0; }
						} // end if
						tmpImage = graphicPool.preview_JumpUp.get(animationInt_MoveRight);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_MoveUpX) && (y == Constants.windowPreviewCharacters_MoveUpY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_MoveUp;
							if (animationInt_MoveUp == graphicPool.preview_MoveUp.size()) { animationInt_MoveUp = 0; }
						} // end if
						tmpImage = graphicPool.preview_MoveUp.get(animationInt_MoveUp);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_MoveDownX) && (y == Constants.windowPreviewCharacters_MoveDownY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_MoveDown;
							if (animationInt_MoveDown == graphicPool.preview_MoveDown.size()) { animationInt_MoveDown = 0; }
						} // end if
						tmpImage = graphicPool.preview_MoveDown.get(animationInt_MoveDown);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_FallX) && (y == Constants.windowPreviewCharacters_FallY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_DeathLeft;
							if (animationInt_DeathLeft == graphicPool.preview_Fall.size()) { animationInt_DeathLeft = 0; }
						} // end if
						tmpImage = graphicPool.preview_Fall.get(animationInt_DeathLeft);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_WallSlideX) && (y == Constants.windowPreviewCharacters_WallSlideY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_DeathRight;
							if (animationInt_DeathRight == graphicPool.preview_WallSlide.size()) { animationInt_DeathRight = 0; }
						} // end if
						tmpImage = graphicPool.preview_WallSlide.get(animationInt_DeathRight);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_DeathUpX) && (y == Constants.windowPreviewCharacters_DeathUpY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_DeathUp;
							if (animationInt_DeathUp == graphicPool.preview_DeathUp.size()) { animationInt_DeathUp = 0; }
						} // end if
						tmpImage = graphicPool.preview_DeathUp.get(animationInt_DeathUp);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_DeathDownX) && (y == Constants.windowPreviewCharacters_DeathDownY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_DeathDown;
							if (animationInt_DeathDown == graphicPool.preview_DeathDown.size()) { animationInt_DeathDown = 0; }
						} // end if
						tmpImage = graphicPool.preview_DeathDown.get(animationInt_DeathDown);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_MainLeftX) && (y == Constants.windowPreviewCharacters_MainLeftY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_MainLeft;
							if (animationInt_MainLeft == graphicPool.preview_MainLeft.size()) { animationInt_MainLeft = 0; }
						} // end if
						tmpImage = graphicPool.preview_MainLeft.get(animationInt_MainLeft);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_DeathX) && (y == Constants.windowPreviewCharacters_DeathY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_MainRight;
							if (animationInt_MainRight == graphicPool.preview_Death.size()) { animationInt_MainRight = 0; }
						} // end if
						tmpImage = graphicPool.preview_Death.get(animationInt_MainRight);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_MainUpX) && (y == Constants.windowPreviewCharacters_MainUpY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_MainUp;
							if (animationInt_MainUp == graphicPool.preview_MainUp.size()) { animationInt_MainUp = 0; }
						} // end if
						tmpImage = graphicPool.preview_MainUp.get(animationInt_MainUp);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_MainDownX) && (y == Constants.windowPreviewCharacters_MainDownY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_MainDown;
							if (animationInt_MainDown == graphicPool.preview_MainDown.size()) { animationInt_MainDown = 0; }
						} // end if
						tmpImage = graphicPool.preview_MainDown.get(animationInt_MainDown);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_Sub1LeftX) && (y == Constants.windowPreviewCharacters_Sub1LeftY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_Sub1Left;
							if (animationInt_Sub1Left == graphicPool.preview_Sub1Left.size()) { animationInt_Sub1Left = 0; }
						} // end if
						tmpImage = graphicPool.preview_Sub1Left.get(animationInt_Sub1Left);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_Sub1RightX) && (y == Constants.windowPreviewCharacters_Sub1RightY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_Sub1Right;
							if (animationInt_Sub1Right == graphicPool.preview_Sub1Right.size()) { animationInt_Sub1Right = 0; }
						} // end if
						tmpImage = graphicPool.preview_Sub1Right.get(animationInt_Sub1Right);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_Sub1UpX) && (y == Constants.windowPreviewCharacters_Sub1UpY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_Sub1Up;
							if (animationInt_Sub1Up == graphicPool.preview_Sub1Up.size()) { animationInt_Sub1Up = 0; }
						} // end if
						tmpImage = graphicPool.preview_Sub1Up.get(animationInt_Sub1Up);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_Sub1DownX) && (y == Constants.windowPreviewCharacters_Sub1DownY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_Sub1Down;
							if (animationInt_Sub1Down == graphicPool.preview_Sub1Down.size()) { animationInt_Sub1Down = 0; }
						} // end if
						tmpImage = graphicPool.preview_Sub1Down.get(animationInt_Sub1Down);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_Sub2LeftX) && (y == Constants.windowPreviewCharacters_Sub2LeftY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_Sub2Left;
							if (animationInt_Sub2Left == graphicPool.preview_Sub2Left.size()) { animationInt_Sub2Left = 0; }
						} // end if
						tmpImage = graphicPool.preview_Sub2Left.get(animationInt_Sub2Left);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_Sub2RightX) && (y == Constants.windowPreviewCharacters_Sub2RightY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_Sub2Right;
							if (animationInt_Sub2Right == graphicPool.preview_Sub2Right.size()) { animationInt_Sub2Right = 0; }
						} // end if
						tmpImage = graphicPool.preview_Sub2Right.get(animationInt_Sub2Right);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_Sub2UpX) && (y == Constants.windowPreviewCharacters_Sub2UpY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_Sub2Up;
							if (animationInt_Sub2Up == graphicPool.preview_Sub2Up.size()) { animationInt_Sub2Up = 0; }
						} // end if
						tmpImage = graphicPool.preview_Sub2Up.get(animationInt_Sub2Up);
					} // end else if
					else if ((x == Constants.windowPreviewCharacters_Sub2DownX) && (y == Constants.windowPreviewCharacters_Sub2DownY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_Sub2Down;
							if (animationInt_Sub2Down == graphicPool.preview_Sub2Down.size()) { animationInt_Sub2Down = 0; }
						} // end if
						tmpImage = graphicPool.preview_Sub2Down.get(animationInt_Sub2Down);
					} // end else if
					// Finally draw the image
					g.drawImage(
							tmpImage, 													// Image
							(int) (resizeRatioWidth() * (Constants.windowPreviewCharacters_StartingX
									+ (x * (Constants.windowPreviewCharacters_SizeX 
									+ Constants.windowPreviewCharacters_BetweenGapX)))),			// X coordinate
							(int) (resizeRatioHeight() * (Constants.windowPreviewCharacters_StartingY
									+ (y * (Constants.windowPreviewCharacters_SizeY
									+ Constants.windowPreviewCharacters_BetweenGapY)))),			// Y coordinate
							(int) (resizeRatioWidth() * Constants.windowPreviewCharacters_SizeX), 	// width
							(int) (resizeRatioHeight() * Constants.windowPreviewCharacters_SizeY), 	// height
							startingClass); 														// ImageObserver
				} // end for
			} // end for
		} // end if
	}

	private void paintWindowPreviewProjectiles(Graphics g)
	{
		if (!(/*(data.leftMenuState == Constants.leftMenuState_ListUserProjectilesWithExtra) && */
				(data.leftMenuCursor.getIndexVertical() == 0)))
		{
			// draw the borders
			for (int x = 0; x < Constants.windowPreviewProjectiles_NumOnScreenX; ++x)
			{
				for (int y = 0; y < Constants.windowPreviewProjectiles_NumOnScreenY; ++y)
				{
					g.drawImage(
							graphicPool.cursor, 													// Image
							(int) (resizeRatioWidth() * (Constants.windowPreviewProjectiles_StartingX
									+ (x * (Constants.windowPreviewProjectiles_SizeX 
									+ Constants.windowPreviewProjectiles_BetweenGapX)))),			// X coordinate
							(int) (resizeRatioHeight() * (Constants.windowPreviewProjectiles_StartingY
									+ (y * (Constants.windowPreviewProjectiles_SizeY
									+ Constants.windowPreviewProjectiles_BetweenGapY)))),			// Y coordinate
							(int) (resizeRatioWidth() * Constants.windowPreviewProjectiles_SizeX), 	// width
							(int) (resizeRatioHeight() * Constants.windowPreviewProjectiles_SizeY), // height
							startingClass); 														// ImageObserver
				} // end for
			} // end for
			// draw the strings
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.windowPreviewProjectiles_TextSize)));
			String str = "";
			for (int x = 0; x < Constants.windowPreviewProjectiles_NumOnScreenX; ++x)
			{
				for (int y = 0; y < Constants.windowPreviewProjectiles_NumOnScreenY; ++y)
				{
					if ((x == 0) && (y == 0)) {str = Constants.windowPreviewProjectiles_Str0_0;}
					else if ((x == 1) && (y == 0)) {str = Constants.windowPreviewProjectiles_Str1_0;}
					else if ((x == 2) && (y == 0)) {str = Constants.windowPreviewProjectiles_Str2_0;}
					else if ((x == 3) && (y == 0)) {str = Constants.windowPreviewProjectiles_Str3_0;}
					else if ((x == 0) && (y == 1)) {str = Constants.windowPreviewProjectiles_Str0_1;}
					else if ((x == 1) && (y == 1)) {str = Constants.windowPreviewProjectiles_Str1_1;}
					else if ((x == 2) && (y == 1)) {str = Constants.windowPreviewProjectiles_Str2_1;}
					else if ((x == 3) && (y == 1)) {str = Constants.windowPreviewProjectiles_Str3_1;}
					else {str = "";}
					g.drawString(
							str, 
							(int) (resizeRatioWidth() * (Constants.windowPreviewProjectiles_StartingX
									+ (x * (Constants.windowPreviewProjectiles_SizeX 
									+ Constants.windowPreviewProjectiles_BetweenGapX)))),		// X coordinate
							(int) (resizeRatioHeight() * (Constants.windowPreviewProjectiles_StartingY
									+ (y * (Constants.windowPreviewProjectiles_SizeY
									+ Constants.windowPreviewProjectiles_BetweenGapY))) - 5));	// Y coordinate
				} // end for
			} // end for
			// draw the actual animations
			if (animationDelayCounter != Constants.previewAnimation_Delay) { ++animationDelayCounter; }
			else { animationDelayCounter = 0; }
			Image tmpImage = null;
			for (int x = 0; x < Constants.windowPreviewProjectiles_NumOnScreenX; ++x)
			{
				for (int y = 0; y < Constants.windowPreviewProjectiles_NumOnScreenY; ++y)
				{
					if ((x == Constants.windowPreviewProjectiles_MoveLeftX) && (y == Constants.windowPreviewProjectiles_MoveLeftY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_ProjMoveLeft;
							if (animationInt_ProjMoveLeft == graphicPool.preview_ProjMoveLeft.size()) { animationInt_ProjMoveLeft = 0; }
						} // end if
						tmpImage = graphicPool.preview_ProjMoveLeft.get(animationInt_ProjMoveLeft);
					} // end if
					else if ((x == Constants.windowPreviewProjectiles_MoveRightX) && (y == Constants.windowPreviewProjectiles_MoveRightY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_ProjMoveRight;
							if (animationInt_ProjMoveRight == graphicPool.preview_ProjMoveRight.size()) { animationInt_ProjMoveRight = 0; }
						} // end if
						tmpImage = graphicPool.preview_ProjMoveRight.get(animationInt_ProjMoveRight);
					} // end else if
					else if ((x == Constants.windowPreviewProjectiles_MoveUpX) && (y == Constants.windowPreviewProjectiles_MoveUpY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_ProjMoveUp;
							if (animationInt_ProjMoveUp == graphicPool.preview_ProjMoveUp.size()) { animationInt_ProjMoveUp = 0; }
						} // end if
						tmpImage = graphicPool.preview_ProjMoveUp.get(animationInt_ProjMoveUp);
					} // end else if
					else if ((x == Constants.windowPreviewProjectiles_MoveDownX) && (y == Constants.windowPreviewProjectiles_MoveDownY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_ProjMoveDown;
							if (animationInt_ProjMoveDown == graphicPool.preview_ProjMoveDown.size()) { animationInt_ProjMoveDown = 0; }
						} // end if
						tmpImage = graphicPool.preview_ProjMoveDown.get(animationInt_ProjMoveDown);
					} // end else if
					else if ((x == Constants.windowPreviewProjectiles_HitLeftX) && (y == Constants.windowPreviewProjectiles_HitLeftY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_ProjHitLeft;
							if (animationInt_ProjHitLeft == graphicPool.preview_ProjHitLeft.size()) { animationInt_ProjHitLeft = 0; }
						} // end if
						tmpImage = graphicPool.preview_ProjHitLeft.get(animationInt_ProjHitLeft);
					} // end else if
					else if ((x == Constants.windowPreviewProjectiles_HitRightX) && (y == Constants.windowPreviewProjectiles_HitRightY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_ProjHitRight;
							if (animationInt_ProjHitRight == graphicPool.preview_ProjHitRight.size()) { animationInt_ProjHitRight = 0; }
						} // end if
						tmpImage = graphicPool.preview_ProjHitRight.get(animationInt_ProjHitRight);
					} // end else if
					else if ((x == Constants.windowPreviewProjectiles_HitUpX) && (y == Constants.windowPreviewProjectiles_HitUpY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_ProjHitUp;
							if (animationInt_ProjHitUp == graphicPool.preview_ProjHitUp.size()) { animationInt_ProjHitUp = 0; }
						} // end if
						tmpImage = graphicPool.preview_ProjHitUp.get(animationInt_ProjHitUp);
					} // end else if
					else if ((x == Constants.windowPreviewProjectiles_HitDownX) && (y == Constants.windowPreviewProjectiles_HitDownY))
					{
						if (animationDelayCounter == 0)
						{
							++animationInt_ProjHitDown;
							if (animationInt_ProjHitDown == graphicPool.preview_ProjHitDown.size()) { animationInt_ProjHitDown = 0; }
						} // end if
						tmpImage = graphicPool.preview_ProjHitDown.get(animationInt_ProjHitDown);
					} // end else if
					// Finally draw the image
					g.drawImage(
							tmpImage, 																// Image
							(int) (resizeRatioWidth() * (Constants.windowPreviewProjectiles_StartingX
									+ (x * (Constants.windowPreviewProjectiles_SizeX 
									+ Constants.windowPreviewProjectiles_BetweenGapX)))),			// X coordinate
							(int) (resizeRatioHeight() * (Constants.windowPreviewProjectiles_StartingY
									+ (y * (Constants.windowPreviewProjectiles_SizeY
									+ Constants.windowPreviewProjectiles_BetweenGapY)))),			// Y coordinate
							(int) (resizeRatioWidth() * Constants.windowPreviewProjectiles_SizeX), 	// width
							(int) (resizeRatioHeight() * Constants.windowPreviewProjectiles_SizeY), // height
							startingClass); 														// ImageObserver
				} // end for
			} // end for
		} // end if
	}

	private void paintWindowPreviewObjects(Graphics g)
	{
		if (!((data.leftMenuState == Constants.leftMenuState_ListUserObjectsWithExtra) && 
				(data.leftMenuCursor.getIndexVertical() == 0)))
		{
			// draw the borders
			for (int x = 0; x < Constants.windowPreviewObjects_NumOnScreenX; ++x)
			{
				g.drawImage(
						graphicPool.cursor, 													// Image
						(int) (resizeRatioWidth() * (Constants.windowPreviewObjects_StartingX
								+ (x * (Constants.windowPreviewObjects_SizeX 
								+ Constants.windowPreviewObjects_BetweenGapX)))),				// X coordinate
						(int) (resizeRatioHeight() * Constants.windowPreviewObjects_StartingY),	// Y coordinate
						(int) (resizeRatioWidth() * Constants.windowPreviewObjects_SizeX), 		// width
						(int) (resizeRatioHeight() * Constants.windowPreviewObjects_SizeY), 	// height
						startingClass); 														// ImageObserver
			} // end for
			// draw the strings
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.windowPreviewObjects_TextSize)));
			String str = "";
			for (int x = 0; x < Constants.windowPreviewObjects_NumOnScreenX; ++x)
			{
				if (x == 0) {str = Constants.windowPreviewObjects_Str0;}
				else if (x == 1) {str = Constants.windowPreviewObjects_Str1;}
				else {str = "";}
				g.drawString(
						str, 
						(int) (resizeRatioWidth() * (Constants.windowPreviewObjects_StartingX
								+ Constants.windowPreviewObjects_SizeX / 4
								+ (x * (Constants.windowPreviewObjects_SizeX
								+ Constants.windowPreviewObjects_BetweenGapX)))),						// X coordinate
						(int) (resizeRatioHeight() * (Constants.windowPreviewObjects_StartingY - 10)));	// Y coordinate
			} // end for
			// draw the actual animations
			if (animationDelayCounter != Constants.previewAnimation_Delay) { ++animationDelayCounter; }
			else { animationDelayCounter = 0; }
			Image tmpImage = null;
			for (int x = 0; x < Constants.windowPreviewObjects_NumOnScreenX; ++x)
			{
				if (x == Constants.windowPreviewObjects_MainX)
				{
					if (animationDelayCounter == 0)
					{
						++animationInt_ObjectMain;
						if (animationInt_ObjectMain == graphicPool.preview_ObjectMain.size()) { animationInt_ObjectMain = 0; }
					} // end if
					tmpImage = graphicPool.preview_ObjectMain.get(animationInt_ObjectMain);
				} // end if
				else if (x == Constants.windowPreviewObjects_DestroyX)
				{
					if (animationDelayCounter == 0)
					{
						++animationInt_ObjectDestroy;
						if (animationInt_ObjectDestroy == graphicPool.preview_ObjectDestroy.size()) { animationInt_ObjectDestroy = 0; }
					} // end if
					tmpImage = graphicPool.preview_ObjectDestroy.get(animationInt_ObjectDestroy);
				} // end else if
				// Finally draw the image
				g.drawImage(
						tmpImage, 																// Image
						(int) (resizeRatioWidth() * (Constants.windowPreviewObjects_StartingX
								+ (x * (Constants.windowPreviewObjects_SizeX 
								+ Constants.windowPreviewObjects_BetweenGapX)))),				// X coordinate
						(int) (resizeRatioHeight() * Constants.windowPreviewObjects_StartingY),	// Y coordinate
						(int) (resizeRatioWidth() * Constants.windowPreviewObjects_SizeX), 		// width
						(int) (resizeRatioHeight() * Constants.windowPreviewObjects_SizeY), 	// height
						startingClass); 														// ImageObserver
			} // end for
		} // end if
	}

	private void paintWindowPreviewSounds(Graphics g)
	{
		if (!(((data.leftMenuState == Constants.leftMenuState_ListUserMusicWithExtra) 
				|| (data.leftMenuState == Constants.leftMenuState_ListUserSoundsWithExtra)) && 
				(data.leftMenuCursor.getIndexVertical() == 0)))
		{
			// draw the border
			g.drawImage(
					graphicPool.cursor, 													// Image
					(int) (resizeRatioWidth() * Constants.windowPreviewSounds_StartingX),	// X coordinate
					(int) (resizeRatioHeight() * Constants.windowPreviewSounds_StartingY),	// Y coordinate
					(int) (resizeRatioWidth() * Constants.windowPreviewSounds_SizeX), 		// width
					(int) (resizeRatioHeight() * Constants.windowPreviewSounds_SizeY), 		// height
					startingClass); 														// ImageObserver
			// draw the image
			Image tmpImage = graphicPool.previewNA;
			switch (data.leftMenuState)
			{
			case Constants.leftMenuState_ListAllMusic:
			case Constants.leftMenuState_ListAllSounds:
			case Constants.leftMenuState_ListUserMusic:
			case Constants.leftMenuState_ListUserSounds:
				tmpImage = graphicPool.note;
				break;
			case Constants.leftMenuState_ListUserMusicWithExtra:
			case Constants.leftMenuState_ListUserSoundsWithExtra:
				if (data.leftMenuCursor.getIndexVertical() > 0)
				{
					tmpImage = graphicPool.note;
				}
				break;
			case Constants.leftMenuState_EditLevel:
				if (data.currentLevelData.getBackgroundMusic().compareToIgnoreCase("") != 0)
				{
					tmpImage = graphicPool.note;
				}
			default:
				// ignore invalid cases
				break;
			} // end switch
			g.drawImage(
					tmpImage, 													// Image
					(int) (resizeRatioWidth() * (Constants.windowPreviewSounds_StartingX 
							+ Constants.windowPreviewSounds_BorderSize)),		// X coordinate
					(int) (resizeRatioHeight() * (Constants.windowPreviewSounds_StartingY
							+ Constants.windowPreviewSounds_BorderSize)), 		// Y coordinate
					(int) (resizeRatioWidth() * (Constants.windowPreviewSounds_SizeX
							- Constants.windowPreviewSounds_BorderSize * 2)),	// width
					(int) (resizeRatioHeight() * (Constants.windowPreviewSounds_SizeY
							- Constants.windowPreviewSounds_BorderSize * 2)),	// height
					startingClass); 											// ImageObserver
		} // end if
	}

	// paints the level window objects inside the builder application
	private void paintLevelObjects(Graphics g) {
		// for loop for the x coordinate of the MapPieces within the current
		// window
		int xStart = (int) (resizeRatioWidth() * Constants.window_MainImageStartingX);
		int yStart = (int) (resizeRatioHeight() * Constants.window_MainImageStartingY);
		int xPos = 0;
		for (int x = data.mapWindowShiftOffsetX; x < (data.mapWindowShiftOffsetX 
				+ Constants.window_GridSquareXRangeCount); ++x) {
			// for loop for the y coordinates of the MapPieces within the
			// current window
			int yPos = 0;
			for (int y = data.mapWindowShiftOffsetY; y < (data.mapWindowShiftOffsetY 
					+ Constants.window_GridSquareYRangeCount); ++y) {
				// Only attempt to draw the graphics at that MapPiece if
				// at least one layer has a graphic in it
				MapPiece currentPiece = data.currentLevelData.mapVec.get(x).get(y);
				if (currentPiece.isUsed()) {
					// Only show the current layer and all layers behind the
					// current layer
					for (int z = Constants.layer_LevelBackdrop; z <= data.currentLayer; ++z) {
						GraphicObject temp = currentPiece.layer[z];
						if (temp != null)
						{
							g.drawImage(currentPiece.layer[z].getImage(), 			// Image
									(int) (resizeRatioWidth() * Constants.window_GridSquareWidth) 
									* xPos + xStart, 								// X coordinate
									(int) (resizeRatioHeight() * Constants.window_GridSquareHeight) 
									* yPos + yStart, 								// Y coordinate
									(int) (resizeRatioWidth() 
											* Constants.window_GridSquareWidth), 	// width
									(int) (resizeRatioHeight() 
											* Constants.window_GridSquareHeight),	// height
									startingClass); 								// ImageObserver
						} // end if
					} // end for (z)
				} // end if
				++yPos;
			} // end for (y)
			++xPos;
		} // end for (x)
	}

	// paints the cursors inside the builder application
	private void paintBuilderCursors(Graphics g) {
		// top menu cursor
		g.drawImage(
				data.topMenuCursor.getImage(), 				// Image
				(int) (resizeRatioWidth() * (data.topMenuCursor.getPixelOffsetX() 
						- Constants.cursorThickness / 2)),	// X coordinate
				(int) (resizeRatioHeight() * (data.topMenuCursor.getPixelOffsetY() 
						- Constants.cursorThickness / 2)), 	// Y coordinate
				(int) (resizeRatioWidth() * (data.topMenuCursor.getWidth() 
						+ Constants.cursorThickness)), 		// width
				(int) (resizeRatioHeight() * (data.topMenuCursor.getHeight() 
						+ Constants.cursorThickness)), 		// height
				startingClass); 							// ImageObserver
		// left menu cursor
		g.drawImage(
				data.leftMenuCursor.getImage(), 			// Image
				(int) (resizeRatioWidth() * (data.leftMenuCursor.getPixelOffsetX() 
						- Constants.cursorThickness / 2)), 	// X coordinate
				(int) (resizeRatioHeight() * (data.leftMenuCursor.getPixelOffsetY() 
						- Constants.cursorThickness / 2)), 	// Y coordinate
				(int) (resizeRatioWidth() * (data.leftMenuCursor.getWidth() 
						+ Constants.cursorThickness)), 		// width
				(int) (resizeRatioHeight() * (data.leftMenuCursor.getHeight() 
						+ Constants.cursorThickness)), 		// height
				startingClass); 							// ImageObserver
		// level window cursor
		if (data.mainState == Constants.mainState_BuilderWindow
				&& data.windowState == Constants.windowState_LevelMap) {
			g.drawImage(
					data.builderMapCursor.getImage(), 			// Image
					(int) (resizeRatioWidth() * (data.builderMapCursor.getPixelOffsetX() 
							- Constants.cursorThickness / 4)),	// X coordinate
					(int) (resizeRatioHeight() * (data.builderMapCursor.getPixelOffsetY() 
							- Constants.cursorThickness / 4)), 	// Y coordinate
					(int) (resizeRatioWidth() * (data.builderMapCursor.getWidth() 
							+ Constants.cursorThickness / 2)), 	// width
					(int) (resizeRatioHeight() * (data.builderMapCursor.getHeight() 
							+ Constants.cursorThickness / 2)), 	// height
					startingClass); 							// ImageObserver
		} // end if
	}

	// paints the list arrows inside the builder application
	public void paintBuilderArrows(Graphics g) {
		Image tempImage;
		if (data.leftMenuShiftOffset == 0) {
			tempImage = graphicPool.listUpArrowGray;
		} // end if
		else {
			tempImage = graphicPool.listUpArrow;
		} // end else
		g.drawImage(tempImage, 													// Image
				(int) (resizeRatioWidth() * Constants.arrowUpDownStartingX),	// X coordinate
				(int) (resizeRatioHeight() * Constants.arrowUp_StartingY), 		// Y coordinate
				(int) (resizeRatioWidth() * Constants.arrowUpDownWidth), 		// width
				(int) (resizeRatioHeight() * Constants.arrowUpDownHeight), 		// height
				startingClass); 												// ImageObserver
		if (data.leftMenuShiftOffset >= data.leftMenuCursor.getVerticalMax()
				- Constants.leftMenu_ItemMaxShownOnScreen + 1) {
			tempImage = graphicPool.listDownArrowGray;
		} // end if
		else {
			tempImage = graphicPool.listDownArrow;
		} // end else
		g.drawImage(tempImage, 													// Image
				(int) (resizeRatioWidth() * Constants.arrowUpDownStartingX),	// X coordinate
				(int) (resizeRatioHeight() * Constants.arrowDown_StartingY), 	// Y coordinate
				(int) (resizeRatioWidth() * Constants.arrowUpDownWidth), 		// width
				(int) (resizeRatioHeight() * Constants.arrowUpDownHeight), 		// height
				startingClass); 												// ImageObserver
		if (data.topMenuShiftOffset == 0) {
			tempImage = graphicPool.listLeftArrowGray;
		} // end if
		else {
			tempImage = graphicPool.listLeftArrow;
		} // end else
		g.drawImage(
				tempImage, 															// Image
				(int) (resizeRatioWidth() * Constants.arrowLeft_StartingX), 		// X coordinate
				(int) (resizeRatioHeight() * Constants.arrowLeftRightStartingY),	// Y coordinate
				(int) (resizeRatioWidth() * Constants.arrowLeftRightWidth), 		// width
				(int) (resizeRatioHeight() * Constants.arrowLeftRightHeight), 		// height
				startingClass); 													// ImageObserver
		if (data.topMenuShiftOffset >= data.topMenuCursor.getHorizontalMax()
				- Constants.topMenu_ItemMaxShownOnScreen + 1) {
			tempImage = graphicPool.listRightArrowGray;
		} // end if
		else {
			tempImage = graphicPool.listRightArrow;
		} // end else
		g.drawImage(
				tempImage, 															// Image
				(int) (resizeRatioWidth() * Constants.arrowRight_StartingX), 		// X coordinate
				(int) (resizeRatioHeight() * Constants.arrowLeftRightStartingY),	// Y coordinate
				(int) (resizeRatioWidth() * Constants.arrowLeftRightWidth), 		// width
				(int) (resizeRatioHeight() * Constants.arrowLeftRightHeight), 		// height
				startingClass); 													// ImageObserver
	}

	// paints the text descriptors in the top left corner inside the builder application
	private void paintProjectDescriptionTexts(Graphics g) {
		g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.projectDesc_TextSize)));
		// Project name
		g.drawString(
				FileHelper.returnOnlyName(data.pathToProject.substring(0,data.pathToProject.length() - 1)),
				(int) (resizeRatioWidth() * Constants.projectDesc_StartingX),
				(int) (resizeRatioHeight() * Constants.projectDesc_ProjectNameStartingY));
		// Current Level
		g.drawString(
				FileHelper.returnOnlyName(data.currentLevelPath),
				(int) (resizeRatioWidth() * Constants.projectDesc_StartingX),
				(int) (resizeRatioHeight() * Constants.projectDesc_LevelStartingY));
		if (data.topMenuState == Constants.topMenu_IndexPlaceObject/*
				|| data.topMenuState == Constants.topMenu_IndexWinLoseEvents*/)
		{
			// Current layer
			String layerString = "Layer = ";
			switch (data.currentLayer) {
			case Constants.layer_LevelBackdrop:
				layerString += "Backdrop";
				break;
			case Constants.layer_BackgroundObject:
				layerString += "BG";
				break;
			case Constants.layer_MainObject:
				layerString += "Main";
				break;
			case Constants.layer_ForegroundObject:
				layerString += "FG";
				break;
			case Constants.layer_WinLoseEvent:
				layerString += "Event";
				break;
			default:
				// ignore invalid cases
				break;
			} // end switch
			g.drawString(
					layerString,
					(int) (resizeRatioWidth() * Constants.projectDesc_StartingX),
					(int) (resizeRatioHeight() * Constants.projectDesc_LayerStartingY));
		} // end if
		if (data.mainState == Constants.mainState_BuilderWindow)
		{
			// Level window cursor position
			String tempStrX = "X: " + data.builderMapCursor.getIndexHorizontal();
			String tempStrY = "Y: " + data.builderMapCursor.getIndexVertical();
			g.drawString(
					tempStrX,
					(int) (resizeRatioWidth() * Constants.projectDesc_StartingX),
					(int) (resizeRatioHeight() * Constants.projectDesc_CursorStartingY));
			g.drawString(
					tempStrY,
					(int) (resizeRatioWidth() * Constants.projectDesc_CursorYStartingX),
					(int) (resizeRatioHeight() * Constants.projectDesc_CursorStartingY));
		} // end if
	}
private void paintWindowEditObjects(Graphics g)
	{
		switch(data.leftMenuState)
		{
		case Constants.leftMenuState_Traction_Select:
		{
			String vel= String.valueOf(data.getTraction(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Traction",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
			break;
		case Constants.leftMenuState_MaxAcceleration_Select:
		{
			String vel= String.valueOf(data.maxAcceleration);
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Max Acceleration",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
			break;
		case Constants.leftMenuState_MaxHP_Select:
		{
			String vel= String.valueOf(data.getHp(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Max HP",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
			break;
		case Constants.leftMenuState_MaxVelocityUp_Select:
		{
			String vel= String.valueOf(data.getVelUp(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Max Velocity Up",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
			break;
		case Constants.leftMenuState_MaxVelocityDown_Select:
		{
			String vel= String.valueOf(data.getVelDown(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Max Velocity Down",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
			break;
		case Constants.leftMenuState_MaxVelocityLeft_Select:
		{
			String vel= String.valueOf(data.getVelLeft(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"X Direction Max Velocity",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
			break;
		case Constants.leftMenuState_MaxVelocityRight_Select:
		{
			String vel= String.valueOf(data.getVelRight(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Y Direction Max Velocity ",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
			break;
		case Constants.leftMenuState_MaxPowerUp_Select:
		{
			String vel= String.valueOf(data.getPowerUp(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Max Power Up",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
			break;
		case Constants.leftMenuState_MaxPowerDown_Select:
		{
			String vel= String.valueOf(data.getPowerDown(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Max Power Down",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
			break;
		case Constants.leftMenuState_MaxPowerLeft_Select:
		{
			String vel= String.valueOf(data.getPowerLeft(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Max Power Left",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
			break;
		case Constants.leftMenuState_MaxPowerRight_Select:
		{
			String vel= String.valueOf(data.getPowerRight(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Max Power Right",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
			break;
		case Constants.leftMenuState_MaxDefenseUp_Select:
		{
			String vel= String.valueOf(data.getDefUp(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Max Defense Up",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
			break;
		case Constants.leftMenuState_MaxDefenseDown_Select:
		{
			String vel= String.valueOf(data.getDefDown(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Max Defense Down",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
			break;
		case Constants.leftMenuState_MaxDefenseLeft_Select:
		{
			String vel= String.valueOf(data.getDefLeft(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Max Defense Left",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
			break;
		case Constants.leftMenuState_MaxDefenseRight_Select:
		{
			String vel= String.valueOf(data.getDefRight(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Max Defense Right",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
		break;
		case Constants.leftMenuState_VertGravity_Select:
		{
			String vel= String.valueOf(data.setVertG(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Vertical Gravity",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
		break;
		case Constants.leftMenuState_HorzGravity_Select:
		{
			String vel= String.valueOf(data.setHorzG(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Horizontal Gravity",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
		break;
		case Constants.leftMenuState_XTraction_Select:
		{
			String vel= String.valueOf(data.setXTrac(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"X Traction",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
		break;
		case Constants.leftMenuState_YTraction_Select:
		{
			String vel= String.valueOf(data.setYTrac(data.menuSelect));
			g.drawString(vel,
					(int) (resizeRatioWidth() * Constants.editValue_StartingX),
					(int) (resizeRatioHeight() * (Constants.editLives_StartingY)));
			g.setFont(g.getFont().deriveFont((float) (resizeRatioFont() * Constants.edit_TextSize)));
			g.drawString(
					"Y Traction",
					(int) (resizeRatioWidth() * 200),
					(int) (resizeRatioHeight() * Constants.windowPreviewTitle_StartingY));
		}
		break;
		case Constants.leftMenuState_ListBasicMenu:
		{
			
		}
			break;
		case Constants.leftMenuState_ListSimpleMenu:
		{
			int col1 = 170;
			int col1b = 270;
			int col2 = 370;
			int col2b = 500;
			int col3 = 570;
			int col3b = 750;
			int row1 = 150;
			int row2 = 170;
			int row3 = 190;
			int row4 = 210;
			int row5 = 230;
			int row6 = 250;
			int row7 = 270;
			int row8 = 290;
			String traction = String.valueOf(data.getTraction(data.menuSelect));
			String velUp = String.valueOf(data.getVelUp(data.menuSelect));
			String velDown  = String.valueOf(data.getVelDown(data.menuSelect));
			String velLeft  = String.valueOf(data.getVelLeft(data.menuSelect));
			String velRight  = String.valueOf(data.getVelRight(data.menuSelect));
			String hp =   String.valueOf(data.getHp(data.menuSelect));
			String defUp  = String.valueOf(data.getDefUp(data.menuSelect));
			String defDown = String.valueOf(data.getDefDown(data.menuSelect));
			String defLeft = String.valueOf(data.getDefLeft(data.menuSelect));
			String defRight = String.valueOf(data.getDefRight(data.menuSelect));
			String PowerUp  = String.valueOf(data.getPowerUp(data.menuSelect));
			String PowerDown = String.valueOf(data.getPowerDown(data.menuSelect));
			String PowerLeft = String.valueOf(data.getPowerLeft(data.menuSelect));
			String PowerRight = String.valueOf(data.getPowerRight(data.menuSelect));
			//String maxAcc =  String.valueOf(xmlManager.loadPlayerCharacteristics(data.selectedEditPath,Constants.leftMenuState_MaxAcceleration_Select));
			String vertGravity = String.valueOf(data.setVertG(data.menuSelect));
			String horzGravity = String.valueOf(data.setHorzG(data.menuSelect));
			String xTraction = String.valueOf(data.setXTrac(data.menuSelect));
			String yTraction = String.valueOf(data.setYTrac(data.menuSelect));
			g.drawString("Traction: ",
					(int) (resizeRatioWidth() * col1),
					(int) (resizeRatioHeight() * row1));
			g.drawString(traction,
					(int) (resizeRatioWidth() * col1b),
					(int) (resizeRatioHeight() * row1));
			g.drawString("Max HP: ",
					(int) (resizeRatioWidth() * col1),
					(int) (resizeRatioHeight() * row2));
			g.drawString(hp,
					(int) (resizeRatioWidth() * col1b),
					(int) (resizeRatioHeight() * row2));
			g.drawString("Max Velocity Up: ",
					(int) (resizeRatioWidth() * col1),
					(int) (resizeRatioHeight() * row3));
			g.drawString(velUp,
					(int) (resizeRatioWidth() * col1b),
					(int) (resizeRatioHeight() * row3));
			g.drawString("Max Velocity Down: ",
					(int) (resizeRatioWidth() * col1),
					(int) (resizeRatioHeight() * row4));
			g.drawString(velDown,
					(int) (resizeRatioWidth() * col1b),
					(int) (resizeRatioHeight() * row4));
			g.drawString("Max Velocity Left: ",
					(int) (resizeRatioWidth() * col1),
					(int) (resizeRatioHeight() * row5));
			g.drawString(velLeft,
					(int) (resizeRatioWidth() * col1b),
					(int) (resizeRatioHeight() * row5));
			g.drawString("Max Velocity Right: ",
					(int) (resizeRatioWidth() * col1),
					(int) (resizeRatioHeight() * row6));
			g.drawString(velRight,
					(int) (resizeRatioWidth() * col1b),
					(int) (resizeRatioHeight() * row6));
			g.drawString("Max Power Up: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row1));
			g.drawString(PowerUp,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row1));
			g.drawString("Max Power Down: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row2));
			g.drawString(PowerDown,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row2));
			g.drawString("Max Power Left: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row3));
			g.drawString(PowerLeft,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row3));
			g.drawString("Max Power Right: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row4));
			g.drawString(PowerRight,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row4));
			g.drawString("Max Defense Up: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row5));
			g.drawString(defUp,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row5));
			g.drawString("Max Defense Down: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row6));
			g.drawString(defDown,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row6));
			g.drawString("Max Defense Left: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row7));
			g.drawString(defLeft,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row7));
			g.drawString("Max Defense Right: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row8));
			g.drawString(defRight,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row8));
			g.drawString("Is Affected by vertical Gravity: ",
					(int) (resizeRatioWidth() * col3),
					(int) (resizeRatioHeight() * row1));
			g.drawString(vertGravity,
					(int) (resizeRatioWidth() * col3b),
					(int) (resizeRatioHeight() * row1));
			g.drawString("Is Affected by horizontal Gravity: ",
					(int) (resizeRatioWidth() * col3),
					(int) (resizeRatioHeight() * row2));
			g.drawString(horzGravity,
					(int) (resizeRatioWidth() * col3b),
					(int) (resizeRatioHeight() * row2));
			g.drawString("Is Affected by x direction traction: ",
					(int) (resizeRatioWidth() * col3),
					(int) (resizeRatioHeight() * row3));
			g.drawString(xTraction,
					(int) (resizeRatioWidth() * col3b),
					(int) (resizeRatioHeight() * row3));
			g.drawString("Is Affected by y direction traction: ",
					(int) (resizeRatioWidth() * col3),
					(int) (resizeRatioHeight() * row4));
			g.drawString(yTraction,
					(int) (resizeRatioWidth() * col3b),
					(int) (resizeRatioHeight() * row4));
			
		
			break;
		}			
		case Constants.leftMenuState_ListAdvancedMenu:
		{
			int col1 = 170;
			int col1b = 275;
			int col2 = 370;
			int col2b = 500;
			int col3 = 570;
			int col3b = 750;
			int row1 = 150;
			int row2 = 170;
			int row3 = 190;
			int row4 = 210;
			int row5 = 230;
			int row6 = 250;
			int row7 = 270;
			int row8 = 290;
			
			String traction = String.valueOf(data.getTraction(data.menuSelect));
			String velUp = String.valueOf(data.getVelUp(data.menuSelect));
			String velDown  = String.valueOf(data.getVelDown(data.menuSelect));
			String velLeft  = String.valueOf(data.getVelLeft(data.menuSelect));
			String velRight  = String.valueOf(data.getVelRight(data.menuSelect));
			String hp =   String.valueOf(data.getHp(data.menuSelect));
			String defUp  = String.valueOf(data.getDefUp(data.menuSelect));
			String defDown = String.valueOf(data.getDefDown(data.menuSelect));
			String defLeft = String.valueOf(data.getDefLeft(data.menuSelect));
			String defRight = String.valueOf(data.getDefRight(data.menuSelect));
			String PowerUp  = String.valueOf(data.getPowerUp(data.menuSelect));
			String PowerDown = String.valueOf(data.getPowerDown(data.menuSelect));
			String PowerLeft = String.valueOf(data.getPowerLeft(data.menuSelect));
			String PowerRight = String.valueOf(data.getPowerRight(data.menuSelect));
			//String maxAcc =  String.valueOf(xmlManager.loadPlayerCharacteristics(data.selectedEditPath,Constants.leftMenuState_MaxAcceleration_Select));
			String vertGravity = String.valueOf(data.setVertG(data.menuSelect));
			String horzGravity = String.valueOf(data.setHorzG(data.menuSelect));
			String xTraction = String.valueOf(data.setXTrac(data.menuSelect));
			String yTraction = String.valueOf(data.setYTrac(data.menuSelect));
			g.drawString("Traction: ",
					(int) (resizeRatioWidth() * col1),
					(int) (resizeRatioHeight() * row1));
			g.drawString(traction,
					(int) (resizeRatioWidth() * col1b),
					(int) (resizeRatioHeight() * row1));
			g.drawString("Max HP: ",
					(int) (resizeRatioWidth() * col1),
					(int) (resizeRatioHeight() * row2));
			g.drawString(hp,
					(int) (resizeRatioWidth() * col1b),
					(int) (resizeRatioHeight() * row2));
			g.drawString("Max Velocity Up: ",
					(int) (resizeRatioWidth() * col1),
					(int) (resizeRatioHeight() * row3));
			g.drawString(velUp,
					(int) (resizeRatioWidth() * col1b),
					(int) (resizeRatioHeight() * row3));
			g.drawString("Max Velocity Down: ",
					(int) (resizeRatioWidth() * col1),
					(int) (resizeRatioHeight() * row4));
			g.drawString(velDown,
					(int) (resizeRatioWidth() * col1b),
					(int) (resizeRatioHeight() * row4));
			g.drawString("Max Velocity Left: ",
					(int) (resizeRatioWidth() * col1),
					(int) (resizeRatioHeight() * row5));
			g.drawString(velLeft,
					(int) (resizeRatioWidth() * col1b),
					(int) (resizeRatioHeight() * row5));
			g.drawString("Max Velocity Right: ",
					(int) (resizeRatioWidth() * col1),
					(int) (resizeRatioHeight() * row6));
			g.drawString(velRight,
					(int) (resizeRatioWidth() * col1b),
					(int) (resizeRatioHeight() * row6));
			g.drawString("Max Jump Power: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row1));
			g.drawString(PowerUp,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row1));
		/*	g.drawString("Max Power Down: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row2));
			g.drawString(PowerDown,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row2));
			g.drawString("Max Power Left: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row3));
			g.drawString(PowerLeft,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row3));
			g.drawString("Max Power Right: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row4));
			g.drawString(PowerRight,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row4));*/
			g.drawString("Max Defense Up: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row2));
			g.drawString(defUp,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row2));
			g.drawString("Max Defense Down: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row3));
			g.drawString(defDown,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row3));
			g.drawString("Max Defense Left: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row4));
			g.drawString(defLeft,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row4));
			g.drawString("Max Defense Right: ",
					(int) (resizeRatioWidth() * col2),
					(int) (resizeRatioHeight() * row5));
			g.drawString(defRight,
					(int) (resizeRatioWidth() * col2b),
					(int) (resizeRatioHeight() * row5));
			g.drawString("Is Affected by vertical Gravity: ",
					(int) (resizeRatioWidth() * col3),
					(int) (resizeRatioHeight() * row1));
			g.drawString(vertGravity,
					(int) (resizeRatioWidth() * col3b),
					(int) (resizeRatioHeight() * row1));
			g.drawString("Is Affected by horizontal Gravity: ",
					(int) (resizeRatioWidth() * col3),
					(int) (resizeRatioHeight() * row2));
			g.drawString(horzGravity,
					(int) (resizeRatioWidth() * col3b),
					(int) (resizeRatioHeight() * row2));
			g.drawString("Is Affected by x direction traction: ",
					(int) (resizeRatioWidth() * col3),
					(int) (resizeRatioHeight() * row3));
			g.drawString(xTraction,
					(int) (resizeRatioWidth() * col3b),
					(int) (resizeRatioHeight() * row3));
			g.drawString("Is Affected by y direction traction: ",
					(int) (resizeRatioWidth() * col3),
					(int) (resizeRatioHeight() * row4));
			g.drawString(yTraction,
					(int) (resizeRatioWidth() * col3b),
					(int) (resizeRatioHeight() * row4));
			
		}
			break;
			default:
				break;
		}
		
	}
}