package gamemaker;

import java.awt.Image;
import java.net.URL;
import java.util.Vector;

import org.w3c.dom.NodeList;

public class XmlManager {
	
// =================================================
//  CLASS VARIABLES
// =================================================
	
	private EventHandler eventHandler;
	private CPaint cPaint;
	private GraphicPool graphicPool;
	private AudioPool audioPool;
	private Data data;
	private StartingClassGameMaker startingClass;
	public URL base;
	private Level level = new Level();
	
// =================================================
//  PUBLIC METHODS
// =================================================
	
	// These methods set the references for the appropriate variables
	// NOTE: Some of these may not currently be used or needed
	public void setEventHandler(EventHandler EH){eventHandler = EH;}
	public void setCPaint(CPaint CP){cPaint = CP;}
	public void setGraphicPool(GraphicPool GP){graphicPool = GP;}
	public void setData(Data D){data = D;}
	public void setStartingClass(StartingClassGameMaker SCGM){startingClass = SCGM;}
	public void setAudioPool(AudioPool aPool) {	audioPool = aPool;}
	public void setBase(URL url){base = url;}
	
	// load a project based of the project path in the data object
	public void loadProject()
	{
		loadUserGraphicVectors();
		loadLevelVector();
		data.resetVariablesToInitial();
	}
	
	// load all of the possible levels to select inside the builder
	public void loadLevelVector()
	{
		graphicPool.leftMenu_LevelSelect.clear();
		graphicPool.leftMenu_LevelSelect.add(graphicPool.newButtonGO);
		graphicPool.leftMenu_LevelSelect.add(graphicPool.deleteCurrentButtonGO);
		String tempLevels[] = FileHelper.getDirectoryContents(data.pathToProject + Constants.dir_ProjectLevelData);
		for (int i = 0; i < tempLevels.length; ++i)
		{
			GraphicObject tempGraphicObject = new GraphicObject();
			tempGraphicObject.setXmlPath(tempLevels[i]);
			tempGraphicObject.setImage(startingClass.getImage(graphicPool.base, Constants.graphicPool_BuilderMenuBlankItem));
			graphicPool.leftMenu_LevelSelect.add(tempGraphicObject);
		} // end for
	}
	
	// load the level data from the level whose path is stored in the data object
	public void loadLevelData()
	{
		XmlHelper helper = new XmlHelper(data.currentLevelPath);
		XmlNodeHelper node = helper.getHelperNodeRoot();
		GraphicObject tempObject = new GraphicObject();
		Image tempImage = startingClass.getImage(graphicPool.base, node.getChild("BackgroundImage").getValue());
		tempObject.setImage(tempImage);
		tempObject.setXmlPath(node.getChild("BackgroundImage").getValue());
		data.currentLevelData.setBackgroundImage(tempObject);
		String tempStr = node.getChild("BackgroundMusic").getValue();
		data.currentLevelData.setBackgroundMusic(tempStr);
		int xSize = Integer.valueOf(node.getChild("Cols").getValue());
		int ySize = Integer.valueOf(node.getChild("Rows").getValue());
		data.currentLevelData.setGridSizeOnLoad(xSize, ySize);
		loadAllMapObjects();
		data.builderMapCursor.setNumberInListHorizontal(xSize, true);
		data.builderMapCursor.setNumberInListVertical(ySize, true);
		while(data.builderMapCursor.moveLeft() != Constants.cursorMoveReturn_AtBeginning);
		while(data.builderMapCursor.moveUp() != Constants.cursorMoveReturn_AtBeginning);
	}
	
	// adds a new item to the level xml
	public void addNewMapItemToLevelData(int xPos, int yPos)
	{
		XmlHelper helper = new XmlHelper(data.currentLevelPath);
		XmlNodeHelper node = helper.getHelperNodeRoot();
		XmlNodeHelper childNode = null;
		node = node.getChild("ListOfMapItems");
		// just add the new node
		childNode = node.addChildNode("MapItem");
		childNode.addChildNode("PosX", String.valueOf(xPos));
		childNode.addChildNode("PosY", String.valueOf(yPos));
		childNode.addChildNode("BackgroundLayer");
		childNode.addChildNode("MainLayer");
		childNode.addChildNode("ForegroundLayer");
		childNode.addChildNode("EventLayer");
		finishSavingMapItem(helper, childNode, xPos, yPos);
	}
	
