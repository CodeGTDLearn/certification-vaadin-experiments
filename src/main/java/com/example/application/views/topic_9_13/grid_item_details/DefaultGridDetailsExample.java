package com.example.application.views.topic_9_13.grid_item_details;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@Route("2-grid-details-default")
@Menu(order = 2, icon = LineAwesomeIconUrl.FREEBSD)
public class DefaultGridDetailsExample extends Div {

  public record MyClient(String name, int age) {
  }

  public DefaultGridDetailsExample() {

    Grid<MyClient> clientGrid = new Grid<>();
    clientGrid.addColumn(MyClient::name).setHeader("Nome");

    // Example data
    clientGrid.setItems(new MyClient("João", 28), new MyClient("Maria", 34));

    // Details renderer
    clientGrid.setItemDetailsRenderer(
         new ComponentRenderer<>(
              myClient ->
                   new Div(new Div(
                        myClient.name() + ", " + myClient.age() + " anos"))
         ));

    clientGrid.setSelectionMode(Grid.SelectionMode.NONE);
    clientGrid.setDetailsVisibleOnClick(false);

    // Button in the row to open/close details
    clientGrid.addColumn(
         new NativeButtonRenderer<>(
              "Detalhes",
              item -> clientGrid.setDetailsVisible(item,
                                                   ! clientGrid.isDetailsVisible(
                                                        item)
              )
         ));

    add(clientGrid);
  }
}