package com.tomtrotter.habitatsimulation.view;

import javafx.scene.Scene;

/**
 * Abstract base class for views in the habitat simulation.
 * It provides a mechanism to create and retrieve a scene for the view.
 * Concrete subclasses should implement the `createScene` method to define the specific layout of the view.
 */
public abstract class BaseView {

    // The scene associated with the view, initialized lazily
    protected Scene scene;

    /**
     * Abstract method to create the scene for the specific view.
     * Concrete subclasses should implement this method to define the scene's layout.
     *
     * @return The created scene for the view.
     */
    public abstract Scene createScene();

    /**
     * Retrieves the scene for this view.
     * If the scene hasn't been created yet, it will be created using the `createScene` method.
     *
     * @return The scene for this view.
     */
    public Scene getScene() {
        if (scene == null) {
            scene = createScene();
        }
        return scene;
    }

}
