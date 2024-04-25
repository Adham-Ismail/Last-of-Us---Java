package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ExceptionBox {
	
	public static void display(String Title, String Message) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(Title);
		Label label = new Label();
		label.setText(Message);
		Button close = new Button("Close");
		close.setOnAction(e -> window.close());
		//Layout
		VBox layout = new VBox(3);
		layout.getChildren().addAll(label,close);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout, 300 , 100);
		window.setScene(scene);
		window.showAndWait();
		
	}

}
