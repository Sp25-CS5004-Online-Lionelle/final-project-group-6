package controller;
import java.util.*;
import java.awt.event.ActionListener;

public interface IController {
        /**
         * Create a list of Action listeners to be passed to the view
         * @return
         */
        public List<ActionListener> initActionListeners();
        /**
         * Run app
         * @return
         */
        public void runApp();
}