package com.testmaster.dummyapicaller.View;

import com.testmaster.dummyapicaller.Dao.ApiResponseDao;
import com.testmaster.dummyapicaller.MainView;
import com.testmaster.dummyapicaller.Service.ApiResponseService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Route(value = "list-of-saved-responses", layout = MainView.class)
@PageTitle("Show Responses")
public class ShowDataView extends VerticalLayout {

    public ShowDataView(@Autowired ApiResponseService apiResponseService) {
        H4 title = new H4("Saved Responses");
        TextArea textArea = getTextArea();

        List<ApiResponseDao> responses = apiResponseService.getAllSavedResponsesAsDaoList();
        Grid<ApiResponseDao> grid = getResponseDaoGrid(apiResponseService, responses);
        grid.addComponentColumn(dao -> getViewButton(apiResponseService, dao, textArea))
                .setHeader("View").setAutoWidth(true);
        grid.setItems(responses);

        Button deleteAllButton = getDeleteAllButton(apiResponseService, grid);

        add(title, deleteAllButton, grid, textArea);
    }

    private Button getViewButton(ApiResponseService apiResponseService, ApiResponseDao dao, TextArea textArea) {
        Button viewButton = new Button();
        viewButton.setIcon(new Icon(VaadinIcon.DESKTOP));
        viewButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        viewButton.addClickListener(e -> textArea.setValue(new JSONObject(apiResponseService
                .getApiResponseByName(dao.getApiName(), dao.getRestMethod())).toString(2)));
        return viewButton;
    }

    private Grid<ApiResponseDao> getResponseDaoGrid(ApiResponseService apiResponseService,
                                                   List<ApiResponseDao> responses) {

        Grid<ApiResponseDao> grid = new Grid<>(ApiResponseDao.class, false);
        grid.setWidth("80%");
        grid.setMaxWidth("95%");
        grid.setMaxHeight(80, Unit.EM);
        grid.addColumn(ApiResponseDao::getApiName).setHeader("API Name")
                .setFooter("Number of response(s): " + responses.size()).setAutoWidth(true).setKey("CounterCol");
        grid.addColumn(ApiResponseDao::getApiUrl).setHeader("API URL").setAutoWidth(true);
        grid.addColumn(ApiResponseDao::getRestMethod).setHeader("REST Call").setAutoWidth(true);
        grid.addComponentColumn(dao -> getDeleteButton(apiResponseService, dao, grid)).setHeader("Manage").setAutoWidth(true);
        return grid;
    }

    private Button getDeleteAllButton(ApiResponseService apiResponseService, Grid<ApiResponseDao> grid) {
        ConfirmDialog dialog = getDialogBox();
        dialog.addConfirmListener(e -> {
            apiResponseService.deleteAllResponses();
            Notification.show("Deleted All Responses!", 2000, Notification.Position.TOP_CENTER);
            grid.setItems(apiResponseService.getAllSavedResponsesAsDaoList());
            grid.getColumnByKey("CounterCol").setFooter("Number of response(s): " + 0);
        });

        Button deleteAllButton = new Button("Delete All", e -> dialog.open());
        deleteAllButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        return deleteAllButton;
    }

    private Button getDeleteButton(ApiResponseService apiResponseService, ApiResponseDao dao, Grid<ApiResponseDao> grid) {
        Button deleteButton = new Button();
        deleteButton.setIcon(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        deleteButton.addClickListener(e -> {
            apiResponseService.deleteFileByNameAndRequestType(dao.getApiName(), dao.getRestMethod());
            List<ApiResponseDao> newResponseList = apiResponseService.getAllSavedResponsesAsDaoList();
            grid.setItems(newResponseList);
            grid.getColumnByKey("CounterCol").setFooter("Number of response(s): " + newResponseList.size());
            Notification.show("Deleted!", 2500, Notification.Position.TOP_CENTER);
        });
        return deleteButton;
    }

    private TextArea getTextArea() {
        TextArea textArea = new TextArea();
        textArea.setLabel("Json Response");
        textArea.setReadOnly(true);
        textArea.setWidth("80%");
        textArea.setHeight(30, Unit.EM);
        textArea.setMaxWidth("95%");
        textArea.setMaxHeight(70, Unit.EM);
        textArea.getStyle().set("font-family", "'Fira Code', Consolas, monospace").set("font-size", "10pt");
        textArea.addThemeVariants(TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL,TextAreaVariant.LUMO_HELPER_ABOVE_FIELD);
        return textArea;
    }

    private ConfirmDialog getDialogBox() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Delete All Records ?");
        dialog.setText("This will delete all records permanently. Are you sure you want to delete all?");

        dialog.setCancelable(true);
        dialog.setCancelText("Cancel");

        dialog.setConfirmText("Delete All");
        dialog.setConfirmButtonTheme("error primary");
        return dialog;
    }

}
