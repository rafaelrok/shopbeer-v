package br.com.rafaelvieira.shopbeer.session;

import br.com.rafaelvieira.shopbeer.domain.Beer;
import br.com.rafaelvieira.shopbeer.domain.ItemSale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class TableItemsSale {

    private String uuid;
    private List<ItemSale> itens = new ArrayList<>();

    public TableItemsSale(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getAmount() {
        return itens.stream()
                .map(ItemSale::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public void addIten(Beer beer, Integer quantity) {
        Optional<ItemSale> itemSaleOptional = searchItemByBeer(beer);

        ItemSale itemSale = null;
        if (itemSaleOptional.isPresent()) {
            itemSale = itemSaleOptional.get();
            itemSale.setQuantity(itemSale.getQuantity() + quantity);
        } else {
            itemSale = new ItemSale();
            itemSale.setBeer(beer);
            itemSale.setQuantity(quantity);
            itemSale.setUnitaryValue(beer.getValue());
            itens.add(0, itemSale);
        }
    }

    public void changeQuantityItems(Beer beer, Integer quantity) {
        ItemSale itemSale = searchItemByBeer(beer).get();
        itemSale.setQuantity(quantity);
    }

    public void deleteItem(Beer beer) {
        int index = IntStream.range(0, itens.size())
                .filter(i -> itens.get(i).getBeer().equals(beer))
                .findAny().getAsInt();
        itens.remove(index);
    }

    public int total() {
        return itens.size();
    }

    public List<ItemSale> getItens() {
        return itens;
    }

    private Optional<ItemSale> searchItemByBeer(Beer beer) {
        return itens.stream()
                .filter(i -> i.getBeer().equals(beer))
                .findAny();
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TableItemsSale other = (TableItemsSale) obj;
        if (uuid == null) {
            return other.uuid == null;
        } else return uuid.equals(other.uuid);
    }

}