	// ands a new item to an existing level xml
	public void addMapItemToExistingLevelData(int xPos, int yPos)
	{
		XmlHelper helper = new XmlHelper(data.currentLevelPath);
		XmlNodeHelper node = helper.getHelperNodeRoot();
		XmlNodeHelper childNode = null;
		node = node.getChild("ListOfMapItems");
		// Find The correct child node
		NodeList NL = node.getAllChildren();
		for (int i = 0; i < NL.getLength(); ++i)
		{
			XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());
			if (tempNode.getChild("PosX").getValue().equalsIgnoreCase(String.valueOf(xPos)) &&
					tempNode.getChild("PosY").getValue().equalsIgnoreCase(String.valueOf(yPos)))
			{
				childNode = tempNode;
				break;
			} // end if
		} // end for
		finishSavingMapItem(helper, childNode, xPos, yPos);
	}
	
	// removes an item from the level xml
	public void eraseMapItemFromLevelData(int xPos, int yPos)
	{
		XmlHelper helper = new XmlHelper(data.currentLevelPath);
		XmlNodeHelper node = helper.getHelperNodeRoot();
		XmlNodeHelper childNode = null;
		node = node.getChild("ListOfMapItems");
		// Find The correct child node
		NodeList NL = node.getAllChildren();
		for (int i = 0; i < NL.getLength(); ++i)
		{
			XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());
			if (tempNode.getChild("PosX").getValue().equalsIgnoreCase(String.valueOf(xPos)) &&
					tempNode.getChild("PosY").getValue().equalsIgnoreCase(String.valueOf(yPos)))
			{
				childNode = tempNode;
				break;
			} // end if
		} // end for
		if (childNode != null)
		{
			XmlNodeHelper tmpNode = childNode;
			// find which layer to erase
			switch (data.currentLayer)
			{
			case Constants.layer_BackgroundObject:
				tmpNode = childNode.getChild("BackgroundLayer");
				break;
			case Constants.layer_MainObject:
				tmpNode = childNode.getChild("MainLayer");
				// do additional logic if this is a player
				if (tmpNode.getValue().equalsIgnoreCase(Constants.graphicPool_BuilderMenuP1))
				{
					data.p1_X = -1;
					data.p1_Y = -1;
				}
				/*else if (tmpNode.getValue().equalsIgnoreCase(Constants.graphicPool_BuilderMenuP2))
				{
					data.p2_X = -1;
					data.p2_Y = -1;
				}
				else if (tmpNode.getValue().equalsIgnoreCase(Constants.graphicPool_BuilderMenuP3))
				{
					data.p3_X = -1;
					data.p3_Y = -1;
				}
				else if (tmpNode.getValue().equalsIgnoreCase(Constants.graphicPool_BuilderMenuP4))
				{
					data.p4_X = -1;
					data.p4_Y = -1;
				}*/
				break;
			case Constants.layer_ForegroundObject:
				tmpNode = childNode.getChild("ForegroundLayer");
				break;
			case Constants.layer_WinLoseEvent:
				tmpNode = childNode.getChild("EventLayer");
				break;
			default:
				// ignore invalid cases
				break;
			} // end switch
			tmpNode.setValue("");
			data.currentLevelData.mapVec.get(xPos).get(yPos).layer[data.currentLayer] = null;
			// now check to see if this location has any items at all
			boolean allErased = true;
			if (!childNode.getChild("BackgroundLayer").getValue().equals("")) { allErased = false; }
			if (!childNode.getChild("MainLayer").getValue().equals("")) { allErased = false; }
			if (!childNode.getChild("ForegroundLayer").getValue().equals("")) { allErased = false; }
			if (!childNode.getChild("EventLayer").getValue().equals("")) { allErased = false; }
			if (allErased)
			{
				node.removeChildNode(childNode);
				data.currentLevelData.mapVec.get(xPos).get(yPos).setUsed(false);
			}
		}
		helper.saveXML();
	}
	
	// save the level background of the current level to the xml
	public void saveLevelBackground()
	{
		XmlHelper helper = new XmlHelper(data.currentLevelPath);
		XmlNodeHelper node = helper.getHelperNodeRoot();
		node.getChild("BackgroundImage").setValue(data.currentLevelData.getBackgroundImage().getXmlPath());
		helper.saveXML();
	}
	
	// save the level background of the current level to the xml
	public void saveLevelMusic()
	{
		XmlHelper helper = new XmlHelper(data.currentLevelPath);
		XmlNodeHelper node = helper.getHelperNodeRoot();
		node.getChild("BackgroundMusic").setValue(data.currentLevelData.getBackgroundMusic());
		helper.saveXML();
	}
	
	// save the added user level background images to the xml 
	public void saveUserLevelBackgroundGraphicToVector(String pathToImage)
	{
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserLevelBackgroundGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		node.addChildNode("LevelBackroundImage", pathToImage);
		helper.saveXML();
		graphicPool.leftMenu_UserLevelBackdrops.add(graphicPool.leftMenu_LevelBackdrops[data.leftMenuCursor.getIndexVertical()]);
		graphicPool.leftMenu_UserLevelBackdropsExtra.add(graphicPool.leftMenu_LevelBackdrops[data.leftMenuCursor.getIndexVertical()]);
	}
	
	// save the added user player images to the xml
	public void saveUserPlayerGraphicToVector(String pathToImage)
	{
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		node.addChildNode("PlayerImage", pathToImage);
		helper.saveXML();
		graphicPool.leftMenu_UserPlayers.add(graphicPool.leftMenu_Players[data.leftMenuCursor.getIndexVertical()]);
		graphicPool.leftMenu_UserPlayersExtra.add(graphicPool.leftMenu_Players[data.leftMenuCursor.getIndexVertical()]);
	}
	
	// save the added user NPC images to the xml
	public void saveUserNPCGraphicToVector(String pathToImage)
	{
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserNPCGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		node.addChildNode("NPCImage", pathToImage);
		helper.saveXML();
		graphicPool.leftMenu_UserNPCs.add(graphicPool.leftMenu_NPCs[data.leftMenuCursor.getIndexVertical()]);
		graphicPool.leftMenu_UserNPCsExtra.add(graphicPool.leftMenu_NPCs[data.leftMenuCursor.getIndexVertical()]);
	}
	
	// save the added user object images to the xml
	public void saveUserObjectGraphicToVector(String pathToImage)
	{
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		node.addChildNode("ObjectImage", pathToImage);
		helper.saveXML();
		graphicPool.leftMenu_UserObjects.add(graphicPool.leftMenu_Objects[data.leftMenuCursor.getIndexVertical()]);
		graphicPool.leftMenu_UserObjectsExtra.add(graphicPool.leftMenu_Objects[data.leftMenuCursor.getIndexVertical()]);
	}
	
	// save the added user terrain images to the xml
	public void saveUserTerrainGraphicToVector(String pathToImage)
	{
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserTerrainGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		node.addChildNode("TerrainImage", pathToImage);
		helper.saveXML();
		graphicPool.leftMenu_UserTerrains.add(graphicPool.leftMenu_Terrains[data.leftMenuCursor.getIndexVertical()]);
		graphicPool.leftMenu_UserTerrainsExtra.add(graphicPool.leftMenu_Terrains[data.leftMenuCursor.getIndexVertical()]);
	}
	
	// save the added user projectile images to the xml
	public void saveUserProjectileGraphicToVector(String pathToImage)
	{
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserProjectileGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		node.addChildNode("ProjectileImage", pathToImage);
		helper.saveXML();
		graphicPool.leftMenu_UserProjectiles.add(graphicPool.leftMenu_Projectiles[data.leftMenuCursor.getIndexVertical()]);
		graphicPool.leftMenu_UserProjectilesExtra.add(graphicPool.leftMenu_Projectiles[data.leftMenuCursor.getIndexVertical()]);
	}
	
	// save the added user music to the xml
	public void saveUserMusicGraphicToVector(String pathToImage)
	{
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserMusicGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		node.addChildNode("MusicPath", pathToImage);
		helper.saveXML();
		graphicPool.leftMenu_UserMusic.add(graphicPool.leftMenu_Music[data.leftMenuCursor.getIndexVertical()]);
		graphicPool.leftMenu_UserMusicExtra.add(graphicPool.leftMenu_Music[data.leftMenuCursor.getIndexVertical()]);
	}
	
	// save the added user sound to the xml
	public void saveUserSoundGraphicToVector(String pathToImage)
	{
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserSoundGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		node.addChildNode("SoundPath", pathToImage);
		helper.saveXML();
		graphicPool.leftMenu_UserSounds.add(graphicPool.leftMenu_Sounds[data.leftMenuCursor.getIndexVertical()]);
		graphicPool.leftMenu_UserSoundsExtra.add(graphicPool.leftMenu_Sounds[data.leftMenuCursor.getIndexVertical()]);
	}
	
	public void saveUserCharacteristicToVector(String pathToImage)
	{
		level.setObjectDataPath(data.pathToProject);		
		//edit character
		if(data.menuSelect == 0){
		switch(data.leftMenuState)
		{ case Constants.leftMenuState_MaxHP_Select:
		{			
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		if(node.getChild("HpPath")==null){
			node.addChildNode("HpPath",String.valueOf(data.maxHP));
			}
		else{
			node.getChild("HpPath").setValue(String.valueOf(data.maxHP));
			}		
		
		helper.saveXML();
		break;
		}
		case Constants.leftMenuState_Traction_Select:
		{			
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		if(node.getChild("Traction")==null){
			node.addChildNode("Traction",String.valueOf(data.Traction));
			}
		else{
			node.getChild("Traction").setValue(String.valueOf(data.Traction));
			}
		
		helper.saveXML();
		break;
		}
		case Constants.leftMenuState_MaxDefenseUp_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("DefenseUp")==null){
				node.addChildNode("DefenseUp",String.valueOf(data.DefenseUp));
				}
			else{
				node.getChild("DefenseUp").setValue(String.valueOf(data.DefenseUp));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_MaxDefenseUp_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_MaxDefenseDown_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("DefenseDown")==null){
				node.addChildNode("DefenseDown",String.valueOf(data.DefenseDown));
				}
			else{
				node.getChild("DefenseDown").setValue(String.valueOf(data.DefenseDown));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_MaxDefenseDown_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_MaxDefenseLeft_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("DefenseLeft")==null){
				node.addChildNode("DefenseLeft",String.valueOf(data.DefenseLeft));
				}
			else{
				node.getChild("DefenseLeft").setValue(String.valueOf(data.DefenseLeft));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_MaxDefenseLeft_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_MaxDefenseRight_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("DefenseRight")==null){
				node.addChildNode("DefenseRight",String.valueOf(data.DefenseRight));
				}
			else{
				node.getChild("DefenseRight").setValue(String.valueOf(data.DefenseRight));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_MaxDefenseRight_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_MaxPowerUp_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("PowerUp")==null){
				node.addChildNode("PowerUp",String.valueOf(data.PowerUp));
				}
			else{
				node.getChild("PowerUp").setValue(String.valueOf(data.PowerUp));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_MaxPowerUp_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_MaxPowerDown_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("PowerDown")==null){
				node.addChildNode("PowerDown",String.valueOf(data.PowerDown));
				}
			else{
				node.getChild("PowerDown").setValue(String.valueOf(data.PowerDown));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_MaxPowerDown_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_MaxPowerLeft_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("PowerLeft")==null){
				node.addChildNode("PowerLeft",String.valueOf(data.PowerLeft));
				}
			else{
				node.getChild("PowerLeft").setValue(String.valueOf(data.PowerLeft));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_MaxPowerLeft_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_MaxPowerRight_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("PowerRight")==null){
				node.addChildNode("PowerRight",String.valueOf(data.PowerRight));
				}
			else{
				node.getChild("PowerRight").setValue(String.valueOf(data.PowerRight));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_MaxPowerRight_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_MaxVelocityUp_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("VelocityUp")==null){
				node.addChildNode("VelocityUp",String.valueOf(data.VelocityUp));
				}
			else{
				node.getChild("VelocityUp").setValue(String.valueOf(data.VelocityUp));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_MaxVelocityUp_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_MaxVelocityDown_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("VelocityDown")==null){
				node.addChildNode("VelocityDown",String.valueOf(data.VelocityDown));
				}
			else{
				node.getChild("VelocityDown").setValue(String.valueOf(data.VelocityDown));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_MaxVelocityDown_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_MaxVelocityLeft_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("VelocityLeft")==null){
				node.addChildNode("VelocityLeft",String.valueOf(data.VelocityLeft));
				}
			else{
				node.getChild("VelocityLeft").setValue(String.valueOf(data.VelocityLeft));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_MaxVelocityLeft_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_MaxVelocityRight_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("VelocityRight")==null){
				node.addChildNode("VelocityRight",String.valueOf(data.VelocityRight));
				}
			else{
				node.getChild("VelocityRight").setValue(String.valueOf(data.VelocityRight));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_MaxVelocityRight_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_MaxAcceleration_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("MaxAcceleration")==null){
				node.addChildNode("MaxAcceleration",String.valueOf(data.maxAcceleration));
				}
			else{
				node.getChild("MaxAcceleration").setValue(String.valueOf(data.maxAcceleration));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_MaxAcceleration_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_VertGravity_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("VertGravity")==null){
				node.addChildNode("VertGravity",String.valueOf(data.vertGravity));
				}
			else{
				node.getChild("VertGravity").setValue(String.valueOf(data.vertGravity));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_VertGravity_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_HorzGravity_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("HorzGravity")==null){
				node.addChildNode("HorzGravity",String.valueOf(data.horzGravity));
				}
			else{
				node.getChild("HorzGravity").setValue(String.valueOf(data.horzGravity));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_HorzGravity_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_XTraction_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("XTraction")==null){
				node.addChildNode("XTraction",String.valueOf(data.xTraction));
				}
			else{
				node.getChild("XTraction").setValue(String.valueOf(data.xTraction));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_XTraction_Select);			
			
			helper.saveXML();
			break;
		}
		case Constants.leftMenuState_YTraction_Select:
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("YTraction")==null){
				node.addChildNode("YTraction",String.valueOf(data.yTraction));
				}
			else{
				node.getChild("YTraction").setValue(String.valueOf(data.yTraction));
				}			
			int i =loadPlayerCharacteristics(data.pathToProject,Constants.leftMenuState_YTraction_Select);			
			
			helper.saveXML();
			break;
		}
		default:
			break;
		}
		}
		else if(data.menuSelect == 1)//editobject
		{			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			if(node.getChild("ObjectList")==null)
			{				
				node.addChildNode("ObjectList");
				addObjectCharacteristics(pathToImage);
			}
			else
				addObjectCharacteristics(pathToImage);
			
		}
		else if(data.menuSelect == 2)//edit terrain
		{			
			
				addTerrainCharacteristics(pathToImage);
		}
	}
	
	public void addTerrainCharacteristics(String path)
	{
		boolean nameinlist = false;		
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserTerrainGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		XmlNodeHelper childNode = null;
		
		if(node.getChild("TerrainList") ==null){
			node.addChildNode("TerrainList");
		node = node.getChild("TerrainList");
		node = node.addChildNode("TerrainItem");
		node.addChildNode("Name",path);
		node.addChildNode("Traction",String.valueOf(data.Traction));
		helper.saveXML();
		}
		else {			
			node = node.getChild("TerrainList");
		
		NodeList NL = node.getAllChildren();
		for (int i = 0; i < NL.getLength(); ++i)
		{
			XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());
			if(tempNode.getChild("Name")!=null){
			if (tempNode.getChild("Name").getValue().contentEquals(path))
			{
				nameinlist = true;				
				tempNode.getChild("Traction").setValue(String.valueOf(data.Traction));
				helper.saveXML();
				break;
			}
			}// end if
		} // end for
		nameinlist = false;
		}
		if(!nameinlist){		
		node = node.addChildNode("TerrainItem");
		node.addChildNode("Name",path);
		node.addChildNode("Traction",String.valueOf(data.Traction));
		helper.saveXML();
		nameinlist = true;
		}

	}
	
	public void addObjectCharacteristics(String path)
	{		
		switch(data.leftMenuState)
		{ case Constants.leftMenuState_MaxHP_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("maxHP",String.valueOf(data.maxHP));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());				
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("maxHP")==null)
						tempNode.addChildNode("maxHP",String.valueOf(data.maxHP));
					else
					tempNode.getChild("maxHP").setValue(String.valueOf(data.maxHP));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			//node = node.getChild("TerrainList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("maxHP",String.valueOf(data.maxHP));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_Traction_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("Traction",String.valueOf(data.Traction));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());				
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("Traction")==null)
						tempNode.addChildNode("Traction",String.valueOf(data.Traction));
					else
					tempNode.getChild("Traction").setValue(String.valueOf(data.Traction));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			//node = node.getChild("TerrainList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("Traction",String.valueOf(data.Traction));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_MaxDefenseUp_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("DefenseUp",String.valueOf(data.DefenseUp));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());				
				if (tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("DefenseUp")==null)
						tempNode.addChildNode("DefenseUp",String.valueOf(data.DefenseUp));
					else
					tempNode.getChild("DefenseUp").setValue(String.valueOf(data.DefenseUp));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			//node = node.getChild("TerrainList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("DefenseUp",String.valueOf(data.DefenseUp));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_MaxDefenseDown_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("DefenseDown",String.valueOf(data.DefenseDown));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());				
				if (tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("DefenseDown")==null)
						tempNode.addChildNode("DefenseDown",String.valueOf(data.DefenseDown));
					else
					tempNode.getChild("DefenseDown").setValue(String.valueOf(data.DefenseDown));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			//node = node.getChild("TerrainList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("DefenseDown",String.valueOf(data.DefenseDown));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_MaxDefenseLeft_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("DefenseLeft",String.valueOf(data.DefenseLeft));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());				
				if (tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("DefenseLeft")==null)
						tempNode.addChildNode("DefenseLeft",String.valueOf(data.DefenseLeft));
					else
					tempNode.getChild("DefenseLeft").setValue(String.valueOf(data.DefenseLeft));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			//node = node.getChild("TerrainList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("DefenseLeft",String.valueOf(data.DefenseLeft));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_MaxDefenseRight_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("DefenseRight",String.valueOf(data.DefenseRight));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());				
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("DefenseRight")==null)
						tempNode.addChildNode("DefenseRight",String.valueOf(data.DefenseRight));
					else
					tempNode.getChild("DefenseRight").setValue(String.valueOf(data.DefenseRight));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			//node = node.getChild("TerrainList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("DefenseRight",String.valueOf(data.DefenseRight));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_MaxPowerUp_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("PowerUp",String.valueOf(data.PowerUp));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());				
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("PowerUp")==null)
						tempNode.addChildNode("PowerUp",String.valueOf(data.PowerUp));
					else
					tempNode.getChild("PowerUp").setValue(String.valueOf(data.PowerUp));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("PowerUp",String.valueOf(data.PowerUp));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_MaxPowerDown_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("PowerDown",String.valueOf(data.PowerDown));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());				
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("PowerDown")==null)
						tempNode.addChildNode("PowerDown",String.valueOf(data.PowerDown));
					else
					tempNode.getChild("PowerDown").setValue(String.valueOf(data.PowerDown));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("PowerDown",String.valueOf(data.PowerDown));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_MaxPowerLeft_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("PowerLeft",String.valueOf(data.PowerLeft));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());				
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("PowerLeft")==null)
						tempNode.addChildNode("PowerLeft",String.valueOf(data.PowerLeft));
					else
					tempNode.getChild("PowerLeft").setValue(String.valueOf(data.PowerLeft));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("PowerLeft",String.valueOf(data.PowerLeft));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_MaxPowerRight_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("PowerRight",String.valueOf(data.PowerRight));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());				
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("PowerRight")==null)
						tempNode.addChildNode("PowerRight",String.valueOf(data.PowerRight));
					else
					tempNode.getChild("PowerRight").setValue(String.valueOf(data.PowerRight));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("PowerRight",String.valueOf(data.PowerRight));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_MaxVelocityUp_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("VelocityUp",String.valueOf(data.VelocityUp));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());				
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("VelocityUp")==null)
						tempNode.addChildNode("VelocityUp",String.valueOf(data.VelocityUp));
					else
					tempNode.getChild("VelocityUp").setValue(String.valueOf(data.VelocityUp));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("VelocityUp",String.valueOf(data.VelocityUp));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_MaxVelocityDown_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("VelocityDown",String.valueOf(data.VelocityDown));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("VelocityDown")==null)
						tempNode.addChildNode("VelocityDown",String.valueOf(data.VelocityDown));
					else
					tempNode.getChild("VelocityDown").setValue(String.valueOf(data.VelocityDown));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("VelocityDown",String.valueOf(data.VelocityDown));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_MaxVelocityLeft_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("VelocityLeft",String.valueOf(data.VelocityLeft));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("VelocityLeft")==null)
						tempNode.addChildNode("VelocityLeft",String.valueOf(data.VelocityLeft));
					else
					tempNode.getChild("VelocityLeft").setValue(String.valueOf(data.VelocityLeft));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("VelocityLeft",String.valueOf(data.VelocityLeft));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_MaxVelocityRight_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("VelocityRight",String.valueOf(data.VelocityRight));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("VelocityRight")==null)
						tempNode.addChildNode("VelocityRight",String.valueOf(data.VelocityRight));
					else
					tempNode.getChild("VelocityRight").setValue(String.valueOf(data.VelocityRight));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("VelocityRight",String.valueOf(data.VelocityRight));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		
		case Constants.leftMenuState_VertGravity_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("vertGravity",String.valueOf(data.vertGravity));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("vertGravity")==null)
						tempNode.addChildNode("vertGravity",String.valueOf(data.vertGravity));
					else
					tempNode.getChild("vertGravity").setValue(String.valueOf(data.vertGravity));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("vertGravity",String.valueOf(data.vertGravity));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_HorzGravity_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("horzGravity",String.valueOf(data.horzGravity));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("horzGravity")==null)
						tempNode.addChildNode("horzGravity",String.valueOf(data.horzGravity));
					else
					tempNode.getChild("horzGravity").setValue(String.valueOf(data.horzGravity));
					helper.saveXML();
					break;
				} 
				}// end if
			} // end for
			}
			if(!nameinlist){			
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("horzGravity",String.valueOf(data.horzGravity));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_XTraction_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("xTraction",String.valueOf(data.xTraction));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("xTraction")==null)
						tempNode.addChildNode("xTraction",String.valueOf(data.xTraction));
					else
					tempNode.getChild("xTraction").setValue(String.valueOf(data.xTraction));
					helper.saveXML();
					break;
				}
				}// end if
			} // end for
			}
			if(!nameinlist){			
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("xTraction",String.valueOf(data.xTraction));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		case Constants.leftMenuState_YTraction_Select:
		{									
			boolean nameinlist = false;			
			XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
			XmlNodeHelper node = helper.getHelperNodeRoot();
			XmlNodeHelper childNode = null;
			
			if(node.getChild("ObjectList") ==null){
				node.addChildNode("ObjectList");
			node = node.getChild("ObjectList");
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("yTraction",String.valueOf(data.yTraction));
			helper.saveXML();
			}
			else {				
				node = node.getChild("ObjectList");
			
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());
				if(tempNode.getChild("Name")!=null){
				if (tempNode.getChild("Name").getValue().contentEquals(path))
				{
					nameinlist = true;					
					if(tempNode.getChild("yTraction")==null)
						tempNode.addChildNode("yTraction",String.valueOf(data.yTraction));
					else
					tempNode.getChild("yTraction").setValue(String.valueOf(data.yTraction));
					helper.saveXML();
					break;
				} 
				}// end if
			} // end for
			}
			if(!nameinlist){			
			node = node.addChildNode("ObjectItem");
			node.addChildNode("Name",path);
			node.addChildNode("yTraction",String.valueOf(data.yTraction));
			helper.saveXML();
			nameinlist = true;
			}
			break;
		}
		default:
			break;
		}
		//helper.saveXML();
	}
	
	
	// add a new column to the current level xml
	public void addNewLevelCol()
	{
		XmlHelper helper = new XmlHelper(data.currentLevelPath);
		XmlNodeHelper node = helper.getHelperNodeRoot();
		int colNum = Integer.valueOf(node.getChild("Cols").getValue());
		++colNum;
		node.getChild("Cols").setValue(String.valueOf(colNum));
		data.currentLevelData.incrementXSize();
		helper.saveXML();
		// add the col to the current level data
		int currentXSize = data.builderMapCursor.getHorizontalMax();
		int currentYSize = data.builderMapCursor.getVerticalMax();
		Vector<MapPiece> tempVec = new Vector<MapPiece>();
		for (int y = 0; y <= currentYSize; ++y){
			MapPiece tempMapPiece = new MapPiece();
			tempVec.add(tempMapPiece);
		} // end for
		data.currentLevelData.mapVec.add(tempVec);
		data.builderMapCursor.setNumberInListHorizontal(currentXSize + 2, false);
		eventHandler.moveCursorRight(data.builderMapCursor);
	}
	
	// adds a new row the current level xml
	public void addNewLevelRow()
	{
		XmlHelper helper = new XmlHelper(data.currentLevelPath);
		XmlNodeHelper node = helper.getHelperNodeRoot();
		int rowNum = Integer.valueOf(node.getChild("Rows").getValue());
		++rowNum;
		node.getChild("Rows").setValue(String.valueOf(rowNum));
		data.currentLevelData.incrementYSize();
		helper.saveXML();
		// add the row to the current level data
		int currentXSize = data.builderMapCursor.getHorizontalMax();
		int currentYSize = data.builderMapCursor.getVerticalMax();
		for (int x = 0; x <= currentXSize; ++x){
			MapPiece tempMapPiece = new MapPiece();
			data.currentLevelData.mapVec.get(x).add(tempMapPiece);
		} // end for
		data.builderMapCursor.setNumberInListVertical(currentYSize + 2, false);
		eventHandler.moveCursorDown(data.builderMapCursor);
	}
	
	public void optimizeLevelSize()
	{
		data.clearLevelData();
		XmlHelper helper = new XmlHelper(data.currentLevelPath);
		XmlNodeHelper node = helper.getHelperNodeRoot();
		node = node.getChild("ListOfMapItems");
		int xCount = Constants.window_GridSquareXRangeCount;
		int yCount = Constants.window_GridSquareYRangeCount;
		int tempX;
		int tempY;
		// Find The correct child node
		NodeList NL = node.getAllChildren();
		for (int i = 0; i < NL.getLength(); ++i)
		{
			XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());
			tempX = Integer.parseInt(tempNode.getChild("PosX").getValue());
			tempY = Integer.parseInt(tempNode.getChild("PosY").getValue());
			++tempX;
			++tempY;
			if (tempX > xCount) { xCount = tempX; }
			if (tempY > yCount) { yCount = tempY; }
		} // end for
		node = helper.getHelperNodeRoot();
		node.getChild("Rows").setValue(String.valueOf(yCount));
		node.getChild("Cols").setValue(String.valueOf(xCount));
		helper.saveXML();
	}
	
	public void addObjectData()
	{
		
	}
	// Refresh audio graphics and file links from xmls
	public void reloadAudioData() 
	{
		loadUserMusicGraphicVectors();
		loadUserSoundGraphicVectors();
 	}
	
