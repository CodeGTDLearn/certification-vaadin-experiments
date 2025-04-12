package com.example.application.views.topic_9_13.grid_item_details;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@Route("3-grid-details-programmatic")
@Menu(order = 3, icon = LineAwesomeIconUrl.GHOST_SOLID)
public class ProgrammaticGridDetailsExample extends VerticalLayout {

  public record MyClient(String name, int age) {
  }

  public ProgrammaticGridDetailsExample() {

    // Example data
    MyClient[] clients = {new MyClient("João", 28), new MyClient("Maria", 34)};

    Grid<MyClient> clientGrid = new Grid<>();
    clientGrid.addColumn(MyClient::name).setHeader("Name");
    clientGrid.setItems(clients);

    // Details renderer
    clientGrid.setItemDetailsRenderer(
         new ComponentRenderer<>(
              myClient ->
                   new Div(new Div(
                        myClient.name() + ", " + myClient.age() + " anos"))
         ));

    clientGrid.setSelectionMode(Grid.SelectionMode.NONE);
    clientGrid.setDetailsVisibleOnClick(false);

    // External button for programmatic control
    Button toggleButton = new Button(
         "ALternate First Item Details",
         buttonClickEvent -> {
           MyClient firstMyClient = clients[0];
           clientGrid
                .setDetailsVisible(
                     firstMyClient,
                     ! clientGrid.isDetailsVisible(firstMyClient)
                );
         }
    );

    add(clientGrid, toggleButton);
  }
}