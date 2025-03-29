# Initial Design

We will follow model view controller architecture for this project. The UI, user input, and Java Swing components will be handled by the View. The controller will process these inputs and hand them off to the model where the business logic will be performed. The controller will then hand the response to the view to be displayed.

## Model

The model will be tasked primarily with receiving the api response and serializing it. It's secondary role will be providing methods to add, search, filter, and remove information in the database.

```mermaid
---
title: Model Classes
---
classDiagram
    note for Park "Important fields of an api response in a serialized form, could use a record here"
    Database ..> Park : uses/depends
    ParksModel ..|> IModel : implements
    ParksModel ..> Filter : uses/depends
    ParksModel ..> Database : uses/depends
    ParksModel ..> NetUtils : uses/depends

    class Park {
        - String name;
        - String state;
        - String description;
        - String imageUrl;
        + Park(String name, String state, String description, String imageUrl)
        + String getName()
        + String getState()
        + String getDescription()
        + String getImageUrl()
    }
    note for Database "Holds some collection of data, provides methods to access and update the data"
    class Database {
        - Collection<Park> dataList 
        + Park search(String query)
        + Collection<Park> filter(Filter filter)
        + addPark(Park park)
        + removePark(Park park)
        + updateDB()
        + clear()
    }
    note for NetUtils "Performs all networking to get an api response"
    class NetUtils {
        <!-- Assuming query is state/zip-code -->
        - String formatRequest(String query)
        + $ String getParkInfo(String query)
        + $ String getParkImage(String query)
    }
    class Filter {
        CONTAINS_STR
        STATE
        ZIPCODE
        ...
        <!-- More filters would make sense here -->
    }
    note for ParksModel "Higher level class to be used by the controller"
    class ParksModel {
        - Database dbInstance
        + Model()
    }
    class IModel {
        <!-- Gets a response from the API and updates the db -->
        + getResponseAndUpdate(String query)
        <!-- Get filtered list from the database -->
        + getFilteredData(Filter filter)
        <!-- Turns API response into park object -->
        - $ Park serializeResponse(String Json)
    }
```
## View
The view will be in charge of managing the frame, swapping components, and creating custom UI elements. There will be a class called `FrameInstance` that holds all the components that are on screen. This means that UI components can be updated by the controller (Via action listeners).

```mermaid
---
title: View Classes
---
classDiagram
    note for FrameInstance "Holds static fields of all elements on the frame, handles replacing them dynamically"
    %% This is just one way we structure our UI, the benefit to this approach is that we can %%
    %% change the UI easily from the controller since static fields are accessible anywhere %%
    ParksHome ..> FrameInstance : uses/depends
    FrameInstance ..> ControlPanel : uses/depends
    FrameInstance ..> ImageView : uses/depends
    FrameInstance ..> TextView : uses/depends
    class FrameInstance {
        - $ JFrame frame

        <!-- This could be the upper panel that holds the text box and buttons -->
        - $ JComponent controlPanel

        <!-- This could be the middle panel that holds the gallery of images -->
        - $ JComponent imageView

        This could be the lower text panel
        - $ JComponent textView

        + setFrame(JFrame frame)
        + updateControlPanel(JComponent newComponent)
        + updateImageView(JComponent newComponent)
        + updateTextView(JComponent newComponent)
    }
    note for ControlPanel "Extends some JComponent (Like JPanel), a more organized place to do UI/formatting"
    class ControlPanel {
        ControlPanel()
        <!-- Can call superclass in constructor, then do all the custom UI work here -->
    }
    class ImageView {
        ImageView()
        <!-- Can call superclass in constructor, then do all the custom UI work here -->
    }
    class TextView {
        TextView()
        <!-- Can call superclass in constructor, then do all the custom UI work here -->
    }
    class IView {
        <!-- Uses FrameInstance to add ParksHome to frame -->
        + initFrame()
        + setListeners(ActionListener listenerOne, ActionListener listenerTwo)
    }
    ParksHome ..|> IView : implements
    note for ParksHome "Creates and displays main application page"
    class ParksHome {
        <!-- Assembles all other UI components into the frame, initializes and customizes frame -->
        + ParksHome()
    }
```