// =================================================
//  PRIVATE METHODS
// =================================================
	
	// load all of the user added graphics from the xmls
	private void loadUserGraphicVectors()
	{
		loadUserBackgroundGraphicVectors();
		loadUserPlayerGraphicVectors();
		loadUserNPCGraphicVectors();
		loadUserObjectGraphicVectors();
		loadUserTerrainGraphicVectors();
		loadUserProjectileGraphicVectors();
		loadUserMusicGraphicVectors();
		loadUserSoundGraphicVectors();
	}	
	

	
	// load all of the level map items from the current level xml
	private void loadAllMapObjects()
	{
		XmlHelper helper = new XmlHelper(data.currentLevelPath);
		XmlNodeHelper node = helper.getHelperNodeRoot();
		node = node.getChild("ListOfMapItems");
		NodeList NL = node.getAllChildren();
		for (int i = 0; i < NL.getLength(); ++i)
		{
			XmlNodeHelper childNode = new XmlNodeHelper(NL.item(i), node.getDocument());
			int posX = Integer.valueOf(childNode.getChild("PosX").getValue());
			int posY = Integer.valueOf(childNode.getChild("PosY").getValue());
			GraphicObject tempGO = new GraphicObject();
			tempGO.setXmlPath(childNode.getChild("BackgroundLayer").getValue());
			if (!tempGO.getXmlPath().equalsIgnoreCase(""))
			{
				if (tempGO.getXmlPath().contains("."))
				{
					tempGO.setImage(startingClass.getImage(graphicPool.base, tempGO.getXmlPath()));
				} // end if
				else
				{
					tempGO.setImage(startingClass.getImage(graphicPool.base, tempGO.getXmlPath() + "/mainImage.png"));
				} // end else
				data.currentLevelData.mapVec.get(posX).get(posY).layer[Constants.layer_BackgroundObject].setImage(tempGO.getImage());
				data.currentLevelData.mapVec.get(posX).get(posY).layer[Constants.layer_BackgroundObject].setXmlPath(tempGO.getXmlPath());
				data.currentLevelData.mapVec.get(posX).get(posY).setUsed(true);
			} // end if
			tempGO.setXmlPath(childNode.getChild("MainLayer").getValue());
			if (!tempGO.getXmlPath().equalsIgnoreCase(""))
			{
				// find and set the player positions
				if (tempGO.getXmlPath().equalsIgnoreCase(Constants.graphicPool_BuilderMenuP1))
				{
					data.p1_X = posX;
					data.p1_Y = posY;
				}
				/*else if (tempGO.getXmlPath().equalsIgnoreCase(Constants.graphicPool_BuilderMenuP2))
				{
					data.p2_X = posX;
					data.p2_Y = posY;
				}
				else if (tempGO.getXmlPath().equalsIgnoreCase(Constants.graphicPool_BuilderMenuP3))
				{
					data.p3_X = posX;
					data.p3_Y = posY;
				}
				else if (tempGO.getXmlPath().equalsIgnoreCase(Constants.graphicPool_BuilderMenuP4))
				{
					data.p4_X = posX;
					data.p4_Y = posY;
				}*/
				if (tempGO.getXmlPath().contains("."))
				{
					tempGO.setImage(startingClass.getImage(graphicPool.base, tempGO.getXmlPath()));
				} // end if
				else
				{
					tempGO.setImage(startingClass.getImage(graphicPool.base, tempGO.getXmlPath() + "/mainImage.png"));
				} // end else
				data.currentLevelData.mapVec.get(posX).get(posY).layer[Constants.layer_MainObject].setImage(tempGO.getImage());
				data.currentLevelData.mapVec.get(posX).get(posY).layer[Constants.layer_MainObject].setXmlPath(tempGO.getXmlPath());
				data.currentLevelData.mapVec.get(posX).get(posY).setUsed(true);
			} // end if
			tempGO.setXmlPath(childNode.getChild("ForegroundLayer").getValue());
			if (!tempGO.getXmlPath().equalsIgnoreCase(""))
			{
				if (tempGO.getXmlPath().contains("."))
				{
					tempGO.setImage(startingClass.getImage(graphicPool.base, tempGO.getXmlPath()));
				} // end if
				else
				{
					tempGO.setImage(startingClass.getImage(graphicPool.base, tempGO.getXmlPath() + "/mainImage.png"));
				} // end else
				data.currentLevelData.mapVec.get(posX).get(posY).layer[Constants.layer_ForegroundObject].setImage(tempGO.getImage());
				data.currentLevelData.mapVec.get(posX).get(posY).layer[Constants.layer_ForegroundObject].setXmlPath(tempGO.getXmlPath());
				data.currentLevelData.mapVec.get(posX).get(posY).setUsed(true);
			} // end if
			tempGO.setXmlPath(childNode.getChild("EventLayer").getValue());
			if (!tempGO.getXmlPath().equalsIgnoreCase(""))
			{
				if (tempGO.getXmlPath().contains("."))
				{
					tempGO.setImage(startingClass.getImage(graphicPool.base, tempGO.getXmlPath()));
				} // end if
				else
				{
					tempGO.setImage(startingClass.getImage(graphicPool.base, tempGO.getXmlPath() + "/mainImage.png"));
				} // end else
				data.currentLevelData.mapVec.get(posX).get(posY).layer[Constants.layer_WinLoseEvent].setImage(tempGO.getImage());
				data.currentLevelData.mapVec.get(posX).get(posY).layer[Constants.layer_WinLoseEvent].setXmlPath(tempGO.getXmlPath());
				data.currentLevelData.mapVec.get(posX).get(posY).setUsed(true);
			} // end if
		} // end for
	}
	
	// finish saving a map item to the current level
	private void finishSavingMapItem(XmlHelper helper, XmlNodeHelper childNode, int xPos, int yPos)
	{
		GraphicObject tempGO = new GraphicObject();
		switch (data.leftMenuState)
		{
		case Constants.leftMenuState_FourPlayers:
			tempGO.setImage(graphicPool.leftMenu_ManagePlayers[data.leftMenuCursor.getIndexVertical()]);
			switch (data.leftMenuCursor.getIndexVertical())
			{
			case Constants.leftMenuList_ManagePlayersP1:
				tempGO.setXmlPath(Constants.graphicPool_BuilderMenuP1);
				if (data.p1_X != -1 || data.p1_Y != -1) { erasePlayerItemFromLevelData(helper, Constants.leftMenuList_ManagePlayersP1); }
				data.p1_X = xPos;
				data.p1_Y = yPos;
				break;
			/*case Constants.leftMenuList_ManagePlayersP2:
				tempGO.setXmlPath(Constants.graphicPool_BuilderMenuP2);
				if (data.p2_X != -1 || data.p2_Y != -1) { erasePlayerItemFromLevelData(helper, Constants.leftMenuList_ManagePlayersP2); }
				data.p2_X = xPos;
				data.p2_Y = yPos;
				break;
			case Constants.leftMenuList_ManagePlayersP3:
				tempGO.setXmlPath(Constants.graphicPool_BuilderMenuP3);
				if (data.p3_X != -1 || data.p3_Y != -1) { erasePlayerItemFromLevelData(helper, Constants.leftMenuList_ManagePlayersP3); }
				data.p3_X = xPos;
				data.p3_Y = yPos;
				break;
			case Constants.leftMenuList_ManagePlayersP4:
				tempGO.setXmlPath(Constants.graphicPool_BuilderMenuP4);
				if (data.p4_X != -1 || data.p4_Y != -1) { erasePlayerItemFromLevelData(helper, Constants.leftMenuList_ManagePlayersP4); }
				data.p4_X = xPos;
				data.p4_Y = yPos;
				break;*/
			default: 
				// ignore invalid cases
				break;
			} // end switch
			break;
		/*
		case Constants.leftMenuState_ListUserNPCs:
			tempGO = graphicPool.leftMenu_UserNPCs.get(data.leftMenuCursor.getIndexVertical());
			break;
		*/
		case Constants.leftMenuState_ListUserObjects:
			tempGO = graphicPool.leftMenu_UserObjects.get(data.leftMenuCursor.getIndexVertical());
			break;
		case Constants.leftMenuState_ListUserTerrains:
			tempGO = graphicPool.leftMenu_UserTerrains.get(data.leftMenuCursor.getIndexVertical());
			break;
		default: 
			// ignore invalid cases
			break;
		} // end switch
		data.currentLevelData.mapVec.get(xPos).get(yPos).setUsed(true);
		data.currentLevelData.mapVec.get(xPos).get(yPos).layer[data.currentLayer] = tempGO;
		switch (data.currentLayer)
		{
		case Constants.layer_BackgroundObject:
			childNode = childNode.getChild("BackgroundLayer");
			break;
		case Constants.layer_MainObject:
			childNode = childNode.getChild("MainLayer");
			break;
		case Constants.layer_ForegroundObject:
			childNode = childNode.getChild("ForegroundLayer");
			break;
		case Constants.layer_WinLoseEvent:
			childNode = childNode.getChild("EventLayer");
			break;
		default:
			// ignore invalid cases
			break;
		} // end switch
		if (childNode != null)
		{
			childNode.setValue(tempGO.getXmlPath());
		} // end if
		helper.saveXML();
	}
	
	// removes a specified player from the level xml
	public void erasePlayerItemFromLevelData(XmlHelper helper, int playerNum)
	{
		XmlNodeHelper node = helper.getHelperNodeRoot();
		XmlNodeHelper childNode = null;
		node = node.getChild("ListOfMapItems");
		int xPos = -1;
		int yPos = -1;
		switch (data.leftMenuCursor.getIndexVertical())
		{
		case Constants.leftMenuList_ManagePlayersP1:
			xPos = data.p1_X;
			yPos = data.p1_Y;
			break;
		/*case Constants.leftMenuList_ManagePlayersP2:
			xPos = data.p2_X;
			yPos = data.p2_Y;
			break;
		case Constants.leftMenuList_ManagePlayersP3:
			xPos = data.p3_X;
			yPos = data.p3_Y;
			break;
		case Constants.leftMenuList_ManagePlayersP4:
			xPos = data.p4_X;
			yPos = data.p4_Y;
			break;*/
		default: 
			// ignore invalid cases
			break;
		} // end switch
		// Find The correct child node
		NodeList NL = node.getAllChildren();
		for (int i = 0; i < NL.getLength(); ++i)
		{
			XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());
			if (tempNode.getChild("PosX").getValue().equalsIgnoreCase(String.valueOf(xPos)) &&
					tempNode.getChild("PosY").getValue().equalsIgnoreCase(String.valueOf(yPos)))
			{
				childNode = tempNode;
				break;
			} // end if
		} // end for
		if (childNode != null)
		{
			XmlNodeHelper tmpNode = childNode.getChild("MainLayer");
			tmpNode.setValue("");
			data.currentLevelData.mapVec.get(xPos).get(yPos).layer[Constants.layer_MainObject] = null;
			// now check to see if this location has any items at all
			boolean allErased = true;
			if (!childNode.getChild("BackgroundLayer").getValue().equals("")) { allErased = false; }
			if (!childNode.getChild("MainLayer").getValue().equals("")) { allErased = false; }
			if (!childNode.getChild("ForegroundLayer").getValue().equals("")) { allErased = false; }
			if (!childNode.getChild("EventLayer").getValue().equals("")) { allErased = false; }
			if (allErased)
			{
				node.removeChildNode(childNode);
				data.currentLevelData.mapVec.get(xPos).get(yPos).setUsed(false);
			}
		}
	}
	
	// load the added user background images from the xml
	private void loadUserBackgroundGraphicVectors()
	{
		graphicPool.leftMenu_UserLevelBackdrops.clear();
		graphicPool.leftMenu_UserLevelBackdropsExtra.clear();
		graphicPool.leftMenu_UserLevelBackdropsExtra.add(graphicPool.newButtonGO);
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserLevelBackgroundGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		String imagePath;
		for (int i = 0; i < node.getActualNode().getChildNodes().getLength(); ++i)
		{
			imagePath = node.getActualNode().getChildNodes().item(i).getTextContent();
			GraphicObject tempObject = new GraphicObject();
			tempObject.setXmlPath(imagePath);
			Image tempImage = startingClass.getImage(graphicPool.base, imagePath);
			tempObject.setImage(tempImage);
			graphicPool.leftMenu_UserLevelBackdrops.add(tempObject);
			graphicPool.leftMenu_UserLevelBackdropsExtra.add(tempObject);
		} // end for
	}
	
	// load the added user player images from the xml
	private void loadUserPlayerGraphicVectors()
	{
		graphicPool.leftMenu_UserPlayers.clear();
		graphicPool.leftMenu_UserPlayersExtra.clear();
		graphicPool.leftMenu_UserPlayersExtra.add(graphicPool.newButtonGO);
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserPlayerGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		String imagePath;
		for (int i = 0; i < node.getActualNode().getChildNodes().getLength(); ++i)
		{
			imagePath = node.getActualNode().getChildNodes().item(i).getTextContent();
			GraphicObject tempObject = new GraphicObject();
			tempObject.setXmlPath(imagePath);
			Image tempImage = startingClass.getImage(graphicPool.base, imagePath + "/mainImage.png");
			tempObject.setImage(tempImage);
			graphicPool.leftMenu_UserPlayers.add(tempObject);
			graphicPool.leftMenu_UserPlayersExtra.add(tempObject);
		} // end for
	}
	
	// load the added user NPC images from the xml
	private void loadUserNPCGraphicVectors()
	{
		graphicPool.leftMenu_UserNPCs.clear();
		graphicPool.leftMenu_UserNPCsExtra.clear();
		graphicPool.leftMenu_UserNPCsExtra.add(graphicPool.newButtonGO);
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserNPCGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		String imagePath;
		for (int i = 0; i < node.getActualNode().getChildNodes().getLength(); ++i)
		{
			imagePath = node.getActualNode().getChildNodes().item(i).getTextContent();
			GraphicObject tempObject = new GraphicObject();
			tempObject.setXmlPath(imagePath);
			Image tempImage = startingClass.getImage(graphicPool.base, imagePath + "/mainImage.png");
			tempObject.setImage(tempImage);
			graphicPool.leftMenu_UserNPCs.add(tempObject);
			graphicPool.leftMenu_UserNPCsExtra.add(tempObject);
		} // end for
	}
	
	// load the added user object images from the xml
	private void loadUserObjectGraphicVectors()
	{
		graphicPool.leftMenu_UserObjects.clear();
		graphicPool.leftMenu_UserObjectsExtra.clear();
		graphicPool.leftMenu_UserObjectsExtra.add(graphicPool.newButtonGO);
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserObjectGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		String imagePath;
		for (int i = 0; i < node.getActualNode().getChildNodes().getLength(); ++i)
		{
			imagePath = node.getActualNode().getChildNodes().item(i).getTextContent();
			GraphicObject tempObject = new GraphicObject();
			tempObject.setXmlPath(imagePath);
			Image tempImage = startingClass.getImage(graphicPool.base, imagePath + "/mainImage.png");
			tempObject.setImage(tempImage);
			graphicPool.leftMenu_UserObjects.add(tempObject);
			graphicPool.leftMenu_UserObjectsExtra.add(tempObject);
		} // end for
	}
	
	// load the added user terrain images from the xml
	private void loadUserTerrainGraphicVectors()
	{
		graphicPool.leftMenu_UserTerrains.clear();
		graphicPool.leftMenu_UserTerrainsExtra.clear();
		graphicPool.leftMenu_UserTerrainsExtra.add(graphicPool.newButtonGO);
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserTerrainGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		String imagePath;
		for (int i = 0; i < node.getActualNode().getChildNodes().getLength(); ++i)
		{
			imagePath = node.getActualNode().getChildNodes().item(i).getTextContent();
			GraphicObject tempObject = new GraphicObject();
			tempObject.setXmlPath(imagePath);
			Image tempImage = startingClass.getImage(graphicPool.base, imagePath);
			tempObject.setImage(tempImage);
			graphicPool.leftMenu_UserTerrains.add(tempObject);
			graphicPool.leftMenu_UserTerrainsExtra.add(tempObject);
		} // end for
	}
	
	// load the added user projectile images from the xml
	private void loadUserProjectileGraphicVectors()
	{
		graphicPool.leftMenu_UserProjectiles.clear();
		graphicPool.leftMenu_UserProjectilesExtra.clear();
		graphicPool.leftMenu_UserProjectilesExtra.add(graphicPool.newButtonGO);
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserProjectileGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		String imagePath;
		for (int i = 0; i < node.getActualNode().getChildNodes().getLength(); ++i)
		{
			imagePath = node.getActualNode().getChildNodes().item(i).getTextContent();
			GraphicObject tempObject = new GraphicObject();
			tempObject.setXmlPath(imagePath);
			Image tempImage = startingClass.getImage(graphicPool.base, imagePath + "/mainImage.png");
			tempObject.setImage(tempImage);
			graphicPool.leftMenu_UserProjectiles.add(tempObject);
			graphicPool.leftMenu_UserProjectilesExtra.add(tempObject);
		} // end for
	}
	
	// load the added user music from the xml
	private void loadUserMusicGraphicVectors()
	{
		graphicPool.leftMenu_UserMusic.clear();
		graphicPool.leftMenu_UserMusicExtra.clear();
		graphicPool.leftMenu_UserMusicExtra.add(graphicPool.newButtonGO);
		audioPool.leftMenu_User_BGMusic.clear();
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserMusicGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		String imagePath;
		for (int i = 0; i < node.getActualNode().getChildNodes().getLength(); ++i)
		{
			imagePath = node.getActualNode().getChildNodes().item(i).getTextContent();
			GraphicObject tempObject = new GraphicObject();
			tempObject.setXmlPath(imagePath);
			Image tempImage = graphicPool.blankMenuItem;
			tempObject.setImage(tempImage);
			graphicPool.leftMenu_UserMusic.add(tempObject);
			graphicPool.leftMenu_UserMusicExtra.add(tempObject);
			AudioObject tempAudio = new AudioObject();
			tempAudio.setAudio(startingClass.getAudioClip(base, imagePath));
			audioPool.leftMenu_User_BGMusic.add(i,tempAudio);
		} // end for
	}
	
	// load the added user sounds from the xml
	private void loadUserSoundGraphicVectors()
	{
		graphicPool.leftMenu_UserSounds.clear();
		graphicPool.leftMenu_UserSoundsExtra.clear();
		graphicPool.leftMenu_UserSoundsExtra.add(graphicPool.newButtonGO);
		XmlHelper helper = new XmlHelper(data.pathToProject + "UserSoundGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		String imagePath;
		for (int i = 0; i < node.getActualNode().getChildNodes().getLength(); ++i)
		{
			imagePath = node.getActualNode().getChildNodes().item(i).getTextContent();
			GraphicObject tempObject = new GraphicObject();
			tempObject.setXmlPath(imagePath);
			Image tempImage = graphicPool.blankMenuItem;
			tempObject.setImage(tempImage);
			graphicPool.leftMenu_UserSounds.add(tempObject);
			graphicPool.leftMenu_UserSoundsExtra.add(tempObject);
			AudioObject tempAudio = new AudioObject();
			tempAudio.setAudio(startingClass.getAudioClip(base, imagePath));
			audioPool.leftMenu_User_SoundFX.add(i,tempAudio);
		} // end for
	}
	
	public int loadPlayerCharacteristics(String path, int value)
	{
		//String t = data.pathToProject;
		//path = data.pathToProject;		
		XmlHelper helper = new XmlHelper(path+"UserPlayerGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		//int v  = Integer.valueOf(value);
		switch(value)
		{
		case Constants.leftMenuState_MaxHP_Select:
			if(node.getChild("HpPath")==null)
					return 50;
			else
			return Integer.valueOf(node.getChild("HpPath").getValue());
		case Constants.leftMenuState_MaxAcceleration_Select:
			if(node.getChild("MaxAcceleration")==null)
				return 1;
		else
			return Integer.valueOf(node.getChild("MaxAcceleration").getValue());
			//return ret;
		case Constants.leftMenuState_Traction_Select:
			if(node.getChild("Traction")==null)
				return 80;
		else
			return Integer.valueOf(node.getChild("Traction").getValue());
			//return ret;
		case Constants.leftMenuState_VertGravity_Select:
			if(node.getChild("VertGravity")==null)
				return 1;
		else if(Boolean.valueOf(node.getChild("VertGravity").getValue()))
			return 1;
			else
				return 0;
			//return ret;
		case Constants.leftMenuState_HorzGravity_Select:
			if(node.getChild("HorzGravity")==null)
				return 1;
		else if(Boolean.valueOf(node.getChild("HorzGravity").getValue()))
			return 1;
			else
				return 0;
		case Constants.leftMenuState_XTraction_Select:
			if(node.getChild("XTraction")==null)
				return 1;
		else if(Boolean.valueOf(node.getChild("XTraction").getValue()))
			return 1;
			else
				return 0;
		case Constants.leftMenuState_YTraction_Select:
			if(node.getChild("YTraction")==null)
				return 1;
		else if(Boolean.valueOf(node.getChild("YTraction").getValue()))
			return 1;
			else
				return 0;
		case Constants.leftMenuState_MaxDefenseUp_Select:
			if(node.getChild("DefenseUp")==null)
				return 15;
		else
			return Integer.valueOf(node.getChild("DefenseUp").getValue());
			//return ret;
		case Constants.leftMenuState_MaxDefenseDown_Select:
			if(node.getChild("DefenseDown")==null)
				return 15;
		else
			return Integer.valueOf(node.getChild("DefenseDown").getValue());
			//return ret;
		case Constants.leftMenuState_MaxDefenseLeft_Select:
			if(node.getChild("DefenseLeft")==null)
				return 15;
		else
			return Integer.valueOf(node.getChild("DefenseLeft").getValue());
			//return ret;
		case Constants.leftMenuState_MaxDefenseRight_Select:
			if(node.getChild("DefenseRight")==null)
				return 15;
		else
			return Integer.valueOf(node.getChild("DefenseRight").getValue());
			//return ret;
		case Constants.leftMenuState_MaxPowerUp_Select:
			if(node.getChild("PowerUp")==null)
				return 15;
		else
			return Integer.valueOf(node.getChild("PowerUp").getValue());
			//return ret;
		case Constants.leftMenuState_MaxPowerDown_Select:
			if(node.getChild("PowerDown")==null)
				return 15;
		else
			return Integer.valueOf(node.getChild("PowerDown").getValue());
			//return ret;
		case Constants.leftMenuState_MaxPowerLeft_Select:
			if(node.getChild("PowerLeft")==null)
				return 15;
		else
			return Integer.valueOf(node.getChild("PowerLeft").getValue());
			//return ret;
		case Constants.leftMenuState_MaxPowerRight_Select:
			if(node.getChild("PowerRight")==null)
				return 15;
		else
			return Integer.valueOf(node.getChild("PowerRight").getValue());
			//return ret;
		case Constants.leftMenuState_MaxVelocityUp_Select:
			if(node.getChild("VelocityUp")==null)
				return 15;
		else
			return Integer.valueOf(node.getChild("VelocityUp").getValue());
			//return ret;
		case Constants.leftMenuState_MaxVelocityDown_Select:
			if(node.getChild("VelocityUp")==null)
				return 15;
		else
			return Integer.valueOf(node.getChild("VelocityUp").getValue());
			//return ret;
		case Constants.leftMenuState_MaxVelocityLeft_Select:
			if(node.getChild("VelocityLeft")==null)
				return 15;
		else
			return Integer.valueOf(node.getChild("VelocityLeft").getValue());
			//return ret;
		case Constants.leftMenuState_MaxVelocityRight_Select:
			if(node.getChild("VelocityLeft")==null)
				return 15;
		else
			return Integer.valueOf(node.getChild("VelocityLeft").getValue());
			//return ret;
		default:
			return 1;
			
		}
	}
	public int loadObjectCharacteristics(String path, String value,String object)
	{

		int ret =10;
		//String r = "0";						
		boolean nameinlist = false;		
		XmlHelper helper = new XmlHelper(path + "UserObjectGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		XmlNodeHelper childNode = null;		
		if(node.getChild("ObjectList")!=null)
		{
			node = node.getChild("ObjectList");
	
			NodeList NL = node.getAllChildren();
			for (int i = 0; i < NL.getLength(); ++i)
			{
				XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());
				if(tempNode.getChild("Name")!=null)
				{
					if (tempNode.getChild("Name").getValue().contentEquals(object))
					{
						nameinlist = true;				
						if(tempNode.getChild(value)==null)
						{
							if(value.contentEquals("xTraction")||value.contentEquals("yTraction")||
									value.contentEquals("vertGravity")||value.contentEquals("horzGravity"))
								return 1;
							else if(value.contentEquals("Traction"))
								return 80;
							else
								return 10;
						}
						else
						{
							if(value.contentEquals("xTraction")||value.contentEquals("yTraction")||
									value.contentEquals("vertGravity")||value.contentEquals("horzGravity"))
							{
								if(tempNode.getChild(value).getValue().contentEquals("true"))
									ret =1;
								else 
									ret =0;
							}
							else
								ret = Integer.valueOf(tempNode.getChild(value).getValue());
						}
					 
				//helper.saveXML();
						break;
					}
				}
				else if(value.contentEquals("Traction"))
						return 80;
					else
						return 10;
				
			// end for
			}
			if(!nameinlist)
			{
				if(value.contentEquals("xTraction")||value.contentEquals("yTraction")||
						value.contentEquals("vertGravity")||value.contentEquals("horzGravity"))
					return 1;
			}
		}
		else if(value.contentEquals("Traction"))
				return 80;
		else if(value.contentEquals("xTraction")||value.contentEquals("yTraction")||
				value.contentEquals("vertGravity")||value.contentEquals("horzGravity"))
			return 1;
		
		
		//System.out.println("Else 4");
	return ret;
	}
	
	public int loadTerrainCharacteristics(String path,String object)
	{
		int ret =80;
		//String r = "0";
		//System.out.println("in y case");
		//System.out.println(data.pathToProject);
		//System.out.println(path);
		boolean nameinlist = false;
		//System.out.println(path);
		XmlHelper helper = new XmlHelper(path + "UserTerrainGraphics.xml");
		XmlNodeHelper node = helper.getHelperNodeRoot();
		XmlNodeHelper childNode = null;
		//System.out.println("in elsse");
		if(node.getChild("TerrainList")!=null){
		node = node.getChild("TerrainList");
	
		NodeList NL = node.getAllChildren();
		for (int i = 0; i < NL.getLength(); ++i)
			{
			XmlNodeHelper tempNode = new XmlNodeHelper(NL.item(i), node.getDocument());
			//System.out.println("in for " + tempNode.getChild("Name").getValue());
			if(tempNode.getChild("Name")!=null){
			if (tempNode.getChild("Name").getValue().contentEquals(object))
			{
				nameinlist = true;
				//System.out.println("in if " );
				if(tempNode.getChild("Traction")==null)
					return 80;
					else
					ret = Integer.valueOf(tempNode.getChild("Traction").getValue());
				
					 
				//helper.saveXML();
				break;
			}
			}
			else
				return 80;// end if
			// end for
			}
		}
	
	
	return ret;
	}

}
