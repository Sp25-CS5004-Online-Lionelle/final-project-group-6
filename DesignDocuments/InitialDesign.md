# Initial Design

We will follow model view controller architecture as closely as possible. This means that the UI, user input, Java Swing components will be handled by the View. The controller will process these inputs and hand them off to the model where the business logic will be performed.

## Model

The model will be tasked primarily with receiving the api response, serializing it, ect. It's secondary role will be providing an interface to the database for the controller to add, search, and remove information stored from responses. As a rough outline here are some classes that would make sense to have.

```mermaid
---
title: Model Classes
---
classDiagram
    note for ResponseData "Important fields of an api response in a serialized form, could use a record here"
    Database ..> ResponseData : uses/depends
    Model ..> DBOperation : uses/depends
    Model ..> Database : uses/depends
    Model ..> NetUtils : uses/depends

    class ResponseData {
        - String someValue
        - double someOther
        + String getStringField()
        + String getDoubleField()
        ect..()
    }
    note for Database "Holds some collection of data, provides methods to access and update the data"
    class Database {
        - Collection<ResponseData> dataList 
        + ResponseData search(String query)
        + add(ResponseData query)
        + remove(ResponseData query)
        + clearDB()
    }
    note for NetUtils "Performs all networking to get an api response"
    class NetUtils {
        - String formatRequest(String query)
        + $ String getApiResponseA()
        + $ String getApiResponseB()
        ect..()
    }
    class DBOperation {
        ADD
        REMOVE
        CLEAR
    }
    note for Model "Higher level class to be used by the controller"
    class Model {
        Database dbInstance
        + Model()
        + getResponseAndUpdate(String query)
        + performOperation(DBOperation operation, String element)
        - $ ResponseData formatResponse(String Json)
    }
```
The view will be in charge of managing the frame, swapping components, and creating custom UI elements. There will be a class called `FrameInstance` that holds all the components that are on screen. This means that UI components can be reached and changed from the controller (the listeners will likely want to be there).

```mermaid
---
title: View Classes
---
classDiagram
    note for FrameInstance "Holds static fields that represent all visible UI"
    UI ..> FrameInstance : depends
    FrameInstance ..> CustomComponentA : depends
    FrameInstance ..> CustomComponentB : depends
    class FrameInstance {
        - $ JFrame frame
        - $ JComponent componentA
        - $ JComponent componentB
        + setFrame(JFrame frame)
        + updateComponentA(JComponent newComponent)
        + updateComponentB(JComponent newComponent)
    }
    note for CustomComponentA "Extends some JComponent, could be a good place to do UI/formatting"
    class CustomComponentA {
        ...
    }
    class CustomComponentB {
        ...
    }
    note for UI "Higher level class for Controller to use"
    class UI {
        + UI()
        + initFrame()
        <!-- If we have two many action listeners we could consider making a list of them -->
        + setListeners(ActionListener listenerOne, ActionListener listenerTwo)
    }
```

The controller will be the lightest element of our design, it's only real task will be setting the ActionListeners for the view to use and passing the appropriate information to the Model.

Controller {

}