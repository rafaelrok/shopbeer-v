package br.com.rafaelvieira.shopbeer.session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.rafaelvieira.shopbeer.domain.Beer;
import br.com.rafaelvieira.shopbeer.domain.ItemSale;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@SessionScope
@Component
public class TablesItemsSession {
    private final Set<TableItemsSale> tables = new HashSet<>();

    public void addItem(String uuid, Beer beer, int quantity) {
        TableItemsSale table = searchTableByUuid(uuid);
        table.addIten(beer, quantity);
        tables.add(table);
    }

    public void changeQuantityItems(String uuid, Beer beer, Integer quantity) {
        TableItemsSale table = searchTableByUuid(uuid);
        table.changeQuantityItems(beer, quantity);
    }

    public void deleteItem(String uuid, Beer beer) {
        TableItemsSale table = searchTableByUuid(uuid);
        table.deleteItem(beer);
    }

    public List<ItemSale> getItens(String uuid) {
        return searchTableByUuid(uuid).getItens();
    }

    public Object getAmount(String uuid) {
        return searchTableByUuid(uuid).getAmount();
    }

    private TableItemsSale searchTableByUuid(String uuid) {
        TableItemsSale table = tables.stream()
                .filter(t -> t.getUuid().equals(uuid))
                .findAny()
                .orElse(new TableItemsSale(uuid));
        return table;
    }
    
}
