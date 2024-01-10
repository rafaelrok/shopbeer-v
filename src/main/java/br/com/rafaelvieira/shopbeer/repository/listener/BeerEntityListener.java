package br.com.rafaelvieira.shopbeer.repository.listener;

import br.com.rafaelvieira.shopbeer.Application;
import br.com.rafaelvieira.shopbeer.domain.Beer;
import br.com.rafaelvieira.shopbeer.storage.PhotoStorage;
import jakarta.persistence.PostLoad;

public class BeerEntityListener {

    @PostLoad
    public void postLoad(final Beer beer) {
        PhotoStorage photoStorage = Application.getBean(PhotoStorage.class);

        beer.setUrlPhoto(photoStorage.getUrl(beer.getPhotoOrMock()));
        beer.setUrlThumbnailPhoto(photoStorage.getUrl(PhotoStorage.THUMBNAIL_PREFIX + beer.getPhotoOrMock()));
    }
}
