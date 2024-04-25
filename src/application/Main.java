package application;


import engine.Game;
import exceptions.*;
import model.characters.*;
import model.collectibles.*;
import model.world.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.characters.Hero;
import model.collectibles.Vaccine;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class Main extends Application{
	Hero chosen;
	Hero Playing;
	Hero Target;
	Zombie zombie;
	Stage window;
	Scene MainMenu, Characterselect;
	GridPane grid = new GridPane();
	GridPane grid2 = new GridPane();
	GridPane Map = new GridPane();
	Button chB = new Button("");
	Button Attack = new Button("");
	Button Cure = new Button("");
	Button EndTurn = new Button("");
	Button Special = new Button("");
	Label Name1 = new Label("");
	Label Health1 = new Label("Health: ");
	Label Actions = new Label("Actions :");
	Label Type1 = new Label("Type: ");
	Label aDmg = new Label("Attack effect: 0");
	Label Vac = new Label("Vaccines Available: 0");
	Label Sup = new Label("Supplies Available: 0");
	public static boolean Ult;

	public static void main(String [] args) {
		launch(args);		
	}

	@Override
	public void start(Stage primary) throws Exception {
		try {
			Game.loadHeroes("Heroes.csv");
		}catch(Exception e) {

		}


		Label Characters = new Label("Hero Selection");
		Label Name = new Label("Name:");
		Label Type = new Label("Type:");
		Label maxActions = new Label("Maximum Actions: ");
		Label Health = new Label("Health: ");
		Label AttackDmg = new Label("Attack Damage: ");
		window = primary;
		window.setTitle("The Last Of Us - Legacy");

		Button startgame = new Button("Start Game");
		startgame.setOnAction(e -> {
			window.setScene(Characterselect);
			window.setFullScreen(true);
			window.setResizable(true);
		});


		Button Exit = new Button("Exit");
		Exit.setOnAction(e -> closeProgram());
		window.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});

		//Layout

		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(30,30,30,30));
		GridPane.setConstraints(startgame, 5, 30);
		GridPane.setConstraints(Exit, 5, 40);
		grid.getChildren().addAll(startgame,Exit);
		MainMenu = new Scene(grid);

		Map.setPadding(new Insets(0,0,0,0));
		Map.setPrefWidth(1200);
		Map.setPrefHeight(1200);
		Map.setHgap(0);
		Map.setVgap(0);
		Map.setAlignment(Pos.CENTER);
		for (int i = 0 ; i<15 ; i++) {
			for (int j = 0 ; j<15 ; j++) {

				Button CellB = new Button("");
				Image Cell = new Image(getClass().getResourceAsStream("Cell2.jpg"));
				ImageView CellV = new ImageView();
				CellB.setPrefSize(60, 60);
				CellB.setMaxSize(60, 60);
				CellV.setImage(Cell);
				CellV.setFitWidth(CellB.getPrefWidth());
				CellV.setFitHeight(CellB.getPrefHeight());
				CellB.setGraphic(CellV);
				CellB.setStyle("-fx-background-color: transparent; -fx-padding: 1;");
				Map.add(CellB, i, j);
			}
		} 	
		Image mapimg = new Image("Scene3.jpg"); 
		BackgroundImage mapk = new BackgroundImage(mapimg, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO , false, false, false, true));
		Map.setBackground(new Background(mapk));

		VBox vbox = new VBox(2);
		vbox.setSpacing(3);	
		vbox.setAlignment(Pos.CENTER);
		VBox vbox2 = new VBox(2);
		vbox2.setSpacing(20);	
		vbox2.setAlignment(Pos.CENTER);

		Image atk = new Image(getClass().getResourceAsStream("attack.png"));
		ImageView atkV = new ImageView(atk);
		Attack.setPrefSize(80, 80);
		atkV.setFitWidth(Attack.getPrefWidth());
		atkV.setFitHeight(Attack.getPrefHeight());
		Attack.setGraphic(atkV);
		Attack.setStyle("-fx-background-color: transparent; -fx-padding: 1;");

		Attack.setOnAction(e -> {
			try{	Playing.getTarget();
			Playing.attack();
			updateMap();
			updateData(Playing);
			GameOver();

			}catch(Exception ex) {
				ErrorMessage.display(ex.getMessage());
			}
		});

		Image cur = new Image(getClass().getResourceAsStream("Cure.png"));
		ImageView curV = new ImageView(cur);
		Cure.setPrefSize(80, 80);
		curV.setFitWidth(Attack.getPrefWidth());
		curV.setFitHeight(Attack.getPrefHeight());
		Cure.setGraphic(curV);
		Cure.setStyle("-fx-background-color: transparent; -fx-padding: 1;");

		Cure.setOnAction(e -> {
			try{	
				Playing.setTarget(zombie);
				Playing.cure();
				updateMap();
				GameWon();
				GameOver();

			}catch(Exception ex) {
				ErrorMessage.display(ex.getMessage());
			}
		});

		Image end = new Image(getClass().getResourceAsStream("End.png"));
		ImageView endV = new ImageView(end);
		EndTurn.setPrefSize(80, 80);
		endV.setFitWidth(Attack.getPrefWidth());
		endV.setFitHeight(Attack.getPrefHeight());
		EndTurn.setGraphic(endV);
		EndTurn.setStyle("-fx-background-color: transparent; -fx-padding: 1;");
		EndTurn.setOnAction(e->{
			try {
				Game.endTurn();
				updateMap();
				updateData(Playing);
				GameOver();

			}catch(Exception ex) {
				ErrorMessage.display(ex.getMessage());
			}

		});

		Image spe = new Image(getClass().getResourceAsStream("Special.png"));
		ImageView speV = new ImageView(spe);
		Special.setPrefSize(80, 80);
		speV.setFitWidth(Attack.getPrefWidth());
		speV.setFitHeight(Attack.getPrefHeight());
		Special.setGraphic(speV);
		Special.setStyle("-fx-background-color: transparent; -fx-padding: 1;");
		Special.setOnAction(e -> {
			try{
				if (Playing instanceof Medic && Target == null)
					Playing.useSpecial();
				if (Playing instanceof Medic && Target != null)
					Playing.setTarget(Target);
					Playing.useSpecial();
				updateMap();
				updateData(Playing);
				updateData(Target);
			}catch(Exception ex) {
				ErrorMessage.display(ex.getMessage());
			}
		});
		

		vbox2.getChildren().addAll(Attack,Cure,EndTurn,Special);
		vbox.getChildren().addAll(Name1,Type1,Health1,aDmg,Actions,Vac,Sup);
		BorderPane border = new BorderPane();

		border.setCenter(Map);
		border.setLeft(vbox);
		border.setRight(vbox2);
		border.setPadding(new Insets(20,20,20,20));
		Image borderimg = new Image("Scene3.jpg"); 
		BackgroundImage borderk = new BackgroundImage(borderimg, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO , false, false, false, true));
		border.setBackground(new Background(borderk));
		Scene game = new Scene(border);


		Label NameT = new Label();
		Label TypeT= new Label();
		Label maxActionsT = new Label();
		Label HealthT = new Label();
		Label AttackDmgT = new Label();


		Characters.setStyle("-fx-font-size: 40px;");
		Button GoBack = new Button("Back");
		GoBack.setOnAction(e -> {
			window.setScene(MainMenu);
			window.setFullScreen(true);
			window.setResizable(true);
		});



		Button JoelB = new Button("");
		JoelB.setPrefSize(180, 180);
		Image Joel = new Image(getClass().getResourceAsStream("Joel.jpg"));
		ImageView JoelV = new ImageView();
		JoelV.setImage(Joel);
		JoelV.setFitWidth(JoelB.getPrefWidth());
		JoelV.setFitHeight(JoelB.getPrefHeight());
		JoelB.setGraphic(JoelV);

		JoelB.setOnAction(e ->{
			
			chosen = Game.availableHeroes.get(0);
			Name1.setText("Name: " + Game.availableHeroes.get(0).getName());
			Type1.setText("Type: Fighter");
			Health1.setText("Health: " + Game.availableHeroes.get(0).getCurrentHp() );
			Actions.setText("Available Actions: " + Game.availableHeroes.get(0).getActionsAvailable());
			aDmg.setText("Attack: " + Game.availableHeroes.get(0).getAttackDmg());
			Vac.setText("Vaccines Available: " + Game.availableHeroes.get(0).getVaccineInventory().size());
			Sup.setText("Supplies Available: " + Game.availableHeroes.get(0).getSupplyInventory().size());
			Game.startGame(Game.availableHeroes.get(0));
			updateMap();
			window.setScene(game);
			window.setFullScreen(true);
			window.setResizable(true);
		} );


		//Ellie Button
		Button EllieB = new Button("");
		EllieB.setPrefSize(180, 180);
		Image Ellie = new Image(getClass().getResourceAsStream("Ellie.jpg"));
		ImageView EllieV = new ImageView();
		EllieV.setImage(Ellie);
		EllieV.setFitWidth(EllieB.getPrefWidth());
		EllieV.setFitHeight(EllieB.getPrefHeight());
		EllieB.setGraphic(EllieV);

		EllieB.setOnAction(e ->{
			
			chosen = Game.availableHeroes.get(1);
			Name1.setText("Name: " + Game.availableHeroes.get(1).getName());
			Type1.setText("Type: Medic");
			Health1.setText("Health: " + Game.availableHeroes.get(1).getCurrentHp() );
			Actions.setText("Available Actions: " + Game.availableHeroes.get(1).getActionsAvailable());
			aDmg.setText("Attack: " + Game.availableHeroes.get(1).getAttackDmg());
			Vac.setText("Vaccines Available: " + Game.availableHeroes.get(1).getVaccineInventory().size());
			Sup.setText("Supplies Available: " + Game.availableHeroes.get(1).getSupplyInventory().size());
			Game.startGame(Game.availableHeroes.get(1));
			updateMap();
			window.setScene(game);
			window.setFullScreen(true);
			window.setResizable(true);
		} );

		//Tommy Button
		Button TommyB = new Button("");
		TommyB.setPrefSize(180, 180);
		Image Tommy = new Image(getClass().getResourceAsStream("Tommy.jpg"));
		ImageView TommyV = new ImageView();
		TommyV.setImage(Tommy);
		TommyV.setFitWidth(TommyB.getPrefWidth());
		TommyV.setFitHeight(TommyB.getPrefHeight());
		TommyB.setGraphic(TommyV);

		TommyB.setOnAction(e ->{
			
			chosen = Game.availableHeroes.get(4);

			Name1.setText("Name: " + Game.availableHeroes.get(4).getName());
			Type1.setText("Type: Explorer");
			Health1.setText("Health: " + Game.availableHeroes.get(4).getCurrentHp() );
			Actions.setText("Available Actions: " + Game.availableHeroes.get(4).getActionsAvailable());
			aDmg.setText("Attack: " + Game.availableHeroes.get(4).getAttackDmg());
			Vac.setText("Vaccines Available: " + Game.availableHeroes.get(4).getVaccineInventory().size());
			Sup.setText("Supplies Available: " + Game.availableHeroes.get(4).getSupplyInventory().size());
			Game.startGame(Game.availableHeroes.get(4));
			updateMap();
			window.setScene(game);
			window.setFullScreen(true);
			window.setResizable(true);
		} );

		//Tess Button
		Button TessB = new Button("");
		TessB.setPrefSize(180, 180);
		Image Tess = new Image(getClass().getResourceAsStream("Tess.jpg"));
		ImageView TessV = new ImageView();
		TessV.setImage(Tess);
		TessV.setFitWidth(TessB.getPrefWidth());
		TessV.setFitHeight(TessB.getPrefHeight());
		TessB.setGraphic(TessV);
		TessB.setOnAction(e ->{
			
			chosen = Game.availableHeroes.get(2);
			Name1.setText("Name: " + Game.availableHeroes.get(2).getName());
			Type1.setText("Type: Explorer");
			Health1.setText("Health: " + Game.availableHeroes.get(2).getCurrentHp() );
			Actions.setText("Available Actions: " + Game.availableHeroes.get(2).getActionsAvailable());
			aDmg.setText("Attack: " + Game.availableHeroes.get(2).getAttackDmg());
			Vac.setText("Vaccines Available: " + Game.availableHeroes.get(2).getVaccineInventory().size());
			Sup.setText("Supplies Available: " + Game.availableHeroes.get(2).getSupplyInventory().size());
			Game.startGame(Game.availableHeroes.get(2));
			updateMap();
			window.setScene(game);
			window.setFullScreen(true);
			window.setResizable(true);
		} );

		//Henery Button
		Button HeneryB = new Button("");
		HeneryB.setPrefSize(180, 180);
		Image Henery = new Image(getClass().getResourceAsStream("Henry.jpg"));
		ImageView HeneryV = new ImageView();
		HeneryV.setImage(Henery);
		HeneryV.setFitWidth(HeneryB.getPrefWidth());
		HeneryV.setFitHeight(HeneryB.getPrefHeight());
		HeneryB.setGraphic(HeneryV);

		HeneryB.setOnAction(e ->{
			
			chosen = Game.availableHeroes.get(7);

			Name1.setText("Name: " + Game.availableHeroes.get(7).getName());
			Type1.setText("Type: Medic");
			Health1.setText("Health: " + Game.availableHeroes.get(7).getCurrentHp() );
			Actions.setText("Available Actions: " + Game.availableHeroes.get(7).getActionsAvailable());
			aDmg.setText("Attack: " + Game.availableHeroes.get(7).getAttackDmg());
			Vac.setText("Vaccines Available: " + Game.availableHeroes.get(7).getVaccineInventory().size());
			Sup.setText("Supplies Available: " + Game.availableHeroes.get(7).getSupplyInventory().size());
			Game.startGame(Game.availableHeroes.get(7));
			updateMap();
			window.setScene(game);
			window.setFullScreen(true);
			window.setResizable(true);
		} );

		//David Button
		Button DavidB = new Button("");
		DavidB.setPrefSize(180, 180);
		Image David = new Image(getClass().getResourceAsStream("David.jpg"));
		ImageView DavidV = new ImageView();
		DavidV.setImage(David);
		DavidV.setFitWidth(DavidB.getPrefWidth());
		DavidV.setFitHeight(DavidB.getPrefHeight());
		DavidB.setGraphic(DavidV);

		DavidB.setOnAction(e ->{
			
			chosen = Game.availableHeroes.get(6);

			Name1.setText("Name: " + Game.availableHeroes.get(6).getName());
			Type1.setText("Type: Fighter");
			Health1.setText("Health: " + Game.availableHeroes.get(6).getCurrentHp() );
			Actions.setText("Available Actions: " + Game.availableHeroes.get(6).getActionsAvailable());
			aDmg.setText("Attack: " + Game.availableHeroes.get(6).getAttackDmg());
			Vac.setText("Vaccines Available: " + Game.availableHeroes.get(6).getVaccineInventory().size());
			Sup.setText("Supplies Available: " + Game.availableHeroes.get(6).getSupplyInventory().size());
			Game.startGame(Game.availableHeroes.get(6));
			updateMap();
			window.setScene(game);
			window.setFullScreen(true);
			window.setResizable(true);
		} );

		//Riley Button
		Button RileyB = new Button("");
		RileyB.setPrefSize(180, 180);
		Image Riley = new Image(getClass().getResourceAsStream("Riley.jpg"));
		ImageView RileyV = new ImageView();
		RileyV.setImage(Riley);
		RileyV.setFitWidth(RileyB.getPrefWidth());
		RileyV.setFitHeight(RileyB.getPrefHeight());
		RileyB.setGraphic(RileyV);

		RileyB.setOnAction(e ->{
			
			chosen = Game.availableHeroes.get(3);

			Name1.setText("Name: " + Game.availableHeroes.get(3).getName());
			Type1.setText("Type: Explorer");
			Health1.setText("Health: " + Game.availableHeroes.get(3).getCurrentHp() );
			Actions.setText("Available Actions: " + Game.availableHeroes.get(3).getActionsAvailable());
			aDmg.setText("Attack: " + Game.availableHeroes.get(3).getAttackDmg());
			Vac.setText("Vaccines Available: " + Game.availableHeroes.get(3).getVaccineInventory().size());
			Sup.setText("Supplies Available: " + Game.availableHeroes.get(3).getSupplyInventory().size());
			Game.startGame(Game.availableHeroes.get(3));
			updateMap();
			window.setScene(game);
			window.setFullScreen(true);
			window.setResizable(true);
		} );

		//Bill Button
		Button BillB = new Button("");
		BillB.setPrefSize(180, 180);
		Image Bill = new Image(getClass().getResourceAsStream("Bill.jpg"));
		ImageView BillV = new ImageView();
		BillV.setImage(Bill);
		BillV.setFitWidth(BillB.getPrefWidth());
		BillV.setFitHeight(BillB.getPrefHeight());
		BillB.setGraphic(BillV);

		BillB.setOnAction(e ->{
			
			chosen = Game.availableHeroes.get(5);

			Name1.setText("Name: " + Game.availableHeroes.get(5).getName());
			Type1.setText("Type: Medic");
			Health1.setText("Health: " + Game.availableHeroes.get(5).getCurrentHp() );
			Actions.setText("Available Actions: " + Game.availableHeroes.get(5).getActionsAvailable());
			aDmg.setText("Attack: " + Game.availableHeroes.get(5).getAttackDmg());
			Vac.setText("Vaccines Available: " + Game.availableHeroes.get(5).getVaccineInventory().size());
			Sup.setText("Supplies Available: " + Game.availableHeroes.get(5).getSupplyInventory().size());
			Game.startGame(Game.availableHeroes.get(5));
			updateMap();
			window.setScene(game);
			window.setFullScreen(true);
			window.setResizable(true);
		} );

		JoelB.setOnMouseEntered(e ->{
			NameT.setText(Game.availableHeroes.get(0).getName());
			TypeT.setText("Fighter");
			maxActionsT.setText(""+Game.availableHeroes.get(0).getMaxActions());
			AttackDmgT.setText(""+Game.availableHeroes.get(0).getAttackDmg());
			HealthT.setText(""+Game.availableHeroes.get(0).getMaxHp());      	
		});

		JoelB.setOnMouseExited(e ->{
			NameT.setText("");
			TypeT.setText("");
			maxActionsT.setText("");
			AttackDmgT.setText("");
			HealthT.setText("");      	
		});
		EllieB.setOnMouseEntered(e ->{
			NameT.setText(Game.availableHeroes.get(1).getName());
			TypeT.setText("Medic");
			maxActionsT.setText(""+Game.availableHeroes.get(1).getMaxActions());
			AttackDmgT.setText(""+Game.availableHeroes.get(1).getAttackDmg());
			HealthT.setText(""+Game.availableHeroes.get(1).getMaxHp());      	
		});
		EllieB.setOnMouseExited(e ->{
			NameT.setText("");
			TypeT.setText("");
			maxActionsT.setText("");
			AttackDmgT.setText("");
			HealthT.setText("");      	
		});

		TommyB.setOnMouseEntered(e ->{
			NameT.setText(Game.availableHeroes.get(4).getName());
			TypeT.setText("Explorer");
			maxActionsT.setText(""+Game.availableHeroes.get(4).getMaxActions());
			AttackDmgT.setText(""+Game.availableHeroes.get(4).getAttackDmg());
			HealthT.setText(""+Game.availableHeroes.get(4).getMaxHp());      	
		});
		TommyB.setOnMouseExited(e ->{
			NameT.setText("");
			TypeT.setText("");
			maxActionsT.setText("");
			AttackDmgT.setText("");
			HealthT.setText("");      	
		});

		TessB.setOnMouseEntered(e ->{
			NameT.setText(Game.availableHeroes.get(2).getName());
			TypeT.setText("Explorer");
			maxActionsT.setText(""+Game.availableHeroes.get(2).getMaxActions());
			AttackDmgT.setText(""+Game.availableHeroes.get(2).getAttackDmg());
			HealthT.setText(""+Game.availableHeroes.get(2).getMaxHp());      	
		});

		TessB.setOnMouseExited(e ->{
			NameT.setText("");
			TypeT.setText("");
			maxActionsT.setText("");
			AttackDmgT.setText("");
			HealthT.setText("");      	
		});

		HeneryB.setOnMouseEntered(e ->{
			NameT.setText(Game.availableHeroes.get(7).getName());
			TypeT.setText("Medic");
			maxActionsT.setText(""+Game.availableHeroes.get(7).getMaxActions());
			AttackDmgT.setText(""+Game.availableHeroes.get(7).getAttackDmg());
			HealthT.setText(""+Game.availableHeroes.get(7).getMaxHp());     	
		});

		HeneryB.setOnMouseExited(e ->{
			NameT.setText("");
			TypeT.setText("");
			maxActionsT.setText("");
			AttackDmgT.setText("");
			HealthT.setText("");      	
		});

		DavidB.setOnMouseEntered(e ->{
			NameT.setText(Game.availableHeroes.get(6).getName());
			TypeT.setText("Fighter");
			maxActionsT.setText(""+Game.availableHeroes.get(6).getMaxActions());
			AttackDmgT.setText(""+Game.availableHeroes.get(6).getAttackDmg());
			HealthT.setText(""+Game.availableHeroes.get(6).getMaxHp()); 	
		});

		DavidB.setOnMouseExited(e ->{
			NameT.setText("");
			TypeT.setText("");
			maxActionsT.setText("");
			AttackDmgT.setText("");
			HealthT.setText("");      	
		});

		RileyB.setOnMouseEntered(e ->{
			NameT.setText(Game.availableHeroes.get(3).getName());
			TypeT.setText("Explorer");
			maxActionsT.setText(""+Game.availableHeroes.get(3).getMaxActions());
			AttackDmgT.setText(""+Game.availableHeroes.get(3).getAttackDmg());
			HealthT.setText(""+Game.availableHeroes.get(3).getMaxHp());    	
		});

		RileyB.setOnMouseExited(e ->{
			NameT.setText("");
			TypeT.setText("");
			maxActionsT.setText("");
			AttackDmgT.setText("");
			HealthT.setText("");      	
		});

		BillB.setOnMouseEntered(e ->{
			NameT.setText(Game.availableHeroes.get(5).getName());
			TypeT.setText("Medic");
			maxActionsT.setText(""+Game.availableHeroes.get(5).getMaxActions());
			AttackDmgT.setText(""+Game.availableHeroes.get(5).getAttackDmg());
			HealthT.setText(""+Game.availableHeroes.get(5).getMaxHp());     	
		});

		BillB.setOnMouseExited(e ->{
			NameT.setText("");
			TypeT.setText("");
			maxActionsT.setText("");
			AttackDmgT.setText("");
			HealthT.setText("");      	
		});





		grid2.setHgap(2);
		grid2.setVgap(2);
		grid2.setPadding(new Insets(20,20,20,20));
		GridPane.setConstraints(Characters, 0, 1);
		GridPane.setConstraints(Name, 1, 2);
		GridPane.setConstraints(NameT, 2, 2);
		GridPane.setConstraints(Type, 1, 3);
		GridPane.setConstraints(TypeT, 2, 3);
		GridPane.setConstraints(Health, 1, 4);
		GridPane.setConstraints(HealthT, 2, 4);
		GridPane.setConstraints(maxActions, 1, 5);
		GridPane.setConstraints(maxActionsT, 2, 5);
		GridPane.setConstraints(AttackDmg, 1, 6);
		GridPane.setConstraints(AttackDmgT, 2, 6);
		GridPane.setConstraints(GoBack, 0, 8);
		GridPane.setConstraints(JoelB, 1, 7);
		GridPane.setConstraints(EllieB, 2, 7);
		GridPane.setConstraints(TommyB, 3, 7);
		GridPane.setConstraints(TessB, 4, 7);
		GridPane.setConstraints(HeneryB, 1, 8);
		GridPane.setConstraints(DavidB, 2, 8);
		GridPane.setConstraints(RileyB, 3, 8);
		GridPane.setConstraints(BillB, 4, 8);
		grid2.getChildren().addAll( Characters, GoBack,JoelB,BillB,EllieB,TommyB,TessB,HeneryB,DavidB,RileyB,Name,Type,Health,maxActions,AttackDmg,NameT,TypeT,maxActionsT,HealthT,AttackDmgT);
		Characterselect = new Scene(grid2,1500,1500);

		String css = this.getClass().getResource("application.css").toExternalForm();
		MainMenu.getStylesheets().add(css);
		Characterselect.getStylesheets().add(css);





		Image GridbackgroundWallpaper = new Image("Selectheroes.jpg"); 
		BackgroundImage gridBk = new BackgroundImage(GridbackgroundWallpaper, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO , false, false, false, true));
		grid2.setBackground(new Background(gridBk));

		Image MainMenuWallpaper = new Image("MainMenu.jpg"); 
		BackgroundImage gridB = new BackgroundImage(MainMenuWallpaper, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO , false, false, false, true));
		grid.setBackground(new Background(gridB));






		Map.setOnKeyPressed(e->{
			if (e.getCode()==KeyCode.W) {
				try {
					if(Game.map[(int) ((Playing.getLocation().getX())+1)][(int)Playing.getLocation().getY()] instanceof TrapCell){
						Traper.display();
					}
					Playing.move(Direction.UP);
				}catch(Exception ex) {
					System.out.println(ex.getMessage());
					ErrorMessage.display(ex.getMessage().toString());
				}
			}
			if (e.getCode()==KeyCode.A) {
				try {
					if(Game.map[(int) ((Playing.getLocation().getX()))][(int)(Playing.getLocation().getY())-1] instanceof TrapCell){
						Traper.display();
					}
					Playing.move(Direction.LEFT);
				}catch(Exception ex) {
					System.out.println(ex.getMessage());
					ErrorMessage.display(ex.getMessage().toString());
				}
			}
			if (e.getCode()==KeyCode.S) {
				try {
					if(Game.map[(int) ((Playing.getLocation().getX()))-1][(int)(Playing.getLocation().getY())] instanceof TrapCell){
						Traper.display();
					}
					Playing.move(Direction.DOWN);
				}catch(Exception ex) {
					System.out.println(ex.getMessage());
					ErrorMessage.display(ex.getMessage().toString());
				}
			}
			if (e.getCode()==KeyCode.D) {
				try {
					if(Game.map[(int) ((Playing.getLocation().getX()))][(int)(Playing.getLocation().getY())+1] instanceof TrapCell){
						Traper.display();
					}
					Playing.move(Direction.RIGHT);
				}catch(Exception ex) {
					System.out.println(ex.getMessage());
					ErrorMessage.display(ex.getMessage().toString());
				}
			}
			updateMap();
			updateData(Playing);

		});


		window.setScene(MainMenu);
		window.setFullScreen(true);
		window.setResizable(true);
		primary.show();

	}


	public void GameOver(){
		if(Game.checkGameOver()==true){
			Stage exiter = new Stage();

			exiter.initModality(Modality.APPLICATION_MODAL);
			exiter.setTitle("Game Over!");
			Label label = new Label();
			label.setText("You Lost!");
			Button close = new Button("Close Game");
			close.setOnAction(e ->{ 
				exiter.close();
				window.close();
			});
			//Layout
			VBox layout = new VBox(3);
			layout.getChildren().addAll(label,close);
			layout.setAlignment(Pos.CENTER);

			Scene scene = new Scene(layout, 300 , 100);
			window.setScene(scene);
			window.showAndWait();
		}
	}
	public void GameWon(){
		if(Game.checkWin()==true){
			Stage exiter = new Stage();

			exiter.initModality(Modality.APPLICATION_MODAL);
			exiter.setTitle("Game Over!");
			Label label = new Label();
			label.setText("You Won!");
			Button close = new Button("Close Game");
			close.setOnAction(e ->{ 
				exiter.close();
				window.close();
			});
			//Layout
			VBox layout = new VBox(3);
			layout.getChildren().addAll(label,close);
			layout.setAlignment(Pos.CENTER);

			Scene scene = new Scene(layout, 300 , 100);
			window.setScene(scene);
			window.showAndWait();	
		}
	}
	private void closeProgram() {
		boolean answer = ConfirmationBox.display("Exit", "Are you sure you want to exit?");
		if (answer == true)
			window.close();
	}



	public ImageView getImage(Hero chosen) {
		if (chosen.getName().equals("Joel Miller")) {
			Image ch = new Image(getClass().getResourceAsStream("JoelPixel.png"));
			ImageView chV = new ImageView();
			chV.setImage(ch);
			chV.setFitWidth(40);
			chV.setFitHeight(45);
			return chV;
		}
		else if (chosen.getName().equals("Ellie Williams")) {
			Image ch = new Image(getClass().getResourceAsStream("ElliePixel.png"));
			ImageView chV = new ImageView();
			chV.setFitWidth(40);
			chV.setFitHeight(45);
			chV.setImage(ch);
			return chV;
		}
		else if (chosen.getName().equals("Tess")) {
			Image ch = new Image(getClass().getResourceAsStream("TessPixel.png"));
			ImageView chV = new ImageView();
			chV.setFitWidth(40);
			chV.setFitHeight(45);
			chV.setImage(ch);
			return chV;
		}
		else if (chosen.getName().equals("Riley Abel")) {
			Image ch = new Image(getClass().getResourceAsStream("RileyPixel.png"));
			ImageView chV = new ImageView();
			chV.setFitWidth(40);
			chV.setFitHeight(45);
			chV.setImage(ch);
			return chV;
		}
		else if (chosen.getName().equals("Tommy Miller")) {
			Image ch = new Image(getClass().getResourceAsStream("TommyPixel.png"));
			ImageView chV = new ImageView();
			chV.setFitWidth(40);
			chV.setFitHeight(45);
			chV.setImage(ch);
			return chV;
		}
		else if (chosen.getName().equals("Bill")) {
			Image ch = new Image(getClass().getResourceAsStream("BillPixel.png"));
			ImageView chV = new ImageView();
			chV.setFitWidth(40);
			chV.setFitHeight(45);
			chV.setImage(ch);
			return chV;
		}
		else if (chosen.getName().equals("David")) {
			Image ch = new Image(getClass().getResourceAsStream("DavidPixel.png"));
			ImageView chV = new ImageView();
			chV.setFitWidth(40);
			chV.setFitHeight(45);
			chV.setImage(ch);
			return chV;
		}
		else if (chosen.getName().equals("Henry Burell")) {
			Image ch = new Image(getClass().getResourceAsStream("HenryPixel.png"));
			ImageView chV = new ImageView();
			chV.setFitWidth(40);
			chV.setFitHeight(45);
			chV.setImage(ch);
			return chV;
		}

		return null;


	}



	public void updateData(Hero h){
		

		Name1.setText("Name: " + h.getName());
		if(h.getName().equals("Joel Miller")||h.getName().equals("David")){
			Type1.setText("Type: Fighter");
		}
		else if(h.getName().equals("Tess")||h.getName().equals("Riley Abel")||h.getName().equals("Tommy Miller")){
			Type1.setText("Type: Explorer");
		}
		else{
			Type1.setText("Type: Medic");
		}
		
		Actions.setText("Available Actions: "+(h.getActionsAvailable()));
		Health1.setText("Health: "+(h.getCurrentHp()));
		Vac.setText("Vaccine Inventory: "+ h.getVaccineInventory().size());
		Sup.setText("Supply Inventory: "+h.getSupplyInventory().size());

	}



	public void updateMap() {		
		Map.getChildren().clear();

		for (int i = 0;i <15;i++) {
			for (int j = 0;j<15;j++) {
				if (Game.map[i][j].isVisible()==true) {
					Button CellB = new Button("");
					Image Cell = new Image(getClass().getResourceAsStream("Cell2.jpg"));
					ImageView CellV = new ImageView();
					CellB.setPrefSize(60, 60);
					CellB.setMaxSize(60, 60);
					CellV.setImage(Cell);
					CellV.setFitWidth(CellB.getPrefWidth());
					CellV.setFitHeight(CellB.getPrefHeight());
					CellB.setGraphic(CellV);
					CellB.setStyle("-fx-background-color: transparent; -fx-padding: 1;");
					Map.add(CellB, j, 14-i);

				}
				if (Game.map[i][j].isVisible()==false) {
					Button CellB = new Button("");
					Image Cell = new Image(getClass().getResourceAsStream("DarkenCell2.png"));
					ImageView CellV = new ImageView();
					CellB.setPrefSize(60, 60);
					CellB.setMaxSize(60, 60);
					CellV.setImage(Cell);
					CellV.setFitWidth(CellB.getPrefWidth());
					CellV.setFitHeight(CellB.getPrefHeight());
					CellB.setGraphic(CellV);
					CellB.setStyle("-fx-background-color: transparent; -fx-padding: 1;");
					Map.add(CellB, j, 14-i);
				}
				if(Game.map[i][j].isVisible()==true&& Game.map[i][j] instanceof CollectibleCell && ((CollectibleCell)Game.map[i][j]).getCollectible() instanceof Supply) {
					Button CellB = new Button("");
					Image Cell = new Image(getClass().getResourceAsStream("Supply.png"));
					ImageView CellV = new ImageView();
					CellB.setPrefSize(60, 60);
					CellB.setMaxSize(60, 60);
					CellV.setImage(Cell);
					CellV.setFitWidth(CellB.getPrefWidth());
					CellV.setFitHeight(CellB.getPrefHeight());
					CellB.setGraphic(CellV);
					CellB.setStyle("-fx-background-color: transparent; -fx-padding: 1;");
					Map.add(CellB, j, 14-i);
				}
				if(Game.map[i][j].isVisible()==true&& Game.map[i][j] instanceof CollectibleCell && ((CollectibleCell)Game.map[i][j]).getCollectible() instanceof Vaccine) {
					Button CellB = new Button("");
					Image Cell = new Image(getClass().getResourceAsStream("Vacccine.png"));
					ImageView CellV = new ImageView();
					CellB.setPrefSize(60, 60);
					CellB.setMaxSize(60, 60);
					CellV.setImage(Cell);
					CellV.setFitWidth(CellB.getPrefWidth());
					CellV.setFitHeight(CellB.getPrefHeight());
					CellB.setGraphic(CellV);
					CellB.setStyle("-fx-background-color: transparent; -fx-padding: 1;");
					Map.add(CellB, j, 14-i);
				}
				if(Game.map[i][j].isVisible()==true&& Game.map[i][j] instanceof CharacterCell && ((CharacterCell)Game.map[i][j]).getCharacter() instanceof Zombie) {
					ZombieButton zb = new ZombieButton((Zombie)((CharacterCell)Game.map[i][j]).getCharacter());
					Image Cell = new Image(getClass().getResourceAsStream("Zombie.png"));
					ImageView CellV = new ImageView();
					zb.setPrefSize(60, 60);
					zb.setMaxSize(60, 60);
					CellV.setImage(Cell);
					CellV.setFitWidth(zb.getPrefWidth());
					CellV.setFitHeight(zb.getPrefHeight());
					zb.setGraphic(CellV);
					zb.setStyle("-fx-background-color: transparent; -fx-padding: 1;");

					zb.setOnAction(e->{
						zombie = ((ZombieButton)e.getSource()).z;
						Playing.setTarget(zombie);
					});
					Map.add(zb, j, 14-i);
				}
				
				if( Game.map[i][j] instanceof CharacterCell && ((CharacterCell)Game.map[i][j]).getCharacter() instanceof Hero) {
					chosen = (Hero) ((CharacterCell)Game.map[i][j]).getCharacter();
					HeroButton hb = new HeroButton((Hero)((CharacterCell)Game.map[i][j]).getCharacter());
					getImage(chosen).setFitWidth(hb.getPrefWidth());
					getImage(chosen).setFitHeight(hb.getPrefHeight());
					hb.setStyle("-fx-background-color: transparent;");
					hb.setGraphic(getImage(chosen));
					hb.setOnMouseClicked(e->{
						Playing = ((HeroButton)e.getSource()).h;
						
						if (e.getClickCount() == 2) {
							Target= ((HeroButton)e.getSource()).h;
					    }
						updateData(Playing);

					});
					Map.add(hb, j,14-i);
				}
				
			}	
		}


	}


}
