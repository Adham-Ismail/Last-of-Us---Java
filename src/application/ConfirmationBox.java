package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmationBox {
	static boolean answer;
	public static boolean display(String Title, String Message) {
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(Title);
		Label label = new Label();
		label.setText(Message);
		
		
		Button yes = new Button("Yes");
		yes.setOnAction(e -> {
			answer = true;
			window.close();
		});
		
		Button no = new Button("No");
		no.setOnAction(e -> {
			answer = false;
			window.close();
		});
		
		//Layout
		VBox layout = new VBox(3);
		layout.getChildren().addAll(label,yes,no);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout, 300 , 100);
		window.setScene(scene);
		window.showAndWait();
		return answer;
		
	}

}
