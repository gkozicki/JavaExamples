package gamemaker;

import java.awt.event.KeyEvent;

import testMode.TestMain;

import java.util.Vector;

public class EventHandler {

// =================================================
//  CLASS VARIABLES
// =================================================

	private XmlManager xmlManager;
	private CPaint cPaint;
	private GraphicPool graphicPool;
	private AudioPool audioPool;
	private Data data;
	private TestMain testMain;

// =================================================
//  PUBLIC METHODS
// =================================================

	// These methods set the references for the appropriate variables
	// NOTE: Some of these may not currently be used or needed
	public void setCPaint(CPaint CP){cPaint = CP;}
	public void setXmlManager(XmlManager XM){xmlManager = XM;}
	public void setGraphicPool(GraphicPool GP){graphicPool = GP;}
	public void setData(Data D){data = D;}
	public void setTestMain(TestMain TM){testMain = TM;}
	public void setAudioPool(AudioPool aPool) {	audioPool = aPool;}

	// attempts to move a cursor left
	public void moveCursorLeft(Cursor cursor)
	{
		int result = cursor.moveLeft();
		if (result == Constants.cursorMoveReturn_ShiftBeginning){
			if (cursor.equals(data.topMenuCursor))
			{
				--data.topMenuShiftOffset;
			} // end if
			else if (cursor.equals(data.builderMapCursor)){
				--data.mapWindowShiftOffsetX;
			} // end else if
		} // end if
		if((result == Constants.cursorMoveReturn_AtBeginning) &&
			    (data.leftMenuState == Constants.leftMenuState_ListAllMusic) ||
				(data.leftMenuState == Constants.leftMenuState_ListUserMusicWithExtra) ||
				(data.leftMenuState == Constants.leftMenuState_ListAllSounds) ||
				(data.leftMenuState == Constants.leftMenuState_ListUserSoundsWithExtra)) {
			audioPool.activatePreview();
		} else if(data.mainState != Constants.mainState_SplashMenuLoad) {
			audioPool.activateMenu();
		}
		data.updateStates();
	}

	// attempts to move a cursor right
	public void moveCursorRight(Cursor cursor)
	{
		int result = cursor.moveRight();
		if (result == Constants.cursorMoveReturn_ShiftEnd){
			if (cursor.equals(data.topMenuCursor))
			{
				++data.topMenuShiftOffset;
			} // end if
			else if (cursor.equals(data.builderMapCursor)){
				++data.mapWindowShiftOffsetX;
			} // end else if
		} // end if
		else if (result == Constants.cursorMoveReturn_AtEnd &&
				cursor.equals(data.builderMapCursor))
		{
			// add a new column to the level
			xmlManager.addNewLevelCol();
		} // end else if
		else if(data.mainState != Constants.mainState_SplashMenuLoad) {
			audioPool.activateMenu();
		}
		data.updateStates();
	}

	// attempts to move a cursor up
	public void moveCursorUp(Cursor cursor)
	{
		int result = cursor.moveUp();
		if (result == Constants.cursorMoveReturn_ShiftBeginning){
			if (cursor.equals(data.leftMenuCursor))
			{
				--data.leftMenuShiftOffset;
			} // end if
			else if (cursor.equals(data.builderMapCursor)){
				--data.mapWindowShiftOffsetY;
			} // end else if
			else if (cursor.equals(data.splashLoadCursor)){
				--data.splashLoadShiftOffset;
			} // end else if
		} // end if
		if (cursor.equals(data.leftMenuCursor) && 
				(data.leftMenuState == Constants.leftMenuState_ListAllPlayers) ||
				//(data.leftMenuState == Constants.leftMenuState_ListAllNPCs) ||
				(data.leftMenuState == Constants.leftMenuState_ListUserPlayersWithExtra) ||
				(data.leftMenuState == Constants.leftMenuState_ListUserPlayers) ||
				//(data.leftMenuState == Constants.leftMenuState_ListUserNPCsWithExtra) ||
				(data.leftMenuState == Constants.leftMenuState_ListAllObjects) || 
				(data.leftMenuState == Constants.leftMenuState_ListUserObjectsWithExtra)
				/* ||
				(data.leftMenuState == Constants.leftMenuState_ListAllProjectiles) ||
				(data.leftMenuState == Constants.leftMenuState_ListUserProjectilesWithExtra)
				*/)

		{
			graphicPool.loadPreviewAnimations();
		}
		if (cursor.equals(data.leftMenuCursor) && 
				(data.leftMenuState == Constants.leftMenuState_ListAllMusic) ||
				(data.leftMenuState == Constants.leftMenuState_ListUserMusicWithExtra) ||
				(data.leftMenuState == Constants.leftMenuState_ListAllSounds) ||
				(data.leftMenuState == Constants.leftMenuState_ListUserSoundsWithExtra))
		{
			audioPool.activatePreview();
		}
		checkForResourceManagerItemState();
		checkIfLayerNeedsToChange();
		data.updateStates();
	}

	// attempts to move a cursor down
	public void moveCursorDown(Cursor cursor)
	{
		int result = cursor.moveDown();
		if (result == Constants.cursorMoveReturn_ShiftEnd){
			if (cursor.equals(data.leftMenuCursor))
			{
				++data.leftMenuShiftOffset;
			} // end if
			else if (cursor.equals(data.builderMapCursor)){
				++data.mapWindowShiftOffsetY;
			} // end else if
			else if (cursor.equals(data.splashLoadCursor)){
				++data.splashLoadShiftOffset;
			} // end else if
		} // end if
		else if (result == Constants.cursorMoveReturn_AtEnd &&
				cursor.equals(data.builderMapCursor))
		{
			// add a new row to the level
			xmlManager.addNewLevelRow();
		} // end else if
		if (cursor.equals(data.leftMenuCursor) && 
				(data.leftMenuState == Constants.leftMenuState_ListAllPlayers) ||
				//(data.leftMenuState == Constants.leftMenuState_ListAllNPCs) ||
				(data.leftMenuState == Constants.leftMenuState_ListUserPlayersWithExtra) ||
				(data.leftMenuState == Constants.leftMenuState_ListUserPlayers) ||
				//(data.leftMenuState == Constants.leftMenuState_ListUserNPCsWithExtra) ||
				(data.leftMenuState == Constants.leftMenuState_ListAllObjects) || 
				(data.leftMenuState == Constants.leftMenuState_ListUserObjectsWithExtra) 
				/* ||
				(data.leftMenuState == Constants.leftMenuState_ListAllProjectiles) ||
				(data.leftMenuState == Constants.leftMenuState_ListUserProjectilesWithExtra)
				*/
				)
		{
			graphicPool.loadPreviewAnimations();
		}
		if (cursor.equals(data.leftMenuCursor) && 
				(data.leftMenuState == Constants.leftMenuState_ListAllMusic) ||
				(data.leftMenuState == Constants.leftMenuState_ListUserMusicWithExtra) ||
				(data.leftMenuState == Constants.leftMenuState_ListAllSounds) ||
				(data.leftMenuState == Constants.leftMenuState_ListUserSoundsWithExtra))
		{
			audioPool.activatePreview();
		}

		checkForResourceManagerItemState();
		checkIfLayerNeedsToChange();
		data.updateStates();
	}

	// called when user selects a top menu item
	// redirects action to more specific methods
	public void selectMenu()
	{
		switch (data.topMenuCursor.getIndexHorizontal())
		{
		case Constants.topMenu_IndexManageResources:
			selectMenu_ManageResources();
			break;
		case Constants.topMenu_IndexEditObjects:
			selectMenu_EditObjects();
			break;
		case Constants.topMenu_IndexManagePlayers:
			selectMenu_ManagePlayers();
			break;
		case Constants.topMenu_IndexLevelSelect:
			selectMenu_LevelSelect();
			break;
		case Constants.topMenu_IndexEditLevel:
			selectMenu_EditLevel();
			break;
		case Constants.topMenu_IndexPlaceObject:
			selectMenu_PlaceObject();
			break;
/*		case Constants.topMenu_IndexWinLoseEvents:
			selectMenu_WinLoseEvents();
			break;*/
		case Constants.topMenu_IndexTestLevel:
			selectMenu_TestLevel();
			break;
		default:
			// ignore invalid states
			break;
		} // end switch
	}

