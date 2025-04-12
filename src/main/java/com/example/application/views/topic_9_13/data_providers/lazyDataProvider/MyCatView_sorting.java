package com.example.application.views.topic_9_13.data_providers.lazyDataProvider;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Route("2.2-lazy-data-provider-sorting")
@Menu(order = 7, icon = LineAwesomeIconUrl.GAVEL_SOLID)
public class MyCatView_sorting extends Div {

  public record MyCatEntity(String name, int age) { }

  private static class MyCatDatasource {

    private List<MyCatEntity> cats = new ArrayList<>();

    public MyCatDatasource() {

      cats.add(new MyCatEntity("Whiskers", 5));
      cats.add(new MyCatEntity("Luna", 3));
      cats.add(new MyCatEntity("Oliver", 7));
    }

    List<MyCatEntity> getCats() {

      return cats;
    }
  }

  private static class MyCatService {

    private List<MyCatEntity> cats;

    public MyCatService(MyCatDatasource source) { this.cats = source.getCats();}

    // Offset based in the scroll position
    //   - Ex: itens 01-25: offset=00, limit=25
    //   - Ex: itens 26-50: offset=25, limit=25
    public Stream<MyCatEntity> getCats(int offset, int limit, List<QuerySortOrder> sortOrders) {

      // Step 1: Skip if sorting is absent
      if (sortOrders.isEmpty()) return cats.stream().skip(offset).limit(limit);

      // Step 2: Choose 'QuerySortOrder' as ASCENDING'(index 0)
      QuerySortOrder order = sortOrders.get(0);

      // Step 3: Create Comparator based on Property
      Comparator<MyCatEntity> comparator = switch (order.getSorted()) {
        case "name" -> Comparator.comparing(MyCatEntity::name);
        case "age" -> Comparator.comparing(MyCatEntity::age);
        default -> null;
      };

      // Step 4: Define Comparator's Sort Direction
      if (comparator == null) return cats.stream().skip(offset).limit(limit);
      if (order.getDirection() == SortDirection.DESCENDING) comparator = comparator.reversed();

      return cats.stream().sorted(comparator).skip(offset).limit(limit);
    }

    public int countCats() { return cats.size(); }
  }

  public MyCatView_sorting() {

    MyCatDatasource datasource = new MyCatDatasource();
    MyCatService service = new MyCatService(datasource);

    Grid<MyCatEntity> catGrid = createCatGrid();

    // Step 1: Create a lazy data provider + sorting
    CallbackDataProvider<MyCatEntity, Void> dataProvider =
         DataProvider
              .fromCallbacks(

                   // 1° Callback: get Cats with 'offset', 'limit' and 'sortOrder'
                   query -> {     //Query is managed by Vaadin

                     // 1.1) Extract pagination information
                     int offset = query.getOffset();
                     int limit = query.getLimit();

                     // 1.2) Extract sorting information
                     List<QuerySortOrder> sortOrders = query.getSortOrders();

                     // 1.3) Call service method with all parameters
                     return service.getCats(offset, limit, sortOrders);
                   },

                   // 2° Callback: count total items (no sorting needed here)
                   query -> service.countCats()
              );

    // Step 2: Set the dataProvider to the grid
    catGrid.setItems(dataProvider);

    VerticalLayout layout = new VerticalLayout(catGrid);
    layout.setSizeFull();
    add(layout);
    setSizeFull();
  }

  private Grid<MyCatEntity> createCatGrid() {

    Grid<MyCatEntity> catGrid = new Grid<>(MyCatEntity.class, false);
    catGrid.addColumn(MyCatEntity::name).setHeader("Name").setSortable(true).setKey("name");
    catGrid.addColumn(MyCatEntity::age).setHeader("Age").setSortable(true).setKey("age");
    return catGrid;
  }
}