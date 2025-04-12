package com.example.application.views.topic_9_13;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("0 Hello World")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.FILE_SIGNATURE_SOLID)
public class BasicView extends HorizontalLayout {

  private TextField name;
  private Button sayHello;

  public BasicView() {

    name = new TextField("Your name");

    sayHello = new Button("Say hello");

    sayHello.addClickListener(
         click -> {
           Notification.show("Hello " + name.getValue());
         });

    sayHello.addClickShortcut(Key.ENTER);

    setMargin(true);
    setVerticalComponentAlignment(Alignment.END, name, sayHello);

    add(name, sayHello);
  }

}