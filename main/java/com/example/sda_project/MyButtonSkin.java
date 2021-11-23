package com.example.sda_project;

import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.scene.control.skin.ButtonSkin;
import javafx.util.Duration;

public class MyButtonSkin extends ButtonSkin {

    public MyButtonSkin(Button control) {
        super(control);

        final FadeTransition fadeOut = new FadeTransition(Duration.millis(100));
        fadeOut.setNode(control);
        fadeOut.setToValue(1);
        control.setOnMouseExited(e ->fadeOut.playFromStart());

        final FadeTransition fadeIn = new FadeTransition(Duration.millis(100));
        fadeIn.setNode(control);
        fadeIn.setToValue(0.8);
        control.setOnMouseEntered(e -> fadeIn.playFromStart());


        control.setOpacity(1);
    }

}