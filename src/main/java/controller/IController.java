package controller;
import java.util.*;
import java.awt.event.ActionListener;

public interface IController {
        public List<ActionListener> setActionListeners();
        public void runApp();
}