The controller will be the lightest element of our design, it is mostly in charge of setting the ActionListeners for the view. Here the controller can get a response from the model and instruct the view to display the response.

## Controller
```mermaid
---
title: Controller
---
classDiagram
    note for ParkController "Put all the pieces together to run the app"
    class IController {
        + setActionListeners()
        + runApp()
    }
    class ParkController {
        - ParksModel model
        - ParksHome view
        + ParkController()
    }
    ParkController ..|> IController: implements
```

## Complete Diagram 
```mermaid
---
title: Parks App
---
classDiagram

    Database ..> Park : uses/depends
    ParksModel ..> Filter : uses/depends
    ParksModel ..> Database : uses/depends
    ParksModel ..> NetUtils : uses/depends
    ParksModel ..|> IModel : implements
    ParksApp ..> IController

    class ParksApp {
        + $ main()
    }
    class IModel {
        <!-- Gets a response from the API and updates the db -->
        + getResponseAndUpdate(String query)
        <!-- Get filtered list from the database -->
        + getFilteredData(Filter filter)
        <!-- Turns API response into park object -->
        - $ Park serializeResponse(String Json)
    }
    class ParkController {
        - ParksModel model
        - ParksHome view
    }
    ParkController ..|> IController: implements
    class IController {
        + setActionListeners()
        + runApp()
    }
    class Park {
        - String name;
        - String state;
        - String description;
        - String imageUrl;
        + Park(String name, String state, String description, String imageUrl)
        + String getName()
        + String getState()
        + String getDescription()
        + String getImageUrl()
    }
    class Database {
        - Collection<Park> dataList 
        + Park search(String query)
        + Collection<Park> filter(Filter filter)
        + addPark(Park park)
        + removePark(Park park)
        + updateDB()
        + clear()
    }
    class NetUtils {
        <!-- Assuming query is state/zip-code -->
        - String formatRequest(String query)
        + $ String getParkInfo(String query)
        + $ String getParkImage(String query)
    }
    class Filter {
        CONTAINS_STR
        STATE
        ZIPCODE
        ...
        <!-- More filters would make sense here -->
    }
    class ParksModel {
        - Database dbInstance
        + Model()
        <!-- Gets a response from the API and updates the db -->
        + getResponseAndUpdate(String query)
        <!-- Get filtered list from the database -->
        + getFilteredData(Filter filter)
        <!-- Turns API response into park object -->
        - $ Park serializeResponse(String Json)
    }

    %% This is just one way we structure our UI, the benefit to this approach is that we can %%
    %% change the UI easily from the controller since static fields are accessible anywhere %%
    ParksHome ..> FrameInstance : uses/depends
    FrameInstance ..> ControlPanel : uses/depends
    FrameInstance ..> ImageView : uses/depends
    FrameInstance ..> TextView : uses/depends

    class FrameInstance {
        - $ JFrame frame

        <!-- This could be the upper panel that holds the text box and buttons -->
        - $ JComponent controlPanel

        <!-- This could be the middle panel that holds the gallery of images -->
        - $ JComponent imageView

        This could be the lower text panel
        - $ JComponent textView

        + setFrame(JFrame frame)
        + updateControlPanel(JComponent newComponent)
        + updateImageView(JComponent newComponent)
        + updateTextView(JComponent newComponent)
    }
    class ControlPanel {
        ControlPanel()
        <!-- Can call superclass in constructor, then do all the custom UI work here -->
    }
    class ImageView {
        ImageView()
        <!-- Can call superclass in constructor, then do all the custom UI work here -->
    }
    class TextView {
        TextView()
        <!-- Can call superclass in constructor, then do all the custom UI work here -->
    }

    IController ..> IModel
    IController ..> IView
    class ParkController {
        - IModel model
        - IView view
        + ParkController()
    }
    class IView {
        + setActionListeners()
        + runApp()
    }
    ParksHome ..|> IView : implements
    note for ParksHome "Creates and displays main application page"
    class ParksHome {
        <!-- Assembles all other UI components into the frame, initializes and customizes frame -->
        + ParksHome()
    }
```