	// navigate back to the main splash menu
	public void returnToSplashMenu()
	{
		xmlManager.optimizeLevelSize();
		data.mainState = Constants.mainState_SplashMenu;
		data.splashLoadShiftOffset = 0;
		// set the cursor position to the beginning
		while (data.splashLoadCursor.moveLeft() != Constants.cursorMoveReturn_AtBeginning);
	}

	// returns back to the next parent menu
	// redirects action to more specific methods
	public void returnMenu()
	{
		switch (data.topMenuCursor.getIndexHorizontal())
		{
		case Constants.topMenu_IndexManageResources:
			returnMenu_ManageResources();
			break;
		case Constants.topMenu_IndexManagePlayers:
			returnMenu_ManagePlayers();
			break;
		case Constants.topMenu_IndexLevelSelect:
			returnMenu_LevelSelect();
			break;
		case Constants.topMenu_IndexEditLevel:
			returnMenu_EditLevel();
			break;
		case Constants.topMenu_IndexEditObjects:
			returnMenu_EditObject();
			audioPool.activateMenu();
			break;
		case Constants.topMenu_IndexPlaceObject:
			returnMenu_PlaceObject();
			break;
		case Constants.topMenu_IndexTestLevel:
			returnMenu_TestLevel();
			break;
		default:
			// ignore invalid states
			break;
		} // end switch
	}

	// places an object on the level map
	public void placeObjectOnMap()
	{
		int xPos = data.builderMapCursor.getIndexHorizontal();
		int yPos = data.builderMapCursor.getIndexVertical();
		if (!data.currentLevelData.mapVec.get(xPos).get(yPos).isUsed())
		{
			xmlManager.addNewMapItemToLevelData(
					xPos, 
					yPos);
		} // end if
		else
		{
			xmlManager.addMapItemToExistingLevelData(
					xPos,
					yPos);
		} // end else
	}

	// erases on object on the level map
	public void eraseObjectOnMap()
	{
		int xPos = data.builderMapCursor.getIndexHorizontal();
		int yPos = data.builderMapCursor.getIndexVertical();
		if (data.currentLevelData.mapVec.get(xPos).get(yPos).isUsed())
		{
			xmlManager.eraseMapItemFromLevelData(
					xPos, 
					yPos);
		} // end if
	}

// =================================================
//  PRIVATE METHODS
// =================================================

	// Currently this is not being used
	private void updateCursors()
	{
		updateCursor(data.topMenuCursor);
		updateCursor(data.leftMenuCursor);
		updateCursor(data.builderMapCursor);
	}

	// Currently this is not being used
	private void updateCursor(Cursor cursor) 
	{
		if ((cursor.getHorizontalMoveButtonHeld() != Constants.horizontalMoveButtonHeld_None 
				|| cursor.getVerticalMoveButtonHeld() != Constants.verticalMoveButtonHeld_None))
		{
			if (cursor.getSpeedCounter() < Constants.cursorSpeedDelay)
			{
				cursor.incrementSpeedCounter();
			} // end if
			else
			{
				cursor.resetSpeedCounter();
				if (cursor.getHorizontalMoveButtonHeld() == Constants.horizontalMoveButtonHeld_Left)
				{
					moveCursorLeft(cursor);
				} // end if
				else if (cursor.getHorizontalMoveButtonHeld() == Constants.horizontalMoveButtonHeld_Right)
				{
					moveCursorRight(cursor);
				} // end else if
				if (cursor.getVerticalMoveButtonHeld() == Constants.verticalMoveButtonHeld_Up)
				{
					moveCursorUp(cursor);
				} // end if
				else if (cursor.getVerticalMoveButtonHeld() == Constants.verticalMoveButtonHeld_Down)
				{
					moveCursorDown(cursor);
				} // end else if
			} // end else
		} // end if
	}

	// because specific menu items correspond to specific layers, we need to 
	// check and set the current layer
	private void checkIfLayerNeedsToChange()
	{
		if (data.topMenuState == Constants.topMenu_IndexPlaceObject)
		{
			if (data.leftMenuState == Constants.leftMenuState_PlaceObjects)
			{
				if (data.leftMenuCursor.getIndexVertical() == Constants.leftMenuList_PlaceObjectsTerrains)
				{
					data.currentLayer = Constants.layer_ForegroundObject;
				} // end if
				else
				{
					data.currentLayer = Constants.layer_MainObject;
				} // end else
			} // end if
			else if (data.leftMenuState == Constants.leftMenuState_Layers)
			{
				if (data.leftMenuCursor.getIndexVertical() == Constants.leftMenuList_PlaceObjectLayerBackground)
				{
					data.currentLayer = Constants.layer_BackgroundObject;
				} // end if
				else if (data.leftMenuCursor.getIndexVertical() == Constants.leftMenuList_PlaceObjectLayerMain)
				{
					data.currentLayer = Constants.layer_MainObject;
				} // end else if
				else if (data.leftMenuCursor.getIndexVertical() == Constants.leftMenuList_PlaceObjectLayerForeground)
				{
					data.currentLayer = Constants.layer_ForegroundObject;
				} // end else if
			} // end else if
		} // end if
	}

