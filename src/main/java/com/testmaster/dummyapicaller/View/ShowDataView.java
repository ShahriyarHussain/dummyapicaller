package com.testmaster.dummyapicaller.View;

import com.testmaster.dummyapicaller.Dao.ApiResponseDao;
import com.testmaster.dummyapicaller.MainView;
import com.testmaster.dummyapicaller.Service.ApiResponseService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
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

        List<ApiResponseDao> responses = apiResponseService.getSavedResponsesAsDao();
        Grid<ApiResponseDao> grid = getResponseDaoGrid(apiResponseService, responses);
        grid.addComponentColumn(dao -> {
            Button viewButton = new Button();
            viewButton.setIcon(new Icon(VaadinIcon.DESKTOP));
            viewButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            viewButton.addClickListener(e -> {
                textArea.setValue(new JSONObject(apiResponseService.getApiResponseByName(dao.getApiName())).toString(2));
            });
            return viewButton;
        }).setHeader("View").setAutoWidth(true);
        grid.setItems(responses);

        Button button = new Button("Delete All", e -> {
            apiResponseService.deleteAll();
            Notification.show("Deleted All Responses!", 2500, Notification.Position.TOP_CENTER);
            grid.setItems(apiResponseService.getSavedResponsesAsDao());
            grid.getColumnByKey("CounterCol").setFooter("Number of response(s): " + 0);
        });
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        add(title, button, grid, textArea);
    }

    public Grid<ApiResponseDao> getResponseDaoGrid(ApiResponseService apiResponseService, List<ApiResponseDao> responses) {
        Grid<ApiResponseDao> grid = new Grid<>(ApiResponseDao.class, false);
        grid.setWidth("70%");
        grid.setMaxWidth("90%");
        grid.setMaxHeight(80, Unit.EM);
        grid.addColumn(ApiResponseDao::getApiName).setHeader("API Name")
                .setFooter("Number of response(s): " + responses.size()).setAutoWidth(true).setKey("CounterCol");
        grid.addColumn(ApiResponseDao::getApiUrl).setHeader("API URL").setAutoWidth(true);
        grid.addColumn(ApiResponseDao::getRestMethod).setHeader("REST Call").setAutoWidth(true);
        grid.addComponentColumn(dao -> {
            Button deleteButton = new Button();
            deleteButton.setIcon(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
            deleteButton.addClickListener(e -> {
                apiResponseService.deleteFileByName(dao.getApiName());
                List<ApiResponseDao> newResponseList = apiResponseService.getSavedResponsesAsDao();
                grid.setItems(newResponseList);
                grid.getColumnByKey("CounterCol").setFooter("Number of response(s): " + newResponseList.size());
                Notification.show("Deleted!", 2500, Notification.Position.TOP_CENTER);
            });
            return deleteButton;
        }).setHeader("Manage").setAutoWidth(true);
        return grid;
    }

    public TextArea getTextArea() {
        TextArea textArea = new TextArea();
        textArea.setLabel("Json Response");
        textArea.setReadOnly(true);
        textArea.setWidth("70%");
        textArea.setHeight(30, Unit.EM);
        textArea.setMaxWidth("90%");
        textArea.setMaxHeight(70, Unit.EM);
        textArea.getStyle().set("font-family", "monospace").set("font-size", "9pt");
        return textArea;
    }

}
