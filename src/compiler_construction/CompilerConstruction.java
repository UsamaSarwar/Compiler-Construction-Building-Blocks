package compiler_construction;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Usama Sarwar <UsamaSarwarOfficial@gmail.com>
 * @version v1.0.0
 * @since 2020
 *
 */

public class CompilerConstruction extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Compiler Construction Building Blocks v1.0.0");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
