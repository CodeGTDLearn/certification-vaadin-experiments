package com.example.application.views.topic_9_13.data_providers.lazyDataProvider;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.stream.Stream;

@Route("2.1-lazy-data-provider-filtering")
@Menu(order = 6, icon = LineAwesomeIconUrl.FREEBSD)
public class MyClientView_filtering extends Div {

  public record MyClient(String name, int age) {}

  private static class MyClientRepository {

    public static Stream<MyClient> getClients() {

      return Stream.of(
           new MyClient("João", 28),
           new MyClient("Maria", 34),
           new MyClient("Carlos", 42)
      );
    }
  }

  private static class MyClientService {

    public static Stream<MyClient> getClients(String nameFilter) {

      Stream<MyClient> clients = MyClientRepository.getClients();

      if (nameFilter != null && ! nameFilter.isEmpty()) {
        clients = clients.filter(
             client -> client.name()
                             .toLowerCase()
                             .contains(nameFilter.toLowerCase()));
      }

      return clients;
    }
  }

  public MyClientView_filtering() {

    Grid<MyClient> myClientGrid = new Grid<>();
    myClientGrid.addColumn(MyClient::name).setHeader("Nome");
    myClientGrid.addColumn(MyClient::age).setHeader("Age");
    myClientGrid.setSelectionMode(Grid.SelectionMode.NONE);
    myClientGrid.setDetailsVisibleOnClick(false);

    // Step 1: Create a DataProvider with filtering + lazy loading
    DataProvider<MyClient, String> lazyDataProvider =
         DataProvider
              .fromFilteringCallbacks(
                   query -> {    //Query is managed by Vaadin

                     int offset = query.getOffset();
                     int limit = query.getLimit();

                     String queryNameFilter = query.getFilter().orElse(null);

                     return MyClientService
                               .getClients(queryNameFilter)
                               .skip(offset)
                               .limit(limit);
                   },
                   query -> {
                     String queryNameFilter = query.getFilter().orElse(null);
                     return (int) MyClientService.getClients(queryNameFilter).count(); });

    // Step 2: Wrap the DataProvider with a ConfigurableFilterDataProvider
    ConfigurableFilterDataProvider<MyClient, Void, String> wrappedDataProvider =
         lazyDataProvider.withConfigurableFilter();

    // Step 3: Create a TextField for filtering
    TextField filterField = new TextField("Filter by Name");
    filterField.setPlaceholder("Type to filter...");
    filterField
         .addValueChangeListener(
              valueChanged -> {
                String filterValue = valueChanged.getValue();
                wrappedDataProvider.setFilter(filterValue.isEmpty() ? null : filterValue); });

    // Step 4: Set the 'wrappedDataProvider' in the Grid
    myClientGrid.setDataProvider(wrappedDataProvider);

    add(filterField, myClientGrid);
  }
}