package com.testmaster.dummyapicaller.View;

import com.testmaster.dummyapicaller.Exception.BadRequestException;
import com.testmaster.dummyapicaller.MainView;
import com.testmaster.dummyapicaller.Service.ApiResponseService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value="save-response", layout = MainView.class)
@PageTitle("Save API Response")
public class SaveView extends VerticalLayout {

    public SaveView(@Autowired ApiResponseService apiResponseService) {
        H4 title = new H4("Save A Response");
        TextField apiNameField = new TextField("API Name:");
        TextArea jsonResponseText = getTextArea();
        Button replaceButton = new Button("Replace");
        Button button = new Button("Save");
        button.addClickListener(e -> {
            try {
                apiResponseService.saveJsonResponse(apiNameField.getValue(), new JSONObject(jsonResponseText.getValue()));
                Notification notification = getSuccessNotification();
                notification.setText("Saved!");
                notification.open();
            } catch (BadRequestException ex) {
                Notification notification = getErrorNotification();
                notification.setText("File With Name Already Exists!!");
                notification.open();
                replace(button, replaceButton);
            } catch (Exception ex) {
                Notification notification = getErrorNotification();
                notification.setText("Unexpected Error! " + ex.getMessage());
                notification.open();
            }
        });
        replaceButton.addClickListener(e -> {
            try {
                apiResponseService.overwriteFileContentByName(apiNameField.getValue(), new JSONObject(jsonResponseText.getValue()));
                Notification notification = getSuccessNotification();
                notification.setText("Saved!");
                notification.open();
                replace(replaceButton, button);
            } catch (Exception ex) {
                Notification notification = getErrorNotification();
                notification.setText("Unexpected Error! " + ex.getMessage());
                notification.open();
            }
        });
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_LARGE);
        add(title, apiNameField, button, jsonResponseText);
    }

    public TextArea getTextArea() {
        TextArea textArea = new TextArea();
        textArea.setLabel("Enter A Valid Json Response");
        textArea.setValueChangeMode(ValueChangeMode.EAGER);
        textArea.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + " characters"));
        textArea.setWidth("60%");
        textArea.setHeight(30, Unit.EM);
        textArea.setMaxWidth("90%");
        textArea.setMaxHeight(50, Unit.EM);
        return textArea;
    }

    public Notification getErrorNotification() {
        Notification notification = new Notification();
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        return notification;
    }

    public Notification getSuccessNotification() {
        Notification notification = new Notification();
        notification.setDuration(2500);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        return notification;
    }
}
