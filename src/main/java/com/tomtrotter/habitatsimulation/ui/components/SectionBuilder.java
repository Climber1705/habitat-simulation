package com.tomtrotter.habitatsimulation.ui.components;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
* Utility builder class for constructing styled VBox sections with a title label.
* Useful for creating clean, consistent UI sections across views.
*/
public class SectionBuilder {

    private final VBox box;

    /**
    * Initializes an empty section builder.
    */
    public SectionBuilder() {
        this.box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: #ccc; -fx-border-width: 1;");
    }

    /**
    * Sets the title of the section.
    *
    * @param title The title text to display at the top.
    * @return The current VBoxBuilder instance (for chaining).
    */
    public SectionBuilder setTitle(String title) {
        Label label = new Label(title);
        label.setFont(Font.font("Segoe UI Emoji"));
        box.getChildren().add(label);
        return this;
    }

    /**
    * Adds a single UI node to the section.
    *
    * @param node The JavaFX Node to add.
    * @return The current VBoxBuilder instance (for chaining).
    */
    public SectionBuilder add(Node node) {
        box.getChildren().add(node);
        return this;
    }

    /**
    * Adds multiple UI nodes to the section.
    *
    * @param nodes The JavaFX Nodes to add.
    * @return The current VBoxBuilder instance (for chaining).
    */
    public SectionBuilder addAll(Node... nodes) {
        box.getChildren().addAll(nodes);
        return this;
    }

    /**
    * Builds and returns the final VBox containing the section UI.
    *
    * @return A fully constructed VBox for the section.
    */
    public VBox build() {
        return box;
    }

}
