package br.com.rafaelvieira.shopbeer.services.event;

import br.com.rafaelvieira.shopbeer.domain.Beer;
import br.com.rafaelvieira.shopbeer.domain.ItemSale;
import br.com.rafaelvieira.shopbeer.repository.BeerRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SaleListener {

    private final BeerRepository beerRepository;

    public SaleListener(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @EventListener
    public void vendaEmitida(SaleEvent saleEvent) {
        for (ItemSale item : saleEvent.sale().getItens()) {
            Beer beer = beerRepository.getReferenceById(item.getBeer().getCode());
            beer.setQuantityStock(beer.getQuantityStock() - item.getQuantity());
            beerRepository.save(beer);
        }
    }
}
