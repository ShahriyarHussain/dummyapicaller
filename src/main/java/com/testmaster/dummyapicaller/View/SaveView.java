package com.testmaster.dummyapicaller.View;

import com.testmaster.dummyapicaller.Enum.RequestTypes;
import com.testmaster.dummyapicaller.Exception.BadRequestException;
import com.testmaster.dummyapicaller.MainView;
import com.testmaster.dummyapicaller.Service.ApiResponseService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


@Route(value = "save-response", layout = MainView.class)
@PageTitle("Save API Response")
public class SaveView extends VerticalLayout {

    public SaveView(@Autowired ApiResponseService apiResponseService) {
        H4 title = new H4("Save A Response");

        TextField apiNameField = getApiNameField();
        Select<RequestTypes> requestTypesSelector = getRequestTypesSelector();
        //        Select<StatusTypes> statusSelector = getStatusSelector();
        HorizontalLayout layout = getHLayoutAndAddItems(apiNameField, requestTypesSelector);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button saveButton = getSaveButton();
        Button replaceButton = getReplaceButton();
        buttonLayout.add(saveButton);

        TextArea jsonResponseText = getTextArea();
        Label label = new Label("Or upload a .json file instead (Max 10 MB)");
        Upload upload = getJsonFileUpload(jsonResponseText, apiNameField);

        saveButton.addClickListener(e -> {
            try {
                apiResponseService.saveJsonResponse(apiNameField.getValue(),
                        new JSONObject(jsonResponseText.getValue()), requestTypesSelector.getValue());
                getSuccessNotificationWithMessage("Saved!").open();
            } catch (BadRequestException ex) {
                getErrorNotificationWithMessage("File With Name Already Exists!!").open();
                buttonLayout.replace(saveButton, replaceButton);
            } catch (Exception ex) {
                getErrorNotificationWithMessage("Unexpected Error! " + ex.getMessage());
            }
        });
        replaceButton.addClickListener(e -> {
            try {
                apiResponseService.overwriteFileContentByName(apiNameField.getValue(),
                        new JSONObject(jsonResponseText.getValue()), requestTypesSelector.getValue());
                getSuccessNotificationWithMessage("Saved!").open();
                buttonLayout.replace(replaceButton, saveButton);
            } catch (Exception ex) {
                getErrorNotificationWithMessage("Unexpected Error! " + ex.getMessage());
            }
        });

        Button reloadButton = new Button(new Icon(VaadinIcon.REFRESH));
        reloadButton.addClickListener(e -> {
            apiNameField.clear();
            jsonResponseText.clear();
            upload.clearFileList();
        });
        buttonLayout.add(reloadButton);

        add(title, layout, buttonLayout, jsonResponseText, label, upload);
    }

    private static HorizontalLayout getHLayoutAndAddItems(TextField apiNameField, Select<RequestTypes> requestTypesSelector) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.add(apiNameField, requestTypesSelector);
        return layout;
    }

    private TextField getApiNameField() {
        TextField apiNameField = new TextField("API Name:");
        apiNameField.setAutofocus(true);
        apiNameField.setRequired(true);
        apiNameField.setRequiredIndicatorVisible(true);
        return apiNameField;
    }

//    private Select<StatusTypes> getStatusSelector() {
//        Select<StatusTypes> statusSelector = new Select<>();
//        statusSelector.setItems(StatusTypes.values());
//        statusSelector.setLabel("Status");
//        statusSelector.setWidth(20, Unit.EM);
//        return statusSelector;
//    }

    private Select<RequestTypes> getRequestTypesSelector() {
        Select<RequestTypes> requestTypesSelector = new Select<>();
        requestTypesSelector.setItems(RequestTypes.values());
        requestTypesSelector.setLabel("Request Type");
        requestTypesSelector.setRequiredIndicatorVisible(true);
        return requestTypesSelector;
    }

    private Button getSaveButton() {
        Button button = new Button("Save");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        return button;
    }

    private Button getReplaceButton() {
        Button replaceButton = new Button("Save/Replace");
        replaceButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        return replaceButton;
    }

    private TextArea getTextArea() {
        TextArea textArea = new TextArea();
        textArea.setLabel("Enter A Valid Json Response");
        textArea.setValueChangeMode(ValueChangeMode.EAGER);
        textArea.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + " characters"));
        textArea.setWidth("80%");
        textArea.setHeight(30, Unit.EM);
        textArea.setMaxWidth("95%");
        textArea.setMaxHeight(70, Unit.EM);
        textArea.setRequired(true);
        textArea.setRequiredIndicatorVisible(true);
        textArea.getStyle().set("font-family", "'Fira Code', Consolas, monospace").set("font-size", "10pt");
        return textArea;
    }

    private Notification getErrorNotificationWithMessage(String errorMessage) {
        Notification notification = new Notification();
        notification.setDuration(2000);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setText(errorMessage);
        return notification;
    }

    private Notification getSuccessNotificationWithMessage(String successMessage) {
        Notification notification = new Notification();
        notification.setDuration(1500);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.setText(successMessage);
        return notification;
    }

    private Upload getJsonFileUpload(TextArea textArea, TextField textField) {
        MemoryBuffer fileBuffer = new MemoryBuffer();
        Upload upload = new Upload(fileBuffer);
        upload.setDropAllowed(true);
        upload.setMaxWidth(40, Unit.EM);
        upload.setAcceptedFileTypes("application/json", ".json");
        int MEGABYTE = 1024 * 1024;
        upload.setMaxFileSize(10 * MEGABYTE);
        upload.addFileRejectedListener(e -> getErrorNotificationWithMessage(e.getErrorMessage()).open());
        upload.addSucceededListener(e -> {
            final int LENGTH_OF_JSON_EXTENSION = 5;
            try {
                textArea.setValue(processFileContent(fileBuffer.getInputStream()));
                textField.setValue(e.getFileName().substring(0, e.getFileName().length() - LENGTH_OF_JSON_EXTENSION));
            } catch (Exception ex) {
                getErrorNotificationWithMessage(ex.getMessage()).open();
            }
        });
        return upload;
    }

    private String processFileContent(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining(System.lineSeparator()));
    }


}