	// redirected method for handling the selection action while the top menu is on Manage Resources
	private void selectMenu_ManageResources()
	{
		switch (data.leftMenuState)
		{
		case Constants.leftMenuState_ManageResources:
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case Constants.leftMenuList_ManageResourcesLevelBackdrops:
				data.resetLeftMenu(true);
				data.leftMenuState = Constants.leftMenuState_ListUserLevelBackdropsWithExtra;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserLevelBackdropsExtra.size(), true);
				break;
			case Constants.leftMenuList_ManageResourcesPlayers:
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListUserPlayersWithExtra;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserPlayersExtra.size(), true);
				graphicPool.loadPreviewAnimations();
				break;
			/*
			case Constants.leftMenuList_ManageResourcesNPCs:
				data.resetLeftMenu(false);
				//data.leftMenuState = Constants.leftMenuState_ListUserNPCsWithExtra;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserNPCsExtra.size(), true);
				graphicPool.loadPreviewAnimations();
				break;
			*/
			case Constants.leftMenuList_ManageResourcesObjects:
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListUserObjectsWithExtra;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserObjectsExtra.size(), true);
				graphicPool.loadPreviewAnimations();
				break;
			case Constants.leftMenuList_ManageResourcesTerrains:
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListUserTerrainsWithExtra;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserTerrainsExtra.size(), true);
				break;
			/*
			case Constants.leftMenuList_ManageResourcesProjectiles:
				data.resetLeftMenu(false);
				//data.leftMenuState = Constants.leftMenuState_ListUserProjectilesWithExtra;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserProjectilesExtra.size(), true);
				graphicPool.loadPreviewAnimations();
				break;
			*/
			case Constants.leftMenuList_ManageResourcesMusic:
				data.resetLeftMenu(true);
				data.leftMenuState = Constants.leftMenuState_ListUserMusicWithExtra;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserMusicExtra.size(), true);
				break;
			case Constants.leftMenuList_ManageResourcesSounds:
				data.resetLeftMenu(true);
				data.leftMenuState = Constants.leftMenuState_ListUserSoundsWithExtra;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserSoundsExtra.size(), true);
				break;
			default:
				// ignore invalid cases
				break;
			} // end switch
			break;
		case Constants.leftMenuState_ListAllLevelBackdrops:
			if (!doesThisAlreadyExist(graphicPool.leftMenu_UserLevelBackdropsExtra, graphicPool.leftMenu_LevelBackdrops[data.leftMenuCursor.getIndexVertical()].getXmlPath()))
			{
				xmlManager.saveUserLevelBackgroundGraphicToVector(graphicPool.leftMenu_LevelBackdrops[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				data.resourceManager_ItemState = Constants.windowPreviewBottomText_StateNewAdded;
			} // end if
			break;
		case Constants.leftMenuState_ListUserLevelBackdropsWithExtra:
			if (data.leftMenuCursor.getIndexVertical() == 0)
			{
				data.resetLeftMenu(true);
				data.leftMenuState = Constants.leftMenuState_ListAllLevelBackdrops;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_LevelBackdrops.length, true);
				checkForResourceManagerItemState();
			} // end if
			break;
		case Constants.leftMenuState_ListAllPlayers:
			if (!doesThisAlreadyExist(graphicPool.leftMenu_UserPlayersExtra, graphicPool.leftMenu_Players[data.leftMenuCursor.getIndexVertical()].getXmlPath()))
			{
				xmlManager.saveUserPlayerGraphicToVector(graphicPool.leftMenu_Players[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				data.resourceManager_ItemState = Constants.windowPreviewBottomText_StateNewAdded;
			} // end if
			break;
		case Constants.leftMenuState_ListUserPlayersWithExtra:
			if (data.leftMenuCursor.getIndexVertical() == 0)
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListAllPlayers;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_Players.length, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
			} // end if
			break;
		/*
		case Constants.leftMenuState_ListAllNPCs:
			if (!doesThisAlreadyExist(graphicPool.leftMenu_UserNPCsExtra, graphicPool.leftMenu_NPCs[data.leftMenuCursor.getIndexVertical()].getXmlPath()))
			{
				xmlManager.saveUserNPCGraphicToVector(graphicPool.leftMenu_NPCs[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				data.resourceManager_ItemState = Constants.windowPreviewBottomText_StateNewAdded;
			} // end if
			break;
		case Constants.leftMenuState_ListUserNPCsWithExtra:
			if (data.leftMenuCursor.getIndexVertical() == 0)
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListAllNPCs;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_NPCs.length, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
			} // end if
			break;
		*/
		case Constants.leftMenuState_ListAllObjects:
			if (!doesThisAlreadyExist(graphicPool.leftMenu_UserObjectsExtra, graphicPool.leftMenu_Objects[data.leftMenuCursor.getIndexVertical()].getXmlPath()))
			{
				xmlManager.saveUserObjectGraphicToVector(graphicPool.leftMenu_Objects[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				data.resourceManager_ItemState = Constants.windowPreviewBottomText_StateNewAdded;
			} // end if
			break;
		case Constants.leftMenuState_ListUserObjectsWithExtra:
			if (data.leftMenuCursor.getIndexVertical() == 0)
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListAllObjects;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_Objects.length, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
			} // end if
			break;
		case Constants.leftMenuState_ListAllTerrains:
			if (!doesThisAlreadyExist(graphicPool.leftMenu_UserTerrainsExtra, graphicPool.leftMenu_Terrains[data.leftMenuCursor.getIndexVertical()].getXmlPath()))
			{
				xmlManager.saveUserTerrainGraphicToVector(graphicPool.leftMenu_Terrains[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				data.resourceManager_ItemState = Constants.windowPreviewBottomText_StateNewAdded;
			} // end if
			break;
		case Constants.leftMenuState_ListUserTerrainsWithExtra:
			if (data.leftMenuCursor.getIndexVertical() == 0)
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListAllTerrains;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_Terrains.length, true);
				checkForResourceManagerItemState();
			} // end if
			break;
		/*case Constants.leftMenuState_ListAllProjectiles:
			if (!doesThisAlreadyExist(graphicPool.leftMenu_UserProjectilesExtra, graphicPool.leftMenu_Projectiles[data.leftMenuCursor.getIndexVertical()].getXmlPath()))
			{
				xmlManager.saveUserProjectileGraphicToVector(graphicPool.leftMenu_Projectiles[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				data.resourceManager_ItemState = Constants.windowPreviewBottomText_StateNewAdded;
			} // end if
			break;
		case Constants.leftMenuState_ListUserProjectilesWithExtra:
			if (data.leftMenuCursor.getIndexVertical() == 0)
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListAllProjectiles;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_Projectiles.length, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
			} // end if
			break;
		*/
		case Constants.leftMenuState_ListAllMusic:
			if (!doesThisAlreadyExist(graphicPool.leftMenu_UserMusicExtra, graphicPool.leftMenu_Music[data.leftMenuCursor.getIndexVertical()].getXmlPath()))
			{
				xmlManager.saveUserMusicGraphicToVector(graphicPool.leftMenu_Music[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				data.resourceManager_ItemState = Constants.windowPreviewBottomText_StateNewAdded;
				xmlManager.reloadAudioData();
			} // end if
			break;
		case Constants.leftMenuState_ListUserMusicWithExtra:
			if (data.leftMenuCursor.getIndexVertical() == 0)
			{
				data.resetLeftMenu(true);
				data.leftMenuState = Constants.leftMenuState_ListAllMusic;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_Music.length, true);
				checkForResourceManagerItemState();
			} else {// end if
				audioPool.setCurrLooper(audioPool.leftMenu_User_BGMusic.get(data.leftMenuCursor.getIndexVertical()-1));
				audioPool.currLooper.loopAudio();
			}
			break;
		case Constants.leftMenuState_ListAllSounds:
			if (!doesThisAlreadyExist(graphicPool.leftMenu_UserSoundsExtra, graphicPool.leftMenu_Sounds[data.leftMenuCursor.getIndexVertical()].getXmlPath()))
			{
				xmlManager.saveUserSoundGraphicToVector(graphicPool.leftMenu_Sounds[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				data.resourceManager_ItemState = Constants.windowPreviewBottomText_StateNewAdded;
				xmlManager.reloadAudioData();
			} // end if
			break;
		case Constants.leftMenuState_ListUserSoundsWithExtra:
			if (data.leftMenuCursor.getIndexVertical() == 0)
			{
				data.resetLeftMenu(true);
				data.leftMenuState = Constants.leftMenuState_ListAllSounds;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_Sounds.length, true);
				checkForResourceManagerItemState();
			} else {// end if
				audioPool.stopAllAudio();
				audioPool.leftMenu_User_SoundFX.get(data.leftMenuCursor.getIndexVertical()-1).playAudio();
			}
			break;
		default:
			// ignore invalid cases
			break;
		} // end switch
	}

	// redirected method for handling the selection action while the top menu is on Edit Objects
	private void selectMenu_EditObjects()
	{
		data.setValues();
switch (data.leftMenuState)
		{
		case Constants.leftMenuState_EditObjects:
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case Constants.leftMenuList_EditObjectsPlayers:
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListUserPlayers;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserPlayers.size(), true);
				graphicPool.loadPreviewAnimations();
				break;
			/*case Constants.leftMenuList_EditObjectsNPCs:
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListUserNPCs;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserNPCs.size(), true);
				graphicPool.loadPreviewAnimations();
				break;*/
			case Constants.leftMenuList_EditObjectsObjects:
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListUserObjects;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserObjects.size(), true);
				graphicPool.loadPreviewAnimations();
				break;
			case Constants.leftMenuList_EditObjectsTerrains:
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListUserTerrains;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserObjects.size(), true);
				graphicPool.loadPreviewAnimations();
				break;
			/*case Constants.leftMenuList_EditObjectsProjectiles:
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListUserProjectiles;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserObjects.size(), true);
				graphicPool.loadPreviewAnimations();
				break;*/
			default:
				// ignore invalid cases
				break;
			} // end switch
			break;
		case Constants.leftMenuState_ListUserPlayers:
			//if (data.leftMenuCursor.getIndexVertical() == 0)
			{
				data.menuSelect =0;
				//data.selectedEditPath = graphicPool.leftMenu_UserNPCs.get(data.leftMenuCursor.getIndexVertical()).getXmlPath();
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListAdvancedMenu; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsAdvanced.length, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
			break;
		/*case Constants.leftMenuState_ListAllNPCs:
			if (!doesThisAlreadyExist(graphicPool.leftMenu_UserNPCsExtra, graphicPool.leftMenu_NPCs[data.leftMenuCursor.getIndexVertical()].getXmlPath()))
			{
				xmlManager.saveUserNPCGraphicToVector(graphicPool.leftMenu_NPCs[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				data.resourceManager_ItemState = Constants.windowPreviewBottomText_StateNewAdded;
			} // end if
			break;*/
		/*case Constants.leftMenuState_ListUserNPCs:
			if (data.leftMenuCursor.getIndexVertical() == 0)
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListAdvancedMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsAdvanced.length, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
			} // end if
			break;*/
		/*case Constants.leftMenuState_ListAllObjects:
			if (!doesThisAlreadyExist(graphicPool.leftMenu_UserObjectsExtra, graphicPool.leftMenu_Objects[data.leftMenuCursor.getIndexVertical()].getXmlPath()))
			{
				xmlManager.saveUserObjectGraphicToVector(graphicPool.leftMenu_Objects[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				data.resourceManager_ItemState = Constants.windowPreviewBottomText_StateNewAdded;
			} // end if
			break;*/
		case Constants.leftMenuState_ListUserObjects:
			//if (data.leftMenuCursor.getIndexVertical() == 0)
			{
				data.selectedEditPath = graphicPool.leftMenu_UserObjects.get(data.leftMenuCursor.getIndexVertical()).getXmlPath();

				data.menuSelect = 1;
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListSimpleMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsSimple.length, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
			} // end if
			break;
		case Constants.leftMenuState_ListUserTerrains:
			//if (data.leftMenuCursor.getIndexVertical() == 0)
			{
				data.selectedEditPath = graphicPool.leftMenu_UserTerrains.get(data.leftMenuCursor.getIndexVertical()).getXmlPath();
				data.menuSelect = 2;
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListBasicMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsBasic.length, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
			} // end if
			break;
		/*case Constants.leftMenuState_ListUserProjectiles:
			if (data.leftMenuCursor.getIndexVertical() == 0)
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_ListSimpleMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsSimple.length, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
			} // end if
			break;*/
		case Constants.leftMenuState_ListBasicMenu:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case Constants.leftMenuList_Traction:
			//if (data.leftMenuCursor.getIndexVertical() == 0)
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_Traction_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
			break;
			default:
			break;
			}
		}
			break;
		case Constants.leftMenuState_ListSimpleMenu:
			{switch (data.leftMenuCursor.getIndexVertical())
			{
			case Constants.leftMenuList_MaxHP:
				//if (data.leftMenuCursor.getIndexVertical() == 0)
				{
					data.resetLeftMenu(false);
					data.leftMenuState = Constants.leftMenuState_MaxHP_Select;
					data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
					checkForResourceManagerItemState();
					graphicPool.loadPreviewAnimations();
					//data.mainState = Constants.mainState_Edit;
				} // end if
				break;
			case Constants.leftMenuList_Traction:
				//if (data.leftMenuCursor.getIndexVertical() == 0)
				{
					data.resetLeftMenu(false);
					data.leftMenuState = Constants.leftMenuState_Traction_Select; //Constants.leftMenuState_listAdvancedOptions
					data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
					checkForResourceManagerItemState();
					graphicPool.loadPreviewAnimations();
					//data.mainState = Constants.mainState_Edit;
				} // end if
				break;
			case Constants.leftMenuList_MaxAcceleration:
				//if (data.leftMenuCursor.getIndexVertical() == 0)
				{
					data.resetLeftMenu(false);
					data.leftMenuState = Constants.leftMenuState_MaxAcceleration_Select; //Constants.leftMenuState_listAdvancedOptions
					data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
					checkForResourceManagerItemState();
					graphicPool.loadPreviewAnimations();
					//data.mainState = Constants.mainState_Edit;
				} // end if
				break;
				case Constants.leftMenuList_MaxVelocity:
					//if (data.leftMenuCursor.getIndexVertical() == 0)
					{
						data.resetLeftMenu(false);
						data.leftMenuState = Constants.leftMenuState_MaxVelocity; //Constants.leftMenuState_listAdvancedOptions
						data.leftMenuCursor.setNumberInListVertical(2, true);
						//data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditDirection.length, true);
						checkForResourceManagerItemState();
						graphicPool.loadPreviewAnimations();
						//data.mainState = Constants.mainState_Edit;
					} // end if
					break;
				case Constants.leftMenuList_MaxDefense:
					//if (data.leftMenuCursor.getIndexVertical() == 0)
					{
						data.resetLeftMenu(false);
						data.leftMenuState = Constants.leftMenuState_MaxDefense; //Constants.leftMenuState_listAdvancedOptions
						data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditDirection.length, true);
						checkForResourceManagerItemState();
						graphicPool.loadPreviewAnimations();
						//data.mainState = Constants.mainState_Edit;
					} // end if
					break;
				case Constants.leftMenuList_MaxPower:
					//if (data.leftMenuCursor.getIndexVertical() == 0)
					{
						data.resetLeftMenu(false);
						data.leftMenuState = Constants.leftMenuState_MaxPower; //Constants.leftMenuState_listAdvancedOptions
						data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditDirection.length, true);
						checkForResourceManagerItemState();
						graphicPool.loadPreviewAnimations();
						//data.mainState = Constants.mainState_Edit;
					} // end if
					break;
				case Constants.leftMenuList_isAffectedBy:
					//if (data.leftMenuCursor.getIndexVertical() == 0)
					{
						data.resetLeftMenu(false);
						data.leftMenuState = Constants.leftMenuState_isAffectedBy; //Constants.leftMenuState_listAdvancedOptions
						data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditAffectedBy.length, true);
						checkForResourceManagerItemState();
						graphicPool.loadPreviewAnimations();
						//data.mainState = Constants.mainState_Edit;
					} // end if
					break;
					default:
					
					break;
			}
			}
			break;
		case Constants.leftMenuState_ListAdvancedMenu:
		{	switch (data.leftMenuCursor.getIndexVertical())
			{
			case Constants.leftMenuList_MaxHP:
				//if (data.leftMenuCursor.getIndexVertical() == 0)
				{
					data.resetLeftMenu(false);
					data.leftMenuState = Constants.leftMenuState_MaxHP_Select; //Constants.leftMenuState_listAdvancedOptions
					data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
					checkForResourceManagerItemState();
					graphicPool.loadPreviewAnimations();
					//data.mainState = Constants.mainState_Edit;
				} // end if
				break;
			case Constants.leftMenuList_Traction:
				//if (data.leftMenuCursor.getIndexVertical() == 0)
				{
					data.resetLeftMenu(false);
					data.leftMenuState = Constants.leftMenuState_Traction_Select; //Constants.leftMenuState_listAdvancedOptions
					data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
					checkForResourceManagerItemState();
					graphicPool.loadPreviewAnimations();
					//data.mainState = Constants.mainState_Edit;
				} // end if
				break;
			case Constants.leftMenuList_MaxAcceleration:
				//if (data.leftMenuCursor.getIndexVertical() == 0)
				{
					data.resetLeftMenu(false);
					data.leftMenuState = Constants.leftMenuState_MaxAcceleration_Select; //Constants.leftMenuState_listAdvancedOptions
					data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
					checkForResourceManagerItemState();
					graphicPool.loadPreviewAnimations();
					//data.mainState = Constants.mainState_Edit;
				} // end if
				break;
				case Constants.leftMenuList_MaxVelocity:
					//if (data.leftMenuCursor.getIndexVertical() == 0)
					{
						data.resetLeftMenu(false);
						data.leftMenuState = Constants.leftMenuState_MaxVelocity; //Constants.leftMenuState_listAdvancedOptions
						data.leftMenuCursor.setNumberInListVertical(2, true);
						checkForResourceManagerItemState();
						graphicPool.loadPreviewAnimations();
						//data.mainState = Constants.mainState_Edit;
					} // end if
					break;
				case Constants.leftMenuList_MaxDefense:
					//if (data.leftMenuCursor.getIndexVertical() == 0)
					{
						data.resetLeftMenu(false);
						data.leftMenuState = Constants.leftMenuState_MaxDefense; //Constants.leftMenuState_listAdvancedOptions
						data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditDirection.length, true);
						checkForResourceManagerItemState();
						graphicPool.loadPreviewAnimations();
						//data.mainState = Constants.mainState_Edit;
					} // end if
					break;
				case Constants.leftMenuList_MaxPower:
					//if (data.leftMenuCursor.getIndexVertical() == 0)
					{
						data.resetLeftMenu(false);
						data.leftMenuState = Constants.leftMenuState_MaxPowerUp_Select; //Constants.leftMenuState_listAdvancedOptions
						data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
						checkForResourceManagerItemState();
						graphicPool.loadPreviewAnimations();
						//data.mainState = Constants.mainState_Edit;
					} // end if
					break;
				case Constants.leftMenuList_isAffectedBy:
					//if (data.leftMenuCursor.getIndexVertical() == 0)
					{
						data.resetLeftMenu(false);
						data.leftMenuState = Constants.leftMenuState_isAffectedBy; //Constants.leftMenuState_listAdvancedOptions
						data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditAffectedBy.length, true);
						checkForResourceManagerItemState();
						graphicPool.loadPreviewAnimations();
						//data.mainState = Constants.mainState_Edit;
					} // end if
					break;
					default:
					
					break;
			}
		}
		break;
		case Constants.leftMenuState_MaxVelocity:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case Constants.leftMenuList_Up:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_MaxVelocityUp_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
			//	graphicsPool.leftMenu_UpDown[3] = 
				//data.mainState = Constants.mainState_Edit;
			} // end if
			break;
			/*case Constants.leftMenuList_Down:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_MaxVelocityDown_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
				break;*/
			case Constants.leftMenuList_Down:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_MaxVelocityLeft_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
				break;
		/*	case Constants.leftMenuList_Right:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_MaxVelocityRight_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
				break;*/
				
			} // end if
		}
			break;
		case Constants.leftMenuState_MaxHP:
		{
			data.resetLeftMenu(false);
			data.leftMenuState = Constants.leftMenuState_MaxHP_Select; //Constants.leftMenuState_listAdvancedOptions
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
			checkForResourceManagerItemState();
			graphicPool.loadPreviewAnimations();
			//data.mainState = Constants.mainState_Edit;
		} // end if
			break;
		case Constants.leftMenuState_MaxDefense:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case Constants.leftMenuList_Up:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_MaxDefenseUp_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
			break;
			case Constants.leftMenuList_Down:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_MaxDefenseDown_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
				break;
			case Constants.leftMenuList_Left:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_MaxDefenseLeft_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
				break;
			case Constants.leftMenuList_Right:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_MaxDefenseRight_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
				break;
				
			} // end if
		}
			break;
		case Constants.leftMenuState_MaxPower:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case Constants.leftMenuList_Up:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_MaxPowerUp_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
			break;
			case Constants.leftMenuList_Down:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_MaxPowerDown_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
				break;
			case Constants.leftMenuList_Left:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_MaxPowerLeft_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
				break;
			case Constants.leftMenuList_Right:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_MaxPowerRight_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
				break;
				
			} // end if
		}
			break;
		case Constants.leftMenuState_Traction:
		{
			data.resetLeftMenu(false);
			data.leftMenuState = Constants.leftMenuState_Traction_Select; //Constants.leftMenuState_listAdvancedOptions
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
			checkForResourceManagerItemState();
			graphicPool.loadPreviewAnimations();
			//data.mainState = Constants.mainState_Edit;
		} // end if
			break;
		case Constants.leftMenuState_isAffectedBy:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case Constants.leftMenuList_VertGravity:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_VertGravity_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
			break;
			case Constants.leftMenuList_HorzGravity:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_HorzGravity_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
				break;
			case Constants.leftMenuList_XTraction:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_XTraction_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
				break;
			case Constants.leftMenuList_YTraction:
			{
				data.resetLeftMenu(false);
				data.leftMenuState = Constants.leftMenuState_YTraction_Select; //Constants.leftMenuState_listAdvancedOptions
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
				checkForResourceManagerItemState();
				graphicPool.loadPreviewAnimations();
				//data.mainState = Constants.mainState_Edit;
			} // end if
				break;
				
			} // end if
		}
			break;
		case Constants.leftMenuState_MaxAcceleration:
		{
			data.resetLeftMenu(false);
			data.leftMenuState = Constants.leftMenuState_MaxAcceleration_Select; //Constants.leftMenuState_listAdvancedOptions
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UpDown.length-1, true);
			checkForResourceManagerItemState();
			graphicPool.loadPreviewAnimations();
			//data.mainState = Constants.mainState_Edit;
		} // end if
			break;
			
		case Constants.leftMenuState_MaxAcceleration_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.maxAcceleration ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.maxAcceleration--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
		case Constants.leftMenuState_Traction_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.Traction ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.Traction--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
		case Constants.leftMenuState_MaxHP_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.maxHP ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				if(data.maxHP>0)
				{data.maxHP--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);}
				break;
				default:
					break;
			}
		}
			
		case Constants.leftMenuState_MaxVelocityUp_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.VelocityUp ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.VelocityUp--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_MaxVelocityDown_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.VelocityDown ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.VelocityDown--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_MaxVelocityLeft_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.VelocityLeft ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.VelocityLeft--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_MaxVelocityRight_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.VelocityRight ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.VelocityRight--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_MaxDefenseUp_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.DefenseUp ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.DefenseUp--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_MaxDefenseDown_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.DefenseDown ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.DefenseDown--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_MaxDefenseLeft_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.DefenseLeft ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.DefenseLeft--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_MaxDefenseRight_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.DefenseRight ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.DefenseRight--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_MaxPowerUp_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.PowerUp ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.PowerUp--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_MaxPowerDown_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.PowerDown ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.PowerDown--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_MaxPowerLeft_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.PowerLeft ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.PowerLeft--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_MaxPowerRight_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.PowerRight ++;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.PowerRight--;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_VertGravity_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.vertGravity = true;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.vertGravity = false;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_HorzGravity_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.horzGravity = true;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.horzGravity = false;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_XTraction_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.xTraction = true;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.xTraction = false;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
		case Constants.leftMenuState_YTraction_Select:
		{
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case 0:
				data.yTraction = true;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
			break;
			case 1:
				data.yTraction = false;
				xmlManager.saveUserCharacteristicToVector(data.selectedEditPath);
				break;
				default:
					break;
			}
		}
			break;
			
			
				default:
			// ignore invalid cases
			break;
		} // end switch

	}

	// redirected method for handling the selection action while the top menu is on Manage Players
	private void selectMenu_ManagePlayers()
	{
		
		switch (data.leftMenuState)
		{
		case Constants.leftMenuState_ManagePlayers:
			data.resetLeftMenu(false);
			data.leftMenuState = Constants.leftMenuState_ListUserPlayers;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserPlayers.size(), true); 
			graphicPool.loadPreviewAnimations();
			break;
		case Constants.leftMenuState_ListUserPlayers:
			//Add selected player to XML
			String selectedImage = graphicPool.leftMenu_UserPlayers.get(data.leftMenuCursor.getIndexVertical()).getXmlPath();
			selectedImage = selectedImage.replace("data/Players/", "");
			XmlHelper xml = new XmlHelper(data.pathToProject + "ProjectConfig.xml");
			XmlNodeHelper help = xml.getHelperNodeRoot();
			if(help.getChild("SelectedPlayer")==null){
				help.addChildNode("SelectedPlayer", selectedImage);
			}else{
				help.getChild("SelectedPlayer").setValue(selectedImage);
			}
			xml.saveXML();
			data.resetLeftMenu(false);
			data.leftMenuState = Constants.leftMenuState_ManagePlayers;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_ManagePlayers.length, true); 
			break;
		default:
			// ignore
			break;
		}
	}

	// redirected method for handling the selection action while the top menu is on Layer Select
	private void selectMenu_LevelSelect()
	{
		String selectedLevel = graphicPool.leftMenu_LevelSelect.get(data.leftMenuCursor.getIndexVertical()).getXmlPath();
		int currentIndex = data.leftMenuCursor.getIndexVertical();
		int vecSize = graphicPool.leftMenu_LevelSelect.size();
		if (currentIndex == Constants.leftMenu_IndexAddNew)
		{
			String tempPath = FileHelper.addLevelToProject(data.pathToProject);
			GraphicObject tempGO = new GraphicObject();
			tempGO.setImage(graphicPool.blankMenuItem);
			tempGO.setXmlPath(tempPath);
			graphicPool.leftMenu_LevelSelect.add(tempGO);
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_LevelSelect.size(), true);
		} // end if
		else if (currentIndex == Constants.leftMenu_IndexDeleteCurrent && 
				vecSize > Constants.leftMenu_LevelSelectMinimumSize)
		{
			String fileToDelete = data.currentLevelPath;
			data.currentLevelPath = graphicPool.leftMenu_LevelSelect.get(Constants.leftMenu_IndexLevelSelectDefault).getXmlPath();
			data.userLevel = 0;
			if (FileHelper.returnOnlyName(data.currentLevelPath).equalsIgnoreCase(FileHelper.returnOnlyName(fileToDelete)))
			{
				data.currentLevelPath = graphicPool.leftMenu_LevelSelect.get(Constants.leftMenu_IndexLevelSelectDefault + 1).getXmlPath();
			} // end if
			data.resetLeftMenu(true);
			FileHelper.deleteFile(fileToDelete);
			xmlManager.loadLevelVector();
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_LevelSelect.size(), true);
			data.clearLevelData();
			xmlManager.loadLevelData();
		} // end else if
		else if (!FileHelper.returnOnlyName(data.currentLevelPath).equalsIgnoreCase(FileHelper.returnOnlyName(selectedLevel)))
		{
			// change to the selected level
			xmlManager.optimizeLevelSize();
			data.currentLevelPath = graphicPool.leftMenu_LevelSelect.get(data.leftMenuCursor.getIndexVertical()).getXmlPath();
			data.currentLevelPathBackup = data.currentLevelPath;
			data.userLevel = data.leftMenuCursor.getIndexVertical() - 2;
			data.clearLevelData();
			xmlManager.loadLevelData();
		} // end else if
	}

	// redirected method for handling the selection action while the top menu is on Edit Level
	private void selectMenu_EditLevel()
	{
		switch (data.leftMenuState)
		{
		case Constants.leftMenuState_EditLevel:
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case Constants.leftMenuList_EditLevelSetBackdrop:
				if (graphicPool.leftMenu_UserLevelBackdrops.size() > 0)
				{
					data.resetLeftMenu(true);
					data.leftMenuState = Constants.leftMenuState_ListUserLevelBackdrops;
					data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserLevelBackdrops.size(), true);
				} // end if
				break;
			case Constants.leftMenuList_EditLevelSetMusic:
				if (graphicPool.leftMenu_UserMusic.size() > 0)
				{
					data.resetLeftMenu(true);
					data.leftMenuState = Constants.leftMenuState_ListUserMusic;
					data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserMusic.size(), true);
				} // end if
				break;
