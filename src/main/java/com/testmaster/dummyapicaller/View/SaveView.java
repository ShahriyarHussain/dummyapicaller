package com.testmaster.dummyapicaller.View;

import com.testmaster.dummyapicaller.Service.ApiResponseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;



@Route("save-response")
public class SaveView extends VerticalLayout {

    public SaveView(@Autowired ApiResponseService apiResponseService) {
        H3 title = new H3("Save A Response");
        TextField apiNameField = new TextField("API Name:");
        TextArea jsonResponseText = new TextArea("Enter Json Response");
        Button button = new Button("Save", e -> {
            apiResponseService.saveJsonResponse(apiNameField.getValue(), new JSONObject(jsonResponseText.getValue()));
            Notification.show("Saved!", 2500, Notification.Position.TOP_CENTER );
//            Notification.show("Saved!");
        });
//        Button button = new Button("Say hello", e -> Notification.show(apiNameField.getValue()));
//        HorizontalLayout titleLayout = new HorizontalLayout(title);
//        HorizontalLayout nameLayout = new HorizontalLayout(apiNameField, button);
//        nameLayout.setAlignItems(Alignment.END);
//        HorizontalLayout jsonTextLayout = new HorizontalLayout(jsonResponseText);
//        add(titleLayout, nameLayout, jsonTextLayout);
        add(title, apiNameField, button, jsonResponseText);
    }
}
