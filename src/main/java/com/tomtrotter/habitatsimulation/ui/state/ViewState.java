package com.tomtrotter.habitatsimulation.ui.state;

import com.tomtrotter.habitatsimulation.ui.base.BaseView;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
* Singleton class that manages the state of the views in the simulation.
* It stores and switches between different views, manages the current generation,
* and provides window settings for the simulation's user interface.
*/

public final class ViewState {

    private static ViewState INSTANCE;

    private Stage stage;
    private final Map<Class<? extends BaseView>, BaseView> views = new HashMap<>();

    private ViewState() {}

    /**
    * Retrieves the single instance of ViewState.
    * If the instance doesn't exist, it creates one.
    *
    * @return The Singleton instance of ViewState.
    */
    public static ViewState getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ViewState();
        }
        return INSTANCE;
    }

    /**
    * Registers a view with its class type. The view will be associated with the given class.
    *
    * @param viewClass The class of the view to register.
    * @param view The instance of the view to register.
    * @param <T> The type of the view, extending BaseView.
    */
    public <T extends BaseView> void registerView(Class<T> viewClass, T view) {
        views.put(viewClass, view);
    }

    /**
    * Switches to a view identified by its class type. If the view is registered,
    * it sets the scene of the stage to the view's scene.
    *
    * @param viewClass The class type of the view to switch to.
    * @param <T> The type of the view, extending BaseView.
    * @throws IllegalArgumentException If the view is not registered.
    */
    public <T extends BaseView> void switchTo(Class<T> viewClass) {
        BaseView view = views.get(viewClass);
        if (view != null) {
            stage.setScene(view.getScene());
        } else {
            throw new IllegalArgumentException("View not registered: " + viewClass.getSimpleName());
        }
    }

    /**
    * Retrieves a registered view by its class type.
    *
    * @param viewClass The class type of the view to retrieve.
    * @param <T> The type of the view, extending BaseView.
    * @return The instance of the view if it is registered.
    */
    public <T extends BaseView> T getView(Class<T> viewClass) {
        return viewClass.cast(views.get(viewClass));
    }

    /**
    * Sets the stage (window) for the simulation's views.
    *
    * @param stage The stage to be set.
    */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
    * Gets the width of the application window.
    *
    * @return The window width (in pixels).
    */
    public int getWindowWidth() {
        return 960;
    }

    /**
    * Gets the height of the application window.
    *
    * @return The window height (in pixels).
    */
    public int getWindowHeight() {
        return 600;
    }

    /**
    * Gets the prefix text used to display the generation number.
    *
    * @return The prefix for generation text.
    */
    public String getGenPrefix() {
        return "Generation: ";
    }

}