/*			
 			case Constants.leftMenuList_EditLevelMakeStartingLevel:
				break;
*/
			default: 
				// ignore invalid cases
				break;
			} // end switch
			break;
		case Constants.leftMenuState_ListUserLevelBackdrops:
			data.currentLevelData.setBackgroundImage(graphicPool.leftMenu_UserLevelBackdrops.get(data.leftMenuCursor.getIndexVertical()));
			xmlManager.saveLevelBackground();
			data.resetLeftMenu(true);
			data.leftMenuState = Constants.leftMenuState_EditLevel;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditLevel.length, true);
			break;
		case Constants.leftMenuState_ListUserMusic:
			data.currentLevelData.setBackgroundMusic(graphicPool.leftMenu_UserMusic.get(data.leftMenuCursor.getIndexVertical()).getXmlPath());
			xmlManager.saveLevelMusic();
			data.resetLeftMenu(true);
			data.leftMenuState = Constants.leftMenuState_EditLevel;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditLevel.length, true);
			break;
		default:
			// ignore invalid cases
			break;
		} // end switchs
	}

	// redirected method for handling the selection action while the top menu is on Place Objects
	private void selectMenu_PlaceObject()
	{
		switch (data.leftMenuState)
		{
		case Constants.leftMenuState_PlaceObjects:
			switch(data.leftMenuCursor.getIndexVertical())
			{
			case Constants.leftMenuList_PlaceObjectsPlayers:
				data.leftMenuState = Constants.leftMenuState_FourPlayers;
				data.leftMenuCursor.setNumberInListVertical(Constants.leftMenuList_ManagePlayersIndexMax, true);
				data.resetLeftMenu(false);
				break;
			/*case Constants.leftMenuList_PlaceObjectsNPCs:
				if (graphicPool.leftMenu_UserNPCs.size() > 0)
				{
					data.leftMenuState = Constants.leftMenuState_ListUserNPCs;
					data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserNPCs.size(), true);
					data.resetLeftMenu(false);
				} // end if
				break;
			*/
			case Constants.leftMenuList_PlaceObjectsObjects:
				if (graphicPool.leftMenu_UserObjects.size() > 0)
				{
					data.leftMenuState = Constants.leftMenuState_ListUserObjects;
					data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserObjects.size(), true);
					data.resetLeftMenu(false);
				} // end if
				break;
			case Constants.leftMenuList_PlaceObjectsTerrains:
				if (graphicPool.leftMenu_UserTerrains.size() > 0)
				{
					data.leftMenuState = Constants.leftMenuState_Layers;
					data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_PlaceObjectLayer.length, true);
					data.resetLeftMenu(true);
					data.currentLayer = Constants.layer_BackgroundObject;
				} // end if
				break;
			default:
				// ignore invalid cases
				break;
			} // end switch
			break;
		case Constants.leftMenuState_Layers:
			if (graphicPool.leftMenu_UserTerrains.size() > 0)
			{
				data.leftMenuState = Constants.leftMenuState_ListUserTerrains;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserTerrains.size(), true);
				data.resetLeftMenu(false);
			} // end if
			break;
		case Constants.leftMenuState_FourPlayers:
		//case Constants.leftMenuState_ListUserNPCs:
		case Constants.leftMenuState_ListUserObjects:
		case Constants.leftMenuState_ListUserTerrains:
			data.mainState = Constants.mainState_BuilderWindow;
			break;
		default:
			// ignore invalid cases
			break;
		} // end switch
	}

	// add to this one, homie
	private void selectMenu_WinLoseEvents()
	{
		/*
		data.leftMenuState = Constants.leftMenuState_ListAllWinTiles;
		data.leftMenuCursor.setNumberInListVertical(Constants.leftMenuList_WinLoseEventsIndexMax, true);
		data.resetLeftMenu(false);
		*/
	}

	// redirected method for handling the selection action while the top menu is on Test Level
	private void selectMenu_TestLevel()
	{
		switch (data.leftMenuCursor.getIndexVertical())
		{
		case Constants.leftMenuList_PlayTestLevel:
			testMain.currentLevel = data.userLevel; // Ensure that level is set to user's currently selected level
			xmlManager.optimizeLevelSize();
			xmlManager.loadLevelData();
			audioPool.stopAllAudio();
			testMain.startTestMode();
			data.mainState = Constants.mainState_Test;
			break;
		case Constants.leftMenuList_PlayGame:
			testMain.currentLevel = 0; // Set current level to 0 to start at the beginning
			xmlManager.optimizeLevelSize();
			xmlManager.loadLevelData();
			audioPool.stopAllAudio();			
			testMain.startTestMode();
			data.mainState = Constants.mainState_Test;
			break;
		default: // ignore invalid cases
				break;
		}

	}

	// redirected method for handling the return action while the top menu is on Manage Resources
	private void returnMenu_ManageResources()
	{
		switch (data.leftMenuState)
		{
		case Constants.leftMenuState_ListAllLevelBackdrops:
			data.resetLeftMenu(true);
			data.leftMenuState = Constants.leftMenuState_ListUserLevelBackdropsWithExtra;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserLevelBackdropsExtra.size(), true);
			break;
		case Constants.leftMenuState_ListAllPlayers:
			data.resetLeftMenu(false);
			data.leftMenuState = Constants.leftMenuState_ListUserPlayersWithExtra;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserPlayersExtra.size(), true);
			break;
		/*
		case Constants.leftMenuState_ListAllNPCs:
			data.resetLeftMenu(false);
			data.leftMenuState = Constants.leftMenuState_ListUserNPCsWithExtra;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserNPCsExtra.size(), true);
			break;
		*/
		case Constants.leftMenuState_ListAllObjects:
			data.resetLeftMenu(false);
			data.leftMenuState = Constants.leftMenuState_ListUserObjectsWithExtra;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserObjectsExtra.size(), true);
			break;
		case Constants.leftMenuState_ListAllTerrains:
			data.resetLeftMenu(false);
			data.leftMenuState = Constants.leftMenuState_ListUserTerrainsWithExtra;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserTerrainsExtra.size(), true);
			break;
		/*
		case Constants.leftMenuState_ListAllProjectiles:
			data.resetLeftMenu(false);
			data.leftMenuState = Constants.leftMenuState_ListUserProjectilesWithExtra;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserProjectilesExtra.size(), true);
			break;
		*/
		case Constants.leftMenuState_ListAllMusic:
			data.resetLeftMenu(true);
			data.leftMenuState = Constants.leftMenuState_ListUserMusicWithExtra;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserMusicExtra.size(), true);
			break;
		case Constants.leftMenuState_ListAllSounds:
			data.resetLeftMenu(true);
			data.leftMenuState = Constants.leftMenuState_ListUserSoundsWithExtra;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserSoundsExtra.size(), true);
			break;
		case Constants.leftMenuState_ListUserLevelBackdropsWithExtra:
		case Constants.leftMenuState_ListUserPlayersWithExtra:
		//case Constants.leftMenuState_ListUserNPCsWithExtra:
		case Constants.leftMenuState_ListUserObjectsWithExtra:
		case Constants.leftMenuState_ListUserTerrainsWithExtra:
		//case Constants.leftMenuState_ListUserProjectilesWithExtra:
		case Constants.leftMenuState_ListUserMusicWithExtra:
		case Constants.leftMenuState_ListUserSoundsWithExtra:
			data.resetLeftMenu(true);
			data.leftMenuState = Constants.leftMenuState_ManageResources;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_ManageResources.length, true);
			break;
		default:
			// ignore invalid cases
			break;
		} // end switch
	}

	// redirected method for handling the return action while the top menu is on Edit Object
	private void returnMenu_EditObject()
	{
		if(data.leftMenuState==Constants.leftMenuState_EditObjects)
			data.leftMenuState = Constants.leftMenuState_EditObjects;
		else
		{
switch (data.leftMenuState)
		{

	
		case Constants.leftMenuState_Traction_Select:
			if(data.menuSelect==2){
				data.leftMenuState = Constants.leftMenuState_ListBasicMenu;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsBasic.length, true);
			}
			else if(data.menuSelect==1){
				data.leftMenuState = Constants.leftMenuState_ListSimpleMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsSimple.length, true);
			}
			else{
				data.leftMenuState = Constants.leftMenuState_ListAdvancedMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsAdvanced.length, true);
			}
				
			break;
		case Constants.leftMenuState_ListBasicMenu:
			data.leftMenuState = Constants.leftMenuState_ListUserTerrains;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserObjects.size(), true);
			break;
		case Constants.leftMenuState_ListUserTerrains:
			data.leftMenuState = Constants.leftMenuState_EditObjects;
			break;
		case Constants.leftMenuState_ListSimpleMenu:
			data.leftMenuState = Constants.leftMenuState_ListUserObjects;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserObjects.size(), true);
			break;
		case Constants.leftMenuState_ListUserObjects:
			data.leftMenuState = Constants.leftMenuState_EditObjects;
			break;
		case Constants.leftMenuState_ListAdvancedMenu:
			data.leftMenuState = Constants.leftMenuState_ListUserPlayers;
			break;
		case Constants.leftMenuState_ListUserPlayers:
			data.leftMenuState = Constants.leftMenuState_EditObjects;
			break;
		case Constants.leftMenuState_MaxHP_Select:
			if(data.menuSelect==1){
				data.leftMenuState = Constants.leftMenuState_ListSimpleMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsSimple.length, true);
			}
			else{
				data.leftMenuState = Constants.leftMenuState_ListAdvancedMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsAdvanced.length, true);
			}
			break;
		/*case Constants.leftMenuList_Traction:
			if(data.menuSelect==0)
				data.leftMenuState = Constants.leftMenuState_ListAdvancedMenu;
			else
				data.leftMenuState = Constants.leftMenuState_ListSimpleMenu;
			break;*/
		case Constants.leftMenuList_MaxAcceleration:
			
			break;
		case Constants.leftMenuState_MaxAcceleration_Select:
			data.leftMenuState = Constants.leftMenuState_ListAdvancedMenu;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsAdvanced.length, true);
		
		
			break;
		case Constants.leftMenuList_isAffectedBy:
			break;
		case Constants.leftMenuState_MaxVelocityUp_Select:
			data.leftMenuState = Constants.leftMenuState_MaxVelocity;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_UserObjects.size(), true);
			data.leftMenuCursor.setNumberInListVertical(2, true);
			break;
		case Constants.leftMenuState_MaxVelocityLeft_Select:
			data.leftMenuState = Constants.leftMenuState_MaxVelocity;
			data.leftMenuCursor.setNumberInListVertical(2, true);
			break;
		case Constants.leftMenuState_MaxVelocity:
			data.leftMenuState = Constants.leftMenuList_MaxVelocity;
		case Constants.leftMenuList_MaxVelocity:
			if(data.menuSelect==1){
				data.leftMenuState = Constants.leftMenuState_ListSimpleMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsSimple.length, true);
			}
			else{
				data.leftMenuState = Constants.leftMenuState_ListAdvancedMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsAdvanced.length, true);
			}
			break;
		case Constants.leftMenuState_MaxDefenseUp_Select:
			data.leftMenuState = Constants.leftMenuState_MaxDefense;
			data.leftMenuCursor.setNumberInListVertical(4, true);
			break;
		case Constants.leftMenuState_MaxDefenseDown_Select:
			data.leftMenuState = Constants.leftMenuState_MaxDefense;
			data.leftMenuCursor.setNumberInListVertical(4, true);
			break;
		case Constants.leftMenuState_MaxDefenseLeft_Select:
			data.leftMenuState = Constants.leftMenuState_MaxDefense;
			data.leftMenuCursor.setNumberInListVertical(4, true);
			break;
		case Constants.leftMenuState_MaxDefenseRight_Select:
			data.leftMenuState = Constants.leftMenuState_MaxDefense;
			data.leftMenuCursor.setNumberInListVertical(4, true);
		case Constants.leftMenuState_MaxDefense:
			data.leftMenuState = Constants.leftMenuList_MaxDefense;
		case Constants.leftMenuList_MaxDefense:
			if(data.menuSelect==1){
				data.leftMenuState = Constants.leftMenuState_ListSimpleMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsSimple.length, true);
			}
			else{
				data.leftMenuState = Constants.leftMenuState_ListAdvancedMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsAdvanced.length, true);
			}
			break;
		case Constants.leftMenuState_MaxPowerUp_Select:
			if(data.menuSelect==1){
				data.leftMenuState = Constants.leftMenuState_MaxPower;
				data.leftMenuCursor.setNumberInListVertical(4, true);
				}
			else{
				data.leftMenuState = Constants.leftMenuState_ListAdvancedMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsAdvanced.length, true);
			}
			break;
		case Constants.leftMenuState_MaxPowerDown_Select:
			data.leftMenuState = Constants.leftMenuState_MaxPower;
			data.leftMenuCursor.setNumberInListVertical(4, true);
			break;
		case Constants.leftMenuState_MaxPowerLeft_Select:
			data.leftMenuState = Constants.leftMenuState_MaxPower;
			data.leftMenuCursor.setNumberInListVertical(4, true);
			break;
		case Constants.leftMenuState_MaxPowerRight_Select:
			data.leftMenuState = Constants.leftMenuState_MaxPower;
			data.leftMenuCursor.setNumberInListVertical(4, true);
		case Constants.leftMenuState_MaxPower:
			data.leftMenuState = Constants.leftMenuList_MaxPower;
			if(data.menuSelect==1){
				data.leftMenuState = Constants.leftMenuState_ListSimpleMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsSimple.length, true);
			}
			else{
				data.leftMenuState = Constants.leftMenuState_ListAdvancedMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsAdvanced.length, true);
			}
			break;
		case Constants.leftMenuState_isAffectedBy:
			if(data.menuSelect==1){
				data.leftMenuState = Constants.leftMenuState_ListSimpleMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsSimple.length, true);
			}
			else{
				data.leftMenuState = Constants.leftMenuState_ListAdvancedMenu;
				data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditCharacteristicsAdvanced.length, true);
			}
			break;
		case Constants.leftMenuState_VertGravity_Select:
			data.leftMenuState = Constants.leftMenuState_isAffectedBy; //Constants.leftMenuState_listAdvancedOptions
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditAffectedBy.length, true);
			break;
		case Constants.leftMenuState_HorzGravity_Select:
			data.leftMenuState = Constants.leftMenuState_isAffectedBy; //Constants.leftMenuState_listAdvancedOptions
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditAffectedBy.length, true);
			break;
		case Constants.leftMenuState_XTraction_Select:
			data.leftMenuState = Constants.leftMenuState_isAffectedBy; //Constants.leftMenuState_listAdvancedOptions
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditAffectedBy.length, true);
			break;
		case Constants.leftMenuState_YTraction_Select:
			data.leftMenuState = Constants.leftMenuState_isAffectedBy; //Constants.leftMenuState_listAdvancedOptions
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditAffectedBy.length, true);
			break;
			default:
				break;
		}
		}
	}

	// redirected method for handling the return action while the top menu is on Manage Players
	private void returnMenu_ManagePlayers()
	{
		data.resetLeftMenu(false);
		data.leftMenuState = Constants.leftMenuState_ManagePlayers;
		data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_ManagePlayers.length, true); 
	}

	// redirected method for handling the return action while the top menu is on Level Select
	private void returnMenu_LevelSelect()
	{

	}

	// redirected method for handling the return action while the top menu is on Edit Level
	private void returnMenu_EditLevel()
	{
		switch (data.leftMenuState)
		{
		case Constants.leftMenuState_ListUserLevelBackdrops:
		case Constants.leftMenuState_ListUserMusic:
			data.resetLeftMenu(true);
			data.leftMenuState = Constants.leftMenuState_EditLevel;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_EditLevel.length, true);
			break;
		default:
			// ignore invalid cases
			break;
		} // end switch
	}

	// redirected method for handling the return action while the top menu is on Place Objects
	private void returnMenu_PlaceObject()
	{
		switch (data.leftMenuState)
		{
		case Constants.leftMenuState_FourPlayers:
		case Constants.leftMenuState_Layers:
		//case Constants.leftMenuState_ListUserNPCs:
		case Constants.leftMenuState_ListUserObjects:
			data.resetLeftMenu(true);
			data.leftMenuState = Constants.leftMenuState_PlaceObjects;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_PlaceObjects.length, true);
			data.currentLayer = Constants.layer_MainObject;
			break;
		case Constants.leftMenuState_ListUserTerrains:
			data.resetLeftMenu(true);
			data.leftMenuState = Constants.leftMenuState_Layers;
			data.leftMenuCursor.setNumberInListVertical(graphicPool.leftMenu_PlaceObjectLayer.length, true);
			data.currentLayer = Constants.layer_BackgroundObject;
			break;
		default:
			// ignore invalid cases
			break;
		} // end switch
	}

	// redirected method for handling the return action while the top menu is on TestLevel
	private void returnMenu_TestLevel()
	{

	}

	// checks for an object in the Vector<GraphicObject> that has a specific pathToImage
	private boolean doesThisAlreadyExist(Vector<GraphicObject> vec, String pathToImage)
	{
		boolean returnValue = false;
		for (int i = 0; i < vec.size(); ++i)
		{
			if (vec.get(i).getXmlPath().equalsIgnoreCase(pathToImage))
			{
				returnValue = true;
			} // end if
		} // end for
		return returnValue;
	}

	private void checkForResourceManagerItemState()
	{
		if (data.topMenuState == Constants.topMenu_IndexManageResources)
		{
			boolean itemExists = false;
			switch (data.leftMenuState)
			{
			case Constants.leftMenuState_ListAllPlayers:
				itemExists = doesThisAlreadyExist(graphicPool.leftMenu_UserPlayers, 
						graphicPool.leftMenu_Players[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				break;
			case Constants.leftMenuState_ListAllLevelBackdrops:
				itemExists = doesThisAlreadyExist(graphicPool.leftMenu_UserLevelBackdrops, 
						graphicPool.leftMenu_LevelBackdrops[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				break;
			/*
			case Constants.leftMenuState_ListAllNPCs:
				itemExists = doesThisAlreadyExist(graphicPool.leftMenu_UserNPCs, 
						graphicPool.leftMenu_NPCs[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				break;
			*/
			case Constants.leftMenuState_ListAllObjects:
				itemExists = doesThisAlreadyExist(graphicPool.leftMenu_UserObjects, 
						graphicPool.leftMenu_Objects[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				break;
			/*
			case Constants.leftMenuState_ListAllProjectiles:
				itemExists = doesThisAlreadyExist(graphicPool.leftMenu_UserProjectiles, 
						graphicPool.leftMenu_Projectiles[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				break;
			*/
			case Constants.leftMenuState_ListAllMusic:
				itemExists = doesThisAlreadyExist(graphicPool.leftMenu_UserMusic, 
						graphicPool.leftMenu_Music[data.leftMenuCursor.getIndexVertical()].getXmlPath());
						audioPool.leftMenu_BackgroundMusic[data.leftMenuCursor.getIndexVertical()].loopAudio();
				break;
			case Constants.leftMenuState_ListAllSounds:
				itemExists = doesThisAlreadyExist(graphicPool.leftMenu_UserSounds, 
						graphicPool.leftMenu_Sounds[data.leftMenuCursor.getIndexVertical()].getXmlPath());
						audioPool.leftMenu_SoundEffects[data.leftMenuCursor.getIndexVertical()].playAudio();
				break;
			case Constants.leftMenuState_ListAllTerrains:
				itemExists = doesThisAlreadyExist(graphicPool.leftMenu_UserTerrains, 
						graphicPool.leftMenu_Terrains[data.leftMenuCursor.getIndexVertical()].getXmlPath());
				break;
			default:
				// Ignore invalid cases
				break;
			}
			if (itemExists)
			{
				data.resourceManager_ItemState = Constants.windowPreviewBottomText_StateAlreadyAdded;
			}
			else
			{
				data.resourceManager_ItemState = Constants.windowPreviewBottomText_StateNone;
			}
		} // end if
	}
}
